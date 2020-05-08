package Springs;
import java.awt.Color;
import java.util.ArrayList;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.DisplayFrame;

/**
 * @author Kara Siegel and Eeshan Tripathi
 * 
 */

public class slingShot extends AbstractSimulation{
	ArrayList <particle2D> spring1 = new ArrayList<particle2D>(); //declares an arraylist of particles (this is essentially the bungee cord)
	ArrayList <particle2D> spring2 = new ArrayList<particle2D>();
	DisplayFrame frame = new DisplayFrame("x", "y", "Frame"); 
	DisplayFrame frame2 = new DisplayFrame("x", "y", "Side View");

	double timestep=0;
	int numberSprings=0;
	double totalMass = 0;
	double lengthTotal=0;
	double k=0;
	double m=0;
	double l=0;
	double mBall=0;
	ArrayList <Double> positionsX1 = new ArrayList<Double>();
	ArrayList <Double> positionsY1 = new ArrayList<Double>();
	ArrayList <Double> positionsX2 = new ArrayList<Double>();
	ArrayList <Double> positionsY2 = new ArrayList<Double>();
	boolean fin = false;
	double g = (-9.81); 
	double B=(0.02); 
	particle2D rock;
	particle2D rock2;
	particle2D shootingRock;
	boolean collided=false;
	int collisionParticle=0;
	boolean collision1=false;

