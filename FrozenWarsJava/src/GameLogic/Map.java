package GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;

public class Map {
	
	//Type of a square in the boardGame
	
	public enum TypeSquare{empty,unbreakable,breakable,bootUpgrade,rangeUpgrade,numHarpoonUpgrade,
		throwUpgrade,Harpoon,barrelWithFissure, fissureC, fissureSX, fissureSY, water1SOpN, water1SOpS, water1SOpE, 
		water1SOpW, water2SOpCornerEN, water2SOpCornerNW, water2SOpCornerSE, water2SOpCornerWS,
		water2SOpBridgeX, water2SOpBridgeY, water3SOpN, water3SOpS, water3SOpE, water3SOpW, water4SOp}
	
	//Total number of each Upgrade
	
	private int maxBootUpgrades;
	private int maxRangeUpgrades;
	private int maxNumLancesUpgrades;
	private int maxThrowUpgrades;
	
	//Board Attributes
	
	private String mapName;
	private int length;
	private int width;
	private TypeSquare[][] boardGame;
	
	//LoadFile
	
	private String xmlMap;
	
	public Map(int lenght, int width, String mapPath) {
		this.length=lenght;
		this.width=width;
		this.boardGame = new TypeSquare[lenght][width];
		for (int i=0;i<width;i++){
			for (int j=0;j<length;j++){
				if ((i % 2 == 1) && (j % 2 == 1)) boardGame[i][j] = TypeSquare.unbreakable;
				else boardGame[i][j] = TypeSquare.empty;
			}
		}
		loadMap(mapPath);
	}

	private String getData(String xml, String data){
		int begin;
		int end;
		
		begin = xml.indexOf("<".concat(data).concat(">")) + data.length() + 2;
		end = xml.indexOf("</".concat(data).concat(">"));
		
		return xml.substring(begin, end);
	}
	
	private void loadMap(String mapPath) {
		loadXML(mapPath);
		if (xmlMap.equals("")){
			maxBootUpgrades = 8;
			maxRangeUpgrades = 8;
			maxNumLancesUpgrades = 8;
			maxThrowUpgrades = 8;
		} else {
			xmlMap = getData(xmlMap, "Map");
			loadName();
			loadUpgrades();
			loadBoxes();
		}	
	}

	private void loadXML(String mapPath) {
		/*xmlMap = "<Map> <Name>mapaGuay</Name> <Upgrades> <Boots>5</Boots> <MaxRange>4</MaxRange> <NumLances>6</NumLances> <Throw>4</Throw> </Upgrades>" +
				" <Boxes> <Breakable>4-5</Breakable> <Breakable>1-4</Breakable> <Breakable>2-4</Breakable> <Breakable>5-6</Breakable> " +
				"<Breakable>4-6</Breakable> <Breakable>8-5</Breakable> </Boxes> </Map>";*/
		try {
			FileHandle handle = Gdx.files.internal("data/".concat(mapPath)); //Gdx.files.getFileHandle("data/".concat("mapaPrueba.xml"), FileType.External);
			xmlMap = handle.readString();
		} catch (Exception e) {
			System.out.println("Error de carga de fichero");
		}
	}	
	
	private void loadName(){
		mapName = getData(xmlMap, "Name");
	}
	
	private void loadUpgrades(){
		String xmlUpgrades = getData(xmlMap, "Upgrades");
		
		maxBootUpgrades = Integer.parseInt(getData(xmlUpgrades, "Boots"));
		maxRangeUpgrades = Integer.parseInt(getData(xmlUpgrades, "MaxRange"));
		maxNumLancesUpgrades = Integer.parseInt(getData(xmlUpgrades, "NumLances"));
		maxThrowUpgrades = Integer.parseInt(getData(xmlUpgrades, "Throw"));			
	}
	
	private void loadBoxes(){
		String xmlBoxes = getData(xmlMap, "Boxes");
		String box;
		
		while (xmlBoxes.contains("Breakable")){
			box = getData(xmlBoxes, "Breakable");
			loadBox(box, TypeSquare.breakable);
			xmlBoxes = xmlBoxes.replaceFirst("<Breakable>".concat(box).concat("</Breakable>"), "");
		}
		
		while (xmlBoxes.contains("Unbreakable")){
			box = getData(xmlBoxes, "Unbreakable");
			loadBox(box, TypeSquare.unbreakable);
			xmlBoxes = xmlBoxes.replaceFirst("<Unbreakable>".concat(box).concat("</Unbreakable>"), "");
		}
		
		while (xmlBoxes.contains("Empty")){
			box = getData(xmlBoxes, "Empty");
			loadBox(box, TypeSquare.empty);
			xmlBoxes = xmlBoxes.replaceFirst("<Empty>".concat(box).concat("</Empty>"), "");
		}
	}
	
