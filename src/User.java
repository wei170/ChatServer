
public class User {
    private String username;
    private String password;
    private SessionCookie cookie;
    // TODO: fix it

    public User(String username, String password, SessionCookie cookie) {
        this.username = username;
        this.password = password;
        this.cookie = cookie;
        // TODO: fix it
    }

    public String getName() {
        return this.username;
    }

    public boolean checkPassword() {
        // TODO
        return true;
    }

    public SessionCookie getCookie() {
        return this.cookie;
    }

    public void setCookie(SessionCookie cookie) {
        // TODO
    }
}
