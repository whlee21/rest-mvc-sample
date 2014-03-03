./rest-mvc-core/stop.sh
rm -rf rest-mvc-ember/public
mvn clean prepare-package package
[ $? -eq 0 ] && ./rest-mvc-core/start.sh
