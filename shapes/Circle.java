package shapes;

public class Circle {
private double r;



public Circle(double r) {
	this.r = r;
}

public double getR() {
	return r;
}

public void setR(double r) {
	this.r = r;
}

public double calculateCircleArea(){
	return Math.PI * Math.pow(r, 2);
}
public double calculateCircleCurcum(){
	return 2 * (Math.PI * r);
}

}
