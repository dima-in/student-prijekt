package ex.java.studentorder.domain;

import java.time.LocalDate;
// НАСЛЕДНИК!!! ПОТОМОК!!! вызывает конструктор предка, родителя, и передает ему параметры
public class Adult extends Person
{
    private String passportSeries;
    private String passportNumber;
    private LocalDate issueData;
    private PassportOffice issueDepartment;
    private University university;
    private String studentID;
//TODO разобраться почему не работает конструктор по умолчанию

    public Adult() {
    }

    public Adult(String surName, String givenName, String patronymic, LocalDate dateOfBirth) {
        super(surName, givenName, patronymic, dateOfBirth);

    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(String passportSeries) {
        this.passportSeries = passportSeries;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public LocalDate getIssueData() {
        return issueData;
    }

    public void setIssueData(LocalDate issueData) {
        this.issueData = issueData;
    }

    public PassportOffice getIssueDepartment() {
        return issueDepartment;
    }

    public void setIssueDepartment(PassportOffice issueDepartment) {
        this.issueDepartment = issueDepartment;
    }
}
