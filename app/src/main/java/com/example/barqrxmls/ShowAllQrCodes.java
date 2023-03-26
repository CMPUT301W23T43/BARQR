package com.example.barqrxmls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        Query getUsersByScores = codesRef.orderBy("points", Query.Direction.DESCENDING);

            // Retrieve all documents in the collection
        getUsersByScores.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Access the document data
                            Code obj = document.toObject(Code.class);
                            Log.d("MyActivity", obj.toString());
                            AllCodeDataList.add(obj);
                            CodeAdapter = new CodeArrayAdapter(ShowAllQrCodes.this, AllCodeDataList);
                            AllCodesList.setAdapter(CodeAdapter);
                            CodeAdapter.notifyDataSetChanged();
                        }
                    } else {
                        // Handle errors here
                        Log.e("MyActivity", "Error getting documents: " + task.getException().getMessage());
                    }
                }
            });

    }
}