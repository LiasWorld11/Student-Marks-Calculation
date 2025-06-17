package newpackage;

import java.util.ArrayList;

class Courses {
    private String code; 
    private String name; 
    private int feWeight; //final exam weight
    private int cwWeight; //course work weight
    private ArrayList<TestComponent> testCompList; //list to store all test components

    private static class TestComponent {
        private String name;
        private int weight;

        //constructor
        public TestComponent(String name, int weight) {
            this.name = name; 
            this.weight = weight;
        }
        
        //getters
        public String getName(){
            return name;
        }
        
        public int getWeight(){
            return weight;
        }
        
        @Override
        public String toString() {
            return "Test Component   : " + name + "\n" + //component name
                   "Mark Contribution: " + weight + "\n"; //weightage in marks
        }
    }

    //constructor
    public Courses(String code, String name, int feWeight, int cwWeight) {
        this.code = code;
        this.name = name;
        this.feWeight = feWeight;
        this.cwWeight = cwWeight;
        this.testCompList = new ArrayList<>(); //initialize empty list for test component
    }
    
    //check if a test component with the given name already exists
    public boolean hasTestComponent(String testComponentName) {
        for (TestComponent component : testCompList) {
            if (component.getName().equalsIgnoreCase(testComponentName)) {
                return true; //found a duplicate
            }
        }
        return false; //no duplicate found
    }

    public void addTestComponent(String name, int weight) {
        testCompList.add(new TestComponent(name, weight)); //create and add a new test component to the list
    }

    //getters
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getFinalExamWeight() {
        return feWeight;
    }

    public int getCourseWorkWeight() {
        return cwWeight;
    }

    public int getTestComponentCount() {
        return this.testCompList.size();
    }

    public String getTestComponentName(int index) {
        if (index >= 0 && index < testCompList.size()) {
            return testCompList.get(index).name;
        }
        return "Test Component " + (index + 1);
    }

    //method to clear all test components from the course (user enter wrongly and have to reenter)
    public void clearTestComponents() {
        this.testCompList.clear();
    }

    @Override
    public String toString() {
        String result = "Course Code      : " + code + "\n" +
                        "Course Name      : " + name + "\n" +
                        "Final Exam Weight: " + feWeight + "%\n" +
                        "Coursework Weight: " + cwWeight + "%\n";

        //check if there are test components to include in the representation
        if (!testCompList.isEmpty()) {
            result += """
                      \n---------------
                      Test Components
                      ---------------\n""";
            for (int i = 0; i < testCompList.size(); i++) {
                TestComponent test = testCompList.get(i);
                result += "Component " + "[" + (i + 1) + "]\n" + test.toString() + "\n";
            }
        }
        return result;
    }
}