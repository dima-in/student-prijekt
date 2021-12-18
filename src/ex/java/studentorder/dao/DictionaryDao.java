package ex.java.studentorder.dao;

import ex.java.studentorder.domain.CountryArea;
import ex.java.studentorder.domain.PassportOffice;
import ex.java.studentorder.domain.RegisterOffice;
import ex.java.studentorder.domain.Street;
import ex.java.studentorder.exception.DaoException;

import java.sql.SQLException;
import java.util.List;

public interface DictionaryDao {
    List<Street> findStreets(String pattern) throws DaoException;
    List<PassportOffice> findPassportOffice(String areaId) throws DaoException;
    List<RegisterOffice> findRegisterOffice(String areaId) throws DaoException;
    List<CountryArea> findAreas(String areaId) throws DaoException, SQLException;
}
