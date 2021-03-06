package Springs;
import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display3d.simple3d.DrawingPanel3D;
import org.opensourcephysics.frames.Display3DFrame;

/**
 * @author Kara Siegel and Eeshan Tripathi
 * 
 */

public class slingShot3D extends AbstractSimulation{
	ArrayList <particle3D> springLeft1 = new ArrayList<particle3D>();  
	ArrayList <particle3D> springLeft2 = new ArrayList<particle3D>(); 
	ArrayList <particle3D> springRight1 = new ArrayList<particle3D>();  
	ArrayList <particle3D> springRight2 = new ArrayList<particle3D>();
	Display3DFrame frame = new Display3DFrame("Sling Shot");
	DrawingPanel3D panel = new DrawingPanel3D();
	double timestep=0;
	int numberSprings=0;
	double totalMass = 0;
	double lengthTotal=0;
	double k=0;
	double m=0;
	double l=0;
	double mBall=0;
	ArrayList <Double> positionsXLeft1 = new ArrayList<Double>();
	ArrayList <Double> positionsYLeft1 = new ArrayList<Double>();
	ArrayList <Double> positionsXLeft2 = new ArrayList<Double>();
	ArrayList <Double> positionsYLeft2 = new ArrayList<Double>();
	ArrayList <Double> positionsXRight1 = new ArrayList<Double>();
	ArrayList <Double> positionsYRight1 = new ArrayList<Double>();
	ArrayList <Double> positionsXRight2 = new ArrayList<Double>();
	ArrayList <Double> positionsYRight2 = new ArrayList<Double>();
	boolean fin = false;
	double g = (-9.81); 
	double B=(0.02); 
	particle3D rock;
	//	particle3D rock2;
	//	particle3D shootingRock;
	int collidedLeft=0;
	int collidedRight=2;
	int collisionParticle=0;
	boolean collision1=false;


