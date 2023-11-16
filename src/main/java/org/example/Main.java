package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    static Connection connection = null;
    static Statement statement = null;
    public static void main(String[] args) throws SQLException {
        connect();

        statement.execute("drop table if exists public.progress;\n" +
                "\n" +
                "drop table if exists public.students;\n" +
                "\n" +
                "drop table if exists public.subjects;");
        statement.execute("CREATE TABLE IF NOT EXISTS students\n" +
                "(id serial NOT NULL PRIMARY KEY,\n" +
                " name varchar(30) NOT NULL,\n" +
                " PassportSerial SMALLINT not null,\n" +
                " PassportNumber INTEGER CHECK (PassportNumber BETWEEN 1 AND 999999) not null,\n" +
                " UNIQUE (PassportSerial, PassportNumber)\n" +
                ");");

        statement.execute("INSERT into students (id, name, PassportSerial, PassportNumber)\n" +
                "values\n" +
                "    (1, 'Студент1', '3320', '123456'),\n" +
                "    (2, 'Студент2', '3321', '789012'),\n" +
                "    (3, 'Студент3', '3322', '345678'),\n" +
                "    (4, 'Студент4', '3323', '901234'),\n" +
                "    (5, 'Студент5', '3324', '567890'),\n" +
                "    (6, 'Студент6', '3325', '123789'),\n" +
                "    (7, 'Студент7', '3326', '456123'),\n" +
                "    (8, 'Студент8', '3330', '890567'),\n" +
                "    (9, 'Студент9', '3328', '234890'),\n" +
                "    (10, 'Студент10', '3329', '678123'),\n" +
                "    (11, 'Студент11', '3330', '901456'),\n" +
                "    (12, 'Студент12', '3321', '234789'),\n" +
                "    (13, 'Студент13', '3322', '567234'),\n" +
                "    (14, 'Студент14', '3320', '890567'),\n" +
                "    (15, 'Студент15', '3330', '123890');");

        statement.execute("CREATE TABLE IF NOT EXISTS subjects\n" +
                "(id serial NOT NULL PRIMARY KEY,\n" +
                " name varchar(50) NOT NULL\n" +
                ");");

        statement.execute("INSERT into subjects (id, name)\n" +
                "values\n" +
                "    (1, 'География'),\n" +
                "    (2, 'История'),\n" +
                "    (3, 'Литература'),\n" +
                "    (4, 'Иностранный язык'),\n" +
                "    (5, 'Художественная культура'),\n" +
                "    (6, 'Физкультура'),\n" +
                "    (7, 'Хореография'),\n" +
                "    (8, 'Музыка');\n");

        statement.execute("CREATE TABLE IF NOT EXISTS progress\n" +
                "(id serial NOT NULL PRIMARY KEY,\n" +
                " student int NOT NULL REFERENCES students(id) ON DELETE CASCADE,\n" +
                " subject int NOT NULL REFERENCES subjects(id),\n" +
                " mark smallint NOT NULL CHECK(mark BETWEEN 2 and 5)\n" +
                ");");

        statement.execute("INSERT into progress (id, student, subject, mark)\n" +
                "values\n" +
                "    (1, 1, 2, 2),\n" +
                "    (2, 1, 3, 3),\n" +
                "    (3, 2, 1, 5),\n" +
                "    (4, 2, 7, 4),\n" +
                "    (5, 2, 8, 5),\n" +
                "    (6, 3, 2, 5),\n" +
                "    (8, 3, 1, 4),\n" +
                "    (9, 3, 3, 3),\n" +
                "    (10, 4, 4, 4),\n" +
                "    (11, 5, 5, 3),\n" +
                "    (12, 6, 6, 4),\n" +
                "    (13, 7, 1, 5),\n" +
                "    (14, 8, 1, 5),\n" +
                "    (15, 8, 3, 3),\n" +
                "    (17, 9, 5, 3),\n" +
                "    (18, 10, 6, 5),\n" +
                "    (19, 10, 7, 3),\n" +
                "    (20, 11, 1, 4),\n" +
                "    (21, 11, 3, 2),\n" +
                "    (22, 12, 4, 3),\n" +
                "    (24, 13, 6, 2),\n" +
                "    (27, 14, 8, 3),\n" +
                "    (28, 14, 4, 3),\n" +
                "    (29, 15, 5, 2),\n" +
                "    (30, 15, 7, 3);\n");

        //---------------------------------------
        System.out.println("3 и выше");
        var one = statement.executeQuery("Select s.name, p.Mark, ss.name from Students s\n" +
                "INNER JOIN Progress p\n" +
                "on s.id = p.student\n" +
                "inner join subjects ss\n" +
                "on ss.id = p.subject\n" +
                "WHERE p.mark > 3 and ss.name = 'История';");
        while (one.next()) {
            String subjName = one.getString(1);
            int mark = one.getInt(2);
            String studName = one.getString(3);
            System.out.println(subjName + " " + mark + " " + studName);
        }

        System.out.println("Вывести список студентов, сдавших определенный предмет, на оценку выше 3");
        var res = statement.executeQuery("Select s.name, p.Mark, ss.name from Students s\n" +
                "INNER JOIN Progress p\n" +
                "on s.id = p.id\n" +
                "inner join subjects ss\n" +
                "on ss.id = p.id\n" +
                "WHERE p.mark > 3 and ss.name = 'Математика';");
        while (res.next()) {
            String subjName = res.getString(1);
            int mark = res.getInt(2);
            String studName = res.getString(3);
            System.out.println(subjName + " " + mark + " " + studName);
        }

        //---------------------------------------
        System.out.println("avg(subject)");
        var two = statement.executeQuery("select avg(p.mark) as \"Средний балл\" from progress p\n" +
                "inner join subjects s on p.subject = s.id\n" +
                "where s.name = 'История';");
        while (two.next()) {
            double avg = two.getDouble(1);
            System.out.println(avg);
        }

        //---------------------------------------
        System.out.println("avg(student)");
        var three = statement.executeQuery("select avg(p.mark) as \"Средний балл\" from progress p\n" +
                "inner join subjects s on p.subject = s.id\n" +
                "inner join students s2 on p.student = s2.id\n" +
                "where s2.name = 'Студент3';");
        while (three.next()) {
            double avg = three.getDouble(1);
            System.out.println(avg);
        }

        //---------------------------------------
        System.out.println("3 предмета с наибольшей успеваемостью");
        var four = statement.executeQuery("SELECT count(*), s.name from progress p\n" +
                "inner join subjects s on s.id = p.subject\n" +
                "where p.mark > 1\n" +
                "group by s.name\n" +
                "order by count(*) desc limit 3;");
        while (four.next()) {
            int cnt = four.getInt(1);
            String name  =  four.getString(2);
            System.out.println(cnt + " " + name);
        }

        System.out.println("Доп");
        

        var five = statement.executeQuery("SELECT s.name " +
                "FROM students s " +
                "WHERE s.id IN ( " +
                "    SELECT p.student " +
                "    FROM progress p " +
                "    WHERE p.mark IN (4, 5) " +
                "    GROUP BY p.student " +
                "    HAVING COUNT(*) = (SELECT COUNT(*) FROM progress WHERE student = p.student) " +
                ")");

        while (five.next()) {
            String name  =  five.getString(1);
            System.out.println(name);
        }
        disconnect();
    }
    public static void connect() {

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/lp10", "postgres", "189176");
            statement = connection.createStatement();
            System.out.println("Connected");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try{
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
            System.out.println("Disconnected");
        }
        catch (SQLException ee) {
            ee.printStackTrace();
        }
    }
}