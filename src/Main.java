import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    /**
     * Reads a CSV file into an ArrayList of students
     * @param filename The filename inputted by the user
     * @throws IOException
     */
    public static void readCSV(String filename) throws IOException {

        List<Student> students = new ArrayList<>(); // the list of students
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

        // reads the first line (headers of CSV file)
        String header = bufferedReader.readLine();

        // reads the first student
        String line = bufferedReader.readLine();

        // the header of the CSV file must be "Student Name"
        if(header.contains("Student Name")) {

            while (line != null) {

                // gets the student's name
                String [] student = line.split(",");
                // adds name
                students.add(new Student(student[0]));
                // next line
                line = bufferedReader.readLine();

            }
            for(Student s: students){
                System.out.println(s);
            }


        } else {
            System.out.println("Invalid header name");
        }

    }

    /**
     * Checks if the inputted file is a CSV file
     * @param filename The filename inputted by the user
     * @throws IOException
     */
    public static void validateFile(String filename) throws IOException {

        // Checking file extension
        if(filename.endsWith(".csv")) {
            readCSV(filename);
        } else {
            System.out.println("Invalid file type");
        }


    }


    public static void main(String [] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the CSV file");
        String filename = scanner.nextLine();
        validateFile(filename);




    }

}
