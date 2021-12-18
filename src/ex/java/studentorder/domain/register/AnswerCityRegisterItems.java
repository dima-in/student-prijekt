package ex.java.studentorder.domain.register;

import ex.java.studentorder.domain.Person;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
//статус ответа, проверка для конкретной персоны и
// доп класс для описания ошибки
public class AnswerCityRegisterItems {
//статус ответа ГРН
    public enum CityStatus{
        YES, NO, ERROR;
    }
    public static class CityError {
//если ошибка, то код ошибки текст ошибки
        private String code;
        private String text;

        public CityError(String code, String text) {
            this.code = code;
            this.text = text;
        }

        public String getCode() {
            return code;
        }

        public String getText() {
            return text;
        }
    }
    private CityStatus status;
    private Person person;
    private CityError error;

    public AnswerCityRegisterItems(CityStatus status, Person person) {
        this.status = status;
        this.person = person;
    }

    public AnswerCityRegisterItems(CityStatus status, Person person, CityError error) {
        this.status = status;
        this.person = person;
        this.error = error;
    }

    public CityStatus getStatus() {
        return status;
    }

    public Person getPerson() {
        return person;
    }

    public CityError getError() {
        return error;
    }
}
