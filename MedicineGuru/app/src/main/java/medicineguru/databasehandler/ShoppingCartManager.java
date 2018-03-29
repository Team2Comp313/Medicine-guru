package medicineguru.databasehandler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import medicineguru.dto.Medicine;
import medicineguru.dto.ShoppingCart;
import medicineguru.dto.ShoppingCartItem;

/**
 * Created by Brinder Saini on 19/03/2018.
 */

public class ShoppingCartManager {
    public ShoppingCart shoppingCart;
    private  String userId;
    private int quantity=0;
    private ShoppingCartItem cartItem;
    final FireBaseDatabaseHandler db;
    String key;
    final String node="ShoppingCarts";
    public ShoppingCartManager(List<ShoppingCartItem> shopCartItems,String userId){
        this.userId=userId;
       // shoppingCart=new ShoppingCart(shopCartItems,userId);
        db=new FireBaseDatabaseHandler();
    }

    public ShoppingCartManager(String userId) {
        this.userId=userId;
        shoppingCart=new ShoppingCart();
        db=new FireBaseDatabaseHandler();
    }

    public ShoppingCartManager() {
        db=new FireBaseDatabaseHandler();
    }

    public void addCartItem(ShoppingCartItem item, String userId)
    {
      this.cartItem=item;
        this.userId=userId;
        Query query = db.getmFirebaseInstance().getReference().child(node).child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               List<ShoppingCart> shopCartList=new ArrayList<ShoppingCart>();
                if (dataSnapshot.exists()) {

                   addItem();
                }
                else
                {
                    createCart();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void createCart(){
        ShoppingCart cart=new ShoppingCart();
        cart.addCartItem(cartItem);
        cart.setUserId(userId);
        db.getNodeReference(node).child(userId).setValue(cart);
    }
    public void addItem(){
        db.getNodeReference(node).child(userId).child("shoppingCartItems").push().setValue(cartItem);
    }
    public void deleteCartItem(String cartItemId)
    {

        db.getNodeReference(node).child(userId).child("shoppingCartItems").orderByChild("carttemId").equalTo(cartItemId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String,HashMap<String,ShoppingCartItem>> shopCartList=(HashMap<String, HashMap<String,ShoppingCartItem>>) dataSnapshot.getValue();
                        if (dataSnapshot.exists()) {
                            for (Map.Entry<String,HashMap<String,ShoppingCartItem>> cartitem : shopCartList.entrySet()) {
                                db.getNodeReference(node).child(userId).child("shoppingCartItems").child(cartitem.getKey()).removeValue();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }
    public void updateCartItem(String cartItemId,int value)
    {
        quantity=value;
        db.getNodeReference(node).child(userId).child("shoppingCartItems").orderByChild("carttemId").equalTo(cartItemId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String,HashMap<String,ShoppingCartItem>> shopCartList=(HashMap<String, HashMap<String,ShoppingCartItem>>) dataSnapshot.getValue();
                        if (dataSnapshot.exists()) {
                            for (Map.Entry<String,HashMap<String,ShoppingCartItem>> cartitem : shopCartList.entrySet()) {
                                    key=cartitem.getKey();
                                updateQuantity();


                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
    public void updateQuantity()
    {
        db.getNodeReference(node).child(userId).child("shoppingCartItems").child(key).child("quantity").setValue(Integer.toString(quantity));
    }
}
