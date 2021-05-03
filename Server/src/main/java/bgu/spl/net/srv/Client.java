package bgu.spl.net.srv;

import bgu.spl.net.srv.Messages.NotificationMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    private int connectionId;
    private String username;
    private String password;
    private List<String> myFollowers; // a list of those who follow me
    private List<String> iFollow; // a list of those I follow
    private  boolean logedIn;
    private List<NotificationMessage> awaitingMessages; // contains messages that waits for me to log-in
    private AtomicInteger numOfPosts;

    public Client(int connectionId, String username, String password){
        this.connectionId = connectionId;
        this.username = username;
        this.password = password;
        myFollowers = new LinkedList<>();
        iFollow = new LinkedList<>();
        this.logedIn=false;
        awaitingMessages = new LinkedList<>();
        numOfPosts = new AtomicInteger(0);
    }


    public List<String> getiFollow() {
        return iFollow;
    }

    public List<String> getMyFollowers() {
        return myFollowers;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return connectionId;
    }

    public List<String> follow(List<String> usernames){
        LinkedList<String> list = new LinkedList<>();
        for(String user: usernames){ // checking if the client already following them
            if(!iFollow.contains(user)){
                iFollow.add(user);
                list.add(user);
            }
        }
        return list;// return list of new users to follow
    }

    public List<String> unFollow(List<String> usernames){
        LinkedList<String> list = new LinkedList<>();
        for(String user: usernames){ // checking if the client is not following them
            if(iFollow.contains(user)){
                iFollow.remove(user);
                list.add(user);
            }
        }
        return list;// return list of new users to un-follow
    }

    public void addToMyFollowers(String username){
        if (!myFollowers.contains(username))
            myFollowers.add(username);
    }

    public void removeFromMyFollowers(String username){
        if (myFollowers.contains(username))
            myFollowers.remove(username);
    }

    public boolean isLogedIn() {
        return logedIn;
    }

    public void setLogedIn(boolean logedIn) {
        this.logedIn = logedIn;
    }

    public void addMessage(NotificationMessage message){
        awaitingMessages.add(message);
    }

    public List<NotificationMessage> getAwaitingMessages(){
        return awaitingMessages;
    }

    public void setConnectionId(int connectionId){
        this.connectionId=connectionId;
    }

    public void incrementNumOfPosts(){
        numOfPosts.incrementAndGet();
    }

    public int getNumOfPosts(){
        return numOfPosts.get();
    }

}
