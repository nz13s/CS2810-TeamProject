import java.util.Objects;

/**
 * Template class for Waiter to test if this stuff commits
 */
public class Staff {
  private String username;
  private String pwd;
  private Table table;

  public Staff(String username, String pwd, Table table) {
    this.username = username;
    this.pwd = pwd;
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
        Objects.equals(table, staff.table);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, pwd, table);
  }

  //template to add a Waiter to the table when he logs in, add him as a row to the table
  public void waiterLogin(){}

  //remove him from table (as in DB table row, not actual table) when waiter logs out
  public void waiterLogout(){}

  //gets order from table and sends it to kitchen
  public void getOrder(){}

}
