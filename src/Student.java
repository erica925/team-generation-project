/**
 * The student class represents a single with all of its attributes
 * @author Erica Oliver
 * @version 1.0 - Oct 26, 2021
 */

public class Student {
    private String name;
    private String studID;
    private String program;
    private String grade;
    private String email; //not for optimization, just for the output file

    /**
     * Constructor for when we start using the optimization criteria
     *
     * @param name The student's name
     * @param studID The student's ID number
     * @param program The student's program enrollment
     * @param grade The student's grade from a prerequisite course
     * @param email The student's email address
     */
    public Student(String name, String studID, String program, String grade, String email) {
        this.name = name;
        this.studID = studID;
        this.program = program;
        this.grade = grade;
        this.email = email;
    }

    /**
     * Simple constructor for now
     *
     * @param name The student's name
     */
    public Student(String name){
        this.name = name;
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
     * Team leaders should be enrolled in either Software or Compsys
     * @return true if the student can be a team leader
     */
    public boolean isDefaultLeader(){
        if (program.equals("Software Engineering") |
                program.equals("Computer Systems Engineering") |
                program.equals("Communications Engineering") ) {
            return true;
        }
        return false;
    }

    /**
     * If no Software or compsys students can be found, the team leader
     * should come from one of the other SYSC programs (comm, elec, biomed)
     * @return true is the student can be a team leader
     */
    public boolean isBackupLeader(){
        if (program.equals("Biomedical Engineering") |
                program.equals("Communications Engineering") |
                program.equals("Electrical Engineering")) {
            return true;
        }
        return false;
    }

    /**
     * Print the student's attributes
     */
    public void printStudent(){
        System.out.println("Name: " + name + ", Student ID: " + studID + ", Program: " + program + ", Grade: " + grade);
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
     * Getter for Email attribute
     * @return The student's email
     */
    public String getEmail() {
        return email;
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
     * Setter for Email attribute
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
