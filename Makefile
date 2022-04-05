target:
	javac ./src/*.java -d ./class -classpath ./class
run:
	java -cp .:connector.jar ./class main
clean:
	rm -rf ./class/*.class