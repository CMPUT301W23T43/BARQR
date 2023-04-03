/**
* tests the user class, currently does not run as we are unsure how to run tests with firestore
*/

package com.example.barqrxmls;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;



public class UserUnitTest {

    static User myUser;
    static Code myCode;
    @BeforeAll
    static void setup() {
        myUser = new User("me","2","email");
        myCode = new Code("0c372b92d060f83c2af3a938b8ddd8ed8fc6cf8f20a9a23849e4ed7bc3a94f5f");
     }

//    FirebaseFirestore database = FirebaseFirestore.getInstance();
//    CollectionReference userRef = database.collection("Users");

    @Test
    public void userCreationTest() {
        // check if all getters and setters are correct
        assert(myUser.getId().equals("2"));
        assert(myUser.getUserName().equals("me"));
        assert(myUser.getCodes().isEmpty());
        assert(myUser.getTotalPoints() == 0);
        assert(myUser.getEmail().equals("email"));
        assert(myUser.getNumCodes() == 0);
    }

    @Test
    public void userAddAndRemoveCode() {
        // remove code if already there
        myUser.removeCode(myCode.getHash(),myCode.getPoints());

        // add new code
        myUser.addCode(myCode.getHash(),myCode.getPoints());
        // check if num codes and total points is correct
        assert(myUser.getNumCodes() == 1);
        assert(myUser.getTotalPoints() == myCode.getPoints());
        // check if all code information was correctly initialized
        assert(Objects.equals(myUser.getCodes().get(myCode.getHash()).get("geolocation"),""));
        assert(Objects.equals(myUser.getCodes().get(myCode.getHash()).get("comment"),""));
        assert(Objects.equals(myUser.getCodes().get(myCode.getHash()).get("image"),""));

        // remove the code and check if the number of codes and points is correct
        myUser.removeCode(myCode.getHash(),myCode.getPoints());
        assert(myUser.getCodes().isEmpty());
        assert(myUser.getNumCodes() == 0);
        assert(myUser.getTotalPoints() == 0);
  }

    @Test
    public void userAddAndRemoveComment() {
        // add a code if not already there
        myUser.addCode(myCode.getHash(),myCode.getPoints());
        // add a comment to the code
        myUser.addComment(myCode.getHash(),"comment");
        assert(myUser.getCodes().get(myCode.getHash()).get("comment").equals("comment"));
        // test hasComment method
        assert(myUser.hasComment(myCode.getHash()));
        // remove the comment and check if comment has been removed
        myUser.removeComment(myCode.getHash());
        assert(myUser.getCodes().get(myCode.getHash()).get("comment").equals(""));
        // test hasComment method
        assert(!myUser.hasComment(myCode.getHash()));
   }

    @Test
    public void userAddCodeWithGeolocation() {
        // remove code if already there
        myUser.removeCode(myCode.getHash(),myCode.getPoints());
        // add code with geolocation
        myUser.addCode(myCode.getHash(),"fake location",myCode.getPoints());
        //check num codes and total points is correct
        assert(myUser.getNumCodes() == 1);
        assert(myUser.getTotalPoints() == myCode.getPoints());
        //check if all code information was correctly initialized
        assert(Objects.equals(myUser.getCodes().get(myCode.getHash()).get("geolocation"),"fake location"));
        assert(Objects.equals(myUser.getCodes().get(myCode.getHash()).get("comment"),""));
        assert(Objects.equals(myUser.getCodes().get(myCode.getHash()).get("image"),""));
    }

    @Test
    public void addImage() {
        // add code if not already there
        myUser.addCode(myCode.getHash(),myCode.getPoints());
        //add image and check if it was correctly added
        byte[] b = new byte[0];
        myUser.addImage(myCode.getHash(),b);
        assert(Objects.equals(myUser.getCodes().get(myCode.getHash()).get("image"),b));
    }

}
