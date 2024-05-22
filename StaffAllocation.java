
package examseatingallotment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class StaffAllocation {
    private List<String> distinctCombinations = new ArrayList<>();
    List<Integer> facultyIds = new ArrayList<>();
    private List<Faculty> faculties;
    private Stage primaryStage;
    
    
    public StaffAllocation( List<Faculty> faculties) {
        this.faculties = faculties;
    }

   public List<Integer> getFacultyList() {
        System.out.println("FACULTY ILISTE");
        //Collections.shuffle(faculties);
        for (Faculty faculty : faculties) {
            int facultyId = faculty.getFacultyId();
            facultyIds.add(facultyId);
        }
        retrieveDistinctCombinations();
        return facultyIds;
    }

    public void retrieveDistinctCombinations() {
       // System.out.println("1RETEVE DISNTIT");
        try {
            
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating","root","vasu0812");
            Statement statement = connection.createStatement();


            String sqlQuery = "SELECT DISTINCT date, assigned_hall, time FROM seatallocationresult ORDER BY date, time, assigned_hall";
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String hall = resultSet.getString("assigned_hall");
                String time = resultSet.getString("time");

                String combination = date + "/" + hall + "/" + time;
                distinctCombinations.add(combination);
            }
            

            if (distinctCombinations.isEmpty()) {
                ExamSeatingGUI esg = new ExamSeatingGUI();
                esg.showError("Seat not alloted for students yet","frnhg");
              
            }
            
            resultSet.close();
            statement.close();
            connection.close();
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<StaffAllocationResult> allocateStaffToStudents() throws SQLException {
    List<StaffAllocationResult> allocationResults = new ArrayList<>();
   
    int numFaculty = facultyIds.size();
    int facultyIndex = 0;

    for (String combination : distinctCombinations) {
       
        List<String> students = retrieveStudentsForCombination(combination);
        if (!students.isEmpty()) {
            
            List<List<String>> studentGroups = divideStudentsIntoGroups(students);
            String[] parts = combination.split("/");
            String date = parts[0];
            String hall = parts[1];
            String time = parts[2];

            
            for (List<String> group : studentGroups) {
                 
                if (facultyIndex >= numFaculty) {
                    // Display an error message if faculty is not sufficient
                    System.err.println("Error: Insufficient faculty for allocation");
                    return null;
                }
                int facultyId = facultyIds.get(facultyIndex);
                facultyIndex = (facultyIndex + 1) % numFaculty;
                String facultyName = getFacultyNameById(facultyId);

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating", "root", "vasu0812");
                String seatQuery = "SELECT seat_number, row_num FROM seatallocationresult WHERE roll_number = ?";

                String firstRollNumber = group.get(0); // Get the first roll number
                String lastRollNumber = group.get(group.size() - 1); // Get the last roll number
                int firstSeatNumber = 0;
                int firstRowNumber = 0;
                int lastSeatNumber = 0;
                int lastRowNumber = 0;

                try {
           
                    String sqlQuery = "SELECT MIN(seat_number) AS min_seat, MAX(seat_number) AS max_seat, " +
                              "MIN(row_num) AS min_row, MAX(row_num) AS max_row " +
                              "FROM seatallocationresult " +
                              "WHERE roll_number BETWEEN ? AND ?";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                         preparedStatement.setString(1, firstRollNumber);
                         preparedStatement.setString(2, lastRollNumber);

                     ResultSet resultSet = preparedStatement.executeQuery();

                     if (resultSet.next()) {
                         firstSeatNumber = resultSet.getInt("min_seat");
                         lastSeatNumber = resultSet.getInt("max_seat");
                         firstRowNumber = resultSet.getInt("min_row");
                         lastRowNumber = resultSet.getInt("max_row");

                    }
                }
            }   
            catch (SQLException e) {
                e.printStackTrace();
  
            } 
 
            String seatNumberRange = "R" + firstRowNumber + ": S" + firstSeatNumber + " - " + "R" + lastRowNumber + ": S" + lastSeatNumber;

                
            StaffAllocationResult allocationResult = new StaffAllocationResult(
                    facultyId, facultyName, date, time, hall, seatNumberRange);

            allocationResults.add(allocationResult);
            allocationResult.display();
            allocationResult.resultsToDB(allocationResult);
            }
        }
    }
    
        return allocationResults;
    }

    
    private List<List<String>> divideStudentsIntoGroups(List<String> rollNumbers) {
        
        List<List<String>> groups = new ArrayList<>();
        int groupSize = 20; // You can change this to the desired group size

        for (int i = 0; i < rollNumbers.size(); i += groupSize) {
             int endIndex = Math.min(i + groupSize, rollNumbers.size());
             List<String> group = rollNumbers.subList(i, endIndex);
             System.out.println("Range of students:"+group);
             groups.add(group);
        }

        return groups;
    }

    private List<String> retrieveStudentsForCombination(String combination) {
       
        List<String> rollNumbers = new ArrayList<>();
        String sqlQuery = "SELECT r.roll_number " +
                     "FROM seatallocationresult r " +
                     "WHERE r.date = ? AND r.assigned_hall = ? AND r.time = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating", "root", "vasu0812");
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){

        String[] parts = combination.split("/");
        String date = parts[0];
        String hall = parts[1];
        String time = parts[2];
        preparedStatement.setString(1, date);
        preparedStatement.setString(2, hall);
        preparedStatement.setString(3, time);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String rollNumber = resultSet.getString("roll_number");
            System.out.println(rollNumber);
            
            rollNumbers.add(rollNumber);
        }
        System.out.println("\n");
    } 
    catch (SQLException e) {
        e.printStackTrace();
    }

    return rollNumbers;
}
   
    private String getFacultyNameById(int facultyId) {
        for (Faculty faculty : faculties) {
            if (faculty.getFacultyId() == facultyId) {
                return faculty.getFacultyName();
            }
        }
        return null; 
    }

}
