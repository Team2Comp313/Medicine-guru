package com.example.team2.medicineguru;

/**
 * Created by ASPIRE on 11-03-2018.
 */
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
public class ProductView extends Fragment {
    private Context context;
    Product prdcd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         * Inflate the layout for this fragment
         */
        View view = inflater.inflate(R.layout.pm_fragment, container, false);
        view = inflater.inflate(R.layout.product_view, null);

        TextView description = (TextView) view.findViewById(R.id.description);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView price = (TextView) view.findViewById(R.id.price);
        ImageView image = (ImageView) view.findViewById(R.id.image);

        title.setText(getArguments().getString("title"));
        description.setText(getArguments().getString("desc"));
        price.setText("$" + getArguments().getString("price"));
        String abc = getArguments().getString("image");
        Resources res = getResources();
        int resID = res.getIdentifier(abc , "drawable", getActivity().getPackageName());
        image.setImageResource(resID);
        return view;
    }


}