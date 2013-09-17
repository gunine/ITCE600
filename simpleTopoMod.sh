#!/bin/sh
MY_PATH=/root/itce600/
java -classpath "$MY_PATH/lib/*:$MY_PATH/target/bin" kr.ac.postech.itce.SimpleTopoMod $1 $2
