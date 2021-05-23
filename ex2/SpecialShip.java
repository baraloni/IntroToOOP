/**
 * This is a SpecialShip Class that inherits from SpaceShip Class.
 */
public class SpecialShip extends SpaceShip {
    //* the maximal distance between this ship and the closest one,
    // in which the ship will attempt to activate it's shield
    final public double SHIELD_ACTIVATION_DISTANCE = 0.19;
    // the maximal angel to the closest ship in which the ship will fire
    final double FIRE_ANGLE = 0.21;
    //* the maximal angle from the closest ship to me, in which this ship will attempt teleport
    final double TELEPORT_ANGLE = 0.23;

    @Override
    public void doAction(SpaceWars game) {
        this.isShielded = false;
        SpaceShip closestShip =game.getClosestShipTo(this);
        //teleport part:
        if (Math.abs(closestShip.getPhysics().angleTo(this.position)) < TELEPORT_ANGLE){
            teleport();
        }//move part:
        int turn = this.determineTurningDir(closestShip);
        this.position.move(true, turn);
        //shield part:
        if (this.position.distanceFrom(closestShip.getPhysics()) <= SHIELD_ACTIVATION_DISTANCE) {
            shieldOn();
        }//fire part:
        if (Math.abs(this.position.angleTo(closestShip.getPhysics())) < FIRE_ANGLE){
            fire(game);
        }// updating:
        this.regenerateEnergy();
        this.reduceRoundsToFire();
    }
}
