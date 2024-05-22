package examseatingallotment;

//import static java.awt.SystemColor.text;
import static java.awt.SystemColor.text;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
//import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.Text;



public class ExamSeatingGUI extends Application {
    private static final String STUDENT_LOGIN = "STUDENT LOGIN";
    private static final String STAFF_LOGIN = "STAFF LOGIN";
    private static final String ADMIN_LOGIN = "ADMIN LOGIN";

    private BorderPane root;
    private GridPane loginGrid;
    private ToggleGroup loginOptions;
    private RadioButton studentRadio;
    private RadioButton staffRadio;
    private RadioButton adminRadio;
    private TextField usernameField;
    private PasswordField passwordField;
    private Scene loginScene;
    private Scene resultScene;
    private Stage primaryStage;
    private Scene adminScene;
    public String username;
    private TableView<SeatAllocationResult> tableView;
    private TableView<StaffAllocationResult> staffTableView;
    private TableView<SeatAllocationResult> tableViewReport = new TableView<>();
    private ObservableList<SeatAllocationResult> tableData = FXCollections.observableArrayList();//for report filtered
    ComboBox<String> dateFilter;
    ComboBox<String> timeFilter;
    ComboBox<String> hallFilter;
    ComboBox<String> courseNameFilter;

    @Override
    
    public void start(Stage primaryStage) {
       primaryStage.setTitle("Exam Seating Arrangement");
       this.primaryStage = primaryStage; // Set the primaryStage reference
       createLoginScene(primaryStage);
    
    }

    
    private void createLoginScene(Stage primaryStage) {
        root = new BorderPane();
        createLoginOptions();

        loginScene = new Scene(root, 550, 400);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void createResultScene(Stage primaryStage) {
        String rollNumber = "";
        String name = "";
        BorderPane resultRoot = new BorderPane();

    
        resultRoot.setStyle("-fx-background-color: lightgray;");
        Label resultLabel = new Label("Welcome to the Exam Seating Result Page");
        resultLabel.setStyle("-fx-font-size: 20; -fx-padding: 20;");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> createLoginScene(primaryStage));
        Connection connection = null;

    try {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating", "root", "vasu0812");

        
        String query = "SELECT roll_number, name FROM seatallocationresult WHERE roll_number = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
             rollNumber = resultSet.getString("roll_number");
             name = resultSet.getString("name");
        } 
        else {
            System.out.println("No records found for the username: " + username);
        }
    } 
    catch (SQLException e) {
        e.printStackTrace();
       
    } 
    finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
        HBox header = new HBox();
        header.setSpacing(10);
        Label rollNumberLabel = new Label("Roll Number: " + rollNumber);
        Label nameLabel       = new Label("Name          : " + name);
        Label nLabel          = new Label("                          ");
   
    
       VBox topContainer = new VBox();
       topContainer.getChildren().addAll(resultLabel,rollNumberLabel,nameLabel,nLabel);
       resultRoot.setTop(topContainer);

       tableView = new TableView<>();
       System.out.println("TableView added to resultRoot: " + resultRoot.getCenter());

