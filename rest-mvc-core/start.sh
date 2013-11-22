#!/bin/sh

BASEDIR=$(dirname $0)
cd $BASEDIR

#SOLRHOME=$PWD/conf/solr
#GAIA_DATA_HOME=$PWD/data/solr

CLASSPATH=./conf:$(find "$PWD/target/rest-mvc-core-0.1-dist" -name '*.jar' |xargs echo  |tr ' ' ':')

nohup java -cp $CLASSPATH -Duser.language=ko -Duser.country=KR -Dhome.dir=$PWD/target/rest-mvc-core-0.1-dist/rest-mvc-core-0.1  -Dlog4j.configuration=file:"./conf/log4j.properties" rest.mvc.core.controller.HelloServer > $PWD/rest-mvc.log 2>&1 &

tail -f $PWD/rest-mvc.log
