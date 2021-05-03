package bgu.spl.net.srv.Messages;

import java.util.List;

public class AckUserlistMessage implements OpMessage {
    String usernames;

    public AckUserlistMessage(String usernames){
        this.usernames = usernames;
    }

    public byte[] getBytes(){
        //---------------------getting the bytes of the users string-------------

        byte[] usersBytes= usernames.getBytes();
        int numOfUsers = 0;
        for(int i=0;i<usernames.length();i++){ // counting the users
            if(usernames.charAt(i) == '\0')
                numOfUsers++;
        }

        //---------------------------creating the bytes array--------------------------
        byte[] bytes= new byte[usersBytes.length+6];

        bytes[0] = (byte)((10 >> 8) & 0xFF); // ACK opCode
        bytes[1] = (byte)(10 & 0xFF);
        bytes[2] = (byte)((7 >> 8) & 0xFF); //USELIST opCode
        bytes[3] = (byte)(7 & 0xFF);
        bytes[4] = (byte)((numOfUsers >> 8) & 0xFF);
        bytes[5] = (byte)(numOfUsers & 0xFF);

        for(int i=6;i<bytes.length;i++){ // adding the users
            bytes[i] = usersBytes[i-6];
        }

        return bytes;
    }
}
