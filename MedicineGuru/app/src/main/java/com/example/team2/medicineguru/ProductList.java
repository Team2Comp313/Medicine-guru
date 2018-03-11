package com.example.team2.medicineguru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
public class ProductList extends AppCompatActivity {

    // Definig Array List - Object Type
    private ArrayList<Property> comp313_products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        //Find list view and bind it with the custom adapter
        ListView listView = findViewById(R.id.customListView);
        //create our property elements
        comp313_products.add(new Property("Lorem Ipsum","Lorem Ipsum is simply dummy text of the printing and typesetting industry.", 450.00, "medicine_comp313_1",false));
        comp313_products.add(new Property("Lorem Ipsum", "Lorem Ipsum is simply dummy text of the printing and typesetting industry", 320.00, "medicine_comp313_2",false));
        comp313_products.add(new Property("Lorem Ipsum","Lorem Ipsum is simply dummy text of the printing and typesetting industry.", 360.00, "medicine_comp313_3", true));
        comp313_products.add(new Property("Lorem Ipsum", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", 360.00, "medicine_comp313_4" ,false));
        comp313_products.add(new Property("Lorem Ipsum", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", 360.00, "medicine_comp313_5" , false));
        //create our new array adapter
        ArrayAdapter<Property> adapter = new propertyArrayAdapter(this, 0, comp313_products);
        listView.setAdapter(adapter);
        //add event listener so we can handle clicks
        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Property property = comp313_products.get(position);

                Intent intent = new Intent(ProductList.this,DetailActivity.class);
                intent.putExtra("title", property.getTitle());
                intent.putExtra("description", property.getDescription());
                intent.putExtra("price", property.getPrice());
                intent.putExtra("image", property.getImage());
                startActivityForResult(intent, 1000);
            }
        };
        //set the listener to the list view
       listView.setOnItemClickListener(adapterViewListener);
    }

    //custom ArrayAdapater
    class propertyArrayAdapter extends ArrayAdapter<Property>{

        private Context context;
        private List<Property> rentalProperties;

        //constructor, call on creation
        public propertyArrayAdapter(Context context, int resource, ArrayList<Property> objects) {
            super(context, resource, objects);

            this.context = context;
            this.rentalProperties = objects;
        }

        //called when rendering the list
        public View getView(int position, View convertView, ViewGroup parent) {

            //get the property we are displaying
            Property property = rentalProperties.get(position);

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            //conditionally inflate either standard or special template
            View view;
            if(property.getFeatured() == true){
                view = inflater.inflate(R.layout.property_layout_alt, null);
            }else{
                view = inflater.inflate(R.layout.property_layout, null);
            }


            TextView description = (TextView) view.findViewById(R.id.description);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView price = (TextView) view.findViewById(R.id.price);
            ImageView image = (ImageView) view.findViewById(R.id.image);

            //display trimmed excerpt for description
            int descriptionLength = property.getDescription().length();
            if(descriptionLength >= 100){
                String descriptionTrim = property.getDescription().substring(0, 100) + "...";
                description.setText(descriptionTrim);
            }else{
                description.setText(property.getDescription());
            }

            //set price and rental attributes
            price.setText("$" + String.valueOf(property.getPrice()));
            title.setText(String.valueOf(property.getTitle()));
            //get the image associated with this property
            int imageID = context.getResources().getIdentifier(property.getImage(), "drawable", context.getPackageName());
            image.setImageResource(imageID);
            return view;
        }
    }
}
