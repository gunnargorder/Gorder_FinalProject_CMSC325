
package physics;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.List;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Spatial;

/**
 * Update author: Gunnar Gorder
 * Updated: 11/29/2015
 * CMSC 325, Project 2, UMUC Fall 2015
 * File: PhysicsTestHelper.java
 * Description:  The PhysicsTestHelper class creates the specified
 * physics and environmental objects to be added to the scene.
 */
public class PhysicsTestHelper {

    /**
     * creates a simple physics test world with a floor, an obstacle and some test boxes
     * @param rootNode
     * @param assetManager
     * @param space
     */
    
    //Updated variable targest list provides targets which count as a "hit"
    public static List<Spatial> balls = new ArrayList<Spatial>();
    public static List<Spatial> walls = new ArrayList<Spatial>();
    public static List<Spatial> bullets = new ArrayList<Spatial>();
    public static List<Spatial> targets = new ArrayList<Spatial>();
    public static Node rootNode;
    public static AssetManager assetManager;
    public static PhysicsSpace space;
    public static int ballnum = 0;
    public static int ballHitCounter = 0;
    public static int bulletsFired = 0;
    public static CameraNode camNode;
    
    public static int cubenum = 0;
    public static int cubeHitCounter = 0;    
    public static List<Spatial> cubes = new ArrayList<Spatial>();
    
    public static int torusnum = 0;
    public static int torusHitCounter = 0;    
    public static List<Spatial> toruses = new ArrayList<Spatial>();
    
    public static int discnum = 0;
    public static int discHitCounter = 0;    
    public static List<Spatial> discs = new ArrayList<Spatial>();
    
    public static int monkeynum = 0;
    public static int monkeyHitCounter = 0;    
    public static List<Spatial> monkeys = new ArrayList<Spatial>();
    
    public static List<Spatial> getBalls(){
        return balls;
    }
    public static List<Spatial> getWalls(){
        return walls;
    }

    //createPhysicsTestWorld creates and adds moveable and imoveable objects
    public static void createPhysicsTestWorld(Node rootNode, AssetManager assetManager, PhysicsSpace space) {
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.LightGray);
        rootNode.addLight(light);
        float vis = 0f;
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/splat/grass.jpg"));
        
