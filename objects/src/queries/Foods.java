package queries;

import entities.Food;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Foods {

  private Connection c;

  public Foods(Connection c){
    this.c = c;
  }

}
