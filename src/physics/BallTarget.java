/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import java.util.Random;

/**
 * Author: Gunnar Gorder
 * Updated: 12/12/2015
 * CMSC 325, Final Project, UMUC Fall 2015
 * File: BallTarget.java
 * Description:  The class creates a shape object to act as targets
 * for the player and adds them to the physics environment as well as the respective
 * target list in the PhysicsTestHelper class.  The construtor of the class is
 * called when a shape has been destroyed and needs to be replaced
 */
public class BallTarget {

    public BallTarget(Node rootNode, AssetManager assetManager, PhysicsSpace space, int i){
        Material ballMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                ballMaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/Sky/St Peters/StPeters.jpg"));
                Sphere ball = new Sphere(20, 20, 5);
                String ballName = "Ball" + i;
                Geometry ballGeometry = new Geometry(ballName, ball);
                ballGeometry.setName(ballName);
                ballGeometry.setMaterial(ballMaterial);
                Random rand = new Random();
                float x = rand.nextFloat()*100 - 50;
                float y = rand.nextFloat()*40;
                float z = rand.nextFloat()*100 - 50;
                ballGeometry.setLocalTranslation(x,y, z);
                //RigidBodyControl automatically uses box collision shapes when attached to single geometry with box mesh
                ballGeometry.addControl(new RigidBodyControl(70));
                ballGeometry.getControl(RigidBodyControl.class).setRestitution(1f);
                ballGeometry.getControl(RigidBodyControl.class).setFriction(0.1f);
                 x = rand.nextFloat()*30+20;
                 y = rand.nextFloat()*10;
                 z = rand.nextFloat()*30+20;
                ballGeometry.getControl(RigidBodyControl.class).setLinearVelocity(new Vector3f(x,y,z));
                rootNode.attachChild(ballGeometry);
                space.add(ballGeometry);                
                PhysicsTestHelper.balls.add(ballGeometry);
    }
}
