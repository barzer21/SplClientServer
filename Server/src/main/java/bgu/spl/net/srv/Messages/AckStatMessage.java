package bgu.spl.net.srv.Messages;

public class AckStatMessage implements OpMessage{
    private int numPosts;
    private int numFollowers;
    private int numFollowing;

    public AckStatMessage(int posts, int followers, int following){
        numPosts = posts;
        numFollowers = followers;
        numFollowing = following;
    }

    public byte[] getBytes(){

        byte[] bytes= new byte[10];

        bytes[0] = (byte)((10 >> 8) & 0xFF); // ACK opCode
        bytes[1] = (byte)(10 & 0xFF);
        bytes[2] = (byte)((8 >> 8) & 0xFF); // STAT opCode
        bytes[3] = (byte)(8 & 0xFF);
        bytes[4] = (byte)((numPosts >> 8) & 0xFF); // num of post
        bytes[5] = (byte)(numPosts & 0xFF);
        bytes[6] = (byte)((numFollowers >> 8) & 0xFF); // num of followers
        bytes[7] = (byte)(numFollowers & 0xFF);
        bytes[8] = (byte)((numFollowing >> 8) & 0xFF); // num of following
        bytes[9] = (byte)(numFollowing & 0xFF);

        return bytes;
    }


}
