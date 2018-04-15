package com.example.team2.medicineguru;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import medicineguru.UtilityClasses.LoginSessionManager;
import medicineguru.databasehandler.FireBaseDatabaseHandler;
import medicineguru.dto.Order;
import medicineguru.dto.ShoppingCart;
import medicineguru.dto.ShoppingCartItem;


public class OrderListUserFragment extends Fragment {

    // TODO: Rename and change types of parameters

    private View thisFragment;
    private OnFragmentInteractionListener mListener;
    List<Order> order_list;
    FireBaseDatabaseHandler db;
    private String userId;
    private LoginSessionManager loginInfo;

    public OrderListUserFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OrderListUserFragment newInstance(String param1, String param2) {
        OrderListUserFragment fragment = new OrderListUserFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisFragment=inflater.inflate(R.layout.fragment_order_list_user, container, false);
        db=new FireBaseDatabaseHandler();
        db.getmFirebaseInstance().getReference();
        loginInfo=new LoginSessionManager(getContext());

        userId=loginInfo.getUserDetails().get("userId");
        Query query;
        if(loginInfo.getUserDetails().get("role").equals("admin"))
        {
            query =  db.getmFirebaseInstance().getReference().child("Orders");
        }
        else {
            query =  db.getmFirebaseInstance().getReference().child("Orders").orderByChild("userId").equalTo(userId);
        }

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    order_list=new ArrayList<Order>();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        HashMap<String,Order> orders=(HashMap<String,Order>) item.getValue();
                        for (Map.Entry<String, Order> thisOrder :orders.entrySet())
                        {
                            order_list.add(thisOrder .getValue());
                        }



                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return thisFragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
