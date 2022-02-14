# team-generation-project

This program sorts a group of students into groups for project-based labs given a 
set of optimization criteria (group size, lab section, grade in a prerequisite course, 
and program enrollment). 

To run the program:
-
- Download the jar file and open command prompt 
- Run the jar file (java -jar team-generation-project.jar)
- A window will open with checkboxes for each of the optimization criteria. Select each 
of the criteria you wish to use, select the group size, and select your input file.
- Verify the output files containing the groups and the optimization summary

Input file specifications:
- 
- The file must be of type csv - you can convert an Excel sheet to csv
- The columns must be "Student Name,Student ID,Program,Grade,Lab Section,Email" 
(be careful with spacing)
- The grades must be given in letter form
- For any unused criteria, you must still include each column and you can fill 
each cell with the same value (ex. assign all students "A" for grade if you do 
not have their individual grades)
- See "students.csv" and "test.csv" for examples of formatting

Output files:
- 
The program will output two files:
- "filename_groups.csv" : The list of groups where the first student in each group is 
the team leader and each group has been given a group number in the form "Lx.Gy" where 
x is the lab section and y is the group number. filename is the name of your input file.
- "filename - optimization summary.txt" : For each of the optimization criteria, the group number
of the groups that do and do not adhere are listed, and the percentage of groups that adhere is calculated.
At the end, all groups that adhere to all the criteria are listed and this percentage is calculated. 
