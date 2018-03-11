package com.example.team2.medicineguru;

/**
 * Created by ASPIRE on 09-03-2018.
 */

import android.app.Fragment;
import android.app.FragmentManager;
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

public class PM_Fragement extends Fragment {

    // Definig Array List - Object Type
    private ArrayList<Product> comp313_products = new ArrayList<>();

    //custom ArrayAdapater
    class propertyArrayAdapter extends ArrayAdapter<Product>{

        private Context context;
        private List<Product> productLists;

        //constructor, call on creation
        public propertyArrayAdapter(Context context, int resource, ArrayList<Product> objects) {
            super(context, resource, objects);
            this.context = context;
            this.productLists = objects;
        }

        //called when rendering the list
        public View getView(int position, View convertView, ViewGroup parent) {

            //get the property we are displaying
            Product prd = productLists.get(position);

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            //conditionally inflate either standard or special template
            View view;
            if(prd.getFeatured() == true){
                view = inflater.inflate(R.layout.product_layout_alt, null);
            }else{
                view = inflater.inflate(R.layout.product_layout, null);
            }


            TextView description = (TextView) view.findViewById(R.id.description);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView price = (TextView) view.findViewById(R.id.price);
            ImageView image = (ImageView) view.findViewById(R.id.image);

            //display trimmed excerpt for description
            int descriptionLength = prd.getDescription().length();
            if(descriptionLength >= 100){
                String descriptionTrim = prd.getDescription().substring(0, 100) + "...";
                description.setText(descriptionTrim);
            }else{
                description.setText(prd.getDescription());
            }

            //set price and rental attributes
            price.setText("$" + String.valueOf(prd.getPrice()));
            title.setText(String.valueOf(prd.getTitle()));
            //get the image associated with this property
            int imageID = context.getResources().getIdentifier(prd.getImage(), "drawable", context.getPackageName());
            image.setImageResource(imageID);


            return view;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment         */
        final View view = inflater.inflate(R.layout.pm_fragment, container, false);

        //Find list view and bind it with the custom adapter
        ListView listView = view.findViewById(R.id.customListView);
        //create our property elements
        comp313_products.add(new Product("Lorem Ipsum","Lorem Ipsum is simply dummy text of the printing and typesetting industry.", 360.00, "medicine_comp313_3", true));
        comp313_products.add(new Product("Lorem Ipsum","Lorem Ipsum is simply dummy text of the printing and typesetting industry.", 450.00, "medicine_comp313_1",false));
        comp313_products.add(new Product("Lorem Ipsum","Lorem Ipsum is simply dummy text of the printing and typesetting industry.", 360.00, "medicine_comp313_3", true));
        comp313_products.add(new Product("Lorem Ipsum", "Lorem Ipsum is simply dummy text of the printing and typesetting industry", 320.00, "medicine_comp313_2",false));
        comp313_products.add(new Product("Lorem Ipsum","Lorem Ipsum is simply dummy text of the printing and typesetting industry.", 360.00, "medicine_comp313_3", true));
        comp313_products.add(new Product("Lorem Ipsum", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", 360.00, "medicine_comp313_4" ,false));
        comp313_products.add(new Product("Lorem Ipsum", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", 360.00, "medicine_comp313_5" , false));
        comp313_products.add(new Product("Lorem Ipsum","Lorem Ipsum is simply dummy text of the printing and typesetting industry.", 360.00, "medicine_comp313_3", true));

        //create our new array adapter
        ArrayAdapter<Product> adapter = new propertyArrayAdapter(getActivity(), 0, comp313_products);
        listView.setAdapter(adapter);
        //add event listener so we can handle clicks
        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get the property we are displaying

                Bundle args = new Bundle();
                args.putString("title",comp313_products.get(position).getTitle());
                args.putString("desc",comp313_products.get(position).getDescription());
                args.putString("price", String.valueOf(comp313_products.get(position).getPrice()));
                args.putString("image",comp313_products.get(position).getImage());
                //args.putString("featured", String.valueOf(comp313_products.get(position).getFeatured()));

                ProductView pv = new ProductView();
                pv.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.pm_fragment,pv).commit();
            }
        };

        //set the listener to the list view
        listView.setOnItemClickListener(adapterViewListener);
        view.post(new Runnable() {
            @Override
            public void run() {
                int height=view.getMeasuredHeight(); // for instance
            }
        });
        return view;
    }
}