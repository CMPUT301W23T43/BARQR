package com.example.barqrxmls;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ShowAllQrCodes extends AppCompatActivity {
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();;
    CollectionReference usersRef = dataBase.collection("Users");
    CollectionReference codesRef = dataBase.collection("Codes");
    private CodeArrayAdapter CodeAdapter;
    private ArrayList<Code> AllCodeDataList;
    ListView AllCodesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_qr_codes);
        AllCodesList = findViewById(R.id.codesList);
        AllCodeDataList = new ArrayList<>();
        Code testCode1 = new Code("/usr/code1");
        Code testCode2 = new Code(";lkajsdf");
        AllCodeDataList.add(testCode1);
        AllCodeDataList.add(testCode2);

        CodeAdapter = new CodeArrayAdapter(this, AllCodeDataList);
        AllCodesList.setAdapter(CodeAdapter);
        CodeAdapter.notifyDataSetChanged();

    }
}