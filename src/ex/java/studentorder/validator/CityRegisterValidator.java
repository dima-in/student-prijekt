package ex.java.studentorder.validator;

import ex.java.studentorder.domain.Person;
import ex.java.studentorder.domain.register.AnswerCityRegister;
import ex.java.studentorder.domain.Child;
import ex.java.studentorder.domain.register.AnswerCityRegisterItems;
import ex.java.studentorder.domain.register.CityRegisterResponse;
import ex.java.studentorder.domain.StudentOrder;
import ex.java.studentorder.exception.CityRegisterException;
import ex.java.studentorder.exception.TransportException;

//проверяет наличие информации из заявки в городском реестре
//методом checkCityRegister
public class CityRegisterValidator
{//объявление переменной personChecker,
    // проверяет персоны в ГРН
    public static String IN_CODE = "NO_GRN ";

    private CityRegisterChecker personChecker = new FakeCityRegisterChecker();
// Конструктор инициализирует personChecker.ссылка на  plug заглушку
    public CityRegisterValidator() {
        personChecker = new FakeCityRegisterChecker();
    }

//у FakeCityRegisterChecker через personChecker вызывается метод checkPerson()
// с параметром so.getHusband()
// so.getHusband() проверятся данные всей семьи в реестре
// у StudentOrder через so. вызывается метод геттер getHusband()
    public AnswerCityRegister checkCityRegistr(StudentOrder so) {
        AnswerCityRegister ans = new AnswerCityRegister();//создаем обобщенный ответ в виде отчета
            ans.addItems(checkPerson(so.getHusband()));
            ans.addItems(checkPerson(so.getWife()));                                        //            for (Iterator<Child> iterator = children.iterator(); iterator.hasNext();){CityRegisterCheckerResponse cans = personChecker.checkPerson(iterator.next());}
            for (Child child : so.getChildren()) {
                ans.addItems(checkPerson(child));
            }
        return ans;
    }


    private AnswerCityRegisterItems checkPerson(Person person) {
        AnswerCityRegisterItems.CityStatus status = null;
        AnswerCityRegisterItems.CityError error = null;

        try {
            CityRegisterResponse tmp = personChecker.checkPerson(person);
            status = tmp.isExisting() ?
                    AnswerCityRegisterItems.CityStatus.YES :
                    AnswerCityRegisterItems.CityStatus.NO;
        } catch (CityRegisterException ex) {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItems.CityStatus.ERROR;
            error = new AnswerCityRegisterItems.CityError(ex.getCode(), ex.getMessage());
        } catch (TransportException tex) {
            tex.printStackTrace(System.out);
            status = AnswerCityRegisterItems.CityStatus.ERROR;
            error = new AnswerCityRegisterItems.CityError(IN_CODE, tex.getMessage());
        } catch (Exception e) {
            e.printStackTrace(System.out);
            status = AnswerCityRegisterItems.CityStatus.ERROR;
            error = new AnswerCityRegisterItems.CityError(IN_CODE, e.getMessage());
        }
        //конкретный ответ по результатам проверки персоны
        AnswerCityRegisterItems ans = new AnswerCityRegisterItems(status,person,error);

        return ans;
    }
}
