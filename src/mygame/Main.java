package mygame;

import AI.SoundEmitterControl;
import appstate.InputAppState;
import physics.PhysicsTestHelper;
import characters.MyGameCharacterControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import java.io.File;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

/**
 * Update author: Gunnar Gorder
 * Updated: 12/12/2015
 * File: Main.java
 * CMSC 325, Final Project, UMUC Fall 2015
 * Description:  The main class is the driving class for the application
 * it sets up the environment and adds the characters and physics attributes
 * to the environment calling the other application classes as needed.  The
 * Main class also reads the high score file and maintains the HUD with updated 
 * score counts during game play.
 */
public class Main extends SimpleApplication {
    
    public static BulletAppState bulletAppState;
    private Vector3f normalGravity = new Vector3f(0, -9.81f, 0);
    BitmapText hitText;
    BitmapText btmpHighScore;
    BitmapText btmpTimer;
    BitmapText btmpHighScores;
    public static Material lineMat;
    private InputAppState appStateThis;
    public static String ballPosOutput ="";
    private static long startTime, endTime, timeRemaining;
    String strBulletsFired;
    int totalHits;
    BitmapText instructions;
    String strInstructions = "Shoot as many objects as you can in one minute,\n"
            + "but don't let the monkey get near you!\n"
            + "He'll take away all of your points!";
    boolean instrNotDisplayed = true;    
    private static TreeMap<Integer, String> listHighScores = new TreeMap<Integer, String>();
    private static int lowestHighScore = 0;
    private static String strHighScores;
    boolean highScoresNotDisplayed = true;    
    boolean highScoreChecked = false;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    //This method initializes the application
    //Updated to include custom models and additional characters.
    @Override
    public void simpleInitApp() {
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        getFlyByCamera().setMoveSpeed(45f);
        lineMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Node scene = setupWorld();
        
        CameraNode camNode = new CameraNode("CamNode", cam);
        camNode.setControlDir(CameraControl.ControlDirection.CameraToSpatial);
        camNode.addControl(new SoundEmitterControl());
        getFlyByCamera().setMoveSpeed(45);
        rootNode.attachChild(camNode);
        PhysicsTestHelper.targets.add(camNode);
        PhysicsTestHelper.camNode = camNode;
        
        //setupCharacter(scene);
        //createAICharacter();
    }
    
    private Node setupWorld(){

        //Load the scene created in Homework 2
        Node scene = (Node) assetManager.loadModel("Scenes/Week2Scene.j3o");
        rootNode.attachChild(scene);
        
        
        
        PhysicsTestHelper.createPhysicsTestWorld(rootNode, assetManager, 
                bulletAppState.getPhysicsSpace());
        PhysicsTestHelper.rootNode = rootNode;
        PhysicsTestHelper.assetManager = assetManager;
        PhysicsTestHelper.space = bulletAppState.getPhysicsSpace();
        //Add the Player to the world and use the customer character and input control classes
        
        MyGameCharacterControl charControl = new MyGameCharacterControl(1f,6f,8f);
        charControl.setCamera(cam);
        charControl.setGravity(normalGravity);
        bulletAppState.getPhysicsSpace().add(charControl);
        
        InputAppState appState = new InputAppState();
        this.appStateThis = appState;
        appState.setCharacter(charControl);
        appState.setTargets(PhysicsTestHelper.balls);
        stateManager.attach(appState);
        
     
                
        //Add the "bullets" to the scene to allow the player to shoot the balls
        PhysicsTestHelper.createBallShooter(this,rootNode,bulletAppState.getPhysicsSpace());
        
        //Add a custom font and text to the scene
        BitmapFont myFont = assetManager.loadFont("Interface/Fonts/BasicFont.fnt");

        //Create small x in center of screen for cross-hairs
        BitmapText hudText = new BitmapText(myFont, true);
        hudText.setText("+");
        hudText.setColor(ColorRGBA.White);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());
        
        //Set the text in the middle of the screen
        //Updated centering math for accuracy
        hudText.setLocalTranslation((settings.getWidth()  - hudText.getLineWidth())/2 , settings.getHeight() / 2 + hudText.getLineHeight(), 0f); //Positions text to middle of screen
        guiNode.attachChild(hudText);
        
        //Add title text to the upper left corner of screen
        BitmapText titleText = new BitmapText(myFont, true);
        titleText.setText("Gunnar Gorder - CMSC 325 - Fall 2015");
        titleText.setColor(ColorRGBA.Orange);
        titleText.setSize(guiFont.getCharSet().getRenderedSize());
        
        titleText.setLocalTranslation(1f , settings.getHeight() - hudText.getLineHeight(), 0f);
        guiNode.attachChild(titleText);
        
