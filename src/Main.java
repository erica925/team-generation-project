
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;
import java.io.*;
import java.util.ListIterator;
import java.util.Scanner;

/**
 *
 * @author Erica Oliver, Wintana Yosief

 * @version 1.2 - Nov 28, 2021

 */

public class Main {
    private static ArrayList<Student> students;
    private static double maximumGroupSize;
    private static double minimumGroupSize;
    private static ArrayList<ArrayList<Student>> groups;
    private static int totalStudents;

    public static void main(String args[]) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the CSV file");
        String filename = scanner.nextLine();
        validateFile(filename);
        groups = new ArrayList<>();
        maximumGroupSize = 4;
        minimumGroupSize = 3;
        totalStudents = students.size();
        ArrayList<ArrayList<Student>> labSectionGroups = sortLabSections(students);
        for(ArrayList<Student> labSectionGroup: labSectionGroups){
            sortPrograms(labSectionGroup);
        }
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
        if(header.contains("Student Name,Student ID,Program,Grade,Lab Section,Email")) {
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
            writer.append("\n");
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
               // students.get(0).setGroupNum(groupCounter);
                students.remove(0);
                if (students.size() == 0){
                    break;
                }
            }
            groupCounter++;
        }
    }

    /**
     * Sorting students into groups based on programs
     * At most two students are from the same program
     * Students must be sorted into groups of 3 or 4
     *
     * @param labSectionStudents The list of students to be sorted
     */
    public static void sortPrograms(ArrayList<Student> labSectionStudents){
        int num3Groups = 0; //the number of groups with 3 students
        int num4Groups = 0; //the number of groups with 4 students
        if (labSectionStudents.size()%4 == 0) {
            num3Groups = 0;
            num4Groups = labSectionStudents.size()/4;
        }
        if (labSectionStudents.size()%4 == 1) {
            num3Groups = 3;
            num4Groups = (labSectionStudents.size()-9)/4;
        }
        if (labSectionStudents.size()%4 == 2) {
            num3Groups = 2;
            num4Groups = (labSectionStudents.size()-6)/4;
        }
        if (labSectionStudents.size()%4 == 3) {
            num3Groups = 1;
            num4Groups = (labSectionStudents.size()-3)/4;
        }
        int numGroups = num3Groups + num4Groups;

        // Creates groups and adds a team leader
        ArrayList<ArrayList<Student>> sortedGroups = new ArrayList<ArrayList<Student>>();
        for(int i = 0; i < numGroups; i++){
           ArrayList<Student> group = new ArrayList<>();
           boolean foundLeader = false;
           for (Student s : labSectionStudents){
               if (s.isDefaultLeader()) {
                   group.add(s);
                   labSectionStudents.remove(s);
                   foundLeader = true;
                   break;
               }
           }
           if (!foundLeader){
               for (Student s : labSectionStudents){
                   if (s.isBackupLeader()) {
                       group.add(s);
                       labSectionStudents.remove(s);
                       foundLeader = true;
                       break;
                   }
               }
           }
           if (!foundLeader) group.add(labSectionStudents.get(0));
           group.get(0).setGroupNum(labSectionStudents.get(0).getLabSection() + ".G" + (i + 1));
           sortedGroups.add(group);
        }

       ListIterator<Student> studentsIterator = labSectionStudents.listIterator();
       boolean groupFound = false;
       // Iterates over students
       while(studentsIterator.hasNext()) {
           Student s = studentsIterator.next();
           //System.out.println(s.getName());
           // Iterates over groups
           for (int i = 0; i < sortedGroups.size(); i++) {
               double groupSize;
               if (i < num4Groups) groupSize = maximumGroupSize;
               else  groupSize = minimumGroupSize;

               ArrayList<Student> group = sortedGroups.get(i);
               ListIterator<Student> groupIterator = group.listIterator();
               // Iterating over each student in a single group
               while (groupIterator.hasNext()) {
                   Student student = groupIterator.next();
                   // Checking if students are in the same program
                   if (!s.sameProgram(s, student) && group.size() < groupSize) {
                       groupIterator.add(s); // adds student to group
                       groupFound = true;
                       s.setGroupNum(s.getLabSection() + ".G" + (i + 1));
                       studentsIterator.remove(); // removes student from list
                       break;
                   } else {
                       if(group.size() < groupSize){
                            // If only one student has matching program add student
                            int matchingPrograms = countMatchingPrograms(s, group);
                            if(matchingPrograms < 2) {
                                groupIterator.add(s); // adds student to group
                                groupFound = true;
                                s.setGroupNum(s.getLabSection() + ".G" + (i + 1));
                                studentsIterator.remove(); // removes student from list
                                break;
                            }
                       }
                   }
               }
               if(groupFound){
                   groupFound = false;
                   break;
               }
           }
       }
       groups.addAll(sortedGroups);
    }

    /**
     * Counts how many students have matching programs with the given Student
     * @param s The student
     * @param group The group
     * @return
     */
    private static int countMatchingPrograms(Student s, ArrayList<Student> group) {
        int count = 0;
        for (Student student : group) {
            if (s.sameProgram(s, student) && !s.equals(student)) {
                count += 1;
            }
        }
        return count;
    }

     /**
     * Split the list of students by lab section
     *
     * @param students The list of students to be sorted
     */
    public static ArrayList<ArrayList<Student>> sortLabSections(ArrayList<Student> students){
        ArrayList<ArrayList<Student>> labSections = new ArrayList<ArrayList<Student>>();
        while (!students.isEmpty()){
            ArrayList<Student> group = new ArrayList<>();
            //groups.add(group);
            labSections.add(group);
            //compare the lab section of the first student in the list to every other student
            Student stud = students.get(0);
            group.add(stud);
            students.remove(stud);
            for (int i = 0; i < students.size(); i++){
                if (stud.getLabSection().equals(students.get(i).getLabSection())){
                    group.add(students.get(i));
                    students.remove(i);
                    i--;
                }
            }
        }
        return labSections;
    }

    /**
     * Sort each student by their grade (A+,A,A-,...)
     * Each group shall have a multiple of 4 students or
     * a multiple of 4 + 3
     *
     * @param students The list of students to be sorted by grade
     * @return A list of groups
     */
    public static ArrayList<ArrayList<Student>> sortGrades(ArrayList<Student> students){
        ArrayList<ArrayList<Student>> grades = new ArrayList<>();
        while (!students.isEmpty()) {
            ArrayList<Student> group = new ArrayList<>();
            grades.add(group);
            Student stud = students.get(0);
            group.add(stud);
            students.remove(stud);
            for (int i = 0; i < students.size(); i++){

            }
        }
        return grades;
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
        ArrayList<String> groupsOf4 = new ArrayList(); //the groups that have 4 students
        ArrayList<String> groupsOf3 = new ArrayList(); //the groups that have 3 students
        ArrayList<String> groupsOfInvalid = new ArrayList(); // the groups that have an invalid number of students
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
        writer.append("\nThe percentage of groups that adhere to the group size criterion is " + groupsOf4.size()*100/groups.size() + "%\n");

        //check lab section criteria - each group should have students all from the same lab section
        //for each group {count groups with all same lab section and which ones have mixed lab sections}
        ArrayList<String> diffLabs = new ArrayList(); //the number of groups in which NOT all students are registered in the same lab section
        ArrayList<String> sameLabs = new ArrayList(); //the number of groups in which all students are registered in the same lab section
        for (ArrayList<Student> group : groups){
            for (int i = 0; i < group.size()-1; i++){
                for (int j = i + 1; j < group.size(); j++){
                    if (!group.get(i).getLabSection().equals(group.get(j).getLabSection())){
                        diffLabs.add(group.get(0).getGroupNum());
                        break;
                    }
                }
            }
            sameLabs.add(group.get(0).getGroupNum());
        }
        writer.append("\nThe number of groups in which all students are registered in the same lab section is " + sameLabs.size() + " : " + sameLabs);
        writer.append("\nThe number of groups in which not all students are registered in the same lab section is " + diffLabs.size() + " : " + diffLabs);
        writer.append("\nThe percentage of groups that adhere to the lab section criterion is " + sameLabs.size()*100/groups.size() + "%");
        writer.append("\nThe groups that do not adhere to the lab section criterion are: " + diffLabs.toString() + "\n");

        //next check the programs criteria
        ArrayList<String> repeatPrograms = new ArrayList<>();
        ArrayList<String> uniquePrograms = new ArrayList<>();
        for (ArrayList<Student> group : groups){
            boolean hasRepeat = false;
            for (Student student : group){
                if (countMatchingPrograms(student, group) > 0 && !hasRepeat) {
                    System.out.println(group.get(0).getGroupNum());
                    System.out.println(countMatchingPrograms(student,group));
                    hasRepeat = true;
                    repeatPrograms.add(student.getGroupNum());
                }
            }
            if (!hasRepeat) uniquePrograms.add(group.get(0).getGroupNum());
        }
        System.out.println(uniquePrograms);
        System.out.println(repeatPrograms);
        writer.append("\nThe number of groups that adhere to the programs criteria is " + (uniquePrograms.size()) + " : " + uniquePrograms);
        writer.append("\nThe number of groups that adhere do not to the programs criteria is " + (repeatPrograms.size()) + " : " + repeatPrograms);
        writer.append("\nThe percentage of groups that adhere to the programs criteria is " + uniquePrograms.size()*100/groups.size() + "%");

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

        writer.flush();
        writer.close();
    }
}
