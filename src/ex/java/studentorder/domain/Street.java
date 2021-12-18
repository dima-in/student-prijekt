package ex.java.studentorder.domain;

public class Street {
    private Long street_code;
    private String street_name;
//пустой конструктор, чтобы создать улицы а потом инициализтровать поля

    public Street() {
    }

    public Street(Long street_code, String street_name) {
        this.street_code = street_code;
        this.street_name = street_name;
    }

    public Long getstreetCode() {
        return street_code;
    }

    public void setStreet_code(Long street_code) {
        this.street_code = street_code;
    }

    public String getStreetName() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }
}
