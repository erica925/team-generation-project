# team-generation-project

This program sorts a group of students into groups for project-based labs given a 
set of optimization criteria (group size, lab section, grade in a prerequisite course, 
and program enrollment). The user specifies the size of the groups and whether each of 
the following optimization criteria will be used: Lab Section, Grade, Mix of Programs, Team Lader from SYSC department

The lab section criterion will ensure that each group has students all enrolled in the same lab section
The grade criterion will ensure that students in the same group have similar grades
The mix of programs criterion will ensure that the students in a group are all enrolled in a different program
The team leader criterion will ensure that each group is assigned a team leader who is enrolled in one of the 
programs offered by the Department of Systems and Computer engineering

The program can either create groups from a list of students or modify a list of pre-existing groups.
To create groups, click the "New Students" button to chose your file(s), select the criteria, and click the
"Create groups" button. To modify groups, click the "New Students" button to select the file with newly registered 
students, click the "Withdrawn Students" button to select the file of students that have withdrawn from the 
course, click the "Groups" button to select the file of pre-existing groups, select the optimization criteria,
then click the "Modify Groups" button to modify the groups. Both use cases output the same files. 

To run the program:
-
- You must have a java version installed that can run code compiled with jdk 15 (else please follow the instructions to compile and package the project yourself)
- Download the jar file and open command prompt 
- Add your input file to the same folder as the jar file
- Run the jar file (java -jar team-generation-project.jar)
- A window will open with checkboxes for each of the optimization criteria. Select each 
of the criteria you wish to use, select the group size, and select your input file(s).
- Verify the output files containing the groups and the optimization summary

If you prefer to create the jar file from the source code:
-
- Download the project as a ZIP file and extract it
- Compile the java files using the following command: javac --module-path "Path to your javafx sdk lib folder" --add-modules javafx.controls,javafx.fxml *.java
- Copy Manifest.txt, the input file, the jar files from the java sdk lib folder, and all the dll files from the javafx sdk bin folder to the same directory as your compiled java files
- Create a new folder called "main/resources" and copy the GUI.fxml file from src/main/resources and place it in the new folder
- Create the jar file using the following command: jar cfm team-generation-project.jar Manifest.txt *.class *.dll main *.jar
- Run the jar file using the following command: java --module-path "path to the java sdk lib folder" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media -jar team-generation-project.jar
- A window will open with checkboxes for each of the optimization criteria. Select each 
of the criteria you wish to use, select the group size, and select your input file.
- Verify the output files containing the groups and the optimization summary

Input file specifications:
- 
- The file must be of type csv - you can convert an Excel sheet to csv
- The columns must be "Student Name,Student ID,Program,Grade,Lab Section,Email" 
- The grades must be given in letter form
- For any unused criteria, you may omit the column
- See "students.csv" or "test.csv" for examples of formatting
- Multiple files may be chosen (they will be merged by the program), just be sure that 
each file includes the students names and ID numbers

Output files:
- 
The program will output two files:
- "groups.csv" : The list of groups where the first student in each group is 
the team leader and each group has been given a group number in the form "Lx.Gy" where 
x is the lab section and y is the group number.
- "optimization_summary.txt" : For each of the optimization criteria, the group number
of the groups that do and do not adhere are listed, and the percentage of groups that adhere is calculated.
At the end, all groups that adhere to all the criteria are listed and this percentage is calculated. 

