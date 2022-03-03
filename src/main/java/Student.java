/**
 * The student class represents a single student with all of its attributes
 * @author Erica Oliver
 * @version 1.3 - Jan 19, 2022
 */

public class Student {
    private final String name;
    private final String studID;
    private String program;
    private String grade;
    private String labSection;
    private String email;
    private String groupNum; //the group that the student is sorted into
    private int gradeInt; //the grade represented as an integer where A+ is 0 and F is 12

    /**
     * Constructor for when we start using the optimization criteria
     *
     * @param name    The student's name
     * @param studID  The student's ID number
     * @param program The student's program enrollment
     * @param grade   The student's grade from a prerequisite course
     * @param email   The student's email address
     */
    public Student(String name, String studID, String program, String grade, String labSection, String email) {
        this.name = name;
        this.studID = studID;
        this.program = program;
        this.grade = grade;
        this.labSection = labSection;
        this.email = email;
        this.gradeToInt();
    }

    /**
     * @param s1 A student
     * @param s2 A student
     * @return true if the students are enrolled in the same program
     */
    public boolean sameProgram(Student s1, Student s2) {
        return (s1.program.equals(s2.program));
    }

    /**
     * Team leaders should be enrolled in either Software or Computer Systems Engineering
     *
     * @return true if the student can be a team leader
     */
    public boolean isDefaultLeader() {
        if (program.equals("Software Engineering") ||
                program.equals("Computer Systems Engineering")) {
            return true;
        }
        return false;
    }

    /**
     * If no Software or Computer Systems students can be found, the team leader
     * should come from one of the other SYSC programs (Communications or Biomedical Engineering)
     *
     * @return true is the student can be a team leader
     */
    public boolean isBackupLeader() {
        if (program.equals("Biomedical Engineering") ||
                program.equals("Communications Engineering")) {
            return true;
        }
        return false;
    }

    /**
     * @return a string representation of the student
     */
    public String csvRepresentation() {
        return name + "," + studID + "," + program + "," + grade + "," + labSection + "," + email + "," + groupNum + "\n";
    }

    /**
     * Convert letter grades (A+, A, ..., D-, F) to
     * integers (0, 1, ..., 11, 12) respectively, so
     * they can be easily compared when analysing the
     * optimization of the final groups
     */
    private void gradeToInt() {
        if (grade.equals("A+")) gradeInt = 0;
        else if (grade.equals("A")) gradeInt = 1;
        else if (grade.equals("A-")) gradeInt = 2;
        else if (grade.equals("B+")) gradeInt = 3;
        else if (grade.equals("B")) gradeInt = 4;
        else if (grade.equals("B-")) gradeInt = 5;
        else if (grade.equals("C+")) gradeInt = 6;
        else if (grade.equals("C")) gradeInt = 7;
        else if (grade.equals("C-")) gradeInt = 8;
        else if (grade.equals("D+")) gradeInt = 9;
        else if (grade.equals("D")) gradeInt = 10;
        else if (grade.equals("D-")) gradeInt = 11;
        else if (grade.equals("F")) gradeInt = 12;
    }

    /**
     * Compare the grades of two students and if their grades are
     * within 2 grades level of each other then they are similar
     *
     * For example if s1 has a grade of A- (which is 2) then it
     * is similar to A+, A, B+, and B
     *
     * @param s2 The student to be compared to the current student
     * @return true if the grades are similar
     */
    public boolean areGradesSimilar(Student s2) {
        if (Math.abs(this.getGradeInt() - s2.getGradeInt()) <= 2) {
            return true;
        }
        return false;
    }

    /**
     * Getter for name attribute
     *
     * @return The student's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for student id attribute
     *
     * @return The student's id
     */
    public String getStudID() {
        return studID;
    }

    /**
     * Getter for email attribute
     *
     * @return The student's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for program attribute
     *
     * @return The student's program
     */
    public String getProgram() {
        return program;
    }

    /**
     * Getter for grade attribute
     *
     * @return The student's grade in string form
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Getter for labSection attribute
     *
     * @return The student's lab section
     */
    public String getLabSection() {
        return labSection;
    }

    /**
     * Getter for groupNum attribute
     *
     * @return The group that the student was sorted into
     */
    public String getGroupNum() {
        return groupNum;
    }

    /**
     * Setter for groupNum attribute
     */
    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum;
    }

    /**
     * Getter for gradeInt attribute
     *
     * @return The student's grade in integer form
     */
    public int getGradeInt() {
        return gradeInt;
    }

    /**
     * Setter for program attribute
     */
    public void setProgram(String program) {
        this.program = program;
    }

    /**
     * Setter for grade attribute
     */
    public void setGrade(String grade) {
        this.grade = grade;
        gradeToInt();
    }

    /**
     * Setter for labSection attribute
     */
    public void setLabSection(String labSection) {
        this.labSection = labSection;
    }

    /**
     * Setter for email attribute
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
