#!/bin/bash
if [ $# -lt 1 ]
then
    echo "No Args Input..."
    exit ;
fi
case $1 in
"start")
    echo "-------- start hadoop--------"

    echo "-------- start hdfs--------"
    ssh hadoop102 "/opt/software/hadoop/sbin/start-dfs.sh"

    echo "-------- start yarn--------"
    ssh hadoop103 "/opt/software/hadoop/sbin/start-yarn.sh"
;;
"stop")
    echo "-------- stop hadoop--------"

    echo "-------- stop yarn--------"
    ssh hadoop103 "/opt/software/hadoop/sbin/stop-yarn.sh"

    echo "-------- stop hdfs--------"
    ssh hadoop102 "/opt/software/hadoop/sbin/stop-dfs.sh"
;;
*)
    echo "Input Args Error..."
;;
esac
