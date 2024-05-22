# Exam-Seating-Arrangement-System

**Exam Seating Allotment System Overview:**

The Exam Seating Allotment System is a sophisticated java based application designed to efficiently manage seat allocation for students and staff during examinations.It offers a range of essential features, including the allocation of student and staff seats based on various criteria like date, time, assigned hall, and course.The system provides user-friendly interfaces, utilizing JavaFX, to facilitate the
allocation process and offers the capability to view, print, and filter seating allocation results. The integration with a MySQL database ensures the secure storage and retrieval of seating data, while comprehensive error handling and informative messaging enhance the overall user experience

The system facilitates administrators to input crucial details into the database for exam seating allotment.

**Database Tables:**
1. **Students:** Stores student information like roll number, name, department, year of study, and password.  
2. **Halls:** Contains information about available exam halls, including hall number, floor, seating capacity, and layout dimensions.  
3. **Exam Schedule:** Maintains the timetable for exams, including course code, course name, department, date, and time.  
4. **Faculty:** Stores details about faculty members, including staff ID, name, department, and password.  

**Seating Allocation Algorithm:**
Utilizes an algorithm to allocate seats for semester exams, ensuring stricter security by avoiding seating students from the same department in close proximity.

**User Authentication:**
Students and faculty members can log in using their ID and password to access the system.

**Student Functionality:**
Upon login, students can view their exam details such as course name, course details, and their assigned seat (hall number, row number, and seat number).

**Faculty Functionality:**
Faculty members can log in to view their assigned halls for invigilation. They can also access information about the range of row numbers and corresponding roll number ranges of students assigned to those rows.
