import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {


    public static void readCSV(String filename) throws IOException {
        List<Student> students = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String header = bufferedReader.readLine();
        String line = bufferedReader.readLine();
        if(header.contains("Student Name")) {

            while (line != null) {
                String[] student = line.split(",");
                students.add(new Student(student[0]));
                line = bufferedReader.readLine();

            }

            for (Student s : students) {
                System.out.println(s);
            }

        } else {
            System.out.println("Invalid header name");
        }


    }

    public static void main(String [] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the CSV file");
        String filename = scanner.nextLine();
        Main.readCSV(filename);

    }

}
