package battleship.players;

import java.util.Arrays;

import battleship.handling.Settings;
import battleship.ships.Ship;
import battleship.tiles.ShipTile;
import battleship.utilities.Coordinates;

/**
 * This class lets you make your own AI for a computer-controlled player.
 * Read the notes below to find out what you should rewrite.
 */
public class CustomComputerOpponent extends ComputerOpponent {

    /**
     * This is this class' constructor.
     * @param name The player's name.
     */
	private enum State{SEARCH, THINK, SHOOT};
	private State currentState;
	private CurrentShootShip currentShootShip;
	int [][]maps;
	int []ships;
	int []possibleShips;
	Coordinates lastHit;
    public CustomComputerOpponent(String name) {
        super(name); // This calls the base class' constructor, which calls its base class' constructor. You should keep this line.
        currentState=State.SEARCH;
        maps=new int[Settings.PLAYING_FIELD_VERTICAL_SIZE][Settings.PLAYING_FIELD_HORIZONTAL_SIZE];
        ships=new int[] {Settings.ONE_TILE_SHIPS, Settings.TWO_TILE_SHIPS, Settings.THREE_TILE_SHIPS, Settings.FOUR_TILE_SHIPS, Settings.FIVE_TILE_SHIPS};
        lastHit=new Coordinates(-1,-1);
        possibleShips=new int[5];
        currentShootShip=new CurrentShootShip();
    }

    /**
	 toString() overrides an object's behavior when it is cast to or used as a string.
     */
    @Override
    public String toString() {
        return name + " (Custom AI)";
    }

    // ------------------------------------------------------------------------------------------------
    // In runPlayerTypeSpecificShipPlacement() you can change how the AI places its ships.
    // ------------------------------------------------------------------------------------------------

    /**
     * This method handles the placement of all ships available to this player.
     * It overrides ComputerOpponent's default behavior, which is to place all ships randomly.
     * If you want to keep that behaviour, you can call super.placeShipsRandomly().
     * 
     * If you want to write your own ship placement procedure, these are the rules:
     * 1. You must place all the ships contained in the player's fleet's ship list. You can get this list by calling fleet.getShips().
     * 2. You must place every ship exactly once. You must not place any ship twice, or leave any ships unplaced.
     * 3. Ships may not overlap.
     * 4. Ships must be entirely within the playing field. To get the playing field's extents, get Settings.PLAYING_FIELD_HORIZONTAL_SIZE and Settings.PLAYING_FIELD_VERTICAL_SIZE. 
     *
     * Breaking any of these rules will throw an exception and abort the game.
     * 
     * To actually place a ship, call placeShip(Ship ship, Coordinates coordinates, int length, Orientation orientation).
     * To check whether a spot is free, call fleet.locationIsFree(Coordinates coordinates, int length, Orientation orientation).
     */
    @Override
    public void runPlayerTypeSpecificShipPlacement() throws Exception {
        super.runPlayerTypeSpecificShipPlacement();	// This calls the base class' implementation of this method, in which all ships are placed randomly.
    }

    // ------------------------------------------------------------------------------------------------
    // In the methods below you can let your AI decide its next move.
    // ------------------------------------------------------------------------------------------------

    /**
     * This method is called whenever your turn begins, but before being prompted to make your shot.
     * You could use it to print some information about your AI's state.
     */
    @Override
    public void yourTurnHasBegun() {
        // TODO Auto-generated method stub
//    	for(int i=0;i<maps.length;i++)
//    		System.out.println(Arrays.toString(maps[i]));
//    	System.out.println("currentship: "+currentShootShip.small+currentShootShip.big);
    }

    /**
     * 
     * @param target This is the enemy fleet, and you can use this reference to look at all its ships. But you shouldn't do that, because that would be cheating. That you can do this is actually bad programming on my part; it shouldn't be possible! So just be nice and don't cheat.
     */
    @Override
    public Coordinates promptToFireShot() throws Exception {
        //return fireAtRandomTarget();
    	Coordinates prompt;
    	System.out.println(currentState);
    	if(this.currentState==State.SEARCH)
    		prompt= this.findMostPossiblePosition();
    	else if(this.currentState==State.THINK)
    		prompt= this.findDirection();
    	else
    		prompt= this.shootShip();
//    	System.out.println(prompt);
    	return prompt;
    }


