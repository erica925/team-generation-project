/**
 * The student class represents a single student with all of its attributes
 * @author Erica Oliver
 * @version 1.2 - Nov 14, 2021
 */

public class Student {
    private String name;
    private String studID;
    private String program;
    private String grade;
    private String labSection;
    private String email;
    private String groupNum; //the group that the student is sorted into

    /**
     * Constructor for when we start using the optimization criteria
     *
     * @param name The student's name
     * @param studID The student's ID number
     * @param program The student's program enrollment
     * @param grade The student's grade from a prerequisite course
     * @param email The student's email address
     */
    public Student(String name, String studID, String program, String grade, String labSection, String email) {
        this.name = name;
        this.studID = studID;
        this.program = program;
        this.grade = grade;
        this.labSection = labSection;
        this.email = email;
    }

    /**
     *
     * @param s1 A student
     * @param s2 A student
     * @return true if the students are enrolled in the same program
     */
    public boolean sameProgram(Student s1, Student s2){
        if (s1.program.equals(s2.program)){
            return true;
        }
        return false;
    }

    /**
     * Team leaders should be enrolled in either Software or Computer Systems Engineering
     * @return true if the student can be a team leader
     */
    public boolean isDefaultLeader(){
        if (program.equals("Software Engineering") ||
                program.equals("Computer Systems Engineering")) {
            return true;
        }
        return false;
    }

    /**
     * If no Software or Computer Systems students can be found, the team leader
     * should come from one of the other SYSC programs (Communications or Biomedical Engineering)
     * @return true is the student can be a team leader
     */
    public boolean isBackupLeader(){
        if (program.equals("Biomedical Engineering") ||
                program.equals("Communications Engineering")) {
            return true;
        }
        return false;
    }

    /**
     * @return a string representation of the student
     */
    public String csvRepresentation(){
        return name + "," + studID + "," + program + "," + grade + "," + labSection + "," + email + "," + groupNum + "\n";
    }

    /**
     * Getter for Name attribute
     * @return The student's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for Student ID attribute
     * @return The student's student ID
     */
    public String getStudID() {
        return studID;
    }

    /**
     * Getter for Program attribute
     * @return The student's program
     */
    public String getProgram() {
        return program;
    }

    /**
     * Getter for Grade attribute
     * @return The student's grade
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Getter for labSection attribute
     * @return The student's lab section
     */
    public String getLabSection() {
        return labSection;
    }

    /**
     * Getter for Email attribute
     * @return The student's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for groupNum attribute
     * @return The group that the student was sorted into
     */
    public String getGroupNum() {
        return groupNum;
    }

    /**
     * Setter for Name attribute
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for Student ID attribute
     */
    public void setStudID(String studID) {
        this.studID = studID;
    }

    /**
     * Setter for Program attribute
     */
    public void setProgram(String program) {
        this.program = program;
    }

    /**
     * Setter for Grade attribute
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * Setter for labSection attribute
     */
    public void setLabSection(String labSection) {
        this.labSection = labSection;
    }

    /**
     * Setter for Email attribute
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter for groupNum attribute
     */
    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum;
    }
}
