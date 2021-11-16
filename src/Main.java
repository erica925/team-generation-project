
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;
import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Erica Oliver, Wintana Yosief
 * @version 1.2 - Nov 15, 2021
 */

public class Main {
    private static ArrayList<Student> students;
    private static double maximumGroupSize;
    private static ArrayList<ArrayList<Student>> groups;
    private static int totalStudents;

    public static void main(String args[]) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the CSV file");
        String filename = scanner.nextLine();
        validateFile(filename);
        groups = new ArrayList<>();
        maximumGroupSize = 4;
        totalStudents = students.size();
        //simpleSort(students);
        sortLabSections(students);
        writeCSV();
        optimizationSummary(filename);
    }
  
  /**
     * Reads a CSV file into an ArrayList of students
     * @param filename The filename inputted by the user
     * @throws IOException
     */
    public static void readCSV(String filename) throws IOException {
        //List<Student> students = new ArrayList<>(); // the list of students
        students = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

        // reads the first line (headers of CSV file)
        String header = bufferedReader.readLine();

        // reads the first student
        String line = bufferedReader.readLine();

        // the header of the CSV file must be "Student Name, Student ID, Program, Grade, Lab Section, Email"
        if(header.contains("Student Name, Student ID, Program, Grade, Lab Section, Email")) {
            while (line != null) {
                // gets the student's info
                String [] student = line.split(",");
                // adds info
                students.add(new Student(student[0], student[1], student[2], student[3], student[4], student[5]));
                // next line
                line = bufferedReader.readLine();
            }
        } else {
            System.out.println("Invalid header name");
        }
    }
  
  /**
     * Checks if the inputted file is a CSV file
     * @param filename The filename inputted by the user
     * @throws IOException
     */
    public static void validateFile(String filename) throws IOException {
        // Checking file extension
        if(filename.endsWith(".csv")) {
            readCSV(filename);
        } else {
            System.out.println("Invalid file type");
        }
    }

    /**
     * Write the groups to a CSV file which can be opened in Excel
     * @throws IOException
     */
    public static void writeCSV() throws IOException {
        FileWriter writer = new FileWriter("groups.csv");
        writer.append("Name,Student ID,Program,Grade,Lab Section,Email Address,Group Number\n");
        for (ArrayList<Student> group : groups){
            for (Student student : group){
                writer.append(student.csvRepresentation());
            }
        }
        writer.flush();
        writer.close();
    }

    /**
     * Just split the students into groups of 4
     *
     * @param students The list of students to be grouped
     */
    public static void simpleSort(ArrayList<Student> students) {
        int groupCounter = 1;
        for (int x = 0; x <= Math.ceil(students.size()/maximumGroupSize); x++) {
            ArrayList group = new ArrayList<>();
            groups.add(group);
            for (int n = 1; n <= maximumGroupSize; n++) {
                group.add(students.get(0));
                students.get(0).setGroupNum(groupCounter);
                students.remove(0);
                if (students.size() == 0){
                    break;
                }
            }
            groupCounter++;
        }
    }

    /**
     * Split the list of students by lab section
     *
     * @param students The list of students to be sorted
     */
    public static void sortLabSections(ArrayList<Student> students){
        while (!students.isEmpty()){
            ArrayList<Student> group = new ArrayList<>();
            groups.add(group);
            //compare the lab section of the first student in the list to every other student
            Student stud = students.get(0);
            group.add(stud);
            students.remove(stud);
            for (int i = 0; i < students.size(); i++){
                if (stud.getLabSection().equals(students.get(0).getLabSection())){
                    group.add(students.get(0));
                    students.remove(0);
                }
            }
        }
    }

    /**
     * Print an optimization summary of the formed groups
     * This is used to compare algorithms/methods of creating the groups
     * to see which adheres best to the requirements
     *
     * IN PROGRESS
     */
    public static void optimizationSummary(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename + " - optimization summary.txt");

        //first check the group size criteria
        //check the groups that have 4, 3 or an invalid number of students
        ArrayList<Integer> groupsOf4 = new ArrayList(); //the groups that have 4 students
        ArrayList<Integer> groupsOf3 = new ArrayList(); //the groups that have 3 students
        ArrayList<Integer> groupsOfInvalid = new ArrayList(); // the groups that have an invalid number of students
        for (ArrayList<Student> group: groups){
            if (group.size() == 4) groupsOf4.add(group.get(0).getGroupNum());
            else if (group.size() == 3) groupsOf3.add(group.get(0).getGroupNum());
            else groupsOfInvalid.add(group.get(0).getGroupNum());
        }
        //check how many groups of 3 should be expected based on the number of students registered
        int x = -1;
        if (totalStudents%4 == 0) x = 0;
        else if (totalStudents%4 == 1) x = 3;
        else if (totalStudents%4 == 2) x = 2;
        else if (totalStudents%4 == 3) x = 1;
        writer.append("\nSince there are " + totalStudents + " students, we should at least expect " + x + " group(s) of 3" );
        writer.append("\nThe number of groups with 4 students is " + groupsOf4.size() + " : " + groupsOf4);
        writer.append("\nThe number of groups with 3 students is " + groupsOf3.size() + " : " + groupsOf3);
        writer.append("\nThe number of groups with an invalid number of students is " + groupsOfInvalid.size() + " : " + groupsOfInvalid);
        //writer.append("\nThe percentage of groups that adhere to the group size criterion is " + groupsOf4.size()*100/groups.size() + "\n");

        /*
        //next check the team leader criteria
        int hasDefaultLeader = 0;
        int hasBackupLeader = 0;
        int hasNoLeader = 0;
        for (ArrayList<Student> group : groups){
            if (group.get(0).isDefaultLeader()) hasDefaultLeader++;
            else if (group.get(0).isBackupLeader()) hasBackupLeader++;
            else hasNoLeader++;
        }
        writer.append("The number of groups with a team leader from software or computer systems engineering is " + hasDefaultLeader);
        writer.append("The number of groups with a team leader from another SYSC program is " + hasBackupLeader);
        writer.append("The number of groups with no team leader is " + hasNoLeader);

        //check the mix of programs criteria
        //check the grade criteria
        */


        //check lab section criteria - each group should have students all from the same lab section
        //for each group {count groups with all same lab section and which ones have mixed lab sections}
        int countDiffLabs = 0; //the number of groups in which NOT all students are registered in the same lab section
        ArrayList<ArrayList> diffLabs = new ArrayList();
        for (ArrayList<Student> group : groups){
            for (int i = 0; i < group.size()-1; i++){
                for (int j = 1; j < group.size(); i++){
                    if (!group.get(i).getLabSection().equals(group.get(j).getLabSection())){
                        diffLabs.add(group);
                        break;
                    }
                }
            }
        }
        int countSameLab = groups.size()-countDiffLabs; //the number of groups in which all students are registered in the same lab section
        writer.append("\nThe number of groups in which all students are registered in the same lab section is " + countSameLab);
        writer.append("\nThe number of groups in which not all students are registered in the same lab section is " + countDiffLabs);
        //writer.append("\nThe percentage of groups that adhere to the lab section criterion is " + countSameLab*100/groups.size());
        writer.append("\nThe groups that do not adhere to the lab section criterion are: \n" + diffLabs.toString());

        writer.flush();
        writer.close();
    }

    /* IN PROGRESS
    /**
     * Convert grades to letter grades without +/-
     *
     * A -> 80-100
     * B -> 70-79
     * C -> 60-69
     * D -> 50-59
     * F -> below 50
     *
     * @param students
     * @return
     */
    /*public ArrayList<Student> convertLetterGrade(ArrayList<Student> students){
        for (Student s : students){
            if (s.getGrade() > 80){
                s.setGrade("A");
            }
        }
        return students;
    }*/
}
