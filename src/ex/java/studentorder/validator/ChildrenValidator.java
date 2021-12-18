package ex.java.studentorder.validator;

import ex.java.studentorder.domain.children.AnswerChildren;
import ex.java.studentorder.domain.StudentOrder;

public class ChildrenValidator
{
    String hostChildCheck;

    public AnswerChildren checkChildren(StudentOrder so) {
        System.out.println("checkChildren is run " + hostChildCheck);
        AnswerChildren ans = new AnswerChildren();
        return ans;
    }
}
