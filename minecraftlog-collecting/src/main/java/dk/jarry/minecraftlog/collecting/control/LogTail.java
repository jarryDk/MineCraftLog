package dk.jarry.minecraftlog.collecting.control;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogTail {

	private final static Logger logger = Logger.getLogger(LogTail.class.getCanonicalName());
	
	private AtomicLong lastPointer = new AtomicLong(0L);

	private String filePath;
	private String fileName;
	
	public LogTail(String filePath,String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;		
		logger.info("File : " + Paths.get(filePath, fileName).toAbsolutePath());
	}
		
	public void tail(TailProcessing tailProcessing)
			throws IOException, InterruptedException {
		List<TailProcessing> tailProcessings = new ArrayList<>();
		tailProcessings.add(tailProcessing);
		tail(tailProcessings);
	}

	public void tail(List<TailProcessing> tailProcessings) throws IOException, InterruptedException {

		logger.info("Start tailing : " + ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
		
		WatchService watchService = FileSystems.getDefault().newWatchService();

		Paths.get(filePath).register(watchService, //
				StandardWatchEventKinds.ENTRY_CREATE, //
				StandardWatchEventKinds.ENTRY_MODIFY, //
				StandardWatchEventKinds.ENTRY_DELETE);
		
		do {
			WatchKey key = watchService.take();
			List<WatchEvent<?>> watchEvents = key.pollEvents();

			/**
			 * Handle situation where the file we are talling have been replaced with
			 * a new file with same name.
			 */
			watchEvents.stream() //
				.filter(i -> //
					StandardWatchEventKinds.ENTRY_DELETE == i.kind() //
					&& fileName.equals(i.context().toString())
			).forEach(i -> {
				logger.info(filePath + " have beed deleted/rolled - reset lastPointer");
				lastPointer = new AtomicLong(0L);
			});	
		
			watchEvents.stream() //
				.filter(i -> //
					StandardWatchEventKinds.ENTRY_MODIFY == i.kind() //
					&& fileName.equals(i.context().toString()
				)
			).forEach(i -> {

				File contentFile = Paths.get(filePath + "/" + i.context()).toFile();
				StringBuilder str = new StringBuilder();

				/**
				 * Read the file from last pointer *
				 */
				lastPointer.set(getFileContent(contentFile, lastPointer.get(), str));

				if (str.length() != 0) {
					if(str.indexOf("\n") != -1 ) {
						Stream.of(str.toString().split("\n"))
      						.map (elem -> new String(elem))
      						.collect(Collectors.toList())
							.forEach( 
								line -> {
									processLogLine(tailProcessings, line);
								});
					} else {
						processLogLine(tailProcessings, str.toString());						
					}	
				}

			});
			key.reset();
		} while (true);
	}

	private void processLogLine(List<TailProcessing> tailProcessings, String logLine){
		tailProcessings.stream() //
			.parallel() //
			.forEach(p -> p.process(filePath, fileName, logLine));
	}

	/**
	 * BeginPointer < 0 will re-read
	 * 
	 * @param contentFile
	 * @param beginPointer
	 * @Param str content will be spliced ​​into
	 * @Return the number of bytes read, -1 read failure
	 */
	private static long getFileContent(File contentFile, long beginPointer, StringBuilder str) {

		if (beginPointer < 0) {
			beginPointer = 0;
		}

		RandomAccessFile file = null;
		boolean top = true;
		try {
			file = new RandomAccessFile(contentFile, "r");
			file.seek(beginPointer);
			String line;
			while ((line = file.readLine()) != null) {
				if (top) {
					top = false;
				} else {
					str.append("\n");
				}
				str.append(new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
			}
			return file.getFilePointer();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
