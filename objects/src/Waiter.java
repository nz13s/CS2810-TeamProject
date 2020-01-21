import java.util.Objects;

/**
 * Template class for Waiter to test if this stuff commits
 */
public class Waiter {
  private String username;
  private String pwd;

  public Waiter(String username, String pwd) {
    this.username = username;
    this.pwd = pwd;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Waiter waiter = (Waiter) o;
    return Objects.equals(username, waiter.username) &&
        Objects.equals(pwd, waiter.pwd);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, pwd);
  }
}
