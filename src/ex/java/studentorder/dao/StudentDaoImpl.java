package ex.java.studentorder.dao;

import ex.java.studentorder.config.Config;
import ex.java.studentorder.domain.StudentOrder;
import ex.java.studentorder.exception.DaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//
public class StudentDaoImpl implements StudentOrderDao{
//TODO refactoring make one method

    private Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(
                Config.getProperty(Config.DB_URL),
                Config.getProperty(Config.DB_LOGIN),
                Config.getProperty(Config.DB_PASSWORD));
        return con;
    }
    @Override
    public Long saveStudentOrder(StudentOrder so) throws DaoException {

    }
}
