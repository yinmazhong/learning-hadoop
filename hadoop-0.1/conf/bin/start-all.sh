#!/bin/bash

# Start all hadoop daemons.  Run this on master node.

# $0 当前脚本的文件名
# 找到执行该脚本的目录到脚本真正存放目录的相对地址,然后　cd 进去,pwd 找到文件的绝对目录

bin=`dirname "$0"`
echo $bin

bin=`cd "$bin"; pwd`

echo $bin
# start dfs daemons
# start namenode after datanodes, to minimize time namenode is up w/o data
# note: datanodes will log connection errors until namenode starts
"$bin"/hadoop-daemons.sh start datanode
"$bin"/hadoop-daemon.sh start namenode

# start mapred daemons
# start jobtracker first to minimize connection errors at startup
"$bin"/hadoop-daemon.sh start jobtracker
"$bin"/hadoop-daemons.sh start tasktracker
