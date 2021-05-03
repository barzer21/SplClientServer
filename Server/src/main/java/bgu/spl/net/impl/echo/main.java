package bgu.spl.net.impl.echo;

public class main {

    public static void main(String[] args) {
        String s = "" + '\0';
        s = s + "aaaaaa" + '\0';
        s = s + "ssssss" + '\0';
        byte[] bytes = s.getBytes();

        if((char)bytes[0] == '\0')
            System.out.println("ddd");
    }

}
