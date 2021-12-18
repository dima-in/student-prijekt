package ex.java.studentorder.mail;

import ex.java.studentorder.domain.StudentOrder;

public class AnswerMail {
    String ansverMail;

    public void sendMail(StudentOrder so) {
        System.out.println("почта отправлена");
    }
}