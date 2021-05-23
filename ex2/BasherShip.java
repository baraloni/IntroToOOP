/**
 * This is a BasherShip Class that inherits from SpaceShip Class.
 */
public class BasherShip extends SpaceShip {
    //* the maximal distance between this ship and the closest one,
    // in which the ship will attempt to activate it's shield
    final private static double SHIELD_PROXIMITY = 0.19;

    @Override
    public void doAction(SpaceWars game) {
        this.isShielded = false;
        SpaceShip closestShip =game.getClosestShipTo(this);
        // check turn & accelerate:
        int turn = this.determineTurningDir(closestShip);
        this.position.move(true,turn);
        //check shield:
        if ( this.position.distanceFrom(closestShip.getPhysics()) <= SHIELD_PROXIMITY) {
            this.shieldOn();
        }
        // updating:
        this.regenerateEnergy();
        this.reduceRoundsToFire();
    }
}