        TableColumn<SeatAllocationResult, String> courseNameCol = new TableColumn<>("Course Name");
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("course"));

        TableColumn<SeatAllocationResult, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<SeatAllocationResult, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<SeatAllocationResult, String> hallCol = new TableColumn<>("Assigned Hall");
        hallCol.setCellValueFactory(new PropertyValueFactory<>("hallNumber"));

        TableColumn<SeatAllocationResult, String> seatNumberCol = new TableColumn<>("Seat Number");
        seatNumberCol.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));

        TableColumn<SeatAllocationResult, String> rowNumberCol = new TableColumn<>("Row Number");
        rowNumberCol.setCellValueFactory(new PropertyValueFactory<>("rowNumber"));

        
        tableView.getColumns().addAll( courseNameCol, dateCol, timeCol, hallCol, seatNumberCol, rowNumberCol);
        tableView.setPrefSize(100, 100); 
        displayExamSeating(username);
    
        resultRoot.setCenter(tableView);
        VBox bottomContainer = new VBox();
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.getChildren().add(logoutButton);
        resultRoot.setBottom(bottomContainer);

        resultScene = new Scene(resultRoot, 550, 400);
        primaryStage.setScene(resultScene);
    }
    
    private void createStaffResultScene(Stage primaryStage) {
    int facultyId = -1;
    String facultyName = "";

    BorderPane resultRoot = new BorderPane();
    resultRoot.setStyle("-fx-background-color: lightgray;");

    Label resultLabel = new Label("Welcome to the Staff Exam Seating Result Page");
    resultLabel.setStyle("-fx-font-size: 20; -fx-padding: 20;");

    Button logoutButton = new Button("Logout");
    logoutButton.setOnAction(e -> createLoginScene(primaryStage));

    Connection connection = null;

    try {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating", "root", "vasu0812");

        String query = "SELECT facultyId, facultyName FROM staffallocationresult WHERE facultyId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            facultyId = resultSet.getInt("facultyId");
            facultyName = resultSet.getString("facultyName");
        } else {
            System.out.println("No records found for the facultyId: " + username);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    HBox header = new HBox();
    header.setSpacing(10);
    Label facultyIdLabel = new Label("Faculty ID: " + facultyId);
    Label facultyNameLabel = new Label("Faculty Name: " + facultyName);
    Label nLabel = new Label("                          ");
    staffTableView = new TableView<>();
    VBox topContainer = new VBox();
    topContainer.getChildren().addAll(resultLabel, facultyIdLabel, facultyNameLabel, nLabel);
    resultRoot.setTop(topContainer);

    
    System.out.println("TableView added to resultRoot: " + resultRoot.getCenter());

    TableColumn<StaffAllocationResult, String> dateCol = new TableColumn<>("Date");
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

    TableColumn<StaffAllocationResult, String> timeCol = new TableColumn<>("Time");
    timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

    TableColumn<StaffAllocationResult, String> hallCol = new TableColumn<>("Assigned Hall");
    hallCol.setCellValueFactory(new PropertyValueFactory<>("assignedHall"));

    TableColumn<StaffAllocationResult, String> seatNumberRangeCol = new TableColumn<>("Seat Number Range");
    seatNumberRangeCol.setCellValueFactory(new PropertyValueFactory<>("rollNumberRange"));

    staffTableView.getColumns().addAll(dateCol, timeCol, hallCol, seatNumberRangeCol);
    staffTableView.setPrefSize(100, 100);
    displayStaffSeating(username);

    resultRoot.setCenter(staffTableView);

    VBox bottomContainer = new VBox();
    bottomContainer.setAlignment(Pos.CENTER);
    bottomContainer.getChildren().add(logoutButton);
    resultRoot.setBottom(bottomContainer);

    resultScene = new Scene(resultRoot, 600, 400);
    primaryStage.setScene(resultScene);
}

    
    private void createAdminScene() {
    BorderPane adminRoot = new BorderPane();
    adminRoot.setStyle("-fx-background-color: dimgray;");
    Label adminHeading = new Label("ADMIN");
    adminHeading.setStyle("-fx-font-size: 20; -fx-text-fill: white;");


    Button scheduleStudentsButton = createStyledButton("Schedule Seats to Students");
    Button scheduleStaffsButton = createStyledButton("Schedule Staffs to Halls");
    Button generateSeatReportButton = createStyledButton("Generate Report for Seat Schedule");
    Button generateStaffReportButton = createStyledButton("Generate Report for Staff Schedule");

    scheduleStudentsButton.setOnAction(e -> {
        ExamSeatingAllotment esa = new ExamSeatingAllotment();
        try {
            esa.seatallocation();
            showsuccess("Seat");
        } catch (SQLException ex) {
            Logger.getLogger(ExamSeatingGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    });

    scheduleStaffsButton.setOnAction(e -> {
        ExamSeatingAllotment esa = new ExamSeatingAllotment();
        try {
            esa.staffallocation();
            showsuccess("Staff");
        } catch (SQLException ex) {
            Logger.getLogger(ExamSeatingGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    });

    generateSeatReportButton.setOnAction(e -> {
        createReportScene();
    });

    generateStaffReportButton.setOnAction(e -> {
        createStaffReportScene();
    });

    // Create the "Back" button
    Button backButton = createStyledButton("Back");

    // Add an event handler to the "Back" button to switch to the login scene
    backButton.setOnAction(e -> createLoginScene(primaryStage));

    // Create a VBox to hold the "Back" button and align it to the bottom center
    VBox backButtonContainer = new VBox(backButton);
    backButtonContainer.setAlignment(Pos.BOTTOM_CENTER);

    // Create the adminButtons VBox
    VBox adminButtons = new VBox(10); // 10px spacing
    adminButtons.setPadding(new Insets(20));
    adminButtons.setAlignment(Pos.CENTER); // Align buttons to the center
    adminButtons.getChildren().addAll(
        adminHeading,
        scheduleStudentsButton,
        scheduleStaffsButton,
        generateSeatReportButton,
        generateStaffReportButton,
        backButtonContainer
    );

    adminRoot.setCenter(adminButtons);
    adminScene = new Scene(adminRoot, 550, 400);
    primaryStage.setScene(adminScene);
}

    
    public Scene createStaffReportScene() {
    TableView<StaffAllocationResult> tableView = new TableView<>();
    tableView.setMinWidth(400);
    tableView.setMaxWidth(800);
    tableView.setMinHeight(400);
    tableView.setMaxHeight(800);

    // Define columns for the StaffAllocationResult class
    TableColumn<StaffAllocationResult, Integer> facultyIdCol = new TableColumn<>("Faculty ID");
    facultyIdCol.setCellValueFactory(new PropertyValueFactory<>("facultyId"));

    TableColumn<StaffAllocationResult, String> facultyNameCol = new TableColumn<>("Faculty Name");
    facultyNameCol.setCellValueFactory(new PropertyValueFactory<>("facultyName"));

    TableColumn<StaffAllocationResult, String> dateCol = new TableColumn<>("Date");
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

    TableColumn<StaffAllocationResult, String> timeCol = new TableColumn<>("Time");
    timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

    TableColumn<StaffAllocationResult, String> hallCol = new TableColumn<>("Assigned Hall");
    hallCol.setCellValueFactory(new PropertyValueFactory<>("assignedHall"));

    TableColumn<StaffAllocationResult, String> seatNumberRangeCol = new TableColumn<>("Seat Number Range");
    seatNumberRangeCol.setCellValueFactory(new PropertyValueFactory<>("rollNumberRange"));

    tableView.getColumns().addAll(facultyIdCol, facultyNameCol, dateCol, timeCol, hallCol, seatNumberRangeCol);

    ObservableList<StaffAllocationResult> data = FXCollections.observableArrayList();

    
        data.addAll(fetchAllStaffAllocationData());
    

    tableView.setItems(data);

   
    Button backButton = new Button("Back");
    backButton.setOnAction(e -> createAdminScene());

    // Create an HBox to hold the "Back" button and align it to the center
    HBox backButtonContainer = new HBox(backButton);
    backButtonContainer.setAlignment(Pos.CENTER);

    // Create a VBox to hold the table view and the "Back" button
    VBox layout = new VBox(10);
    layout.getChildren().addAll(tableView, backButtonContainer);

    // Create the scene with the layout
    Scene staffReportScene = new Scene(layout, 550, 470);
    primaryStage.setScene(staffReportScene);
    primaryStage.show();

    
  
    return staffReportScene;
}

// You'll need to implement the following methods to retrieve or populate data
private ObservableList<StaffAllocationResult> fetchAllStaffAllocationData() {
    ObservableList<StaffAllocationResult> data = FXCollections.observableArrayList();
    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating", "root", "vasu0812");
        String query = "SELECT * FROM staffallocationresult"; // Replace with your staff allocation table name
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            StaffAllocationResult staffDetails = new StaffAllocationResult(
                resultSet.getInt("facultyId"),
                resultSet.getString("facultyName"),
                resultSet.getString("date"),
                resultSet.getString("time"),
                resultSet.getString("assignedHall"),
                resultSet.getString("seatNumberRange")
            );

            data.add(staffDetails);
        }

        connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    

    return data;
}



    public void createReportScene() {
    
        Button generateOverallReportButton = new Button("Generate Overall Report");
        Button generateReportButton = new Button("Generate Report");
        
        
      dateFilter = createFilterComboBox("Filter by Date", "date");
      timeFilter = createFilterComboBox("Filter by Time", "time");
hallFilter = createFilterComboBox("Filter by Hall", "assigned_hall");
courseNameFilter = createFilterComboBox("Filter by Course Name", "course_name");


        int boxWidth = 200; 
    
        dateFilter.setPrefWidth(boxWidth);
        timeFilter.setPrefWidth(boxWidth);
        hallFilter.setPrefWidth(boxWidth);
        courseNameFilter.setPrefWidth(boxWidth);

    
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            generateOverallReportButton,
            dateFilter,
            timeFilter,
            hallFilter,
            courseNameFilter,
            generateReportButton
         );
        layout.setAlignment(Pos.CENTER);
     
        generateOverallReportButton.setOnAction(e -> createOverallReportScene(1));
        generateReportButton.setOnAction(e -> createOverallReportScene(2));
        
        Button backButton = new Button("Back");
       backButton.setOnAction(e -> createAdminScene());
       String sceneStyle = "-fx-background-color: dimgrey;";
       layout.setStyle(sceneStyle);
       
       String buttonFontSize = "-fx-font-size: 16px;"; // You can adjust the font size (16px in this example)
    generateOverallReportButton.setStyle(buttonFontSize);
   // generateReportButton.setStyle(buttonFontSize);
   // backButton.setStyle(buttonFontSize);
      
    // Add the "Back" button to the layout
       layout.getChildren().add(backButton);
        Scene reportScene = new Scene(layout, 550, 400);
        primaryStage.setScene(reportScene);
        primaryStage.show();
    }
    
     public Scene createOverallReportScene(int flag) {
    
        TableView<SeatAllocationResult> tableView = new TableView<>();
        tableView.setMinWidth(400); // Set the minimum width
        tableView.setMaxWidth(800); // Set the maximum width
        tableView.setMinHeight(400); // Set the minimum height
        tableView.setMaxHeight(800); 

        TableColumn<SeatAllocationResult, String> rollNumberCol = new TableColumn<>("Roll Number");
        rollNumberCol.setCellValueFactory(new PropertyValueFactory<>("rollNumber"));

        TableColumn<SeatAllocationResult, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<SeatAllocationResult, String> courseNameCol = new TableColumn<>("Course Name");
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("course"));

        TableColumn<SeatAllocationResult, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<SeatAllocationResult, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<SeatAllocationResult, String> hallCol = new TableColumn<>("Assigned Hall");
        hallCol.setCellValueFactory(new PropertyValueFactory<>("hallNumber"));

        TableColumn<SeatAllocationResult, String> seatNumberCol = new TableColumn<>("Row Number");
        seatNumberCol.setCellValueFactory(new PropertyValueFactory<>("rowNumber"));

        TableColumn<SeatAllocationResult, String> rowNumberCol = new TableColumn<>("Seat Number");
        rowNumberCol.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));

    
        tableView.getColumns().addAll(rollNumberCol, nameCol, courseNameCol, dateCol, timeCol, hallCol, seatNumberCol, rowNumberCol);
       if(flag ==1 ){
        ObservableList<SeatAllocationResult> data = fetchAllSeatAllocationData();
        tableView.setItems(data);
       }
       if(flag==2){
           ObservableList<SeatAllocationResult> data = populateTableView();
           tableView.setItems(data);
       }
        

   
        VBox layout = new VBox(10);
        layout.getChildren().addAll(tableView);
        Button backButton = new Button("Back");
    backButton.setOnAction(e -> createReportScene()); // Navigate back to the report scene

    
    HBox backButtonContainer = new HBox(backButton);
    backButtonContainer.setAlignment(Pos.CENTER);

    
    layout.getChildren().add(backButtonContainer);
        Scene overallReportScene = new Scene(layout, 700, 500);
        primaryStage.setScene(overallReportScene);
        primaryStage.show();
        return overallReportScene;
}

 private ComboBox<String> createFilterComboBox(String promptText, String columnName) {
    ComboBox<String> comboBox = new ComboBox<>();
    comboBox.setPromptText(promptText);
    int boxWidth = 200; // Adjust the width as needed
    comboBox.setPrefWidth(boxWidth);

    // Retrieve distinct values for the given column from the database and add them to the ComboBox
    ObservableList<String> distinctValues = getDistinctValuesFromDatabase(columnName);
    comboBox.getItems().addAll(distinctValues);

    return comboBox;
}

