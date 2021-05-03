package bgu.spl.net.srv;

import bgu.spl.net.srv.Messages.*;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.DataBase;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.List;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<OpMessage> {
    private int connectionId;
    private Connections<OpMessage> connections;
    private DataBase dataBase;

    public BidiMessagingProtocolImpl(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public void start(int connectionId, Connections<OpMessage> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }

    //-------------------------------------process according to message type---------------------------------------
    @Override
    public void process(OpMessage message) {
        if (message instanceof RegisterMessage)
            processRegisterMessage((RegisterMessage) message);
        if (message instanceof LoginMessage)
            processLoginMessage((LoginMessage) message);
        if (message instanceof LogoutMessage)
            processLogoutMessage((LogoutMessage) message);
        if (message instanceof FollowMessage)
            processFollowMessage((FollowMessage) message);
        if (message instanceof PostMessage)
            processPostMessage((PostMessage) message);
        if (message instanceof PmMessage)
            processPmMessage((PmMessage) message);
        if (message instanceof UserlistMessage)
            processUserlistMessage((UserlistMessage) message);
        if (message instanceof StatMessage)
            processStatMessage((StatMessage) message);

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

    private void processRegisterMessage(RegisterMessage message) {
        Client client = new Client(connectionId, message.getUsername(), message.getPassword()); // creating a new client
        if (dataBase.register(client))//returns false if already register
            connections.send(connectionId, new AckMessage(1));
        else
            connections.send(connectionId, new ErrorMessage(1));
    }


    private void processLoginMessage(LoginMessage message) {
        List<NotificationMessage> awaitingMessages = dataBase.login(message.getUsername(), message.getPassword(), connectionId); // getting the awaiting messages of the user
        if (awaitingMessages == null)
            connections.send(connectionId, new ErrorMessage(2));
        else {
            connections.send(connectionId, new AckMessage(2));//success log in
            for (NotificationMessage notificationMessage : awaitingMessages) {// send all the awaitingMessages when login
                connections.send(connectionId, notificationMessage);
            }
        }
    }

    private void processLogoutMessage(LogoutMessage message) {
        if (dataBase.logout(connectionId)) {
            connections.send(connectionId, new AckMessage(3));
            connections.disconnect(connectionId);//remove from the connections
        } else
            connections.send(connectionId, new ErrorMessage(3));
    }

    private void processFollowMessage(FollowMessage message) {
        List<String> succesfullUsers = dataBase.follow(connectionId, message.getUsernameArr(), message.isFollow()); // getting succesfulUsers

        if (succesfullUsers == null || succesfullUsers.isEmpty()) // the user is not loggedIn or all users failed to follow/unFollow
            connections.send(connectionId, new ErrorMessage(4));
        else {
            connections.send(connectionId, new AckFollowMessage(succesfullUsers));
        }
    }

    private void processPostMessage(PostMessage message) {
        Client client = dataBase.getClientIfLoggedIn(connectionId);
        if (client == null) // the client is not logged-in
            connections.send(connectionId, new ErrorMessage(5));
        else {
            connections.send(connectionId, new AckMessage(5));
            List<Integer> logedInUsers = dataBase.post(connectionId, message.getUsersToSend(), message.getContent());

            for (Integer userId : logedInUsers) { // sending the message to the logged-in users
                connections.send(userId, new NotificationMessage(1, client.getUsername(), message.getContent()));
            }
        }

    }

    private void processPmMessage(PmMessage message) {
        Client client = dataBase.getClientIfLoggedIn(connectionId);
        if (client == null || !dataBase.isRegistered(message.getUsername())) // the client is not logged-in
            connections.send(connectionId, new ErrorMessage(6));
        else {
            connections.send(connectionId, new AckMessage(6));
            int id = dataBase.sendPM(connectionId, message.getUsername(), message.getContent());
            if (id != -1)// the user is logged-in so we need to send it the message
                connections.send(id, new NotificationMessage(0, client.getUsername(), message.getContent()));
        }
    }

    private void processUserlistMessage(UserlistMessage message) {

        Client client = dataBase.getClientIfLoggedIn(connectionId);
        if (client == null) // the client is not logged-in
            connections.send(connectionId, new ErrorMessage(7));
        else {
            String usersList = dataBase.getUserList();
            connections.send(connectionId, new AckUserlistMessage(usersList));
        }
    }

    private void processStatMessage(StatMessage message) {
        Client client = dataBase.getClientIfLoggedIn(connectionId);
        if (client == null) // the client is not logged-in
            connections.send(connectionId, new ErrorMessage(8));
        else {
            Client user = dataBase.getClientIfRegistered(message.getUsername());
            if (user == null)
                connections.send(connectionId, new ErrorMessage(8));
            else { // adding the arguments of stat message
                int numOfPosts = user.getNumOfPosts();
                int numOfFollowers = user.getMyFollowers().size();
                int numOfFollowing = user.getiFollow().size();
                connections.send(connectionId, new AckStatMessage(numOfPosts, numOfFollowers, numOfFollowing));
            }
        }
    }
}
