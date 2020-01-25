package entities;

import java.util.Objects;

public class Staff {
  private String username;
  private String pwd; //hashed password
  private String pwdSalt; //salt for the user
  private Table table;

  public Staff(String username, String pwd, Table table, String pwdSalt) {
    this.username = username;
    this.pwd = pwd;
    this.pwdSalt = pwdSalt;
    this.table = table;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public String getPwdSalt() {
    return pwdSalt;
  }

  public void setPwdSalt(String pwdSalt) {
    this.pwdSalt = pwdSalt;
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Staff staff = (Staff) o;
    return Objects.equals(username, staff.username) &&
        Objects.equals(pwd, staff.pwd) &&
        Objects.equals(pwdSalt, staff.pwdSalt) &&
        Objects.equals(table, staff.table);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, pwd, pwdSalt, table);
  }
}
