package medicineguru.dto;

import java.util.List;

/**
 * Created by Brinder Saini on 17/03/2018.
 */

public class ShoppingCartItem {
    private String  medicineId;
    private String price;
    private String quantity;


    public ShoppingCartItem(String medicineId, String price, String quatity) {
        this.medicineId = medicineId;
        this.price = price;
        this.quantity = quatity;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getQuatity() {
        return quantity;
    }

    public void setQuatity(String quatity) {
        this.quantity = quatity;
    }





    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