    // ------------------------------------------------------------------------------------------------
    // The methods below here are called when either player has fired a shot.
    // Use these to see how your strategy is working out, and to adjust it.
    // The calls to base functions (super.something) feed information to the base player class' results observation.
    // You don't have to use it, but be aware:
    // Without calling these base functions at the appropriate times, the default results observation will not work and you have to make your own.
    // ------------------------------------------------------------------------------------------------

    @Override
    public void youHaveBeenMissed(Coordinates coordinates) {
        // TODO Auto-generated method stub
    }

    @Override
    public void youHaveBeenHit(Coordinates coordinates, Ship ship) {
        // TODO Auto-generated method stub
    }

    @Override
    public void yourShipHasBeenSunk(Coordinates lastHit, Ship ship) {
        // TODO Auto-generated method stub
    }

    @Override
    public void youHaveMissed(Coordinates coordinates) {
        super.youHaveMissed(coordinates);
        maps[coordinates.y][coordinates.x]=1;
        // TODO Auto-generated method stub
    }

    @Override
    public void youHaveHitYourTarget(Coordinates coordinates) {
        super.youHaveHitYourTarget(coordinates);
        currentShootShip.setCoordinates(coordinates);
        maps[coordinates.y][coordinates.x]=2;
        if(this.currentState==State.SEARCH)
        	this.currentState=State.THINK;
        else if(this.currentState==State.THINK) {
        	this.currentState=State.SHOOT;
        	currentShootShip.findDirection();
        	}
        // TODO Auto-generated method stub
    }

    @Override
    public void youHaveSunkAnEnemyShip(Coordinates lastHit, Ship ship) {
        super.youHaveSunkAnEnemyShip(lastHit, ship);
        this.currentState=State.SEARCH;
        this.currentShootShip.Reset();
        for(ShipTile tile:ship.getTiles()) {
        	this.maps[tile.coordinates().y][tile.coordinates().x]=1;
        	if(tile.coordinates().y>0)
        		this.maps[tile.coordinates().y-1][tile.coordinates().x]=1;
        	if(tile.coordinates().y+1<Settings.PLAYING_FIELD_VERTICAL_SIZE)
        		this.maps[tile.coordinates().y+1][tile.coordinates().x]=1;
        	if(tile.coordinates().x>0)
        		this.maps[tile.coordinates().y][tile.coordinates().x-1]=1;
        	if(tile.coordinates().x+1<Settings.PLAYING_FIELD_HORIZONTAL_SIZE)
        		this.maps[tile.coordinates().y][tile.coordinates().x+1]=1;
        }
        this.ships[ship.length()-1]--;
        // TODO Auto-generated method stub
    }

    @Override
    public void youKeepFiringAtASunkShip(Coordinates lastHit, Ship ship) {
        // TODO Auto-generated method stub
    }

    @Override
    public void youKeepFiringAtTheSameHole(Coordinates lastHit) {
        // TODO Auto-generated method stub
    }

    @Override
    public void youKeepFiringAtNothing(Coordinates lastHit) {
        // TODO Auto-generated method stub
    }

    @Override
    public void theEnemyKeepsFiringAtASunkShip(Coordinates lastHit, Ship ship) {
        // TODO Auto-generated method stub
    }

    @Override
    public void theEnemyKeepsFiringAtTheSameHole(Coordinates lastHit, Ship ship) {
        // TODO Auto-generated method stub
    }

    @Override
    public void theEnemyKeepsFiringAtNothing(Coordinates lastHit) {
        // TODO Auto-generated method stub
    }
    
