package Springs;
import org.opensourcephysics.display.Circle;

public class particle extends Circle{
	double k=0; //k constant of each spring
	double m=0; //mass of each particle
	double length=0; //length in between each mass
	double velocity=0; //velocity of each particle
	double a=0; //acceleration of each particle
	double position=0; //position of each particle
	
	
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

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
	}

	public particle (double k, double m, double length, double velocity, double a, double position){
		this.k=k;
		this.m=m;
		this.length=length;
		this.velocity=velocity;
		this.a=a;
		this.position=position;
	}
	
}

