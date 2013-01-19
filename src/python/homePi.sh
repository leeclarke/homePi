#!/bin/sh

runlog=/home/pi/homepi/log/running.log

#make not that the schelscript ran
NOW=$(date +"%F %T")
echo "[$NOW] Checking homePi run" >>  $runlog

python /home/pi/homepi/homePi.py &

