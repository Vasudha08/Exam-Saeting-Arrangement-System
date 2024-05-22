
package examseatingallotment;

//import com.sun.jdi.connect.spi.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SeatAllocation {
     private List<String> CourseCodesList;
    private List<Student> StudentsList;
    private List<Hall> HallsList;
    private List<Hall> allhalls; 
    private List<Student> allstudents; 
    private List<ExamSchedule> allexams; 
    String[][][] seatAvailability = null;

    public SeatAllocation(List<Hall> halls, List<Student> students, List<ExamSchedule> exams) {
        this.allhalls = halls;
        this.allstudents = students;
        this.allexams = exams;
    }
    
    public List<String> getCourseCodes(List<ExamSchedule> allExams, String targetDate, String targetTime) {
        Set<String> courseCodes = new HashSet<>();

        for (ExamSchedule exam : allExams) {
            if (exam.getDate().equals(targetDate) && exam.getTime().equals(targetTime)) {
                courseCodes.add(exam.getCourseCode());    
            }
        }

        CourseCodesList = new ArrayList<>(courseCodes);

        return CourseCodesList;
    }

    public List<Student> getStudentsList(String department, int yearOfStudy, List<Student> allStudents) {
         StudentsList = new ArrayList<>();

        for (Student student : allStudents) {
            if (student.getDepartment().equals(department) && student.getYearOfStudy() == yearOfStudy) {
                StudentsList.add(student);
            }   
        }

        Collections.sort(StudentsList, Comparator.comparing(Student::getDepartment).thenComparingInt(Student::getYearOfStudy));
        return StudentsList;
    }
    
    public List<Hall> getHallsList(List<Hall> allHalls) {
        HallsList = new ArrayList<>();
        for (Hall hall : allHalls) {
            if (hall.getNumberOfSeats() > 0 && hall.getNumberOfRows() > 0) {
                HallsList.add(hall);
                System.out.println(hall.getHallNumber());
            }
        }
        
        Collections.sort(HallsList, Comparator.comparing(Hall::getNumberOfSeats));
        return HallsList;
    }
     
    private Set<String> getDistinctCombinationsOfDateAndTime() {
        Set<String> distinctCombinations = new HashSet<>();

        for (ExamSchedule exam : allexams) {
            String combination = exam.getDate() + "/" + exam.getTime();
            distinctCombinations.add(combination);
        }
        return distinctCombinations;
    }

    public void allocateSeats() throws SQLException {  
     
        Set<String> distinctCombinations = getDistinctCombinationsOfDateAndTime();
       
        for (String combination : distinctCombinations) {
             String[] parts = combination.split("/");
             String date = parts[0];
             String time = parts[1];
             
             List<Hall> hallsList = getHallsList(allhalls);
             seatAvailability = initializeSeatAvailability(hallsList);
        
             List<String> courseCodesList = getCourseCodes(allexams,date, time);
             for (String courseCode : courseCodesList) {
                 System.out.println(courseCode);
                 String department = getDepartmentFromCourseCode(courseCode,allexams);
                 int yearOfStudy = getYearOfStudyFromCourseCode(courseCode,allexams);

        
                 List<Student> studentsList = getStudentsList(department, yearOfStudy, allstudents);
                 allocateSeatsForCourse(courseCode, studentsList, hallsList);
                 System.out.println("Running");
        }
             displayHallSeatingTable(seatAvailability);
          
    }
    
}
    
