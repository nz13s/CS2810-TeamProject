import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

  /**
   * Template connection method
   */
  public static Connection connection() throws SQLException {
    Connection connection = null;
    String url = "temp";
    String username = "temp";
    String password = "temp";
    try {
      connection = DriverManager.getConnection(url, username, password);
      System.out.println("Connection established");
    } catch (SQLException e) {
      System.out.println("Connection failed, see below: ");
      e.printStackTrace();
    }
    return connection;
  }
}
