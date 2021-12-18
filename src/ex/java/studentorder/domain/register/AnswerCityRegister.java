package ex.java.studentorder.domain.register;

import java.util.ArrayList;
import java.util.List;
//создаем обобщенный ответ в виде отчета
public class AnswerCityRegister {
    private List<AnswerCityRegisterItems> itemsList;

    public List<AnswerCityRegisterItems> getItemsList() {
        return itemsList;
    }
    public void addItems(AnswerCityRegisterItems item){
        if (itemsList == null){
            itemsList = new ArrayList<>(10);
        }
        itemsList.add(item);
    }
}
