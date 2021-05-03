package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.srv.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.MessageEncoderDecoderImpl;
import bgu.spl.net.srv.Server;



public class ReactorMain {
    public static void main(String[] args) {
        DataBase database = new DataBase(); //one shared object

        Server.reactor(
                Integer.parseInt(args[1]), //num of threads
                Integer.parseInt(args[0]), //port
                () ->  new BidiMessagingProtocolImpl(database), //protocol factory
                () -> new MessageEncoderDecoderImpl()//message encoder decoder factory
        ).serve();

    }
}
