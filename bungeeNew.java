package Springs;
import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.DisplayFrame;

/**
 * This program models a bungee 40 meter long bungee cord with a 50km person on the bottom of it.
 * The object of this is to find the k constant so that the person will safely land at the bottom of the bridge
 * OUR ANSWER: k=28.75 N/m
 * @author Kara Siegel and Eeshan Tripathi
 *
 */
public class bungeeNew extends AbstractSimulation{
	ArrayList <particle> spring = new ArrayList<particle>(); //declares an arraylist of particles (this is essentially the bungee cord)
	DisplayFrame frame = new DisplayFrame("x", "y", "Frame"); 
	boolean first = true; //this variable lets the computer know if the program has just started running and this lets the bungee free fall for one instant
	double timestep; 
	double k; //this is the k constant of each individual spring
	double l; //this is the length of each individual spring (the total length divided by the number of springs)
	double m; //this is the mass of each particle in between the springs
	double kTotal; //this is the k constant of the whole springs
	ArrayList <Double> positions = new ArrayList<Double>(); //this arraylist holds the values of the new positions of the particles before we move them in the display frame
	
	/**
	 * The doStep continues to repeat and this is where the new values of acceleration, position, and velocity are calculated
	 */
	@Override
	protected void doStep() {
		positions.add(0, 0.0);
		
		for (int i = 1; i < spring.size(); i++) {
			if(first) { //if it is the first time the doStep is run then each mass should just free fall
				double acceleration = -9.81;
				spring.get(i).setA(acceleration);
			}
			else { 
				if (i<spring.size()-1) { // if it is not the bottom particle (the person)
					//Calculates the new acceleration by doing Fnet/m
					double a = (k*(spring.get(i-1).getPosition()-spring.get(i).getPosition()-l)-k*(spring.get(i).getPosition()-spring.get(i+1).getPosition()-l)-(m*9.81))/m;
					//Sets the new acceleration
					spring.get(i).setA(a);
				}
				else { //if it is the bottom particle (person) then the only force down is the force of gravity
					//Calculates acceleration of person
					double a = (k*(spring.get(i-1).getPosition()-spring.get(i).getPosition()-l)-(50*9.81))/50;
					//Sets the acceleration of the person
					spring.get(i).setA(a);
				}
			}
			//Sets the velocity of the person
			spring.get(i).setVelocity(spring.get(i).getVelocity()+spring.get(i).getA()*timestep);
			
			//Calculates the new position and adds it to the arraylist of positions
			positions.add(i, spring.get(i).getPosition() + spring.get(i).getVelocity()*timestep+(1/2)*spring.get(i).getA()*timestep*timestep);
		}
		first = false; //tells the computer that the bungee should no longer free fall
		
		for (int i = 1; i < spring.size(); i++) {
			//Sets the new position of each mass particle
			spring.get(i).setPosition(positions.get(i)); 
			//Changes the position of each particle in the simulation 
			spring.get(i).setXY(0, positions.get(i));
		}
//		//ONLY RUN THIS PART OF THE CODE IF YOU ARE TRYING TO FIND THE RIGHT K VALUE (PROGRAM WILL NOT BUNGEE)
//		//If you are running this then set the original K of the whole spring (in the reset panel) to 29
//		if ((double)Math.round(spring.get(spring.size()-1).getVelocity()*10d)/10d>-0.1 && (double)Math.round(spring.get(spring.size()-1).getVelocity()*10d)/10d<0.1) { //if the velocity of the person is 0
//			if(spring.get(spring.size()-1).getPosition()<0) { //if the position of the person is less then 0 then the k constant is too small
//				System.out.println("Safest value of k: " + (kTotal+0.25)); //prints out one greater than the k constant (the k constant right before the one that fails)
//			}
//			else { //if the person does not die, then make the k consstant a little bit smaller
//				//RESET ALL THE VALUES
//				double position=100;
//				double acceleration = 9.81;
//				kTotal=kTotal-0.25;
//				double lengthTotal=40;
//				for (int i = 0; i < spring.size(); i++) {
//					spring.get(i).setA(-9.81);
//					spring.get(i).setK(kTotal*spring.size());
//					spring.get(i).setLength(lengthTotal/spring.size());
//					spring.get(i).setVelocity(-Math.sqrt(2*9.81*(lengthTotal/spring.size()*i)));
//					spring.get(i).setPosition(position-i*lengthTotal/spring.size());
//					first=true;
//					
//				}
//				k=kTotal*spring.size();
//			}
//		}
	}
	/**
	 * The reset method allows the user to change the original values when they run the simulation
	 */
	public void reset() {
		control.setValue("Timestep", .01);
		control.setValue("Number of Springs", 50);
		control.setValue("Mass of Bungee", 10);
		control.setValue("Original K", 28.75);
	}
	
	/**
	 * The initialize sets all the variables before running the simulation
	 */
	public void initialize() {
		this.setDelayTime(1); //Sets how often the doStep runs
		
		//Sets all the variables that the user adjusted in the reset method
		timestep = control.getDouble("Timestep");
		double numberSprings = control.getDouble("Number of Springs");
		double totalMass = control.getDouble("Mass of Bungee");
		kTotal=control.getDouble("Original K");
		
		double position=100; //sets the original position of the bungee
		double acceleration = 9.81;
		double lengthTotal=40; //length of the whole spring
		k=kTotal*numberSprings; //the k constant of each individual spring
		m=totalMass/numberSprings; //the mass of each individual spring
		l=lengthTotal/numberSprings; //the length of each spring
		
		for (int i = 0; i < numberSprings; i++) {
			//declaring a new particle for each of the springs
			particle p = new particle(kTotal*numberSprings, totalMass/numberSprings, lengthTotal/numberSprings, 0, acceleration, position-i*lengthTotal/numberSprings);
			if (i==numberSprings-1) { //if it is the last spring, then it is actually the person so we need to change the mass
				p.setM(50); //changes the mass of the person
				p.color=Color.blue; 
				p.pixRadius=7; //sets the person to a different size then the rest of the particles in the bungee cord
			}
			frame.addDrawable(p); //adds particles to the displayframe
			spring.add(p); //adds particles to the spring arraylist
			p.setXY(0, p.getPosition()); 
			p.setVelocity(-Math.sqrt(2*9.81*(lengthTotal/numberSprings*i)));
		}
		frame.setPreferredMinMax(-50, 50, -10, 100);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		SimulationControl.createApp(new bungeeNew()); //runs the simulation
	}

}