	@Override
	protected void doStep() {
		//Don't move the first spring in the string:
		positionsX1.add(0, spring1.get(0).getPx());
		positionsY1.add(0, spring1.get(0).getPy());
		positionsX2.add(0, spring2.get(0).getPx());
		positionsY2.add(0, spring2.get(0).getPy());

		//Move the rest of the spring:
		for (int i = 1; i < Math.floor(numberSprings/2)+1; i++) {
			if(i==(Math.floor(numberSprings/2))) { //Set acceleration for ball
				spring1.get(i).setAx(getAX(spring1.get(i), spring2.get(i-1), spring1.get(i-1), k));
				spring1.get(i).setAy(getAY(spring1.get(i), spring2.get(i-1), spring1.get(i-1), k));
				spring2.get(i).setAx(getAX(spring2.get(i), spring1.get(i-1), spring2.get(i-1), k));
				spring2.get(i).setAy(getAY(spring2.get(i), spring1.get(i-1), spring2.get(i-1), k));

				if (spring1.get(i).getAx()<1000 && spring1.get(i).getAx()>-1000) { //if the acceleration of the ball is 0 then let it free fall
					if(!fin) {
						//add the rock into the frame
						rock = new particle2D(k, mBall, l, spring1.get(i).getVx(), spring1.get(i).getVy(), spring1.get(i).getAx(), spring1.get(i).getPy(), spring1.get(i).getPx(), spring1.get(i).getPy());
						rock.color=Color.blue;
						frame.addDrawable(rock); //adds particles to the displayframe
						rock.setXY(rock.getPx(), rock.getPy()); 
						spring1.get(numberSprings/2).color=Color.RED;
						spring1.get(numberSprings/2).setM(m);
						spring2.get(numberSprings/2).color=Color.RED;
						spring2.get(numberSprings/2).setM(m);

						//add the ball to the other frame
						frame2.setVisible(true);
						rock2 = new particle2D(k, mBall, l, spring1.get(i).getVx(), spring1.get(i).getVy(), spring1.get(i).getAx(), spring1.get(i).getPy(), spring1.get(i).getPx(), 3);
						rock2.color=Color.blue;
						frame2.addDrawable(rock2); //adds particles to the displayframe
						rock2.setXY(rock2.getPx(), rock2.getPy()); 


						shootingRock = new particle2D(0, mBall*3, 0, -rock.getVx()*3, 0, rock.getAx(), 0, 15, -2);
						shootingRock.color = Color.GREEN;
						frame.addDrawable(shootingRock);
						shootingRock.setXY(shootingRock.getPx(), shootingRock.getPy());
					}
					fin=true; //release the rock

				}
			}
			else {//Set acceleration of the rest of the springs
				spring1.get(i).setAx(getAX(spring1.get(i), spring1.get(i+1), spring1.get(i-1), k));
				spring1.get(i).setAy(getAY(spring1.get(i), spring1.get(i+1), spring1.get(i-1), k));	
				spring2.get(i).setAx(getAX(spring2.get(i), spring2.get(i+1), spring2.get(i-1), k));
				spring2.get(i).setAy(getAY(spring2.get(i), spring2.get(i+1), spring2.get(i-1), k));
			}

			//Sets velocity:
			spring1.get(i).setVx(spring1.get(i).getVx()+spring1.get(i).getAx()*timestep);
			spring1.get(i).setVy(spring1.get(i).getVy()+spring1.get(i).getAy()*timestep);
			spring2.get(i).setVx(spring2.get(i).getVx()+spring2.get(i).getAx()*timestep);
			spring2.get(i).setVy(spring2.get(i).getVy()+spring2.get(i).getAy()*timestep);

			//Calculates the new position and adds it to the arraylist of positions
			positionsX1.add(i, spring1.get(i).getPx() + spring1.get(i).getVx()*timestep+(1/2)*spring1.get(i).getAx()*timestep*timestep);
			positionsY1.add(i, spring1.get(i).getPy() + spring1.get(i).getVy()*timestep+(1/2)*spring1.get(i).getAy()*timestep*timestep);
			positionsX2.add(i, spring2.get(i).getPx() + spring2.get(i).getVx()*timestep+(1/2)*spring2.get(i).getAx()*timestep*timestep);
			positionsY2.add(i, spring2.get(i).getPy() + spring2.get(i).getVy()*timestep+(1/2)*spring2.get(i).getAy()*timestep*timestep);
		}

		if(fin) {
			//X MOTION ROCK
			rock.setAx(-(B*(rock.getVx()*rock.getVx()))); 
			rock.setVx((rock.getVx()+(rock.getAx()*timestep))); 
			rock.setPx(((((rock.getVx())*timestep)+rock.getPx())));  
			rock.setX(rock.getPx());

			//X MOTION ROCK2
			rock2.setAx(-(B*(rock2.getVx()*rock2.getVx()))); 
			rock2.setVx((rock2.getVx()+(rock2.getAx()*timestep))); 
			rock2.setPx(((((rock2.getVx())*timestep)+rock2.getPx())));  
			rock2.setX(rock2.getPx());
			
			//Y MOTION ROCK2
			if(rock2.getPy()<0) {
				this.stopSimulation();
			}
			if(rock2.getVy()>0) {
				rock2.setAy(g-(B*rock2.getVy()*rock2.getVy()));
			}
			else if(rock2.getVy()<=0) {
				rock2.setAy(g+(B*rock2.getVy()*rock2.getVy()));
			}
			//			System.out.println(rock2.getAy());
			rock2.setVy(rock2.getVy()+(rock2.getAy()*timestep*50000));
			rock2.setPy((((rock2.getVy())*timestep)+rock2.getPy()));
			rock2.setY(rock2.getPy());
			
//			System.out.println(collided);
			if(collided==false) {
				
				for (int j = 0; j < Math.floor(numberSprings/2)+1; j++) {
//					System.out.println(shootingRock.getPx());
//					System.out.println(spring2.get(j).getPx());
					if(shootingRock.getPx()<=spring1.get(j).getPx() && shootingRock.getPy()==spring1.get(j).getPy()) {
						collided=true;
						shootingRock.setVx(collision(spring1.get(j)));
						shootingRock.setAx(spring1.get(j).getAx());
						spring1.get(j).setVx(collision(spring1.get(j)));
						spring1.get(j).setM(shootingRock.getM());
						spring1.get(j).color=Color.MAGENTA;
//						shootingRock.setPx(spring1.get(j).getPx());
//						System.out.println("collided");
						collisionParticle=j;
						collision1=true;
					}
					else if (shootingRock.getPx()<=spring2.get(j).getPx()+1 && shootingRock.getPx()>spring2.get(j).getPx()-1 && shootingRock.getPy()==spring2.get(j).getPy()) {
//					else if (shootingRock.getPx()<=spring2.get(j).getPx() && shootingRock.getPy()==spring2.get(j).getPy()) {	
						collided=true;
						shootingRock.setVx(collision(spring2.get(j)));
						shootingRock.setAx(spring2.get(j).getAx());
						spring2.get(j).setVx(collision(spring2.get(j)));
						spring2.get(j).setM(shootingRock.getM());
						spring2.get(j).color=Color.magenta;
						collisionParticle=j;
					}
				}
			}
		}

		for (int i = 0; i < Math.floor(numberSprings/2)+1; i++) {
			//Sets the new position of each mass particle
			spring1.get(i).setPx(positionsX1.get(i)); 
			spring1.get(i).setPy(positionsY1.get(i)); 
			spring2.get(i).setPx(positionsX2.get(i)); 
			spring2.get(i).setPy(positionsY2.get(i)); 
			//Changes the position of each particle in the simulation 
			spring1.get(i).setXY(positionsX1.get(i), positionsY1.get(i));
			spring2.get(i).setXY(positionsX2.get(i), positionsY2.get(i));
		}
//		System.out.println(fin + "fin");
//		System.out.println("collided" + collided);
//		//SAME POSITION CODE:
		if(fin==true) {
//			if(collided==true && shootingRock.getPx()<0) { //&& shootingRock.getAx()>1495 shootingRock.getPx()<-2
//				if(collision1==true) {
//					shootingRock.setAx(spring1.get(collisionParticle).getAx());
//					shootingRock.setVx(spring1.get(collisionParticle).getVx());
//					shootingRock.setPx(spring1.get(collisionParticle).getPx());
//					shootingRock.setX(shootingRock.getPx());
//				}
//				else {
//					shootingRock.setAx(spring2.get(collisionParticle).getAx());
//					shootingRock.setVx(spring2.get(collisionParticle).getVx());
//					shootingRock.setPx(spring2.get(collisionParticle).getPx());
//					shootingRock.setX(shootingRock.getPx());
//				}
//				
//			}
//			else {
				shootingRock.setAx(-(B*(shootingRock.getVx()*shootingRock.getVx()))); 
				shootingRock.setVx((shootingRock.getVx()+(shootingRock.getAx()*timestep))); 
				shootingRock.setPx(((((shootingRock.getVx())*timestep)+shootingRock.getPx())));  
				shootingRock.setX(shootingRock.getPx());
//			}
//			System.out.println(shootingRock.getAx());
			
//		System.out.println(shootingRock.getPx());
//		System.out.println("spring" + spring2.get(collisionParticle).getPx());
		}
	}