        Box floorBox = new Box(140, 0.25f, 140);
        Geometry floorGeometry = new Geometry("Floor", floorBox);
        floorGeometry.setName("Floor");
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, 0, 0);
        floorGeometry.addControl(new RigidBodyControl(0));
        floorGeometry.getControl(RigidBodyControl.class).setRestitution(0.5f);
        floorGeometry.getControl(RigidBodyControl.class).setFriction(0.1f);
        rootNode.attachChild(floorGeometry);
        space.add(floorGeometry);
        
        //Create planes to bound field
        
        Material southWallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        southWallMaterial.setColor("Color", new ColorRGBA(1,0,0,vis));
        southWallMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        Box southWall = new Box(280, 140, 0);
        Geometry southWallGeometry = new Geometry("SouthWall", southWall);
        southWallGeometry.setName("SouthWall");
        southWallGeometry.setMaterial(southWallMaterial);
        southWallGeometry.setLocalTranslation(-120, 120, 120);
        southWallGeometry.addControl(new RigidBodyControl(0));
        southWallGeometry.setQueueBucket(Bucket.Transparent);
        southWallGeometry.getControl(RigidBodyControl.class).setRestitution(1f);
        southWallGeometry.getControl(RigidBodyControl.class).setFriction(0f);
        southWallGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(southWall), 0));
        rootNode.attachChild(southWallGeometry);
        space.add(southWallGeometry);
        walls.add(southWallGeometry);
        
        Material northWallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        northWallMaterial.setColor("Color", new ColorRGBA(0,1,0,vis));
        northWallMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        Box northWall = new Box(280, 140, 0);
        Geometry northWallGeometry = new Geometry("NorthWall", northWall);
        northWallGeometry.setName("NorthWall");
        northWallGeometry.setMaterial(northWallMaterial);
        northWallGeometry.setLocalTranslation(-120, 120, -120);
        northWallGeometry.addControl(new RigidBodyControl(0));
        northWallGeometry.setQueueBucket(Bucket.Transparent);
        northWallGeometry.getControl(RigidBodyControl.class).setRestitution(1f);
        northWallGeometry.getControl(RigidBodyControl.class).setFriction(0f);
        northWallGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(northWall), 0));
        rootNode.attachChild(northWallGeometry);
        space.add(northWallGeometry);
        walls.add(northWallGeometry);
        
        Material westWallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        westWallMaterial.setColor("Color", new ColorRGBA(0,0,1,vis));
        westWallMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        Box westWall = new Box(0, 140, 280);
        Geometry westWallGeometry = new Geometry("WestWall", westWall);
        westWallGeometry.setName("WestWall");
        westWallGeometry.setMaterial(westWallMaterial);
        westWallGeometry.setLocalTranslation(120, 120, -120);
        westWallGeometry.addControl(new RigidBodyControl(0));
        westWallGeometry.setQueueBucket(Bucket.Transparent);
        westWallGeometry.getControl(RigidBodyControl.class).setRestitution(1f);
        westWallGeometry.getControl(RigidBodyControl.class).setFriction(0f);
        westWallGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(westWall), 0));
        rootNode.attachChild(westWallGeometry);
        space.add(westWallGeometry);
        walls.add(westWallGeometry);
        
        Material eastWallMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        eastWallMaterial.setColor("Color", new ColorRGBA(0,1,1,vis));
        eastWallMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        Box eastWall = new Box(0, 140, 280);
        Geometry eastWallGeometry = new Geometry("EastWall", eastWall);
        eastWallGeometry.setName("EastWall");
        eastWallGeometry.setMaterial(eastWallMaterial);
        eastWallGeometry.setLocalTranslation(-120, 120, -120);
        eastWallGeometry.addControl(new RigidBodyControl(0));
        eastWallGeometry.setQueueBucket(Bucket.Transparent);
        eastWallGeometry.getControl(RigidBodyControl.class).setRestitution(1f);
        eastWallGeometry.getControl(RigidBodyControl.class).setFriction(0f);
        eastWallGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(eastWall), 0));
        rootNode.attachChild(eastWallGeometry);
        space.add(eastWallGeometry);
        walls.add(eastWallGeometry);
        
        Material lidMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        lidMaterial.setColor("Color", new ColorRGBA(1,0,1,vis));
        lidMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        Box lidWall = new Box(280, 0, 280);
        Geometry lidGeometry = new Geometry("Lid", lidWall);
        lidGeometry.setName("Lid");
        lidGeometry.setMaterial(lidMaterial);
        lidGeometry.setLocalTranslation(-120, 120, -120);
        lidGeometry.addControl(new RigidBodyControl(0));
        lidGeometry.setQueueBucket(Bucket.Transparent);
        lidGeometry.getControl(RigidBodyControl.class).setRestitution(1f);
        lidGeometry.getControl(RigidBodyControl.class).setFriction(0f);
        lidGeometry.addControl(new RigidBodyControl(new MeshCollisionShape(lidWall), 0));
        rootNode.attachChild(lidGeometry);
        space.add(lidGeometry);
        walls.add(lidGeometry);
        
        //Creates bouncing ball targets
        for (int i = 0; i < 3; i++) {   
                ballnum = i;
                cubenum = i;
                torusnum = i;
                discnum = i;
                BallTarget ball = new BallTarget(rootNode, assetManager, space, ballnum);
                CubeTarget cube = new CubeTarget(rootNode, assetManager, space, cubenum);
                TorusTarget torus = new TorusTarget(rootNode, assetManager, space, torusnum);
                DiscTarget disc = new DiscTarget(rootNode, assetManager, space, discnum);
                
        }
        
        MonkeyTarget monkey = new MonkeyTarget(rootNode, assetManager, space, discnum);
        

        CollisionDetector collide = new CollisionDetector();
    }

    /**
     * creates the necessary inputlistener and action to shoot balls from the camera
     * @param app
     * @param rootNode
     * @param space
     */
    public static void createBallShooter(final Application app, final Node rootNode, final PhysicsSpace space) {
        ActionListener actionListener = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {
                Sphere bullet = new Sphere(20, 20, 0.25f, true, false);
                bullet.setTextureMode(TextureMode.Projected);
                Material mat2 = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                TextureKey key2 = new TextureKey("Textures/Sky/Bright/FullskiesBlueClear03.dds");
                key2.setGenerateMips(true);
                Texture tex2 = app.getAssetManager().loadTexture(key2);
                mat2.setTexture("ColorMap", tex2);
                if (name.equals("shoot") && !keyPressed) {
                    Geometry bulletg = new Geometry("bullet", bullet);
                    bulletg.setName("bullet");
                    bulletg.setMaterial(mat2);
                    bulletg.setShadowMode(ShadowMode.CastAndReceive);
                    bulletg.setLocalTranslation(app.getCamera().getLocation());
                    RigidBodyControl bulletControl = new RigidBodyControl(100);
                    bulletg.addControl(bulletControl);
                    bulletControl.setLinearVelocity(app.getCamera().getDirection().mult(100));
                    bulletControl.setRestitution(1f);
                    bulletg.addControl(bulletControl);
                    rootNode.attachChild(bulletg);
                    bullets.add(bulletg);
                    space.add(bulletControl);
                    bulletsFired++;
                }
            }
        };
        app.getInputManager().addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        app.getInputManager().addListener(actionListener, "shoot");
    }
    
    public static void addBall(){
        ballnum++;
        BallTarget ball = new BallTarget(rootNode, assetManager, space, ballnum);
        
    }
    public static void addCube(){
        cubenum++;
        CubeTarget ball = new CubeTarget(rootNode, assetManager, space, cubenum);
        
    }
    public static void addTorus(){
        torusnum++;
        TorusTarget torus = new TorusTarget(rootNode, assetManager, space, torusnum);
        
    }
    public static void addDisc(){
        discnum++;
        DiscTarget disc = new DiscTarget(rootNode, assetManager, space, discnum);
        
    }
    public static void addMonkey(){
        monkeynum++;
       MonkeyTarget monkey = new MonkeyTarget(rootNode, assetManager, space, monkeynum);
       
    }
    
}
