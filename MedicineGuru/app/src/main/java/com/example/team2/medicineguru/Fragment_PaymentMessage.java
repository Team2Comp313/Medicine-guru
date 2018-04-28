package com.example.team2.medicineguru;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Fragment_PaymentMessage extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_paymentmessage, container, false);
        TextView itemText = (TextView) view.findViewById(R.id.order_details);
        String transObj = getArguments().getString("serverObject");

        try {
            JSONObject obj = new JSONObject(transObj);
            String order_msg = "Transaction ID: "+obj.getString("id");
            itemText.setText(order_msg);

        } catch (Throwable tx) {
            Log.e("My App", "Could not parse malformed JSON");
        }

        return view;
    }
}
