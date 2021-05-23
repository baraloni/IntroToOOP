import java.awt.Image;
import oop.ex2.*;

/**
 * The API spaceships need to implement for the SpaceWars game. 
 * It is your decision whether SpaceShip.java will be an interface, an abstract class,
 *  a base class for the other spaceships or any other option you will choose.
 *  
 * @author oop
 */
    public abstract class SpaceShip{
    /* constants */
    // the ship's initial health level
    final static int INITIAL_HEALTH = 25;
    // the ship's initial energy level
    final static int INITIAL_ENERGY = 200;
    //the ship's maximal energy level
    final static int MAXIMAL_ENERGY = 250;

    /* health & energy reductions or increases constants : game rules */
    // the reduction from the ship's current health level, after it got hit (either getting shot at or colliding)
    final static int HEALTH_REDUCTION = 1;
    // the value added to the ship's maximal energy, when it has its shields up and collides with another ship
    final static int BASHING_MAX_ENERGY_BOOST = 20;
    // the reduction from the ship's current energy level, when it has its shields up and collides with another ship
    final static int BASHING_CUR_ENERGY_REDUCTION = 15;
    // the reduction from the ship's maximal energy level, after it got hit (either getting shot at or colliding)
    // while the shields are down or while the shields are up but there is not enough energy for the cost
    final static int HIT_MAX_ENERGY_REDUCTION = 5;
    // the reduction from the ship's current energy level, after it got hit (either getting shot at or colliding)
    // while the shields are down or while the shields are up but there is not enough energy for the cost
    final static int HIT_CUR_ENERGY_REDUCTION = 10;
    // the reduction from the ship's current energy level, when it got shot while it has its shields up
    final static int HIT_SHIELD_CUR_ENERGY_REDUCTION = 2;
    // the reduction from the ship's current energy level, when it fires a shot
    final static int FIRE_CUR_ENERGY_REDUCTION = 15;
    // the reduction from the ship's current energy level, when it teleports
    final static int TELEPORT_CUR_ENERGY_REDUCTION = 100;
    // reduction of the round's shield activation use from current energy
    final static int SHIELD_CUR_ENERGY_REDUCTION = 3;
    // the coefficient of the current energy's refill every round
    // (formula: floor value of CurrentVelocity /MaximalVelocity , + 1 )
    final static int ENERGY_REFILL_COEFFICIENT = 2;
    // cooling period duration after firing
    final static int GUN_COOLING_PERIOD = 7;

    /* class fields*/
    SpaceShipPhysics position = new SpaceShipPhysics();
    int maxEnergy = MAXIMAL_ENERGY;
    int curEnergy = INITIAL_ENERGY;
    int curHealth = INITIAL_HEALTH;
    boolean isShielded = false;
    int numRoundsToFire = 0;

    /**
     * Does the actions of this ship for this round. 
     * This is called once per round by the SpaceWars game driver.
     * 
     * @param game the game object to which this ship belongs.
     */
    abstract public void doAction(SpaceWars game);

    /**
     * This method is called every time a collision with this ship occurs 
     */
    public void collidedWithAnotherShip() {
        if (!hitUnSufficientShield()){
            this.maxEnergy = Math.max(this.maxEnergy + BASHING_MAX_ENERGY_BOOST, 0);
            this.curEnergy = Math.max(this.curEnergy - BASHING_CUR_ENERGY_REDUCTION, 0);
        }
    }

    /** 
     * This method is called whenever a ship has died. It resets the ship's 
     * attributes, and starts it at a new random position.
     */
    public void reset(){
        this.position = new SpaceShipPhysics();
        this.maxEnergy = MAXIMAL_ENERGY;
        this.curEnergy = INITIAL_ENERGY;
        this.curHealth = INITIAL_HEALTH;
        this.isShielded = false;
        this.numRoundsToFire = 0;

    }

    /**
     * Checks if this ship is dead.
     * 
     * @return true if the ship is dead. false otherwise.
     */
    public boolean isDead() {return this.curHealth == 0;}

    /**
     * Gets the physics object that controls this ship.
     * 
     * @return the physics object that controls the ship.
     */
    public SpaceShipPhysics getPhysics() {
        return this.position;
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship
     * gets shot at (with or without a shield).
     */
    public void gotShot() {
        if (!hitUnSufficientShield()){
            this.curEnergy = Math.max(this.curEnergy - HIT_SHIELD_CUR_ENERGY_REDUCTION, 0);
        }
    }

    /**
     * updates the ships inner health & energy status in case of getting hit while shields ae down or up but cant
     * support the energy cost.
     * @return true if the ship got hit while the shields were down or while the shields were up but there was
     * not enough energy for the cost. false otherwise.
     */
    private boolean hitUnSufficientShield(){
        if (!this.isShielded){
            this.curHealth -= HEALTH_REDUCTION;}
        if ((!this.isShielded)|(this.isShielded & this.curEnergy < BASHING_CUR_ENERGY_REDUCTION)) {
            this.maxEnergy = Math.max(this.maxEnergy - HIT_MAX_ENERGY_REDUCTION, 0);//Energy never neg
            this.curEnergy = Math.max(this.curEnergy - HIT_CUR_ENERGY_REDUCTION, 0);
            return true;
        }return false;
    }
    /**
     * Gets the image of this ship. This method should return the image of the
     * ship with or without the shield. This will be displayed on the GUI at
     * the end of the round.
     * 
     * @return the image of this ship.
     */
    public Image getImage(){
        if (this.isShielded){
            return GameGUI.ENEMY_SPACESHIP_IMAGE_SHIELD;
        }else{
            return GameGUI.ENEMY_SPACESHIP_IMAGE;
        }
    }

    /**
     * Attempts to fire a shot.
     * 
     * @param game the game object.
     */
    public void fire(SpaceWars game) {
        if ((this.curEnergy >= FIRE_CUR_ENERGY_REDUCTION) &(this.numRoundsToFire == 0)) {
            this.curEnergy -= FIRE_CUR_ENERGY_REDUCTION;
            game.addShot(this.position);
            this.numRoundsToFire = GUN_COOLING_PERIOD;
        }
    }

    /**
     * Attempts to turn on the shield.
     */
    public void shieldOn() {
        if(this.curEnergy >= SHIELD_CUR_ENERGY_REDUCTION){
            this.curEnergy -= SHIELD_CUR_ENERGY_REDUCTION;
            this.isShielded = true;
        }
    }

    /**
     * Attempts to teleport.
     */
    public void teleport() {
        if(this.curEnergy >= TELEPORT_CUR_ENERGY_REDUCTION){
            this.curEnergy -= TELEPORT_CUR_ENERGY_REDUCTION;
            this.position = new SpaceShipPhysics();
        }
    }

    /**
     * updating the ship's energy in shipWars game at the end of every turn.
     * calculated by the formula: 2*floor value of (CurrentVelocity /MaximalVelocity) + 1
     */
    protected void regenerateEnergy(){
        int energyBoost = ENERGY_REFILL_COEFFICIENT *
                (int)(this.position.getVelocity()/this.position.getMaxVelocity()) + 1;
        this.curEnergy = Math.min(this.curEnergy + energyBoost, this.maxEnergy);
    }

    /**
     * updates ship's ability to fire
     */
    protected void reduceRoundsToFire(){
        if (this.numRoundsToFire > 0) {
            this.numRoundsToFire -= 1;

        }
    }

    /**
     * decides which way to turn in order to encounter the closest SpaceShip object.
     * @param closestShip the other ship this ship wants to encounter
     * @return : int, -1 to turn right, 1 to turn left or 0 to keep current heading.
     */
    protected int determineTurningDir(SpaceShip closestShip) {
        double facingPosition = this.position.angleTo(closestShip.getPhysics());
        if (facingPosition < 0) {
            return -1;
        }if (0 < facingPosition) {
            return 1;
        } else {
            return 0;
        }
    }
}