private ObservableList<String> getDistinctValuesFromDatabase(String columnName) {
    ObservableList<String> distinctValues = FXCollections.observableArrayList();

    // Add database retrieval logic here
    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating","root","vasu0812");
        String sqlQuery = "SELECT DISTINCT " + columnName + " FROM seatallocationresult";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String value = resultSet.getString(columnName);
            distinctValues.add(value);
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return distinctValues;
}


    private String createLoginOptions() {
        VBox optionsBox = new VBox();
        optionsBox.setPadding(new Insets(10));
        optionsBox.setSpacing(20);
        optionsBox.setStyle("-fx-background-color: dimgray; -fx-text-fill: white; -fx-font-family: 'Lucida Bright';");

        studentRadio = new RadioButton(STUDENT_LOGIN);
        staffRadio = new RadioButton(STAFF_LOGIN);
        adminRadio = new RadioButton(ADMIN_LOGIN);
        Label nun = new Label("                ");

        loginOptions = new ToggleGroup();
        studentRadio.setToggleGroup(loginOptions);
        staffRadio.setToggleGroup(loginOptions);
        adminRadio.setToggleGroup(loginOptions);
    
        studentRadio.setTextFill(Color.WHITE);
        staffRadio.setTextFill(Color.WHITE);
        adminRadio.setTextFill(Color.WHITE);

        studentRadio.setSelected(true);

        studentRadio.setOnAction(e -> createLoginPage(STUDENT_LOGIN));
        staffRadio.setOnAction(e -> createLoginPage(STAFF_LOGIN));
        adminRadio.setOnAction(e -> createLoginPage(ADMIN_LOGIN));

        optionsBox.getChildren().addAll(nun,studentRadio, staffRadio, adminRadio);

        root.setLeft(optionsBox);
        username = createLoginPage(STUDENT_LOGIN);
        
        return username;
    }

    
    private String createLoginPage(String userType) {
       if (loginGrid != null) {
        root.setCenter(null);
       }

        loginGrid = new GridPane();
        loginGrid.setPadding(new Insets(20));
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);

        Label headingLabel = new Label(userType);
        headingLabel.setStyle("-fx-font-size: 20;");

        HBox headingBox = new HBox(headingLabel);
        headingBox.setAlignment(Pos.CENTER);

        loginGrid.add(headingBox, 0, 0, 2, 1);
        loginGrid.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("USERNAME:");
        usernameField = new TextField();
        loginGrid.add(usernameLabel, 0, 2);
        loginGrid.add(usernameField, 1, 2);

        Label passwordLabel = new Label("PASSWORD:");
        passwordField = new PasswordField();
        loginGrid.add(passwordLabel, 0, 3);
        loginGrid.add(passwordField, 1, 3);

        Button loginButton = new Button("LOGIN");
        loginGrid.add(loginButton, 1, 4);

   
        loginButton.setOnAction(e -> {
              username = usernameField.getText();
              String password = passwordField.getText();

        

            if (username.isEmpty()) {
                showError("Username Not Provided", "Please enter a username.");
            } 
            else if (password.isEmpty()) {
                showError("Password Not Provided", "Please enter a password.");
            } 
            else{
                 boolean validUser = false;
                try {
                    validUser = authenticateUser(username, password, userType);
                } 
                catch (SQLException ex) {
                    Logger.getLogger(ExamSeatingGUI.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (validUser) {
                    if (userType.equals(ADMIN_LOGIN)) {
                        createAdminScene(); 
                        primaryStage.setScene(adminScene); 
                    } 
                    else if(userType.equals(STAFF_LOGIN)){
                        createStaffResultScene(primaryStage);
                    }
                    else {
                        createResultScene(primaryStage);
                    }
                } 
                else {
                    showError("Incorrect username or password!", "Incorrect username or password.");
                }
            }
    });

    root.setCenter(loginGrid);
    return username;
}

    private void displayExamSeating(String username) {
   
    Connection connection = null;

    try {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating", "root", "vasu0812");

        String query = "SELECT roll_number, name, course_name, date, time, assigned_hall, seat_number, row_num FROM seatallocationresult WHERE roll_number = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);

       
        ResultSet resultSet = statement.executeQuery();
        ObservableList<SeatAllocationResult> data = FXCollections.observableArrayList();
      
        while (resultSet.next()) {
            String rollNumber = resultSet.getString("roll_number");
            String name = resultSet.getString("name");
            String courseName = resultSet.getString("course_name");
            String date = resultSet.getString("date");
            String time = resultSet.getString("time");
            String assignedHall = resultSet.getString("assigned_hall");
            int seatNumber = resultSet.getInt("seat_number");
            int rowNumber = resultSet.getInt("row_num");
            if (rollNumber != null && name != null && courseName != null){
            SeatAllocationResult ed = new SeatAllocationResult(rollNumber, name, courseName, date, time, assignedHall, rowNumber, seatNumber);
            ed.display();
            data.add(ed);
            }   
            System.out.println("Row Data: Roll Number: " + rollNumber + ", Name: " + name + ", Course: " + courseName);
        }
        
        tableView.setItems(data);
         
        VBox tableContainer = new VBox();
        HBox header = new HBox();
        header.setSpacing(10); // Set the spacing between elements
        Label rollNumberHeader = new Label("Roll Number");
        Label nameHeader = new Label("Name");
        header.getChildren().addAll(rollNumberHeader, nameHeader);
        tableContainer.getChildren().add(header);
        tableContainer.getChildren().add(tableView);
        root.setCenter(tableContainer);
        primaryStage.setScene(resultScene);
      
    } catch (SQLException e) {
        e.printStackTrace();
        
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

  
private ObservableList<SeatAllocationResult> fetchAllSeatAllocationData() {
   
    ObservableList<SeatAllocationResult> data = FXCollections.observableArrayList();
    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating", "root", "vasu0812");
        String query = "SELECT * FROM seatallocationresult";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            SeatAllocationResult examDetails = new SeatAllocationResult(
                resultSet.getString("roll_number"),
                resultSet.getString("name"),
                resultSet.getString("course_name"),
                resultSet.getString("date"),
                resultSet.getString("time"),
                
                resultSet.getString("assigned_hall"),
                resultSet.getInt("row_num"),
                resultSet.getInt("seat_number")
                
                
            );

            data.add(examDetails);
        }

        connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
       
    }

    return data;
}

private void displayStaffSeating(String username) {
    Connection connection = null;

    try {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating", "root", "vasu0812");

        String query = "SELECT facultyId, facultyName, date, time, assignedHall, seatNumberRange FROM staffallocationresult WHERE facultyId = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();
        ObservableList<StaffAllocationResult> staffdata = FXCollections.observableArrayList();

        while (resultSet.next()) {
            int facultyId = resultSet.getInt("facultyId");
            String facultyName = resultSet.getString("facultyName");
            String date = resultSet.getString("date");
            String time = resultSet.getString("time");
            String assignedHall = resultSet.getString("assignedHall");
            String seatNumberRange = resultSet.getString("seatNumberRange");

            if (facultyId != -1 && facultyName != null) {
                StaffAllocationResult staffResult = new StaffAllocationResult(facultyId, facultyName, date, time, assignedHall, seatNumberRange);
                staffdata.add(staffResult);
            }
        }

        staffTableView.setItems(staffdata);

        VBox tableContainer = new VBox();
        HBox header = new HBox();
        header.setSpacing(10); // Set the spacing between elements
        Label facultyIdHeader = new Label("Faculty ID");
        Label facultyNameHeader = new Label("Faculty Name");
        header.getChildren().addAll(facultyIdHeader, facultyNameHeader);
        tableContainer.getChildren().add(header);
        tableContainer.getChildren().add(staffTableView);
        root.setCenter(tableContainer);
        primaryStage.setScene(resultScene);
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


    
    public void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.show();
    }

    
 

private Button createStyledButton(String text) {
    Button button = new Button(text);
    button.setStyle("-fx-min-width: 250px; -fx-min-height: 40px;-fx-font-size: 14px;");
    return button;
}

private void showsuccess(String msg) {
    String message = msg + " allocation is successful!";
    Text text = new Text(message);
    text.setFont(Font.font(24)); // You can adjust the font size (24 in this example)

    StackPane root = new StackPane();
    root.getChildren().add(text);

    // Create a "Back" button
    Button backButton = new Button("Back");
    backButton.setOnAction(e -> createAdminScene()); // Go back to the admin scene when clicked
    VBox layout = new VBox(10);
    layout.getChildren().addAll(root, backButton);
    layout.setAlignment(Pos.CENTER);

    Scene scene = new Scene(layout, 500, 400);

    primaryStage.setTitle("Seat Allocation Result");
    primaryStage.setScene(scene);
    primaryStage.show();
}


private boolean authenticateUser(String username, String password, String userType) throws SQLException {
    Connection connection = null;
    String tableName = "";
    String name = "";
    String pass = "";

    try {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating", "root", "vasu0812");

        if (userType.equals(STUDENT_LOGIN)) {
            tableName = "student";
            name = "RollNumber";
            pass = "Password";
        } else if (userType.equals(STAFF_LOGIN)) {
            tableName = "faculty";
            name = "faculty_id";
            pass = "password";
        } else if (userType.equals(ADMIN_LOGIN)) {
            tableName = "admin";
            name = "username";
            pass = "password";
        }

        String query = "SELECT " + pass + " FROM " + tableName + " WHERE " + name + " = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String storedPassword = resultSet.getString(pass);
            return storedPassword.equals(password);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return false;
}

private ObservableList<SeatAllocationResult>  populateTableView() {
    tableData.clear();

    String selectedDate = dateFilter.getValue();
    String selectedTime = timeFilter.getValue();
    String selectedHall = hallFilter.getValue();
    String selectedCourseName = courseNameFilter.getValue();

    

    try (Connection connection =  DriverManager.getConnection("jdbc:mysql://localhost:3306/exam_seating", "root", "vasu0812") ) {
        StringBuilder query = new StringBuilder("SELECT * FROM seatallocationresult WHERE 1 = 1");

        if (selectedDate != null) {
            query.append(" AND date = ?");
        }
        if (selectedTime != null) {
            query.append(" AND time = ?");
        }
        if (selectedHall != null) {
            query.append(" AND assigned_hall = ?");
        }
        if (selectedCourseName != null) {
            query.append(" AND course_name = ?");
        }

        PreparedStatement statement = connection.prepareStatement(query.toString());

        int parameterIndex = 1;

        if (selectedDate != null) {
            statement.setString(parameterIndex++, selectedDate);
        }
        if (selectedTime != null) {
            statement.setString(parameterIndex++, selectedTime);
        }
        if (selectedHall != null) {
            statement.setString(parameterIndex++, selectedHall);
        }
        if (selectedCourseName != null) {
            statement.setString(parameterIndex, selectedCourseName);
        }

        ResultSet resultSet = statement.executeQuery();
        ObservableList<SeatAllocationResult> data = FXCollections.observableArrayList();
while (resultSet.next()) {
    SeatAllocationResult examDetails = new SeatAllocationResult(
        resultSet.getString("roll_number"),
        resultSet.getString("name"),
        resultSet.getString("course_name"),
        resultSet.getString("date"),
        resultSet.getString("time"),
        resultSet.getString("assigned_hall"),
        resultSet.getInt("row_num"),
        resultSet.getInt("seat_number")
    );
    tableData.add(examDetails);
}

//tableViewReport.setItems(tableData);

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return tableData;
}


    
    public static void main(String[] args) throws SQLException{
        launch(args);
    }
}

 

   
    
