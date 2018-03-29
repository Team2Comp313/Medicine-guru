package medicineguru.dto;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Brinder Saini on 19/03/2018.
 */

public class ShoppingCart {
    private HashMap<String,ShoppingCartItem> shoppingCartItems;
    private String userId;

    public ShoppingCart() {
        shoppingCartItems=new HashMap<>();
    }

    public ShoppingCart(HashMap<String,ShoppingCartItem> shoppingCartItems, String userId) {
        this.shoppingCartItems = shoppingCartItems;
        this.userId = userId;
    }

    public HashMap<String,ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void setShoppingCartItems(HashMap<String,ShoppingCartItem> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }

    public String getUserId() {
        return userId;
    }
    public void addCartItem(ShoppingCartItem itm){
        shoppingCartItems.put(UUID.randomUUID().toString(),itm);
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
