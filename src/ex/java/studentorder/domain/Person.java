package ex.java.studentorder.domain;

import java.time.LocalDate;

public abstract class Person { //РОДИТЕЛЬ!!! ПРЕДОК!!! ИМЕЕТ ОБЩИЕ ДЛЯ НАСЛЕДНИКОВ ПОЛЯ

    private String surName;
    private String givenName;
    private String patronymic;
    private LocalDate dateOfBirth;
    private Address address;

    public Person(String surName, String givenName, String patronymic, LocalDate dateOfBirth, Address address) {
        this.surName = surName;
        this.givenName = givenName;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.address = address;//TODO разобраться что не так с getAddress
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
