target:
	javac ./src/*.java -d ./class -classpath ./class
run:
	java -cp ./class main
clean:
	rm -rf ./class/*.class