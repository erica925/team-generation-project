
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.lang.Math;
import java.io.*;


/**
 * @author Erica Oliver, Wintana Yosief
 * @version 3 - Feb 02, 2022
 */
public class Main {
    private static ArrayList<Student> students;
    private static int maximumGroupSize;
    private static int minimumGroupSize;
    private static ArrayList<ArrayList<Student>> groups;
    private static int totalStudents;

    private static boolean teamLeaderFlag;
    private static boolean programsFlag;
    private static boolean gradeFlag;
    private static boolean labSectionFlag;


    /**
     * Main method where each of the sorting methods are invoked
     *
     * @param args args
     * @throws IOException throws IOException
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the CSV file");
        String filename = scanner.nextLine();
        readCSV(filename);

        //set flags
        labSectionFlag = true;
        gradeFlag = true;
        teamLeaderFlag = true;
        programsFlag = false;

        groups = new ArrayList<>(); //final groups
        maximumGroupSize = 4; //from user input
        minimumGroupSize = maximumGroupSize - 1;
        totalStudents = students.size();

        sort();
        assignGroupNumbers();
        writeCSV(filename);
        optimizationSummary(filename);
    }

    /**
     * The main sorting method where other sorting methods are called
     */
    public static void sort() {

        groups = new ArrayList<>();
        totalStudents = students.size();

        ArrayList<ArrayList<Student>> allStudents = new ArrayList<>();
        allStudents.add(students);

        ArrayList<ArrayList<Student>> labSectionGroups = new ArrayList<>();
        // sort by lab section
        if (labSectionFlag) {
            for (ArrayList<Student> labSectionGroup : allStudents) {
                labSectionGroups.addAll(sortLabSections(labSectionGroup));
            }
        } else labSectionGroups = allStudents;

        ArrayList<ArrayList<Student>> gradeGroups = new ArrayList<>();
        // sort by grade
        if (gradeFlag) {
            for (ArrayList<Student> labSectionGroup : labSectionGroups) {
                gradeGroups.addAll(sortGrades(labSectionGroup));
            }
        } else gradeGroups = labSectionGroups;


        if (teamLeaderFlag && programsFlag) {
            for (ArrayList<Student> gradeGroup : gradeGroups) {
                ArrayList<ArrayList<Student>> teamLeaderGroups = getTeamLeaders(gradeGroup);
                groups.addAll(teamLeaderGroups);
                sortPrograms(teamLeaderGroups, gradeGroup);
            }
        }
        else if (teamLeaderFlag) {
            for (ArrayList<Student> gradeGroup : gradeGroups) {
                ArrayList<ArrayList<Student>> teamLeaderGroups = getTeamLeaders(gradeGroup);
                groups.addAll(teamLeaderGroups);

                //fill with students from any program
                int i = getNumGroupsOfMinSize(teamLeaderGroups.size() + gradeGroup.size());
                for (ArrayList<Student> group : teamLeaderGroups) {
                    if (i > 0) {
                        for (int j = 1; j < minimumGroupSize; j++) {
                            group.add(gradeGroup.get(0));
                            gradeGroup.remove(0);
                        }
                        i--;
                    } else {
                        for (int j = 1; j < maximumGroupSize; j++) {
                            group.add(gradeGroup.get(0));
                            gradeGroup.remove(0);
                        }
                    }
                }
            }
        }
        else if (programsFlag) {
            for (ArrayList<Student> gradeGroup : gradeGroups) {
                int numGroupsOfMinSize = getNumGroupsOfMinSize(gradeGroup.size());
                int numGroupsOfMaxSize = (gradeGroup.size() - numGroupsOfMinSize * minimumGroupSize) / maximumGroupSize;
                int numGroups = numGroupsOfMinSize + numGroupsOfMaxSize;
                ArrayList<ArrayList<Student>> emptyGroups = new ArrayList<>();
                for (int i = 0; i < numGroups; i++){
                    ArrayList<Student> a = new ArrayList<>();
                    a.add(gradeGroup.get(0));
                    gradeGroup.remove(0);
                    emptyGroups.add(a);
                }
                groups.addAll(emptyGroups);
                sortPrograms(emptyGroups, gradeGroup);
            }
        }
        else {
            for (ArrayList<Student> gradeGroup : gradeGroups) {
                simpleSort(gradeGroup);
            }
        }
    }

