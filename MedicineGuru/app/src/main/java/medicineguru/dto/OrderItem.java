package medicineguru.dto;

/**
 * Created by Brinder Saini on 19/03/2018.
 */

public class OrderItem {


    private String ordertemId;
    private String imagepath;
    private String  name;
    private String  medicineId;
    private String price;
    private String quantity;



    public OrderItem(String imagepath, String name, String medicineId, String price, String quantity) {
        this.imagepath = imagepath;
        this.name = name;
        this.medicineId = medicineId;
        this.price = price;
        this.quantity = quantity;
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
    public String getOrdertemId() {
        return ordertemId;
    }

    public void setOrdertemId(String ordertemId) {
        this.ordertemId = ordertemId;
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
