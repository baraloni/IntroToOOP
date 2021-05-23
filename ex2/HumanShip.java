import oop.ex2.GameGUI;
import oop.ex2.SpaceShipPhysics;

import java.awt.*;

/**
 * This is a HumanShip Class that inherits from SpaceShip Class.
 */

public class HumanShip extends SpaceShip {

    @Override
    public void doAction(SpaceWars game) {
        this.isShielded = false;
        //check teleport:
        if(game.getGUI().isTeleportPressed()){
            this.teleport();
        }
        // check turn & accelerate:
        int turn = 0;
        if (game.getGUI().isLeftPressed() & !game.getGUI().isRightPressed()) {
            turn = 1;
        }if (!game.getGUI().isLeftPressed() & game.getGUI().isRightPressed()) {
            turn = -1;
        }
        this.position.move(game.getGUI().isUpPressed(),turn);
        //check shield:
        if (game.getGUI().isShieldsPressed()) {
            this.shieldOn();
        }
        // check fire:
        if (game.getGUI().isShotPressed()) {
            this.fire(game);
        }
        // updating:
        this.regenerateEnergy();
        this.reduceRoundsToFire();
    }

    @Override
    public Image getImage(){
        if (this.isShielded){
            return GameGUI.SPACESHIP_IMAGE_SHIELD;
        }else{
            return GameGUI.SPACESHIP_IMAGE;
        }
    }
}
