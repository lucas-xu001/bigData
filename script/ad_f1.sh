#!/bin/bash

case $1 in
"start"){
        for i in hadoop102 hadoop103
        do
                echo " --------start $i flume-------"
                ssh $i "nohup /opt/software/flume/bin/flume-ng agent -n a1 -c /opt/software/flume/conf/ -f /opt/software/flume/job/ad_file_to_kafka.conf >/dev/null 2>&1 &"
        done
};; 
"stop"){
        for i in hadoop102 hadoop103
        do
                echo " --------stop $i flume-------"
                ssh $i "ps -ef | grep ad_file_to_kafka | grep -v grep |awk  '{print \$2}' | xargs -n1 kill -9 "
        done

};;
esac

