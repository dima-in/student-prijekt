package ex.java.studentorder.domain;

public enum StudentOrderStatus {
    START, CHECKED;
// тк у нас число. а по числу мы получить не можем(TODO понять о чем это)
    // метод возвращает статус по целому числу
// полученному из БД столбца student_order_status
    public static StudentOrderStatus fromValue(int value) {
        //values() возвращает все перечисляемые типы, которые в нем есть
        for (StudentOrderStatus sos : StudentOrderStatus.values()){
            if (sos.ordinal() == value) {
                return sos;
            }
        }// если не 0 и не 1, тогда ошибка
        throw new RuntimeException("Unknown value:" + value);
    }
}
