
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
 * @version 1.1 - Nov 5, 2021
 */

public class Main {
    static ArrayList<Student> students;
    static double maximumGroupSize;
    static ArrayList<ArrayList> groups;

    public static void main(String args[]) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the CSV file");
        String filename = scanner.nextLine();
        validateFile(filename);
        groups = new ArrayList<>();
        maximumGroupSize = 4;
        sort(students);
        writeCSV();
    }
  
  /**
     * Reads a CSV file into an ArrayList of students
     * @param filename The filename inputted by the user
     * @throws IOException
     */
    public static void readCSV(String filename) throws IOException {

        List<Student> students = new ArrayList<>(); // the list of students
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

        // reads the first line (headers of CSV file)
        String header = bufferedReader.readLine();

        // reads the first student
        String line = bufferedReader.readLine();

        // the header of the CSV file must be "Student Name"
        if(header.contains("Student Name")) {

            while (line != null) {
                // gets the student's name
                String [] student = line.split(",");
                // adds name
                students.add(new Student(student[0]));
                // next line
                line = bufferedReader.readLine();
            }
            for(Student s: students){
                System.out.println(s);
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
        writer.append("Name,Student ID,Program,Grade,Email Address,Group Number\n");
        for (ArrayList<Student> group : groups){
            for (Student student : group){
                writer.append(student.csvRepresentation());
            }
        }
        writer.flush();
        writer.close();
    }

    /**
     * Sort the students into groups following the optimization criteria
     *
     * For iteration 1 there is no criteria, they are just split into groups of 4
     *
     * @param students The list of students to be grouped
     */
    public static void sort(ArrayList<Student> students) {
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
     * Print an optimization summary of the formed groups
     * This is used to compare algorithms/methods of creating the groups
     * to see which adheres best to the requirements
     *
     * IN PROGRESS
     */
    public static void optimizationSummary() throws IOException {
        FileWriter writer = new FileWriter("Optimization Summary.txt");
        //first check the group size criteria
        int count4 = 0;
        int count3 = 0;
        int countInvalid = 0;
        for (ArrayList<ArrayList> g: groups){
            if (g.size() == 4) count4++;
            else if (g.size() == 4) count3++;
            else countInvalid++;
        }
        writer.append("The number of groups with 4 students is " + count4);
        writer.append("The number of groups with 4 students is " + count3);
        writer.append("The number of groups with an invalid number of students is " + countInvalid);

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