public List<SeatAllocationResult> allocateSeatsForCourse(String courseCode, List<Student> students, List<Hall> halls) throws SQLException {
        List<SeatAllocationResult> seatAllocations = new ArrayList<>();
        System.out.println("Course code:" + courseCode);
        Collections.shuffle(students);
         Collections.shuffle(students);
        ExamSchedule examSchedule = null;
        for (ExamSchedule exam : allexams) {
            if (exam.getCourseCode().equals(courseCode)) {
                examSchedule = exam;
                break; 
            }
        }

        if (examSchedule == null) {
            System.out.println("1");
             return seatAllocations; 
        }

        String date = examSchedule.getDate();
        String time = examSchedule.getTime();

        boolean seatallocated = false;

        for (Student student : students) {
            seatallocated = false; 
         //   System.out.println("sudent");
            for (int hallIndex = 0; hallIndex < halls.size(); hallIndex++) {
                Hall hall = halls.get(hallIndex);
               // System.out.println("hall " + hallIndex);
                if (seatallocated) {
                    break; 
                }

                for (int rowNumber = 1; rowNumber <= hall.getNumberOfRows(); rowNumber++) {
                  //  System.out.println("row"+rowNumber);
                    if (seatallocated) {
                        break; 
                    }

                    for (int seatNumber = 1; seatNumber <= hall.getNumberOfColumns(); seatNumber++) {
                        if (seatAvailability[hallIndex][rowNumber][seatNumber] == null) {
                           // System.out.println("Seatnumner"+seatNumber);
                            boolean proximityTestPassed = checkProximity(student, hall,hallIndex, rowNumber, seatNumber, courseCode);

                            if (proximityTestPassed) {
                               
                                seatAvailability[hallIndex][rowNumber][seatNumber] = courseCode;
                                seatallocated = true;

                                SeatAllocationResult allocationResult = new SeatAllocationResult(
                                        student.getRollNumber(),
                                        student.getName(),
                                        courseCode,
                                        date,
                                        time,
                                        hall.getHallNumber(),
                                        rowNumber,
                                        seatNumber
                                );

                                seatAllocations.add(allocationResult);
                                break; 
                            }
                        }
                    }
                }
            }
            
            if (!seatallocated) {
    boolean allHallsFilled = true;

    for (int hallIndex = 0; hallIndex < halls.size(); hallIndex++) {
         Hall hall = halls.get(hallIndex);
        for (int rowNumber = 1; rowNumber <= hall.getNumberOfRows(); rowNumber++) {
            for (int seatNumber = 1; seatNumber <= hall.getNumberOfColumns(); seatNumber++) {
                if (seatAvailability[hallIndex][rowNumber][seatNumber] == null) {
                    allHallsFilled = false;
                    break;
                }
            }
        }
    }

    String reason = allHallsFilled ? "All halls fully occupied" : "Proximity issues";

    // Insert the conflict report into the database
    insertConflictReport(student,courseCode, date, time, reason);
}



        }

        displayAllocationResult(seatAllocations);
        return seatAllocations;
    }
 
// Method to insert a conflict report into the database
private void insertConflictReport(Student student, String courseCode, String date, String time, String reason) {
    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating", "root", "vasu0812");
        String query = "INSERT INTO conflict_reports (student_id, course_code, date, time, reason) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, student.getRollNumber()); // Adjust this based on your table structure
        statement.setString(2, courseCode);
        statement.setString(3, date);
        statement.setString(4, time);
        statement.setString(5, reason);
        statement.executeUpdate();
        connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


private String[][][] initializeSeatAvailability(List<Hall> halls) {
        int maxRows = halls.stream().mapToInt(Hall::getNumberOfRows).max().orElse(0);
        int maxColumns = halls.stream().mapToInt(Hall::getNumberOfColumns).max().orElse(0);
        int maxHalls = halls.size();

        seatAvailability = new String[maxHalls][maxRows + 1][maxColumns + 1];
        return seatAvailability;
    }

public void displayHallSeatingTable(String[][][] seatAvailability) {
    for (int rowNumber = 1; rowNumber < seatAvailability[0].length; rowNumber++) {
        System.out.print("Row " + rowNumber + ":\t");
        for (int seatNumber = 1; seatNumber < seatAvailability[0][rowNumber].length; seatNumber++) {
            String courseCode = seatAvailability[0][rowNumber][seatNumber];
            System.out.print(courseCode + "\t");
        }
        System.out.println();
    }
}

    
public String getDepartmentFromCourseCode(String courseCode, List<ExamSchedule> exams) {
    for (ExamSchedule exam : exams) {
        if (exam.getCourseCode().equals(courseCode)) {
            return exam.getDepartment();
        }
    }
    return ""; 
}

public int getYearOfStudyFromCourseCode(String courseCode, List<ExamSchedule> exams) {
    for (ExamSchedule exam : exams) {
        if (exam.getCourseCode().equals(courseCode)) {
            return exam.getYearOfStudy();
        }
    }
    return -1; 
}

    
private void displayAllocationResult(List<SeatAllocationResult> seatAllocations) throws SQLException {
      
        for(SeatAllocationResult allocation : seatAllocations){
            allocation.display();
            allocation.resulttoDB(allocation);
        }  
        
}
 

private boolean checkProximity(Student student, Hall hall,int Hallindex ,int rowNumber, int seatNumber, String courseCode) {
    // Define the proximity distance you want to check (e.g., front, back, side seats)
    int proximityDistance = 2; // You can adjust this value based on your preferences
    boolean ans = true;
    // Check for the proximity in front, back, and sides
    for (int i = -proximityDistance; i <= proximityDistance; i++) {
        for (int j = -proximityDistance; j <= proximityDistance; j++) {
            int newRow = rowNumber + i;
            int newSeat = seatNumber + j;

            if (newRow >= 1 && newRow <= hall.getNumberOfRows() &&
             newSeat >= 1 && newSeat <= hall.getNumberOfColumns()) {
             String adjacentCourseCode = seatAvailability[Hallindex][newRow][newSeat];
                 if (adjacentCourseCode != null && courseCode.equals(adjacentCourseCode)) {
                     ans = false;
                      return false; // Proximity test failed
    }
}

        }
    }
  //  ans = true;
  //  System.out.println(ans);
    return true; // Proximity test passed
}
}
