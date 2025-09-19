package shapes;

import java.util.Scanner;

public class All_shapes {

	public static void main(String[] args) {
		Scanner shp = new Scanner(System.in);
		Rectangle screen = new Rectangle(12,20);
		Square box = new Square(4);
		Circle cir = new Circle(6);
		Triangle tr = new Triangle(2,3,5);

		
		char continueChoice;

		do{
			System.out.println("Select a shape");
			System.out.println("1. Rectangle");
			System.out.println("2. Circle");
			System.out.println("3. Square");
			System.out.println("4. Triangle");


			int  choice = shp.nextInt();
			
			switch(choice){
			case 1:
				System.out.println("Rectangle");
				System.out.println("--------------");
				System.out.println("1. Area");
				System.out.println("2. Perimeter");
				int calculate = shp.nextInt();
				switch (calculate){
				case 1:
					System.out.println("Area: "+screen.calculateRectangleArea());
					break;
				case 2:
					System.out.println("Perimeter: "+screen.calculateRectanglePerim());
					break;
				default:
					System.out.println("Invalid calculation choice");
				}
				break;

			case 2:
				System.out.println("Circle");
				System.out.println("--------------");
				System.out.println("1. Area");
				System.out.println("2. circumfrance");
				int calculate1 = shp.nextInt();
				switch (calculate1){
				case 1:
					System.out.println("Area: "+cir.calculateCircleArea());
					break;
				case 2:
					System.out.println("circumfrance: "+cir.calculateCircleCurcum());
					break;
				default:
					System.out.println("Invalid calculation choice");
				}
				break;

			case 3:
				System.out.println("Sqare");
				System.out.println("--------------");
				System.out.println("1. Area");
				System.out.println("2. Perimeter");
				int calculate2 = shp.nextInt();
				switch (calculate2){
				case 1:
					System.out.println("Area: "+box.calculateSquareArea());
					break;
				case 2:
					System.out.println("Perimeter: "+box.calculateSquarePerimeter());
					break;
				default:
					System.out.println("Invalid calculation choice");
				}
				break;

			case 4:
				System.out.println("Triangle");
				System.out.println("--------------");
				System.out.println("1. Area");
				System.out.println("2. Perimeter");
				int calculate3 = shp.nextInt();
				switch (calculate3){
				case 1:
					System.out.println("Area: "+tr.calcTriangleArea());
					break;
				case 2:
					System.out.println("Perimeter: "+tr.calcTrianglePerimeter());
					break;
				default:
					System.out.println("Invalid calculation choice");
				}
				break;
			default:
				System.out.println("Invalid shape choice");

			 }

		    System.out.println("Do you want to continue? (y/n)");
		    continueChoice = shp.next().charAt(0);

		} while (continueChoice == 'y' || continueChoice == 'Y');

		System.out.println("Program ended.");
		shp.close();

	}



}