	private void loadBox(String box, TypeSquare type){
		int dash = box.indexOf("-");
		int x = Integer.parseInt(box.substring(0, dash));
		int y = Integer.parseInt(box.substring(dash+1, box.length()));
		if (x < width && y < length && x >= 0 && y >=0) boardGame[x][y] = type;
	}
	
	public TypeSquare getposition(int squareX,int squareY) {
		return boardGame[squareX][squareY];
	}
	
	public void putLanceAt(int xLancePosition, int yLancePosition) {
		boardGame[xLancePosition][yLancePosition]= TypeSquare.Harpoon;
	}

	public void putfissureAt(int xLancePosition, int yLancePosition, int fissureRange) {
		//The fissure center is in the same position that the harpoon
		
		boardGame[xLancePosition][yLancePosition]= TypeSquare.fissureC;
		
		//The fissure will be draw in 4 directions while the field in the board game isn't unbreakable
		// always less or equal than fissureRange, if there are fields with breakable squares this fields 
		// will be draw like empty fields
		int i = 1;
		boolean blocked = false;
		//NORTH
		while (!blocked && (yLancePosition+i<length) && i<=fissureRange && canIputFissure(xLancePosition,yLancePosition+i)){
			if (boardGame[xLancePosition][yLancePosition+i] == TypeSquare.empty)
				boardGame[xLancePosition][yLancePosition+i] = TypeSquare.fissureSY;
			else if (boardGame[xLancePosition][yLancePosition+i] == TypeSquare.breakable){
				boardGame[xLancePosition][yLancePosition+i] = TypeSquare.barrelWithFissure;
				blocked = true;
			}
			i++;
		}
		i = 1; blocked = false;
		
		//SOUTH
		while (!blocked && yLancePosition-i>=0 && i<=fissureRange && canIputFissure(xLancePosition,yLancePosition-i)){
			if (boardGame[xLancePosition][yLancePosition-i] == TypeSquare.empty){
				boardGame[xLancePosition][yLancePosition-i] = TypeSquare.fissureSY;
			}
			else if (boardGame[xLancePosition][yLancePosition-i] == TypeSquare.breakable){
				boardGame[xLancePosition][yLancePosition-i] = TypeSquare.barrelWithFissure;
				blocked = true;
			}
		i++;
		}
		i = 1; blocked = false;
		//EAST
		while (!blocked && (xLancePosition+i<width) && i<=fissureRange && canIputFissure(xLancePosition+i,yLancePosition)){
					if (boardGame[xLancePosition+i][yLancePosition] == TypeSquare.empty)
						boardGame[xLancePosition+i][yLancePosition] = TypeSquare.fissureSX;
					else if (boardGame[xLancePosition+i][yLancePosition] == TypeSquare.breakable){
						boardGame[xLancePosition+i][yLancePosition] = TypeSquare.barrelWithFissure;
						blocked = true;
					}
		i++;
		}
		i = 1; blocked = false;
		//WEST
		while (!blocked && (xLancePosition-i>=0) && i<=fissureRange && canIputFissure(xLancePosition-i,yLancePosition)){
					if (boardGame[xLancePosition-i][yLancePosition] == TypeSquare.empty)
						boardGame[xLancePosition-i][yLancePosition] = TypeSquare.fissureSX;
					else if (boardGame[xLancePosition-i][yLancePosition] == TypeSquare.breakable){
						boardGame[xLancePosition-i][yLancePosition] = TypeSquare.barrelWithFissure;
						blocked = true;
					}
		i++;
		}

}
	
