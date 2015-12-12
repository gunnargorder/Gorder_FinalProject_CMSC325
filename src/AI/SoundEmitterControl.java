/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 * Author: Prof Wireman
 * Update author: Gunnar Gorder
 * Updated: 12/12/2015
 * CMSC 325, Final Prject, UMUC Fall 2015
 * File: SoundEmitterControl.java
 * Description:  The SoundEmitterControl class simulates noise to be sensed by
 * the AI.  This class was updated to return noise in all cases to allow the AI
 * to track the target (Player) across the entire field of play.
 */
public class SoundEmitterControl extends AbstractControl{

    private Vector3f lastPosition = new Vector3f();
    private float noiseEmitted = 0f;
    private float maxSpeed = 25f;
    
    @Override
    protected void controlUpdate(float tpf) {
        float movementSpeed = lastPosition.distance(spatial.getWorldTranslation()) / tpf;
        //Noise is always emitted
        if(movementSpeed > 0f){
            movementSpeed = Math.min(movementSpeed, maxSpeed);
            noiseEmitted = movementSpeed / maxSpeed;
        } else {
            //Noise is always emitted
            noiseEmitted = 1f;
        }
        lastPosition.set(spatial.getWorldTranslation());
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public float getNoiseEmitted(){
        return noiseEmitted;
    }
}
