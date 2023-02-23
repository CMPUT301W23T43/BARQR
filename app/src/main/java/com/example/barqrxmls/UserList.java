package com.example.barqrxmls;

import java.util.ArrayList;
import java.util.List;

public class UserList {


    private List<Code> scannedCodes = new ArrayList<>();


    public void addCode(Code code){
        if (scannedCodes.contains(code)){
            throw new IllegalArgumentException();
        }
        scannedCodes.add(code);
    }

    public List<Code> getCodes() {
        List<Code> list = scannedCodes;
        return list;
    }

    public boolean hasCode(Code code) {
        return scannedCodes.contains(code);
    }

    public void delete(Code code) {
        if(scannedCodes.contains(code)){
            scannedCodes.remove(code);
        }else{
            throw new RuntimeException();
        }
    }

    public int countCodes(){
        return scannedCodes.size();
    }
}
