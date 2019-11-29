package sample.Firebase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUser {

    public String username;
    public boolean keepShow;

    public List<Integer> cardf1;
    public List<String> cardf2, cardf3;
    public List<Boolean> cardf4;

    public FirebaseUser(String username) {
        this.username = username;

        cardf1 = new ArrayList<>(); cardf2 = new ArrayList<>();
        cardf3 = new ArrayList<>(); cardf4 = new ArrayList<>();

        keepShow = false;
    }
    public FirebaseUser() {} // Default Constructor for the Firebase to work properly
}