        //Add hit counters to upper left, below title
        hitText = new BitmapText(myFont, true);
        hitText.setText("Balls Hit = " + PhysicsTestHelper.ballHitCounter +
              "\nCubes Hit = " + PhysicsTestHelper.cubeHitCounter+
              "\nToruses Hit = " + PhysicsTestHelper.torusHitCounter+
              "\nDiscs Hit = " + PhysicsTestHelper.discHitCounter+
              "\nMonkeys Hit = " + PhysicsTestHelper.monkeyHitCounter+
              "\nYour Score = 0");
        hitText.setColor(ColorRGBA.Orange);
        hitText.setSize(guiFont.getCharSet().getRenderedSize());
        
        hitText.setLocalTranslation(1f , settings.getHeight() - hudText.getLineHeight() - hitText.getLineHeight(), 0f);
        guiNode.attachChild(hitText);
        
        
        //Add timer to upper right of screen
        setTimer(1);
        btmpTimer = new BitmapText(myFont, true);
        btmpTimer.setText("Time Remaining - 3:00");
        btmpTimer.setColor(ColorRGBA.Orange);
        btmpTimer.setSize(30f);
        
        btmpTimer.setLocalTranslation(settings.getWidth()- btmpTimer.getLineWidth() - 50f , settings.getHeight(), 0f);
        guiNode.attachChild(btmpTimer);
        
        //Add instructions to the screen on launch
        instructions = new BitmapText(myFont, true);
        instructions.setText(strInstructions);
        instructions.setColor(ColorRGBA.Orange);
        instructions.setSize(guiFont.getCharSet().getRenderedSize());
        
        instructions.setLocalTranslation((settings.getWidth()  - instructions.getLineWidth())/2 , settings.getHeight() / 2 - instructions.getLineHeight(), 0f);
        
        //Prepare bitmap for high scores text
        btmpHighScores = new BitmapText(myFont, true);
        btmpHighScores.setText(strHighScores);
        btmpHighScores.setColor(ColorRGBA.Orange);
        btmpHighScores.setSize(guiFont.getCharSet().getRenderedSize());
        
        btmpHighScores.setLocalTranslation((settings.getWidth()  - btmpHighScores.getLineWidth())/2 , settings.getHeight() / 2 - btmpHighScores.getLineHeight(), 0f);
        
        //Initialize the previous high score list
        initHighScoreList();
        
        //Display highest score recorded
        String strHighScore = "Highest Score = " + listHighScores.lastEntry().getKey();
        btmpHighScore = new BitmapText(myFont, true);
        btmpHighScore.setText(strHighScore);
        btmpHighScore.setColor(ColorRGBA.Orange);
        btmpHighScore.setSize(guiFont.getCharSet().getRenderedSize());
        
        btmpHighScore.setLocalTranslation(1f , settings.getHeight() - 
                hudText.getLineHeight() - hitText.getLineHeight() * 7 -
                btmpHighScore.getLineHeight(), 0f);
        guiNode.attachChild(btmpHighScore);
        
