//
// Created by ozerye@wincs.cs.bgu.ac.il on 12/31/18.
//

#ifndef BOOST_ECHO_CLIENT_KEYBOARDREADER_H
#define BOOST_ECHO_CLIENT_KEYBOARDREADER_H


#include <ClientEncoderDecoder.h>


class KeyboardReader {
private:
    ClientEncoderDecoder *encoder;

public:
    KeyboardReader(ClientEncoderDecoder *encoderDecoder);
    void run();
};


#endif //BOOST_ECHO_CLIENT_KEYBOARDREADER_H
