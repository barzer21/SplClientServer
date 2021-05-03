
#include "connectionHandler.h"
#include <iostream>
#include <vector>

#ifndef BOOST_ECHO_CLIENT_CLIENTENCODERDECODER_H
#define BOOST_ECHO_CLIENT_CLIENTENCODERDECODER_H

#endif //BOOST_ECHO_CLIENT_CLIENTENCODERDECODER_H

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
using std::vector;

class ClientEncoderDecoder {

private:
    ConnectionHandler &handler;
    bool login;
    void encodeRegister(string command);
    void encodeLogin(string command);
    void encodeLogout(string command);
    void encodeFollow(string command);
    void encodePost(string command);
    void encodePM(string command);
    void encodeUserlist(string command);
    void encodeStat(string command);


    void sendOpCode(short opCode);
    short bytesToShort(char* bytesArr);



public:

    bool getLogin();

    ClientEncoderDecoder(ConnectionHandler &handler);

    void Encode(string command);

    bool Decode();




};