/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import AI.AIControl;
import AI.SoundEmitterControl;
import animations.AIAnimationControl;
import characters.AICharacterControl;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static mygame.Main.lineMat;

/**
 *
 * @author Gunnar
 */
/**
 * Author: Gunnar Gorder
 * Updated: 11/29/2015
 * CMSC 325, Project 2, UMUC Fall 2015
 * File: BallTarget.java
 * Description:  The BallTarget class creates ball object to act as targets
 * for the AI and adds them to the physics environment as well as the target
 * list field of the PhysicsTestHelper class.
 */
public class MonkeyTarget {

    public MonkeyTarget(Node rootNode, AssetManager assetManager, PhysicsSpace space, int i){
          
        Node monkey = (Node) assetManager.loadModel("Models/Jaime/Jaime.j3o");
        
        monkey.setLocalScale(2.0f);
        
        //Node mainPlayer = createPlayerCharacter();
        AICharacterControl physicsCharacter = new AICharacterControl(0.3f, 2.5f, 8f);
        
        monkey.addControl(physicsCharacter);
        space.add(physicsCharacter);
        rootNode.attachChild(monkey);
        monkey.addControl(new AIControl());
        monkey.addControl(new AIAnimationControl());
        Random rand = new Random();
                float x = rand.nextFloat()*100 - 50;
                float y = rand.nextFloat()*40;
                float z = rand.nextFloat()*100 - 50;
                monkey.setLocalTranslation(x, y, z);
        
        
        Geometry g = new Geometry("", new Box(1,2,1));
        g.setName("Monkey" + i);
        g.setModelBound(new BoundingSphere(5f, Vector3f.ZERO));
        g.updateModelBound();
        g.setMaterial(lineMat);
        //PhysicsTestHelper.camNode.attachChild(g);
        //targets.add(mainPlayer);
        
        //sinbad.getControl(AIControl.class).setState(AIControl.State.Follow);
        monkey.getControl(AIControl.class).setTargetList(PhysicsTestHelper.targets);
        //sinbad.getControl(AIControl.class).setTarget(camNode);

        PhysicsTestHelper.monkeys.add(monkey);
    }
}
