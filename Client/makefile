
CC = g++
CFLAGS  = -g -Wall -Weffc++ -std=c++11 -Iinclude
LFLAGS  = -lboost_system -lpthread

# All Targets
all: BGSclient

# Tool invocations
# Executable "BGSclient" depends on the files BGSclient and run.o.
BGSclient: bin/ClientEncoderDecoder.o bin/connectionHandler.o bin/KeyboardReader.o bin/echoClient.o
	@echo 'Building target: BGSclient'
	@echo 'Invoking: C++ Linker'
	$(CC) -o bin/BGSclient  bin/ClientEncoderDecoder.o bin/connectionHandler.o bin/KeyboardReader.o bin/echoClient.o $(LFLAGS)
	@echo 'Finished building target: BGSclient'
	@echo ' '

bin/echoClient.o: src/echoClient.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/echoClient.o src/echoClient.cpp

bin/ClientEncoderDecoder.o: src/ClientEncoderDecoder.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/ClientEncoderDecoder.o src/ClientEncoderDecoder.cpp

bin/KeyboardReader.o: src/KeyboardReader.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/KeyboardReader.o src/KeyboardReader.cpp

bin/connectionHandler.o: src/connectionHandler.cpp
	$(CC) $(CFLAGS) -c -Iinclude -o bin/connectionHandler.o src/connectionHandler.cpp
#Clean the build directory
clean: 
	rm -f bin/*
