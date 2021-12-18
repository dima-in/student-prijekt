package ex.java.studentorder.dao;

import ex.java.studentorder.config.Config;
import ex.java.studentorder.domain.CountryArea;
import ex.java.studentorder.domain.PassportOffice;
import ex.java.studentorder.domain.RegisterOffice;
import ex.java.studentorder.domain.Street;
import ex.java.studentorder.exception.DaoException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
//класс для доступа к БД
public class DictionaryDaoImpl implements DictionaryDao{
    //date access object обьект для доступа к данным
    // подключнеие к postgreSql
    //выделение отдельного метода для получения коннекта
     private static final String GET_STREET =
            "SELECT street_code, street_name FROM cat_street WHERE upper(street_name) like upper(?)";
     private static final String GET_PASSPORT_OFFICE =
             "SELECT p_office_id, p_office_area_id, p_office_name FROM passport_office WHERE p_office_area_id LIKE ?";
     private static final String GET_REGISTER_OFFICE =
             "SELECT r_office_id, r_office_area_id, r_office_name FROM register_office WHERE r_office_area_id LIKE ?";
     public static final String GET_COUNTRY_AREA =
             "SELECT area_id, area_name FROM country_struct WHERE area_id LIKE ? AND area_id <> ?";
//TODO refactoring make one method
    private Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(
                Config.getProperty(Config.DB_URL),
                Config.getProperty(Config.DB_LOGIN),
                Config.getProperty(Config.DB_PASSWORD));
        return con;
    }

    public List<Street> findStreets(String pattern) throws DaoException {
        //определить список улиц как результат
        // создаем список обьектов улица
        List<Street> result = new LinkedList<>();


        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_STREET)){
            stmt.setString(1,"%" + pattern +"%");
            ResultSet rs = stmt.executeQuery();//выполнить запрос
            while (rs.next()) {
                Street str = new Street(rs.getLong("street_code"), rs.getString("street_name"));
                result.add(str);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    @Override
    public List<PassportOffice> findPassportOffice(String areaId) throws DaoException {
        List<PassportOffice> resultP = new LinkedList<>();


        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_PASSPORT_OFFICE)){
            stmt.setString(1,"%" + areaId +"%");
            ResultSet rs = stmt.executeQuery();//выполнить запрос
            while (rs.next()) {
                PassportOffice passportOffice = new PassportOffice(rs.getLong("p_office_id"),
                        rs.getString("p_office_area_id"),
                        rs.getString("p_office_name"));
                resultP.add(passportOffice);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return resultP;
    }

    @Override
    public List<RegisterOffice> findRegisterOffice(String areaId) throws DaoException {
        List<RegisterOffice> resultR = new LinkedList<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_REGISTER_OFFICE)){
            stmt.setString(1,"%" + areaId +"%");
            ResultSet rs = stmt.executeQuery();//выполнить запрос
            while (rs.next()) {
                RegisterOffice registerOffice = new RegisterOffice(
                        rs.getLong("r_office_id"),
                        rs.getString("r_office_area_id"),
                        rs.getString("r_office_name"));
                resultR.add(registerOffice);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return resultR;
    }

    @Override
    public List<CountryArea> findAreas(String areaId) throws DaoException, SQLException {
        List<CountryArea> resultA = new LinkedList<>();
        String param1 = buildParam(areaId);
        String param2 = areaId;

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_COUNTRY_AREA)){
            stmt.setString(1, "%" + param1 + "%");
            stmt.setString(2, "%" + param2 + "%");
            ResultSet rs = stmt.executeQuery();//выполнить запрос
            while (rs.next()) {
                CountryArea countryArea = new CountryArea(
                        rs.getString("area_id"),
                        rs.getString("area_name"));
                resultA.add(countryArea);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return resultA;
    }

    private String buildParam(String areaId) throws SQLException {
        if (areaId == null || areaId.trim().isEmpty()) {
            return "__0000000000";
        } else if (areaId.endsWith("0000000000")) {
            return areaId.substring(0, 2) + "___0000000";
        } else if (areaId.endsWith("0000000")) {
            return areaId.substring(0, 5) + "___0000";
        } else if (areaId.endsWith("0000")) {
            return areaId.substring(0, 8) + "____";
        }
        throw new SQLException("Invalid parametr 'areaId': " + areaId);
    }
}
