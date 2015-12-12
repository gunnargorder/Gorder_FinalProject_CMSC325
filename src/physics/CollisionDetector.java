/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.GhostControl;
import com.jme3.scene.Spatial;
import java.util.Iterator;


/**
 * Author: Gunnar Gorder
 * Updated: 12/12/2015
 * CMSC 325, Final Project, UMUC Fall 2015
 * File: CollisionDetector.java
 * Description:  The CollisionDetector class adds a collision detection object
 * to the physics envinronment.  This class is called by the PhysicsTestHelper 
 * class.  Additionally, this class is used to destroy and create object when 
 * bullets collide with targets.
 */
public class CollisionDetector extends GhostControl implements PhysicsCollisionListener {
    
    int ball0count = 0, ball1count = 0, ball2count = 0;
    
    //Add collision detector to physics space
    public CollisionDetector(){
        mygame.Main.bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }
    
    //When a collision occurs evaluate it
     public void collision(PhysicsCollisionEvent event) {
         Spatial nodeA = event.getNodeA();
         Spatial nodeB = event.getNodeB();
         
         
         //if its a bullet then remove the bullet
         Iterator itB = PhysicsTestHelper.bullets.iterator();
         while(itB.hasNext()){
             Spatial bullet = (Spatial) itB.next();             
                if((nodeA == bullet) || (nodeB == bullet)){ 
                    //If the bullet hits a ball remove the ball
                    Iterator it = PhysicsTestHelper.balls.iterator();
                    while(it.hasNext()){
                        Spatial ball = (Spatial) it.next();
                        if((nodeA == ball) || (nodeB == ball)){
                            PhysicsTestHelper.ballHitCounter++;
                            PhysicsTestHelper.space.remove(ball);                            
                            ball.removeFromParent();
                            it.remove();                
                        }
                    }
                    //If the bullet hits a cube remove the cube
                    Iterator itCube = PhysicsTestHelper.cubes.iterator();
                    while(itCube.hasNext()){
                        Spatial cube = (Spatial) itCube.next();
                        if((nodeA == cube) || (nodeB == cube)){
                            PhysicsTestHelper.cubeHitCounter++;
                            PhysicsTestHelper.space.remove(cube);                            
                            cube.removeFromParent();
                            itCube.remove();                
                        }
                    }
                    
                    //If the bullet hits a torus, remove it
                    Iterator itTor = PhysicsTestHelper.toruses.iterator();
                    while(itTor.hasNext()){
                        Spatial torus = (Spatial) itTor.next();
                        if((nodeA == torus) || (nodeB == torus)){
                            PhysicsTestHelper.torusHitCounter++;
                            PhysicsTestHelper.space.remove(torus);                            
                            torus.removeFromParent();
                            itTor.remove();                
                        }
                    }
                    
                    //If the bullet hits a disc, remove it
                    Iterator itDisc = PhysicsTestHelper.discs.iterator();
                    while(itDisc.hasNext()){
                        Spatial disc = (Spatial) itDisc.next();
                        if((nodeA == disc) || (nodeB == disc)){
                            PhysicsTestHelper.discHitCounter++;
                            PhysicsTestHelper.space.remove(disc);                            
                            disc.removeFromParent();
                            itDisc.remove();                
                        }
                    }
                    
                    //If the bullet hits the monkey, remove it
                    Iterator itMonkey = PhysicsTestHelper.monkeys.iterator();
                    while(itMonkey.hasNext()){
                        Spatial monkey = (Spatial) itMonkey.next();
                        if((nodeA == monkey) || (nodeB == monkey)){
                            PhysicsTestHelper.monkeyHitCounter++;
                            PhysicsTestHelper.space.remove(monkey);                            
                            monkey.removeFromParent();
                            itMonkey.remove();                
                        }
                    }
                    
                    PhysicsTestHelper.space.remove(bullet);
                    bullet.removeFromParent();
                    itB.remove();                    
               }
            } 
            //Replace the object removed from the space to maintain the same
            //number of elements in the field of play
            if(PhysicsTestHelper.balls.size() < 3){
                PhysicsTestHelper.addBall();
            } 
            if(PhysicsTestHelper.cubes.size() < 3){
                PhysicsTestHelper.addCube();
            }    
            if(PhysicsTestHelper.toruses.size() < 3){
                PhysicsTestHelper.addTorus();
            }       
            if(PhysicsTestHelper.discs.size() < 3){
                PhysicsTestHelper.addDisc();
            }           
            if(PhysicsTestHelper.monkeys.size() < 1){
                PhysicsTestHelper.addMonkey();
            } 
         } 
    }
     
