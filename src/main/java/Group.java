import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Erica Oliver
 * @version 1 - Feb 16, 2022
 */
public class Group extends ArrayList<Student>{
    // grades range from 0 to 12 representing A+ to F
    private int highestGrade;
    private int lowestGrade;

    public Group(){
        highestGrade = 12;
        lowestGrade = 0;
    }

    /**
     * Calculates the difference between the highest and lowest grades in the group
     * where 0 = A+ and 12 = F so lower scores are more optimal.
     * @return the groups grade score
     */
    public int calculateGradeScore(){
        for (Student s : this){
            if (s.getGradeInt() > lowestGrade){
                lowestGrade = s.getGradeInt();
            } if (s.getGradeInt() < highestGrade) {
                highestGrade = s.getGradeInt();
            }
        }
        return (lowestGrade - highestGrade);
    }

    /**
     * Assigns a score based on how well the group fits the programs criteria.
     * A score of 0 means all students in the group are in different programs.
     * The maximum score possible is the group size -1 meaning all students
     * are in the same program.
     *
     * @return the
     */
    public int calculateProgramsScore(){
        int x = 0;
        for (Student s : this){
            if (countMatchingPrograms(s) > x){
                x = countMatchingPrograms(s);
            }
        }
        return x;
    }

    /**
     * Counts how many students have matching programs with the given Student
     *
     * @param student The student being compared
     * @return the number of students whose program matches the given student
     */
    public int countMatchingPrograms(Student student) {
        ArrayList<String> programs = new ArrayList<>();
        for (Student s : this){
            programs.add(s.getProgram());
        }
        return Collections.frequency(programs, student.getProgram()) - 1;
    }

    /**
     * @return true if the group has a team leader assigned to it
     */
    public boolean hasTeamLeader(){
        return this.get(0).isDefaultLeader() || this.get(0).isBackupLeader();
    }

    /**
     * @param student The student assigned as the team's leader
     */
    public void setTeamLeader(Student student){
        this.set(0, student);
    }

    /**
     * @return the group's leader
     */
    public Student getTeamLeader() {
        return this.get(0);
    }

    /**
     * @return the highest grade in the group
     */
    public int getHighestGrade(){
        return highestGrade;
    }
}
