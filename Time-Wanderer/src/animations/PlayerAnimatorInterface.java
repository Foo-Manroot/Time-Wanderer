/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animations;

import org.newdawn.slick.Animation;

/**
 *
 * @author propietario
 */
public interface PlayerAnimatorInterface {

    public void setScale(float sc);

    public void resetJumpAnim();

    public void resetAttackAnim();

    public void advanceClimbAnimation();

    public Animation getClimb1();

    public Animation getIdle1R();
    
    public Animation getJump1R();
    
    public Animation getWalk1R();
    
    public Animation getDie1R();
    
    public Animation getAttack1R();
    
    public Animation getIdle1L();
    
    public Animation getJump1L();
    
    public Animation getWalk1L();
    
    public Animation getDie1L();
    
    public Animation getAttack1L();

}
