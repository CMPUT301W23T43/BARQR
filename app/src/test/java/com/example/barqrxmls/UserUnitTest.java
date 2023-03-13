package com.example.barqrxmls;

import com.google.errorprone.annotations.DoNotMock;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;

//@RunWith(MockitoJUnitRunner.class)

/**
 * tests the user class, currently does not run as we are unsure how to run tests with firestore
 */
public class UserUnitTest {
    User myUser;
    Code myCode;

    //@DoNotMock
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    //@DoNotMock
    CollectionReference userRef = database.collection("Users");


    //Note need to figure out how to mock firestore database
    @Test
    public void userCreationTest() {
        myUser = new User("me","2","email");

        //tests updateInDatabase
//        userRef.document("me").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    //if username already in use popup invalid username text
//                    assert(document.exists());
//                }
//            }
//        });

        assert(myUser.getUserName().equals("me"));
        assert(myUser.getId().equals("2"));
        assert(myUser.getCodes().isEmpty());
        assert(myUser.getTotalPoints() == 0);
        assert(myUser.getEmail().equals("email"));
        assert(myUser.getNumCodes() == 0);

    }

    @Test
    public void userAddCodeWithoutComment() {
        myCode = new Code("0c372b92d060f83c2af3a938b8ddd8ed8fc6cf8f20a9a23849e4ed7bc3a94f5f");
        myUser.addCode(myCode.getHash(),myCode.getPoints());
        assert(myUser.getCodes().get(myCode.getHash()).equals(""));
        //tests updateInDatabase
//        userRef.document("me").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    //if username already in use popup invalid username text
//                    HashMap test = (HashMap)document.get("codes");
//                    assert(test.get(myCode.getHash()).equals(""));
//                }
//            }
//        });
    }

    @Test
    public void userAddComment() {
        myUser.addComment(myCode.getHash(),"comment");
        assert(myUser.getCodes().get(myCode.getHash()).equals("comment"));
        //tests updateInDatabase
//        userRef.document("me").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    //if username already in use popup invalid username text
//                    HashMap test = (HashMap)document.get("codes");
//                    assert(test.get(myCode.getHash()).equals("comment"));
//                }
//            }
//        });
    }

    @Test
    public void userRemoveComment() {
        myUser.removeComment(myCode.getHash());
        assert(myUser.getCodes().get(myCode.getHash()).equals(""));
        //tests updateInDatabase
//        userRef.document("me").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    //if username already in use popup invalid username text
//                    HashMap test = (HashMap)document.get("codes");
//                    assert(test.get(myCode.getHash()).equals(""));
//                }
//            }
//        });
    }


    @Test
    public void userRemoveCode() {
        myUser.removeCode(myCode.getHash(),myCode.getPoints());
        assert(myUser.getCodes().isEmpty());
//        userRef.document("me").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    //if username already in use popup invalid username text
//                    HashMap test = (HashMap)document.get("codes");
//                    assert(test.isEmpty());
//                }
//            }
//        });

    }

    @Test
    public void userAddCodeWithComment() {
        myUser.addCode(myCode.getHash(),"comment",myCode.getPoints());
        assert(myUser.getCodes().get(myCode.getHash()).equals("comment"));
        //tests updateInDatabase
//        userRef.document("me").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    //if username already in use popup invalid username text
//                    HashMap test = (HashMap)document.get("codes");
//                    assert(test.get(myCode.getHash()).equals("comment"));
//                }
//            }
//        });
    }

    @Test
    public void userHasComment() {
        assert(myUser.hasComment(myCode.getHash()));
    }


}
