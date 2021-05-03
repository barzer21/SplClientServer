package bgu.spl.net.srv.Messages;

import java.util.List;

public class AckFollowMessage implements OpMessage {
    List<String> usernameList;

    public AckFollowMessage(List<String> arr){
        usernameList = arr;
    }

    public byte[] getBytes(){
        //---------------------getting the bytes of the users string-------------
        String users="";
        for( String userName: usernameList) {
            users = users + userName + '\0';
        }

        byte[] usersBytes= users.getBytes();

        //---------------------------creating the bytes array--------------------------
        byte[] bytes= new byte[usersBytes.length+6];

        bytes[0] = (byte)((10 >> 8) & 0xFF);
        bytes[1] = (byte)(10 & 0xFF);
        bytes[2] = (byte)((4 >> 8) & 0xFF);
        bytes[3] = (byte)(4 & 0xFF);
        bytes[4] = (byte)((usernameList.size() >> 8) & 0xFF);
        bytes[5] = (byte)(usernameList.size() & 0xFF);

        for(int i=6;i<bytes.length;i++){
            bytes[i] = usersBytes[i-6];
        }

        return bytes;
    }

}
