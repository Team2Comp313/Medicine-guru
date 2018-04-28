package com.example.team2.medicineguru;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import medicineguru.UtilityClasses.LoginSessionManager;
import medicineguru.databasehandler.FireBaseDatabaseHandler;
import medicineguru.databasehandler.ShoppingCartManager;
import medicineguru.dto.Medicine;
import medicineguru.dto.ShoppingCart;
import medicineguru.dto.ShoppingCartItem;
import pl.droidsonroids.gif.GifTextView;
import pl.droidsonroids.gif.GifTextureView;


@SuppressLint("ShowToast")
public class ShoppingCartFragment extends Fragment  {
    private static final long MOVE_DEFAULT_TIME = 150;
    private static final long FADE_DEFAULT_TIME = 50;
    private OnCartChangeListener mListener;
    FireBaseDatabaseHandler db;
    LoginSessionManager loginInfo;
    private String userId;
    int count=0;
    boolean fragloaded=false;
    int totalCartItemCount =0;
    double totalCartValue = 0;
    View myFragmentView;
    final String[] qtyValues = {"1","2","3","4","5","6","7","8","9","10"};
    private ShoppingCartManager shoppingCart;
    List<ShoppingCartItem> cart_list;
    GifTextView loadingImg;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        db=new FireBaseDatabaseHandler();
        loadingImg=(GifTextView) myFragmentView.findViewById(R.id.loadingImg);
        cart_list = new ArrayList<>();
        fragloaded=true;
        loginInfo=new LoginSessionManager(getContext());
        if(!loginInfo.isLoggedIn())
        {
            TextView cartEmpty = (TextView) myFragmentView.findViewById(R.id.cart_empty);
            cartEmpty.setVisibility(myFragmentView.VISIBLE);
            loadingImg.setVisibility(myFragmentView.INVISIBLE);
        }
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
                        updateCartIcon(cart_list.size());
                        populateCart();
                        //setBadgeCount(getContext(),);

                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {


                if (dataSnapshot.exists()) {
                    cart_list=new ArrayList<ShoppingCartItem>();

                        ShoppingCart data = dataSnapshot.getValue(ShoppingCart.class);
                        for (Map.Entry<String, ShoppingCartItem> cartitem :data.getShoppingCartItems().entrySet())
                        {
                            cart_list.add(cartitem.getValue());
                        }
                    updateCartIcon(cart_list.size());
                    if (fragloaded)
                    {
                        populateCart();
                    }

                        //setBadgeCount(getContext(),);


                }
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //populateCart();

        return myFragmentView;
    }
    public void populateCart(){
        totalCartItemCount = cart_list.size();
        totalCartValue =0;
        int temp1=0;
        for (ShoppingCartItem item :cart_list)
        {

            totalCartValue = totalCartValue + (Double.parseDouble(item.getPrice())*Integer.parseInt(item.getQuantity()));
        }
        MainActivity activity = (MainActivity) getActivity();


        TextView itemText = (TextView) myFragmentView.findViewById(R.id.item_text);
        TextView itemCount = (TextView) myFragmentView.findViewById(R.id.item_count);
        TextView shippingText = (TextView) myFragmentView.findViewById(R.id.shipping_text);
        TextView shippingAmount = (TextView) myFragmentView.findViewById(R.id.shipping_amount);
        TextView totalAmount = (TextView) myFragmentView.findViewById(R.id.total_amount);
        Button checkout = (Button) myFragmentView.findViewById(R.id.checkout);
        ListView lv1=(ListView) myFragmentView.findViewById(R.id.listView1);
        TextView cartEmpty = (TextView) myFragmentView.findViewById(R.id.cart_empty);
        TextView subtotal = (TextView) myFragmentView.findViewById(R.id.subtotalTxt);
        loadingImg.setVisibility(myFragmentView.INVISIBLE);
        if (totalCartItemCount == 0)
        {
            itemText.setVisibility(myFragmentView.INVISIBLE);
            itemCount.setVisibility(myFragmentView.INVISIBLE);
            shippingText.setVisibility(myFragmentView.INVISIBLE);
            shippingAmount.setVisibility(myFragmentView.INVISIBLE);
            totalAmount.setVisibility(myFragmentView.INVISIBLE);
            checkout.setVisibility(myFragmentView.INVISIBLE);
            lv1.setVisibility(myFragmentView.INVISIBLE);
            cartEmpty.setVisibility(myFragmentView.VISIBLE);
            subtotal.setVisibility(myFragmentView.INVISIBLE);
        }

        else
        {
            itemText.setVisibility(myFragmentView.VISIBLE);
            itemCount.setVisibility(myFragmentView.VISIBLE);
            shippingText.setVisibility(myFragmentView.VISIBLE);
            shippingAmount.setVisibility(myFragmentView.VISIBLE);
            totalAmount.setVisibility(myFragmentView.VISIBLE);
            checkout.setVisibility(myFragmentView.VISIBLE);
            lv1.setVisibility(myFragmentView.VISIBLE);
            cartEmpty.setVisibility(myFragmentView.INVISIBLE);
            subtotal.setVisibility(myFragmentView.VISIBLE);
        }


        itemCount.setText("("+ totalCartItemCount + ")");

        if (totalCartValue>30)
        {
            shippingAmount.setText("Your order qualifies for Free shipping");
            totalAmount.setText("$ "+ totalCartValue);
        }
        else
        {
            shippingAmount.setText("$ 10");
            totalAmount.setText("$ "+ (totalCartValue+10));
        }
       /* if(activity!=null)
        {
            Typeface type= Typeface.createFromAsset(activity.getAssets(),"fonts/Roboto-Light.ttf");
            itemText.setTypeface(type);
            itemCount.setTypeface(type);
            shippingText.setTypeface(type);
            shippingAmount.setTypeface(type);
            totalAmount.setTypeface(type);
            checkout.setTypeface(type);

        }*/


        lv1.setAdapter(new custom_list_one(this.getActivity(),cart_list));

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Payment_Fragment pay = new Payment_Fragment();
               // FragmentManager fragmentManager = getFragmentManager();
               // fragmentManager.beginTransaction().add(R.id.pm_fragment, pay).addToBackStack(null).commit();
                performTransition(pay);
            }
        });

    }
    public static   void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

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
    class custom_list_one extends BaseAdapter
    {
        private LayoutInflater layoutInflater;
        ViewHolder holder;
        private List<ShoppingCartItem> cartList=new ArrayList<>();
        int cartCounter;
        Typeface type;
        Context context;

        public custom_list_one(Context context,List<ShoppingCartItem> cart_list) {
            layoutInflater = LayoutInflater.from(context);
            this.cartList=cart_list;
            this.cartCounter= cartList.size();
            this.context = context;
            type= Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Light.ttf");
        }

        @Override
        public int getCount() {

            return cartCounter;
        }

        @Override
        public Object getItem(int arg0) {

            return cartList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {

            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            ShoppingCartItem tempProduct = cart_list.get(position);


            if (convertView == null)
            {
                convertView = layoutInflater.inflate(R.layout.cartlist_customlayout, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.product_name);

                holder.product_mrpvalue = (TextView) convertView.findViewById(R.id.product_mrpvalue);
                holder.qty = (Spinner) convertView.findViewById(R.id.spinner1);
                holder.cancel = (ImageButton) convertView.findViewById(R.id.delete);
                holder.itemImg= (ImageView) convertView.findViewById(R.id.itemImg);


                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.name.setText(tempProduct.getName());
            //holder.name.setTypeface(type);

            //holder.product_mrp.setTypeface(type);


            Glide.with(context)
                    .load(tempProduct.getImagepath())
                    .into(holder.itemImg);
            holder.product_mrpvalue.setText("$"+tempProduct.getPrice());
           // holder.product_mrpvalue.setTypeface(type);


            ArrayAdapter<String> aa=new ArrayAdapter<String>(context,R.layout.qty_spinner,qtyValues);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            holder.qty.setAdapter(aa);

            holder.qty.setSelection(Integer.parseInt(tempProduct.getQuantity())-1);




            holder.cancel.setOnClickListener(new DeleteClickListener("button_delete",tempProduct));

            holder.qty.setOnItemSelectedListener(new OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView parent, View view,int selectionIndex, long id)
                {
                    //if user has changed the quantity, then save it in the DB. refresh cart_list

                   if ((parent.getSelectedItemPosition()+1) != Integer.parseInt(cart_list.get(position).getQuantity()))
                    {
                        shoppingCart.updateCartItem(cart_list.get(position).getCarttemId(),parent.getSelectedItemPosition()+1);
                        View parentView = (View) view.getParent().getParent().getParent().getParent().getParent().getParent().getParent();

                        TextView txtTotalAmount = (TextView) parentView.findViewById(R.id.total_amount);
                        TextView txtTotalItems = (TextView) parentView.findViewById(R.id.item_count);
                        TextView txtShippingAmt = (TextView) parentView.findViewById(R.id.shipping_amount);

                        totalCartItemCount = cart_list.size();
                        totalCartValue =0;

                        for (int temp1=0; temp1 < cart_list.size(); temp1++)
                        {
                            totalCartValue = totalCartValue + (Double.parseDouble(cart_list.get(temp1).getPrice())*(parent.getSelectedItemPosition()));
                        }

                        txtTotalItems.setText("("+ totalCartItemCount + ")");

                        if (totalCartValue> 30)
                        {
                            txtShippingAmt.setText("Your order qualifies for Free shipping");
                            txtTotalAmount.setText("$"+ totalCartValue);
                        }
                         else
                        {
                            txtShippingAmt.setText("$ 10");
                            txtTotalAmount.setText("$"+ (totalCartValue+10));
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView arg0)
                {

                }
            });

            return convertView;
        }
        class ViewHolder
        {
            TextView name;
            TextView product_mrp;
            TextView product_mrpvalue;
            ImageView itemImg;
            ImageButton cancel;
            Spinner qty;

        }

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCartChangeListener) {
            mListener = (OnCartChangeListener) context;
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

    public class DeleteClickListener implements OnClickListener {

        String button_name;
        ShoppingCartItem prod;
        int tempQty;
        int tempValue;

        public DeleteClickListener(String button_name, ShoppingCartItem prod) {
            this.prod = prod;
            this.button_name = button_name;
        }

        @Override
        public void onClick(View v) {

            if (button_name == "button_delete") {
                db.getNodeReference("ShoppingCarts").child(userId).child("shoppingCartItems").orderByChild("carttemId").equalTo(prod.getCarttemId())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap<String,HashMap<String,ShoppingCartItem>> shopCartList=(HashMap<String, HashMap<String,ShoppingCartItem>>) dataSnapshot.getValue();
                                if (dataSnapshot.exists()) {
                                    for (Map.Entry<String,HashMap<String,ShoppingCartItem>> cartitem : shopCartList.entrySet()) {
                                        db.getNodeReference("ShoppingCarts").child(userId).child("shoppingCartItems").child(cartitem.getKey()).removeValue();

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                View lView = (View) v.getParent().getParent().getParent();

                ((ListView) lView).setAdapter(new custom_list_one(getActivity(), cart_list));
                populateCart();


            }

        }
    }
    public interface OnCartChangeListener {

        void onCartChange(int i);
    }
    public void updateCartIcon(int id)
    {
        if(mListener!=null)
            mListener.onCartChange(id);
    }
    public class OnCheckOutClickListener implements OnClickListener
    {

        String button_name;
        ShoppingCartItem prod;
        int tempQty;
        int tempValue;

        public OnCheckOutClickListener(String button_name, ShoppingCartItem prod)
        {
            this.prod = prod;
            this.button_name = button_name;
        }

        @Override
        public void onClick(View v)
        {

            if (button_name == "button_delete")
            {

                shoppingCart.deleteCartItem(prod.getMedicineId());


                View lView = (View) v.getParent().getParent();

                ((ListView) lView).setAdapter(new custom_list_one(getActivity(),cart_list));


                TextView txtTotalAmount = (TextView) myFragmentView.findViewById(R.id.total_amount);
                TextView txtTotalItems = (TextView) myFragmentView.findViewById(R.id.item_count);
                TextView txtShippingAmt = (TextView) myFragmentView.findViewById(R.id.shipping_amount);
                TextView itemText = (TextView) myFragmentView.findViewById(R.id.item_text);
                TextView shippingText = (TextView) myFragmentView.findViewById(R.id.shipping_text);
                Button checkout = (Button) myFragmentView.findViewById(R.id.checkout);
                ListView lv1=(ListView) myFragmentView.findViewById(R.id.listView1);
                TextView cartEmpty = (TextView) myFragmentView.findViewById(R.id.cart_empty);

                totalCartItemCount = cart_list.size();
                totalCartValue =0;
                for (int temp1=0; temp1 < cart_list.size(); temp1++)
                {
                    totalCartValue = totalCartValue + Integer.parseInt(cart_list.get(temp1).getPrice());
                }

                txtTotalItems.setText("("+ totalCartItemCount + ")");

                if (totalCartValue > 30)
                {
                    txtShippingAmt.setText("Your order qualifies for Free shipping");
                    txtTotalAmount.setText("$ "+ totalCartValue);
                }
                else
                {
                    txtShippingAmt.setText("$ 10");
                    txtTotalAmount.setText("$ "+ (totalCartValue+10));
                }


                if (totalCartItemCount == 0)
                {
                    itemText.setVisibility(myFragmentView.INVISIBLE);
                    txtTotalItems.setVisibility(myFragmentView.INVISIBLE);
                    shippingText.setVisibility(myFragmentView.INVISIBLE);
                    txtShippingAmt.setVisibility(myFragmentView.INVISIBLE);
                    txtTotalAmount.setVisibility(myFragmentView.INVISIBLE);
                    checkout.setVisibility(myFragmentView.INVISIBLE);
                    lv1.setVisibility(myFragmentView.INVISIBLE);
                    cartEmpty.setVisibility(myFragmentView.VISIBLE);
                }

                else
                {
                    itemText.setVisibility(myFragmentView.VISIBLE);
                    txtTotalItems.setVisibility(myFragmentView.VISIBLE);
                    shippingText.setVisibility(myFragmentView.VISIBLE);
                    txtShippingAmt.setVisibility(myFragmentView.VISIBLE);
                    txtTotalAmount.setVisibility(myFragmentView.VISIBLE);
                    checkout.setVisibility(myFragmentView.VISIBLE);
                    lv1.setVisibility(myFragmentView.VISIBLE);
                    cartEmpty.setVisibility(myFragmentView.INVISIBLE);

                }

            }

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
}
