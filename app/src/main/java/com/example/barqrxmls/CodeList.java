package com.example.barqrxmls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeList {


    private List<User> scannedBy = new ArrayList<>();

    /**
     *
     * @param user
     */
    public void addUser(User user){
        if (scannedBy.contains(user)){
            throw new IllegalArgumentException();
        }
        scannedBy.add(user);
    }

    public List<User> getCodes() {
        List<User> list = scannedBy;
        return list;
    }

    public boolean hasUser(User user){
        return scannedBy.contains(user);
    }

    public void delete(User user){
        if (scannedBy.contains(user)){
            scannedBy.remove(user);
        }else{
            throw new RuntimeException();
        }
    }

    public int countScans() {
        return scannedBy.size();
    }
}
