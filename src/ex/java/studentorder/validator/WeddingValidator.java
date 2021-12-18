package ex.java.studentorder.validator;

import ex.java.studentorder.domain.wedding.AnswerWeddind;
import ex.java.studentorder.domain.StudentOrder;

public class WeddingValidator
{
    String hostWed;
    public AnswerWeddind checkWedding(StudentOrder so) {
        System.out.println("checkWedding is run " + hostWed);
        AnswerWeddind ans = new AnswerWeddind();
        return ans;
    }
}