package sample;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUserDetail {

    public String username, fullName;
    public String email, password;
    public boolean keepShow;

    public List<Integer> cardf1;
    public List<String> cardf2, cardf3;
    public List<Boolean> cardf4;

    public FirebaseUserDetail(String username, String fullName, String email, String password) {
        this.username = username; this.fullName = fullName;
        this.email = email; this.password = password;

        cardf1 = new ArrayList<>(); cardf2 = new ArrayList<>();
        cardf3 = new ArrayList<>(); cardf4 = new ArrayList<>();

        keepShow = false;
    }
    public FirebaseUserDetail() {} // Default Constructor for the Firebase to work properly
}
