package shashi.com.driving_style.models;

public class UserLoginInput {
    public String user_name;
    public String user_password;
    public String user_type;

    public UserLoginInput(String user_name, String user_password, String user_type) {
        this.user_name = user_name;
        this.user_password = user_password;
        this.user_type = user_type;
    }
}