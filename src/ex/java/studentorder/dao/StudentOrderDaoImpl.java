package ex.java.studentorder.dao;

import ex.java.studentorder.config.Config;
import ex.java.studentorder.domain.*;
import ex.java.studentorder.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentOrderDaoImpl implements StudentOrderDao{

    private static final String INSERT_ORDER = "INSERT INTO student_order(" +
            "student_order_status, student_order_date, h_sur_name, " + // 3
            "h_given_name, h_patronymic, h_date_of_birth, h_passport_seria, h_passport_number, " + // 5
            "h_passport_date, h_passport_office_id, h_post_index, h_street_code, h_building, " + // 5
            "h_extension, h_apartment, h_university_id, h_student_number," + // 4
            "w_sur_name, w_given_name, w_patronymic, w_date_of_birth, " + // 4
            "w_passport_seria, w_passport_number, w_passport_date, w_passport_office_id, " + // 4
            "w_post_index, w_street_code, w_building, w_extension, " + // 4
            "w_apartment, w_university_id, w_student_number, " + // 3
            "marriage_certificate_id, register_office_id, marriage_date)" + //3
            "VALUES (?, ?, ?, " +
            "?, ?, ?, ?, ?, " +
            "?, ?, ?, ?, ?, " +
            "?, ?, ?, ?, " +
            "?, ?, ?, ?, " +
            "?, ?, ?, ?, " +
            "?, ?, ?, ?, " +
            "?, ?, ?, " +
            "?, ?, ?);";
    private static final String INSERT_CHILD = "INSERT INTO student_child(" +
            "student_order_id, ch_sur_name, ch_given_name, ch_patronymic," +// 4
            " ch_date_of_birth, ch_certificate_number, ch_certificate_date, ch_register_office_id, " +//4
            "ch_post_index, ch_street_code, ch_building, ch_extension, ch_apartment)" + //5
            "VALUES (?, ?, ?, ?," +
            " ?, ?, ?, ?, " +
            "?, ?, ?, ?, ?)";
    public static final String SELECT_ORDERS =
            "SELECT so.*, ro.r_office_area_id, ro.r_office_name, " +
                    "po_h.p_office_area_id as h_p_office_area_id, " +
                    "po_h.p_office_name as h_p_office_name, " +
                    "po_w.p_office_area_id as w_p_office_area_id, " +
                    "po_w.p_office_name as w_p_office_name " +
                    "FROM student_order so " +
                    "INNER JOIN register_office ro ON ro.r_office_id = so.register_office_id " +
                    "INNER JOIN passport_office po_h ON po_h.p_office_id = so.h_passport_office_id " +
                    "INNER JOIN passport_office po_w ON po_w.p_office_id = so.w_passport_office_id " +
                    "WHERE student_order_status = ? ORDER BY student_order_date "; // статус необработанного заявления StudentOrderStatus.START.ordinal()

    public static final String SELECT_CHILD =
            "SELECT sch.*, ro.r_office_area_id, ro.r_office_name " +
                    "FROM student_child sch " +
                    "INNER JOIN register_office ro ON ro.r_office_id = sch.ch_register_office_id " +
                    "WHERE sch.student_order_id  IN ";


//TODO refactoring make one method

    private Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(
                Config.getProperty(Config.DB_URL),
                Config.getProperty(Config.DB_LOGIN),
                Config.getProperty(Config.DB_PASSWORD));//avg
        return con;
    }
    //Сохранение необработанных студенческих заявлений
    @Override
    public Long saveStudentOrder(StudentOrder so) throws DaoException {
        Long result = -1L;
        try (Connection con = getConnection();
             //массив из автосгенерированных в BD id в столбце student_order_id
             PreparedStatement stmt =
                     con.prepareStatement(INSERT_ORDER, new String[]{"student_order_id"}))
        {
            con.setAutoCommit(false); //запрещаем автокомит, помещаем транзакцию в блок try
            try {
                //header
                stmt.setInt(1, StudentOrderStatus.START.ordinal());
                stmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
                //husband and wife
                setAdultParam(stmt, 3, so.getHusband());
                setAdultParam(stmt, 18, so.getWife());
                //marriage
                stmt.setString(33, so.getMarriageCertificateId());
                stmt.setLong(34, so.getMarriageOffice().getOfficeId());
                stmt.setDate(35, java.sql.Date.valueOf(so.getMarriageDate()));

                stmt.executeUpdate();//возвращает количество записей, затронутых изменениями

                ResultSet gkRs = stmt.getGeneratedKeys();//возвращает  авто-сгенерированные в BD поля
                // возвращение авто-сгенерированного идентификатора студ.заявления
                if (gkRs.next()) {
                    //получить значение из первого столбца таблицы student_order
                    result = gkRs.getLong(1);
                }
                gkRs.close();

                saveChildren(con, so, result);


                con.commit();// если все ок, проводим транзакцию
            } catch (SQLException ex) {
                con.rollback();
            }
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
                stmt.addBatch(); // добавить пакет транзакций
            }
            stmt.executeBatch(); // отправляет транзакции пакетами записи пакетами
        }
    }

    private void setAdultParam(PreparedStatement stmt, int start, Adult adult) throws SQLException {
        setParamsForPerson(stmt, start, adult);
        stmt.setString(start + 4 , adult.getPassportSeries());
        stmt.setString(start + 5 , adult.getPassportNumber());
        stmt.setDate(  start + 6 , Date.valueOf(adult.getIssueData()));
        stmt.setLong(  start + 7 , adult.getIssueDepartment().getOfficeId());
        setAddressForPerson(stmt, start + 8, adult);
        stmt.setLong(start + 13, adult.getUniversity().getUniversutyId());
        stmt.setString(start + 14, adult.getStudentID());
    }

    private void setParamsForPerson(PreparedStatement stmt, int start, Person person) throws SQLException {
        stmt.setString(start , person.getSurName());
        stmt.setString(start + 1 , person.getGivenName());
        stmt.setString(start + 2 , person.getPatronymic());
        stmt.setDate(  start + 3 , Date.valueOf(person.getDateOfBirth()));
    }

    private void setAddressForPerson(PreparedStatement stmt, int start, Person person) throws SQLException {
        Address adult_address = person.getAddress();
        stmt.setString(  start , adult_address.getPostCode());
        stmt.setLong(  start + 1 , adult_address.getStreet().getstreetCode());
        stmt.setString(start + 2, adult_address.getBuilding());
        stmt.setString(start + 3, adult_address.getExtension());
        stmt.setString(start + 4, adult_address.getApartment());
    }

    private void setParamsForChild(PreparedStatement stmt, Child child) throws SQLException {

        setParamsForPerson(stmt, 2, child);
        stmt.setString(6, child.getCertificateNumber());
        stmt.setDate(7, Date.valueOf(child.getIssueDate()));
        stmt.setLong(8, child.getIssueDepartment().getOfficeId());
        setAddressForPerson(stmt, 9,child);
    }

    // получение студенческих заялений для проверки
    @Override
    public List<StudentOrder> getStudentOrders() throws DaoException{ //исключения для метода getStudentOrders()
        List<StudentOrder> result = new LinkedList<>(); // пустой список
        try (Connection con = getConnection(); //внутри метода может произойти SQLException
             PreparedStatement stmt = con.prepareStatement(SELECT_ORDERS);

             ) {
//          статус заявки, обработана или нет, student_order_status(chapter58)
            stmt.setInt(1,StudentOrderStatus.START.ordinal());


            ResultSet rs = stmt.executeQuery();  // возвращает результат запроса
            while (rs.next()) {
                StudentOrder so = new StudentOrder(); // если есть записи, создаем заявку so,
                fillStudentOrder(rs, so);               // заполняем временными данными
                fillWedding(rs, so);                    // и добаляем so в List<> result
                Adult husband = fillAdult(rs, "h_");
                Adult wife = fillAdult(rs, "w_");
//                List<Child> child = findChildren(rs);
                so.setHusband(husband);
                so.setWife(wife);
//                so.setChildren(child);
                result.add(so);
            }
            findChildren(con, result); //передаем con чтобы сформировать запрос PreparedStatement на детей

            rs.close();
        } catch (SQLException ex) { //если будет SQLExc, создается свой экземпляр
            throw new DaoException(ex);
        }
        return result; //список с заявками
    }
    //находит и добавляет детей к соответствующему заявлению по student_order_id
    private void findChildren(Connection con, List<StudentOrder> result) throws SQLException {
//получение из потока заявок только soId,
// создание параметра для запроса SELECT_CHILD "WHERE sch.student_order_id  IN (......)"
        String cl = "(" + result.stream().map(so -> String.valueOf(so.getStudentOrderID()))
                .collect(Collectors.joining(",")) + ")";
//  toMap таблица (типа json) key:value
//  ассоциативный массив для поиска и сопоставления детей соответствующей заявке по student_order_id
//  на конвеере лежит so, so -> so.getStudentOrderID() вытаскиваем 1й аргумент id, 2й аргумент весь so
        Map<Long, StudentOrder> maps = result.stream()
                .collect(Collectors.toMap(so -> so.getStudentOrderID(), so -> so));

        try(PreparedStatement stmt = con.prepareStatement(SELECT_CHILD + cl)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){  // пока есть результаты rs запроса stmt
                System.out.println(rs.getLong(1) + ":" + rs.getString(4));
                Child ch = fillChild(rs);
// из ассоциативного массива maps возвращается so, student_order_id которого соответствует
// student_order_id, полученного из SELECT_CHILD
                StudentOrder so = maps.get(rs.getLong("student_order_id"));
// к полученному so додбавляются соответствующие дети
                so.addChild(ch);
            }
        }
    }
    //    получение данных взрослых
    private Adult fillAdult(ResultSet rs, String pref) throws SQLException {
        Adult adult = new Adult();
        adult.setSurName(rs.getString(pref + "sur_name"));
        adult.setGivenName(rs.getString(pref + "given_name"));
        adult.setPatronymic(rs.getString(pref + "patronymic"));
        adult.setDateOfBirth(rs.getDate(pref + "date_of_birth").toLocalDate());
        adult.setPassportSeries(rs.getString(pref + "passport_seria"));
        adult.setPassportNumber(rs.getString(pref + "passport_number"));
        adult.setIssueData(rs.getDate(pref + "passport_date").toLocalDate());

        Long poId = rs.getLong(pref + "passport_office_id");
        String poAreaId = rs.getString(pref + "p_office_area_id");
        String poName = rs.getString(pref + "p_office_name");
        PassportOffice po = new PassportOffice(poId,poAreaId, poName);

        adult.setIssueDepartment(po); // TODO разобраться
        Address address = new Address();
        address.setPostCode(rs.getString(pref + "post_index"));
        Street street = new Street(rs.getLong(pref + "street_code"), "");
        address.setStreet(street);
        address.setBuilding(rs.getString(pref + "building"));
        address.setExtension(rs.getString(pref + "extension"));
        address.setApartment(rs.getString(pref + "apartment"));
        adult.setAddress(address);
        University university = new University(rs.getLong(pref + "university_id"),"");
        adult.setUniversity(university);
        adult.setStudentID(rs.getString(pref + "student_number"));
        return adult;
    }
    // заголовки
    // получение данных заявления из БД, получение данных из rs может порождать исключения
    private void fillStudentOrder(ResultSet rs, StudentOrder so) throws SQLException{
        so.setStudentOrderID(rs.getLong("student_order_id"));
        so.setStudentOrderDate(rs.getTimestamp("student_order_date").toLocalDateTime());
        so.setStudentOrderStatus(StudentOrderStatus.fromValue(rs.getInt("student_order_status")));
    }
    // получение данных о браке
    private void fillWedding(ResultSet rs, StudentOrder so) throws SQLException {
        so.setMarriageCertificateId(rs.getString("marriage_certificate_id"));
        so.setMarriageDate(rs.getDate("marriage_date").toLocalDate());
        Long roId = rs.getLong("register_office_id");
        String ariaId = rs.getString("r_office_area_id"); // данные из таблицы register_office
        String officeName = rs.getString("r_office_name"); // полученны inner join on
        RegisterOffice ro = new RegisterOffice(roId, ariaId,officeName);
        so.setMarriageOffice(ro);
    }
    //
    private Child fillChild(ResultSet rs) throws SQLException {
                String chSurName = rs.getString("ch_sur_name");
                String chGivenName = rs.getString("ch_given_name");
                String chPatronymic = rs.getString("ch_patronymic");
                LocalDate chDateOfBirth = rs.getDate("ch_date_of_birth").toLocalDate();

                Child child = new Child(chSurName,chGivenName,chPatronymic,chDateOfBirth);

                child.setCertificateNumber(rs.getString("ch_certificate_number"));
                child.setIssueDate(rs.getDate("ch_certificate_date").toLocalDate());

                Long roID = rs.getLong("ch_register_office_id");
                String roOfficeAreaID = rs.getString("r_office_area_id");
                String roOfficeName = rs.getString("r_office_name");
                RegisterOffice ro = new RegisterOffice(roID, roOfficeAreaID, roOfficeName);
                child.setIssueDepartment(ro);

                Address adr = new Address();
                Long srteetcode = rs.getLong("ch_street_code");
                Street st = new Street(srteetcode, "");
                adr.setStreet(st);
                adr.setPostCode(rs.getString("ch_post_index"));
                adr.setBuilding(rs.getString("ch_building"));
                adr.setExtension(rs.getString("ch_extension"));
                adr.setApartment(rs.getString("ch_apartment"));

                child.setAddress(adr);

                return child;
    }
}





