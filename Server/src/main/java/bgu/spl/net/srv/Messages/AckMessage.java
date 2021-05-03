package bgu.spl.net.srv.Messages;

public class AckMessage implements OpMessage{
    private short opCode;

    public AckMessage(int opCode){
        this.opCode=(short)opCode;
    }// general ACK message

    public byte[] getBytes(){
        byte[] bytes= new byte[4];

        bytes[0] = (byte)((10 >> 8) & 0xFF);
        bytes[1] = (byte)(10 & 0xFF);
        bytes[2] = (byte)((opCode >> 8) & 0xFF);
        bytes[3] = (byte)(opCode & 0xFF);

        return bytes;
    }
}
