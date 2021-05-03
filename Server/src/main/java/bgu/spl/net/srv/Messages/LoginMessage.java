package bgu.spl.net.srv.Messages;

import java.nio.charset.StandardCharsets;

public class LoginMessage implements OpMessage {
    private String username = "";
    private String password = "";

    public LoginMessage(byte[] bytes, int len) {
        int PreZeroIndex = -1; // indicates the last zero found index
        for (int i = 0; i < len; i++) {
            if ((char) bytes[i] == '\0') {
                String result = new String(bytes, PreZeroIndex + 1, i-PreZeroIndex-1, StandardCharsets.UTF_8); // creating the string out of the bytes

                if (PreZeroIndex == -1)  // we got to the first zero
                    username = result;
                else // we got to the second zero
                    password = result;

                PreZeroIndex = i;
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
