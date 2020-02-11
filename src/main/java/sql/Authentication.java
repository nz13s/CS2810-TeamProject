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
    private PreparedStatement modifyUserPass; //todo


    public Authentication(Connection connection) throws SQLException {
        getUser = connection.prepareStatement(
                "SELECT staff_id, pwd_hash, pwd_salt, username from staff where username = ?");

        addUser = connection.prepareStatement(
                "INSERT INTO staff (pwd_hash, pwd_salt, username) values (?, ?, ?) ", PreparedStatement.RETURN_GENERATED_KEYS);

        modifyUserPass = connection.prepareStatement(
                "UPDATE staff set pwd_hash = ?, pwd_salt = ? where staff_id = ?");
    }

    /**
     * Attempt to log the user in. Returns userID if successful, else a negative, failure number.
     *
     * @return return >= 0 on success, return < 0 on failure - -1 for user not found or password d
     */
    public int login(String username, String password) {
        try {
            getUser.setString(1, username.toLowerCase());
            ResultSet resultSet = getUser.executeQuery();
            boolean match = resultSet.first();
            if (match) {
                byte[] pwd_salt = resultSet.getBytes("pwd_salt");
                byte[] pwd_hash = resultSet.getBytes("pwd_hash");

                byte[] hash = genPasswordHash(password, pwd_salt, PBKDF2_ITERATIONS);

                match = Arrays.equals(pwd_hash, hash);
            }
            if (!match) {
                return -1;
            }

            return resultSet.getInt("staff_id");

        } catch (SQLException e) {
            return -2;
        }

    }

    public int addNewUser(String username, String password) {
        try {

            byte[] salt = genNewSalt();
            byte[] hash = genPasswordHash(password, salt, PBKDF2_ITERATIONS);

            addUser.setBytes(1, hash);
            addUser.setBytes(2, salt);
            addUser.setString(3, username.toLowerCase());

            addUser.executeUpdate();

            ResultSet resultSet = addUser.getGeneratedKeys();

            return resultSet.getInt("staff_id");

        } catch (SQLException ex) {
            return -1;
        }
    }

    public boolean updateUserPassword(int userID, String password) {
        try {
            byte[] salt = genNewSalt();
            byte[] hash = genPasswordHash(password, salt, PBKDF2_ITERATIONS);

            modifyUserPass.setBytes(1, hash);
            modifyUserPass.setBytes(2, salt);
            modifyUserPass.setInt(3, userID);

            modifyUserPass.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
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
    private static byte[] genPasswordHash(String password, byte[] salt, int iterations) {
        try {
            char[] chars = password.toCharArray();
            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            return null;
        }
    }

    private static byte[] genNewSalt() {
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
