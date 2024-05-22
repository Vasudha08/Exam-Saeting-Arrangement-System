package examseatingallotment;

public class ExamSchedule {
    private String courseCode;
    private String courseName;
    private String department;
    private String date;
    private String time;
    private int yos;
    
    public ExamSchedule(String courseCode, String courseName, String department, String date, String time,int yos) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.department = department;
        this.date = date;
        this.time = time;
        this.yos = yos;
    }

    public String getCourseCode() {
        return courseCode;
    }
    
    public String getCourseName() {
        return courseName;
    }

    public String getDepartment() {
        return department;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
    
    public int getYearOfStudy(){
        return yos;
    }

    public void displayExamSchedule() {
        System.out.println("Course Code: " + courseCode);
        System.out.println("Course Name: " + courseName);
        System.out.println("Department: " + department);
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);
    }
}
