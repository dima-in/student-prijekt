package ex.java.studentorder.validator;

import ex.java.studentorder.domain.student.AnswerStudent;
import ex.java.studentorder.domain.StudentOrder;

public class StudentValidator
{
    String hostStud;
    public AnswerStudent checkStudent(StudentOrder so) {
        System.out.println("checkStudent is run " + hostStud);
        AnswerStudent ans = new AnswerStudent();
        return ans;
    }
}
