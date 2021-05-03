package bgu.spl.net.srv.Messages;

import java.nio.charset.StandardCharsets;

public class StatMessage implements OpMessage {

    private String username;

    public StatMessage(byte[] bytes, int len){
        username = new String(bytes, 0, len-1, StandardCharsets.UTF_8); // creating the string out of the bytes
    }

    public String getUsername() {
        return username;
    }
}
