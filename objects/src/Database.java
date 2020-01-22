import queries.Foods;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Database {
  private Connection connection;
  private PreparedStatement preparedStatement;

  public static Foods FOODS;

  public Database(Connection c){
    connection = c;
    FOODS = new Foods(c);
  }

}
