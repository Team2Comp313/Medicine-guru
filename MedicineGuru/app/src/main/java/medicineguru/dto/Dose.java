package medicineguru.dto;

import java.util.UUID;

/**
 * Created by Brinder Saini on 09/02/2018.
 */

public class Dose {

    public int quantity;
    public String unit;

    public Dose() {
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Dose( int quantity, String unit) {

        this.quantity = quantity;
        this.unit = unit;
    }
}
