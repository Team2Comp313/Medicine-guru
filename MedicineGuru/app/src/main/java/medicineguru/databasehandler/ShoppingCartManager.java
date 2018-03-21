package medicineguru.databasehandler;

import java.util.ArrayList;
import java.util.List;

import medicineguru.dto.ShoppingCart;
import medicineguru.dto.ShoppingCartItem;

/**
 * Created by Brinder Saini on 19/03/2018.
 */

public class ShoppingCartManager {
    public ShoppingCart shoppingCart;
    private  String userId;
    final FireBaseDatabaseHandler db;
    final String node="ShoppingCarts";
    public ShoppingCartManager(List<ShoppingCartItem> shopCartItems,String userId){
        this.userId=userId;
        shoppingCart=new ShoppingCart(shopCartItems,userId);
        db=new FireBaseDatabaseHandler();
    }

    public ShoppingCartManager(String userId) {
        this.userId=userId;
        shoppingCart=new ShoppingCart();
        db=new FireBaseDatabaseHandler();
    }
    public void addCartItem(ShoppingCartItem item)
    {
       db.getNodeReference(node).child(userId).push().setValue(item);
    }
    public void deleteCartItem(String medicineId)
    {
        db.getNodeReference(node).child(userId).child(medicineId).removeValue();
    }
    public void updateCartItem(String medicineId,int value)
    {
        db.getNodeReference(node).child(userId).child(medicineId).child("quantity").setValue(value);
    }
}
