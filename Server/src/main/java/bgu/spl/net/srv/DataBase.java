package bgu.spl.net.srv;

import bgu.spl.net.srv.Messages.NotificationMessage;
import bgu.spl.net.srv.Messages.PmMessage;
import bgu.spl.net.srv.Messages.PostMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {

    private ConcurrentHashMap<String, Client> registeredClients = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, String> logedinClients = new ConcurrentHashMap<>(); // key:connectionId, value:username
    private ConcurrentHashMap<String, String[]> messages = new ConcurrentHashMap<>();
    private String userList="";

    public DataBase(){}

    public boolean register(Client client) {
        synchronized (registeredClients) {
            if (registeredClients.containsKey(client.getUsername())) // return false if already register
                return false;
            registeredClients.put(client.getUsername(), client);
            userList=userList+client.getUsername()+'\0';
            return true;
        }
    }

    public List<NotificationMessage> login(String userName, String password, int connectionId) {
        if (!registeredClients.containsKey(userName))// return null if not register
            return null;
        Client client = registeredClients.get(userName);

        synchronized (logedinClients) {
            if (!client.getPassword().equals(password) || logedinClients.containsKey(connectionId) || logedinClients.contains(userName))//wrong password or client already logged in
                return null;

            logedinClients.put(connectionId, userName);
            client.setConnectionId(connectionId);//setting the new connectionId
            client.setLogedIn(true);
            return client.getAwaitingMessages();
        }
    }

    public boolean logout(int connectionId) {
        if (!logedinClients.containsKey(connectionId))
            return false;

        Client client = registeredClients.get(logedinClients.get(connectionId)); // getting the client

        synchronized (client) {
            client.setLogedIn(false);
            logedinClients.remove(connectionId);
            return true;
        }
    }

    public List<String> follow(int connectionId, String[] users, boolean follow) {
        if (!logedinClients.containsKey(connectionId)) // return null if not logged-in
            return null;
        Client client = registeredClients.get(logedinClients.get(connectionId));
        List<String> registeredUsers = new LinkedList<>();
        for (String user : users) {// find the registered users
            if (registeredClients.containsKey(user)) {
                registeredUsers.add(user);
            }
        }
        if (follow) {

            List<String> succesfullUsers = client.follow(registeredUsers);
            for (String user : succesfullUsers) {// add the follower to the users
                Client toFollow = registeredClients.get(user);
                toFollow.addToMyFollowers(client.getUsername());
            }
            return succesfullUsers;
        } else {//unFollow
            List<String> succesfullUsers = client.unFollow(registeredUsers);

            for (String user : succesfullUsers) { // update the following clients
                if (registeredClients.containsKey(user)) {
                    Client toFollow = registeredClients.get(user);
                    toFollow.removeFromMyFollowers(client.getUsername());
                }
            }

            return succesfullUsers;
        }
    }

    public List<Integer> post(int connectionId, List<String> users, String content) {
        if (!logedinClients.containsKey(connectionId))
            return new LinkedList<>();
        Client client = registeredClients.get(logedinClients.get(connectionId));
        client.incrementNumOfPosts();

        String[] s = new String[2];
        s[0] = "POST";
        s[1] = content;
        messages.put(client.getUsername(), s); // add the post to a data fucking structure

        List<String> list = new LinkedList<>(users); // copying the users list
        for (String follower : client.getMyFollowers()) { // adding the followers to the list
            if (!list.contains(follower))
                list.add(follower);
        }

        List<Integer> loggedInUsers = new LinkedList<>(); // a list of logged-in client (the connectionId of them)

        for (String user : list) { // send the post to the client's followers
            if (registeredClients.containsKey(user)) {
                Client userClient = registeredClients.get(user);
                synchronized (userClient) {
                    if (!userClient.isLogedIn())
                        userClient.addMessage(new NotificationMessage(1, client.getUsername(), content));
                    else
                        loggedInUsers.add(userClient.getId());
                }
            }
        }

        return loggedInUsers;
    }

    public Client getClientIfLoggedIn(int connectionId) {
        if (logedinClients.containsKey(connectionId))
            return registeredClients.get(logedinClients.get(connectionId));

        return null;
    }

    public boolean isRegistered(String username) {
        return registeredClients.containsKey(username);
    }

    public int  sendPM(int connectionId, String username, String content) {
        Client client2send2 = registeredClients.get(username);

        synchronized (client2send2) {
            if (!client2send2.isLogedIn()) {//the username not login- add to his awaitmessage
                client2send2.addMessage(new NotificationMessage(0, logedinClients.get(connectionId), content));
                return -1;
            }

            String[] s = new String[2];
            s[0] = "PM";
            s[1] = content;
            messages.put(logedinClients.get(connectionId), s);// add the PM to a data fucking structure

            return client2send2.getId();
        }
    }

    public String getUserList() {
        return userList;
    }

    public Client getClientIfRegistered(String username) { // check if the username is registered and returns the client
        if (!registeredClients.containsKey(username))
            return null;

        return registeredClients.get(username);
    }


}
