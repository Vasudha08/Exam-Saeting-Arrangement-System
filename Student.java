
package examseatingallotment;

public class Student {
    private String rollNumber;
    private String name;
    private String department;
    private int yearOfStudy;
    private String password;
    
    
    public Student(String rollNumber, String name, String department, int yearOfStudy, String password) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.password = password;
    }
    
   
    public String getRollNumber() {
        return rollNumber;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public String getPassword() {
        return password;
    }
    
    public void displayStudentInfo() {
        System.out.println("Roll Number: " + rollNumber);
        System.out.println("Name: " + name);
        System.out.println("Department: " + department);
        System.out.println("Year of Study: " + yearOfStudy);
        System.out.println("Password: " + password);
    }
}
