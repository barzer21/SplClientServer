package bgu.spl.net.srv.Messages;

public class NotificationMessage implements OpMessage {
    int notificationType;
    String postingUser;
    String content;


    public NotificationMessage(int notificationType, String postingUser, String content){
        this.notificationType = notificationType;
        this.postingUser = postingUser;
        this.content = content;
    }

    public byte[] getBytes(){
        byte[] postingUserBytes = postingUser.getBytes();
        byte[] contentBytes = content.getBytes();

        byte[] bytes = new byte[postingUserBytes.length + contentBytes.length + 5];

        bytes[0] = (byte)((9 >> 8) & 0xFF); // NOTIFICATION opCode
        bytes[1] = (byte)(9 & 0xFF);
        bytes[2] = (byte)notificationType; // PM/Public
        int bytesIndex = 3;
        for(byte b: postingUserBytes){ // adding the writer
            bytes[bytesIndex] = b;
            bytesIndex++;
        }
        bytes[bytesIndex] = (byte) '\0';
        bytesIndex++;

        for(byte b: contentBytes){ // adding the content
            bytes[bytesIndex] = b;
            bytesIndex++;
        }
        bytes[bytesIndex] = (byte) '\0';

        return bytes;
    }

}
