package shapes;

public class Triangle {
private double base;
private double hight;
private double hypo;

public Triangle(double base, double hight, double hypo) {
	
	this.base = base;
	this.hight = hight;
	this.hypo = hypo;
}

public double getBase() {
	return base;
}

public void setBase(double base) {
	this.base = base;
}

public double getHight() {
	return hight;
}

public void setHight(double hight) {
	this.hight = hight;
}

public double getHypo() {
	return hypo;
}

public void setHypo(double hypo) {
	this.hypo = hypo;
}
 public double calcTriangleArea(){
	 return (this.base * this.hight) / 2;
 }
 public double calcTrianglePerimeter(){
	 return this.base + this.hight + this.hypo;
 }

}
