import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MainTest {
    private List<String> filenames = new ArrayList<>();
    private List<String> newStudentsFilenames = new ArrayList<>();
    private List<String> withdrawnStudentsFilenames = new ArrayList<>();
    private List<String> groupsFilenames = new ArrayList<>();

    @Test
    public void testReadCSV_Sort() throws IOException {
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(true);

        // insert a valid file
        filenames.add("sample files/students_sample.csv");
        assertTrue(Main.readCSVCreate(filenames));

        filenames.remove("sample files/students_sample.csv");

        // insert two files and merge
        filenames.add("test files/test_merge_1.csv");
        filenames.add("test files/test_merge_2.csv");
        assertTrue(Main.readCSVCreate(filenames));
        Main.setMaximumGroupSize(3);
        Main.setMinimumGroupSize();
        Main.beginCreate();

        filenames.clear();


    }

    @Test
    public void testReadCSV_Modify() throws IOException {
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(true);

        // insert a file with new students
        newStudentsFilenames.add("test files/test_modify_new_students.csv");

        // insert a file with withdrawing students
        withdrawnStudentsFilenames.add("test files/test_modify_withdrawn.csv");

        // insert a file with groups
        groupsFilenames.add("test files/test_modify_groups.csv");

        assertTrue(Main.readCSVModify(newStudentsFilenames, withdrawnStudentsFilenames, groupsFilenames));

        // insert another file with new students
        newStudentsFilenames.add("test_modify_new_students_2.csv");

        assertTrue(Main.readCSVModify(newStudentsFilenames, withdrawnStudentsFilenames, groupsFilenames));

        // insert another file with withdrawn students
        withdrawnStudentsFilenames.add("test_modify_withdrawn_2.csv");

        assertTrue(Main.readCSVModify(newStudentsFilenames, withdrawnStudentsFilenames, groupsFilenames));

        newStudentsFilenames.clear();
        withdrawnStudentsFilenames.clear();
        groupsFilenames.clear();

    }

    @Test
    public void testSortLabSection() throws IOException {

        // set lab section flag
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(false);
        Main.setTeamLeaderFlag(false);
        Main.setProgramsFlag(false);

        // adds test student file
        filenames.add("sample files/students_sample.csv");
        // reads file
        assertTrue(Main.readCSVCreate(filenames));
        // sets group size
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        // creates groups
        Main.beginCreate();

        filenames.clear();

    }

    @Test
    public void testSortGrades() throws IOException {

        // set lab section and grade flags to true
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setTeamLeaderFlag(false);
        Main.setProgramsFlag(false);

        // adds students file
        filenames.add("sample files/students_sample.csv");
        // reads file
        assertTrue(Main.readCSVCreate(filenames));
        // sets group size
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        // creates groups
        Main.beginCreate();

        filenames.clear();

    }

    @Test
    public void testSortProgramsTeamLeader() throws IOException {

        // sets lab section, team leader, and programs flags
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(false);
        Main.setTeamLeaderFlag(true);
        Main.setProgramsFlag(true);

        // add student file
        filenames.add("sample files/students_sample.csv");
        // reads file
        assertTrue(Main.readCSVCreate(filenames));
        // sets group size
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        // creates groups
        Main.beginCreate();

        filenames.clear();
    }

    @Test
    public void testSortTeamLeader() throws IOException {

        // sets lab section and team leader flags
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(false);
        Main.setTeamLeaderFlag(true);
        Main.setProgramsFlag(false);

        // adds students file
        filenames.add("sample files/students_sample.csv");
        // reads file
        assertTrue(Main.readCSVCreate(filenames));
        // sets group size
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        // creates groups
        Main.beginCreate();

        filenames.clear();
    }

    @Test
    public void testSortPrograms() throws IOException {

        // sets lab section and programs flag
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(false);
        Main.setTeamLeaderFlag(false);
        Main.setProgramsFlag(true);

        // adds students file
        filenames.add("sample files/students_sample.csv");
        // reads file
        assertTrue(Main.readCSVCreate(filenames));
        // sets group size
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        // creates groups
        Main.beginCreate();

        filenames.clear();
    }

    @Test
    public void testSort1() throws IOException {

        // sets lab section, grade, and programs flag
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(false);

        // adds students file
        filenames.add("sample files/students_sample.csv");
        // reads file
        assertTrue(Main.readCSVCreate(filenames));
        // set group size
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        // creates group
        Main.beginCreate();

        filenames.clear();


    }

    @Test
    public void testSort2() throws IOException {

        // sets all flags
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(true);

        // adds students file
        filenames.add("sample files/students_sample.csv");
        // reads file
        assertTrue(Main.readCSVCreate(filenames));
        // sets group size
        Main.setMaximumGroupSize(5);
        Main.setMinimumGroupSize();
        // creates groups
        Main.beginCreate();

        filenames.clear();
    }

    @Test
    public void testSort3() throws IOException {

        // sets flags
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(true);

        // adds two student files to be merged
        filenames.add("test files/test_merge_1.csv");
        filenames.add("test files/test_merge_2.csv");
        // sets group size
        Main.setMaximumGroupSize(5);
        Main.setMinimumGroupSize();
        // reads files
        assertTrue(Main.readCSVCreate(filenames));
        // creates groups
        Main.beginCreate();

        filenames.clear();
    }

    @Test
    public void testSort4() throws IOException {

        // sets flags
        Main.setLabSectionFlag(false);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(true);

        // adds student file
        filenames.add("test files/student_list.csv");
        // sets group size
        Main.setMaximumGroupSize(5);
        Main.setMinimumGroupSize();
        // reads file
        assertTrue(Main.readCSVCreate(filenames));
        // creates groups
        Main.beginCreate();

        filenames.clear();
    }

    @Test
    public void randomAlgorithm1() throws IOException {
        // sets flags
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(true);

        // adds student file
        filenames.add("sample files/students_sample.csv");
        // sets group size
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        // reads file
        assertTrue(Main.readCSVCreate(filenames));
        // creates groups
        Main.beginRandomCreate();

        filenames.clear();
    }

    @Test
    public void randomAlgorithm2() throws IOException {
        // sets flags
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(true);

        // adds student file
        filenames.add("sample files/students_sample.csv");
        // sets group size
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        // reads file
        assertTrue(Main.readCSVCreate(filenames));
        // creates groups
        Main.beginRandomCreate();

        filenames.clear();

    }

    @Test
    public void randomAlgorithm3() throws IOException {
        // sets flags
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(true);

        // adds student file
        filenames.add("sample files/students_sample.csv");
        // sets group size
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        // reads file
        assertTrue(Main.readCSVCreate(filenames));
        Main.beginRandomCreate();
    }

    @Test
    public void originalAlgorithm() throws IOException {
        // sets flags
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(true);

        // adds student file
        filenames.add("sample files/students_sample.csv");
        // sets group size
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        // reads file
        assertTrue(Main.readCSVCreate(filenames));
        Main.beginCreate();
    }
}