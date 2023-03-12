package com.example.barqrxmls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CodeArrayAdapter extends ArrayAdapter<Code> {

    public CodeArrayAdapter(@NonNull Context context, ArrayList<Code> codes) {
        super(context,0, codes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.code_array_adapter,
                    parent, false);
        } else {
            view = convertView;
        }
        Code code = getItem(position);
        TextView codeName = view.findViewById(R.id.codeText);
        codeName.setText(code.getName());
        return view;
    }
}


