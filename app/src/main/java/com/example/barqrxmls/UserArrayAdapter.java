//package com.example.barqrxmls;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import java.util.ArrayList;
//
//public class UserArrayAdapter extends ArrayAdapter<User> {
//
//    public UserArrayAdapter(@NonNull Context context, ArrayList<User> users) {
//        super(context,0, users);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
//            parent) {
//        View view;
//        if (convertView == null) {
//            view = LayoutInflater.from(getContext()).inflate(R.layout.user_array_adapter,
//                    parent, false);
//        } else {
//            view = convertView;
//        }
//
//        User user = getItem(position);
//        TextView username = view.findViewById(R.id.userText);
//        username.setText(user.getUserName());
//        return view;
//    }
//}
