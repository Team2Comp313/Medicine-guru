package com.example.team2.medicineguru;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import medicineguru.UtilityClasses.LoginSessionManager;
import medicineguru.databasehandler.FireBaseDatabaseHandler;
import medicineguru.databasehandler.ShoppingCartManager;
import medicineguru.dto.Dose;
import medicineguru.dto.Image;
import medicineguru.dto.Medicine;
import medicineguru.dto.ShoppingCart;
import medicineguru.dto.ShoppingCartItem;
import medicineguru.dto.Symptom;
import android.app.FragmentManager;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements ShoppingCartFragment.OnCartChangeListener, NavigationView.OnNavigationItemSelectedListener {
    LoginSessionManager session;
    NavigationView navigationView;

    ProductListFragment pm_fragment;
    LayerDrawable icon;
    int cartcount = 0;
    FireBaseDatabaseHandler db;
    private static final long MOVE_DEFAULT_TIME = 150;
    private static final long FADE_DEFAULT_TIME = 50;
    TextView navText;

    //Empty_Fragment start_fragment;

    Menu nav_Menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new FireBaseDatabaseHandler();
        setContentView(R.layout.activity_main);
        LoginSessionManager loginInfo;
        session = new LoginSessionManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //start_fragment = new Empty_Fragment();
        pm_fragment = new ProductListFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.pm_fragment, pm_fragment).addToBackStack(null).commit();
        loginInfo = new LoginSessionManager(this);
        if (loginInfo.isLoggedIn()) {
            String userId = loginInfo.getUserDetails().get("userId");
            ShoppingCartManager shoppingCart = new ShoppingCartManager(userId);
            db.getmFirebaseInstance().getReference();


            Query query = db.getmFirebaseInstance().getReference().child("ShoppingCarts").orderByChild("userId").equalTo(userId);
            query.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (dataSnapshot.exists()) {
                        ArrayList<ShoppingCartItem> cart_list = new ArrayList<ShoppingCartItem>();
                        for (DataSnapshot item : dataSnapshot.getChildren()) {

                            ShoppingCart data = item.getValue(ShoppingCart.class);
                            for (Map.Entry<String, ShoppingCartItem> cartitem : data.getShoppingCartItems().entrySet()) {
                                cart_list.add(cartitem.getValue());
                            }
                        }
                        cartcount = cart_list.size();
                        setBadgeCount(getApplicationContext(), icon, Integer.toString(cartcount));
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            cartcount = 0;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeaderView = navigationView.getHeaderView(0);
        navText = navHeaderView.findViewById(R.id.nav_header_text);
        hideShowMenuItems();
        setUserNameInNavBar();
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void setUserNameInNavBar()
    {
        if(session.isLoggedIn())
        {
            String msg = session.getUserDetails().get("name");
            Toast.makeText(this,"Welcome "+msg,Toast.LENGTH_SHORT).show();
            navText.setText(msg);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        icon = (LayerDrawable) itemCart.getIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            ShoppingCartFragment shopingCartFragment = new ShoppingCartFragment();
            performTransition(shopingCartFragment);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        /// Toast.makeText(this, "The Mistbelt Forests", Toast.LENGTH_SHORT).show();
        int id = item.getItemId();
 
     if (id == R.id.login) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_share) {
            pm_fragment = new ProductListFragment();
            performTransition(pm_fragment);
        } else if (id == R.id.insert_medicine) {
             startActivity(new Intent(MainActivity.this, InsertMedicine.class));
             finish();
         }
        else if (id == R.id.login) {
             startActivity(new Intent(MainActivity.this, LoginActivity.class));
             finish();
         }else if (id == R.id.logout) {
            session.logoutUser();
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                            finish();
                        }
                    });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void hideShowMenuItems() {
       if(session.isLoggedIn()){
            if(session.getUserDetails().containsValue("brinderjitsingh30@gmail.com"))
            {
                navigationView.getMenu().findItem(R.id.insert_medicine).setVisible(true);
            }
            else{
                navigationView.getMenu().findItem(R.id.insert_medicine).setVisible(false);
            }
            navigationView.getMenu().findItem(R.id.logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
        } else {
            navigationView.getMenu().findItem(R.id.login).setVisible(true);
            navigationView.getMenu().findItem(R.id.logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.insert_medicine).setVisible(false);
        }
    }


    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;
        if (icon != null) {
            // Reuse drawable if possible
            Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
            if (reuse != null && reuse instanceof BadgeDrawable) {
                badge = (BadgeDrawable) reuse;
            } else {
                badge = new BadgeDrawable(context);
            }

            badge.setCount(count);
            icon.mutate();
            icon.setDrawableByLayerId(R.id.ic_badge, badge);
        }

    }

    private void performTransition(Fragment m) {
        if (isDestroyed()) {
            return;
        }
        FragmentManager mFragmentManager = getFragmentManager();
        Fragment previousFragment = mFragmentManager.findFragmentById(R.id.pm_fragment);
        Fragment nextFragment = m;

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // 1. Exit for Previous Fragment
        Fade exitFade = new Fade();
        exitFade.setDuration(FADE_DEFAULT_TIME);
        previousFragment.setExitTransition(exitFade);

        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
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
    public void onCartChange(int i) {
        setBadgeCount(this, icon, Integer.toString(i));
    }

}