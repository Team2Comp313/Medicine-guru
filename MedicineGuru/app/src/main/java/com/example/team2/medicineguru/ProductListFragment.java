package com.example.team2.medicineguru;

/**
 * Created by Piyush Sharma on 09-03-2018.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import bannerslider.banners.Banner;
import bannerslider.banners.RemoteBanner;
import bannerslider.events.OnBannerClickListener;
import bannerslider.views.BannerSlider;
import bannerslider.views.indicators.IndicatorShape;

import medicineguru.UtilityClasses.LoginSessionManager;
import medicineguru.databasehandler.FireBaseDatabaseHandler;
import medicineguru.databasehandler.ShoppingCartManager;
import medicineguru.dto.Medicine;
import medicineguru.dto.ShoppingCartItem;

public class ProductListFragment extends Fragment {
    LoginSessionManager loginInfo;
    // Definig Array List - Object Type
    private ArrayList<Medicine> comp313_products = new ArrayList<>();
    private BannerSlider bannerSlider;
    View rootView;
    boolean search=false;
    private static final long MOVE_DEFAULT_TIME = 150;
    private static final long FADE_DEFAULT_TIME = 50;
    ShoppingCartManager cartManager;
    FireBaseDatabaseHandler FireDb;
    ListView productListView;
    private Context context;
    private String[] banners = {"","","sale_banner1","","sale_banner2","","sale_banner3"};
    private int bnrPos=0;
    public String search_item;
    Medicine prd;
    private String desc;
    List<Medicine> medicineList;
    EditText openSearchActivity;
    //custom ArrayAdapater
    class ProductsAdapter extends ArrayAdapter<Medicine>{
        private Context context;
        public List<Medicine> MedicineLists;


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
                if(search)
                {
                    rootView = LayoutInflater.from(getActivity()).inflate(R.layout.product_layout, parent, false);
                    setListItems(rootView,position);
                }
                else
                {
                    rootView = LayoutInflater.from(getActivity()).inflate(R.layout.productt_list_layout, parent, false);
                    setupViews();

                }

                // this.bannerRow = "true";

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
                Button addToCartBtn= (Button) view.findViewById(R.id.cart);
                setListItems(view,position);
            }
            return view;
        }
        public void setListItems(View view,int position)
        {
            Button addToCartBtn= (Button) view.findViewById(R.id.cart);
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
            price.setText(String.valueOf("$"+prd.getPrice()));



            new ImageDisplay(image).execute(prd.getImages().get(0));

            Button button1 = (Button) view.findViewById(R.id.view);
            addToCartBtn.setTag(position);
            addToCartBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(String.valueOf(v.getTag()));
                    prd = MedicineLists.get(pos);
                    ShoppingCartItem item=new ShoppingCartItem(prd.getTitle(),prd.getTitle(),prd.getPrice().toString(),"1");
                    item.setImagepath(prd.getImages().get(0));
                    item.setCarttemId(UUID.randomUUID().toString());
                    //args.putString("featured", String.valueOf(comp313_products.get(position).getFeatured()));
                    if(loginInfo.isLoggedIn())
                    {
                        cartManager.addCartItem(item,loginInfo.getUserDetails().get("userId"));
                        Snackbar.make(v, "Added to cart sucessfully", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else {
                        Snackbar.make(v, "Please login to add medicine to cart", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    // FragmentManager fragmentManager = getFragmentManager();
                    //  fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.pm_fragment,pv).commit();

                }
            });
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
                    args.putString("price",String.valueOf(prd.getPrice()));
                    args.putString("image", prd.getImages().get(0));
                    //args.putString("featured", String.valueOf(comp313_products.get(position).getFeatured()));
                    ProductView pv = new ProductView();
                    pv.setArguments(args);
                    performTransition(pv);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.pm_fragment, pv).commit();
                }
            });

        }
    }

    private void performTransition(Fragment m)
    {

        FragmentManager mFragmentManager=getFragmentManager();
        Fragment previousFragment = mFragmentManager.findFragmentById(R.id.pm_fragment);
        Fragment nextFragment = m;

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // 1. Exit for Previous Fragment
        Fade exitFade = new Fade();
        exitFade.setDuration(FADE_DEFAULT_TIME);
        previousFragment.setExitTransition(exitFade);

        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
        enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME);
        nextFragment.setSharedElementEnterTransition(enterTransitionSet);

        // 3. Enter Transition for New Fragment
        Fade enterFade = new Fade();
        enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
        enterFade.setDuration(FADE_DEFAULT_TIME);
        nextFragment.setEnterTransition(enterFade);
        fragmentTransaction.replace(R.id.pm_fragment, nextFragment);
        fragmentTransaction.addToBackStack(null).commitAllowingStateLoss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        /**
         * Inflate the layout for this fragment         */
        final View view = inflater.inflate(R.layout.product_list_fragment, container, false);
        medicineList=new ArrayList<Medicine>();
        /**
         * Author Deepak
         * Search Code or Search bar
         */
         openSearchActivity= (EditText) view.findViewById(R.id.editText);
        openSearchActivity.addTextChangedListener(new TextWatcher() {//listener for the text change in the edittext search bar
            public void onTextChanged(CharSequence s, int start, int before, int count) {//Search the text whenever change in the text
                if(count>0) {
                    search = true;//flag to display filtered product list or product list
                    ArrayList<Medicine> filteredmedicineList = new ArrayList<Medicine>();
                    EditText search = view.findViewById(R.id.editText);
                    search_item = search.getText().toString();
                    //filtering the product list based on the item to search
                    for (Medicine medicine : medicineList) {
                        if (medicine.getTitle().contains(search_item) || medicine.getDescription().contains(search_item)) {
                            filteredmedicineList.add(medicine);
                        }
                    }
                    setProductsAdapter(filteredmedicineList);//setting the adapter with the filtered list
                }
                //else statement to display the product list
                else{
                    search = false;
                    setProductsAdapter(medicineList);}
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {



            }
        });

       // ImageButton openSearchActivity = (ImageButton) view.findViewById(R.id.search_button);
       /* openSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = true;
                ArrayList<Medicine> filteredmedicineList=new ArrayList<Medicine>();
                EditText search= view.findViewById(R.id.editText);
                search_item=search.getText().toString();
                for(Medicine medicine:medicineList){
                    if(medicine.getTitle().contains(search_item)||medicine.getDescription().contains(search_item))
                    {
                        filteredmedicineList.add(medicine);
                    }
                }setProductsAdapter(filteredmedicineList);

            }
        });
*/

        //Find list view and bind it with the custom adapter
        productListView = view.findViewById(R.id.customListView);
        loginInfo=new LoginSessionManager(getContext());
        cartManager=new ShoppingCartManager(loginInfo.getUserDetails().get("userId"));
        //create our property elements
        FireDb = new FireBaseDatabaseHandler();
        Query query = FireDb.getmFirebaseInstance().getReference().child("Medicine");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot medicine : dataSnapshot.getChildren()) {
                        Medicine data = medicine.getValue(Medicine.class);
                        medicineList.add(data);
                        Log.d("inside main ","main");
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

                if (Build.VERSION.SDK_INT>=23) {
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
