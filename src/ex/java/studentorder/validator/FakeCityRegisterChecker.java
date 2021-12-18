package ex.java.studentorder.validator;

import ex.java.studentorder.domain.*;
import ex.java.studentorder.domain.Child;
import ex.java.studentorder.domain.register.CityRegisterResponse;
import ex.java.studentorder.exception.CityRegisterException;
import ex.java.studentorder.exception.TransportException;

public class FakeCityRegisterChecker implements CityRegisterChecker {
//якобы ходит в городской реестр населения,
//проверяет данные на соответствие метод checkPerson(Person person)
// принимает для проверки не ордер а более конкретно персону

    private static final String GOOD_1 = "1000";
    private static final String GOOD_2 = "2000";
    private static final String GOOD_3 = "30000";
    private static final String BAD_1 = "1001";
    private static final String BAD_2 = "2001";
    private static final String BAD_3 = "30001";
    private static final String ERROR_1 = "1002";
    private static final String ERROR_2 = "2002";
    private static final String ERROR_T1 = "1003";
    private static final String ERROR_T2 = "2003";

    @Override
    public CityRegisterResponse checkPerson(Person person)
            throws CityRegisterException, TransportException {
        CityRegisterResponse res = new CityRegisterResponse();

        if (person instanceof Adult) {
            Adult t = (Adult)person;
            String ps = t.getPassportSeria();

            if (ps.equals(GOOD_1)||ps.equals(GOOD_2)) {
                //возвращаемое значение
                res.setExisting(true);
                res.setTemporal(false);

            }
            if (ps.equals(BAD_1)||ps.equals(BAD_2)) {
                //возвращаемое значение
                res.setExisting(false);

            }
            if (ps.equals(ERROR_1)||ps.equals(ERROR_2)) {
                CityRegisterException cex = new CityRegisterException("1", "GRN ERROR" + ps);
                throw cex;
            }
            if (ps.equals(ERROR_T1)||ps.equals(ERROR_T2)) {
                TransportException cex = new TransportException("Transport ERROR" + ps);
                throw cex;
            }
        }

        if (person instanceof Child) {
            res.setExisting(true);
            res.setTemporal(true);
        }
        System.out.println(res);
        return res;
    }
}