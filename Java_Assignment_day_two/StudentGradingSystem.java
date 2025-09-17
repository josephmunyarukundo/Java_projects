package Day_two.Assignement;

import java.util.Scanner;

public class StudentGradingSystem {
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int totalStudents = 0;
        int passCount = 0;
        int failCount = 0;

        System.out.println("Enter student marks (0-100). Enter -1 to stop.");

        int marks = 0;
        while (true) {
            System.out.print("Enter marks: ");
            marks = scanner.nextInt();

            if (marks == -1) {
                break; // exit loop when sentinel value is entered
            }

            if (marks < 0 || marks > 100) {
                System.out.println("Invalid marks. Please enter between 0 and 100 or -1 to stop.");
                continue;
            }

            totalStudents++;

            char grade;
            if (marks >= 80) {
                grade = 'A';
            } else if (marks >= 70) {
                grade = 'B';
            } else if (marks >= 60) {
                grade = 'C';
            } else if (marks >= 50) {
                grade = 'D';
            } else {
                grade = 'F';
            }

         
            if (marks >= 50) {
                passCount++;
            } else {
                failCount++;
            }

            System.out.println("Grade: " + grade);
        }

        double passRate = 0;
        if (totalStudents > 0) {
            passRate = ((double) passCount / totalStudents) * 100;
        }
        System.out.println("\n----- SUMMARY REPORT -----");
        System.out.println("Total students: " + totalStudents);
        System.out.println("Passed: " + passCount);
        System.out.println("Failed: " + failCount);
        System.out.println("Pass rate: "+passRate);

        scanner.close();
    }

}
