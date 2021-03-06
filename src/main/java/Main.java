import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.*;

/**
 * @author Erica Oliver, Wintana Yosief
 * @version April 2, 2022
 */
public class Main {
    private static Group students;
    private static Group withdrawnStudents;
    private static int maximumGroupSize;
    private static int minimumGroupSize;
    private static ArrayList<Group> groups;
    private static int totalStudents;
    private static ArrayList<Group> modifiedGroups;

    private static boolean teamLeaderFlag;
    private static boolean programsFlag;
    private static boolean gradeFlag;
    private static boolean labSectionFlag;
    private static boolean modifyFlag;

    /**
     * The sorting use case where students are sorted into groups
     * @throws IOException
     */
    public static void beginCreate() throws IOException {
        sort();
        sort2();
        assignGroupNumbers();
        writeCSV();
        optimizationSummary();
    }

    /**
     * Creates random assignments of groups
     * @throws IOException
     */
    public static void beginRandomCreate() throws IOException {
        groups = new ArrayList<>();
        totalStudents = students.size();

        randomSort(students);
        assignGroupNumbers();
        writeCSV();
        optimizationSummary();
    }

    /**
     * The modify use case where some pre-made groups are given with some new and withdrawn students
     * Removes the withdrawn students and adds the new students to the groups
     * Outputs the new groups on top with the unaffected groups at the bottom
     *
     * @throws IOException
     */
    public static void beginModify() throws IOException {
        // remove withdrawn students
        for (Student student : withdrawnStudents) {
            breakline:
            for (Group group : groups) {
                for (Student stud : group) {
                    if ((stud.getName().equals(student.getName())) && stud.getStudID().equals(student.getStudID())) {
                        group.remove(stud);
                        break breakline;
                    }
                }
            }
        }

        ArrayList<Group> groupsNotAffected = new ArrayList<>();
        // remove full groups
        ListIterator<Group> groupsIterator = groups.listIterator();
        while (groupsIterator.hasNext()) {
            Group group = groupsIterator.next();
            if (group.size() == maximumGroupSize) {
                groupsNotAffected.add(group);
                groupsIterator.remove();
            }
        }

        // find number of modified groups
        modifiedGroups = new ArrayList<>(groups);

        // finds the total number of students
        totalStudents = 0;
        for (Group group : groups) {
            totalStudents += group.size();
        }
        totalStudents += students.size();

        int numGroupsOfMinSize = getNumGroupsOfMinSize(totalStudents);
        int numGroupsOfMaxSize = (totalStudents - numGroupsOfMinSize * minimumGroupSize) / maximumGroupSize;
        int numGroups = numGroupsOfMaxSize + numGroupsOfMinSize;

        if (numGroups > groups.size()) {
            // create additional groups
            int additionalGroups = numGroups - groups.size();
            for (int i = 0; i < additionalGroups; i++) {
                Group group = new Group();
                // find a team leader
                ListIterator<Student> studentsIterator = students.listIterator();
                boolean found = false;
                while (studentsIterator.hasNext()) {

                    Student s = studentsIterator.next();
                    if (s.isDefaultLeader()) {
                        found = true;
                        group.add(s);
                        studentsIterator.remove();
                        break;
                    }
                }
                if (!found) {
                    studentsIterator = students.listIterator();
                    while (studentsIterator.hasNext()) {
                        Student s = studentsIterator.next();
                        if (s.isBackupLeader()) {
                            found = true;
                            group.add(s);
                            studentsIterator.remove();
                            break;
                        }
                    }
                    if (!found) {
                        studentsIterator = students.listIterator();
                        while (studentsIterator.hasNext() && !found) {
                            Student s = studentsIterator.next();
                            group.add(s);
                            studentsIterator.remove();
                            found = true;
                        }
                    }
                }
            }
        }

        // if there are still students left to be added or groups that are too small, re-sort them
        // add the groups to the "students" field
        if (!students.isEmpty()) {
            for (Group group : groups) {
                ListIterator<Student> studentsIterator = students.listIterator();
                while (studentsIterator.hasNext()) {
                    Student s = studentsIterator.next();
                    if (group.get(0).getLabSection().equals(s.getLabSection()) && group.size() < maximumGroupSize) {
                        group.add(s);
                        studentsIterator.remove();
                        break;
                    }
                }
            }

            // remove full groups
            groupsIterator = groups.listIterator();
            while (groupsIterator.hasNext()) {
                Group group = groupsIterator.next();
                if (group.size() == maximumGroupSize) {
                    groupsNotAffected.add(group);
                    groupsIterator.remove();
                }
            }

            if (!students.isEmpty()) {
                ArrayList<Student> withoutGroup = new ArrayList<>(students);
                // notify user that these students cannot be added to a group because
                // groups in this/these lab section(s) are full
                GUIMain.labSectionsFull(withoutGroup);
            }
            //sort();
            //sort2();

            // add the other groups back
            groups.addAll(groupsNotAffected);

            totalStudents = 0;
            for (Group group : groups) {
                totalStudents += group.size();
            }
            assignGroupNumbers();
            writeCSV();
            optimizationSummary();
        }
    }

