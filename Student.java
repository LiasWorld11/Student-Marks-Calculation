package newpackage;

import java.util.ArrayList;

public class Student {
    private String stuID;
    private String stuName;
    private ArrayList<CourseRecord> resultsList; //list of course records for the student

    //constructor
    public Student(String id, String name) {
        stuID = id;
        stuName = name;
        resultsList = new ArrayList<>();
    }

    //inner class to represent students record for a specific course
    public class CourseRecord {
        private Courses course;
        private double feMark; //final exam mark
        private double[] cwMarks; //course work mark for each component

        //constructor
        public CourseRecord(Courses course) {
            this.course = course;
            this.feMark = 0.0;
            this.cwMarks = new double[course.getTestComponentCount()];
        }

        //getters for course
        public Courses getCourse() {
            return course;
        }

        //setters for marks
        public void setFinalExamMark(double mark) {
            this.feMark = mark;
        }

        public void setCourseworkMark(int index, double mark) {
            cwMarks[index] = mark;
        }

        //getters for marks
        public double getFEMark() {//return the final exam mark
            return feMark;
        }

        public double getCourseworkMark(int index) {//return the mark for the coursework component at the specified index
            return cwMarks[index]; 
        }
    }

    //method to add a new course record for the student
    public void addCourseRecord(Courses course) {
        resultsList.add(new CourseRecord(course)); //create a new CourseRecord object and add it to the results list
    }

    //ethod to retrieve the course record for a specific course
    public CourseRecord getCourseRecord(Courses course) {
        for (CourseRecord record:resultsList) {
            if (record.getCourse().equals(course)) { //check if the courses matche
                return record;
            }
        }
        return null; //if no matching record is found
    }

    //getter
    public String getStuID() {
        return stuID;
    }

    public String getStuName() {
        return stuName;
    }

    public ArrayList<CourseRecord> getResultsList() {
        return resultsList;
    }

    //setters
    public void setStuID(String id) {
        stuID = id;
    }

    public void setStuName(String name) {
        stuName = name;
    }

    @Override
    public String toString() {
        return "Student ID: " + stuID +
               "\nStudent Name: " + stuName;
    }
}
