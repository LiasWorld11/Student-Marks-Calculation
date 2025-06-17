package newpackage;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.InputMismatchException;

public class MarkSystemController {
    ArrayList<Courses> courses = new ArrayList<>();
    ArrayList<Student> students = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    //Course Module 
    public void addCourse() {
        Scanner sc = new Scanner(System.in);
        String code = "";
        String courseName = "";
        boolean saveSuccess = false;

        while (!saveSuccess) {
            //Check code uniqueness 
            code = getValidCodEntity("\nEnter Course Code: ");
            //Check if the course code is unique
            if (!isCourseCodeUnique(code)) {
                System.out.println("Course code must be unique. Please enter a different code!");
                continue; //Prompt for input again
            }
            //Handle empty course name 
            courseName = getValidName("Enter Course Name: ");
            //Check if the course name is unique 
            if (!isCourseNameUnique(courseName)) {
                System.out.println("Name already exists. Please enter a unique name.");
                continue; // Prompt for input again
            }

            int finalExamWeight, courseWorkWeight;
            do {
                //Get Final Exam Weight & Course Work Weight 
                finalExamWeight = getValidWeight("Final Exam");
                courseWorkWeight = getValidWeight("Coursework");

                if (finalExamWeight + courseWorkWeight != 100) {
                    System.out.println("Total Weightage MUST Equal 100%!");
                }
            } while (finalExamWeight + courseWorkWeight != 100);

            //create course object 
            Courses newCourse = new Courses(code, courseName, finalExamWeight, courseWorkWeight);

            //get test component and add to the course 
            int totalTestWeight = getTestMarks(newCourse);
            //Check if the total weight is equal to course work weight 
            if (totalTestWeight != courseWorkWeight) {
                System.out.printf("\n******************************************\n");
                System.out.println("COURSEWORK WOULD NOT BE SAVED! ENTER AGAIN!");
                System.out.printf("******************************************\n");
            } else {
                saveSuccess = confirmSave();
                if (saveSuccess) {
                    courses.add(newCourse);
                    System.out.printf("\n*********************************\n");
                    System.out.println("\nCourse Added Successfully!!\n\n");
                }
            }
        }
    }

    public String getValidCodEntity(String promptSub) {
        Scanner sc = new Scanner(System.in);
        String codEntity = "";
        boolean valid = false;

        while (!valid) {
            try {
                //Display prompt for course code input
                System.out.print(promptSub);
                //Read and store user input
                codEntity = sc.next();

                // Validate if input contains only alphanumeric characters or empty space
                if (!codEntity.matches("^[a-zA-Z0-9]+$")) {
                    throw new IllegalArgumentException("Please input valid code! Please try again.");
                }
                //Have a valid and unique course code
                valid = true; //Set valid to true to exit the loop
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                sc.nextLine(); //Clear invalid input if necessary
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                sc.nextLine(); //Clear invalid input if necessary
            }
        }
        return codEntity;
    }

    /**
     * Checks if the course code is unique
     *
     * @param code
     */
    public boolean isCourseCodeUnique(String code) {
        for (Courses course : courses) {
            if (course.getCode().equalsIgnoreCase(code)) {
                return false; //If a match is found, course code is not unique
            }
        }
        return true; //If no match is found, course code is unique
    }

    public String getValidName(String promptSub) {
        Scanner sc = new Scanner(System.in);
        String name = "";
        boolean valid = false;

        while (!valid) {
            //Prompt for the name
            System.out.print(promptSub);
            name = sc.next();
            //Check if the string is empty
            if (name.isEmpty()) {
                System.out.println("Course name CANNOT BE EMPTY. Please enter a valid name!");
                continue;
            }
            //Check if the course name contains only alphabetical characters
            if (!name.matches("[a-zA-Z ]+") || name.matches("[ ]+")) {  // have fun explaning regex
                System.out.println("Course name must contain only alphabetical characters. Please try again!");
                continue; // Prompt for input again
            }
            valid = true;
        }
        return name; // Return the valid name
    }

    /**
     * Checks if the course name is unique
     *
     * @param courseName
     */
    public boolean isCourseNameUnique(String courseName) {
        for (Courses course : courses) {
            if (course.getName().equalsIgnoreCase(courseName)) {
                return false; //If a match is found, course name is not unique
            }
        }
        return true; //If no match is found, course name is unique
    }

    public int getValidWeight(String weightType) {
        Scanner sc = new Scanner(System.in);
        int weight = -1; //Initialize with an invalid value

        while (weight < 0 || weight > 100) {
            System.out.printf("Enter %s Weightage (%%): ", weightType);
            try {
                weight = sc.nextInt();
                sc.nextLine(); //Clear buffer

                //Checks if the weight is between 0 and 100 
                if (weight < 0 || weight > 100) {
                    System.out.println("INVALID INPUT! PLEASE ENTER A VALUE BETWEEN 0 AND 100!");
                }
            } catch (InputMismatchException e) {
                System.out.println("INVALID INPUT! PLEASE ENTER A NUMERIC VALUE!");
                sc.nextLine(); // Clear invalid input
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                sc.nextLine(); //Clear invalid input
            }
        }
        return weight;
    }

