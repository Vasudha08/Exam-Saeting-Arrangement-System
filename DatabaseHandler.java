package examseatingallotment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class DatabaseHandler {
     private Connection connection;

    public DatabaseHandler(Connection connection) {
        this.connection = connection;
    }

    public List<Student> getAllStudentsFromDB() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sqlQuery = "SELECT RollNumber, Name, Department, YearOfStudy, Password FROM student";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                // Extract data from the result set
                String rollNumber = resultSet.getString("RollNumber");
                String name = resultSet.getString("Name");
                String department = resultSet.getString("Department");
                int yearOfStudy = resultSet.getInt("YearOfStudy");
                String password = resultSet.getString("Password");

                // Create a Student object and add it to the list
                Student student = new Student(rollNumber, name, department, yearOfStudy, password);
                students.add(student);
            }
        }

        return students;
    }

   
    public List<ExamSchedule> getAllExamsFromDB() throws SQLException {
        List<ExamSchedule> exams = new ArrayList<>();
        
        String sqlQuery = "SELECT CourseCode, CourseName, Department,Year, ExamDate, ExamTime  FROM exam_schedule";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                
                String courseCode = resultSet.getString("CourseCode");
                String courseName = resultSet.getString("CourseName");
                String department = resultSet.getString("Department");
                int yos = resultSet.getInt("Year");
                String date = resultSet.getString("ExamDate");
                String time = resultSet.getString("ExamTime");
                
                ExamSchedule exam = new ExamSchedule(courseCode, courseName, department, date, time,yos);
                exams.add(exam);
            }
        }

        return exams;
    }

   
    public List<Hall> getAllHallsFromDB() throws SQLException {
        List<Hall> halls = new ArrayList<>();

       
        String sqlQuery = "SELECT HallNumber, NumberOfSeats, NumberOfRows, Floor, NumberOfColumns, Availability FROM halls";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                
                String hallNumber = resultSet.getString("HallNumber");
                int numberOfSeats = resultSet.getInt("NumberOfSeats");
                int numberOfRows = resultSet.getInt("NumberOfRows");
                int floor = resultSet.getInt("Floor");
                int numberOfColumns = resultSet.getInt("NumberOfColumns");
                boolean availability = resultSet.getBoolean("Availability");

                
                Hall hall = new Hall(hallNumber, numberOfSeats, numberOfRows, floor, numberOfColumns);
                halls.add(hall);
            }
        }

        return halls;
    }
    
    public List<Faculty> getAllFacultyFromDB() throws SQLException {
    List<Faculty> faculties = new ArrayList<>();
    String sqlQuery = "SELECT faculty_id, faculty_name, dept, password FROM faculty";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
         ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
            // Extract data from the result set
            int facultyId = resultSet.getInt("faculty_id");
            String facultyName = resultSet.getString("faculty_name");
            String department = resultSet.getString("dept");
            String password = resultSet.getString("password");

            // Create a Faculty object and add it to the list
            Faculty faculty = new Faculty(facultyId, facultyName, department, password);
            faculties.add(faculty);
        }
    }

    return faculties;
}
}
