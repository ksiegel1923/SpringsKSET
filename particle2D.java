package Springs;
import org.opensourcephysics.display.Circle;

public class particle2D extends Circle{
	double k=0; //k constant of each spring
	double m=0; //mass of each particle
	double length=0; //length in between each mass
	double vx=0; //velocity of each particle
	double vy=0;
	double ax=0; //acceleration of each particle
	double ay=0;
	double px=0; //position of each particle
	double py=0;
	public double getK() {
		return k;
	}
	public void setK(double k) {
		this.k = k;
	}
	public double getM() {
		return m;
	}
	public void setM(double m) {
		this.m = m;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getVx() {
		return vx;
	}
	public void setVx(double vx) {
		this.vx = vx;
	}
	public double getVy() {
		return vy;
	}
	public void setVy(double vy) {
		this.vy = vy;
	}
	public double getAx() {
		return ax;
	}
	public void setAx(double ax) {
		this.ax = ax;
	}
	public double getAy() {
		return ay;
	}
	public void setAy(double ay) {
		this.ay = ay;
	}
	public double getPx() {
		return px;
	}
	public void setPx(double px) {
		this.px = px;
	}
	public double getPy() {
		return py;
	}
	public void setPy(double py) {
		this.py = py;
	}
	public particle2D (double k, double m, double length, double vx, double vy, double ax, double ay, double px, double py){
		this.k=k;
		this.m=m;
		this.length=length;
		this.vx=vx;
		this.vy=vy;
		this.ax=ax;
		this.ay=ay;
		this.px=px;
		this.py=py;
	}
}
