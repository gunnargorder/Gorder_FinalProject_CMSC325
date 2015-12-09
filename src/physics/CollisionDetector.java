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
 * Updated: 11/29/2015
 * CMSC 325, Project 2, UMUC Fall 2015
 * File: CollisionDetector.java
 * Description:  The CollisionDetector class adds a collision detection object
 * to the physics envinronment.  This class is called by the PhysicsTestHelper 
 * class.  Additionally, this class tracks the ball collisions and updates the 
 * ballPosOutput string variable in the Main class.  At the same time, it outputs
 * collision data to the console for user review.
 */
public class CollisionDetector extends GhostControl implements PhysicsCollisionListener {
    
    int ball0count = 0, ball1count = 0, ball2count = 0;
    
    
    public CollisionDetector(){
        mygame.Main.bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }
    
     public void collision(PhysicsCollisionEvent event) {
         Spatial nodeA = event.getNodeA();
         Spatial nodeB = event.getNodeB();
         
         
         //if its a bullet then remove the bullet
         Iterator itB = PhysicsTestHelper.bullets.iterator();
         while(itB.hasNext()){
             Spatial bullet = (Spatial) itB.next();             
                if((nodeA == bullet) || (nodeB == bullet)){  
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
                    PhysicsTestHelper.space.remove(bullet);
                    bullet.removeFromParent();
                    itB.remove();                    
               }
            } 
            if(PhysicsTestHelper.balls.size() < 3){
                PhysicsTestHelper.addBall();
            }            
         }
            
            
            
         
    }
     
