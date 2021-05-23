import oop.ex2.*;

/**
 * this is a SpaceShipFactory: creates ships.
 */
public class SpaceShipFactory {

    /**
     *  creates an array of ships according to arguments.
     * @param args arguments to initialize ships with.
     * @return SpaceShip array.
     */
    public static SpaceShip[] createSpaceShips(String[] args) {
        SpaceShip[] spaceShips = new SpaceShip[args.length];
        for(int idx = 0 ; idx < args.length ; idx ++ ){
            String val = args[idx];
            switch (val){
                case "h":
                    spaceShips[idx] = new HumanShip();
                    break;
                case "r":
                    spaceShips[idx] = new RunnerShip();
                    break;
                case "b":
                    spaceShips[idx] = new BasherShip();
                    break;
                case "a":
                    spaceShips[idx] = new AggressiveShip();
                    break;
                case "d":
                    spaceShips[idx] = new DrunkardShip();
                    break;
                case "s":
                    spaceShips[idx] = new SpecialShip();
                    break;
            }
        } return spaceShips;
    }
}
