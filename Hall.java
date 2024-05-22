
package examseatingallotment;

public class Hall {
    private String hallNumber;
    private int numberOfSeats;
    private int numberOfRows;
    private int floor;
    private int numberOfColumns;

    public Hall(String hallNumber, int numberOfSeats, int numberOfRows, int floor, int numberOfColumns) {
        this.hallNumber = hallNumber;
        this.numberOfSeats = numberOfSeats;
        this.numberOfRows = numberOfRows;
        this.floor = floor;
        this.numberOfColumns = numberOfColumns;
    }

    public String getHallNumber() {
        return hallNumber;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getFloor() {
        return floor;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public void displayHallInfo() {
        System.out.println("Hall Number: " + hallNumber);
        System.out.println("Number of Seats: " + numberOfSeats);
        System.out.println("Number of Rows: " + numberOfRows);
        System.out.println("Floor: " + floor);
        System.out.println("Number of Columns: " + numberOfColumns);
    }
}
