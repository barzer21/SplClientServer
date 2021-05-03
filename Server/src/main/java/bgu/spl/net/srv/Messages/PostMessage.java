package bgu.spl.net.srv.Messages;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class PostMessage implements OpMessage {
    private String content;
    private List<String> usersToSend;

    public PostMessage(byte[] bytes, int len){
        usersToSend = new LinkedList<>();
        content = new String(bytes, 0, len-1, StandardCharsets.UTF_8); // creating the string out of the bytes

        //--------------------------adds tagged users to the "usersToSend" list--------------------
        for(int i=0; i<content.length(); i++){
            if(content.charAt(i) == '@'){
                String name = "";
                i++;
                while(i<content.length() && content.charAt(i) != ' '){
                    name = name + content.charAt(i);
                    i++;
                }

                if(!usersToSend.contains(name))
                    usersToSend.add(name);
            }
        }
    }

    public String getContent(){
        return content;
    }

    public List<String> getUsersToSend(){
        return usersToSend;
    }

}
