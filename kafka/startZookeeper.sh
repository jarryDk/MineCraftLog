#!/bin/bash

export KAFKA_HOME="/opt/apache/kafka/kafka_2.13-2.7.0"

$KAFKA_HOME/bin/zookeeper-server-start.sh zookeeper.properties