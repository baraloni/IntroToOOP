
/**
 * This is a AggressiveShip Class that inherits from SpaceShip Class.
 *
 */
public class AggressiveShip extends SpaceShip {
    // the maximal angel to the closest ship in which the ship will fire
    final private static double MAX_ANGLE_FROM_ME = 0.21;

    @Override
    public void doAction(SpaceWars game) {
        this.isShielded = false;
        SpaceShip closestShip = game.getClosestShipTo(this);
        //check turn & accelerate:
        int turn = this.determineTurningDir(closestShip);
        this.position.move(true,turn);
        // check fire:
        if (Math.abs(this.position.angleTo(closestShip.getPhysics())) < MAX_ANGLE_FROM_ME){
            this.fire(game);
        }
        // updating:
        this.regenerateEnergy();
        this.reduceRoundsToFire();
    }
}
