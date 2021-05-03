#include <stdlib.h>
#include <connectionHandler.h>
#include <KeyboardReader.h>
#include <thread>

using namespace std;
/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    ClientEncoderDecoder *clientEncoderDecoder= new ClientEncoderDecoder(connectionHandler); // creating the encdec in the heap
    KeyboardReader reader(clientEncoderDecoder);

    std::thread keyboardThread(&KeyboardReader::run, &reader); // starting the reader thread

    bool logOut = false;
    while (!logOut) {
        logOut = clientEncoderDecoder->Decode(); // returns true if the decoder got a logout ACK
    }

    keyboardThread.join(); // waits for the keyboard thread to finish

    delete(clientEncoderDecoder);

    return 0;
}
