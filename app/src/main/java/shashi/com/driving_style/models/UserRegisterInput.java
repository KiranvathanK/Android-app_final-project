package shashi.com.driving_style.models;

/**
 * Created by admin on 17/02/2017.
 */
public class UserRegisterInput {
    public String user_name;
    public String uid;
    public String user_password;
    public String user_mob;
    public String user_addr;
    public String user_type;

    public UserRegisterInput(String user_name, String uid, String user_password, String user_mob, String user_addr, String user_type) {
        this.user_name = user_name;
        this.uid = uid;
        this.user_password = user_password;
        this.user_mob = user_mob;
        this.user_addr = user_addr;
        this.user_type = user_type;
    }
}
//	"user_name":"user_name",
//            "uid":"uid",
//            "user_password":"user_password",
//            "user_mob":"user_mob",
//            "user_addr":"user_addr",
//            "user_type":"user_type"