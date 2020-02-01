package com.wsiz.wirtualny.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wsiz.wirtualny.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ArrayList<String> MessageslistOfString = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    CustomAdapter customAdapterr;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_aktualnosci, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        textView.setText("Wiadomości: ");

        customAdapterr = new CustomAdapter(MessageslistOfString, getContext());
        final ListView online_list = root.findViewById(R.id.online_list);
        online_list.setAdapter(customAdapterr);
        online_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int ll = (int) l;
                Toast.makeText(getContext(), "Wybrano element: "+ll, Toast.LENGTH_SHORT).show();
            }
        });
        MessageslistOfString.add("halo halo");
        MessageslistOfString.add("halo halo2");
        MessageslistOfString.add("halo halo3");
        customAdapterr.notifyDataSetChanged();

        return root;
    }
}