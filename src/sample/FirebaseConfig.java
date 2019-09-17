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

                        new SplashController().generateStickyFrame();

                        return;
                    }
                }

                System.out.println("new agent");
                new SplashController().generateStickyFrame();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("doneerror");
            }
        });
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
