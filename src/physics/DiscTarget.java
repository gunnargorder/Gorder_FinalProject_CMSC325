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
import com.jme3.scene.shape.Cylinder;
import java.util.Random;

/**
 * Author: Gunnar Gorder
 * Updated: 12/12/2015
 * CMSC 325, Final Project, UMUC Fall 2015
 * File: DiscTarget.java
 * Description:  The class creates a shape object to act as targets
 * for the player and adds them to the physics environment as well as the respective
 * target list in the PhysicsTestHelper class.  The construtor of the class is
 * called when a shape has been destroyed and needs to be replaced
 */
public class DiscTarget {

    public DiscTarget(Node rootNode, AssetManager assetManager, PhysicsSpace space, int i){
        Material discMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                discMaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg"));
                Cylinder disc = new Cylinder(20, 20, 4, 0.5f, true);
                String discName = "Disc" + i;
                Geometry discGeometry = new Geometry(discName, disc);
                discGeometry.setName(discName);
                discGeometry.setMaterial(discMaterial);
                Random rand = new Random();
                float x = rand.nextFloat()*100 - 50;
                float y = rand.nextFloat()*40;
                float z = rand.nextFloat()*100 - 50;
                discGeometry.setLocalTranslation(x,y, z);
                //RigidBodyControl automatically uses box collision shapes when attached to single geometry with box mesh
                discGeometry.addControl(new RigidBodyControl(70));
                discGeometry.getControl(RigidBodyControl.class).setRestitution(1f);
                discGeometry.getControl(RigidBodyControl.class).setFriction(0.1f);
                 x = rand.nextFloat()*30+20;
                 y = rand.nextFloat()*10;
                 z = rand.nextFloat()*30+20;
                discGeometry.getControl(RigidBodyControl.class).setLinearVelocity(new Vector3f(x,y,z));
                rootNode.attachChild(discGeometry);
                space.add(discGeometry);                
                PhysicsTestHelper.discs.add(discGeometry);
    }
}
