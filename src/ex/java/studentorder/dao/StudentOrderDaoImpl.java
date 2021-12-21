package ex.java.studentorder.dao;

import ex.java.studentorder.config.Config;
import ex.java.studentorder.domain.StudentOrder;
import ex.java.studentorder.domain.StudentOrderStatus;
import ex.java.studentorder.exception.DaoException;

import java.sql.*;
import java.time.LocalDateTime;

//
public class StudentOrderDaoImpl implements StudentOrderDao{

    private static final String INSERT_ORDER = "INSERT INTO student_order(" +
            "student_order_status, student_order_date, h_sur_name, " +  //
            "h_given_name, h_patronymic, h_date_of_birth, h_passport_seria, h_passport_number, " +
            "h_passport_date, h_passport_office_id, h_post_index, h_street_code, h_building, " +
            "h_extension, h_apartment, w_sur_name, w_given_name, w_patronymic, w_date_of_birth, " +
            "w_passport_seria, w_passport_number, w_passport_date, w_passport_office_id, " +
            "w_post_index, w_street_code, w_building, w_extension, w_apartment, " +
            "marriage_certificate_id, register_office_id, marriage_date)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

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
        Long result = -1L;
        try (Connection con = getConnection(); //массив из автосгенерированных id в psql в колонке student_order_id
             PreparedStatement stmt = con.prepareStatement(INSERT_ORDER, new String[]{"student_order_id"})){

             stmt.setInt(1, StudentOrderStatus.START.ordinal());
             stmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
                        int stert = 3;
            stmt.setString(stert , so.getHusband().getSurName());
            stmt.setString(stert + 1, so.getHusband().getGivenName());
            stmt.setString(stert + 2, so.getHusband().getPatronymic());
            stmt.setDate(  stert + 3 , java.sql.Date.valueOf(so.getHusband().getDateOfBirth()));
            stmt.setString(stert + 4 ,so.getHusband().getPassportSeries());
            stmt.setString(stert + 5 ,so.getHusband().getPassportNumber());
            stmt.setDate(  stert + 6 ,java.sql.Date.valueOf(so.getHusband().getIssueData()));
            stmt.setLong(  stert + 7 ,so.getHusband().getIssueDepartment().getOfficeId());
            stmt.setLong(  stert + 8 , so.getHusband().getAddress().getPostCode());
            stmt.setLong(  stert + 9 , so.getHusband().getAddress().getStreet().getstreetCode());
            stmt.setString(stert + 10, so.getHusband().getAddress().getBuilding());
            stmt.setString(stert + 11, so.getHusband().getAddress().getExtension());
            stmt.setString(stert + 12,so.getHusband().getAddress().getApartment());

            stmt.setString(stert + 13, so.getWife().getSurName());
            stmt.setString(stert + 14, so.getWife().getGivenName());
            stmt.setString(stert + 15, so.getWife().getPatronymic());
            stmt.setDate(  stert + 16, java.sql.Date.valueOf(so.getWife().getDateOfBirth()));
            stmt.setString(stert + 17, so.getWife().getPassportSeries());
            stmt.setString(stert + 18, so.getWife().getPassportNumber());
            stmt.setDate(  stert + 19, java.sql.Date.valueOf(so.getWife().getIssueData()));
            stmt.setLong(  stert + 20, so.getWife().getIssueDepartment().getOfficeId());
            stmt.setLong(  stert + 21, so.getWife().getAddress().getPostCode());
            stmt.setLong(  stert + 22, so.getWife().getAddress().getStreet().getstreetCode());
            stmt.setString(stert + 23, so.getWife().getAddress().getBuilding());
            stmt.setString(stert + 24, so.getWife().getAddress().getExtension());
            stmt.setString(stert + 25, so.getWife().getAddress().getApartment());




             stmt.setString(29,so.getMarriageCertificateId());
             stmt.setLong(  30,so.getMarriageOffice().getOfficeId());
             stmt.setDate(  31,java.sql.Date.valueOf(so.getMarriageDate()));

             stmt.executeUpdate();//
            ResultSet gkRs = stmt.getGeneratedKeys();//возвращает  сгенерированные поля
            if (gkRs.next()){// возвращение автосгенерированного идентификатора студ.заявления
                result = gkRs.getLong(1);
            }
            gkRs.close();
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return result;
    }
}
