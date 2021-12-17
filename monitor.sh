#!/bin/bash
#CPU使用率
CPU=`top -b -n 1|grep java|awk '{print $9}'`
#内存使用率
MEM=`top -b -n 1|grep java|awk '{print $10}'`
#CPU告警
if [ ${CPU%.*} -gt 20 ];then
      echo "告警！java进程的CPU使用率已超过20%，此时占用率:"${CPU}"%，" >> /root/notepad/dingding_robot/log.txt
      java -jar robot.jar
fi
#内存告警
if [ ${MEM%.*} -gt 2 ];then
      echo "告警！java进程的内存使用率已超过2%，此时占用率:"${MEM}"%，" >> /root/notepad/dingding_robot/log.txt
      java -jar robot.jar
fi
