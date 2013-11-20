#!/bin/bash

SERVER_PID=`jps | grep HelloServer | tr -d 'HelloServer'`
[[ -n $SERVER_PID ]] && kill $SERVER_PID
