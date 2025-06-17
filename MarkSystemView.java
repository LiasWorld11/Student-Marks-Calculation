package newpackage;

import java.util.Scanner;

public class MarkSystemView {
    public static void main(String args[]) {
        MarkSystemController msc = new MarkSystemController();
        mainMenu(msc);
    }

    public static int mainMenu(MarkSystemController msc) {
        Scanner sc = new Scanner(System.in);

        int opt;
        do {
            System.out.println("Student Mark Management System\n");
            System.out.printf("%-3s[%-15s]%n", "1", "Courses Module");
            System.out.printf("%-3s[%-15s]%n", "2", "Student Module");
            System.out.printf("%-3s[%-15s]%n", "3", "Exit");
            System.out.print("\nEnter Option: ");

            opt = sc.nextInt();
            sc.nextLine(); // Clear buffer
            System.out.println("");

            switch (opt) {
                case 1 ->
                    courseModule(msc);
                case 2 ->
                    stuModule(msc);
                case 3 ->
                    System.out.println("Exiting the System. Goodbye!");
                default ->
                    System.out.println("Invalid option. Please try again.");
            }
        } while (opt != 3);

        return opt;
    }

    public static void courseModule(MarkSystemController msc) {
        Scanner sc = new Scanner(System.in);
        int subOpt = 0;

        do {
            System.out.printf("********************************\n\n");
            System.out.printf("%-3s[%-15s]%n", "1", "Add Course");
            System.out.printf("%-3s[%-15s]%n", "2", "Edit Course");
            System.out.printf("%-3s[%-15s]%n", "3", "Delete Course");
            System.out.printf("%-3s[%-15s]%n", "4", "View Course");
            System.out.printf("%-3s[%-15s]%n", "5", "Back to Main Menu");
            System.out.print("\nEnter Option: ");

            //Input validation
            if (sc.hasNextInt()) {
                subOpt = sc.nextInt(); //Get user input
            } else {
                System.out.println("\nInvalid input! Please enter a valid input!\n");
                sc.next(); //Clear the invalid input
                continue; //Continue prompting for valid input
            }

            switch (subOpt) {
                case 1 ->
                    msc.addCourse();
                case 2 ->
                    msc.editCourse();
                case 3 ->
                    msc.deleteCourse();
                case 4 ->
                    msc.viewCourse();
                case 5 ->
                    System.out.println("Returning to main menu...");
                default ->
                    System.out.println("Invalid option! Please enter a valid option!");
            }
        } while (subOpt != 5);//Continue until user chooses to return to main menu    
    }

    public static void stuModule(MarkSystemController msc){
        Scanner sc = new Scanner(System.in);
        int subOpt;
        do {
            System.out.printf("********************************\n\n");
            System.out.printf("%-3s[%-25s]%n", "1", "Add Student Mark");
            System.out.printf("%-3s[%-25s]%n", "2", "Edit Student Mark");
            System.out.printf("%-3s[%-25s]%n", "3", "Delete Student Mark");
            System.out.printf("%-3s[%-25s]%n", "4", "View Student Mark");
            System.out.printf("%-3s[%-25s]%n", "5", "Back to Main Menu");
            System.out.print("\nEnter Option: ");

            subOpt = sc.nextInt();
            sc.nextLine(); //Clear buffer

            switch (subOpt) {
                case 1 -> msc.addStuMark();
                case 2 -> msc.editStuMark();
                case 3 -> msc.deleteStuMark();
                case 4 -> msc.viewStuMark();
                case 5 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option. Please try again.\n");
            }
        } while (subOpt != 5);
    }
}
