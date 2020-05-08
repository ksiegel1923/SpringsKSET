package Springs;
import java.awt.Color;
import java.util.ArrayList;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.*;
import org.opensourcephysics.frames.*;

import ProjectileKS.OneDMotion;

public class bungee extends AbstractSimulation{
	Circle person = new Circle();
	ArrayList <particle> string = new ArrayList<particle>();
	DisplayFrame frame = new DisplayFrame("x", "y", "Frame");
	double gravity = 9.81;
	double timestep, velocity, acceleration;
	double position = 100;
	int numberSprings;
	double totalMass=10;
	double m=totalMass/numberSprings; //mass of each individual spring
	double kTotal=30000;
	double k=0;
	double lengthTotal=40;
	double length = lengthTotal/numberSprings;
	double personMass = 50;
	double positionAfter=0;
	
	@Override
	protected void doStep() {
		double springPosition = position;
		person.setXY(0, position);
//		if (string.get(numberSprings-1).getPosition()>60) {
//			acceleration = -gravity;
//			
//			for (int i = 0; i < numberSprings; i++) {
//				string.get(i).setVelocity(string.get(i).getVelocity()+acceleration*timestep);
//				string.get(i).setPosition(string.get(i).getPosition()+string.get(i).getVelocity()*timestep + acceleration/2*timestep*timestep);
//				string.get(i).setXY(0, string.get(i).getPosition());
//			}
//		}
//		else {
			for (int i = 0; i < numberSprings; i++) {
//				//Get new position:
//				positionAfter=(string.get(i).getPosition()-(string.get(i).getVelocity()*timestep));
//				
//				//Set new velocity:
//				double deltaY = positionAfter-string.get(i).getPosition();
//				string.get(i).setVelocity(string.get(i).getVelocity()-(string.get(i).getK()*(deltaY)*timestep/getM(string.get(i))));
//			
//				//Set new position:
//				string.get(i).setPosition(positionAfter);
				if(i==0) {
					string.get(i).setA(0);
				}
				
				
//				Get new position:
				positionAfter=(string.get(i).getPosition()+(string.get(i).getVelocity()*timestep));
				
				//Set new velocity:
				string.get(i).setVelocity(string.get(i).getVelocity()+string.get(i).getA()*timestep);
			
				//Set new position:
				string.get(i).setPosition(positionAfter);
				
				//Set new acceleration:
				string.get(i).setA(getF(string, i)/string.get(i).getM());
				string.get(i).setXY(0, string.get(i).getPosition());
				
				control.println("index: " +  i + " position: " + string.get(i).getPosition() + " velocity: " + string.get(i).getVelocity() + " acceleration: " + string.get(i).getA());
			}
//		}
			
//		frame.setPreferredMinMax(-5, 5, position-(1*string.size())-10, position+10);
//		System.out.println(getM(string.get(5)));
	}
	
	public void reset() {
		control.setValue("Timestep", .01);
		control.setValue("Number of springs", 50);
		control.setValue("Mass of Bungee", 10);
		
	}
	
	public void initialize() {
//		this.setDelayTime(1);
		
		timestep=control.getDouble("Timestep");
		numberSprings = control.getInt("Number of springs");
		totalMass = control.getDouble("Mass of Bungee");
		position = 100;
		velocity = 0;
		acceleration = -gravity;
		double springPosition = position;
		for (int i = 0; i < numberSprings; i++) {
			m = totalMass/numberSprings;
			k = kTotal*numberSprings;
			length = lengthTotal/numberSprings;
			particle p = new particle(k, m, length, velocity, acceleration, springPosition-length*i); //velocity acceleration positionY for each
			p.setVelocity(-Math.sqrt(2*gravity*(length*i)));
			if (i==numberSprings-1) {
				p.setM(50);
//				p.setVelocity(-28);
				p.color=Color.blue;
			}
			p.pixRadius= 6;
			string.add(p);
			string.get(i).setXY(0, string.get(i).getPosition());
			
			frame.addDrawable(p);
		}		
		
		frame.setPreferredMinMax(-50, 50, -10, 100);
		frame.setVisible(true);
//		frame.addDrawable(person);
	}
	
	public static void main(String[] args) {
		SimulationControl.createApp(new bungee()); //runs the simulation
	}
	
	public double getLength (ArrayList<particle> string, int index) {
		double length=0;
		if (index==0) {
			length=0;
		}
		else {
//			System.out.println(index);
			length = (string.get(index-1).getPosition()-string.get(index).getPosition());
		}
		return length;
	}
	
	public double getF(ArrayList<particle> string, int index) {
		double force=0;
		double forceDown=0;
		double forceUp =0;
//		if (index==0) {
//			forceDown = (-getM(string.get(index))*gravity);
//		}
		if (index>0) {
			double forceDown1 = (-string.get(index).getM()*gravity);
			double forceDown2 = -string.get(index).getK()*(getLength(string, index+1)-lengthTotal/numberSprings);
			forceDown = (forceDown1+forceDown2);
			forceUp = string.get(index).getK()*(getLength(string, index)-lengthTotal/numberSprings);
		}
		if(index==string.size()-1) {
//			forceUp=-forceDown;
//			forceUp = string.get(index).getK()*(getLength(string, index)-lengthTotal/numberSprings);
			forceDown = -string.get(index).getM()*gravity;
		}
			
		force = (forceUp + forceDown);
		if (index==0) {
			return 0;
		}
		return force;
	}
	
}
