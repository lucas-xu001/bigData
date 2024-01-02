#!/bin/bash

case $1 in
"start")
        echo " --------start hadoop104 flume-------"
        ssh hadoop104 "nohup /opt/software/flume/bin/flume-ng agent -n a1 -c /opt/software/flume/conf -f /opt/software/flume/job/ad_kafka_to_hdfs.conf >/dev/null 2>&1 &"
;;
"stop")

        echo " --------stop hadoop104 flume-------"
        ssh hadoop104 "ps -ef | grep ad_kafka_to_hdfs | grep -v grep |awk '{print \$2}' | xargs -n1 kill"
;;
esac

