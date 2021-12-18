package ex.java.studentorder.domain;

import java.time.LocalDate;
// НАСЛЕДНИК!!! ПОТОМОК!!! вызывает конструктор предка, родителя, и передает ему параметры
public class Adult extends Person
{
    private String passportSeria;
    private String passportNumber;
    private LocalDate issueData;
    private PassportOffice issueDepartment;
    private String university;
    private String studentID;

    public Adult(String surName,
                 String givenName,
                 String patronymic,
                 LocalDate dateOfBirth) {
        super(surName, givenName, patronymic, dateOfBirth);
    }


    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getPassportSeria() {
        return passportSeria;
    }

    public void setPassportSeria(String passportSeria) {
        this.passportSeria = passportSeria;
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
