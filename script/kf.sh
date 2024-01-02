#!/bin/bash

case $1 in
"start") {
    for i in hadoop102 hadoop103 hadoop104
    do
        echo "-------- start $i kafka--------"
        ssh $i "/opt/software/kafka/bin/kafka-server-start.sh -daemon /opt/software/kafka/config/server.properties"
    done
};;
"stop") {
    for i in hadoop102 hadoop103 hadoop104
    do
        echo "-------- start $i kafka--------"
        ssh $i "/opt/software/kafka/bin/kafka-server-stop.sh"
    done
};;
esac