    //Add course 
    public int getTestMarks(Courses course) {
        Scanner sc = new Scanner(System.in);
        int totalWeight = 0;//Start with 0 wieght 
        int expectedWeight = course.getCourseWorkWeight();//Get the expected weight for coursework
        boolean valid = false;

        while (!valid) {
            course.clearTestComponents();//Clear previous test components if any
            totalWeight = 0;  //Reset total weight before new input cycle

            //Get the number of components and save it to numComponents
            int numComponents = getNumComponent();

            for (int i = 0; i < numComponents; i++) {
                String testComponent = getTestComponent(i + 1, course);
                int weight = getTestMarkContribution(i + 1);

                if (weight < 0) {
                    System.out.println("Invalid input! Please enter a positive value.");
                    i--; //Decrease index to re-enter this test component
                    continue;
                }
                course.addTestComponent(testComponent, weight);
                totalWeight += weight;  // Accumulate the weight
            }
            //Check if the total weight matches the expected weight
            if (totalWeight == expectedWeight) {
                valid = true;
            } else {
                System.out.println("Total weight does not match the expected weight of " + expectedWeight + "%.");
                System.out.println("Please re-enter the test components.");
            }
        }
        return totalWeight;
    }

    public String getTestComponent(int componentNumber, Courses course) {
        Scanner sc = new Scanner(System.in);
        String testComponent = "";
        boolean valid = false;

        while (!valid) {
            try {
                System.out.printf("\nEnter Test Component %d: ", componentNumber);
                testComponent = sc.next().trim();

                //Ensure non-empty
                if (testComponent.isEmpty()) {
                    System.out.println("Test Component Name must not left EMPTY!");
                    continue;
                }

                //Ensure Test Component Name is unique 
                if (course.hasTestComponent(testComponent)) {
                    System.out.println("Test Component Name must be UNIQUE!");
                    continue;
                }

                //Check if the course name contains only alphabetical characters
                if (!testComponent.matches("[a-zA-Z ]+")) {
                    System.out.println("Test name must contain only aplhabetical characters. Please try again!");
                    continue; // Prompt for input again
                }
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a numeric value.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                sc.nextLine(); // Clear invalid input
            }
            valid = true;
        }
        return testComponent;
    }

    public int getNumComponent() {
        Scanner sc = new Scanner(System.in);
        int numComponents = 0;
        boolean valid = false;

        while (!valid) {
            try {
                System.out.print("Enter number of test components: ");
                numComponents = sc.nextInt();
                sc.nextLine(); // Clear buffer

                // Ensure positive number
                if (numComponents <= 0) {
                    System.out.println("Number of test components must be greater than 0.");
                    continue;
                }
                valid = true; // Exit loop when valid input is received
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a numeric value.");
                sc.nextLine(); // Clear invalid input
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                sc.nextLine(); // Clear invalid input
            }
        }
        return numComponents;
    }

    public int getTestMarkContribution(int componentNumber) {
        Scanner sc = new Scanner(System.in);
        int testMark = 0;
        boolean validInput = false;
        do {
            try {
                System.out.printf("Enter Test Component %d Mark Contribution (%%): ", componentNumber);
                testMark = sc.nextInt();
                sc.nextLine(); // Clear buffer

                if (testMark < 0 || testMark > 100) {
                    System.out.println("Mark contribution must be between 0 and 100.");
                } else {
                    validInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a numeric value.");
            } catch (Exception e) {
                System.out.println("Program Error: " + e.getMessage());
                sc.nextLine(); // Clear invalid input
            }
        } while (!validInput);
        return testMark;
    }

    public boolean confirmSave() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Save Coursework [Y/N]: ");
        String saveOption = sc.nextLine().trim();
        return saveOption.equalsIgnoreCase("Y");
    }

    public void editCourse() {
        Scanner sc = new Scanner(System.in);
        String newName = "";
        checkCourseEmpty();
        availableCourse();

        System.out.print("\nEnter Course Code to edit: ");
        String code = sc.next().trim();
        Courses courseToEdit = null;

        //Find the course to edit
        for (Courses course : courses) {
            if (course.getCode().equalsIgnoreCase(code)) {
                courseToEdit = course;
                break;
            }
        }

        if (courseToEdit != null) {
            System.out.println("\nEditing Course.....\n\n" + courseToEdit + "\n");
            addCourse();
        } else {
            System.out.println("Course not found!");
        }
    }

