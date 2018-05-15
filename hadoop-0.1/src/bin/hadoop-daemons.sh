#!/bin/bash
# 
# Run a Hadoop command on all slave hosts.

usage="Usage: hadoop-daemons.sh [start|stop] command args..."
#script 执行必须带参数，如果没带参数，那么就打印错误提示，也就是该脚本的用法并退出

# if no args specified, show usage
if [ $# -le 1 ]; then
  echo $usage
  exit 1
fi

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

exec "$bin/slaves.sh" "$bin/hadoop-daemon.sh" "$@"