	/*FIXME* Water isn't drawing correctly. Maybe the problem is in the harpoon position */
	public void putWaterAt(int xLancePosition, int yLancePosition, int fissureRange) {
		
	// Fissure center <= Water Center
	String connectingSidesPos = ConnectingSidesPosition(xLancePosition, yLancePosition, fissureRange);
	boardGame[xLancePosition][yLancePosition] = getWaterPiece(connectingSidesPos);
		
	int i = 1; boolean blocked = false;
	//
	//NORTH
	blocked = false;
	while (!blocked && (yLancePosition+i<length) && i<=fissureRange && ((boardGame[xLancePosition][yLancePosition+i] == TypeSquare.fissureSY)
			||(boardGame[xLancePosition][yLancePosition+i] == TypeSquare.barrelWithFissure))){
		if (boardGame[xLancePosition][yLancePosition+i] == TypeSquare.fissureSY){
			connectingSidesPos = ConnectingSidesPosition(xLancePosition, yLancePosition+i, fissureRange);
			boardGame[xLancePosition][yLancePosition+i] = getWaterPiece(connectingSidesPos);
		}else if (boardGame[xLancePosition][yLancePosition+i] == TypeSquare.barrelWithFissure){
			// Upgrades not included yet. Barrel will be empty.
			boardGame[xLancePosition][yLancePosition+i] = TypeSquare.empty;
			blocked = true;
		}
	i++;
	} 
	i = 1;
	
	//SOUTH
	while (!blocked && yLancePosition-i>=0 && i<=fissureRange && ((boardGame[xLancePosition][yLancePosition-i] == TypeSquare.fissureSY)
			||(boardGame[xLancePosition][yLancePosition-i] == TypeSquare.barrelWithFissure))){
			if (boardGame[xLancePosition][yLancePosition-i] == TypeSquare.fissureSY){
				connectingSidesPos = ConnectingSidesPosition(xLancePosition, yLancePosition-i, fissureRange);
				boardGame[xLancePosition][yLancePosition-i] = getWaterPiece(connectingSidesPos);
			}else if (boardGame[xLancePosition][yLancePosition-i] == TypeSquare.barrelWithFissure){
				// Upgrades not included yet. Barrel will be empty.
				boardGame[xLancePosition][yLancePosition-i] = TypeSquare.empty;
				blocked = true;
			}
		i++;
		}
		i = 1;
	
	//EAST
	blocked = false;
	while (!blocked && (xLancePosition+i<width) && i<=fissureRange	&& ((boardGame[xLancePosition+i][yLancePosition] == TypeSquare.fissureSX)
			||(boardGame[xLancePosition+i][yLancePosition] == TypeSquare.barrelWithFissure))){
		if (boardGame[xLancePosition+i][yLancePosition] == TypeSquare.fissureSX){
			connectingSidesPos = ConnectingSidesPosition(xLancePosition+i, yLancePosition, fissureRange);
			boardGame[xLancePosition+i][yLancePosition] = getWaterPiece(connectingSidesPos);
		}else if (boardGame[xLancePosition+i][yLancePosition] == TypeSquare.barrelWithFissure){
			// Upgrades not included yet. Barrel will be empty.
			boardGame[xLancePosition+i][yLancePosition] = TypeSquare.empty;
			blocked = true;
		}
	i++;
	}
	i = 1;
	//WEST
	blocked = false;
	while (!blocked && (xLancePosition-i>=0) && i<=fissureRange	&& ((boardGame[xLancePosition-i][yLancePosition] == TypeSquare.fissureSX)
			||(boardGame[xLancePosition-i][yLancePosition] == TypeSquare.barrelWithFissure))){
		if (boardGame[xLancePosition-i][yLancePosition] == TypeSquare.fissureSX){
			connectingSidesPos = ConnectingSidesPosition(xLancePosition-i, yLancePosition, fissureRange);
			boardGame[xLancePosition-i][yLancePosition] = getWaterPiece(connectingSidesPos);
		}else if (boardGame[xLancePosition-i][yLancePosition] == TypeSquare.barrelWithFissure){
			// Upgrades not included yet. Barrel will be empty.
			boardGame[xLancePosition-i][yLancePosition] = TypeSquare.empty;
			blocked = true;
		}
	i++;

	}
	
}

private TypeSquare getWaterPiece (String connectingSidesPos){	
		if (connectingSidesPos.equals("N")){return TypeSquare.water1SOpN;}
		else if (connectingSidesPos.equals("S")){return TypeSquare.water1SOpS;}
		else if (connectingSidesPos.equals("E")){return TypeSquare.water1SOpE;}
		else if (connectingSidesPos.equals("W")){return TypeSquare.water1SOpW;}
		else if (connectingSidesPos.equals("EW")){return TypeSquare.water2SOpBridgeX;}
		else if (connectingSidesPos.equals("NS")){return TypeSquare.water2SOpBridgeY;}
		else if (connectingSidesPos.equals("NE")){return TypeSquare.water2SOpCornerEN;}
		else if (connectingSidesPos.equals("NW")){return TypeSquare. water2SOpCornerNW;}
		else if (connectingSidesPos.equals("SE")){return TypeSquare.water2SOpCornerSE;}
		else if (connectingSidesPos.equals("SW")){return TypeSquare.water2SOpCornerWS;}
		else if (connectingSidesPos.equals("NSW")){return TypeSquare.water3SOpE;}
		else if (connectingSidesPos.equals("SEW")){return TypeSquare.water3SOpN;}
		else if (connectingSidesPos.equals("NEW")){return TypeSquare.water3SOpS;}
		else if (connectingSidesPos.equals("NSE")){return TypeSquare.water3SOpW;}
		else if (connectingSidesPos.equals("NSEW")){return TypeSquare.water4SOp;}		
return TypeSquare.empty;
}


private boolean canIputWater(int xIni,int yIni,int xFin,int yFin){	
	//The new field is in the East. we have to check that this field is a fissure or is a water block with a connection in the west side
	if(xIni < xFin){
		return(boardGame[xFin][yFin] == TypeSquare.fissureSX || boardGame[xFin][yFin] == TypeSquare.water1SOpW 
				|| boardGame[xFin][yFin] == TypeSquare.water2SOpCornerNW || boardGame[xFin][yFin] == TypeSquare.water2SOpCornerWS
				|| boardGame[xFin][yFin] == TypeSquare.water3SOpE || boardGame[xFin][yFin] == TypeSquare.water3SOpN
				|| boardGame[xFin][yFin] == TypeSquare.water3SOpS || boardGame[xFin][yFin] == TypeSquare.water4SOp);
	
	//The new field is in the West. we have to check that this field is a fissure or is a water block with a connection in the east side
	}else if(xIni > xFin){
		return(boardGame[xFin][yFin] == TypeSquare.fissureSX || boardGame[xFin][yFin] == TypeSquare.water1SOpE 
				|| boardGame[xFin][yFin] == TypeSquare.water2SOpCornerEN || boardGame[xFin][yFin] == TypeSquare.water2SOpCornerSE
				|| boardGame[xFin][yFin] == TypeSquare.water3SOpW || boardGame[xFin][yFin] == TypeSquare.water3SOpN
				|| boardGame[xFin][yFin] == TypeSquare.water3SOpS || boardGame[xFin][yFin] == TypeSquare.water4SOp);
			
	//The new field is in the South. we have to check that this field is a fissure or is a water block with a connection in the north side
	}else if(yIni > yFin){
		return(boardGame[xFin][yFin] == TypeSquare.fissureSY || boardGame[xFin][yFin] == TypeSquare.water1SOpN
				|| boardGame[xFin][yFin] == TypeSquare.water2SOpCornerNW || boardGame[xFin][yFin] == TypeSquare.water2SOpCornerEN
				|| boardGame[xFin][yFin] == TypeSquare.water3SOpE || boardGame[xFin][yFin] == TypeSquare.water3SOpW
				|| boardGame[xFin][yFin] == TypeSquare.water3SOpS || boardGame[xFin][yFin] == TypeSquare.water4SOp);

	//The new field is in the North. we have to check that this field is a fissure or is a water block with a connection in the south side
	}else if(yIni < yFin){
		return(boardGame[xFin][yFin] == TypeSquare.fissureSY || boardGame[xFin][yFin] == TypeSquare.water1SOpS
				|| boardGame[xFin][yFin] == TypeSquare.water2SOpCornerSE || boardGame[xFin][yFin] == TypeSquare.water2SOpCornerWS
				|| boardGame[xFin][yFin] == TypeSquare.water3SOpE || boardGame[xFin][yFin] == TypeSquare.water3SOpW
				|| boardGame[xFin][yFin] == TypeSquare.water3SOpN || boardGame[xFin][yFin] == TypeSquare.water4SOp);
	}		
return true;
}

private boolean canIputFissure(int xLancePosition,	int yLancePosition){
	return (boardGame[xLancePosition][yLancePosition] == TypeSquare.empty 
			|| boardGame[xLancePosition][yLancePosition] == TypeSquare.breakable);
}

private String ConnectingSidesPosition(int xLancePosition,	int yLancePosition, int fissureRange) {
	String posiciones = "";
	//NORTH
	if(yLancePosition+1<length && canIputWater(xLancePosition, yLancePosition, xLancePosition, yLancePosition+1)) 
		posiciones = posiciones.concat("N");
	//SOUTH
	if(yLancePosition-1>=0 && canIputWater(xLancePosition, yLancePosition, xLancePosition, yLancePosition-1)) 
		posiciones = posiciones.concat("S");
	//EAST
	if(xLancePosition+1>=0 && canIputWater(xLancePosition, yLancePosition, xLancePosition+1, yLancePosition))
		posiciones = posiciones.concat("E");
	//WEST
	if(xLancePosition-1<width && canIputWater(xLancePosition, yLancePosition, xLancePosition-1, yLancePosition))
		posiciones = posiciones.concat("W");
	
return posiciones;
}

	

	// Getters and Setters
		
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
	public int getMaxNumLancesUpgrades() {
		return maxNumLancesUpgrades;
	}
	public void setMaxNumLancesUpgrades(int maxNumLancesgrades) {
		this.maxNumLancesUpgrades = maxNumLancesgrades;
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
	public void setLength(int length) {
		this.length = length;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public TypeSquare[][] getBoardGame() {
		return boardGame;
	}
	public void setBoardGame(TypeSquare[][] boardGame) {
		this.boardGame = boardGame;
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

}
