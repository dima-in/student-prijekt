package ex.java.studentorder;

import ex.java.studentorder.domain.*;
import ex.java.studentorder.domain.children.AnswerChildren;
import ex.java.studentorder.domain.register.AnswerCityRegister;
import ex.java.studentorder.domain.student.AnswerStudent;
import ex.java.studentorder.domain.wedding.AnswerWeddind;
import ex.java.studentorder.mail.AnswerMail;
import ex.java.studentorder.validator.ChildrenValidator;
import ex.java.studentorder.validator.CityRegisterValidator;
import ex.java.studentorder.validator.StudentValidator;
import ex.java.studentorder.validator.WeddingValidator;

import java.util.LinkedList;
import java.util.List;

public class StudentOrderValidator {
// проверяет всю заявку методом checkAll()
// обьявление переменных(ссылок) для обращения к классам
// проверяющим каждый кусок заявления
    private CityRegisterValidator validatorCityRegistry;
    private WeddingValidator validatorWedding;
    private ChildrenValidator validatorChildren;
    private StudentValidator validatorStudent;
    private AnswerMail sendMail;
    //конструктор инициализирует-создает каждую переменную-ссылку
    public StudentOrderValidator() {
        validatorCityRegistry = new CityRegisterValidator();
         validatorWedding = new WeddingValidator();
         validatorChildren = new ChildrenValidator();
         validatorStudent = new StudentValidator();
         sendMail = new AnswerMail();
    }

    public static void main(String[] args) {

        StudentOrderValidator sov = new StudentOrderValidator();
        sov.checkAll();
    }

    public void checkAll() {
        List<StudentOrder> soListCheck = readStudentOrders();
        // коллекция soList инициализируется коллекцией soListRead
        //передача завок в checkOneOrder(so)
        for(StudentOrder so : soListCheck){
            checkOneOrder(so);
        }

    }

    public List<StudentOrder> readStudentOrders() { // получить заявление для проверки в виде обьекта типа ex.java.studentorder.domain.StudentOrder// типа ex.java.studentorder.domain.StudentOrder переменная so
        //создание коллекции из 5 элементов.
        List<StudentOrder> soListRead = new LinkedList<>();
        //инициализация массива заявками
        for (int i = 0; i < 5; i++){
            //Метод buildStudentOrder(i)
            //возвращает обьекты StudentOrder so и
            //помещает в коллекцию soList
            soListRead.add(SaveStudentOrder.buildStudentOrder(i));
        }
        return soListRead;
    }
    public void checkOneOrder(StudentOrder so){
            AnswerCityRegister answerCityRegistry = checkCityRegistr(so); // сохранениме результата проверки из городского реестра в переменную типа ex.java.studentorder.domain.register.AnswerCityRegistry ans....
//            AnswerWeddind answerWeddind = checkWedding(so);
//            AnswerChildren answerChildren = checkChildren(so);
//            AnswerStudent answerStudent = checkStudent(so);
//            sendMail(so);
        }



    public AnswerCityRegister checkCityRegistr(StudentOrder so) { // принимаем на проверку форму регистрации типа ex.java.studentorder.domain.StudentOrder os
         // создаем экземпляр ex.java.studentorder.validator.ValidatorCityRegistry для получения доступа к полям host и методам
         // инициализация поля hostName экземпляра экземпляра vcr1
         // инициализация возвращаемого результата экземпляром ex.java.studentorder.validator.ValidatorCityRegistry
        // обьявляется переменная класса ex.java.studentorder.domain.register.AnswerCityRegistry acr и используется как возвращаемое значение
        return validatorCityRegistry.checkCityRegistr(so); //возвращается результат проверки регистрации в формате ex.java.studentorder.domain.register.AnswerCityRegistry
    }
    public AnswerWeddind checkWedding(StudentOrder so) {
        return validatorWedding.checkWedding(so);
    }
    public AnswerChildren checkChildren(StudentOrder so) {
        return validatorChildren.checkChildren(so); // вызов checkChildren(so); у экземпляра new ex.java.studentorder.validator.ValidatorChildren()
    }
    public AnswerStudent checkStudent(StudentOrder so) {
        return validatorStudent.checkStudent(so);
    }
    public void sendMail(StudentOrder so) {
        new AnswerMail().sendMail(so); //у ex.java.studentorder.mail.AnswerMail() вызывается sendMail(so)
    }
}
