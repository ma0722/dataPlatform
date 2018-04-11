#!/bin/sh
mvn package
#~/Desktop/spark-2.0.0-bin-hadoop2.7/bin/spark-submit --master spark://10.109.247.120:7077 target/data-platform-1.0-SNAPSHOT-jar-with-dependencies.jar
#~/Desktop/spark-2.0.0-bin-hadoop2.7/bin/spark-submit target/data-platform-1.0-SNAPSHOT-jar-with-dependencies.jar
~/Desktop/spark-2.0.0-bin-hadoop2.7/bin/spark-submit  target/data-platform-1.0-SNAPSHOT-jar-with-dependencies.jar
