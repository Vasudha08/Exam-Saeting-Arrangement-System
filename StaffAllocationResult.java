
package examseatingallotment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StaffAllocationResult {
    private int facultyId;
    private String facultyName;
    private String date;
    private String time;
    private String assignedHall;
    private String rollNumberRange;

    public StaffAllocationResult(int facultyId, String facultyName, String date, String time, String assignedHall, String rollNumberRange) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.date = date;
        this.time = time;
        this.assignedHall = assignedHall;
        this.rollNumberRange = rollNumberRange;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getAssignedHall() {
        return assignedHall;
    }

    public String getRollNumberRange() {
        return rollNumberRange;
    }
    
    public void display() {
        System.out.println("Faculty ID: " + facultyId);
        System.out.println("Faculty Name: " + facultyName);
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);
        System.out.println("Assigned Hall: " + assignedHall);
        System.out.println("Roll Number Range: " + rollNumberRange);
        System.out.println("-------------------------------------------------------------------------");
    }
    
    public void resultsToDB(StaffAllocationResult allocationResults) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    try {
        
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating", "root", "vasu0812");
        String insertQuery = "INSERT INTO StaffAllocationResult (facultyId, facultyName, date, time, assignedHall, seatNumberRange) VALUES (?, ?, ?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(insertQuery);

       
            preparedStatement.setInt(1, getFacultyId());
            preparedStatement.setString(2, getFacultyName());
            preparedStatement.setString(3, getDate());
            preparedStatement.setString(4, getTime());
            preparedStatement.setString(5, getAssignedHall());
            preparedStatement.setString(6, getRollNumberRange());

           
            preparedStatement.executeUpdate();
            System.out.println("Results stored in the database successfully.");
    } 
    catch (SQLException e) {
        e.printStackTrace();
    } 
    finally {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
    }
    }
}