    private Coordinates findMostPossiblePosition() { 
    	double[][] values=new double[Settings.PLAYING_FIELD_VERTICAL_SIZE][Settings.PLAYING_FIELD_HORIZONTAL_SIZE];
    	possibleShips=new int[5];
    	//find how many possible positions for each kind of ship
    	for(int i=1;i<=5;i++) {
    		if(ships[i-1]!=0) {
    			for(int j=0;j<Settings.PLAYING_FIELD_VERTICAL_SIZE;j++)
    				for(int k=0;k<Settings.PLAYING_FIELD_HORIZONTAL_SIZE;k++) 
    				{
    					boolean veritical=true;
    					boolean horizontal=true;
    					for(int l=0;l<i;l++) {
    						if(j+l>=Settings.PLAYING_FIELD_VERTICAL_SIZE)
    							veritical=false;
    						else
    							if(maps[j+l][k]!=0)
    								veritical=false;
    						if(k+l>=Settings.PLAYING_FIELD_HORIZONTAL_SIZE)
    							horizontal=false;
    						else
    							if(maps[j][k+l]!=0)
    								horizontal=false;
    					}
    					if(veritical)
    						possibleShips[i-1]+=1;
    					if(horizontal)
    						possibleShips[i-1]+=1;
    				}
    		}
    		
    	}
    	//give values for each tiles and find maximum
    	double max=0.0;
    	int max_h=0;
    	int max_v=0;
    	for(int i=1;i<=5;i++) {
    		if(possibleShips[i-1]!=0) {
    			for(int j=0;j<Settings.PLAYING_FIELD_VERTICAL_SIZE;j++)
    				for(int k=0;k<Settings.PLAYING_FIELD_HORIZONTAL_SIZE;k++) {
    					boolean veritical=true;
    					boolean horizontal=true;
    					for(int l=0;l<i;l++) {
    						if(j+l>=Settings.PLAYING_FIELD_VERTICAL_SIZE)
    							veritical=false;
    						else
    							if(maps[j+l][k]!=0)
    								veritical=false;
    						if(k+l>=Settings.PLAYING_FIELD_HORIZONTAL_SIZE)
    							horizontal=false;
    						else
    							if(maps[j][k+l]!=0)
    								horizontal=false;
    					}
    					for(int l=0;l<i;l++) {
    						if(veritical) {
    							values[j+l][k]+=ships[i-1]*1.0/possibleShips[i-1];
    							if(values[j+l][k]>=max) {
    								max=values[j+l][k];
    								max_v=j+l;
    								max_h=k;
    							}
    						}
    						if(horizontal) {
    							values[j][k+l]+=ships[i-1]*1.0/possibleShips[i-1];
    							if(values[j][k+l]>=max) {
    								max=values[j][l+k];
    								max_v=j;
    								max_h=l+k;
    							}
    						}
    					}
    				}
    		}
    	}
//    	for(int i=0;i<values.length;i++)
//    	    System.out.println(Arrays.toString(values[i]));
    	lastHit=new Coordinates(max_h,max_v);
    	return lastHit;
    };
    
    private Coordinates findDirection() {
    	lastHit=currentShootShip.big;
    	double left=0.0,right=0.0,up=0.0,down=0.0;
    	for(int i=2;i<=5;i++) {
    		if(ships[i-1]!=0) {
    			for(int j=1;j<i;j++) {
    				if(checkMap(new Coordinates(lastHit.x-j,lastHit.y),new Coordinates(lastHit.x-j+i-1,lastHit.y),maps))
    					left+=ships[i-1]*1.0/possibleShips[i-1];
    				if(checkMap(new Coordinates(lastHit.x+j,lastHit.y),new Coordinates(lastHit.x+j-i+1,lastHit.y),maps))
    					right+=ships[i-1]*1.0/possibleShips[i-1];
    				if(checkMap(new Coordinates(lastHit.x,lastHit.y-j),new Coordinates(lastHit.x,lastHit.y-j+i-1),maps))
    					up+=ships[i-1]*1.0/possibleShips[i-1];
    				if(checkMap(new Coordinates(lastHit.x,lastHit.y+j),new Coordinates(lastHit.x,lastHit.y+j-i+1),maps))
    					down+=ships[i-1]*1.0/possibleShips[i-1];
    			}
    		}
    	}
//    	System.out.println(left+" "+right+" "+up+" "+down);
    	if(left>=right && left>=up &&left >=down)
    		lastHit= new Coordinates(lastHit.x-1,lastHit.y);
    	else if(right>=left && right>=up && right>=down)
    		lastHit= new Coordinates(lastHit.x+1,lastHit.y);
    	else if(up>=right && up>=left && up>down)
    		lastHit= new Coordinates(lastHit.x,lastHit.y-1);
    	else
    		lastHit= new Coordinates(lastHit.x,lastHit.y+1);
    	return lastHit;
    }
    
