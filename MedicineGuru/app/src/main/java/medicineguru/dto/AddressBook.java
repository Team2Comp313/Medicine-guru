package medicineguru.dto;

/**
 * Created by Brinder Saini on 16/03/2018.
 */

public class AddressBook {
    private String addressId;
    private String userId;
    private String Street;
    private String city;
    private String State;
    private String postalCode;
    private String email;
    private int number;
    public AddressBook(String userId, String street, String city, String state, String postalCode, String email, int number) {
        this.userId = userId;
        Street = street;
        this.city = city;
        State = state;
        this.postalCode = postalCode;
        this.email = email;
        this.number = number;
    }

    public AddressBook(String addressId, String userId, String street, String city, String state, String postalCode, String email, int number) {
        this.addressId = addressId;
        this.userId = userId;
        Street = street;
        this.city = city;
        State = state;
        this.postalCode = postalCode;
        this.email = email;
        this.number = number;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
