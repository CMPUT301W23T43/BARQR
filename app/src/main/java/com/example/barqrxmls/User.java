package com.example.barqrxmls;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userName;
    private String password;


    User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public int compareTo(Object o) {
        User user = (User) o;
        return this.userName.compareTo(user.getUserName());
    }

}
