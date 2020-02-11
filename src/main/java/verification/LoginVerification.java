package verification;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginVerification {

    private static Pattern usernameCheck = Pattern.compile("^[0-9a-zA-Z_-]{5,64}$");
    private static String niceUsernameMessage = "Must be between 5 and 64 characters (inclusive) " +
            "and cannot contain anything other than alphanumeric characters," +
            " underscores or hyphens.";

    public static boolean checkUsername(String username) {
        Matcher m = usernameCheck.matcher(username);
        return m.matches();
    }

    public static String getNiceUsernameMessage() {
        return niceUsernameMessage;
    }

}
