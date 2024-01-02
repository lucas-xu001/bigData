#!/bin/bash

for i in hadoop102 hadoop103
do
	echo "========== $i =========="
        ssh $i "cd /opt/software/ad_mock ; java -jar /opt/software/ad_mock/NginxDataGenerator-1.0-SNAPSHOT-jar-with-dependencies.jar >/dev/null  2>&1 &"
done
