import java.util.ArrayList;
import java.lang.Math;

/**
 *
 * @author Erica Oliver
 * @version 1.0 - Oct 26, 2021
 */

public class Main {
    static ArrayList<Student> students;
    static double maximumGroupSize;
    static ArrayList<ArrayList> groups;

    public static void main(String args[]){
        groups = new ArrayList<>();
        maximumGroupSize = 4;
        initStudents();
        sort(students);
    }

    public void validateFile(){}

    public void readCSV(){}

    public static void initStudents(){
        students = new ArrayList<>();
        students.add(new Student("A"));
        students.add(new Student("B"));
        students.add(new Student("C"));
        students.add(new Student("D"));
        students.add(new Student("E"));
        students.add(new Student("F"));
        students.add(new Student("G"));
    }

    /**
     * Sort the students into groups following the optimization criteria
     *
     * For iteration 1 there is no criteria, they are just split into groups of 4
     *
     * @param students The list of students to be grouped
     */
    public static void sort(ArrayList<Student> students) {
        for (int x = 0; x <= Math.ceil(students.size()/maximumGroupSize); x++) {
            ArrayList group = new ArrayList<>();
            groups.add(group);
            for (int n = 1; n <= maximumGroupSize; n++) {
                group.add(students.get(0));
                students.remove(0);
                if (students.size() == 0){
                    break;
                }
            }
        }
    }

    public static void printStudents(){
        for (Student s : students){
            System.out.println(s.getName());
        }
    }

    public static void printGroups(){
        for (ArrayList<Student> group : groups){
            for (Student s : group){
                System.out.println(s.getName());
            }
            System.out.println();
        }
    }

    /**
     * Print an optimization summary of the formed groups
     * This is used to compare algorithms/methods of creating the groups
     * to see which adheres best to the requirements
     */
    public void optimizationSummary(){
        //first check the group size criteria
        int count4 = 0;
        int count3 = 0;
        int countInvalid = 0;
        for (ArrayList<ArrayList> g: groups){
            if (g.size() == 4) count4++;
            else if (g.size() == 4) count3++;
            else countInvalid++;
        }
        System.out.println("The number of groups with 4 students is " + count4);
        System.out.println("The number of groups with 4 students is " + count3);
        System.out.println("The number of groups with an invalid number of students is " + countInvalid);

        //next check the team leader criteria
        // need to create a method that checks if there is a team leader
        //do this by checking the program of the first student in the group
        int hasDefaultLeader = 0;
        int hasBackupLeader = 0;
        int hasNoLeader = 0;
        for (ArrayList<ArrayList<Student>> g: groups){
            if (g.get(0).get(0).isDefaultLeader()) hasDefaultLeader++;
            else if (g.get(0).get(0).isBackupLeader()) hasBackupLeader++;
            else hasNoLeader++;
        }
        System.out.println("The number of groups with a team leader from software or computer systems engineering is " + hasDefaultLeader);
        System.out.println("The number of groups with a team leader from another SYSC program is " + hasBackupLeader);
        System.out.println("The number of groups with no team leader is " + hasNoLeader);
    }

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
