package data;

public class Person {

    private String name;
    private String email;
    private String address;
    private String id;
    private String socialSecurityNumber;
    private String taxId;
    private String registrationId;

    public Person(String name, String email, String address, String id,
                  String socialSecurityNumber, String taxId, String registrationId) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.id = id;
        this.socialSecurityNumber = socialSecurityNumber;
        this.taxId = taxId;
        this.registrationId = registrationId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public String getTaxId() {
        return taxId;
    }

    public String getRegistrationId() {
        return registrationId;
    }
}
