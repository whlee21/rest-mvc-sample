./rest-mvc-core/stop.sh
rm -rf rest-mvc-web/public
mvn clean prepare-package package
[ $? -eq 0 ] && ./rest-mvc-core/start.sh
