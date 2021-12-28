package ex.java.studentorder.dao;

import ex.java.studentorder.domain.StudentOrder;
import ex.java.studentorder.exception.DaoException;

import java.util.List;

public interface StudentOrderDao {
    Long saveStudentOrder(StudentOrder so) throws DaoException;

    List<StudentOrder> getStudentOrders() throws DaoException;
}

