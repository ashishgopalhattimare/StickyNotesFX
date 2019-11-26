package sample;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

public class FirebaseConfig {

    public static Firebase firebase = null;
    private static JSONParser parser = new JSONParser();
    private static Gson gson = new Gson();

    public static void SetUpConnection() {

        try {
            firebase = new Firebase(Constants.FIREBASE_LINK);
            return;
        }
        catch (Exception e) {}

        firebase = null;
        if(firebase == null) System.out.println("Not Connected");
    }

    public static void addUserToFirebase(UserDetail requestUser) {

        String path = Constants.FIREBASE_LINK + "/Users/.json";
        boolean newUser = false;
        try {
            URL url = new URL(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            String jsonString = br.readLine();
            if(!jsonString.equals("null")) {
                JSONObject jsonObject = (JSONObject) parser.parse(jsonString);

                for(Object keyStr : jsonObject.keySet()) {
                    String key = (String) keyStr;
                    FirebaseUserDetail firebaseUserDetail = gson.fromJson(jsonObject.get(key) + "", FirebaseUserDetail.class);

                    if(firebaseUserDetail.username.equals(requestUser.getUsername())) {
                        newUser = false;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        new SplashController().generateStickyFrame(); // open new frame

        if(!newUser) {
            firebase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot detail : dataSnapshot.getChildren()) {
                        FirebaseUserDetail firebaseUserDetail = detail.getValue(FirebaseUserDetail.class);

                        if(firebaseUserDetail.username.equals(requestUser.getUsername())) {
                            Constants.fbDetails = firebaseUserDetail;

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

    public static void AddUser() {
        if(firebase != null) { addUserToFirebase(Constants.user); }
        else System.out.println("Not Connected");
    }

    public static void syncUserData() {

        String username = Constants.user.getUsername();

        firebase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot detail : dataSnapshot.getChildren()) {
                    FirebaseUserDetail firebaseUserDetail = detail.getValue(FirebaseUserDetail.class);
                    if(firebaseUserDetail.username.equals(username)) {
                        detail.getRef().removeValue();
                        break;
                    }
                }

                if(StickyController.cardList.size() > 0) {
                    UserDetail x = Constants.user;
                    FirebaseUserDetail f = new FirebaseUserDetail(x.getUsername(), x.getFullName(), x.getEmail(), x.getPassword());

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
