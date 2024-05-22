
package examseatingallotment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SeatAllocationResult {
   private String rollNumber;
    private String name;
    private String course;
    private String date;
    private String time;
    private String hallNumber;
    private int rowNumber;
    private int seatNumber;
    private static ObservableList<SeatAllocationResult> data = FXCollections.observableArrayList();
     
    public SeatAllocationResult(){
        
    }
    
    public SeatAllocationResult(String rollNumber, String name, String course, String date, String time, String hallNumber, int rowNumber, int seatNumber) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.course = course;
        this.date = date;
        this.time = time;
        this.hallNumber = hallNumber;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getHallNumber() {
        return hallNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

public int getSeatNumber() {
    return seatNumber;
}

public void display() {
    System.out.println("Seat Allocation Result:");
    System.out.println("Roll Number: " + rollNumber);
    System.out.println("Name: " + name);
    System.out.println("Course: " + course);
    System.out.println("Date: " + date);
    System.out.println("Time: " + time);
    System.out.println("Hall Number: " + hallNumber);
    System.out.println("Row Number: " + rowNumber);
    System.out.println("Seat Number: " + seatNumber);
    System.out.println("------------------------------------------------------\n");
}

    public void resulttoDB(SeatAllocationResult seatAllocations) throws SQLException {
        
        String url = "jdbc:mysql://localhost:3306/exam_seating";
        String username = "root";
        String password = "vasu0812";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            
            String sql = "INSERT INTO seatallocationresult (roll_number, name, course_name, date, time, assigned_hall, row_num, seat_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

          
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, getRollNumber());
                preparedStatement.setString(2, getName());
                preparedStatement.setString(3,getCourse());
                preparedStatement.setString(4, getDate());
                preparedStatement.setString(5, getTime());
                preparedStatement.setString(6, getHallNumber());
                preparedStatement.setInt(7, getRowNumber());
                preparedStatement.setInt(8, getSeatNumber());

                
                preparedStatement.executeUpdate();
                data.add(seatAllocations);
                System.out.println("Seat allocation results have been successfully stored in the database.");
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error storing seat allocation results in the database.");
        }
    }  
    
    public static ObservableList<SeatAllocationResult> getSeatingDetails() {
        for (SeatAllocationResult result : data) {
            System.out.println("Roll Number: " + result.getRollNumber());
            System.out.println("Name: " + result.getName());
            System.out.println("Course: " + result.getCourse());
            System.out.println("Date: " + result.getDate());
            System.out.println("Time: " + result.getTime());
            System.out.println("Hall Number: " + result.getHallNumber());
            System.out.println("Row Number: " + result.getRowNumber());
            System.out.println("Seat Number: " + result.getSeatNumber());
            System.out.println("------------------------------------------------------");
        }  
        return data;
    }
}
