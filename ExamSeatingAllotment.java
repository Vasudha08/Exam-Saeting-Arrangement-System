package examseatingallotment;

import java.util.*;
import java.sql.*;
import javafx.application.Application;

public class ExamSeatingAllotment {

    public static void main(String[] args) throws SQLException {
        Application.launch(ExamSeatingGUI.class, args);
       
    }  
    
    public void seatallocation() throws SQLException{
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating","root","vasu0812");
        Statement statement = connection.createStatement();
        DatabaseHandler databaseHandler = new DatabaseHandler(connection);

        try {
            
            List<Student> students = databaseHandler.getAllStudentsFromDB();
            List<ExamSchedule> exams = databaseHandler.getAllExamsFromDB();
            List<Hall> halls = databaseHandler.getAllHallsFromDB();
            
            SeatAllocation seatAllocation = new SeatAllocation(halls, students, exams);
            String sqlQuery = "TRUNCATE TABLE seatallocationresult";
            String query = "TRUNCATE TABLE conflict_reports";
            statement.execute(sqlQuery);
            statement.execute(query);
            seatAllocation.allocateSeats();
            
        }
        catch(SQLException e){
        }
    }
            
      
    public void staffallocation() throws SQLException{
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating","root","vasu0812");
        Statement statement = connection.createStatement();
        DatabaseHandler databaseHandler = new DatabaseHandler(connection);
        List<Faculty> faculty = databaseHandler.getAllFacultyFromDB();
      
        try{
            String Query = "SELECT * FROM seatallocationresult ORDER BY date, time, assigned_hall, row_num, seat_number";
            statement.executeQuery(Query);
            
            StaffAllocation staffallocation = new StaffAllocation(faculty);
            String sqlQuery = "TRUNCATE TABLE staffallocationresult";
            statement.execute(sqlQuery);
            staffallocation.getFacultyList();
            staffallocation.allocateStaffToStudents();
            
            connection.close();
        } catch (SQLException e) {
            
        }
    }
    
}
