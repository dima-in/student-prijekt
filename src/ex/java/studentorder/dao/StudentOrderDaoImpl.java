package ex.java.studentorder.dao;

import ex.java.studentorder.config.Config;
import ex.java.studentorder.domain.*;
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
    private static final String INSERT_CHILD = "INSERT INTO public.student_child(" +
            "student_order_id, ch_sur_name, ch_given_name, ch_patronymic," +
            " ch_date_of_birth, ch_certificate_number, ch_certificate_date, ch_register_office_id, " +
            "ch_post_index, ch_street_code, ch_building, ch_extension, ch_apartment)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

//TODO refactoring make one method

    private Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(
                Config.getProperty(Config.DB_URL),
                Config.getProperty(Config.DB_LOGIN),
                Config.getProperty(Config.DB_PASSWORD));//avg
        return con;
    }
    @Override
    public Long saveStudentOrder(StudentOrder so) throws DaoException {
        Long result = -1L;
        try (Connection con = getConnection(); //массив из автосгенерированных id в psql в колонке student_order_id
             PreparedStatement stmt = con.prepareStatement(INSERT_ORDER, new String[]{"student_order_id"}))
        {
            stmt.setInt(      1, StudentOrderStatus.START.ordinal());
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            //husband and wife
            setAdultParam(stmt, 3, so.getHusband());
            setAdultParam(stmt, 16, so.getWife());
            //marriage
            stmt.setString(29,so.getMarriageCertificateId());
            stmt.setLong(  30,so.getMarriageOffice().getOfficeId());
            stmt.setDate(  31,java.sql.Date.valueOf(so.getMarriageDate()));

            stmt.executeUpdate();//

            ResultSet gkRs = stmt.getGeneratedKeys();//возвращает  сгенерированные поля
            // возвращение автосгенерированного идентификатора студ.заявления
            if (gkRs.next()) {
                result = gkRs.getLong(1);
            }
            gkRs.close();

            saveChildren(con, so, result);

        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    private void saveChildren(Connection con, StudentOrder so, Long soId) throws SQLException {
        //try без catch чтобы закрывать соеденение 
        try(PreparedStatement stmt = con.prepareStatement(INSERT_CHILD)) {
            for (Child child : so.getChildren()) {
                stmt.setLong(1, soId);
                setParamsForChild(stmt, child);
                stmt.executeUpdate();
            }
        }
    }

    private void setAdultParam(PreparedStatement stmt, int start, Adult adult) throws SQLException {
        setParamsForPerson(stmt, start, adult);
        stmt.setString(start + 4 , adult.getPassportSeries());
        stmt.setString(start + 5 , adult.getPassportNumber());
        stmt.setDate(  start + 6 , Date.valueOf(adult.getIssueData()));
        stmt.setLong(  start + 7 , adult.getIssueDepartment().getOfficeId());
        setAddressForPerson(stmt, start + 8, adult);
    }

    private void setParamsForPerson(PreparedStatement stmt, int start, Person person) throws SQLException {
        stmt.setString(start , person.getSurName());
        stmt.setString(start + 1 , person.getGivenName());
        stmt.setString(start + 2 , person.getPatronymic());
        stmt.setDate(  start + 3 , Date.valueOf(person.getDateOfBirth()));
    }

    private void setAddressForPerson(PreparedStatement stmt, int start, Person person) throws SQLException {
        Address hus_address = person.getAddress();
        stmt.setLong(  start , hus_address.getPostCode());
        stmt.setLong(  start + 1 , hus_address.getStreet().getstreetCode());
        stmt.setString(start + 2, hus_address.getBuilding());
        stmt.setString(start + 3, hus_address.getExtension());
        stmt.setString(start + 4, hus_address.getApartment());
    }

    private void setParamsForChild(PreparedStatement stmt, Child child) throws SQLException {
        setParamsForPerson(stmt, 2, child);
        stmt.setString(6, child.getCertificateNumber());
        stmt.setDate(7, Date.valueOf(child.getIssueDate()));
        stmt.setLong(8, child.getIssueDepartment().getOfficeId());
        setAddressForPerson(stmt, 9,child);
    }
}

