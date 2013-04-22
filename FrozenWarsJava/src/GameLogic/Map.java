package GameLogic;

import java.util.ArrayList;
import java.util.Iterator;
import com.badlogic.gdx.math.Vector3;

public class Map {
	
	//Type of sunken Square in the sunkenBoard
	public enum SunkenTypes{empty,sunkenObject}
	
	//Type of Fissure Square in the fissuresBoard
	public enum FissuresTypes{empty, barrelWithFissure, fissureC, fissureSX, fissureSY, crossingFissures, invisibleFissureSX, 
		invisibleFissureSY, crossInvisibleFissure}
	
	//Type of Water Square in the waterBoard
	public enum WaterTypes{empty,water1SOpN, water1SOpS, water1SOpE, water1SOpW, water2SOpCornerEN, water2SOpCornerNW, 
		water2SOpCornerSE, water2SOpCornerWS, water2SOpBridgeX, water2SOpBridgeY, water3SOpN, 
		water3SOpS, water3SOpE, water3SOpW, water4SOp}
	
	//Type of Basic Square in the boardGame
	public enum TypeSquare{empty,unbreakable,breakable,bootUpgrade,rangeUpgrade,numHarpoonUpgrade,
		invisible,harpoon}
	
	//Total number of each Upgrade
	private int maxBootUpgrades;
	private int maxRangeUpgrades;
	private int maxNumHarpoonsUpgrades;
	private int maxThrowUpgrades;
	private int[] upgrades;//vector de barriles indicando que mejora hay en cada barril, -infinito si no hay mejora.
	private int numBarrels;
	
	//Board Attributes
	private String mapName;
	private int length;
	private int width;
	
	//Game Boards
	private TypeSquare[][] boardGame;
	private FissuresTypes[][] fissuresBoard;
	private WaterTypes[][] waterBoard;
	private SunkenTypes[][] sunkenBoard;
	
	public Map(int lenght, int width, int[] upgrades,XMLMapReader xmlMapReader) {
		this.length=lenght;
		this.width=width;
		this.numBarrels = 0;
		this.upgrades = upgrades;
		this.boardGame = new TypeSquare[lenght][width];
		this.fissuresBoard = new FissuresTypes[lenght][width];
		this.waterBoard = new WaterTypes[lenght][width];
		this.sunkenBoard = new SunkenTypes[lenght][width];
		//Initialize the 3 board with empty square.
		//Only basic board has another type square unbreakeable square
		for (int i=0;i<width;i++){
			for (int j=0;j<length;j++){
				if ((i % 2 == 1) && (j % 2 == 1)) boardGame[i][j] = TypeSquare.unbreakable;
				else boardGame[i][j] = TypeSquare.empty;
				fissuresBoard[i][j] = FissuresTypes.empty;
				waterBoard[i][j] = WaterTypes.empty;
				sunkenBoard[i][j] = SunkenTypes.empty;
			}	
		}
		xmlMapReader.loadMap(this);
	}

	//METHODS OF PUT FISSURE
	
	public void putHarpoonAt(int xHarpoonPosition, int yHarpoonPosition) {
		boardGame[xHarpoonPosition][yHarpoonPosition]= TypeSquare.harpoon;
	}

	public void addAllFissures(ArrayList<Harpoon> harpoonList){
		removeAllFissures();
		Iterator<Harpoon> it = harpoonList.iterator(); 
		while (it.hasNext()){
			Harpoon myHarpoon = (Harpoon) it.next();
			putfissureAt((int)myHarpoon.getPosition().x,(int)myHarpoon.getPosition().y,myHarpoon.getRange());
		}
	}
	
	/**
     * Fill fissure matrix with fissure types. The method put fissure center in (xHarpoonPosition, yHarpoonPosition)
     * and draw fissures in one field around this center (N,S,W,E). The rest of range will be draw with invisible fissures
     * for use in putWaterAt method.
     * @param xHarpoonPosition
     * @param yHarpoonPosition
     * @param fissureRange
     */
   