        return scene;
    }
     
    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }
    
    //Update the game display and check the monkey's position relative to the 
    //camera
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        totalHits = (PhysicsTestHelper.ballHitCounter 
                + PhysicsTestHelper.cubeHitCounter 
                + PhysicsTestHelper.torusHitCounter
                + PhysicsTestHelper.discHitCounter
                + PhysicsTestHelper.monkeyHitCounter);
      hitText.setText("Balls Hit = " + PhysicsTestHelper.ballHitCounter +
              "\nCubes Hit = " + PhysicsTestHelper.cubeHitCounter+
              "\nToruses Hit = " + PhysicsTestHelper.torusHitCounter+
              "\nDiscs Hit = " + PhysicsTestHelper.discHitCounter+
              "\nMonkeys Hit = " + PhysicsTestHelper.monkeyHitCounter+
              "\nBullets Fired = " + PhysicsTestHelper.bulletsFired+
              "\nYour Score = " + totalHits);
        
      btmpTimer.setText(getTimerTime());
      
      displayInstructions();
      displayHighScores();
      newHighScore();
     //Check the position of monkey and camera, if the distance between the two
      //is less than a given amount, zero all scores
      Vector3f camcord = (Vector3f)PhysicsTestHelper.targets.get(0).getWorldTranslation().clone();
      Vector3f monkeycord = (Vector3f)PhysicsTestHelper.monkeys.get(0).getWorldTranslation().clone();
      camcord.y = 0;
      monkeycord.y = 0;
      if(camcord.distance(monkeycord)<4f){
          PhysicsTestHelper.ballHitCounter = 0;
          PhysicsTestHelper.cubeHitCounter = 0;
          PhysicsTestHelper.torusHitCounter = 0;
          PhysicsTestHelper.discHitCounter = 0;
          PhysicsTestHelper.monkeyHitCounter = 0;
      }
      
      
    }
    
    //Initialize the high score list from the HighScore.txt file in the working
    //directory.
    private static void initHighScoreList(){
         try {
            File highScoreFile = new File("HighScores.txt");
            Scanner sc = new Scanner(highScoreFile);
            if (sc.hasNext()) {
                    while (sc.hasNextLine()) {
                            Scanner sc2 = new Scanner(sc.nextLine());
                            sc2.useDelimiter(" - ");
                            if (sc2.hasNext()) {
                                    Integer score;
                                    String initials;						
                                    score = Integer.parseInt(sc2.next());
                                    initials = sc2.next();
                                    listHighScores.put(score, initials);						
                            }
                            sc2.close();
                    }
            }else{
                   System.out.println(
                                "The necessary high score file appears empty.\n"                                
                                + "The program will now close.");
                    System.exit(0);
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("High score file not found or error reading file!");
            System.out.println(e);
        }
        
        lowestHighScore = listHighScores.firstKey();
    }
    
    //Set the timer length
    private void setTimer(int minutes){
        startTime = System.currentTimeMillis();
        endTime = startTime + (minutes * 60 * 1000);
    }
    
    //calculate time remaining on timer
    private String getTimerTime(){
        String strTimer = "Time Remaining - ";
        
        timeRemaining = (endTime - System.currentTimeMillis());
        
        if(timeRemaining < 1){
            strTimer = "Times Up!";
        } else {        
            strTimer += ""+String.format("%d : %d ", 
                TimeUnit.MILLISECONDS.toMinutes( timeRemaining),
                TimeUnit.MILLISECONDS.toSeconds(timeRemaining) - 
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeRemaining)));           
        }
        
        
        return strTimer;
    }
    
    //Display the isntructions for the first 10 seconds of game play
    private void displayInstructions(){
        int timeToDisplayInst = 10000;
        if(System.currentTimeMillis() - startTime < timeToDisplayInst && instrNotDisplayed){
            instrNotDisplayed = false;
            guiNode.attachChild(instructions);
        } else if (System.currentTimeMillis() - startTime > timeToDisplayInst && !instrNotDisplayed) {
            instrNotDisplayed = true;
            instructions.removeFromParent();
        }
    }
    
    //Get the high score text to display
    private String getHighScores(){
        String highScores ="";
        for(Map.Entry<Integer, String> entry : listHighScores.entrySet()) {
            highScores = "" + entry.getValue() +" - "+entry.getKey()+"\n" + highScores;
        }
        highScores = "\nPrevious High Scores:\n" + highScores;
        strHighScores = highScores;
        strHighScores = "Your score = "+ totalHits +"\n" + strHighScores;        
        return strHighScores;
    }
    
    //add the high score display to the screen when the timer expires
    private void displayHighScores(){
        if (timeRemaining < 0 && highScoresNotDisplayed) {
            highScoresNotDisplayed = false;
            btmpHighScores.setText(getHighScores());
            guiNode.attachChild(btmpHighScores);
        }
    }
    
    //if the player's score is higher than the lowest recoreded high score, 
    //ask for their initials and record their score
    private void newHighScore(){
        
        //if the player score is greater than any other high score and time remainings is > 0
        //then request the users initials and record their score
        if((timeRemaining < 0)&& (!highScoreChecked)){
            highScoreChecked = true;
            if(lowestHighScore < totalHits){
                String initials = "";
                
                //If the player enters anything except for 3 characters, ask again
                do{
                 initials = JOptionPane.showInputDialog(null, "New High Score!\nEnter your 3 character initials", null);
                }while((initials.length()<3) || (initials.length()>3) || (initials.contains(" ")));
                
                 listHighScores.put(totalHits, initials);
                 while(listHighScores.size()>5){
                     listHighScores.pollFirstEntry();                     
                 }
                 lowestHighScore = listHighScores.firstKey();
        
                String highScores ="";
                for(Map.Entry<Integer, String> entry : listHighScores.entrySet()) {
                    highScores = "" + entry.getValue() +" - "+entry.getKey()+"\n\r" + highScores;
                }
                
                strHighScores = "\nPrevious High Scores:\n" + highScores;
                strHighScores = "Your score = "+ totalHits +"\n" + strHighScores;
                btmpHighScores.setText(strHighScores);
                
                try{
                PrintWriter writer = new PrintWriter("HighScores.txt", "ASCII");
                for(Map.Entry<Integer, String> entry : listHighScores.entrySet()) {
                    writer.println(entry.getKey() +" - "+entry.getValue());
                }
                writer.close();
                }catch(Exception e){
                    System.out.println("Error writing to 'HighScores.txt'");
                    System.out.println(e);
                }
            }
        }
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    
}
