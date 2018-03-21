package medicineguru.dto;

import java.util.List;

/**
 * Created by Brinder Saini on 19/03/2018.
 */

public class ShoppingCart {
    private List<ShoppingCartItem> shoppingCartItems;
    private String userId;

    public ShoppingCart() {
    }

    public ShoppingCart(List<ShoppingCartItem> shoppingCartItems, String userId) {
        this.shoppingCartItems = shoppingCartItems;
        this.userId = userId;
    }

    public List<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void setShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