    public void availableCourse() {
        System.out.println("\nAvailable Course Code : ");
        if (courses.isEmpty()) {
            System.out.printf("\n*********************************\n");
            System.out.println("No courses available.");
            return;
        }
        //Print courses with index
        for (int i = 0; i < courses.size(); i++) {
            Courses course = courses.get(i);
            System.out.printf("[%d] %s%n", i + 1, course.getCode());
        }
    }

    public void deleteCourse() {
        Scanner sc = new Scanner(System.in);
        checkCourseEmpty();
        availableCourse();

        System.out.print("Enter Course Code to delete: ");
        String code = sc.next().trim();
        boolean found = false;

        // Find and remove the course
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCode().equalsIgnoreCase(code)) {
                System.out.print("Are you sure you want to delete this course? [Y/N]: ");
                String confirm = sc.next().trim();

                if (confirm.equalsIgnoreCase("Y")) {
                    courses.remove(i);
                    found = true;
                    System.out.println("Course deleted successfully!");
                    break;

                } else {
                    System.out.println("Deletion cancelled.");
                    return;
                }
            }
        }
        if (!found) {
            System.out.println("Course not found!");
        }
    }

    public void viewCourse() {
        Scanner sc = new Scanner(System.in);
        checkCourseEmpty();
        System.out.printf("\n*********************************\n");
        System.out.println("\nList of Courses");
        System.out.printf("\n*********************************\n");
        for (Courses course : courses) {
            System.out.println(course); //This will call the toString method of the Course class
        }
    }

    public void checkCourseEmpty() {
        //Check course if it present 
        if (courses.isEmpty()) {
            System.out.println("No courses available to view.");
            return;
        }
    }

    public void addStuMark() {
        Scanner sc = new Scanner(System.in);

        checkCourseEmpty();

        String stuID = getValidCodEntity("\nEnter Student ID: ");
        //check unique id method 

        String stuName = getValidName("Enter Student Name: ");
        //check unique name method 

        //Create new student 
        Student newStudent = new Student(stuID, stuName);

        //Select course that is already available
        String courseCode = getValidCodEntity("\nEnter Course Code: ");
        Courses selectedCourse = findCourse(courseCode);
        if (selectedCourse == null) {
            System.out.println("Course not found!");
            return;
        }

        //Add course record to student
        newStudent.addCourseRecord(selectedCourse);
        Student.CourseRecord record = newStudent.getCourseRecord(selectedCourse);

        //Enter coursework mark for each component
        getCourseMark(selectedCourse, record);
        //Final exam
        getFinalMark(record);

        //Add student to list if not exist
        noExist(newStudent);
        System.out.println("Student mark saved successfully...");
    }

    public void getCourseMark(Courses selectedCourse, Student.CourseRecord record) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < selectedCourse.getTestComponentCount(); i++) {
            System.out.printf("Enter mark for %s: ",
                    selectedCourse.getTestComponentName(i));
            double mark = sc.nextDouble();
            sc.nextLine();//clear buffer
            record.setCourseworkMark(i, mark);
        }
    }

    public void getFinalMark(Student.CourseRecord record) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Proceed Final Exam Mark [Y/N]: ");
        String examChoice = sc.nextLine().trim();

        if (examChoice.equalsIgnoreCase("Y")) {
            System.out.print("Exam Mark: ");
            double examMark = sc.nextDouble();
            sc.nextLine(); // Clear buffer
            record.setFinalExamMark(examMark);
        }
    }

    public void noExist(Student newStudent) {
        Scanner sc = new Scanner(System.in);
        boolean studentExists = false;
        for (Student student : students) {
            if (student.getStuID().equals(newStudent.getStuID())) {
                studentExists = true;
                break;
            }
        }
        if (!studentExists) {
            students.add(newStudent);//Born new student
        }
    }

    public void editStuMark() {
        Scanner sc = new Scanner(System.in);
        //Check if students exist
        checkStuExist();
        String stuID = getValidCodEntity("\nEnter Student ID: ");

        //Find student
        Student studentToEdit = findStudentID(stuID);
        if (studentToEdit == null) {
            System.out.println("Student not found!");
            return;//Prompt for input again
        }
        //Check if the student ID is unique
        if (!isStuIDUnique(stuID)) {
            System.out.println("Student ID must be unique. Please enter a different ID!");
            return; //Prompt for input again
        }

        String courseCode = getValidCodEntity("Enter Course Code: ");

        //Find Course
        Courses selectedCourse = findCourse(courseCode);
        if (selectedCourse == null) {
            System.out.println("Course not found!");
            return;//Prompt for input again
        }
        //Check if the course code is unique
        if (!isCourseCodeUnique(courseCode)) {
            System.out.println("Course code must be unique. Please enter a different code!");
            return; //Prompt for input again
        }

        //Find student's course record
        Student.CourseRecord record = studentToEdit.getCourseRecord(selectedCourse);
        if (record == null) {
            System.out.println("No existing record for this course.");
            return;//Prompt for input again
        }

        //Allow user to edit coursework marks for each test component
        editCourseMark(selectedCourse, record);
        //Edit final exam mark
        editFEMark(record);
        System.out.println("Student mark updated successfully...\n");
    }

    public Student findStudentID(String stuID) {
        for (Student student : students) {
            if (student.getStuID().equalsIgnoreCase(stuID)) {
                System.out.println("Student Name: " + student.getStuName());
                return student;
            }
        }
        return null;
    }

    private boolean isStuIDUnique(String stuID) {
        for (Student student : students) {
            if (student.getStuID().equalsIgnoreCase(stuID)) {
                return false; //If a match is found, course code is not unique
            }
        }
        return true; //If no match is found, course code is unique
    }

    public Courses findCourse(String courseCode) {
        for (Courses course : courses) {
            if (course.getCode().equalsIgnoreCase(courseCode)) {
                System.out.println("Course Name: " + course.getName());
                return course;
            }
        }
        return null;//Course not found 
    }

    public void editCourseMark(Courses selectedCourse, Student.CourseRecord record) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < selectedCourse.getTestComponentCount(); i++) {
            System.out.printf("Current mark for %s: %.2f%n",
                    selectedCourse.getTestComponentName(i),
                    record.getCourseworkMark(i));
            System.out.printf("Enter new mark for %s: ",
                    selectedCourse.getTestComponentName(i));
            double mark = sc.nextDouble();
            sc.nextLine(); //Clear buffer
            record.setCourseworkMark(i, mark);
        }
    }

    public void editFEMark(Student.CourseRecord record) {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Current Exam Mark: %.2f%n", record.getFEMark());
        System.out.print("Proceed to Edit Final Exam Mark [Y/N]: ");
        String examChoice = sc.nextLine().trim();

        if (examChoice.equalsIgnoreCase("Y")) {
            System.out.print("New Exam Mark: ");
            double examMark = sc.nextDouble();
            sc.nextLine(); //Clear buffer
            record.setFinalExamMark(examMark);
        }
    }

    public void deleteStuMark() {
        Scanner sc = new Scanner(System.in);
        //Check if students exist
        checkStuExist();

        //Find student
        String stuID = getValidCodEntity("\nEnter Student ID: ");
        Student studentToDelete = findStudentID(stuID);
        if (studentToDelete == null) {
            System.out.println("Student not found!");
            return;
        }

        //Find Course
        String courseCode = getValidCodEntity("Enter Course Code: ");
        Courses selectedCourse = findCourse(courseCode);
        if (selectedCourse == null) {
            System.out.println("Course not found!");
            return;
        }

        //Confirm deletion
        System.out.print("Are you sure you want to delete this student's marks? [Y/N]: ");
        String confirm = sc.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            //remove course record
            studentToDelete.getResultsList().removeIf(record
                    -> record.getCourse().getCode().equalsIgnoreCase(courseCode));

            System.out.println("Student marks deleted successfully...\n");

            //optional; Remove student if no records remain
            if (studentToDelete.getResultsList().isEmpty()) {
                students.remove(studentToDelete);
                System.out.println("Student removed as no course records remain.");
            }
        } else {
            System.out.println("Deletion cancelled.\n");
        }
    }

    public void viewStuMark() {
        Scanner sc = new Scanner(System.in);
        //Check if students exist
        checkStuExist();

        //Find student
        String stuID = getValidCodEntity("\nEnter Student ID: ");
        Student studentToView = findStudentID(stuID);
        if (studentToView == null) {
            System.out.println("Student not found!");
            return;
        }

        //Display student details
        System.out.println("\nStudent Details:");
        System.out.println("Student ID: " + studentToView.getStuID());
        System.out.println("Student Name: " + studentToView.getStuName());

        //Display course records
        if (studentToView.getResultsList().isEmpty()) {
            System.out.println("No course records found.");
            return;
        }
        System.out.println("\nCourse Records:");
        for (Student.CourseRecord record : studentToView.getResultsList()) {
            Courses course = record.getCourse();

            //Display course record 
            displayRecord(record);
        }
    }

    public void displayRecord(Student.CourseRecord record) {
        Courses course = record.getCourse();

        //Course details
        System.out.println("\nCourse Code: " + course.getCode());
        System.out.println("Course Name: " + course.getName());

        //Coursework marks
        System.out.println("\nCoursework Marks:");
        for (int i = 0; i < course.getTestComponentCount(); i++) {
            System.out.printf("%s: %.2f%%%n",
                    course.getTestComponentName(i),
                    record.getCourseworkMark(i));
        }
        System.out.printf("Final Exam Mark: %.2f%%%n", record.getFEMark());
    }

    public void checkStuExist() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
    }
}

