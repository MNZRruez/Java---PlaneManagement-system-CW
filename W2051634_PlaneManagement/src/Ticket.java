import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Ticket {
    private final char row;
    private final int seat;
    private final double price;

    private final Person person;

    Ticket(char row, int seat, double price, Person person){
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    public char getRow(){
        return row;
    }

    public int getSeat(){
        return seat;
    }

    public double getPrice(){
        return price;
    }

    public Person getPerson(){
        return person;
    }

    /**
     * Prints Ticket Info Row, Seat, Price, Person Info.
     */
    public void printTicketInfo(){
        System.out.println("Row: " + row);
        System.out.println("Seat: " + seat);
        System.out.println("Price: £" + price);
        person.printPersonInfo();
    }

    /**
     * Writes the ticket info and person info into a .txt file
     * @param ticket object
     */
     static void save(Ticket ticket){
        String fileName = ticket.getRow() + String.valueOf(ticket.getSeat()) + ".txt";
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write("Row: " + ticket.getRow() + "\n");
            fileWriter.write("Seat: " + ticket.getSeat() + "\n");
            fileWriter.write("Price: " + ticket.getPrice() + "\n");
            fileWriter.write("Name: " + ticket.getPerson().getName() + "\n");
            fileWriter.write("Surname: " + ticket.getPerson().getSurname() + "\n");
            fileWriter.write("Email: " + ticket.getPerson().getEmail() + "\n\n");
            fileWriter.close();

            System.out.println("Ticket information saved successfully to file " + fileName + "\n");

        }catch (IOException e){
            System.out.println("An error occurred while trying to save ticket information to .txt file "+e.getMessage() + "\n");
        }
    }

    static void delete(char row, int seat){
        String fileName = row + String.valueOf(seat) + ".txt";
        File file = new File(fileName);
        if (file.exists()){
            if(file.delete()) {
                System.out.println("File " + fileName + " deleted.\n");
            }else {
                System.out.println("Error while trying to delete File " + fileName + "\n");
            }
        }
        else{
            System.out.println("File " + fileName + "  does not exist.\n");
        }
    }
}
