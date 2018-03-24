package com.example.team2.medicineguru;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import bannerslider.banners.Banner;
import bannerslider.banners.RemoteBanner;
import bannerslider.events.OnBannerClickListener;
import bannerslider.views.BannerSlider;
import bannerslider.views.indicators.IndicatorShape;



public class Empty_Fragment extends Fragment {
    Front_Fragement pm_fragment;
    private BannerSlider bannerSlider;
    private Context context;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
       rootView = LayoutInflater.from(getActivity()).inflate(R.layout.frontbanner_fragement, container, false);
        setupViews();
       // home_fragment = new Front_Fragement();
        //FragmentManager fragmentManager = getFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.pm_fragmentchild, home_fragment).commit();
        return rootView;
    }


    private void setupViews() {
        //setupToolbar();
        setupBannerSlider();
       // setupPageIndicatorChooser();
        setupSettingsUi();
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
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
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
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
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
