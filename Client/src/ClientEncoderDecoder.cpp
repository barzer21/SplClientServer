#include <ClientEncoderDecoder.h>
#include <boost/algorithm/string.hpp>
#include <bits/stdc++.h>
using namespace std;



ClientEncoderDecoder::ClientEncoderDecoder(ConnectionHandler &connectionHandler) : handler(connectionHandler), login(
        false){

}

bool ClientEncoderDecoder::getLogin(){
    return login;
}

void ClientEncoderDecoder::Encode(string command) {
    unsigned long spaceIndex = command.find_first_of(' '); // finds the first word of the command
    string opWord = command.substr(0, spaceIndex);
    command = command.substr(spaceIndex + 1, command.length());


    if (opWord=="REGISTER")
        encodeRegister(command);
    if (opWord.compare("LOGIN")==0)
        encodeLogin(command);
    if (opWord.compare("LOGOUT")==0)
        encodeLogout(command);
    if (opWord.compare("FOLLOW")==0)
        encodeFollow(command);
    if (opWord.compare("POST")==0)
        encodePost(command);
    if (opWord.compare("PM")==0)
        encodePM(command);
    if (opWord.compare("USERLIST")==0)
        encodeUserlist(command);
    if (opWord.compare("STAT")==0)
        encodeStat(command);

}

//-------------------------------------decode------------------------------------
bool ClientEncoderDecoder::Decode() {
    string ans= "";
    char bytes[2];
    handler.getBytes(bytes, 2);
    short opCode = bytesToShort(bytes); // getting the opCode

    if (opCode == 10) { // ACK opCode
        handler.getBytes(bytes, 2);
        short opCode2 = bytesToShort(bytes);

        ans = "ACK " + std::to_string(opCode2) +" ";

        switch (opCode2) { // specific ACK
            case 2:{ // login
                login=true;
                break;
            }
            case 3: { // logout
                cout << ans << endl;
                return true;
            }
            case 4 : { // follow
                handler.getBytes(bytes, 2);
                short numOfUsers = bytesToShort(bytes); // num of users to follow
                ans = ans + std::to_string(numOfUsers);

                for (int i = 0; i < numOfUsers; i++) {// add the users to the ACK
                    string name="";
                    handler.getLine(name);
                    ans += " " + name;
                }
                break;
            }
            case 7:{ // userlist
                handler.getBytes(bytes, 2);
                short numOfUsers = bytesToShort(bytes);
                ans = ans + std::to_string(numOfUsers);;

                for (int i = 0; i < numOfUsers; i++) {
                    string name="";
                    handler.getLine(name);
                    ans += " " + name;
                }
                break;
            }
            case 8: { // stat
                handler.getBytes(bytes, 2);
                short numPosts= bytesToShort(bytes);
                ans = ans + std::to_string(numPosts);

                handler.getBytes(bytes, 2);
                short numFollowers = bytesToShort(bytes);
                ans = ans + " " + std::to_string(numFollowers);

                handler.getBytes(bytes, 2);
                short numFollowing = bytesToShort(bytes);
                ans = ans + " "+ std::to_string(numFollowing);

                break;
            }
        }

    }
    if (opCode == 9) { // notification message
        ans="NOTIFICATION ";
        handler.getBytes(bytes,1);
        if(bytes[0]==0)
            ans=ans+ "PM";
        else
            ans=ans+"Public";
        string name;//posting user
        handler.getLine(name);
        ans += " " + name;

        string content;//content
        handler.getLine(content);
        ans += " " + content;

    }
    if (opCode == 11) {
        ans="ERROR ";

        handler.getBytes(bytes, 2);
        short opCode = bytesToShort(bytes);
        ans = ans + std::to_string(opCode);
    }

   cout << ans << endl;

    return false;
}

//----------------------------------------------------methods-----------------
short ClientEncoderDecoder::bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void shortToBytes(short num, char* bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

void ClientEncoderDecoder::sendOpCode(short opCode){
    char bytes[2];
    shortToBytes(opCode,bytes);
    handler.sendBytes(bytes,2);
}


//------------------------------------------------------encoding methods---------------------
void ClientEncoderDecoder::encodeRegister(string command) {
    sendOpCode(1);
    vector<string> words;
    boost::split(words,command,boost::is_any_of(" "));

    handler.sendLine(words[0]);
    handler.sendLine(words[1]);

}

void ClientEncoderDecoder::encodeLogin(string command) {
    sendOpCode(2);

    vector<string> words;
    boost::split(words,command,boost::is_any_of(" "));

    handler.sendLine(words[0]);
    handler.sendLine(words[1]);
}

void ClientEncoderDecoder::encodeLogout(string command) {
    sendOpCode(3);
}

void ClientEncoderDecoder::encodeFollow(string command) {
    sendOpCode(4);

    vector<string> words;
    boost::split(words, command, boost::is_any_of(" "));

    char bytes[1];
    if(words[0]=="0")
        bytes[0] = 0;
    else
        bytes[0] = 1;
    
    handler.sendBytes(bytes, 1); // sending 0/1

    sendOpCode(short(stoi(words[1]))); // sending the num of users

    for(unsigned int i=2; i<words.size();i++){
        handler.sendLine(words[i]);
    }

}

void ClientEncoderDecoder::encodePost(string command) {
    sendOpCode(5);

    handler.sendLine(command);
}

void ClientEncoderDecoder::encodePM(string command) {
    sendOpCode(6);

    unsigned long spaceIndex = command.find_first_of(' ');

    string username = command.substr(0,spaceIndex);
    handler.sendLine(username);
    string content = command.substr(spaceIndex+1);
    handler.sendLine(content);

}


void ClientEncoderDecoder::encodeUserlist(string command) {
    sendOpCode(7);
}

void ClientEncoderDecoder::encodeStat(string command) {
    sendOpCode(8);

    handler.sendLine(command);
}
