// Created By Piyush Sharma - 20-04-2018
package com.example.team2.medicineguru;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardMultilineWidget;
import com.stripe.android.model.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import medicineguru.UtilityClasses.LoginSessionManager;
import medicineguru.databasehandler.FireBaseDatabaseHandler;
import medicineguru.databasehandler.ShoppingCartManager;
import medicineguru.dto.ShoppingCart;
import medicineguru.dto.ShoppingCartItem;
import payments.PayviaStripe;

public class Payment_Fragment extends Fragment {

    public CardMultilineWidget  mCardInputWidget;
    //private Context mContext;
    private static final String FUNCTIONAL_PUBLISHABLE_KEY = "pk_test_9fptvt5ZIxoFqnpS8dh0oWti";
    private Stripe stripe;
    private static final long MOVE_DEFAULT_TIME = 150;
    private static final long FADE_DEFAULT_TIME = 50;
    private ShoppingCartManager shoppingCart;
    List<ShoppingCartItem> cart_list;
    private double totalCartValue = 0;
    private String userId;
    LoginSessionManager loginInfo;
    FireBaseDatabaseHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.payment_fragment_layout, container, false);
        loginInfo=new LoginSessionManager(getContext());

        mCardInputWidget = (CardMultilineWidget) view.findViewById(R.id.card_multiline_widget);
        Button paynow = (Button) view.findViewById(R.id.paynow);
        db=new FireBaseDatabaseHandler();

        userId=loginInfo.getUserDetails().get("userId");
        shoppingCart=new ShoppingCartManager(userId);
        db.getmFirebaseInstance().getReference();

        Query query =  db.getmFirebaseInstance().getReference().child("ShoppingCarts").orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    cart_list=new ArrayList<ShoppingCartItem>();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {

                        ShoppingCart data = item.getValue(ShoppingCart.class);
                        for (Map.Entry<String, ShoppingCartItem> cartitem :data.getShoppingCartItems().entrySet())
                        {
                            cart_list.add(cartitem.getValue());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card cardToSave = mCardInputWidget.getCard();
                try{
                    if (cardToSave != null) {
                        Card card = new Card(cardToSave.getNumber(), cardToSave.getExpMonth(), cardToSave.getExpYear(), cardToSave.getCVC());
                        // Remember to validate the card object before you use it to save time.
                        if (!card.validateCard()) {
                            Toast.makeText(getActivity(),"Invalid Card Data Please Try Again",Toast.LENGTH_LONG).show();
                        }else{
                            Stripe stripe = new Stripe(getActivity(), FUNCTIONAL_PUBLISHABLE_KEY);
                            stripe.createToken(
                                    card,
                                    new TokenCallback() {
                                        public void onSuccess(Token token) {
                                            // Send token to your server
                                            Toast.makeText(getActivity(),token.getId(),Toast.LENGTH_LONG).show();
                                            for (ShoppingCartItem item :cart_list)
                                            {

                                                totalCartValue = totalCartValue + (Double.parseDouble(item.getPrice())*Integer.parseInt(item.getQuantity()));
                                            }

                                            if(totalCartValue < 30){
                                                totalCartValue = totalCartValue + 10;
                                            }

                                            totalCartValue = totalCartValue * 100;

                                            PayviaStripe strpay = new PayviaStripe(token.getId(),totalCartValue);
                                            String resp = strpay.sendFormParms();
                                            //Log.e("response", resp);

                                            Bundle args = new Bundle();
                                            args.putString("serverObject", resp);
                                            //args.putString("featured", String.valueOf(comp313_products.get(position).getFeatured()));
                                            Fragment_PaymentMessage fpm = new Fragment_PaymentMessage();
                                            fpm.setArguments(args);
                                            performTransition(fpm);
                                            FragmentManager fragmentManager = getFragmentManager();
                                            fragmentManager.beginTransaction().replace(R.id.pm_fragment, fpm).commit();

                                        }
                                        public void onError(Exception error) {
                                            // Show localized error message
                                            Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );
                        }
                    }else{
                        Toast.makeText(getActivity(),"Invalid Card Data",Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    Log.d("Exception", e.getMessage());
                    Toast.makeText(getActivity(),"Exception appears check log",Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
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
}
