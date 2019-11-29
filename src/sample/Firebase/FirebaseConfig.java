package sample.Firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sample.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class FirebaseConfig {

    public static Firebase firebase = null;

    private static JSONParser parser = new JSONParser();
    private static Gson gson = new Gson();

    public static void SetUpConnection() {

        try {
            firebase = new Firebase(Constants.FIREBASE_LINK);
            return;
        } catch (Exception e) { }

        firebase = null;
        System.out.println("Not Connected");
    }

    public static void addUserDetails(UserDetail object)
    {
        firebase.child("UserDetail").push().setValue(object);
        Constants.userDetail = object;
    }

    // return not null if the user exists
    public static boolean userExistLogin(String username, String password)
    {
        String path = Constants.FIREBASE_LINK + "/UserDetail/.json";

        System.out.println("user exists login validation");
        try {
            URL url = new URL(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            String jsonString = br.readLine();
            if(!jsonString.equals("null")) {
                JSONObject jsonObject = (JSONObject) parser.parse(jsonString);

                for(Object key : jsonObject.keySet()) {
                    UserDetail fud = gson.fromJson(jsonObject.get(key) + "", UserDetail.class);

                    if(fud.getUsername().equals(username)) {
                        if(fud.getPassword().equals(UserDetail.passwordHash(password))) {
                            Constants.userDetail = new UserDetail(username, fud.getEmail(), fud.getPassword());
                            return true;
                        }
                        return false;
                    }
                }
            }
        }
        catch (Exception e) {}

        return false; // user doesnot exists
    }

    public static boolean freeUsername(String username) {
        String path = Constants.FIREBASE_LINK + "/UserDetail/.json";

        System.out.println("check for username already taken or not!!!");
        try {
            URL url = new URL(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String jsonString = br.readLine();
            if(!jsonString.equals("null")) {
                JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
                for(Object key : jsonObject.keySet()) {
                    UserDetail fud = gson.fromJson(jsonObject.get(key) + "", UserDetail.class);
                    if(fud.getUsername().equals(username)) return false;
                }
            }

            return true;
        }
        catch (Exception e) {}
        return false;
    }

    public static void addUserToFirebase(UserDetail requestUserDetail) {

        boolean newUser = false;

        if(userExistLogin(requestUserDetail.getUsername(), requestUserDetail.getPassword())) {
            newUser = false;
        }

        new SplashController().generateStickyFrame(); // open new frame

        if(!newUser) {
            firebase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot detail : dataSnapshot.getChildren()) {
                        FirebaseUser firebaseUser = detail.getValue(FirebaseUser.class);

                        if(firebaseUser.username.equals(requestUserDetail.getUsername())) {
                            Constants.fbDetails = firebaseUser;

                            for(int i = 0; i < Constants.fbDetails.cardf1.size(); i++) {
                                CardDetail temp = new CardDetail(Constants.fbDetails.cardf2.get(i),
                                        Constants.fbDetails.cardf1.get(i), Constants.fbDetails.cardf4.get(i));

                                temp.setDefaultDate(Constants.fbDetails.cardf3.get(i));

                                StickyController.cardList.add(temp);
                                StickyController.s_recyclerView.getItems().add(temp);
                            }

                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) { }
            });
        }

        return;
    }

    public static void AddUser()
    {
        if(firebase != null) addUserToFirebase(Constants.userDetail);
        else
            System.out.println("Not Connected");
    }

    public static void syncUserData() {

        String username = Constants.userDetail.getUsername();

        firebase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot detail : dataSnapshot.getChildren()) {
                    FirebaseUser firebaseUser = detail.getValue(FirebaseUser.class);
                    if(firebaseUser.username.equals(username)) {
                        detail.getRef().removeValue();
                        break;
                    }
                }

                if(StickyController.cardList.size() > 0) {
                    UserDetail x = Constants.userDetail;
                    FirebaseUser f = new FirebaseUser(x.getUsername());

                    for(CardDetail card : StickyController.cardList) {
                        f.cardf1.add(card.getColor());
                        f.cardf2.add(card.getText());
                        f.cardf3.add(card.getDate());
                        f.cardf4.add(card.isFavourite());
                    }
                    firebase.child("Users").push().setValue(f);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
