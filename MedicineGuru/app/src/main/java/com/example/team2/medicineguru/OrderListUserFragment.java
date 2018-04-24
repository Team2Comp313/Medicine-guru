package com.example.team2.medicineguru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import medicineguru.UtilityClasses.LoginSessionManager;
import medicineguru.databasehandler.FireBaseDatabaseHandler;
import medicineguru.dto.Order;
import medicineguru.dto.ShoppingCart;
import medicineguru.dto.ShoppingCartItem;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class OrderListUserFragment extends Fragment{

    // TODO: Rename and change types of parameters

    private View thisFragment;
    private OnFragmentInteractionListener mListener;
    List<Order> order_list;
    FireBaseDatabaseHandler db;
    private String userId;
    private LoginSessionManager loginInfo;
    //expandable variables....
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    public OrderListUserFragment()
    {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OrderListUserFragment newInstance(String param1, String param2) {
        OrderListUserFragment fragment = new OrderListUserFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public void prepareListData()
    {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("All Orders");
        listDataHeader.add("Processed");
        listDataHeader.add("In Process");

        // Adding child data
        List<String> allOrders = new ArrayList<String>();
        for(int i=0; i<order_list.size(); i++) {

            allOrders.add(order_list.get(i).getOrderId());
        }

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), allOrders); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
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
        thisFragment = inflater.inflate(R.layout.fragment_order_list_user, container, false);
        db = new FireBaseDatabaseHandler();
        db.getmFirebaseInstance().getReference();
        loginInfo = new LoginSessionManager(getContext());

        userId = loginInfo.getUserDetails().get("userId");
        Query query;
        if (loginInfo.getUserDetails().get("role").equals("admin")) {
            query = db.getmFirebaseInstance().getReference().child("OrderList");
        } else {
            query = db.getmFirebaseInstance().getReference().child("OrderList").orderByKey().equalTo(userId);
        }
        order_list = new ArrayList<Order>();
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Print Orders",dataSnapshot.toString() );
                if (dataSnapshot.exists()) {
                    Log.d("Print Orders",dataSnapshot.toString() );
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        HashMap<String, Order> orders = (HashMap<String, Order>) item.getValue();
                        for (Map.Entry<String, Order> thisOrder : orders.entrySet()) {
                            Order o=(Order)thisOrder.getValue();

                            order_list.add(o);

                            Log.d("Print Orders",order_list.toString() );
                        }

                    }
                }
                prepareListData();
                listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
                expListView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // get the listview
        expListView = (ExpandableListView) thisFragment.findViewById(R.id.lvExp);

        // preparing list data




        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        return thisFragment;
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