    private Coordinates shootShip() {
    	if(currentShootShip.isHorizontal) {
    		int left=0;
    		int right=0;
    		for(int i=currentShootShip.small.x-1;Coordinates.coordinateIsWithinPlayingField(i,currentShootShip.small.y)&&maps[currentShootShip.small.y][i]==0;i--) {
    			left++;
    		}
    		for(int i=currentShootShip.big.x+1;Coordinates.coordinateIsWithinPlayingField(i,currentShootShip.big.y)&&maps[currentShootShip.big.y][i]==0;i++) {
    			right++;
    		}
//    		System.out.println("left: "+left+" right: "+right);
    		lastHit=left>right?new Coordinates(currentShootShip.small.x-1,currentShootShip.small.y):new Coordinates(currentShootShip.big.x+1,currentShootShip.big.y);
    	}
    	else {
    		int up=0;
    		int down=0;
    		for(int i=currentShootShip.small.y-1;Coordinates.coordinateIsWithinPlayingField(currentShootShip.small.x,i)&&maps[i][currentShootShip.small.x]==0;i--) {
    			up++;
    		}for(int i=currentShootShip.big.y+1;Coordinates.coordinateIsWithinPlayingField(currentShootShip.big.x,i)&&maps[i][currentShootShip.big.x]==0;i++) {
    			down++;
    		}
//    		System.out.println("up: "+up+" down: "+down);
    		lastHit=up>down?new Coordinates(currentShootShip.small.x,currentShootShip.small.y-1):new Coordinates(currentShootShip.big.x,currentShootShip.big.y+1);
    	}
    	return lastHit;
    }
    
    private static boolean checkMap(Coordinates c1, Coordinates c2, int[][]maps) {
    	if(Coordinates.coordinateIsWithinPlayingField(c1.x, c1.y)&&Coordinates.coordinateIsWithinPlayingField(c2.x, c2.y)) {
    		if(c1.y==c2.y) {
    			for(int i=c1.x<c2.x?c1.x:c2.x; i<=(c1.x<c2.x?c2.x:c1.x); i++) {
    				if(maps[c1.y][i]==1)
    					return false;
    			}
    			return true;
    		}
    		else {
    			for(int i=c1.y<c2.y?c1.y:c2.y; i<=(c1.y<c2.y?c2.y:c1.y); i++) {
    				if(maps[i][c1.x]==1)
    					return false;
    			}
    			return true;
    		}
    	}
    	else return false;
    }
    
    private class CurrentShootShip{
    	public boolean isHorizontal;
    	public Coordinates small;
    	public Coordinates big;
    	private boolean isSearch;
    	
    	public CurrentShootShip() {
    		isHorizontal=false;
    		small=new Coordinates(-1,-1);
    		big=new Coordinates(-1,-1);
    		isSearch=true;
    	}
    	
    	public void Reset() {
    		isHorizontal=false;
    		small=new Coordinates(-1,-1);
    		big=new Coordinates(-1,-1);
    		isSearch=true;
    	}
    	
    	public void setCoordinates(Coordinates c) {
    		if(isSearch) {
    			small=c;
    			big=c;
    			isSearch=false;
    		}
    		else{
    			if(c.x<small.x || c.y<small.y)
    				small=c;
    			else
    				big=c;
    		}
    	}
    	
    	public void findDirection() {
    		if(small.y==big.y)
    			isHorizontal=true;
    	}
    }
//    public static void main(String[] arguments) {
//    	int [][]maps=new int[][]{
//    		{0,0,1,0,1,0,1,0},
//    		{0,0,0,0,0,0,1,1},
//    		{1,1,0,1,0,1,0,0},
//    		{1,0,0,0,1,1,1,0},
//    		{1,0,0,1,0,0,1,0},
//    		{1,0,0,1,1,1,0,0},
//    		{1,0,1,1,0,0,1,0},
//    		{1,1,1,0,1,0,1,1}};
//    	System.out.println(checkMap(new Coordinates(2,1),new Coordinates(2,5),maps));
//    	System.out.println(findDirection(new Coordinates(2,1),new int[] {0,0,0,0,2},new int[] {0,0,0,0,4},maps));
//    }
}