	public void reset() {
		control.setValue("Timestep", 5e-5);
		control.setValue("Number of Springs", 101);
		control.setValue("Mass String", .01);
		control.setValue("Mass Ball", 10);
		control.setValue("K", 1000000);
		control.setValue("Length of String", 1);
	}

	public void initialize() {
		this.setDelayTime(1);

		timestep = control.getDouble("Timestep");
		numberSprings = control.getInt("Number of Springs");
		totalMass = control.getDouble("Mass Ball");
		lengthTotal=control.getDouble("Length of String");
		k=control.getDouble("K");
		m=totalMass/numberSprings;
		l=lengthTotal/numberSprings;		
		mBall=control.getDouble("Mass Ball");

		for (double i = 0; i < Math.floor(numberSprings/2)+1; i=i+1) {
			//declaring a new particle for each of the springs
			if(i==Math.floor(numberSprings/2)) {
				particle2D p = new particle2D(k, mBall, l, 0, 0, 0, 0, -10, 0);
				p.color=Color.blue;
				frame.addDrawable(p); //adds particles to the displayframe
				spring1.add(p); //adds particles to the spring arraylist
				p.setXY(p.getPx(), p.getPy()); 
			}
			else {
				particle2D p = new particle2D(k, m, l, 0, 0, 0, 0, -i/5, ((-i/10)+5));
				frame.addDrawable(p); //adds particles to the displayframe
				spring1.add(p); //adds particles to the spring arraylist
				p.setXY(p.getPx(), p.getPy()); 
			}
		}
		for (double i = 0; i < Math.floor(numberSprings/2)+1; i=i+1) {
			if(i==Math.floor(numberSprings/2)) {
				particle2D p = new particle2D(k, mBall, l, 0, 0, 0, 0, -10, 0);
				p.color=Color.blue;
				frame.addDrawable(p); //adds particles to the displayframe
				spring2.add(p); //adds particles to the spring arraylist
				p.setXY(p.getPx(), p.getPy()); 
			}
			else {
				particle2D p = new particle2D(k, m, l, 0, 0, 0, 0, -i/5, (i/10)-5);
				frame.addDrawable(p); //adds particles to the displayframe
				spring2.add(p); //adds particles to the spring arraylist
				p.setXY(p.getPx(), p.getPy()); 
			}
		}
		frame.setPreferredMinMax(-11, 20, -6, 6);
		frame.setSquareAspect(false);
		frame.setVisible(true);
		frame2.setPreferredMinMax(0, 100, 0, 3.5);
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
	public double getFX2 (particle2D p, particle2D p2, double k) {
		double forceX=-k*getDistance(p, p2)*(p.getPx()-p2.getPx())/getDistance(p, p2);
		return forceX;
	}
	public double getFY2 (particle2D p, particle2D p2, double k) {
		double forceY=-k*getDistance(p, p2)*(p.getPy()-p2.getPy())/getDistance(p, p2);
		return forceY;
	}
	public double collision (particle2D p) {
		double vFinal = (p.getM()*p.getVx()+shootingRock.getM()*shootingRock.getVx())/(p.getM()+shootingRock.getM());
		return vFinal;
	}

	public static void main(String[] args) {
		SimulationControl.createApp(new slingShot()); //runs the simulation
	}
}