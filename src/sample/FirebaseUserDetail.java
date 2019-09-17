package sample;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FirebaseUserDetail {

    public String username, fullName;
    public String email, password;

    public List<Integer> cardf1;
    public List<String> cardf2, cardf3;

    public FirebaseUserDetail(String username, String fullName, String email, String password) {
        this.username = username; this.fullName = fullName;
        this.email = email; this.password = password;

        cardf1 = new LinkedList<>(); cardf2 = new LinkedList<>();
        cardf3 = new LinkedList<>();
    }
    public FirebaseUserDetail() {} // Default Constructor for the Firebase to work properly
}
