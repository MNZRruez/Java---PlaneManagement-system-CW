import java.util.*;


public class W2051634_PlaneManagement {
    private static final Scanner sc = new Scanner(System.in);

    private static final int[][] seatsArray = new int[4][]; // Array which stores available and booked seats

    private static final Ticket[][] ticketsArray = new Ticket[4][];  // Array which stores all the ticket object

    public static void main(String[] args){

        System.out.println("\nWelcome to the Plane Management application\n");

        // Initializing Seats Array
        seatsArray[0] = new int[14];
        seatsArray[1] = new int[12];
        seatsArray[2] = new int[12];
        seatsArray[3] = new int[14];

        // Initializing Tickets Array
        ticketsArray[0] = new Ticket[14];
        ticketsArray[1] = new Ticket[12];
        ticketsArray[2] = new Ticket[12];
        ticketsArray[3] = new Ticket[14];


        // Assigning all values of the seats array to zero. Since initially no seats are booked.
        for (int[] i : seatsArray) {
            Arrays.fill(i, 0);
        }

        int optionSelected = -1;

        do {

            // MENU
            System.out.println("*******************************************************************");
            System.out.println("*                           MENU OPTIONS                          *");
            System.out.println("*******************************************************************");
            System.out.println("     1) Buy a seat ");
            System.out.println("     2) Cancel a seat ");
            System.out.println("     3) Find first available seat ");
            System.out.println("     4) Show seating plan ");
            System.out.println("     5) Print ticket information and total sales ");
            System.out.println("     6) Search ticket ");
            System.out.println("     0) Quit ");
            System.out.println("*******************************************************************");
            System.out.println();
            System.out.println("Please select an option ");


            // Read User Selected Option
            try {
                optionSelected = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Error.. Expecting an Integer \n");
                sc.nextLine();
                continue;
            }


            // Switch Case Control Structure For MENU Options Selected
            switch (optionSelected){
                case 0:
                    System.out.println("Thank you for using Plane management System. Exiting the Program... \n");
                    break;

                case 1:
                    buy_seat();
                    break;

                case 2:
                    cancel_seat();
                    break;

                case 3:
                    find_first_available();
                    break;

                case 4:
                    show_seating_plan();
                    break;

                case 5:
                    print_tickets_info();
                    break;

                case 6:
                    search_ticket();
                    break;

                default:
                    System.out.println("Select a valid option from the given list. \n");
                    break;
            }

        } while (optionSelected != 0);
        sc.close(); // Closes Scanner
    }


    /**
     * Checks if user input seat is available, if available the seat is updated as booked in seatsArray,
     * Creates a Person object with person info.
     * Creates Ticket object which is then stored into the tickets array.
     * Saves ticket info and person info in a .txt file.
     */
    static void buy_seat(){
        int[] seatInfo = read_seat();
        if(seatInfo != null){
            int rowIndex = seatInfo[0];
            int seatIndex = seatInfo[1];
            int seatNumber = seatInfo[2];
            char rowLetter = (char) ('A' + rowIndex);
            if(seatsArray[rowIndex][seatIndex] == 0){
                try {
                    System.out.println("Enter person Name: ");
                    String name = sc.next();

                    System.out.println("Enter person Surname: ");
                    String surname = sc.next();

                    System.out.println("Enter person Email: ");
                    String email = sc.next();

                    // creates an object of Person class with name, surname and email.
                    Person person = new Person(name, surname, email);

                    // creates an object of Ticket class with rowLetter, seatNumber, seatPrice and person.
                    Ticket ticket = new Ticket(rowLetter, seatNumber, calculateSeatPrice(seatNumber), person);

                    // initialize ticketsArray with the ticket object
                    ticketsArray[rowIndex][seatIndex] = ticket;

                    seatsArray[rowIndex][seatIndex] = 1;

                    System.out.println("\nSeat " + rowLetter + seatNumber + " Booked Successfully");

                    // use save method from Ticket class to save the ticket info into a .txt file.
                    Ticket.save(ticket);

                    System.out.println();
                } catch (Exception e) {
                    System.out.println("Invalid Person Information\n");

                }
            }else {
                System.out.println("Sorry the Seat " + rowLetter + seatNumber + " is already Booked and Not Available. \n");
            }
        }
    }


