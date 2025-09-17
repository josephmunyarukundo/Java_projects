package Day_two.Assignement;

import java.util.Scanner;

public class supermarket_bill {
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of different items purchased: ");
        int numItems = scanner.nextInt();

        String[] itemNames = new String[numItems];
        double[] prices = new double[numItems];
        int[] quantities = new int[numItems];

        double totalBill = 0;

        for (int i = 0; i < numItems; i++) {
            System.out.println("\nItem " + (i + 1) + ":");
            System.out.print("Enter item name: ");
            itemNames[i] = scanner.next();
            System.out.print("Enter price per unit: ");
            prices[i] = scanner.nextDouble();
            System.out.print("Enter quantity purchased: ");
            quantities[i] = scanner.nextInt();

            double subtotal = prices[i] * quantities[i];
            totalBill += subtotal;
        }

        double discount = 0;
        if (totalBill > 50000) {
            discount = totalBill * 0.05;
        }
        double finalAmount = totalBill - discount;

        // Print receipt
        System.out.println("\n----- RECEIPT -----");
        System.out.println("Item Name       Quantity   Price Per Unit   Subtotal");

        for (int i = 0; i < numItems; i++) {
            double subtotal = prices[i] * quantities[i];
            System.out.println(itemNames[i] + "       " + quantities[i] + "         " + prices[i] + "           " + subtotal);
        }

        System.out.println("\nGrand Total before discount: " + totalBill);
        if (discount > 0) {
            System.out.println("Discount applied (5%): " + discount);
        }
        System.out.println("Final amount payable: " + finalAmount);

        scanner.close();
    }
}
