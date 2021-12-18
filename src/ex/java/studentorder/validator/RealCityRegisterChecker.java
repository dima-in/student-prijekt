package ex.java.studentorder.validator;

import ex.java.studentorder.domain.register.CityRegisterResponse;
import ex.java.studentorder.domain.Person;
import ex.java.studentorder.exception.CityRegisterException;
import ex.java.studentorder.exception.TransportException;

public class RealCityRegisterChecker  implements CityRegisterChecker{
    public CityRegisterResponse checkPerson(Person person)  throws CityRegisterException, TransportException {
        return null;
    }
}
