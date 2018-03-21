package medicineguru.dto;

/**
 * Created by Brinder Saini on 19/03/2018.
 */

public class OrderItem {
    private String  medicineId;
    private String price;
    private String quantity;

    public OrderItem(String medicineId, String price, String quantity) {
        this.medicineId = medicineId;
        this.price = price;
        this.quantity = quantity;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
