package com.example.team2.medicineguru;

/**
 * Created by Piyush Sharma on 09-03-2018.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bannerslider.banners.Banner;
import bannerslider.banners.RemoteBanner;
import bannerslider.events.OnBannerClickListener;
import bannerslider.views.BannerSlider;
import bannerslider.views.indicators.IndicatorShape;

import medicineguru.databasehandler.FireBaseDatabaseHandler;
import medicineguru.dto.Medicine;

public class Front_Fragement extends Fragment {

    // Definig Array List - Object Type
    private ArrayList<Medicine> comp313_products = new ArrayList<>();
    private BannerSlider bannerSlider;
    View rootView;
    FireBaseDatabaseHandler FireDb;
    ListView productListView;
    private Context context;
    private String[] banners = {"","","sale_banner1","","sale_banner2","","sale_banner3"};
   // private int bnrPos=0;
    //custom ArrayAdapater
    class ProductsAdapter extends ArrayAdapter<Medicine>{
        private Context context;
        private List<Medicine> MedicineLists;
        private String desc;
        Medicine prd;
        //private String bannerRow;
        //constructor, call on creation
        public ProductsAdapter(Context context, int resource, List<Medicine> objects) {
            super(context, resource, objects);
            this.context = context;
            this.MedicineLists = objects;
            //this.bannerRow = "false";
        }

        //called when rendering the list & customising the list
        public View getView(int position, View convertView, ViewGroup parent) {
            //get the property we are displaying
            prd = MedicineLists.get(position);
            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            //conditionally inflate either standard or special template
            View view;
            //Log.w("myApp", bannerRow);
            //Log.w("myApp", Integer.toString(position));
            if(position==0){
                rootView = LayoutInflater.from(getActivity()).inflate(R.layout.frontbanner_fragement, parent, false);
               // this.bannerRow = "true";
                setupViews();
                view = rootView;
                //getView(0,convertView,parent);
            }else {
                if (position == 2 || position == 4 || position == 6){
                    view = inflater.inflate(R.layout.small_banner, null);
                    ImageView image = (ImageView) view.findViewById(R.id.imageView2);
                    int imageID = context.getResources().getIdentifier(banners[position], "drawable", context.getPackageName());
                    image.setImageResource(imageID);
                } else {
                    view = inflater.inflate(R.layout.product_layout, null);
                }

                TextView description = (TextView) view.findViewById(R.id.description);
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView price = (TextView) view.findViewById(R.id.price);
                ImageView image = (ImageView) view.findViewById(R.id.image);

                //display trimmed excerpt for description
                int descriptionLength = prd.getDescription().length();
                if (descriptionLength >= 100) {
                    desc = prd.getDescription().substring(0, 100) + "...";
                    description.setText(desc);
                } else {
                    description.setText(prd.getDescription());
                }
                title.setText(String.valueOf(prd.getTitle()));
                price.setText(String.valueOf(prd.getPrice()));

                //get the image associated with this property
                final int imageID = context.getResources().getIdentifier("medicine_comp313_3", "drawable", context.getPackageName());
                image.setImageResource(imageID);

                Button button1 = (Button) view.findViewById(R.id.view);

                // Passing the position variable on button click
                button1.setTag(position);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(String.valueOf(v.getTag()));
                        prd = MedicineLists.get(pos);
                        Bundle args = new Bundle();
                        args.putString("title", prd.getTitle());
                        args.putString("desc", prd.getDescription());
                        args.putString("price", String.valueOf(prd.getPrice()));
                        args.putString("image", "medicine_comp313_3");
                        //args.putString("featured", String.valueOf(comp313_products.get(position).getFeatured()));
                        ProductView pv = new ProductView();
                        pv.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.pm_fragment, pv).commit();
                    }
                });
            }
            return view;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment         */
        final View view = inflater.inflate(R.layout.home_fragment, container, false);
        //Find list view and bind it with the custom adapter
        productListView = view.findViewById(R.id.customListView);

        //create our property elements
        FireDb = new FireBaseDatabaseHandler();
        Query query = FireDb.getmFirebaseInstance().getReference().child("Medicine");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Medicine> medicineList=new ArrayList<Medicine>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot medicine : dataSnapshot.getChildren()) {
                        Medicine data = medicine.getValue(Medicine.class);
                        medicineList.add(data);
                    }
                    setProductsAdapter(medicineList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {     }
        });


        //create our new array adapter
        //add event listener so we can handle clicks
        view.post(new Runnable() {
            @Override
            public void run() {
                int height=view.getMeasuredHeight(); // for instance
            }
        });
        return view;
    }


    // Setting product adapter object and assigning the product list
    public void setProductsAdapter(List<Medicine> mlist){
        ArrayAdapter<Medicine> adapter = new ProductsAdapter(getActivity(), 0, mlist);
        productListView.setAdapter(adapter);
    }

    private void setupViews() {
        //setupToolbar();
        setupBannerSlider();
        // setupPageIndicatorChooser();
        //setupSettingsUi();
    }

    private void setupSettingsUi() {
        final SeekBar intervalSeekBar=(SeekBar) this.rootView.findViewById(R.id.seekbar_interval);
        intervalSeekBar.setMax(10000);
        intervalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    bannerSlider.setInterval(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {  }
        });

        SeekBar indicatorSizeSeekBar=(SeekBar) this.rootView.findViewById(R.id.seekbar_indicator_size);
        indicatorSizeSeekBar.setMax(getResources().getDimensionPixelSize(R.dimen.max_slider_indicator_size));
        indicatorSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    bannerSlider.setIndicatorSize(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {    }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {    }
        });

        SwitchCompat loopSlidesSwitch=(SwitchCompat) this.rootView.findViewById(R.id.checkbox_loop_slides);
        loopSlidesSwitch.setChecked(true);
        SwitchCompat mustAnimateIndicators=(SwitchCompat) this.rootView.findViewById(R.id.checkbox_animate_indicators);
        mustAnimateIndicators.setChecked(true);

        loopSlidesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bannerSlider.setLoopSlides(b);
            }
        });

        mustAnimateIndicators.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bannerSlider.setMustAnimateIndicators(b);
            }
        });

        SwitchCompat hideIndicatorsSwitch=(SwitchCompat) this.rootView.findViewById(R.id.checkbox_hide_indicators);
        hideIndicatorsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bannerSlider.setHideIndicators(b);
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar)  this.rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ImageView githubSourceImageView = (ImageView)  this.rootView.findViewById(R.id.image_github);
        githubSourceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://github.com/saeedsh92/Banner-Slider");

                if (Build.VERSION.SDK_INT>15) {
                    CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                    intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    intentBuilder.setStartAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out);
                    intentBuilder.setExitAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out);
                    CustomTabsIntent customTabsIntent = intentBuilder.build();
                    customTabsIntent.launchUrl(context, uri);
                }else {
                    startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW,uri),"Choose Browser..."));
                }

            }
        });
    }

    private void setupBannerSlider(){
        bannerSlider = (BannerSlider)  this.rootView.findViewById(R.id.banner_slider1);
        addBanners();

        bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int position) {
        // Toast.makeText(MainActivity.this, "Banner with position " + String.valueOf(position) + " clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBanners(){
        List<Banner> remoteBanners=new ArrayList<>();
        //Add banners using image urls
        remoteBanners.add(new RemoteBanner(
                "https://dl.dropboxusercontent.com/s/059deuvz54b6i0j/medicine-cabinet.jpeg"
        ));
        remoteBanners.add(new RemoteBanner(
                "https://dl.dropboxusercontent.com/s/hn139h3b8ddbex2/Placebo_Effect_Max_Strength_package.jpeg"
        ));
        remoteBanners.add(new RemoteBanner(
                "https://dl.dropboxusercontent.com/s/5phzg5pwyx4n35n/Placebos.jpg"
        ));
        remoteBanners.add(new RemoteBanner(
                "https://dl.dropboxusercontent.com/s/x54ewtw9f0qchrx/syper.jpg"
        ));
        remoteBanners.add(new RemoteBanner(
                "https://dl.dropboxusercontent.com/s/maxtyzgtrwn4ckl/web-service.PNG"
        ));

        bannerSlider.setBanners(remoteBanners);

    }

    private void setupPageIndicatorChooser(){
        String[] pageIndicatorsLabels= getResources().getStringArray(R.array.page_indicators);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,
                pageIndicatorsLabels
        );


        Spinner spinner = (Spinner)  this.rootView.findViewById(R.id.spinner_page_indicator);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        bannerSlider.setDefaultIndicator(IndicatorShape.CIRCLE);
                        break;
                    case 1:
                        bannerSlider.setDefaultIndicator(IndicatorShape.DASH);
                        break;
                    case 2:
                        bannerSlider.setDefaultIndicator(IndicatorShape.ROUND_SQUARE);
                        break;
                    case 3:
                        bannerSlider.setDefaultIndicator(IndicatorShape.SQUARE);
                        break;
                    case 4:
                        bannerSlider.setCustomIndicator(VectorDrawableCompat.create(getResources(),
                                R.drawable.selected_slide_indicator, null),
                                VectorDrawableCompat.create(getResources(),
                                        R.drawable.unselected_slide_indicator, null));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}