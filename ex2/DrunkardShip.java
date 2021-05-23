import java.util.Random;

/**
 *This is  a DrunkardShip Class instance of SpaceShip Class.
 */
public class DrunkardShip extends SpaceShip {
    // a random obj, giving randomness to all drunkard pilot's actions
    private Random myRandom = new Random();

    @Override
    public void doAction(SpaceWars game) {
        this.isShielded = false;
        //check teleport:
        if(myRandom.nextBoolean()){
            this.teleport();
        }
        // check turn & accelerate:
        this.position.move(myRandom.nextBoolean(),myRandom.nextInt(3) - 1);
        //check shield:
        if(myRandom.nextBoolean()) {
            this.shieldOn();
        }
        // check fire:
        if(myRandom.nextBoolean()) {
            this.fire(game);
        }
        // updating:
        this.regenerateEnergy();
        this.reduceRoundsToFire();
    }
}
