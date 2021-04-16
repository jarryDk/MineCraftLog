package dk.jarry.minecraftlog.collecting.control;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import dk.jarry.minecraftlog.entity.MineCraftLog;

public class KafkaTailProcessing implements TailProcessing {

	private final static Logger logger = Logger.getLogger(KafkaTailProcessing.class.getCanonicalName());

	private String topicName;

	private final KafkaProducer<String, MineCraftLog> producer;
	private final Boolean isAsync;

	public KafkaTailProcessing(String topicName, Boolean isAsync) {

		this.topicName = topicName;
		this.isAsync = isAsync;

		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("client.id", "KafkaFileProducer");

		producer = new KafkaProducer<String, MineCraftLog>(props, //
				new StringSerializer(), //
				new JsonPOJOSerializer<>());

	}

	@Override
	public void process(String line) {
		logger.info("Log line : " + line);
	}

	@Override
	public void process(String filePath, String fileName, String line) {
		Path path = Paths.get(filePath, fileName);
		logger.fine("Modified file : " + path.toString());

		boolean hasNewLine = line.contains("\n");

		logger.fine("Log line (hasNewLine:" + hasNewLine + ") : " + line);

		sendMessage(UUID.randomUUID().toString(), new MineCraftLog(line));
	}

	private void sendMessage(String key, MineCraftLog mineCraftLog) {
		long startTime = System.currentTimeMillis();
		if (isAsync) { // Send asynchronously
			producer.send( //
					new ProducerRecord<String, MineCraftLog>(topicName, key, mineCraftLog),
					(Callback) new MineCraftCallBack(startTime, key, mineCraftLog));
		} else { // Send synchronously
			try {
				RecordMetadata recordMetadata = producer.send( //
						new ProducerRecord<String, MineCraftLog>(topicName, key, mineCraftLog) //
				).get();
				logger.info("message(" + key + ", " + mineCraftLog + ") sent to partition(" + recordMetadata.partition()
						+ "), " + "offset(" + recordMetadata.offset() + ") - synchronously");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

}

class MineCraftCallBack implements Callback {

	private final static Logger logger = Logger.getLogger(MineCraftCallBack.class.getCanonicalName());

	private long startTime;
	private String key;
	private MineCraftLog mineCraftLog;

	public MineCraftCallBack(long startTime, String key, MineCraftLog mineCraftLog) {
		this.startTime = startTime;
		this.key = key;
		this.mineCraftLog = mineCraftLog;
	}

	/**
	 * A callback method the user can implement to provide asynchronous handling of
	 * request completion. This method will be called when the record sent to the
	 * server has been acknowledged. Exactly one of the arguments will be non-null.
	 *
	 * @param metadata  The metadata for the record that was sent (i.e. the
	 *                  partition and offset). Null if an error occurred.
	 * @param exception The exception thrown during processing of this record. Null
	 *                  if no error occurred.
	 */
	public void onCompletion(RecordMetadata metadata, Exception exception) {
		long elapsedTime = System.currentTimeMillis() - startTime;
		if (metadata != null) {
			logger.info("message(" + key + ", " + mineCraftLog + ") sent to partition(" + metadata.partition() + "), "
					+ "offset(" + metadata.offset() + ") in " + elapsedTime + " ms - asynchronously");
		} else {
			exception.printStackTrace();
		}
	}
}