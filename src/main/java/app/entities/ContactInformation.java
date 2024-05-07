package app.entities;

public class ContactInformation {

    private String name;

    private String address;

    private int postal_code;

    private String city;

    private int phone_number;

    private String email;

    public ContactInformation(String name, String address, int postal_code, String city, int phone_number, String email) {
        this.name = name;
        this.address = address;
        this.postal_code = postal_code;
        this.city = city;
        this.phone_number = phone_number;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(int postal_code) {
        this.postal_code = postal_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
