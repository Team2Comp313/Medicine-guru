package medicineguru.dto;

import java.util.List;

/**
 * Created by Brinder Saini on 17/03/2018.
 */

public class ShoppingCartItem {
    private String carttemId;
    private String imagepath;
    private String  name;
    private String  medicineId;
    private String price;
    private String quantity;


    public ShoppingCartItem(String medicineId,String name ,String price, String quatity) {
        this.medicineId = medicineId;
        this.price = price;
        this.quantity = quatity;
        this.name=name;
    }

    public ShoppingCartItem() {
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getCarttemId() {
        return carttemId;
    }

    public void setCarttemId(String carttemId) {
        this.carttemId = carttemId;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