    /**
     * Checks if user input seat is booked,
     * if booked the seat is cancelled by updating seatsArray.
     */
    static void cancel_seat(){
        int[] seatInfo = read_seat();
        if(seatInfo != null){
            int rowIndex = seatInfo[0];
            int seatIndex = seatInfo[1];
            int seatNumber = seatInfo[2];
            char rowLetter = (char) ('A' + rowIndex);
            if(seatsArray[rowIndex][seatIndex] == 1){
                System.out.println("Your Seat " + rowLetter + seatNumber + " Booking Cancelled Successfully. \n");
                ticketsArray[rowIndex][seatIndex] = null;
                Ticket.delete(rowLetter,seatNumber);
                seatsArray[rowIndex][seatIndex] = 0;

            }else {
                System.out.println("The Entered Seat " + rowLetter + seatNumber + " Was not Booked to Cancel. \n");

            }
        }
    }


    // Loops through seatsArray and prints the first available seat.
    static void find_first_available(){
        boolean found = false;
        for (int rowIndex = 0; rowIndex < seatsArray.length; rowIndex++) {
            for (int seat = 0; seat < seatsArray[rowIndex].length; seat++) {
                if (seatsArray[rowIndex][seat] == 0) {
                    // converts rowIndex to corresponding rowLetter
                    char rowLetter = (char) ('A' + rowIndex);
                    int seatNumber = seat + 1;
                    System.out.println("First available seat is " + rowLetter + seatNumber + ". \n");
                    found = true;
                    break;
                }
            }
            System.out.println();
            if(found){
                break;
            }
        }
        if (!found){
            System.out.println("No seats available. \n");
        }
    }


    // Represent the available seats with O and booked seats with X in a specific format or plan.
    static void show_seating_plan(){
        System.out.println();
        for (int[] row: seatsArray){
            System.out.print("  ");
            for (int seat:row){
                if (seat==0){
                    System.out.print(" O");
                }else {
                    System.out.print(" X");
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    // loops through ticketsArray and print ticket info and person info for the every booking made.
    static void print_tickets_info(){
        int ticketsSold = 0;
        double totalTicketsPrice = 0;
        for (Ticket[] rowTickets: ticketsArray){
            for (Ticket ticket: rowTickets){
                if (ticket != null) {
                    ticket.printTicketInfo();
                    totalTicketsPrice = totalTicketsPrice + ticket.getPrice();
                    ticketsSold++;
                }
            }
        }
        if (ticketsSold==0){
            System.out.println("No Tickets were Sold");
        }else {
            System.out.println("Total price of Tickets: £" + totalTicketsPrice);
        }
        System.out.println();
    }


    // search ticket and prints detail
    static void search_ticket(){
        int[] seatInfo = read_seat();
        if(seatInfo != null){
            int rowIndex = seatInfo[0];
            int seatIndex = seatInfo[1];
            int seatNumber = seatInfo[2];
            char rowLetter = (char) ('A' + rowIndex);
            if(seatsArray[rowIndex][seatIndex] == 1){
                ticketsArray[rowIndex][seatIndex].printTicketInfo();
            }else {
                System.out.println("Seat " + rowLetter + seatNumber + " is Available \n");
            }
        }
        System.out.println();
    }


    /**
     * Ask user to enter row and seat, reduces code repetition.
     * @return the (rowIndex, seatIndex, seatNumber) as an int array
     */
    static int[] read_seat(){
        boolean read = false;
        int seatNumber = -1;
        int rowIndex = -1;
        int seatIndex = -1;

        while (!read) {
            try {
                System.out.println("Enter Row Letter : ");
                char rowLetter = sc.next().toUpperCase().charAt(0);
                System.out.println("Enter Seat Number : ");
                seatNumber = sc.nextInt();
                rowIndex = rowIndex(rowLetter);
                seatIndex = seatNumber - 1;
                read = true;
            } catch (Exception e) {
                System.out.println("Enter a valid Row Letter and/or Seat Number. \n");
                sc.nextLine();
            }
        }
        if (rowIndex == 99 || ((rowIndex == 1 || rowIndex == 2) && seatNumber > 12) || seatNumber > 14) {
            System.out.println("Row Letter and/or Seat Number doesn't exist. \n");
            return null;
        } else {
            System.out.println();
            return new int[]{rowIndex, seatIndex, seatNumber};
        }
    }


    /**
     * Computes the index based on the row letter
     * @param rowLetter the row of the seat
     * @return the row index
     */
    static int rowIndex(char rowLetter){
        return switch (rowLetter) {
            case 'A' -> 0;
            case 'B' -> 1;
            case 'C' -> 2;
            case 'D' -> 3;
            default -> 99;
        };
    }


    /**
     * Calculates the seat price based on the seat number
     * @param seatNumber the seat number of the plane
     * @return the seat price
     */
    static double calculateSeatPrice(int seatNumber){
        double seatPrice;
        if(seatNumber>9){
            seatPrice = 180;
        } else if (seatNumber>5) {
            seatPrice = 150;
        }else {
            seatPrice = 200;
        }
        return seatPrice;
    }
}
