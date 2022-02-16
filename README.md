# team-generation-project

This program sorts a group of students into groups for project-based labs given a 
set of optimization criteria (group size, lab section, grade in a prerequisite course, 
and program enrollment). 

To run the program:
- 
- Download the jar file and open command prompt
- Extract from the jar file (jar xf team_generation_project.jar) the readme file, 
and the sample input file called "test.csv" which shows the required format 
- Run the jar file (java -jar team_generation_project.jar)
- A window will open with checkboxes for each of the optimization criteria. Select each 
of the criteria you wish to use, select the group size, and select your input file.
- Verify the output files containing the groups and the optimization summary

Input file specifications:
- 
- The file must be of type csv - you can convert an Excel sheet to csv
- The columns must be "Student Name,Student ID,Program,Grade,Lab Section,Email" 
(be careful with spacing)
- The grades must be given in letter form
- For any unused criteria...

Output files:
- 
The program will output two files:
- "filename_groups.csv" : The list of groups where the first student in each group is 
the team leader and each group has been given a group number in the form "Lx.Gy" where 
x is the lab section and y is the group number.
- "filename - optimization summary.txt" : For each of the optimization criteria, the group number
of the groups that do and do not adhere are listed, and the percentage of groups that adhere is calculated.
At the end, all groups that adhere to all the criteria are listed and this percentage is calculated. 

