//
// Created by ozerye@wincs.cs.bgu.ac.il on 12/31/18.
//

#include <KeyboardReader.h>

KeyboardReader::KeyboardReader(ClientEncoderDecoder *encoderDecoder):encoder(encoderDecoder){}


void KeyboardReader::run() {
    bool logOut = false;

    while(!logOut){ // if logout exit the loop
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize); // getting the command from the keyboard
        std::string line(buf);

        encoder->Encode(line);

        string opWord = line.substr(0, line.find_first_of(' ')); // check if logout
        if(opWord == "LOGOUT"&& encoder->getLogin()) // check also if logged-in
            logOut = true;
    }

}