    /**
     * The main sorting method where other sorting methods are called
     */
    private static void sort() {
        ArrayList<Group> allStudents = new ArrayList<>();

        groups = new ArrayList<>();
        totalStudents = students.size();

        allStudents.add(students);

        ArrayList<Group> labSectionGroups = new ArrayList<>();
        // sort by lab section
        if (labSectionFlag) {
            for (Group labSectionGroup : allStudents) {
                labSectionGroups.addAll(sortLabSections(labSectionGroup));
            }
        } else labSectionGroups = allStudents;

        ArrayList<Group> gradeGroups = new ArrayList<>();
        // sort by grade
        if (gradeFlag) {
            for (Group labSectionGroup : labSectionGroups) {
                gradeGroups.addAll(sortGrades(labSectionGroup));
            }
        } else gradeGroups = labSectionGroups;

        if (teamLeaderFlag && programsFlag) {
            for (Group gradeGroup : gradeGroups) {
                ArrayList<Group> teamLeaderGroups = getTeamLeaders(gradeGroup);
                groups.addAll(teamLeaderGroups);
                sortPrograms(teamLeaderGroups, gradeGroup);
            }
        }
        else if (teamLeaderFlag) {
            for (Group gradeGroup : gradeGroups) {
                ArrayList<Group> teamLeaderGroups = getTeamLeaders(gradeGroup);
                groups.addAll(teamLeaderGroups);

                //fill with students from any program
                int i = getNumGroupsOfMinSize(teamLeaderGroups.size() + gradeGroup.size());
                for (Group group : teamLeaderGroups) {
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
            for (Group gradeGroup : gradeGroups) {
                int numGroupsOfMinSize = getNumGroupsOfMinSize(gradeGroup.size());
                int numGroupsOfMaxSize = (gradeGroup.size() - numGroupsOfMinSize * minimumGroupSize) / maximumGroupSize;
                int numGroups = numGroupsOfMinSize + numGroupsOfMaxSize;
                ArrayList<Group> emptyGroups = new ArrayList<>();
                for (int i = 0; i < numGroups; i++){
                    Group a = new Group();
                    a.add(gradeGroup.get(0));
                    gradeGroup.remove(0);
                    emptyGroups.add(a);
                }
                groups.addAll(emptyGroups);
                sortPrograms(emptyGroups, gradeGroup);
            }
        }
        else {
            for (Group gradeGroup : gradeGroups) {
                simpleSort(gradeGroup);
            }
        }
    }

    /**
     * Checks all the groups created by sort() to see if they can be optimized better.
     * Swaps students from adjacent groups then checks if the swap was beneficial.
     *
     * The sizes of the groups do not change
     * The adjacent groups must be in the same lab section
     *
     * Reminder that the priority of the criteria is: team leader, grades, programs
     */
    private static void sort2() {
        for (int i = 0; i < groups.size() - 1; i++) {
            Group group1 = groups.get(i);
            Group group2 = groups.get(i + 1);

            if (labSectionFlag && teamLeaderFlag) {
                //the two groups must be the same lab section
                if (group1.get(0).getLabSection().equals(group2.get(0).getLabSection())) { // do not swap from different lab sections
                    // check team leader
                    if (!group1.hasTeamLeader()) { // else move on to grades
                        for (int j = 1; j < group2.size(); j++) {
                            if (group2.get(j).isDefaultLeader() || group2.get(j).isBackupLeader()) { // swap
                                Student temp = group2.get(j);
                                group2.set(j, group1.getTeamLeader());
                                group1.setTeamLeader(temp);
                            }
                        }
                    }

                    if (gradeFlag && programsFlag) {
                        // check grade score
                        // If the difference between the highest and lowest grades of group1 is > 2,
                        // get the student with the lowest grade. Then find a student in group2
                        // that fits better (ie. find the a student whose grade is within 2 grade levels
                        // of the highest grade in group1.

                        boolean stop = false;
                        while (group1.calculateGradeScore() > 2 && !stop) {
                            int group1initialGradeScore = group1.calculateGradeScore();
                            int group2initialGradeScore = group2.calculateGradeScore();

                            // get the lowest grade in group1 excluding the team leader
                            int group1lowestGrade = 0;
                            Student lowestStudent1 = null;
                            for (int n = 1; n < group1.size(); n++) {
                                if (group1.get(n).getGradeInt() > group1lowestGrade) {
                                    group1lowestGrade = group1.get(n).getGradeInt();
                                    lowestStudent1 = group1.get(n);
                                }
                            }
                            int group1Index = group1.indexOf(lowestStudent1);

                            // get the highest grade in group2 excluding the team leader
                            int group2highestGrade = 11;
                            Student highestStudent2 = null;
                            for (int n = 1; n < group2.size(); n++) {
                                if (group2.get(n).getGradeInt() < group2highestGrade) {
                                    group2highestGrade = group2.get(n).getGradeInt();
                                    highestStudent2 = group2.get(n);
                                }
                            }
                            int group2Index = group2.indexOf(highestStudent2);

                            if (Math.abs(group2highestGrade - group1.getHighestGrade()) > group1.calculateGradeScore() ||
                                    group1lowestGrade >= group2highestGrade) {
                                //group1 grade score cannot be improved so move on to checking the program score
                                stop = true;
                            } else {
                                //swap
                                Student temp = group1.get(group1Index);
                                group1.set(group1Index, group2.get(group2Index));
                                group2.set(group2Index, temp);

                                // check if the two groups benefited from the swap
                                if (!(group1initialGradeScore < group1.calculateGradeScore() &&
                                        group2initialGradeScore < group2.calculateGradeScore())) {
                                    // the swap was not beneficial so swap the students back
                                    temp = group1.get(group1Index);
                                    group1.set(group1Index, group2.get(group2Index));
                                    group2.set(group2Index, temp);
                                }
                            }
                        }
                        stop = false;
                        while (group1.calculateProgramsScore() > 0 && !stop) {
                            int group1initialProgramScore = group1.calculateProgramsScore();
                            int group2initialProgramScore = group2.calculateProgramsScore();
                            ArrayList<String> allGroup1programs = new ArrayList<>();

                            // find which student from group1 should be swapped

                            // 1. get the list of programs in group1
                            for (Student s : group1) {
                                allGroup1programs.add(s.getProgram());
                            }

                            // 2. get the repeated program
                            String program = ""; // the repeated program
                            int max = 0;
                            for (String s : allGroup1programs) {
                                if (Collections.frequency(allGroup1programs, s) > max) {
                                    max = Collections.frequency(allGroup1programs, s);
                                    program = s;
                                }
                            }

                            // 3. choose the student to be swapped - this will be the student with
                            // the lowest grade in the repeated program who is not the team leader
                            int lowestGrade = 0; // 0 is actually an A+
                            Student studentToSwap1 = null;
                            for (Student student : group1) {
                                if ((group1.getTeamLeader() != student) && (student.getGradeInt() > lowestGrade) && student.getProgram().equals(program)) {
                                    lowestGrade = student.getGradeInt();
                                    studentToSwap1 = student;
                                }
                            }
                            // 4. get the index of the student to be swapped
                            int index1 = group1.indexOf(studentToSwap1);


                            // find which student from group2 should be swapped

                            // 1. find the student with the highest grade that is not the
                            // team leader and is a different program to those already in group1
                            Student studentToSwap2 = null;
                            int highestGrade = 11; // 11 is a D-
                            for (Student student : group2) {
                                if (student.getGradeInt() < highestGrade && !allGroup1programs.contains(student.getProgram()) && group2.getTeamLeader() != student) {
                                    highestGrade = student.getGradeInt();
                                    studentToSwap2 = student;
                                }
                            }
                            if (studentToSwap2 == null) {
                                stop = true;
                                continue;
                            }
                            // 3. get the index of the student to be swapped
                            int index2 = group2.indexOf(studentToSwap2);

                            int grade1initial = group1.calculateGradeScore();
                            int grade2initial = group2.calculateGradeScore();

                            // swap the two students
                            Student temp = group1.get(index1);
                            group1.set(index1, group2.get(index2));
                            group2.set(index2, temp);

                            // check to see if either of the grade or the program scores decreased
                            // and swap back if necessary
                            if (grade1initial < group1.calculateGradeScore() ||
                                    grade2initial < group2.calculateGradeScore() ||
                                    group1initialProgramScore < group1.calculateProgramsScore() ||
                                    group2initialProgramScore < group2.calculateProgramsScore()) {
                                // the swap was not beneficial so swap the students back
                                temp = group1.get(index1);
                                group1.set(index1, group2.get(index2));
                                group2.set(index2, temp);
                                stop = true;
                            }
                        }
                    } else if (gradeFlag) {
                        // check grade score
                        // If the difference between the highest and lowest grades of group1 is > 2,
                        // get the student with the lowest grade. Then find a student in group2
                        // that fits better (ie. find the a student whose grade is within 2 grade levels
                        // of the highest grade in group1.

                        boolean stop = false;
                        while (group1.calculateGradeScore() > 2 && !stop) {
                            int group1initialGradeScore = group1.calculateGradeScore();
                            int group2initialGradeScore = group2.calculateGradeScore();

                            // get the lowest grade in group1 excluding the team leader
                            int group1lowestGrade = 0;
                            Student lowestStudent1 = null;
                            for (int n = 1; n < group1.size(); n++) {
                                if (group1.get(n).getGradeInt() > group1lowestGrade) {
                                    group1lowestGrade = group1.get(n).getGradeInt();
                                    lowestStudent1 = group1.get(n);
                                }
                            }
                            int group1Index = group1.indexOf(lowestStudent1);

                            // get the highest grade in group2 excluding the team leader
                            int group2highestGrade = 11;
                            Student highestStudent2 = null;
                            for (int n = 1; n < group2.size(); n++) {
                                if (group2.get(n).getGradeInt() < group2highestGrade) {
                                    group2highestGrade = group2.get(n).getGradeInt();
                                    highestStudent2 = group2.get(n);
                                }
                            }
                            int group2Index = group2.indexOf(highestStudent2);

                            if (Math.abs(group2highestGrade - group1.getHighestGrade()) > group1.calculateGradeScore() ||
                                    group1lowestGrade >= group2highestGrade) {
                                //group1 grade score cannot be improved so move on to checking the program score
                                stop = true;

                            } else {
                                //swap
                                Student temp = group1.get(group1Index);
                                group1.set(group1Index, group2.get(group2Index));
                                group2.set(group2Index, temp);

                                // check if the two groups benefited from the swap
                                if (!(group1initialGradeScore < group1.calculateGradeScore() &&
                                        group2initialGradeScore < group2.calculateGradeScore())) {
                                    // the swap was not beneficial so swap the students back
                                    temp = group1.get(group1Index);
                                    group1.set(group1Index, group2.get(group2Index));
                                    group2.set(group2Index, temp);
                                }
                            }
                        }
                    } else if (programsFlag) {
                        boolean stop = false;
                        while (group1.calculateProgramsScore() > 0 && !stop) {
                            int group1initialProgramScore = group1.calculateProgramsScore();
                            int group2initialProgramScore = group2.calculateProgramsScore();
                            ArrayList<String> allGroup1programs = new ArrayList<>();

                            // find which student from group1 should be swapped

                            // 1. get the list of programs in group1
                            for (Student s : group1) {
                                allGroup1programs.add(s.getProgram());
                            }

                            // 2. get the repeated program
                            String program = ""; // the repeated program
                            int max = 0;
                            for (String s : allGroup1programs) {
                                if (Collections.frequency(allGroup1programs, s) > max) {
                                    max = Collections.frequency(allGroup1programs, s);
                                    program = s;
                                }
                            }

                            // 3. choose the student to be swapped - this will be the student with
                            // the lowest grade in the repeated program who is not the team leader
                            Student studentToSwap1 = null;
                            for (Student student : group1) {
                                if ((group1.getTeamLeader() != student) && student.getProgram().equals(program)) {
                                    studentToSwap1 = student;
                                    break;
                                }
                            }
                            // 4. get the index of the student to be swapped
                            int index1 = group1.indexOf(studentToSwap1);

                            // find which student from group2 should be swapped

                            // 1. find the student with the highest grade that is not the
                            // team leader and is a different program to those already in group1
                            Student studentToSwap2 = null;
                            for (Student student : group2) {
                                if (!allGroup1programs.contains(student.getProgram()) && group2.getTeamLeader() != student) {
                                    studentToSwap2 = student;
                                    break;
                                }
                            }
                            if (studentToSwap2 == null) {
                                stop = true;
                                continue;
                            }
                            // 3. get the index of the student to be swapped
                            int index2 = group2.indexOf(studentToSwap2);

                            // swap the two students
                            Student temp = group1.get(index1);
                            group1.set(index1, group2.get(index2));
                            group2.set(index2, temp);

                            // check to see if either of the grade or the program scores decreased
                            // and swap back if necessary
                            if (group1initialProgramScore < group1.calculateProgramsScore() ||
                                    group2initialProgramScore < group2.calculateProgramsScore()) {
                                // the swap was not beneficial so swap the students back
                                temp = group1.get(index1);
                                group1.set(index1, group2.get(index2));
                                group2.set(index2, temp);
                                stop = true;
                            }
                        }
                    }
                }

            } else if (labSectionFlag && gradeFlag) {
                if (group1.get(0).getLabSection().equals(group2.get(0).getLabSection())) {


                    // check grade score
                    // If the difference between the highest and lowest grades of group1 is > 2,
                    // get the student with the lowest grade. Then find a student in group2
                    // that fits better (ie. find the a student whose grade is within 2 grade levels
                    // of the highest grade in group1.
                    boolean stop = false;
                    while (group1.calculateGradeScore() > 2 && !stop) {
                        int group1initialGradeScore = group1.calculateGradeScore();
                        int group2initialGradeScore = group2.calculateGradeScore();

                        // get the lowest grade in group1
                        int group1lowestGrade = 0;
                        Student lowestStudent1 = null;
                        for (Student value : group1) {
                            if (value.getGradeInt() > group1lowestGrade) {
                                group1lowestGrade = value.getGradeInt();
                                lowestStudent1 = value;
                            }
                        }
                        int group1Index = group1.indexOf(lowestStudent1);


                        // get the highest grade in group2
                        int group2highestGrade = 11;
                        Student highestStudent2 = null;

                        for (Student student : group2) {
                            if (student.getGradeInt() < group2highestGrade) {
                                group2highestGrade = student.getGradeInt();
                                highestStudent2 = student;
                            }

                        }
                        int group2Index = group2.indexOf(highestStudent2);

                        if (Math.abs(group2highestGrade - group1.getHighestGrade()) > group1.calculateGradeScore() ||
                                group1lowestGrade >= group2highestGrade) {
                            //group1 grade score cannot be improved
                            stop = true;
                        } else {
                            //swap
                            Student temp = group1.get(group1Index);
                            group1.set(group1Index, group2.get(group2Index));
                            group2.set(group2Index, temp);

                            // check if the two groups benefited from the swap
                            if (!(group1initialGradeScore < group1.calculateGradeScore() &&
                                    group2initialGradeScore < group2.calculateGradeScore())) {
                                // the swap was not beneficial so swap the students back
                                temp = group1.get(group1Index);
                                group1.set(group1Index, group2.get(group2Index));
                                group2.set(group2Index, temp);
                            }
                        }
                    }
                    if (programsFlag) {
                        stop = false;
                        while (group1.calculateProgramsScore() > 0 && !stop) {
                            int group1initialProgramScore = group1.calculateProgramsScore();
                            int group2initialProgramScore = group2.calculateProgramsScore();
                            ArrayList<String> allGroup1programs = new ArrayList<>();

                            // find which student from group1 should be swapped

                            // 1. get the list of programs in group1
                            for (Student s : group1) {
                                allGroup1programs.add(s.getProgram());
                            }

                            // 2. get the repeated program
                            String program = ""; // the repeated program
                            int max = 0;
                            for (String s : allGroup1programs) {
                                if (Collections.frequency(allGroup1programs, s) > max) {
                                    max = Collections.frequency(allGroup1programs, s);
                                    program = s;
                                }
                            }

                            // 3. choose the student to be swapped - this will be the student with
                            // the lowest grade in the repeated program
                            int lowestGrade = 0; // 0 is actually an A+
                            Student studentToSwap1 = null;
                            for (Student student : group1) {
                                if (student.getGradeInt() > lowestGrade && student.getProgram().equals(program)) {
                                    lowestGrade = student.getGradeInt();
                                    studentToSwap1 = student;
                                }
                            }
                            // 4. get the index of the student to be swapped
                            int index1 = group1.indexOf(studentToSwap1);


                            // find which student from group2 should be swapped

                            // 1. find the student with the highest grade that is not the
                            // team leader and is a different program to those already in group1
                            Student studentToSwap2 = null;
                            int highestGrade = 11; // 11 is a D-
                            for (Student student : group2) {
                                if (student.getGradeInt() < highestGrade && !allGroup1programs.contains(student.getProgram()) && group2.getTeamLeader() != student) {
                                    highestGrade = student.getGradeInt();
                                    studentToSwap2 = student;
                                }
                            }
                            if (studentToSwap2 == null) {
                                stop = true;
                                continue;
                            }
                            // 3. get the index of the student to be swapped
                            int index2 = group2.indexOf(studentToSwap2);

                            int grade1initial = group1.calculateGradeScore();
                            int grade2initial = group2.calculateGradeScore();

                            // swap the two students
                            Student temp = group1.get(index1);
                            group1.set(index1, group2.get(index2));
                            group2.set(index2, temp);

                            // check to see if either of the grade or the program scores decreased
                            // and swap back if necessary
                            if (grade1initial < group1.calculateGradeScore() ||
                                    grade2initial < group2.calculateGradeScore() ||
                                    group1initialProgramScore < group1.calculateProgramsScore() ||
                                    group2initialProgramScore < group2.calculateProgramsScore()) {
                                // the swap was not beneficial so swap the students back
                                temp = group1.get(index1);
                                group1.set(index1, group2.get(index2));
                                group2.set(index2, temp);
                                stop = true;
                            }
                        }
                    }
                }
            } else if (labSectionFlag && programsFlag) {

                boolean stop = false;
                // check program score
                while (group1.calculateProgramsScore() > 0 && !stop) {
                    int group1initialProgramScore = group1.calculateProgramsScore();
                    int group2initialProgramScore = group2.calculateProgramsScore();
                    ArrayList<String> allGroup1programs = new ArrayList<>();

                    // find which student from group1 should be swapped

                    // 1. get the list of programs in group1
                    for (Student s : group1) {
                        allGroup1programs.add(s.getProgram());
                    }

                    // 2. get the repeated program
                    String program = ""; // the repeated program
                    int max = 0;
                    for (String s : allGroup1programs) {
                        if (Collections.frequency(allGroup1programs, s) > max) {
                            max = Collections.frequency(allGroup1programs, s);
                            program = s;
                        }
                    }
                    Student studentToSwap1 = null;
                    for (Student student : group1) {
                        if (student.getProgram().equals(program)) {
                            studentToSwap1 = student;
                            break;
                        }
                    }
                    int index1 = group1.indexOf(studentToSwap1);

                    Student studentToSwap2 = null;
                    for (Student student : group2) {
                        if (!allGroup1programs.contains(student.getProgram())) {
                            studentToSwap2 = student;
                            break;
                        }
                    }
                    if (studentToSwap2 == null) {
                        stop = true;
                        continue;
                    }
                    int index2 = group2.indexOf(studentToSwap2);

                    // swap the two students
                    Student temp = group1.get(index1);
                    group1.set(index1, group2.get(index2));
                    group2.set(index2, temp);

                    int group1ProgramScore = group1.calculateProgramsScore();
                    int group2ProgramScore = group2.calculateProgramsScore();

                    if (group1ProgramScore < group1initialProgramScore || group2initialProgramScore < group2ProgramScore) {
                        // the swap was not beneficial so swap the students back
                        temp = group1.get(index1);
                        group1.set(index1, group2.get(index2));
                        group2.set(index2, temp);
                        stop = true;
                    }
                }
            }
        }
    }

    /**
     * Read the chosen CSV file(s) into a list for the sorting use case
     * Any number of files may be chosen but each one must have at least
     * student names and ID numbers
     *
     * @param filenames the files containing the students to be sorted
     * @return true if reading was successful
     * @throws IOException
     */
    public static boolean readCSVCreate(List<String> filenames) throws IOException {
        students = new Group();
        for (String filename : filenames) {
            if (students.isEmpty()) { // for the first file or if only one file is given
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
                // get the position of each attribute
                String line = bufferedReader.readLine(); // header
                String[] header = line.split(",");
                int nameIndex = -1, idIndex = -1, emailIndex = -1, gradeIndex = -1, programIndex = -1, labIndex = -1;
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equals("Student Name")) {
                        nameIndex = i;
                    }
                    if (header[i].equals("Student ID")) {
                        idIndex = i;
                    }
                    if (header[i].equals("Email")) {
                        emailIndex = i;
                    }
                    if (header[i].equals("Lab Section") && labSectionFlag) {
                        labIndex = i;
                    }
                    if (header[i].equals("Program") && (programsFlag || teamLeaderFlag)) {
                        programIndex = i;
                    }
                    if (header[i].equals("Grade") && gradeFlag) {
                        gradeIndex = i;
                    }
                }

                line = bufferedReader.readLine(); // first student
                while (line != null) {
                    // gets the student's info
                    String[] student = line.split(",");

                    String name, id, program, grade, lab, email;
                    if (nameIndex == -1) name = "";
                    else name = student[nameIndex];
                    if (idIndex == -1) id = "";
                    else id = student[idIndex];
                    if (programIndex == -1) program = "";
                    else program = student[programIndex];
                    if (gradeIndex == -1) grade = "";
                    else grade = student[gradeIndex];
                    if (labIndex == -1) lab = "";
                    else lab = student[labIndex];
                    if (emailIndex == -1) email = "";
                    else email = student[emailIndex];

                    // adds info
                    if(!grade.equals("F")) {
                        students.add(new Student(name, id, program, grade, lab, email));
                    }
                        // next line
                    line = bufferedReader.readLine();
                }
            }

            // for merging the next files
            else {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
                // get the position of each attribute
                String line = bufferedReader.readLine();
                String[] header = line.split(",");
                int nameIndex = -1, idIndex = -1, emailIndex = -1, gradeIndex = -1, programIndex = -1, labIndex = -1;
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equals("Student Name")) {
                        nameIndex = i;
                    }
                    if (header[i].equals("Student ID")) {
                        idIndex = i;
                    }
                    if (header[i].equals("Email")) {
                        emailIndex = i;
                    }
                    if (header[i].equals("Lab Section") && labSectionFlag) {
                        labIndex = i;
                    }
                    if (header[i].equals("Program") && (programsFlag || teamLeaderFlag)) {
                        programIndex = i;
                    }
                    if (header[i].equals("Grade") && gradeFlag) {
                        gradeIndex = i;
                    }
                }

                line = bufferedReader.readLine();
                while (line != null) {
                    // gets the student's info
                    String[] stud = line.split(",");
                    String name = "", id = "", program = "", grade = "", lab = "", email = "";
                    if (nameIndex == -1) name = "";
                    else name = stud[nameIndex];
                    if (idIndex == -1) id = "";
                    else id = stud[idIndex];
                    if (programIndex == -1) program = "";
                    else program = stud[programIndex];
                    if (gradeIndex == -1) grade = "";
                    else grade = stud[gradeIndex];
                    if (labIndex == -1) lab = "";
                    else lab = stud[labIndex];
                    if (emailIndex == -1) email = "";
                    else email = stud[emailIndex];


                    // find the student that matches the current line then add the new information
                    for (Student student : students) {
                        if (student.getName().equals(name) && student.getStudID().equals(id)){
                            if (!email.equals("")) {
                                student.setEmail(email);
                            }
                            if (!program.equals("") && (programsFlag || teamLeaderFlag)) {
                                student.setProgram(program);
                            }
                            if (!grade.equals("") && gradeFlag) {
                                student.setGrade(grade);
                            }
                            if (!lab.equals("") && labSectionFlag) {
                                student.setLabSection(lab);
                            }
                            break;
                        }
                    }
                    // next line
                    line = bufferedReader.readLine();
                }
            }
        }

        // once all files are read, check that all the necessary information was provided
        boolean infoMissing = false;
        for (Student student : students) {
            if (student.getName().equals("")) {
                infoMissing = true;
            }
            if (student.getStudID().equals("")) {
                infoMissing = true;
            }
            if (student.getEmail().equals("")) {
                infoMissing = true;
            }
            if ((student.getGrade().equals("") || student.getGrade().equals("DEF") || student.getGrade().equals("GNA")) && gradeFlag) {
                student.setGrade("C+");
            }
            if (student.getProgram().equals("") && (programsFlag || teamLeaderFlag)) {
                infoMissing = true;
            }
            if (student.getLabSection().equals("") && labSectionFlag) {
                infoMissing = true;
            }
        }
        if (infoMissing) {
            GUIMain.infoMissing();
            return false;
        }
        return true;
    }

    /**
     * Read the chosen CSV file(s) into a list for the modify use case
     * The chosen files must include a list of groups previously created
     * by the sorting use case, a list of newly registered students, and
     * a list of withdrawn students
     *
     * @param newStudentsFilenames the files containing new students to be sorted
     * @param withdrawnStudentsFilenames the files containing the students to be removed
     * @param groupsFilenames the files containing the groups to be modified
     * @return true if reading was successful
     * @throws IOException
     */
    public static boolean readCSVModify(List<String> newStudentsFilenames, List<String> withdrawnStudentsFilenames,List<String> groupsFilenames) throws IOException {
        // read groups ***********************************************************
        groups = new ArrayList<>();
        for (String filename : groupsFilenames) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

            // first get the optimization criteria used to create the groups
            String line = bufferedReader.readLine();
            String[] criteria = line.split(" ");
            for (int i = 0; i < criteria.length; i++){
                if (criteria[i].contains("programs")){
                    programsFlag = criteria[i].contains("true");
                }
                if (criteria[i].contains("team_leader")){
                    teamLeaderFlag = criteria[i].contains("true");
                }
                if (criteria[i].contains("grade")){
                    gradeFlag = criteria[i].contains("true");
                }
                if (criteria[i].contains("lab_section")){
                    labSectionFlag = criteria[i].contains("true");
                }
                if (criteria[i].contains("size")){
                    char[] sizeString = criteria[i].toCharArray();
                    for (int j = 0; j < sizeString.length; j++){
                        if (Character.isDigit(sizeString[j])) {
                            setMaximumGroupSize(Character.getNumericValue(sizeString[j]));
                            setMinimumGroupSize();
                        }
                    }
                }
            }


            line = bufferedReader.readLine(); // header
            String[] header = line.split(",");
            if (groups.isEmpty()) { // for the first file or if only one file is given
                // get the position of each attribute
                int nameIndex = -1, idIndex = -1, emailIndex = -1, gradeIndex = -1, programIndex = -1, labIndex = -1;
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equals("Student Name")) {
                        nameIndex = i;
                    }
                    else if (header[i].equals("Student ID")) {
                        idIndex = i;
                    }
                    else if (header[i].equals("Email")) {
                        emailIndex = i;
                    }
                    else if (header[i].equals("Lab Section") && labSectionFlag) {
                        labIndex = i;
                    }
                    else if (header[i].equals("Program") && (programsFlag || teamLeaderFlag)) {
                        programIndex = i;
                    }
                    else if (header[i].equals("Grade") && gradeFlag) {
                        gradeIndex = i;
                    }
                }

                line = bufferedReader.readLine(); // first student
                groups.add(new Group());

                breakline:
                while (line != null) {
                    // blank lines signify the start of a new group
                    // in case there are multiple blank lines, especially at the end of the file
                    while (line.isBlank()) {
                        line = bufferedReader.readLine();
                        if (line == null) {
                            break breakline;
                        }
                        if (!groups.get(groups.size()-1).isEmpty()) {
                            groups.add(new Group());
                        }
                    }

                    // add students to the last group in the list
                    Group currentGroup = groups.get(groups.size()-1);

                    // gets the student's info
                    String[] student = line.split(",");

                    String name, id, program, grade, lab, email;
                    if (nameIndex == -1) name = "";
                    else name = student[nameIndex];
                    if (idIndex == -1) id = "";
                    else id = student[idIndex];
                    if (programIndex == -1) program = "";
                    else program = student[programIndex];
                    if (gradeIndex == -1) grade = "";
                    else grade = student[gradeIndex];
                    if (labIndex == -1) lab = "";
                    else lab = student[labIndex];
                    if (emailIndex == -1) email = "";
                    else email = student[emailIndex];

                    // adds info
                    if(!grade.equals("F")) {
                        currentGroup.add(new Student(name, id, program, grade, lab, email));
                    }
                    // next line
                    line = bufferedReader.readLine();
                }
            }

            // for merging the next files
            else {
                // get the position of each attribute
                int nameIndex = -1, idIndex = -1, emailIndex = -1, gradeIndex = -1, programIndex = -1, labIndex = -1;
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equals("Student Name")) {
                        nameIndex = i;
                    }
                    if (header[i].equals("Student ID")) {
                        idIndex = i;
                    }
                    if (header[i].equals("Email")) {
                        emailIndex = i;
                    }
                    if (header[i].equals("Lab Section") && labSectionFlag) {
                        labIndex = i;
                    }
                    if (header[i].equals("Program") && (programsFlag || teamLeaderFlag)) {
                        programIndex = i;
                    }
                    if (header[i].equals("Grade") && gradeFlag) {
                        gradeIndex = i;
                    }
                }

                line = bufferedReader.readLine();
                while (line != null) {
                    // gets the student's info
                    String[] stud = line.split(",");
                    String name = "", id = "", program = "", grade = "", lab = "", email = "";
                    if (nameIndex == -1) name = "";
                    else name = stud[nameIndex];
                    if (idIndex == -1) id = "";
                    else id = stud[idIndex];
                    if (programIndex == -1) program = "";
                    else program = stud[programIndex];
                    if (gradeIndex == -1) grade = "";
                    else grade = stud[gradeIndex];
                    if (labIndex == -1) lab = "";
                    else lab = stud[labIndex];
                    if (emailIndex == -1) email = "";
                    else email = stud[emailIndex];


                    // find the student that matches the current line then add the new information
                    breakline:
                    for (Group group : groups) {
                        for (Student student : group) {
                            if (student.getName().equals(name) && student.getStudID().equals(id)) {
                                if (!email.equals("")) {
                                    student.setEmail(email);
                                }
                                if (!program.equals("") && (programsFlag || teamLeaderFlag)) {
                                    student.setProgram(program);
                                }
                                if (!grade.equals("") && gradeFlag) {
                                    student.setGrade(grade);
                                }
                                if (!lab.equals("") && labSectionFlag) {
                                    student.setLabSection(lab);
                                }
                                break breakline;
                            }
                        }
                    }
                    // next line
                    line = bufferedReader.readLine();
                }
            }
        }

        // once all group files are read, check that all the necessary information was provided
        boolean infoMissing = false;
        String criteriaSelected = "";
        if (gradeFlag) criteriaSelected.concat(",grade");
        if (teamLeaderFlag || programsFlag) criteriaSelected.concat(",program");
        if (labSectionFlag) criteriaSelected.concat(",lab section");
        for (Group group : groups) {
            for (Student student : group) {
                if (student.getName().equals("")) {
                    infoMissing = true;
                }
                if (student.getStudID().equals("")) {
                    infoMissing = true;
                }
                if (student.getEmail().equals("")) {
                    infoMissing = true;
                }
                if ((student.getGrade().equals("") || student.getGrade().equals("DEF") || student.getGrade().equals("GNA")) && gradeFlag) {
                    student.setGrade("C+");
                }
                if (student.getProgram().equals("") && (programsFlag || teamLeaderFlag)) {
                    infoMissing = true;
                }
                if (student.getLabSection().equals("") && labSectionFlag) {
                    infoMissing = true;
                }
            }
            if (infoMissing) {
                GUIMain.infoMissing();
                return false;
            }
        }

        // read withdrawn students ***********************************************************
        withdrawnStudents = new Group();
        for (String filename : withdrawnStudentsFilenames) {
            if (withdrawnStudents.isEmpty()) { // for the first file or if only one file is given
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

                // get the position of each attribute
                String line = bufferedReader.readLine(); // header
                String[] header = line.split(",");
                int nameIndex = -1, idIndex = -1, emailIndex = -1, gradeIndex = -1, programIndex = -1, labIndex = -1;
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equals("Student Name")) {
                        nameIndex = i;
                    }
                    if (header[i].equals("Student ID")) {
                        idIndex = i;
                    }
                    if (header[i].equals("Email")) {
                        emailIndex = i;
                    }
                    if (header[i].equals("Lab Section") && labSectionFlag) {
                        labIndex = i;
                    }
                    if (header[i].equals("Program") && (programsFlag || teamLeaderFlag)) {
                        programIndex = i;
                    }
                    if (header[i].equals("Grade") && gradeFlag) {
                        gradeIndex = i;
                    }
                }

                line = bufferedReader.readLine(); // first student
                while (line != null) {
                    // gets the student's info
                    String[] student = line.split(",");

                    String name, id, program, grade, lab, email;
                    if (nameIndex == -1) name = "";
                    else name = student[nameIndex];
                    if (idIndex == -1) id = "";
                    else id = student[idIndex];
                    if (programIndex == -1) program = "";
                    else program = student[programIndex];
                    if (gradeIndex == -1) grade = "";
                    else grade = student[gradeIndex];
                    if (labIndex == -1) lab = "";
                    else lab = student[labIndex];
                    if (emailIndex == -1) email = "";
                    else email = student[emailIndex];

                    // adds info
                    withdrawnStudents.add(new Student(name, id, program, grade, lab, email));
                    // next line
                    line = bufferedReader.readLine();
                }
            }

            // for merging the next files
            else {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
                // get the position of each attribute
                String line = bufferedReader.readLine();
                String[] header = line.split(",");
                int nameIndex = -1, idIndex = -1, emailIndex = -1, gradeIndex = -1, programIndex = -1, labIndex = -1;
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equals("Student Name")) {
                        nameIndex = i;
                    }
                    if (header[i].equals("Student ID")) {
                        idIndex = i;
                    }
                    if (header[i].equals("Email")) {
                        emailIndex = i;
                    }
                    if (header[i].equals("Lab Section") && labSectionFlag) {
                        labIndex = i;
                    }
                    if (header[i].equals("Program") && (programsFlag || teamLeaderFlag)) {
                        programIndex = i;
                    }
                    if (header[i].equals("Grade") && gradeFlag) {
                        gradeIndex = i;
                    }
                }

                line = bufferedReader.readLine();
                while (line != null) {
                    // gets the student's info
                    String[] stud = line.split(",");
                    String name = "", id = "", program = "", grade = "", lab = "", email = "";
                    if (nameIndex == -1) name = "";
                    else name = stud[nameIndex];
                    if (idIndex == -1) id = "";
                    else id = stud[idIndex];
                    if (programIndex == -1) program = "";
                    else program = stud[programIndex];
                    if (gradeIndex == -1) grade = "";
                    else grade = stud[gradeIndex];
                    if (labIndex == -1) lab = "";
                    else lab = stud[labIndex];
                    if (emailIndex == -1) email = "";
                    else email = stud[emailIndex];


                    // find the student that matches the current line then add the new information
                    for (Student student : withdrawnStudents) {
                        if (student.getName().equals(name) && student.getStudID().equals(id)){
                            if (!email.equals("")) {
                                student.setEmail(email);
                            }
                            if (!program.equals("") && (programsFlag || teamLeaderFlag)) {
                                student.setProgram(program);
                            }
                            if (!grade.equals("") && gradeFlag) {
                                student.setGrade(grade);
                            }
                            if (!lab.equals("") && labSectionFlag) {
                                student.setLabSection(lab);
                            }
                            break;
                        }
                    }
                    // next line
                    line = bufferedReader.readLine();
                }
            }
        }

        // once all files are read, check that all the necessary information was provided
        infoMissing = false;
        for (Student student : withdrawnStudents) {
            if (student.getName().equals("")) {
                infoMissing = true;
            }
            if (student.getStudID().equals("")) {
                infoMissing = true;
            }
            if (student.getEmail().equals("")) {
                infoMissing = true;
            }
            if ((student.getGrade().equals("") || student.getGrade().equals("DEF") || student.getGrade().equals("GNA")) && gradeFlag) {
                student.setGrade("C+");
            }
            if (student.getProgram().equals("") && (programsFlag || teamLeaderFlag)) {
                infoMissing = true;
            }
            if (student.getLabSection().equals("") && labSectionFlag) {
                infoMissing = true;
            }
        }
        if (infoMissing) {
            GUIMain.infoMissing();
            return false;
        }

        // read new students ***********************************************************
        students = new Group();
        for (String filename : newStudentsFilenames) {
            if (students.isEmpty()) { // for the first file or if only one file is given
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

                // get the position of each attribute
                String line = bufferedReader.readLine(); // header
                String[] header = line.split(",");
                int nameIndex = -1, idIndex = -1, emailIndex = -1, gradeIndex = -1, programIndex = -1, labIndex = -1;
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equals("Student Name")) {
                        nameIndex = i;
                    }
                    if (header[i].equals("Student ID")) {
                        idIndex = i;
                    }
                    if (header[i].equals("Email")) {
                        emailIndex = i;
                    }
                    if (header[i].equals("Lab Section") && labSectionFlag) {
                        labIndex = i;
                    }
                    if (header[i].equals("Program") && (programsFlag || teamLeaderFlag)) {
                        programIndex = i;
                    }
                    if (header[i].equals("Grade") && gradeFlag) {
                        gradeIndex = i;
                    }
                }

                line = bufferedReader.readLine(); // first student
                while (line != null && !line.isBlank()) { //
                    // gets the student's info
                    String[] student = line.split(",");

                    String name, id, program, grade, lab, email;
                    if (nameIndex == -1) name = "";
                    else name = student[nameIndex];
                    if (idIndex == -1) id = "";
                    else id = student[idIndex];
                    if (programIndex == -1) program = "";
                    else program = student[programIndex];
                    if (gradeIndex == -1) grade = "";
                    else grade = student[gradeIndex];
                    if (labIndex == -1) lab = "";
                    else lab = student[labIndex];
                    if (emailIndex == -1) email = "";
                    else email = student[emailIndex];

                    // adds info
                    if(!grade.equals("F")) {
                        students.add(new Student(name, id, program, grade, lab, email));
                    }
                    // next line
                    line = bufferedReader.readLine();
                }
            }

            // for merging the next files
            else {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
                // get the position of each attribute
                String line = bufferedReader.readLine();
                String[] header = line.split(",");
                int nameIndex = -1, idIndex = -1, emailIndex = -1, gradeIndex = -1, programIndex = -1, labIndex = -1;
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equals("Student Name")) {
                        nameIndex = i;
                    }
                    if (header[i].equals("Student ID")) {
                        idIndex = i;
                    }
                    if (header[i].equals("Email")) {
                        emailIndex = i;
                    }
                    if (header[i].equals("Lab Section") && labSectionFlag) {
                        labIndex = i;
                    }
                    if (header[i].equals("Program") && (programsFlag || teamLeaderFlag)) {
                        programIndex = i;
                    }
                    if (header[i].equals("Grade") && gradeFlag) {
                        gradeIndex = i;
                    }
                }

                line = bufferedReader.readLine();
                while (line != null) {
                    // gets the student's info
                    String[] stud = line.split(",");
                    String name = "", id = "", program = "", grade = "", lab = "", email = "";
                    if (nameIndex == -1) name = "";
                    else name = stud[nameIndex];
                    if (idIndex == -1) id = "";
                    else id = stud[idIndex];
                    if (programIndex == -1) program = "";
                    else program = stud[programIndex];
                    if (gradeIndex == -1) grade = "";
                    else grade = stud[gradeIndex];
                    if (labIndex == -1) lab = "";
                    else lab = stud[labIndex];
                    if (emailIndex == -1) email = "";
                    else email = stud[emailIndex];


                    // find the student that matches the current line then add the new information
                    for (Student student : students) {
                        if (student.getName().equals(name) && student.getStudID().equals(id)){
                            if (!email.equals("")) {
                                student.setEmail(email);
                            }
                            if (!program.equals("") && (programsFlag || teamLeaderFlag)) {
                                student.setProgram(program);
                            }
                            if (!grade.equals("") && gradeFlag) {
                                student.setGrade(grade);
                            }
                            if (!lab.equals("") && labSectionFlag) {
                                student.setLabSection(lab);
                            }
                            break;
                        }
                    }
                    // next line
                    line = bufferedReader.readLine();
                }
            }
        }

        // once all files are read, check that all the necessary information was provided
        infoMissing = false;
        for (Student student : students) {
            if (student.getName().equals("")) {
                infoMissing = true;
            }
            if (student.getStudID().equals("")) {
                infoMissing = true;
            }
            if (student.getEmail().equals("")) {
                infoMissing = true;
            }
            if ((student.getGrade().equals("") || student.getGrade().equals("DEF") || student.getGrade().equals("GNA")) && gradeFlag) {
                student.setGrade("C+");
            }
            if (student.getProgram().equals("") && (programsFlag || teamLeaderFlag)) {
                infoMissing = true;
            }
            if (student.getLabSection().equals("") && labSectionFlag) {
                infoMissing = true;
            }
        }
        if (infoMissing) {
            GUIMain.infoMissing();
            return false;
        }

        return true;
    }

    /**
     * Write the groups to a CSV file which can be opened in Excel
     *
     * @throws IOException throws IOException
     */
    private static void writeCSV() throws IOException {
        FileWriter writer = new FileWriter("groups.csv");
        writer.append("Optimization Criteria Used: size=" + maximumGroupSize + ", programs=" + programsFlag +
                ", grades=" + gradeFlag + ", lab_section=" + labSectionFlag + ", team_leader=" + teamLeaderFlag + "\n");
        writer.append("Student Name,Student ID,Program,Grade,Lab Section,Email,Group Number\n\n");
        for (Group group : groups) {
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
    private static void simpleSort(Group students) {
        int numMinGroups = getNumGroupsOfMinSize(students.size());
        for (int i = 0; i < numMinGroups; i++) {
            Group group = new Group();
            groups.add(group);
            for (int j = 0; j < minimumGroupSize; j++) {
                group.add(students.get(0));
                students.remove(0);
                if (students.size() == 0) {
                    break;
                }
            }
        }
        int numGroups = students.size() / maximumGroupSize;
        for (int i = 0; i < numGroups; i++) {
            Group group = new Group();
            groups.add(group);
            for (int j = 0; j < maximumGroupSize; j++) {
                group.add(students.get(0));
                students.remove(0);
                if (students.size() == 0) {
                    break;
                }
            }
        }
    }

    /**
     * Just split the students into groups of maxGroupSize
     * by randomly selecting students
     *
     * @param students The list of students to be grouped
     */
    private static void randomSort(Group students) {
        int numMinGroups = getNumGroupsOfMinSize(students.size());
        for (int i = 0; i < numMinGroups; i++) {
            Group group = new Group();
            groups.add(group);

        }
        Random r = new Random();
        int numGroups = students.size() / maximumGroupSize;
        for (int i = 0; i < numGroups; i++) {
            Group group = new Group();
            groups.add(group);
            for (int j = 0; j < maximumGroupSize; j++) {
                int index = r.nextInt(students.size());
                group.add(students.get(index));
                students.remove(index);
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
    private static ArrayList<Group> getTeamLeaders(Group students){
        // Get the number of groups that will be the maximum and minimum sizes
        int numGroupsOfMinSize = getNumGroupsOfMinSize(students.size());
        int numGroupsOfMaxSize = (students.size() - numGroupsOfMinSize * minimumGroupSize) / maximumGroupSize;
        int numGroups = numGroupsOfMinSize + numGroupsOfMaxSize; // the total number of groups

        // Creates groups and adds a team leader
        ArrayList<Group> sortedGroups = new ArrayList<Group>();
        for (int i = 0; i < numGroups; i++) {
            Group group = new Group();
            boolean foundLeader = false;
            for (Student s : students) {
                if (s.isDefaultLeader()) {
                    group.add(s);
                    group.setTeamLeader(s);
                    students.remove(s);
                    foundLeader = true;
                    break;
                }
            }
            if (!foundLeader) {
                for (Student s : students) {
                    if (s.isBackupLeader()) {
                        group.add(s);
                        group.setTeamLeader(s);
                        students.remove(s);
                        foundLeader = true;
                        break;
                    }
                }
            }
            if (!foundLeader) {
                group.add(students.get(0));
                group.setTeamLeader(students.get(0));
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
    private static void sortPrograms(ArrayList<Group> groups, Group students) {
        // Get the number of groups that will be the maximum and minimum sizes
        int numGroupsOfMinSize = getNumGroupsOfMinSize(students.size() + groups.size()*groups.get(0).size()); //groups may or may not have been assigned a team leader
        int numGroupsOfMaxSize = groups.size() - numGroupsOfMinSize;

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

                Group group = groups.get(i);
                ListIterator<Student> groupIterator = group.listIterator();
                // Iterating over each student in a single group
                while (groupIterator.hasNext()) {
                    Student student = groupIterator.next();
                    // Checking if students are in the same program
                    if (!s.sameProgram(student) && group.size() < groupSize) {
                        groupIterator.add(s); // adds student to group
                        groupFound = true;
                        s.setGroupNum(s.getLabSection() + ".G" + (i + 1));
                        studentsIterator.remove(); // removes student from list
                        break;
                    } else {
                        if (group.size() < groupSize) {
                            // If only one student has matching program add student
                            int matchingPrograms = group.countMatchingPrograms(s);
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
     * Split the list of students by lab section
     *
     * @param students The list of students to be sorted
     */
    private static ArrayList<Group> sortLabSections(Group students) {
        ArrayList<Group> labSections = new ArrayList<Group>();
        while (!students.isEmpty()) {
            Group group = new Group();
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
    private static ArrayList<Group> sortGrades(Group students) {
        ArrayList<Group> gradeGroup = new ArrayList<>();
        Group groupAp = new Group();
        Group groupA = new Group();
        Group groupAm = new Group();
        Group groupBp = new Group();
        Group groupB = new Group();
        Group groupBm = new Group();
        Group groupCp = new Group();
        Group groupC = new Group();
        Group groupCm = new Group();
        Group groupDp = new Group();
        Group groupD = new Group();
        Group groupDm = new Group();

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
        }
        gradeGroup.removeIf(ArrayList::isEmpty);

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
    private static void assignGroupNumbers() {
        for (int i = 0; i < groups.size(); i++) {
            Group g = groups.get(i);
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
    private static int getNumGroupsOfMinSize(int size) {
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

    /**
     * Set the minimum group size to the maximum - 1
     */
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
     * Sets the modify flag value
     * @param value
     */
    public static void setModifyFlag(boolean value){
        modifyFlag = value;
    }

    /**
     * Print an optimization summary of the formed groups
     * This is used to compare algorithms/methods of creating the groups
     * to see which adheres best to the requirements
     */
    private static void optimizationSummary() throws IOException {
        FileWriter writer = new FileWriter("optimization_summary.txt");
        writer.append("Optimization Summary: \n");

        //used for checking how many groups adhere to all criteria
        ArrayList<String> intersection = new ArrayList<>();

        //check the group size criteria
        //check the groups that have the maximum, minimum, or an invalid number of students
        ArrayList<String> groupsOfMaxSize = new ArrayList<>(); //the groups that have the maximum number of students
        ArrayList<String> groupsOfMinSize = new ArrayList<>(); //the groups that have the minimum number of students
        ArrayList<String> groupsOfInvalid = new ArrayList<>(); // the groups that have an invalid number of students
        for (Group group : groups) {
            if (group.size() == maximumGroupSize) groupsOfMaxSize.add(group.get(0).getGroupNum());
            else if (group.size() == minimumGroupSize) groupsOfMinSize.add(group.get(0).getGroupNum());
            else groupsOfInvalid.add(group.get(0).getGroupNum());
        }
        intersection.addAll(groupsOfMaxSize);
        intersection.addAll(groupsOfMinSize);
        int numGroupsOfMinSize = getNumGroupsOfMinSize(totalStudents);
        writer.append("\nTo meet the group size criteria, students must be sorted into groups of " + maximumGroupSize + " or " + minimumGroupSize + ". Any other group size is invalid");
        writer.append("\nSince there are " + totalStudents + " students, we should expect at least " + numGroupsOfMinSize + " group(s) of " + minimumGroupSize);
        writer.append("\nThe number of groups with " + maximumGroupSize + " students is " + groupsOfMaxSize.size() + " : " + groupsOfMaxSize);
        writer.append("\nThe number of groups with " + minimumGroupSize + " students is " + groupsOfMinSize.size() + " : " + groupsOfMinSize);
        writer.append("\nThe number of groups with an invalid number of students is " + groupsOfInvalid.size() + " : " + groupsOfInvalid);
        writer.append("\nThe percentage of groups that adhere to the group size criterion is " + (groupsOfMaxSize.size() + groupsOfMinSize.size()) * 100 / groups.size() + "%\n");

        if (labSectionFlag) {
            //check lab section criteria - each group should have students all from the same lab section
            //for each group {count groups with all same lab section and which ones have mixed lab sections}
            ArrayList<String> diffLabs = new ArrayList(); //the number of groups in which NOT all students are registered in the same lab section
            ArrayList<String> sameLabs = new ArrayList(); //the number of groups in which all students are registered in the same lab section
            for (Group group : groups) {
                boolean diff = false;
                for (int i = 0; i < group.size() - 1; i++) {
                    if (diff) {
                        break;
                    }
                    for (int j = i + 1; j < group.size(); j++) {
                        if (!group.get(i).getLabSection().equals(group.get(j).getLabSection())) {
                            diffLabs.add(group.get(0).getGroupNum());
                            diff = true;
                            break;
                        }
                    }
                }
                if(!diff) {
                    sameLabs.add(group.get(0).getGroupNum());
                }
            }
            intersection.retainAll(sameLabs);
            writer.append("\nTo meet the lab section criteria, all students in the same group must be registered in the same lab section.");
            writer.append("\nThe number of groups in which all students are registered in the same lab section is " + sameLabs.size() + " : " + sameLabs);
            writer.append("\nThe number of groups in which not all students are registered in the same lab section is " + diffLabs.size() + " : " + diffLabs);
            writer.append("\nThe percentage of groups that adhere to the lab section criterion is " + sameLabs.size() * 100 / groups.size() + "%\n");
        }

        if (programsFlag) {
            //check the programs criteria
            ArrayList<String> repeatPrograms = new ArrayList<>();
            Set<String> repeatMore2Programs = new HashSet<>();
            ArrayList<String> uniquePrograms = new ArrayList<>();
            for (Group group : groups) {
                boolean hasRepeat = false;
                for (Student student : group) {
                    if (group.countMatchingPrograms(student) > 1) {
                        repeatMore2Programs.add(student.getGroupNum());
                    }
                    if (group.countMatchingPrograms(student) > 0 && !hasRepeat) {
                        hasRepeat = true;
                        repeatPrograms.add(student.getGroupNum());
                    }
                }
                if (!hasRepeat) uniquePrograms.add(group.get(0).getGroupNum());
            }
            intersection.retainAll(uniquePrograms);
            writer.append("\nTo meet the programs criteria, the all students in the same group must be enrolled in different engineering streams.");
            writer.append("\nThe number of groups that adhere to the programs criteria is " + (uniquePrograms.size()) + " : " + uniquePrograms);
            writer.append("\nThe number of groups that do not adhere to the programs criteria is " + (repeatPrograms.size()) + " : " + repeatPrograms);
            writer.append("\nThe number of groups with more than two students with the same program is " + (repeatMore2Programs.size()) + " : " + repeatMore2Programs);
            writer.append("\nThe percentage of groups that adhere to the programs criterion is " + uniquePrograms.size() * 100 / groups.size() + "%\n");
        }

        if (teamLeaderFlag) {
            //next check the team leader criteria
            ArrayList<String> hasDefaultLeader = new ArrayList<>();
            ArrayList<String> hasBackupLeader = new ArrayList<>();
            ArrayList<String> hasNoLeader = new ArrayList<>();
            for (Group group : groups) {
                if (group.getTeamLeader().isDefaultLeader()) hasDefaultLeader.add(group.getTeamLeader().getGroupNum());
                else if (group.getTeamLeader().isBackupLeader()) hasBackupLeader.add(group.getTeamLeader().getGroupNum());
                else hasNoLeader.add(group.getTeamLeader().getGroupNum());
            }
            intersection.retainAll(hasDefaultLeader);
            writer.append("\nTo meet the team leader criteria, groups must have one student in software or computer systems engineering.");
            writer.append("\nGroups may be assigned a backup team leader from either communications or biomedical engineering, is no default team leader is available");
            writer.append("\nThe number of groups with a team leader from software or computer systems engineering is " + hasDefaultLeader.size() + " : " + hasDefaultLeader);
            writer.append("\nThe number of groups with a team leader from another SYSC program is " + hasBackupLeader.size() + " : " + hasBackupLeader);
            writer.append("\nThe number of groups with no team leader is " + hasNoLeader.size() + " : " + hasNoLeader);
            writer.append("\nThe percentage of groups that have only a backup team leader is " + hasBackupLeader.size() * 100 / groups.size() + "%");
            writer.append("\nThe percentage of groups that adhere to the team leader criterion is " + hasDefaultLeader.size() * 100 / groups.size() + "%\n");
        }

        if (gradeFlag) {
            //check grade criteria
            ArrayList<String> similarGrade = new ArrayList<>();
            ArrayList<String> similarGradeWithScore = new ArrayList<>();
            ArrayList<String> diffGrades = new ArrayList<>();
            ArrayList<String> diffGradesWithScore = new ArrayList<>();
            for (Group group : groups) {
                Student s = group.get(0);
                boolean hasDiffGrade = false;
                for (Student student : group) {
                    if (!s.areGradesSimilar(student)) {
                        diffGrades.add(s.getGroupNum());
                        diffGradesWithScore.add("(" + s.getGroupNum() + "," + group.calculateGradeScore() + ")");
                        hasDiffGrade = true;
                    }
                }
                if (!hasDiffGrade) {
                    similarGradeWithScore.add("(" + s.getGroupNum() + "," + group.calculateGradeScore() + ")");
                    similarGrade.add(s.getGroupNum());
                }
            }
            intersection.retainAll(similarGrade);
            writer.append("\nTo meet the grade criteria, each student in a group's grades should be within two grade levels (A+,A,A-, for example)");
            writer.append("\nNext to each group is a score. This is the difference between the highest and lowest grades in the group. Lower scores are more optimal");
            writer.append("\nThe number of groups with all similar grades is " + similarGrade.size() + " : " + similarGradeWithScore);
            writer.append("\nThe number of groups with different grades is " + diffGrades.size() + " : " + diffGradesWithScore);
            writer.append("\nThe percentage of groups that adhere to the grade criterion is " + similarGrade.size() * 100 / groups.size() + "%\n");
        }

        if(modifyFlag) {
            SortedSet<String> beenModified = new TreeSet<>();

            for(Group group: modifiedGroups){
                for(Student student: group){
                    beenModified.add(student.getGroupNum());
                }
            }
            writer.append("\nThe number of modified groups is " + modifiedGroups.size() + ": " + beenModified.toString());
        }

        writer.append("\nThe number of groups that adhere to all of the above criteria is " + intersection.size() + " out of " + groups.size() + " : " + intersection);
        writer.append("\nThe percentage of groups that adhere to all criteria is " + intersection.size() * 100 / groups.size() + "%");

        writer.flush();
        writer.close();
    }
}
