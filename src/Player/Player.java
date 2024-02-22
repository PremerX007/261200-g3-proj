package Player;

import Maps.Region;

import java.util.LinkedList;

public class Player {
    long budget = 1000;
    Crew myCrew;
    Region mycitycenter;
    LinkedList Myregion;
    public Player(Region mycitycenter, Crew myCrew){
        this.mycitycenter = mycitycenter;
        this.myCrew = myCrew;
    }
    public Player(Region mycitycenter, Crew myCrew, long budget){
        this(mycitycenter, myCrew);
        this.budget = budget;
    }
}
