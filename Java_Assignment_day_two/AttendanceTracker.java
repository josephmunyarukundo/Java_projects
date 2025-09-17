package Day_two.Assignement;

import java.util.Scanner;

public class AttendanceTracker {
	 public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);

	        System.out.print("Enter the total number of students in the class: ");
	        int totalStudents = scanner.nextInt();

	        int maxDays = 30;
	        int[] dailyAttendance = new int[maxDays];
	        int dayCount = 0;

	        String moreDays;

	        do {
	            if (dayCount >= maxDays) {
	                System.out.println("Maximum of 30 days attendance recorded.");
	                break;
	            }

	            System.out.print("Enter number of students present for day " + (dayCount + 1) + ": ");
	            int present = scanner.nextInt();

	            while (present < 0 || present > totalStudents) {
	                System.out.println("Invalid input. Enter number between 0 and " + totalStudents);
	                System.out.print("Enter number of students present for day " + (dayCount + 1) + ": ");
	                present = scanner.nextInt();
	            }
	            dailyAttendance[dayCount] = present;
	            dayCount++;
	            System.out.print("Do you want to enter attendance for another day? (yes/no): ");
	            moreDays = scanner.next();

	        } while (moreDays.equalsIgnoreCase("yes"));
	        int sumAttendance = 0;
	        int lowAttendanceDays = 0;
	        for (int i = 0; i < dayCount; i++) {
	            sumAttendance += dailyAttendance[i];
	            if (dailyAttendance[i] < totalStudents / 2.0) {
	                lowAttendanceDays++;
	            }
	        }
	        double averageAttendance = (dayCount > 0) ? (double) sumAttendance / dayCount : 0;
	        double lowAttendancePercentage = (dayCount > 0) ? ((double) lowAttendanceDays / dayCount) * 100 : 0;
	        System.out.println("\n----- ATTENDANCE SUMMARY -----");
	        System.out.println("Day\tNumber Present");
	        for (int i = 0; i < dayCount; i++) {
	            System.out.println((i + 1) + "\t" + dailyAttendance[i]);
	        }
	        System.out.println("\nAverage attendance: " + Math.round(averageAttendance * 100.0) / 100.0 + " students");
	        System.out.println("Percentage of days with attendance below 50%: " + Math.round(lowAttendancePercentage * 100.0) / 100.0 + "%");

	        scanner.close();
	    }

}
