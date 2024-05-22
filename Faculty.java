
package examseatingallotment;

public class Faculty {
   private int facultyId;
    private String facultyName;
    private String department;
    private String password;

    public Faculty() {
        
    }

    public Faculty(int facultyId, String facultyName, String department, String password) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.department = department;
        this.password = password;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }
}
