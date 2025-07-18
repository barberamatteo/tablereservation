package it.matteobarbera.tablereservation.model.dto;

public class CustomerDTO {
    private String name;
    private String phoneNumber;
    private String email;

    public CustomerDTO(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        String toString = "{"
                + "        \"name\":\"" + name + "\""
                + ",         \"phoneNumber\":\"" + phoneNumber + "\""
                + ",         \"email\":\"" + email + "\""
                + "}";
        return toString.replaceAll("[\n\r]", "   ");
    }
}
