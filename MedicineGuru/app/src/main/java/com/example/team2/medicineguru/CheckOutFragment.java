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
import java.util.List;
import java.util.Map;

import medicineguru.UtilityClasses.LoginSessionManager;
import medicineguru.databasehandler.FireBaseDatabaseHandler;
import medicineguru.dto.Order;
import medicineguru.dto.ShoppingCart;
import medicineguru.dto.ShoppingCartItem;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckOutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckOutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckOutFragment extends Fragment {
    List<ShoppingCartItem> cart_list;
    FireBaseDatabaseHandler db;
    View myFragmentView;
    private String userId;
    private LoginSessionManager loginInfo;
    private OnFragmentInteractionListener mListener;

    public CheckOutFragment() {
        // Required empty public constructor
    }


    public static CheckOutFragment newInstance(String param1, String param2) {
        CheckOutFragment fragment = new CheckOutFragment();
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
        myFragmentView= inflater.inflate(R.layout.fragment_check_out, container, false);

        db=new FireBaseDatabaseHandler();
        db.getmFirebaseInstance().getReference();
        loginInfo=new LoginSessionManager(getContext());
        userId=loginInfo.getUserDetails().get("userId");
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
        return myFragmentView;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