    public void putfissureAt(int xHarpoonPosition, int yHarpoonPosition, int fissureRange) {
        //The fissure center is in the same position that the harpoon
        if(fissuresBoard[xHarpoonPosition][yHarpoonPosition]!=FissuresTypes.fissureC)
            fissuresBoard[xHarpoonPosition][yHarpoonPosition]= FissuresTypes.fissureC;
        // The fissure will be draw in 4 directions 1 position, others positions will be draw with invisible fissure
        // so users can't see the real range of harpoon while the field in the board game isn't unbreakable
        // always less or equal than fissureRange, if there are fields with breakable squares this fields
        // will be draw like empty fields
        int i = 1;
        boolean blocked = false;
        //NORTH
        while (!blocked && (yHarpoonPosition+i<length) && i<=fissureRange && canIputFissure(xHarpoonPosition,yHarpoonPosition+i)){
            if(fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] == FissuresTypes.fissureSX){
                if (i == 1) fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] = FissuresTypes.crossingFissures;
               // else fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] = FissuresTypes.crossInvisibleFissure;
            }
            else if (boardGame[xHarpoonPosition][yHarpoonPosition+i] == TypeSquare.breakable){
                if (i == 1) fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] = FissuresTypes.barrelWithFissure;
                else fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] = FissuresTypes.invisibleFissureSY;
                blocked = true;
            }
            else if (fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] != FissuresTypes.fissureSY)
                if (i == 1) fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] = FissuresTypes.fissureSY;
                else fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] = FissuresTypes.invisibleFissureSY;
            
            i++;
        }
        i = 1; blocked = false;
       
        //SOUTH
        while (!blocked && yHarpoonPosition-i>=0 && i<=fissureRange && canIputFissure(xHarpoonPosition,yHarpoonPosition-i)){
            if(fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] == FissuresTypes.fissureSX){
                if (i == 1) fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] = FissuresTypes.crossingFissures;
               // else fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] = FissuresTypes.crossInvisibleFissure;
            }
            else if (boardGame[xHarpoonPosition][yHarpoonPosition-i] == TypeSquare.breakable){
                if (i == 1) fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] = FissuresTypes.barrelWithFissure;
                else fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] = FissuresTypes.invisibleFissureSY;
                blocked = true;
            }
            else if (fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] != FissuresTypes.fissureSY)
                if (i == 1) fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] = FissuresTypes.fissureSY;
                else fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] = FissuresTypes.invisibleFissureSY;
        i++;
        }
        i = 1; blocked = false;
        //EAST
        while (!blocked && (xHarpoonPosition+i<width) && i<=fissureRange && canIputFissure(xHarpoonPosition+i,yHarpoonPosition)){
            if(fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] == FissuresTypes.fissureSY){
                if (i == 1) fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] = FissuresTypes.crossingFissures;
               // else fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] = FissuresTypes.crossInvisibleFissure;
            }
            else if (boardGame[xHarpoonPosition+i][yHarpoonPosition] == TypeSquare.breakable){
                if (i == 1) fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] = FissuresTypes.barrelWithFissure;
                else fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] = FissuresTypes.invisibleFissureSX;
                blocked = true;
            }
            else if(fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] != FissuresTypes.fissureSX)
                if (i == 1) fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] = FissuresTypes.fissureSX;
                else fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] = FissuresTypes.invisibleFissureSX;
            
        i++;
        }
        i = 1; blocked = false;
        //WEST
        while (!blocked && (xHarpoonPosition-i>=0) && i<=fissureRange && canIputFissure(xHarpoonPosition-i,yHarpoonPosition)){
            if(fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] == FissuresTypes.fissureSY){
                if (i == 1) fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] = FissuresTypes.crossingFissures;
               // else fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] = FissuresTypes.crossInvisibleFissure;
            }
            else if (boardGame[xHarpoonPosition-i][yHarpoonPosition] == TypeSquare.breakable){
                if (i == 1) fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] = FissuresTypes.barrelWithFissure;
                else fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] = FissuresTypes.invisibleFissureSX;
                blocked = true;
            }
            else if (fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] != FissuresTypes.fissureSX)
                if (i == 1) fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] = FissuresTypes.fissureSX;
                else fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] = FissuresTypes.invisibleFissureSX;
        i++;
        }
}
	/**
	 * This method check if the position(x,y) has a empty or breakeable field
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean canIputFissure(int x, int y){
		return (boardGame[x][y] != TypeSquare.unbreakable && boardGame[x][y] != TypeSquare.harpoon);
	}
	
	// END METHODS OF PUT FISSURE
	
	
	//METHODS OF PUT WATER
	
	public void putSunkenHarpoonAt(int xHarpoonPosition, int yHarpoonPosition) {
		boardGame[xHarpoonPosition][yHarpoonPosition]= TypeSquare.empty;
	}
	
	public void paintAllWaters(ArrayList<Harpoon> sunkenHarpoonList){
		removeAllWater();
		Iterator<Harpoon> it = sunkenHarpoonList.iterator(); 
		while (it.hasNext()){
			Harpoon mySunkenHarpoon = (Harpoon) it.next();
			putWaterAt((int)mySunkenHarpoon.getPosition().x,(int)mySunkenHarpoon.getPosition().y,mySunkenHarpoon.getRange());
		}
	}	
	
	private void removeAllFissures(){
		for (int i=0;i<length;i++){
			for (int j=0;j<width;j++){
				fissuresBoard[i][j]=FissuresTypes.empty;
			}
		}
	}

	private void removeAllWater() {
		for (int i=0;i<length;i++){
			for (int j=0;j<width;j++){
				waterBoard[i][j]=WaterTypes.empty;
			}
		}
	}
	
	public boolean hasOneFissure(int x, int y){
		return (fissuresBoard[x][y] == FissuresTypes.fissureSY || fissuresBoard[x][y] == FissuresTypes.fissureSX	
				|| fissuresBoard[x][y] == FissuresTypes.invisibleFissureSX	||(fissuresBoard[x][y] == FissuresTypes.barrelWithFissure
				|| fissuresBoard[x][y] == FissuresTypes.invisibleFissureSY	|| fissuresBoard[x][y] == FissuresTypes.crossInvisibleFissure)
				&& !(fissuresBoard[x][y] == FissuresTypes.fissureC));
		
	}
	
	public void putWaterAt(int xHarpoonPosition, int yHarpoonPosition, int fissureRange) {
		String connectingSidesPos;
		// Fissure center <= Water Center
		if(fissuresBoard[xHarpoonPosition][yHarpoonPosition] == FissuresTypes.fissureC){
			connectingSidesPos = ConnectingSidesPosition(xHarpoonPosition, yHarpoonPosition, fissureRange);
			waterBoard[xHarpoonPosition][yHarpoonPosition] = getWaterPiece(connectingSidesPos);
		}
		int i = 1; boolean blocked = false;
		//
		//NORTH
		blocked = false;
		while (!blocked && (yHarpoonPosition+i<length) && i<=fissureRange && hasOneFissure(xHarpoonPosition,yHarpoonPosition+i)){
			if (fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] == FissuresTypes.fissureSY
				||(fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] == FissuresTypes.invisibleFissureSY
				&& boardGame[xHarpoonPosition][yHarpoonPosition+i] == TypeSquare.empty)){
					connectingSidesPos = ConnectingSidesPosition(xHarpoonPosition, yHarpoonPosition+i, fissureRange);
					waterBoard[xHarpoonPosition][yHarpoonPosition+i] = getWaterPiece(connectingSidesPos);
			}else if (fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] == FissuresTypes.barrelWithFissure
				|| (fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] == FissuresTypes.invisibleFissureSY
				&& boardGame[xHarpoonPosition][yHarpoonPosition+i] == TypeSquare.breakable)){

				fissuresBoard[xHarpoonPosition][yHarpoonPosition+i] = FissuresTypes.empty;
				int typeImprovement = upgrades[numBarrels];
				numBarrels++;
				if (typeImprovement==0){
					boardGame[xHarpoonPosition][yHarpoonPosition+i] = TypeSquare.bootUpgrade;
				}else if (typeImprovement==1){
					boardGame[xHarpoonPosition][yHarpoonPosition+i] = TypeSquare.rangeUpgrade;
				}else if (typeImprovement==2){
					boardGame[xHarpoonPosition][yHarpoonPosition+i] = TypeSquare.numHarpoonUpgrade;
				}else if (typeImprovement==3){
					boardGame[xHarpoonPosition][yHarpoonPosition+i] = TypeSquare.invisible;
				}else{
					boardGame[xHarpoonPosition][yHarpoonPosition+i] = TypeSquare.empty;
				}
					blocked = true;
			}
			i++;
		} 
		i = 1;
		
		//SOUTH
		blocked = false;
		while (!blocked && yHarpoonPosition-i>=0 && i<=fissureRange && hasOneFissure(xHarpoonPosition,yHarpoonPosition-i)){
			if (fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] == FissuresTypes.fissureSY
				||(fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] == FissuresTypes.invisibleFissureSY
				&& boardGame[xHarpoonPosition][yHarpoonPosition-i] == TypeSquare.empty)){
					connectingSidesPos = ConnectingSidesPosition(xHarpoonPosition, yHarpoonPosition-i, fissureRange);
					waterBoard[xHarpoonPosition][yHarpoonPosition-i] = getWaterPiece(connectingSidesPos);
			}else if (fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] == FissuresTypes.barrelWithFissure
				|| (fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] == FissuresTypes.invisibleFissureSY
				&& boardGame[xHarpoonPosition][yHarpoonPosition-i] == TypeSquare.breakable)){

					fissuresBoard[xHarpoonPosition][yHarpoonPosition-i] = FissuresTypes.empty;
					int typeImprovement=upgrades[numBarrels];
					numBarrels++;
					if (typeImprovement==0){
						boardGame[xHarpoonPosition][yHarpoonPosition-i] = TypeSquare.bootUpgrade;
					}else if (typeImprovement==1){
						boardGame[xHarpoonPosition][yHarpoonPosition-i] = TypeSquare.rangeUpgrade;
					}else if (typeImprovement==2){
						boardGame[xHarpoonPosition][yHarpoonPosition-i] = TypeSquare.numHarpoonUpgrade;
					}else if (typeImprovement==3){
						boardGame[xHarpoonPosition][yHarpoonPosition-i] = TypeSquare.invisible;
					}else{
						boardGame[xHarpoonPosition][yHarpoonPosition-i] = TypeSquare.empty;
					}
					blocked = true;
			}
			i++;
		}
			i = 1;
		
		//EAST
		blocked = false;
		while (!blocked && (xHarpoonPosition+i<width) && i<=fissureRange && hasOneFissure(xHarpoonPosition+i, yHarpoonPosition)){
			if (fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] == FissuresTypes.fissureSX
				||(fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] == FissuresTypes.invisibleFissureSX 
				&& boardGame[xHarpoonPosition+i][yHarpoonPosition] == TypeSquare.empty)){
					connectingSidesPos = ConnectingSidesPosition(xHarpoonPosition+i, yHarpoonPosition, fissureRange);
					waterBoard[xHarpoonPosition+i][yHarpoonPosition] = getWaterPiece(connectingSidesPos);
			}else if (fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] == FissuresTypes.barrelWithFissure
				|| (fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] == FissuresTypes.invisibleFissureSX
				&& boardGame[xHarpoonPosition+i][yHarpoonPosition] == TypeSquare.breakable)){

				fissuresBoard[xHarpoonPosition+i][yHarpoonPosition] = FissuresTypes.empty;
				int typeImprovement=upgrades[numBarrels];
				numBarrels++;
				if (typeImprovement==0){
					boardGame[xHarpoonPosition+i][yHarpoonPosition] = TypeSquare.bootUpgrade;
				}else if (typeImprovement==1){
					boardGame[xHarpoonPosition+i][yHarpoonPosition] = TypeSquare.rangeUpgrade;
				}else if (typeImprovement==2){
					boardGame[xHarpoonPosition+i][yHarpoonPosition] = TypeSquare.numHarpoonUpgrade;
				}else if (typeImprovement==3){
					boardGame[xHarpoonPosition+i][yHarpoonPosition] = TypeSquare.invisible;
				}else{
					boardGame[xHarpoonPosition+i][yHarpoonPosition] = TypeSquare.empty;
				}
				blocked = true;
			}
			i++;
		}
		i = 1;
		//WEST
		blocked = false;
		while (!blocked && (xHarpoonPosition-i>=0) && i<=fissureRange && hasOneFissure(xHarpoonPosition-i, yHarpoonPosition)){
			if (fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] == FissuresTypes.fissureSX
				||(fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] == FissuresTypes.invisibleFissureSX 
				&& boardGame[xHarpoonPosition-i][yHarpoonPosition] == TypeSquare.empty)){
					connectingSidesPos = ConnectingSidesPosition(xHarpoonPosition-i, yHarpoonPosition, fissureRange);
					waterBoard[xHarpoonPosition-i][yHarpoonPosition] = getWaterPiece(connectingSidesPos);
			}else if(fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] == FissuresTypes.barrelWithFissure
				|| (fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] == FissuresTypes.invisibleFissureSX 
				&& boardGame[xHarpoonPosition-i][yHarpoonPosition] == TypeSquare.breakable)){
					fissuresBoard[xHarpoonPosition-i][yHarpoonPosition] = FissuresTypes.empty;
					int typeImprovement=upgrades[numBarrels];
					numBarrels++;
					if (typeImprovement==0){
						boardGame[xHarpoonPosition-i][yHarpoonPosition] = TypeSquare.bootUpgrade;
					}else if (typeImprovement==1){
						boardGame[xHarpoonPosition-i][yHarpoonPosition] = TypeSquare.rangeUpgrade;
					}else if (typeImprovement==2){
						boardGame[xHarpoonPosition-i][yHarpoonPosition] = TypeSquare.numHarpoonUpgrade;
					}else if (typeImprovement==3){
						boardGame[xHarpoonPosition-i][yHarpoonPosition] = TypeSquare.invisible;
					}else{
						boardGame[xHarpoonPosition-i][yHarpoonPosition] = TypeSquare.empty;
					}
					blocked = true;
			}
		i++;
		}	
	}
	
	
	
	private WaterTypes getWaterPiece (String connectingSidesPos){	
			if (connectingSidesPos.equals("N")){return WaterTypes.water1SOpN;}
			else if (connectingSidesPos.equals("S")){return WaterTypes.water1SOpS;}
			else if (connectingSidesPos.equals("E")){return WaterTypes.water1SOpE;}
			else if (connectingSidesPos.equals("W")){return WaterTypes.water1SOpW;}
			else if (connectingSidesPos.equals("EW")){return WaterTypes.water2SOpBridgeX;}
			else if (connectingSidesPos.equals("NS")){return WaterTypes.water2SOpBridgeY;}
			else if (connectingSidesPos.equals("NE")){return WaterTypes.water2SOpCornerEN;}
			else if (connectingSidesPos.equals("NW")){return WaterTypes. water2SOpCornerNW;}
			else if (connectingSidesPos.equals("SE")){return WaterTypes.water2SOpCornerSE;}
			else if (connectingSidesPos.equals("SW")){return WaterTypes.water2SOpCornerWS;}
			else if (connectingSidesPos.equals("NSW")){return WaterTypes.water3SOpE;}
			else if (connectingSidesPos.equals("SEW")){return WaterTypes.water3SOpN;}
			else if (connectingSidesPos.equals("NEW")){return WaterTypes.water3SOpS;}
			else if (connectingSidesPos.equals("NSE")){return WaterTypes.water3SOpW;}
			else if (connectingSidesPos.equals("NSEW")){return WaterTypes.water4SOp;}		
	return WaterTypes.empty;
	}


	private boolean canIputWater(int xIni,int yIni,int xFin,int yFin){	
		//The new field is in the East. we have to check that this field is a fissure or is a water block with a connection in the west side
		if(xIni < xFin){
			return(fissuresBoard[xFin][yFin] == FissuresTypes.fissureSX || fissuresBoard[xFin][yFin] == FissuresTypes.invisibleFissureSX
					|| fissuresBoard[xFin][yFin] == FissuresTypes.fissureC || fissuresBoard[xFin][yFin] == FissuresTypes.crossInvisibleFissure
					|| waterBoard[xFin][yFin] == WaterTypes.water1SOpW || waterBoard[xFin][yFin] == WaterTypes.water2SOpCornerNW 
					|| waterBoard[xFin][yFin] == WaterTypes.water2SOpCornerWS || waterBoard[xFin][yFin] == WaterTypes.water2SOpBridgeX 
					|| waterBoard[xFin][yFin] == WaterTypes.water3SOpE || waterBoard[xFin][yFin] == WaterTypes.water3SOpN 
					|| waterBoard[xFin][yFin] == WaterTypes.water3SOpS || waterBoard[xFin][yFin] == WaterTypes.water4SOp);
		
		//The new field is in the West. we have to check that this field is a fissure or is a water block with a connection in the east side
		}else if(xIni > xFin){
			return(fissuresBoard[xFin][yFin] == FissuresTypes.fissureSX  || fissuresBoard[xFin][yFin] == FissuresTypes.invisibleFissureSX
					|| fissuresBoard[xFin][yFin] == FissuresTypes.fissureC || fissuresBoard[xFin][yFin] == FissuresTypes.crossInvisibleFissure
					|| waterBoard[xFin][yFin] == WaterTypes.water1SOpE || waterBoard[xFin][yFin] == WaterTypes.water2SOpCornerEN 
					|| waterBoard[xFin][yFin] == WaterTypes.water2SOpCornerSE || waterBoard[xFin][yFin] == WaterTypes.water2SOpBridgeX 
					|| waterBoard[xFin][yFin] == WaterTypes.water3SOpW || waterBoard[xFin][yFin] == WaterTypes.water3SOpN 
					|| waterBoard[xFin][yFin] == WaterTypes.water3SOpS || waterBoard[xFin][yFin] == WaterTypes.water4SOp);
				
		//The new field is in the South. we have to check that this field is a fissure or is a water block with a connection in the north side
		}else if(yIni > yFin){
			return(fissuresBoard[xFin][yFin] == FissuresTypes.fissureSY || fissuresBoard[xFin][yFin] == FissuresTypes.invisibleFissureSY
					|| fissuresBoard[xFin][yFin] == FissuresTypes.fissureC || fissuresBoard[xFin][yFin] == FissuresTypes.crossInvisibleFissure
					|| waterBoard[xFin][yFin] == WaterTypes.water1SOpN || waterBoard[xFin][yFin] == WaterTypes.water2SOpCornerNW
					|| waterBoard[xFin][yFin] == WaterTypes.water2SOpCornerEN || waterBoard[xFin][yFin] == WaterTypes.water2SOpBridgeY 
					|| waterBoard[xFin][yFin] == WaterTypes.water3SOpE || waterBoard[xFin][yFin] == WaterTypes.water3SOpW 
					|| waterBoard[xFin][yFin] == WaterTypes.water3SOpS || waterBoard[xFin][yFin] == WaterTypes.water4SOp);
	
		//The new field is in the North. we have to check that this field is a fissure or is a water block with a connection in the south side
		}else if(yIni < yFin){
			return(fissuresBoard[xFin][yFin] == FissuresTypes.fissureSY  || fissuresBoard[xFin][yFin] == FissuresTypes.invisibleFissureSY
					|| fissuresBoard[xFin][yFin] == FissuresTypes.fissureC || fissuresBoard[xFin][yFin] == FissuresTypes.crossInvisibleFissure
					|| waterBoard[xFin][yFin] == WaterTypes.water1SOpS || waterBoard[xFin][yFin] == WaterTypes.water2SOpCornerSE 
					|| waterBoard[xFin][yFin] == WaterTypes.water2SOpCornerWS || waterBoard[xFin][yFin] == WaterTypes.water2SOpBridgeY 
					|| waterBoard[xFin][yFin] == WaterTypes.water3SOpE || waterBoard[xFin][yFin] == WaterTypes.water3SOpW 
					|| waterBoard[xFin][yFin] == WaterTypes.water3SOpN || waterBoard[xFin][yFin] == WaterTypes.water4SOp);
		}		
	return true;
	}

	public String ConnectingSidesPosition(int xHarpoonPosition, int yHarpoonPosition, int fissureRange) {
		String positions = "";
		//NORTH
		if(yHarpoonPosition+1<length && canIputWater(xHarpoonPosition, yHarpoonPosition, xHarpoonPosition, yHarpoonPosition+1)) 
			positions = positions.concat("N");
		//SOUTH
		if(yHarpoonPosition-1>=0 && canIputWater(xHarpoonPosition, yHarpoonPosition, xHarpoonPosition, yHarpoonPosition-1)) 
			positions = positions.concat("S");
		//EAST
		if(xHarpoonPosition+1 < width && canIputWater(xHarpoonPosition, yHarpoonPosition, xHarpoonPosition+1, yHarpoonPosition))
			positions = positions.concat("E");
		//WEST
		if(xHarpoonPosition-1>=0 && canIputWater(xHarpoonPosition, yHarpoonPosition, xHarpoonPosition-1, yHarpoonPosition))
			positions = positions.concat("W");
	return positions;
	}
//END OF METHODS PUT WATER

	
//METHOD DELETE SUNKEN OBJECT
	/**
	 * This method delete sunkenObjects image in sunkenBoard
	 * along harpoon range only if waterBoard isn't empty.
	 * @param harpoon
	 */
		public void emptyHarpoonPosInSunkenMatrix(Harpoon harpoon) {
		//Parameters declaration for simplify the code
			//Harpoon Range
			int range = harpoon.getRange();
			//West Delimiter: 
				//If the harpoon center - range is less than bound of board game
				//the X initial position range in board will be 0
			int xIni = (int)harpoon.getPosition().x-range;
			if (xIni < 0) xIni = 0;
			//North Delimiter: 
				//If the harpoon center - range is less than bound of board game
				//the Y initial position range in board will be 0
			int yIni = (int)harpoon.getPosition().y-range;
			if (yIni < 0) yIni = 0;
			//East Delimiter: 
				//If the harpoon center + range is great than bound of board game
				//the X initial position range in board will be width
			int xFin = (int)harpoon.getPosition().x+range;
			if (xFin >= width) xFin = width-1;
			//South Delimiter: 
				//If the harpoon center + range is great than bound of board game
				//the Y initial position range in board will be length
			int yFin = (int)harpoon.getPosition().y+range;
			if (yFin >= length) yFin = length-1;
			
			//Range harpoon spanning when I delete sunkenObject image of sunkenBoard
			//only if there are water in waterBoard in the same position
			for(int i=xIni; i<=xFin; i++)
				for(int j=yIni; j<=yFin; j++)
					//if(waterBoard[i][j] != WaterTypes.empty && sunkenBoard[i][j] == SunkenTypes.sunkenObject)
						sunkenBoard[i][j] = SunkenTypes.empty;
		}
	
