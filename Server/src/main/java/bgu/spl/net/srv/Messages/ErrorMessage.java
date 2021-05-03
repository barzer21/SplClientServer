package bgu.spl.net.srv.Messages;

public class ErrorMessage implements OpMessage {
    private short messageOpcode;

    public ErrorMessage(int messageOpcode){
        this.messageOpcode = (short)messageOpcode;
    }

    public byte[] getBytes(){
        byte[] bytes= new byte[4];

        bytes[0] = (byte)((11 >> 8) & 0xFF);
        bytes[1] = (byte)(11 & 0xFF);
        bytes[2] = (byte)((messageOpcode >> 8) & 0xFF);
        bytes[3] = (byte)(messageOpcode & 0xFF);

        return bytes;
    }

}
