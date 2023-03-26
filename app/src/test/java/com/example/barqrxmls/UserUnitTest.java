/**
* tests the user class, currently does not run as we are unsure how to run tests with firestore
*/

//Acknowledgements:
//  how to mock context -->
//      Source: https://stackoverflow.com/questions/34063848/how-to-mock-context-using-mockito
//      Author: Sipty https://stackoverflow.com/users/2796939/sipty
//  using mockito with firestore -->
//      Source: https://stackoverflow.com/questions/43225804/junit-testing-in-android-studio-with-firebase
//      Author: Petr Šabata https://stackoverflow.com/users/5011335/petr-%c5%a0abata


//package com.example.barqrxmls;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import android.content.Context;
//
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//
//import com.google.firebase.database.DatabaseReference;
//import org.powermock.api.mockito.PowerMockito;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
////import org.junit.runner.RunWith;
//
//
////@RunWith(MockitoJUnitRunner.class)
//

//public class UserUnitTest {
//    User myUser;
//    Code myCode;
//
//    FirebaseFirestore database = FirebaseFirestore.getInstance();
//    CollectionReference userRef = database.collection("Users");
//
//    //@Mock
//    //int mypid = android.os.Process.myPid();
//    //@DoNotMock
////
//    //@BeforeAll
////    @PrepareForTest(FirebaseDatabase.class)
//    //static void Before() {
//        //Context context = mock(Context.class);
//        //FirebaseApp.initializeApp(context);
//        //DatabaseReference mockedDatabaseReference = Mockito.mock(DatabaseReference.class);
//        //FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
//        //when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);
//        //PowerMockito.mockStatic(FirebaseDatabase.class);
//        //when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);
//    //}
//
//    //Note need to figure out how to mock firestore database
//    @Test
//    public void userCreationTest() {
//        myUser = new User("me","2","email");
//
//        //tests updateInDatabase
////        userRef.document("me").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                if(task.isSuccessful()) {
////                    DocumentSnapshot document = task.getResult();
////                    //if username already in use popup invalid username text
////                    assert(document.exists());
////                }
////            }
////        });
//
//        assert(myUser.getUserName().equals("me"));
//        assert(myUser.getId().equals("2"));
//        assert(myUser.getCodes().isEmpty());
//        assert(myUser.getTotalPoints() == 0);
//        assert(myUser.getEmail().equals("email"));
//        assert(myUser.getNumCodes() == 0);
//
//    }
//
//    @Test
//    public void userAddCodeWithoutComment() {
//        myCode = new Code("0c372b92d060f83c2af3a938b8ddd8ed8fc6cf8f20a9a23849e4ed7bc3a94f5f");
//        myUser.addCode(myCode.getHash(),myCode.getPoints());
//        assert(myUser.getCodes().get(myCode.getHash()).equals(""));
//        //tests updateInDatabase
////        userRef.document("me").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                if(task.isSuccessful()) {
////                    DocumentSnapshot document = task.getResult();
////                    //if username already in use popup invalid username text
////                    HashMap test = (HashMap)document.get("codes");
////                    assert(test.get(myCode.getHash()).equals(""));
////                }
////            }
////        });
//    }
//
//    @Test
//    public void userAddComment() {
//        myUser.addComment(myCode.getHash(),"comment");
//        assert(myUser.getCodes().get(myCode.getHash()).equals("comment"));
//        //tests updateInDatabase
////        userRef.document("me").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                if(task.isSuccessful()) {
////                    DocumentSnapshot document = task.getResult();
////                    //if username already in use popup invalid username text
////                    HashMap test = (HashMap)document.get("codes");
////                    assert(test.get(myCode.getHash()).equals("comment"));
////                }
////            }
////        });
//    }
//
//    @Test
//    public void userRemoveComment() {
//        myUser.removeComment(myCode.getHash());
//        assert(myUser.getCodes().get(myCode.getHash()).equals(""));
//        //tests updateInDatabase
////        userRef.document("me").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                if(task.isSuccessful()) {
////                    DocumentSnapshot document = task.getResult();
////                    //if username already in use popup invalid username text
////                    HashMap test = (HashMap)document.get("codes");
////                    assert(test.get(myCode.getHash()).equals(""));
////                }
////            }
////        });
//    }
//
//
//    @Test
//    public void userRemoveCode() {
//        myUser.removeCode(myCode.getHash(),myCode.getPoints());
//        assert(myUser.getCodes().isEmpty());
////        userRef.document("me").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                if(task.isSuccessful()) {
////                    DocumentSnapshot document = task.getResult();
////                    //if username already in use popup invalid username text
////                    HashMap test = (HashMap)document.get("codes");
////                    assert(test.isEmpty());
////                }
////            }
////        });
//
//    }
//
//    @Test
//    public void userAddCodeWithComment() {
//        myUser.addCode(myCode.getHash(),"comment",myCode.getPoints());
//        assert(myUser.getCodes().get(myCode.getHash()).equals("comment"));
//        //tests updateInDatabase
////        userRef.document("me").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                if(task.isSuccessful()) {
////                    DocumentSnapshot document = task.getResult();
////                    //if username already in use popup invalid username text
////                    HashMap test = (HashMap)document.get("codes");
////                    assert(test.get(myCode.getHash()).equals("comment"));
////                }
////            }
////        });
//    }
//
//    @Test
//    public void userHasComment() {
//        assert(myUser.hasComment(myCode.getHash()));
//    }
//
//
//}