    /**
     * Reads a CSV file into an ArrayList of students
     *
     * @param filename The filename inputted by the user
     * @throws IOException throws IOException
     */
    public static void readCSV(String filename) throws IOException {
        if (!filename.endsWith(".csv")) {
            System.out.println("Invalid file type, must end with '.csv'"); //change to popup warning in gui
        }

        students = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

        // reads the first line (headers of CSV file)
        String header = bufferedReader.readLine();

        // reads the first student
        String line = bufferedReader.readLine();

        // the header of the CSV file must be "Student Name, Student ID, Program, Grade, Lab Section, Email"
        if (header.contains("Student Name,Student ID,Program,Grade,Lab Section,Email")) {
            while (line != null) {
                // gets the student's info
                String[] student = line.split(",");

                // adds info
                students.add(new Student(student[0], student[1], student[2], student[3], student[4], student[5]));
                // next line
                line = bufferedReader.readLine();
            }

        } else {
            System.out.println("Invalid header name");
            GUIMain.invalidFileHeaders();

        }
    }

    /**
     * Write the groups to a CSV file which can be opened in Excel
     *
     * @throws IOException throws IOException
     */
    public static void writeCSV(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename + "_groups.csv");
        writer.append("Name,Student ID,Program,Grade,Lab Section,Email Address,Group Number\n");
        for (ArrayList<Student> group : groups) {
            for (Student student : group) {
                writer.append(student.csvRepresentation());
            }
            writer.append("\n");
        }
        writer.flush();
        writer.close();
    }

    /**
     * Just split the students into groups of maxGroupSize in the
     * order they appear in the inputted list
     *
     * @param students The list of students to be grouped
     */
    public static void simpleSort(ArrayList<Student> students) {
        for (int x = 0; x <= Math.ceil(students.size() / maximumGroupSize); x++) {
            ArrayList group = new ArrayList<>();
            groups.add(group);
            for (int n = 1; n <= maximumGroupSize; n++) {
                group.add(students.get(0));
                students.remove(0);
                if (students.size() == 0) {
                    break;
                }
            }
        }
    }

    /**
     * Creates groups with only a team leader assigned
     *
     * @param students The list of students to be sorted
     */
    public static ArrayList<ArrayList<Student>> getTeamLeaders(ArrayList<Student> students){
        // Get the number of groups that will be the maximum and minimum sizes
        int numGroupsOfMinSize = getNumGroupsOfMinSize(students.size());
        int numGroupsOfMaxSize = (students.size() - numGroupsOfMinSize * minimumGroupSize) / maximumGroupSize;
        int numGroups = numGroupsOfMinSize + numGroupsOfMaxSize; // the total number of groups

        // Creates groups and adds a team leader
        ArrayList<ArrayList<Student>> sortedGroups = new ArrayList<ArrayList<Student>>();
        for (int i = 0; i < numGroups; i++) {
            ArrayList<Student> group = new ArrayList<>();
            boolean foundLeader = false;
            for (Student s : students) {
                if (s.isDefaultLeader()) {
                    group.add(s);
                    students.remove(s);
                    foundLeader = true;
                    break;
                }
            }
            if (!foundLeader) {
                for (Student s : students) {
                    if (s.isBackupLeader()) {
                        group.add(s);
                        students.remove(s);
                        foundLeader = true;
                        break;
                    }
                }
            }
            if (!foundLeader) {
                group.add(students.get(0));
                students.remove(0);
            }
            sortedGroups.add(group);
        }
        return sortedGroups;
    }

    /**
     * Sorting students into groups based on programs
     * At most two students are from the same program may be sorted into the same group
     * Students must be sorted into groups of maximumGroupSize or minimumGroupSize
     *
     * The method takes a list of groups that may or may not already have a team leader
     * assigned then fills those groups with the list of students given
     *
     * @param groups The pre-made groups to be filled
     * @param students The list of students to be sorted
     */
    public static void sortPrograms(ArrayList<ArrayList<Student>> groups, ArrayList<Student> students) {
        // Get the number of groups that will be the maximum and minimum sizes
        int numGroupsOfMinSize = getNumGroupsOfMinSize(students.size() + groups.size()*groups.get(0).size()); //groups may or may not have been assigned a team leader
        int numGroupsOfMaxSize = groups.size() - numGroupsOfMinSize;
        //int numGroups = numGroupsOfMinSize + numGroupsOfMaxSize; // the total number of groups

        ListIterator<Student> studentsIterator = students.listIterator();
        boolean groupFound = false;
        // Iterates over students
        while (studentsIterator.hasNext()) {
            Student s = studentsIterator.next();
            // Iterates over groups
            for (int i = 0; i < groups.size(); i++) {
                int groupSize;
                if (i < numGroupsOfMaxSize) groupSize = maximumGroupSize;
                else groupSize = minimumGroupSize;

                ArrayList<Student> group = groups.get(i);
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
                        if (group.size() < groupSize) {
                            // If only one student has matching program add student
                            int matchingPrograms = countMatchingPrograms(s, group);
                            if (matchingPrograms < 2) {
                                groupIterator.add(s); // adds student to group
                                groupFound = true;
                                s.setGroupNum(s.getLabSection() + ".G" + (i + 1));
                                studentsIterator.remove(); // removes student from list
                                break;
                            }
                        }
                    }
                }
                if (groupFound) {
                    groupFound = false;
                    break;
                }
            }
        }
    }

    /**
     * Counts how many students have matching programs with the given Student
     *
     * @param s     The student
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
    public static ArrayList<ArrayList<Student>> sortLabSections(ArrayList<Student> students) {
        ArrayList<ArrayList<Student>> labSections = new ArrayList<ArrayList<Student>>();
        while (!students.isEmpty()) {
            ArrayList<Student> group = new ArrayList<>();
            labSections.add(group);
            //compare the lab section of the first student in the list to every other student
            Student stud = students.get(0);
            group.add(stud);
            students.remove(stud);
            for (int i = 0; i < students.size(); i++) {
                if (stud.getLabSection().equals(students.get(i).getLabSection())) {
                    group.add(students.get(i));
                    students.remove(i);
                    i--;
                }
            }
        }
        return labSections;
    }



    /**
     * Splits the students into subgroups according to their grades
     * @param students The list of students
     * @return a list of groups containing students with all the same grade
     */
    public static ArrayList<ArrayList<Student>> sortGrades(ArrayList<Student> students) {
        ArrayList<ArrayList<Student>> gradeGroup = new ArrayList<>();
        ArrayList<Student> groupAp = new ArrayList<>();
        ArrayList<Student> groupA = new ArrayList<>();
        ArrayList<Student> groupAm = new ArrayList<>();
        ArrayList<Student> groupBp = new ArrayList<>();
        ArrayList<Student> groupB = new ArrayList<>();
        ArrayList<Student> groupBm = new ArrayList<>();
        ArrayList<Student> groupCp = new ArrayList<>();
        ArrayList<Student> groupC = new ArrayList<>();
        ArrayList<Student> groupCm = new ArrayList<>();
        ArrayList<Student> groupDp = new ArrayList<>();
        ArrayList<Student> groupD = new ArrayList<>();
        ArrayList<Student> groupDm = new ArrayList<>();
        ArrayList<Student> groupF = new ArrayList<>();

        gradeGroup.add(groupAp);
        gradeGroup.add(groupA);
        gradeGroup.add(groupAm);
        gradeGroup.add(groupBp);
        gradeGroup.add(groupB);
        gradeGroup.add(groupBm);
        gradeGroup.add(groupCp);
        gradeGroup.add(groupC);
        gradeGroup.add(groupCm);
        gradeGroup.add(groupDp);
        gradeGroup.add(groupD);
        gradeGroup.add(groupDm);
        gradeGroup.add(groupF);

        for (Student s : students) {
            if (s.getGrade().equals("A+")) groupAp.add(s);
            else if (s.getGrade().equals("A")) groupA.add(s);
            else if (s.getGrade().equals("A-")) groupAm.add(s);
            else if (s.getGrade().equals("B+")) groupBp.add(s);
            else if (s.getGrade().equals("B")) groupB.add(s);
            else if (s.getGrade().equals("B-")) groupBm.add(s);
            else if (s.getGrade().equals("C+")) groupCp.add(s);
            else if (s.getGrade().equals("C")) groupC.add(s);
            else if (s.getGrade().equals("C-")) groupCm.add(s);
            else if (s.getGrade().equals("D+")) groupDp.add(s);
            else if (s.getGrade().equals("D")) groupD.add(s);
            else if (s.getGrade().equals("D-")) groupDm.add(s);
            else if (s.getGrade().equals("F")) groupF.add(s);
        }

        ListIterator<ArrayList<Student>> groupIterator = gradeGroup.listIterator();
        while (groupIterator.hasNext()) {
            if (groupIterator.next().isEmpty()) {
                groupIterator.remove();
            }
        }

        //move some students to the first grade level (number of students to be sorted into the smaller groups)
        int numGroupsOfMinSize = getNumGroupsOfMinSize(students.size());
        int numStudents = numGroupsOfMinSize * minimumGroupSize;
        if (numGroupsOfMinSize != 0) {
            while (gradeGroup.get(0).size() < numStudents) {
                //move a student from the next grade level to the first one
                gradeGroup.get(0).add(gradeGroup.get(1).get(0));
                gradeGroup.get(1).remove(0);
                if (gradeGroup.get(1).isEmpty()) gradeGroup.remove(1);
            }
            while (gradeGroup.get(0).size() > numStudents) {
                //remove excess students to next grade level
                gradeGroup.get(1).add(gradeGroup.get(0).get(0));
                gradeGroup.get(0).remove(0);
            }
        }

        // Check that each grade level has a multiple of maximumGroupSize students
        // If smaller groups are needed then start from the second grade level
        int i = 0;
        if (numGroupsOfMinSize != 0) i = 1;
        for (; i < gradeGroup.size() - 1; i++) {
            if (gradeGroup.get(i).isEmpty()) gradeGroup.remove(i);
            if (gradeGroup.get(i).size() % maximumGroupSize != 0) { //need to add more students
                for (int x = 0; x < gradeGroup.get(i).size() % maximumGroupSize; x++) { //x is the number of students needed
                    if (i == gradeGroup.size() - 1) break; //this is the last group
                    //fill with students from the lower grade level
                    gradeGroup.get(i).add(gradeGroup.get(i + 1).get(0));
                    gradeGroup.get(i + 1).remove(0);
                    if (gradeGroup.get(i + 1).isEmpty()) {
                        gradeGroup.remove(i + 1);
                    }
                }
            }
        }
        return gradeGroup;
    }

    /**
     * Assign group numbers to the finished groups
     */
    public static void assignGroupNumbers() {
        for (int i = 0; i < groups.size(); i++) {
            ArrayList<Student> g = groups.get(i);
            for (int j = 0; j < g.size(); j++) {
                g.get(j).setGroupNum(g.get(j).getLabSection() + ".G" + (i + 1));
            }
        }
    }

    /**
     * Checks how many groups of nimGroupSize are needed since we ideally want groups of maxGroupSize
     * and only create groups of minGroupSize when necessary
     *
     * @param size The number of students in the section
     * @return the number of groups of minGroupSize that are necessary
     */
    public static int getNumGroupsOfMinSize(int size) {
        if (size % maximumGroupSize == 0) return 0;
        else return (maximumGroupSize - (size % maximumGroupSize));
    }

    /**
     * Sets the maximum group size variable to the spinner value
     * from the GUI
     * @param value
     */
    public static void setMaximumGroupSize(int value){
        maximumGroupSize = value;
    }

    public static void setMinimumGroupSize(){
        minimumGroupSize = maximumGroupSize - 1;
    }

    /**
     * Sets the lab section flag value
     * from GUI
     * @param value
     */
    public static void setLabSectionFlag(boolean value){
        labSectionFlag = value;
    }

    /**
     * Sets the grade flag value
     * @param value
     */
    public static void setGradeFlag(boolean value){
        gradeFlag = value;
    }

    /**
     * Sets the programs flag value
     * @param value
     */
    public static void setProgramsFlag(boolean value){
        programsFlag = value;
    }

    /**
     * Sets the team leader flag value
     * @param value
     */
    public static void setTeamLeaderFlag(boolean value){
        teamLeaderFlag = value;
    }


    /**
     * Print an optimization summary of the formed groups
     * This is used to compare algorithms/methods of creating the groups
     * to see which adheres best to the requirements
     */
    public static void optimizationSummary(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename + "_optimization_summary.txt");
        writer.append("Optimization Summary: \n");

        //check the group size criteria
        //check the groups that have the maximum, minimum, or an invalid number of students
        ArrayList<String> groupsOfMaxSize = new ArrayList<>(); //the groups that have the maximum number of students
        ArrayList<String> groupsOfMinSize = new ArrayList<>(); //the groups that have the minimum number of students
        ArrayList<String> groupsOfInvalid = new ArrayList<>(); // the groups that have an invalid number of students
        for (ArrayList<Student> group : groups) {
            if (group.size() == maximumGroupSize) groupsOfMaxSize.add(group.get(0).getGroupNum());
            else if (group.size() == minimumGroupSize) groupsOfMinSize.add(group.get(0).getGroupNum());
            else groupsOfInvalid.add(group.get(0).getGroupNum());
        }
        int numGroupsOfMinSize = getNumGroupsOfMinSize(totalStudents);
        writer.append("\nTo meet the group size criteria, students should be sorted into groups of " + maximumGroupSize + " or " + minimumGroupSize + ". Any other group size is invalid");
        writer.append("\nSince there are " + totalStudents + " students, we should expect at least " + numGroupsOfMinSize + " group(s) of " + minimumGroupSize);
        writer.append("\nThe number of groups with " + maximumGroupSize + " students is " + groupsOfMaxSize.size() + " : " + groupsOfMaxSize);
        writer.append("\nThe number of groups with " + minimumGroupSize + " students is " + groupsOfMinSize.size() + " : " + groupsOfMinSize);
        writer.append("\nThe number of groups with an invalid number of students is " + groupsOfInvalid.size() + " : " + groupsOfInvalid);
        writer.append("\nThe percentage of groups that adhere to the group size criterion is " + (groupsOfMaxSize.size() + groupsOfMinSize.size()) * 100 / groups.size() + "%\n");

        //check lab section criteria - each group should have students all from the same lab section
        //for each group {count groups with all same lab section and which ones have mixed lab sections}
        ArrayList<String> diffLabs = new ArrayList(); //the number of groups in which NOT all students are registered in the same lab section
        ArrayList<String> sameLabs = new ArrayList(); //the number of groups in which all students are registered in the same lab section
        for (ArrayList<Student> group : groups) {
            for (int i = 0; i < group.size() - 1; i++) {
                for (int j = i + 1; j < group.size(); j++) {
                    if (!group.get(i).getLabSection().equals(group.get(j).getLabSection())) {
                        diffLabs.add(group.get(0).getGroupNum());
                        break;
                    }
                }
            }
            sameLabs.add(group.get(0).getGroupNum());
        }
        writer.append("\nTo meet the lab section criteria, all students in the same group should be registered in the same lab section.");
        writer.append("\nThe number of groups in which all students are registered in the same lab section is " + sameLabs.size() + " : " + sameLabs);
        writer.append("\nThe number of groups in which not all students are registered in the same lab section is " + diffLabs.size() + " : " + diffLabs);
        writer.append("\nThe percentage of groups that adhere to the lab section criterion is " + sameLabs.size() * 100 / groups.size() + "%\n");

        //check the programs criteria
        ArrayList<String> repeatPrograms = new ArrayList<>();
        Set<String> repeatMore2Programs = new HashSet<>();
        ArrayList<String> uniquePrograms = new ArrayList<>();
        for (ArrayList<Student> group : groups) {
            boolean hasRepeat = false;
            for (Student student : group) {
                if(countMatchingPrograms(student, group) > 1){
                    repeatMore2Programs.add(student.getGroupNum());
                }
                if (countMatchingPrograms(student, group) > 0 && !hasRepeat) {
                    hasRepeat = true;
                    repeatPrograms.add(student.getGroupNum());

                }
            }
            if (!hasRepeat) uniquePrograms.add(group.get(0).getGroupNum());
        }
        writer.append("\nTo meet the programs criteria, the all students in the same group should be enrolled in different engineering streams.");
        writer.append("\nThe number of groups that adhere to the programs criteria is " + (uniquePrograms.size()) + " : " + uniquePrograms);
        writer.append("\nThe number of groups that do not adhere to the programs criteria is " + (repeatPrograms.size()) + " : " + repeatPrograms);
        writer.append("\nThe number of groups with more than two students with the same program is " + (repeatMore2Programs.size()) + " : " + repeatMore2Programs);
        writer.append("\nThe percentage of groups that adhere to the programs criterion is " + uniquePrograms.size() * 100 / groups.size() + "%\n");

        //next check the team leader criteria
        ArrayList<String> hasDefaultLeader = new ArrayList<>();
        ArrayList<String> hasBackupLeader = new ArrayList<>();
        ArrayList<String> hasNoLeader = new ArrayList<>();
        for (ArrayList<Student> group : groups) {
            if (group.get(0).isDefaultLeader()) hasDefaultLeader.add(group.get(0).getGroupNum());
            else if (group.get(0).isBackupLeader()) hasBackupLeader.add(group.get(0).getGroupNum());
            else hasNoLeader.add(group.get(0).getGroupNum());
        }
        writer.append("\nTo meet the team leader criteria, groups should have one student in software or computer systems engineering.");
        writer.append("\nThe number of groups with a team leader from software or computer systems engineering is " + hasDefaultLeader.size() + " : " + hasDefaultLeader);
        writer.append("\nThe number of groups with a team leader from another SYSC program is " + hasBackupLeader.size() + " : " + hasBackupLeader);
        writer.append("\nThe number of groups with no team leader is " + hasNoLeader.size() + " : " + hasNoLeader);
        writer.append("\nThe percentage of groups that adhere to the team leader criterion is " + hasDefaultLeader.size() * 100 / groups.size() + "%\n");

        //check grade criteria
        ArrayList<String> similarGrade = new ArrayList<>();
        ArrayList<String> diffGrades = new ArrayList<>();
        for (ArrayList<Student> group : groups) {
            Student s = group.get(0);
            boolean hasDiffGrade = false;
            for (Student student : group) {
                if (!s.areGradesSimilar(student)) {
                    diffGrades.add(s.getGroupNum());
                    hasDiffGrade = true;
                }
            }
            if (!hasDiffGrade) similarGrade.add(s.getGroupNum());
        }
        writer.append("\nTo meet the grade criteria, each student in a group's grades should be within two grade levels (A+,A,A-, for example)");
        writer.append("\nThe number of groups with all similar grades is " + similarGrade.size() + " : " + similarGrade);
        writer.append("\nThe number of groups with different grades is " + diffGrades.size() + " : " + diffGrades);
        writer.append("\nThe percentage of groups that adhere to the grade criterion is " + similarGrade.size() * 100 / groups.size() + "%\n");

        //check how many groups adhere to all criteria
        ArrayList<String> intersection = new ArrayList<>();
        intersection.addAll(groupsOfMaxSize);
        intersection.addAll(groupsOfMinSize);
        intersection.retainAll(sameLabs);
        intersection.retainAll(uniquePrograms);
        intersection.retainAll(hasDefaultLeader);
        intersection.retainAll(similarGrade);

        writer.append("\nThe number of groups that adhere to all of the above criteria is " + intersection.size() + " : " + intersection);
        writer.append("\nThe percentage of groups that adhere to all criteria is " + intersection.size() * 100 / groups.size() + "%");


        writer.flush();
        writer.close();
    }
}
