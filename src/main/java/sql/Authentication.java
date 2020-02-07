package sql;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Authentication {

    private static int PBKDF2_ITERATIONS = 1000;

    private PreparedStatement getUser;
    private PreparedStatement addUser; //todo
    private PreparedStatement modifyUser; //todo


    public Authentication(Connection connection) throws SQLException {
        getUser = connection.prepareStatement(
                "SELECT staff_id, pwd_hash, pwd_salt, username from staff where username = ?");
    }

    /**
     * Attempt to log the user in. Returns userID if successful, else a negative, failure number.
     *
     * @return return >= 0 on success, return < 0 on failure
     */
    public int login(String username, String password) {
        try {
            getUser.setString(1, username);
            ResultSet resultSet = getUser.executeQuery();

            byte[] pwd_salt = resultSet.getBytes("pwd_salt");
            byte[] pwd_hash = resultSet.getBytes("pwd_hash");

            byte[] hash = genPasswordHash(password, pwd_salt, PBKDF2_ITERATIONS);

            boolean match = Arrays.equals(pwd_hash, hash);

            if (!match) {
                return -1;
            }

            return resultSet.getInt("staff_id");

        } catch (SQLException e) {
            return -2;
        }

    }

    /**
     * Generates the hash of a password, given its salt and iteration num
     *
     * @param password   the plaintext password
     * @param salt       the user's salt (saved in the db)
     * @param iterations the number of iterations (must be the same as when generated)
     * @return byte array of the hash
     */
    public static byte[] genPasswordHash(String password, byte[] salt, int iterations) {
        try {
            char[] chars = password.toCharArray();
            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            return null;
        }
    }

    private static byte[] genSalt() {
        try {
            byte[] salt = new byte[64];
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }


}
