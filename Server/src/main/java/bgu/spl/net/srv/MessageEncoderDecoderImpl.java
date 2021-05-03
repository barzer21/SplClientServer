package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.Messages.*;

import java.util.Arrays;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<OpMessage> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private byte[] opCodeBytes = new byte[2];
    private int len = 0;
    private short opCode = -1;
    private int zeroCounter = 0;
    private short zerosMax = -1;

    @Override
    public OpMessage decodeNextByte(byte nextByte) {
        if (opCode == -1) {
            opCodeBytes[len] = nextByte;
            len++;
            if (len == 2) {
                opCode = bytesToShort(opCodeBytes);
                len = 0; // setting the len again for the "bytes" array
            }
        }
        else {

            switch (opCode) { // REGISTER
                case 1: {
                    return decodeRegister(nextByte);
                }
                case 2: { // LOGIN
                    return decodeLogin(nextByte);
                }
                case 4: { // FOLLOW
                    return decodeFollow(nextByte);
                }
                case 5: { // POST
                    return decodePost(nextByte);
                }
                case 6: { // PM
                    return decodePm(nextByte);
                }
                case 8: { // STAT
                    return decodeStat(nextByte);
                }
            }
        }
        if(opCode==7){
            opCode = -1;
            opCodeBytes = new byte[2];
            return new UserlistMessage();
        }
        if(opCode==3){
            opCode = -1;
            opCodeBytes = new byte[2];
            return new LogoutMessage();
        }
        return null;
    }

    @Override
    public byte[] encode(OpMessage message) {// getting a massage that has a toBytes method
        if(message instanceof AckMessage)
            return ((AckMessage)message).getBytes();
        if(message instanceof AckFollowMessage)
            return ((AckFollowMessage) message).getBytes();
        if(message instanceof AckStatMessage)
            return ((AckStatMessage) message).getBytes();
        if(message instanceof AckUserlistMessage)
            return ((AckUserlistMessage) message).getBytes();
        if(message instanceof ErrorMessage)
            return ((ErrorMessage) message).getBytes();
        if(message instanceof NotificationMessage)
            return ((NotificationMessage) message).getBytes();

        return new byte[0];
    }

    private short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    //---------------------------methods for decode--------------------------------------------------------------

    private void initiateFields(){ // initiate all the fields after creating new message
        len=0;
        bytes = new byte[1 << 10];
        opCodeBytes = new byte[2];
        opCode = -1;
        zeroCounter = 0;
        zerosMax = -1;
    }


    private RegisterMessage decodeRegister(byte nextByte) {
        pushByte(nextByte);
        if ((char) nextByte == '\0') // counting the zeroes
            zeroCounter++;
        if (zeroCounter == 2) {
            RegisterMessage message = new RegisterMessage(bytes,len);
            initiateFields();
            return message;
        }

        return null;
    }


    private LoginMessage decodeLogin(byte nextByte) {
        pushByte(nextByte);
        if ((char) nextByte == '\0')// counting the zeroes
            zeroCounter++;
        if (zeroCounter == 2) {
            LoginMessage message = new LoginMessage(bytes,len);
            initiateFields();
            return message;
        }

        return null;
    }


    private FollowMessage decodeFollow(byte nextByte){
        pushByte(nextByte);
        if(len>=3){
            if(len==3){// getting the number of users
                byte[] numOfUsersBytes = new byte[2];
                numOfUsersBytes[0] = bytes[1];
                numOfUsersBytes[1] = bytes[2];
                zerosMax = bytesToShort(numOfUsersBytes);
            }
            if ((char) nextByte == '\0'){ // counting the zeroes
                zeroCounter++;
            }
            if(zeroCounter == zerosMax){ // if we got to the last user, stop and return the message
                FollowMessage message = new FollowMessage(bytes,len);
                initiateFields();
                return message;
            }
        }

        return null;
    }

    private PostMessage decodePost(byte nextByte){
        pushByte(nextByte);
        if ((char) nextByte == '\0'){ // checking if we got to the zero-byte
            PostMessage message = new PostMessage(bytes,len);
            initiateFields();
            return message;
        }

        return null;
    }


    private PmMessage decodePm(byte nextByte){
        pushByte(nextByte);
        if ((char) nextByte == '\0'){// counting the zeroes
            zeroCounter++;
        }
        if(zeroCounter==2){
            PmMessage message = new PmMessage(bytes,len);
            initiateFields();
            return message;
        }

        return null;
    }

    private StatMessage decodeStat(byte nextByte){
        pushByte(nextByte);
        if ((char) nextByte == '\0'){ // checking if we got to the zero-byte
            StatMessage message = new StatMessage(bytes,len);
            initiateFields();
            return message;
        }

        return null;
    }

}
