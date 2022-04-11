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

To create groups, click the "Create Groups" button in the start window. Then a new window will open where you will
select your ideal group size and optimization criteria and click the "Next" button. In the final window, select
one or more files containing the students to be sorted (each file must contain the students name and id numbers). 
Finally, click the "Create Groups" button to create the groups then close the window and find the output files 
in the directory shown in the text box. 

To modify groups, click the "Modify Groups" button in the start window. In the new window that opens, click the "New 
Students" button to select the file with newly registered students, click the "Withdrawn Students" button to select the 
file of students that have withdrawn from the course, click the "Groups" button to select the file of pre-existing 
groups, select the optimization criteria, then click the "Modify Groups" button to modify the groups. 

Both use cases output the same files - "groups.csv" is the sorted groups with the optimization criteria used in the first 
line. "optimization_summary.txt" is the summary of how well the groups adhere to the chosen criteria. 

To run the program:
-
- Download the project as a ZIP file and extract it
- Compile the java files using the following command in the src/main/java folder: javac --module-path "Path to your javafx sdk lib folder" --add-modules javafx.controls,javafx.fxml *.java
- Copy Manifest.txt, the jar files from the java sdk lib folder, and all the dll files from the javafx sdk bin folder to the same directory as your compiled java files
- Create a new folder called "main/resources" in your working folder and copy the .fxml files from src/main/resources and place it in the new folder
- Create the jar file using the following command: jar cfm team-generation-project.jar Manifest.txt *.class *.dll main *.jar
- Run the jar file using the following command: java --module-path "path to the java sdk lib folder" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media -jar team-generation-project.jar
- A window will open prompting you to choose a use case - create or modify groups
- See the output files containing the groups and the optimization summary

Input file specifications:
- 
- The file(s) must be of type csv - you can convert an Excel sheet to csv
- The criteria headers must be "Student Name", "Student ID", "Program", "Grade", "Lab Section", "Email", (case-sensitive)
- The grades must be given in letter form
- For any unused criteria, omit the column
- Multiple files may be chosen (they will be merged by the program), just be sure that each file includes the 
students names and ID numbers
- See the "sample files" folder for examples of how to format the input files

Output files:
- 
The program will output two files:
- "groups.csv" : The list of groups where the first student in each group is 
the team leader and each group has been given a group number in the form "Lx.Gy" where 
x is the lab section and y is the group number. The first line of the file is the optimization criteria used.
- "optimization_summary.txt" : For each of the optimization criteria, the group number
of the groups that do and do not adhere are listed, and the percentage of groups that adhere is calculated.
At the end, all groups that adhere to all the criteria are listed and this percentage is calculated. For the "Modify\
Groups" use case, the number of groups that were modified is given. 