	@Override
	protected void doStep() {
		for (int c = 0; c < 50; c++) {
			//LEFT SLING SHOT
			positionsXLeft1.add(0, springLeft1.get(0).getPx());
			positionsYLeft1.add(0, springLeft1.get(0).getPy());
			positionsXLeft2.add(0, springLeft2.get(0).getPx());
			positionsYLeft2.add(0, springLeft2.get(0).getPy());
 
			for (int i = 1; i < Math.floor(numberSprings/2)+1; i++) {
				if(i==(Math.floor(numberSprings/2))) { //Set acceleration for ball
					springLeft1.get(i).setAx(getAX(springLeft1.get(i), springLeft2.get(i-1), springLeft1.get(i-1), k));
					springLeft1.get(i).setAy(getAY(springLeft1.get(i), springLeft2.get(i-1), springLeft1.get(i-1), k));
					springLeft2.get(i).setAx(getAX(springLeft2.get(i), springLeft1.get(i-1), springLeft2.get(i-1), k));
					springLeft2.get(i).setAy(getAY(springLeft2.get(i), springLeft1.get(i-1), springLeft2.get(i-1), k));

					if(springLeft1.get(i).getAx()<1000 && springLeft1.get(i).getAx()>-1000) {
						if(!fin) {
							rock = new particle3D(k, mBall, l, springLeft1.get(i).getVx(), springLeft1.get(i).getVy(), springLeft1.get(i).getVz(), springLeft1.get(i).getAx(), springLeft1.get(i).getAz(), springLeft1.get(i).getPy(), springLeft1.get(i).getPx(), springLeft1.get(i).getPy(), springLeft1.get(i).getPz());
							rock.setRadius(.6);
							rock.getStyle().setFillColor(Color.ORANGE);
							panel.addElement(rock);
							frame.add(panel);
							rock.setXYZ(rock.getPx(), rock.getPy(), rock.getPz());
							springLeft1.get(numberSprings/2).getStyle().setFillColor(Color.WHITE);
							springLeft1.get(numberSprings/2).setM(m);
							springLeft2.get(numberSprings/2).getStyle().setFillColor(Color.WHITE);
							springLeft2.get(numberSprings/2).setM(m);
						}
						fin=true;
					}
				}
				else {//Set acceleration of the rest of the springs
					springLeft1.get(i).setAx(getAX(springLeft1.get(i), springLeft1.get(i+1), springLeft1.get(i-1), k));
					springLeft1.get(i).setAy(getAY(springLeft1.get(i), springLeft1.get(i+1), springLeft1.get(i-1), k));	
					springLeft2.get(i).setAx(getAX(springLeft2.get(i), springLeft2.get(i+1), springLeft2.get(i-1), k));
					springLeft2.get(i).setAy(getAY(springLeft2.get(i), springLeft2.get(i+1), springLeft2.get(i-1), k));
				}

				//Sets velocity:
				springLeft1.get(i).setVx(springLeft1.get(i).getVx()+springLeft1.get(i).getAx()*timestep);
				springLeft1.get(i).setVy(springLeft1.get(i).getVy()+springLeft1.get(i).getAy()*timestep);
				springLeft2.get(i).setVx(springLeft2.get(i).getVx()+springLeft2.get(i).getAx()*timestep);
				springLeft2.get(i).setVy(springLeft2.get(i).getVy()+springLeft2.get(i).getAy()*timestep);

				//Calculates the new position and adds it to the arraylist of positions
				positionsXLeft1.add(i, springLeft1.get(i).getPx() + springLeft1.get(i).getVx()*timestep+(1/2)*springLeft1.get(i).getAx()*timestep*timestep);
				positionsYLeft1.add(i, springLeft1.get(i).getPy() + springLeft1.get(i).getVy()*timestep+(1/2)*springLeft1.get(i).getAy()*timestep*timestep);
				positionsXLeft2.add(i, springLeft2.get(i).getPx() + springLeft2.get(i).getVx()*timestep+(1/2)*springLeft2.get(i).getAx()*timestep*timestep);
				positionsYLeft2.add(i, springLeft2.get(i).getPy() + springLeft2.get(i).getVy()*timestep+(1/2)*springLeft2.get(i).getAy()*timestep*timestep);
			}

			//RIGHT SLING SHOT
//			if(fin) {
				positionsXRight1.add(0, springRight1.get(0).getPx());
				positionsYRight1.add(0, springRight1.get(0).getPy());
				positionsXRight2.add(0, springRight2.get(0).getPx());
				positionsYRight2.add(0, springRight2.get(0).getPy());

				for (int i = 1; i < Math.floor(numberSprings/2)+1; i++) {
					if(i==(Math.floor(numberSprings/2))) { //Set acceleration for ball
						springRight1.get(i).setAx(getAX(springRight1.get(i), springRight2.get(i-1), springRight1.get(i-1), k));
						springRight1.get(i).setAy(getAY(springRight1.get(i), springRight2.get(i-1), springRight1.get(i-1), k));
						springRight2.get(i).setAx(getAX(springRight2.get(i), springRight1.get(i-1), springRight2.get(i-1), k));
						springRight2.get(i).setAy(getAY(springRight2.get(i), springRight1.get(i-1), springRight2.get(i-1), k));
					}
					else {//Set acceleration of the rest of the springs
						springRight1.get(i).setAx(getAX(springRight1.get(i), springRight1.get(i+1), springRight1.get(i-1), k));
						springRight1.get(i).setAy(getAY(springRight1.get(i), springRight1.get(i+1), springRight1.get(i-1), k));	
						springRight2.get(i).setAx(getAX(springRight2.get(i), springRight2.get(i+1), springRight2.get(i-1), k));
						springRight2.get(i).setAy(getAY(springRight2.get(i), springRight2.get(i+1), springRight2.get(i-1), k));
					}

					//Sets velocity:
					springRight1.get(i).setVx(springRight1.get(i).getVx()+springRight1.get(i).getAx()*timestep);
					springRight1.get(i).setVy(springRight1.get(i).getVy()+springRight1.get(i).getAy()*timestep);
					springRight2.get(i).setVx(springRight2.get(i).getVx()+springRight2.get(i).getAx()*timestep);
					springRight2.get(i).setVy(springRight2.get(i).getVy()+springRight2.get(i).getAy()*timestep);

					//Calculates the new position and adds it to the arraylist of positions
					positionsXRight1.add(i, springRight1.get(i).getPx() + springRight1.get(i).getVx()*timestep+(1/2)*springRight1.get(i).getAx()*timestep*timestep);
					positionsYRight1.add(i, springRight1.get(i).getPy() + springRight1.get(i).getVy()*timestep+(1/2)*springRight1.get(i).getAy()*timestep*timestep);
					positionsXRight2.add(i, springRight2.get(i).getPx() + springRight2.get(i).getVx()*timestep+(1/2)*springRight2.get(i).getAx()*timestep*timestep);
					positionsYRight2.add(i, springRight2.get(i).getPy() + springRight2.get(i).getVy()*timestep+(1/2)*springRight2.get(i).getAy()*timestep*timestep);
//				}
			}
			if(fin) {
				rock.setAx(-(B*(rock.getVx()*rock.getVx()))); 
				rock.setVx((rock.getVx()+(rock.getAx()*timestep))); 
				rock.setPx(((((rock.getVx())*timestep)+rock.getPx())));  
				rock.setX(rock.getPx());
				if(collidedRight==0) {
					for (int j = 0; j < Math.floor(numberSprings/2)+1; j++) {
						//					System.out.println("rock" + rock.getPy());
						//					System.out.println(springRight2.get(j).getPy());
						if(rock.getPx()>=springRight1.get(j).getPx() && rock.getPy()>springRight1.get(j).getPy()-.1 &&rock.getPy()<springRight1.get(j).getPy()+.1) {
							//if(rock.getPx()>=springRight1.get(j).getPx() && rock.getPy()==springRight1.get(j).getPy()) {	
							collidedRight=1;
							rock.setVx(collision(springRight1.get(j)));
//							rock.setAx(springRight1.get(j).getAx());
							springRight1.get(j).setVx(collision(springRight1.get(j)));
							springRight1.get(j).setM(rock.getM());
							collisionParticle=j;
							collision1=true;
						}
						else if(rock.getPx()>=springRight2.get(j).getPx() && rock.getPy()>springRight2.get(j).getPy()-.1 &&rock.getPy()<springRight2.get(j).getPy()+.1) {
							collidedRight=1;
							rock.setVx(collision(springRight2.get(j)));
//							rock.setAx(springRight2.get(j).getAx());
							springRight2.get(j).setVx(collision(springRight2.get(j)));
							springRight2.get(j).setM(rock.getM());
							collisionParticle=j;
						}
					}
				}
				if(collidedLeft==2) {
					for (int j = 0; j < Math.floor(numberSprings/2)+1; j++) {
						//					System.out.println("rock" + rock.getPy());
						//					System.out.println(springLeft2.get(j).getPy());
						if(rock.getPx()<=springLeft1.get(j).getPx() && rock.getPy()>springLeft1.get(j).getPy()-.1 &&rock.getPy()<springLeft1.get(j).getPy()+.1) {
							collidedLeft=1;
							rock.setVx(collision(springLeft1.get(j)));
//							rock.setAx(springLeft1.get(j).getAx());
							springLeft1.get(j).setVx(collision(springLeft1.get(j)));
							springLeft1.get(j).setM(rock.getM());
							collisionParticle=j;
							collision1=true;
						}
						else if(rock.getPx()<=springLeft2.get(j).getPx() && rock.getPy()>springLeft2.get(j).getPy()-.1 &&rock.getPy()<springLeft2.get(j).getPy()+.1) {
							collidedLeft=1;
							rock.setVx(collision(springLeft2.get(j)));
//							rock.setAx(springLeft2.get(j).getAx());
							springLeft2.get(j).setVx(collision(springLeft2.get(j)));
							springLeft2.get(j).setM(rock.getM());
							collisionParticle=j;
						}
					}
				}
			}
			for (int i = 0; i < Math.floor(numberSprings/2)+1; i++) {
				//LEFT SPRING:
				//Sets the new position of each mass particle
				springLeft1.get(i).setPx(positionsXLeft1.get(i)); 
				springLeft1.get(i).setPy(positionsYLeft1.get(i)); 
				springLeft2.get(i).setPx(positionsXLeft2.get(i)); 
				springLeft2.get(i).setPy(positionsYLeft2.get(i)); 
				//Changes the position of each particle in the simulation 
				springLeft1.get(i).setXYZ(positionsXLeft1.get(i), positionsYLeft1.get(i), 0);
				springLeft2.get(i).setXYZ(positionsXLeft2.get(i), positionsYLeft2.get(i), 0);

				//RIGHT SPRING:
//				if(fin) {
					//Sets the new position of each mass particle
					springRight1.get(i).setPx(positionsXRight1.get(i)); 
					springRight1.get(i).setPy(positionsYRight1.get(i)); 
					springRight2.get(i).setPx(positionsXRight2.get(i)); 
					springRight2.get(i).setPy(positionsYRight2.get(i)); 
					//Changes the position of each particle in the simulation 
					springRight1.get(i).setXYZ(positionsXRight1.get(i), positionsYRight1.get(i), 0);
					springRight2.get(i).setXYZ(positionsXRight2.get(i), positionsYRight2.get(i), 0);
//				}
			}
			//CODE KEEPING BALL IN SAME POSITION AS SPRING FOR RIGHT SIDE:
			if(fin) {
//				System.out.println("CR: " + collidedRight);
//				System.out.println("CL: " + collidedLeft);
//				System.out.println("A: " + springRight2.get(collisionParticle).getAx());
//				if(collidedRight==1 && rock.getPx()>0) {
				if(collidedRight==1) {
					if(collision1==true  && !(springRight1.get(collisionParticle).getAx()<1000 && springRight1.get(collisionParticle).getAx()>-1000)) {
						rock.setAx(springRight1.get(collisionParticle).getAx());
						rock.setVx(springRight1.get(collisionParticle).getVx());
						rock.setPx(springRight1.get(collisionParticle).getPx());
						rock.setX(rock.getPx());
						
						rock.setAy(springRight1.get(collisionParticle).getAy());
						rock.setVy(springRight1.get(collisionParticle).getVy());
						rock.setPy(springRight1.get(collisionParticle).getPy());
						rock.setY(rock.getPy());
//						System.out.println("Right Up");
					}
					else if(collision1==true  && (springRight1.get(collisionParticle).getAx()<1000 && springRight1.get(collisionParticle).getAx()>-1000)) {
						rock.setPx(springRight1.get(collisionParticle).getPx()-.1);
						rock.setX(rock.getPx());
						rock.setAx(-(B*(rock.getVx()*rock.getVx()))); 
						rock.setVx((rock.getVx()+(rock.getAx()*timestep))); 
						rock.setPx(((((rock.getVx())*timestep)+rock.getPx())));  
						rock.setX(rock.getPx());
						
						rock.setPy(springRight1.get(collisionParticle).getPy()-.1);
						rock.setY(rock.getPy());
						rock.setAy(-(B*(rock.getVy()*rock.getVy()))); 
						rock.setVy((rock.getVy()+(rock.getAy()*timestep))); 
						rock.setPy(((((rock.getVy())*timestep)+rock.getPy())));  
						rock.setY(rock.getPy());
						
						collidedRight=0;
						collidedLeft=2;
						collisionParticle=0;
						collision1=false;
					}
					else if(collision1==false  && !(springRight2.get(collisionParticle).getAx()<1000 && springRight2.get(collisionParticle).getAx()>-1000)) {
						rock.setAx(springRight2.get(collisionParticle).getAx());
						rock.setVx(springRight2.get(collisionParticle).getVx());
						rock.setPx(springRight2.get(collisionParticle).getPx());
						rock.setX(rock.getPx());

						rock.setAy(springRight2.get(collisionParticle).getAy());
						rock.setVy(springRight2.get(collisionParticle).getVy());
						rock.setPy(springRight2.get(collisionParticle).getPy());
						rock.setY(rock.getPy());
						
//						System.out.println("Right Down");
					}
					else if(collision1==false  && (springRight2.get(collisionParticle).getAx()<1000 && springRight2.get(collisionParticle).getAx()>-1000)) {
						rock.setPx(springRight2.get(collisionParticle).getPx()-.1);
						rock.setX(rock.getPx());
						rock.setAx(-(B*(rock.getVx()*rock.getVx()))); 
						rock.setVx((rock.getVx()+(rock.getAx()*timestep))); 
						rock.setPx(((((rock.getVx())*timestep)+rock.getPx())));  
						rock.setX(rock.getPx());

						rock.setPy(springRight2.get(collisionParticle).getPy()-.1);
						rock.setY(rock.getPy());
						rock.setAy(-(B*(rock.getVy()*rock.getVy()))); 
						rock.setVy((rock.getVy()+(rock.getAy()*timestep))); 
						rock.setPy(((((rock.getVy())*timestep)+rock.getPy())));  
						rock.setY(rock.getPy());
						
						collidedRight=0;
						collidedLeft=2;
						collisionParticle=0;
						collision1=false;
					}
				}
				//CODE KEEPING BALL IN SAME POSITION AS SPRING FOR LEFT SIDE:
				else if(collidedLeft==1) {
					if(collision1==true && !(springLeft1.get(collisionParticle).getAx()<1000 && springLeft1.get(collisionParticle).getAx()>-1000)) {
						rock.setAx(springLeft1.get(collisionParticle).getAx());
						rock.setVx(springLeft1.get(collisionParticle).getVx());
						rock.setPx(springLeft1.get(collisionParticle).getPx());
						rock.setX(rock.getPx());
						
						rock.setAy(springLeft1.get(collisionParticle).getAy());
						rock.setVy(springLeft1.get(collisionParticle).getVy());
						rock.setPy(springLeft1.get(collisionParticle).getPy());
						rock.setY(rock.getPy());
						
//						System.out.println("Left Up");
					}
					else if(collision1==true  && (springLeft1.get(collisionParticle).getAx()<1000 && springLeft1.get(collisionParticle).getAx()>-1000)) {
						rock.setPx(springLeft1.get(collisionParticle).getPx()+.1);
						rock.setX(rock.getPx());
						rock.setAx(-(B*(rock.getVx()*rock.getVx()))); 
						rock.setVx((rock.getVx()+(rock.getAx()*timestep))); 
						rock.setPx(((((rock.getVx())*timestep)+rock.getPx())));  
						rock.setX(rock.getPx());
						
						rock.setAy(-(B*(rock.getVy()*rock.getVy()))); 
						rock.setVy((rock.getVy()+(rock.getAy()*timestep))); 
						rock.setPy(((((rock.getVy())*timestep)+rock.getPy())));  
						rock.setY(rock.getPy());
						
						collidedRight=0;
						collidedLeft=2;
						collisionParticle=0;
						collision1=false;
					}
					else if(collision1==false  && !(springLeft2.get(collisionParticle).getAx()<1000 && springLeft2.get(collisionParticle).getAx()>-1000)){
						rock.setAx(springLeft2.get(collisionParticle).getAx());
						rock.setVx(springLeft2.get(collisionParticle).getVx());
						rock.setPx(springLeft2.get(collisionParticle).getPx());
						rock.setX(rock.getPx());
						
						rock.setAy(springLeft2.get(collisionParticle).getAy());
						rock.setVy(springLeft2.get(collisionParticle).getVy());
						rock.setPy(springLeft2.get(collisionParticle).getPy());
						rock.setY(rock.getPy());
						
//						System.out.println("Left Down");
					}
					else if(collision1==false  && (springLeft2.get(collisionParticle).getAx()<1000 && springLeft2.get(collisionParticle).getAx()>-1000)) {
						rock.setPx(springLeft2.get(collisionParticle).getPx()+.1);
						rock.setX(rock.getPx());
						rock.setAx(-(B*(rock.getVx()*rock.getVx()))); 
						rock.setVx((rock.getVx()+(rock.getAx()*timestep))); 
						rock.setPx(((((rock.getVx())*timestep)+rock.getPx())));  
						rock.setX(rock.getPx());
						
						rock.setAy(-(B*(rock.getVy()*rock.getVy()))); 
						rock.setVy((rock.getVx()+(rock.getAy()*timestep))); 
						rock.setPy(((((rock.getVy())*timestep)+rock.getPy())));  
						rock.setY(rock.getPy());
						
						collidedRight=0;
						collidedLeft=2;
						collisionParticle=0;
						collision1=false;
					}
				}
				else {
					rock.setAx(-(B*(rock.getVx()*rock.getVx()))); 
					rock.setVx((rock.getVx()+(rock.getAx()*timestep))); 
					rock.setPx(((((rock.getVx())*timestep)+rock.getPx())));  
					rock.setX(rock.getPx());
					
					rock.setAy(-(B*(rock.getVy()*rock.getVy()))); 
					rock.setVy((rock.getVy()+(rock.getAy()*timestep))); 
					rock.setPy(((((rock.getVy())*timestep)+rock.getPy())));  
					rock.setY(rock.getPy());
					
					collidedRight=0;
					collidedLeft=2;
					collisionParticle=0;
					collision1=false;
				}
			}
			for (particle3D p : springLeft1) {
				p.getStyle().setFillColor(Color.WHITE);
				p.setRadius(.2);
			}
			for (particle3D p : springLeft2) {
				p.getStyle().setFillColor(Color.WHITE);
				p.setRadius(.2);
			}
			
			for (particle3D p : springRight1) {
				p.getStyle().setFillColor(Color.WHITE);
				p.setRadius(.2);
			}
			for (particle3D p : springRight2) {
				p.getStyle().setFillColor(Color.WHITE);
				p.setRadius(.2);
			}
		}
	}
	public void reset() {
		control.setValue("Timestep", 5e-5);
		control.setValue("Number of Springs", 101);
		control.setValue("Mass String", 10);
		control.setValue("Mass Ball", 5);
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
				particle3D p = new particle3D(k, mBall, l, 0, 0, 0, 0, 0, 0, -10, 0, 0);
				p.getStyle().setFillColor(Color.ORANGE);
				panel.addElement(p); //adds particles to the displayframe
				springLeft1.add(p); //adds particles to the spring arraylist
				p.setXYZ(p.getPx(), p.getPy(), p.getPz()); 
				p.setRadius(.6);
			}
			else {
				particle3D p = new particle3D(k, m, l, 0, 0, 0, 0, 0, 0, -i/5, ((-i/10)+5), 0);
				panel.addElement(p); //adds particles to the displayframes
				springLeft1.add(p); //adds particles to the spring arraylist
				p.setXYZ(p.getPx(), p.getPy(), p.getPz()); 
				p.getStyle().setFillColor(Color.WHITE);
				p.setRadius(.2);
			}
		}
		for (double i = 0; i < Math.floor(numberSprings/2)+1; i=i+1) {
			if(i==Math.floor(numberSprings/2)) {
				particle3D p = new particle3D(k, mBall, l, 0, 0, 0, 0, 0, 0, -10, 0, 0);
				p.getStyle().setFillColor(Color.ORANGE);
				panel.addElement(p); //adds particles to the displayframe
				springLeft2.add(p); //adds particles to the spring arraylist
				p.setXYZ(p.getPx(), p.getPy(), p.getPz()); 
				p.setRadius(.6);
			}
			else {
				particle3D p = new particle3D(k, m, l, 0, 0, 0, 0, 0, 0, -i/5, (i/10)-5, 0);
				panel.addElement(p); //adds particles to the displayframe
				springLeft2.add(p); //adds particles to the spring arraylist
				p.setXYZ(p.getPx(), p.getPy(), p.getPz()); 
				p.getStyle().setFillColor(Color.WHITE);
				p.setRadius(.2);
			}
		}
		for (double i = 0; i < Math.floor(numberSprings/2)+1; i=i+1) {
			//declaring a new particle for each of the springs
			if(i==Math.floor(numberSprings/2)) {
				particle3D p = new particle3D(k, mBall, l, 0, 0, 0, 0, 0, 0, 40, 0, 0);
				//				p.color=Color.blue;
				panel.addElement(p); //adds particles to the displayframe
				springRight1.add(p); //adds particles to the spring arraylist
				p.setXYZ(p.getPx(), p.getPy(), p.getPz()); 
				p.setRadius(.2);
				p.getStyle().setFillColor(Color.WHITE);
			}
			else {
//				particle3D p = new particle3D(k, m, l, 0, 0, 0, 0, 0, 0, i/5+40, ((-i/10)+5), 0);
				particle3D p = new particle3D(k, m, l, 0, 0, 0, 0, 0, 0, -i/5+50, (((-i/5)/2)+5), 0);
				panel.addElement(p); //adds particles to the displayframe
				springRight1.add(p); //adds particles to the spring arraylist
				p.setXYZ(p.getPx(), p.getPy(), p.getPz()); 
				p.getStyle().setFillColor(Color.WHITE);
				p.setRadius(.2);
			}
		}
		for (double i = 0; i < Math.floor(numberSprings/2)+1; i=i+1) {
			if(i==Math.floor(numberSprings/2)) {
				particle3D p = new particle3D(k, mBall, l, 0, 0, 0, 0,  0, 0, 40, 0, 0);
				//				p.color=Color.blue;
				panel.addElement(p); 
				springRight2.add(p); 
				p.setXYZ(p.getPx(), p.getPy(), p.getPz()); 
				p.setRadius(.2);
				p.getStyle().setFillColor(Color.WHITE);
			}
			else {
//				particle3D p = new particle3D(k, m, l, 0, 0, 0, 0, 0, 0, i/5+40, (i/10)-5, 0);
				particle3D p = new particle3D(k, m, l, 0, 0, 0, 0, 0, 0, -i/5+50, (i/10)-5, 0);

				panel.addElement(p); 
				springRight2.add(p); 
				p.setXYZ(p.getPx(), p.getPy(), p.getPz()); 
				p.getStyle().setFillColor(Color.WHITE);
				p.setRadius(.2);
			}
		}
		
		panel.setPreferredMinMax(0, 30, -5, 5, -5, -5);

		frame.setDrawingPanel3D(panel);
		frame.setVisible(true);
	}

	public double getDistance (particle3D p1, particle3D p2) {
		double distance=0;
		distance=((p1.getPx()-p2.getPx())*(p1.getPx()-p2.getPx())) + ((p1.getPy()-p2.getPy())*(p1.getPy()-p2.getPy()));
		distance=Math.sqrt(distance);
		return distance;
	}
	public double getAX (particle3D p, particle3D pA, particle3D pB, double k) {
		double forceX=-k*getDistance(p, pB)*(p.getPx()-pB.getPx())/getDistance(p, pB) + -k*getDistance(p, pA)*(p.getPx()-pA.getPx())/getDistance(p, pA);
		double aX=forceX/p.getM();
		return aX;
	}
	public double getAY (particle3D p, particle3D pA, particle3D pB, double k) {
		double forceY=-k*getDistance(p, pB)*(p.getPy()-pB.getPy())/getDistance(p, pB) + -k*getDistance(p, pA)*(p.getPy()-pA.getPy())/getDistance(p, pA);;
		double aY = forceY/p.getM();
		return aY;
	}
	public double getFX2 (particle3D p, particle3D p2, double k) {
		double forceX=-k*getDistance(p, p2)*(p.getPx()-p2.getPx())/getDistance(p, p2);
		return forceX;
	}
	public double getFY2 (particle3D p, particle3D p2, double k) {
		double forceY=-k*getDistance(p, p2)*(p.getPy()-p2.getPy())/getDistance(p, p2);
		return forceY;
	}
	public double collision (particle3D p) {
		double vFinal = (p.getM()*p.getVx()+rock.getM()*rock.getVx())/(p.getM()+rock.getM());
		return vFinal;
	}

	public static void main(String[] args) {
		SimulationControl.createApp(new slingShot3D()); //runs the simulation
	}
}