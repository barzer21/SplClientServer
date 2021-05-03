package bgu.spl.net.srv.Messages;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class FollowMessage implements OpMessage {
    private boolean follow;
    private String[] usernameArr;

    public FollowMessage( byte[] bytes, int len){

        //-----------checking if follow/unFollow-------------------
        follow = (bytes[0] ==0);

        //-----------getting the number of users-------------------
        short numOfUsers = (short) ((bytes[1] & 0xff) << 8);
        numOfUsers += (short) (bytes[2] & 0xff);
        usernameArr = new String[numOfUsers];

        //--------------getting the users---------------------------
        int arrayIndex=0;
        int PreZeroIndex = 2; // indicates the last zero found index
        for (int i = 3; i < len; i++) {
            if ((char) bytes[i] == '\0') {
                String result = new String(bytes, PreZeroIndex + 1, i-PreZeroIndex-1, StandardCharsets.UTF_8); // creating the string out of the bytes

                usernameArr[arrayIndex] = result; // adding the username to the array
                arrayIndex++;

                PreZeroIndex = i;
            }
        }
    }

    public boolean isFollow() {
        return follow;
    }

    public String[] getUsernameArr(){
        return usernameArr;
    }
}
