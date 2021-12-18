package ex.java.studentorder;

import ex.java.studentorder.dao.DictionaryDaoImpl;
import ex.java.studentorder.domain.*;

import java.time.LocalDate;
import java.util.List;

public class SaveStudentOrder
{
    public static void main(String[] args) throws Exception {
        String qq = "012345678901";
        System.out.println(qq.substring(0,2));
        List<Street> d = new DictionaryDaoImpl().findStreets("росп");
        for (Street s: d) {
            System.out.println(s.getstreetCode() + " " + " " + s.getStreetName());
        }

        List<PassportOffice> po = new DictionaryDaoImpl().findPassportOffice("010020000000");
        for (PassportOffice p : po) {
            System.out.println(p.getOfficeName() + " " + " " + p.getOfficeAreaId());
        }

        List<RegisterOffice> ro = new DictionaryDaoImpl().findRegisterOffice("020010010001");
        for (RegisterOffice r : ro) {
            System.out.println(r.getOfficeName() + " " + " " + r.getOfficeAreaId());
        }

        List<CountryArea> ca = new DictionaryDaoImpl().findAreas("020010010000");
        for (CountryArea a : ca) {
            System.out.println(a.getAreaName() + " " + " " + a.getAreaId());
        }

    }

    static long saveStudentOrder(StudentOrder studentOrder){
        long answer = 199;
        System.out.println("SaveStudentOrder:");
        return answer;
    }


    public static StudentOrder buildStudentOrder(long id) {

        //temp
        PassportOffice passportOffice1 =
                new PassportOffice(1L,"","Пастортный стол №"  + id);
        PassportOffice passportOffice2 =
                new PassportOffice(2L,"","Пастортный стол №"  + id);
        RegisterOffice registerOffice3 =
                new RegisterOffice(3L, "","ЗАГЗ №"  + id);
        RegisterOffice registerOffice4 =
                new RegisterOffice(4L, "","ЗАГЗ №"  + id);


        StudentOrder so = new StudentOrder();
        so.setStudentOrderID(id); //обращение к методу класса StudentOrder через переменную so
        so.setMarriageCertificateId("" + (1234560000 + id));
        so.setMarriageDate(LocalDate.of(2016,7,4));
        so.setMarriageOffice(registerOffice4);

        Street street = new Street(1L, "Кутузовский проспект");
        Address address = new Address("235714", street, "32","1", "103");

        Adult husband = new Adult("Тенек","Аркадий", "Потапович",LocalDate.of(1952,01,19));
        //вызов сеттеров у класса Adult
        husband.setPassportSeria("" + (1000 + id));
        husband.setPassportNumber("" + (100000 + id));
        husband.setIssueData(LocalDate.of(2010,12,3));
        husband.setIssueDepartment(passportOffice1);
        husband.setStudentID("" + 10000 +id);

        Adult wife = new Adult("Тенек", "Фаина", "Львовна",LocalDate.of(1952, 03,5));
        wife.setPassportSeria("" + (2000 + id));
        wife.setPassportNumber("" + (200000 + id));
        wife.setIssueData(LocalDate.of(2010,10,7));
        wife.setIssueDepartment(passportOffice2);
        wife.setStudentID("" + 20000 +id);

        Child child1 = new Child("Тенек", "Георгий", "Аркадьевич",LocalDate.of(2013, 11,22));
        child1.setCertificateNumber("" + (30000 + id));
        child1.setIssueDate(LocalDate.of(2013, 11, 23));
        child1.setIssueDepartment(registerOffice3);
        child1.setAddress(address);

        Child child2 = new Child("Тенек", "Соня", "Аркадьевна",LocalDate.of(2013, 11,22));
        child2.setCertificateNumber("" + (30000 + id));
        child2.setIssueDate(LocalDate.of(2013, 11, 23));
        child2.setIssueDepartment(registerOffice4);
        child2.setAddress(address);

        //передача параметров
        so.setHusband(husband);
        so.setWife(wife);
        so.addChild(child1);
        so.addChild(child2);

        return so;
    }
}