//END OF METHOD DELETE SUNKEN OBJECT

	public void sunkenObject(int x, int y){
		sunkenBoard[x][y]= SunkenTypes.sunkenObject;
	}	
	
	public boolean canRunThrough(int x, int y) {
		boolean canRunThrough = false;
		if (x>=0 && x<width && y >= 0 && y<length){
			canRunThrough = !(boardGame[x][y].equals(TypeSquare.unbreakable) || 
					boardGame[x][y].equals(TypeSquare.breakable));
		}
		return canRunThrough;
	}
	
// Getters and Setters
	
	public TypeSquare getBasicMatrixSquare(int x,int y){
		try{
			return boardGame[x][y];
		}
		catch (Exception e){
			return TypeSquare.empty;
		}
	}
	
	public void setEmptyBasicMatrixSquare(int x, int y) {
		boardGame[x][y] = TypeSquare.empty;
	}
	
	public void setBasicMatrixSquare(int x, int y,TypeSquare type) {
		if (x < width && y < length && x >= 0 && y >=0) boardGame[x][y] = type;
	}
	
	public FissuresTypes getFissureMatrixSquare(int x,int y) {
		return fissuresBoard[x][y];
	}
	
	public WaterTypes getWaterMatrixSquare(int x,int y) {
		return waterBoard[x][y];
	}
	

	public SunkenTypes getSunkenMatrixSquare(int x, int y) {
		return sunkenBoard[x][y];
	}
	
	public int getMaxBootUpgrades() {
		return maxBootUpgrades;
	}
	public void setMaxBootUpgrades(int maxBootUpgrades) {
		this.maxBootUpgrades = maxBootUpgrades;
	}
	public int getMaxRangeUpgrades() {
		return maxRangeUpgrades;
	}
	public void setMaxRangeUpgrades(int maxRangeUpgrade) {
		this.maxRangeUpgrades = maxRangeUpgrade;
	}
	public int getMaxNumHarpoonsUpgrades() {
		return maxNumHarpoonsUpgrades;
	}
	public void setMaxNumHarpoonsUpgrades(int maxNumHarpoonsgrades) {
		this.maxNumHarpoonsUpgrades = maxNumHarpoonsgrades;
	}
	public int getMaxThrowUpgrades() {
		return maxThrowUpgrades;
	}
	public void setMaxThrowUpgrades(int maxThrowUpgrades) {
		this.maxThrowUpgrades = maxThrowUpgrades;
	}
	public int getLength() {
		return length;
	}
	public int getWidth() {
		return width;
	}
	public boolean isEmptySquare(int x, int y) {
		return (boardGame[x][y]==TypeSquare.empty);
	}		
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
		
	public void setPositionUpgrades(int i,int a) {
	    this.upgrades[i] = a;
	}
		
	public int getPositionUpgrades(int i) {
	    return this.upgrades[i];
	}
		  
	public int[] getUpgrades() {
	   return upgrades;
	}
	
	public void setEmpty(int x,int y) {
		boardGame[x][y]=TypeSquare.empty;
	}

	public boolean existUpgrade(Vector3 pos) {
		return boardGame[(int) pos.x][(int) pos.y]==TypeSquare.bootUpgrade||boardGame[(int) pos.x][(int) pos.y]==TypeSquare.rangeUpgrade||boardGame[(int) pos.x][(int) pos.y]==TypeSquare.numHarpoonUpgrade||boardGame[(int) pos.x][(int) pos.y]==TypeSquare.invisible;
	}
}
