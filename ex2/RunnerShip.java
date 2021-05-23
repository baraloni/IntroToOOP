/**
 * This is a RunnerShip Class that inherits from SpaceShip Class.
 */
public class RunnerShip extends SpaceShip {
    //* the maximal distance between this ship and the closest one, in which this ship will attempt teleport
    private static final double MAX_PROXIMITY = 0.25;
    //* the maximal angle from the closest ship to me, in which this ship will attempt teleport
    private static final double MAX_ANGLE_TO_ME = 0.23;

    @Override
    public void doAction(SpaceWars game) {
        this.isShielded = false;
        SpaceShip closestShip =game.getClosestShipTo(this);
        //check teleport:
        if ((this.position.distanceFrom(closestShip.getPhysics()) < MAX_PROXIMITY) &
                (Math.abs(closestShip.getPhysics().angleTo(this.position)) < MAX_ANGLE_TO_ME)){
            this.teleport();
        }
        // check turn & accelerate:
        int turn = this.determineTurningDir(closestShip);
        this.position.move(true,turn);
        // updating:
        this.regenerateEnergy();
        this.reduceRoundsToFire();
    }

    @Override
    protected int determineTurningDir(SpaceShip closestShip){
        double facingPosition = this.position.angleTo(closestShip.getPhysics());
        if (facingPosition < 0){
            return 1;
        }if (0 < facingPosition){
            return 0;
        }else{
            return -1;
            }
        }
}

