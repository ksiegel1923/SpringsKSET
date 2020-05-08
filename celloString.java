package Springs;
import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.DisplayFrame;
import orbitalKS.Particle;

/**
 * @author Kara Siegel and Eeshan Tripathi
 *1 meter long string
 *100 masses
 *10g
 *tension = 100N
 *timestep = 0.001s
 *shake end point with a sine function
 *amplitude = 1cm
 */
public class celloString extends AbstractSimulation{
	ArrayList <particle2D> spring = new ArrayList<particle2D>(); //declares an arraylist of particles (this is essentially the bungee cord)
	DisplayFrame frame = new DisplayFrame("x", "y", "Frame"); 
	double timestep; 
	double k; //this is the k constant of each individual spring
	double l; //this is the length of each individual spring (the total length divided by the number of springs)
	double m; //this is the mass of each particle in between the springs
	ArrayList <Double> positionsX = new ArrayList<Double>();
	ArrayList <Double> positionsY = new ArrayList<Double>();
	int numberSprings;
	double totalMass;
	double lengthTotal;
	double amp;
	double t=0;
	double freqL=0;
	double freqR=0;
	boolean rightMove=false;

	@Override
	protected void doStep() {
			//Move the left side of the string
			positionsX.add(0, 0.0);
//			positionsY.add(0, amp*Math.sin(freqL*t*2*Math.PI));
			positionsY.add(0, amp*Math.sin(freqL*t*2*Math.PI)+amp*Math.sin(6*freqL*t*2*Math.PI));

			//Move the rest of the string
			for (int i = 1; i < numberSprings-1; i++) {
				//Set acceleration
				spring.get(i).setAx(getAX(spring.get(i), spring.get(i+1), spring.get(i-1), k));
				spring.get(i).setAy(getAY(spring.get(i), spring.get(i+1), spring.get(i-1), k));	

				//Sets velocity:
				spring.get(i).setVx(spring.get(i).getVx()+spring.get(i).getAx()*timestep);
				spring.get(i).setVy(spring.get(i).getVy()+spring.get(i).getAy()*timestep);

				//Calculates the new position and adds it to the arraylist of positions
				positionsX.add(i, spring.get(i).getPx() + spring.get(i).getVx()*timestep+(1/2)*spring.get(i).getAx()*timestep*timestep);
				positionsY.add(i, spring.get(i).getPy() + spring.get(i).getVy()*timestep+(1/2)*spring.get(i).getAy()*timestep*timestep);
			}
			
			//Move the last spring in the string
			positionsX.add(numberSprings-1, lengthTotal);			
			if (rightMove==true) {
				spring.get(numberSprings-1).setAy(getAY2(spring.get(numberSprings-1), spring.get(numberSprings-2), k));
				spring.get(numberSprings-1).setVy(spring.get(numberSprings-1).getVy()+spring.get(numberSprings-1).getAy()*timestep);
				positionsY.add(numberSprings-1, spring.get(numberSprings-1).getPy() + spring.get(numberSprings-1).getVy()*timestep+(1/2)*spring.get(numberSprings-1).getAy()*timestep*timestep);
			}
			else {
				positionsY.add(numberSprings-1, amp*Math.sin(freqR*t*2*Math.PI));
			}
			
			//Move the strings based on the values calculated
			for (int i = 0; i < numberSprings; i++) {
				//Sets the new position of each mass particle
				spring.get(i).setPx(positionsX.get(i)); 
				spring.get(i).setPy(positionsY.get(i)); 
				//Changes the position of each particle in the simulation 
				spring.get(i).setXY(positionsX.get(i), positionsY.get(i));
			}
			t=t+timestep*10;
	}
	/**
	 * The reset method allows the user to change the original values when they run the simulation
	 */
	public void reset() {
		control.setValue("Timestep", 5e-6);
		control.setValue("Number of Springs", 100);
		control.setValue("Mass", .01);
		control.setValue("K", 1000000);
		control.setValue("Length of String", 1);
		control.setValue("Amplitude", .005);
		control.setValue("Frequency Left", 49.8);
		control.setValue("Frequency Right", 0 );
		control.setValue("Right side move freely", false);
	}

	/**
	 * The initialize sets all the variables before running the simulation
	 */
	public void initialize() {
		this.setDelayTime(1); //Sets how often the doStep runs

		//Sets all the variables that the user adjusted in the reset method
		timestep = control.getDouble("Timestep");
		numberSprings = control.getInt("Number of Springs");
		totalMass = control.getDouble("Mass");
		lengthTotal=control.getDouble("Length of String"); //length of the whole spring
		k=control.getDouble("K");
		m=totalMass/numberSprings; //the mass of each individual spring
		l=lengthTotal/numberSprings; //the length of each spring
		amp=control.getDouble("Amplitude");
		freqL=control.getDouble("Frequency Left");
		freqR=control.getDouble("Frequency Right");
		rightMove = control.getBoolean("Right side move freely");

		for (int i = 0; i < numberSprings; i++) {
			//declaring a new particle for each of the springs
			particle2D p = new particle2D(k, m, l, 0, 0, 0, 0, i*l, 0);
			frame.addDrawable(p); //adds particles to the displayframe
			spring.add(p); //adds particles to the spring arraylist
			p.setXY(p.getPx(), p.getPy()); 
		}
		frame.setPreferredMinMax(-0.1, 1.1, -.15, .15);
		frame.setSquareAspect(false);
		frame.setVisible(true);
	}

	public double getDistance (particle2D p1, particle2D p2) {
		double distance=0;
		distance=((p1.getPx()-p2.getPx())*(p1.getPx()-p2.getPx())) + ((p1.getPy()-p2.getPy())*(p1.getPy()-p2.getPy()));
		distance=Math.sqrt(distance);
		return distance;
	}
	public double getAX (particle2D p, particle2D pA, particle2D pB, double k) {
		double forceX=-k*getDistance(p, pB)*(p.getPx()-pB.getPx())/getDistance(p, pB) + -k*getDistance(p, pA)*(p.getPx()-pA.getPx())/getDistance(p, pA);
		double aX=forceX/p.getM();
		return aX;
	}
	public double getAY (particle2D p, particle2D pA, particle2D pB, double k) {
		double forceY=-k*getDistance(p, pB)*(p.getPy()-pB.getPy())/getDistance(p, pB) + -k*getDistance(p, pA)*(p.getPy()-pA.getPy())/getDistance(p, pA);;
		double aY = forceY/p.getM();
		return aY;
	}
	public double getAY2 (particle2D p, particle2D pB, double k) {
		double forceY=-k*getDistance(p, pB)*(p.getPy()-pB.getPy())/getDistance(p, pB);
		double aY = forceY/p.getM();
		return aY;
	}
	
	public static void main(String[] args) {
		SimulationControl.createApp(new celloString()); //runs the simulation
	}

}

