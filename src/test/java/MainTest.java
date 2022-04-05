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
        filenames.add("test.csv");
        assertTrue(Main.readCSVCreate(filenames));

        filenames.remove("test.csv");

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

        Main.setLabSectionFlag(true);
        Main.setGradeFlag(false);
        Main.setTeamLeaderFlag(false);
        Main.setProgramsFlag(false);

        filenames.add("test.csv");
        Main.readCSVCreate(filenames);
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        Main.beginCreate();

        filenames.clear();

    }

    @Test
    public void testSortGrades() throws IOException {
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setTeamLeaderFlag(false);
        Main.setProgramsFlag(false);

        filenames.add("test.csv");
        Main.readCSVCreate(filenames);
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        Main.beginCreate();

        filenames.clear();

    }

    @Test
    public void testSortProgramsTeamLeader() throws IOException {
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(false);
        Main.setTeamLeaderFlag(true);
        Main.setProgramsFlag(true);

        filenames.add("test.csv");
        Main.readCSVCreate(filenames);
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        Main.beginCreate();

        filenames.clear();
    }

    @Test
    public void testSortTeamLeader() throws IOException {
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(false);
        Main.setTeamLeaderFlag(true);
        Main.setProgramsFlag(false);

        filenames.add("test.csv");
        Main.readCSVCreate(filenames);
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        Main.beginCreate();

        filenames.clear();
    }

    @Test
    public void testSortPrograms() throws IOException {
        Main.setLabSectionFlag(true);
        Main.setGradeFlag(false);
        Main.setTeamLeaderFlag(false);
        Main.setProgramsFlag(true);

        filenames.add("test.csv");
        Main.readCSVCreate(filenames);
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        Main.beginCreate();

        filenames.clear();
    }

    @Test
    public void testSort1() throws IOException {

        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(false);

        filenames.add("test.csv");
        Main.readCSVCreate(filenames);
        Main.setMaximumGroupSize(4);
        Main.setMinimumGroupSize();
        Main.beginCreate();

        filenames.clear();


    }

    @Test
    public void testSort2() throws IOException {

        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(true);

        filenames.add("test.csv");
        Main.readCSVCreate(filenames);
        Main.setMaximumGroupSize(5);
        Main.setMinimumGroupSize();
        Main.beginCreate();

        filenames.clear();
    }

    @Test
    public void testSort3() throws IOException {

        Main.setLabSectionFlag(true);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(true);

        filenames.add("test files/test_merge_1.csv");
        filenames.add("test files/test_merge_2.csv");
        Main.setMaximumGroupSize(5);
        Main.setMinimumGroupSize();
        Main.readCSVCreate(filenames);
        Main.beginCreate();

        filenames.clear();
    }

    @Test
    public void testSort4() throws IOException {

        Main.setLabSectionFlag(false);
        Main.setGradeFlag(true);
        Main.setProgramsFlag(true);
        Main.setTeamLeaderFlag(true);

        filenames.add("test files/student_list.csv");
        Main.setMaximumGroupSize(5);
        Main.setMinimumGroupSize();
        Main.readCSVCreate(filenames);
        Main.beginCreate();

        filenames.clear();
    }
}