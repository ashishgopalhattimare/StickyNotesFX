package sample;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class FirebaseConfig {

    public static Firebase firebase = null;

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

        firebase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot user : dataSnapshot.getChildren()) {
                    UserDetail userDetail = user.getValue(UserDetail.class);
//
                    if(userDetail.getUsername().equals(requestUser.getUsername())) {

                        System.out.println("Already Exists");
                         new SplashController().generateStickyFrame();
                        return;
                    }
                }

                firebase.child("Users").push().setValue(requestUser);
                System.out.println("Newly Added");

                // new SplashController().generateStickyFrame();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void AddUser() {
        if(firebase != null) { addUserToFirebase(Constants.user); }
        else System.out.println("Not Connected");
    }
}
