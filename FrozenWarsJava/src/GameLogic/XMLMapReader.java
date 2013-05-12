package GameLogic;

import GameLogic.Map.TypeSquare;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class XMLMapReader {
	
	/**
	 * The current number of upgrades that the game have.
	 */
	
	private static int numUpgrades=4;
	
	/**
	 * Saves all the content of the XML in a string which it will
	 * be used to read and extract information. 
	 */
	private String xmlMap;
	
	/**
	 * The name of the map given on the XML.
	 */
	private String mapName;
	
	/**
	 * <p> Loads the xml map of the path given as an argument and its name. </p>
	 * @param mapPath The absolute path of the map that wants to be loaded.
	 */
	
	public XMLMapReader(String mapPath){
		loadXML(mapPath);
		loadName();
	}
	
	/**
	 * <p> Try to open and load the xml map of the given path.</p> 
	 * @param mapPath The absolute path of the map that wants to be loaded.
	 */
	
	private void loadXML(String mapPath) {
		try {
			FileHandle handle = Gdx.files.internal("data/".concat(mapPath));
			xmlMap = handle.readString();
		} catch (Exception e) {
			System.out.println("Error de carga de fichero");
		}
	}
	
	/**
	 * <p> Reads the content to find all the upgrades that are on the map.</p>
	 * @return An array with the following structure:
	 * 				<p>array[0] - The number of the boots upgrades that are on the map.</p>
	 * 				<p>array[1] - The number of the range upgrades that are on the map.</p>
	 * 				<p>array[2] - The number of the lance upgrades that are on the map.</p>
	 * 				<p>array[3] - The number of the invisible upgrades that are on the map.</p>
	 */
	
	public int[] getUpgrades(){	
		String xmlUpgrades = getData(xmlMap, "Upgrades");
		int[] upgrades = new int[numUpgrades];
		upgrades[0] = Integer.parseInt(getData(xmlUpgrades,"Boots"));
		upgrades[1] = Integer.parseInt(getData(xmlUpgrades,"MaxRange"));
		upgrades[2] = Integer.parseInt(getData(xmlUpgrades,"NumLances"));
		upgrades[3] = Integer.parseInt(getData(xmlUpgrades,"Invisible"));		
		return upgrades;
	}
	
	/**
	 * <p>Reads the content to get the number of breakables</p> 
	 * @return Returns the number of the breakables elements or barrels in the map.
	 */
	
	public int getNumBreakable(){
		String xmlBoxes = getData(xmlMap,"Boxes");
		int numBreakable = 0;
		while (xmlBoxes.contains("Breakable")){
			String box = getData(xmlBoxes, "Breakable");
			xmlBoxes = xmlBoxes.replaceFirst("<Breakable>".concat(box).concat("</Breakable>"), "");
			numBreakable++;
		}	
		return numBreakable;
	}
	
	/**
	 * <p>Reads the XML map and fill the information of the map given on the argumments. 
	 * It loads the positions of all breakables and unbrekeables.</p>
	 * @param map The map that wants to be loaded.
	 */
	
	public void loadMap(Map map){
		
		String xmlBoxes = getData(xmlMap, "Boxes");
		String box;
			
		while (xmlBoxes.contains("Breakable")){
			box = getData(xmlBoxes, "Breakable");
			loadBox(box, TypeSquare.breakable,map);
			xmlBoxes = xmlBoxes.replaceFirst("<Breakable>".concat(box).concat("</Breakable>"), "");
		}	
		while (xmlBoxes.contains("Unbreakable")){
			box = getData(xmlBoxes, "Unbreakable");
			loadBox(box, TypeSquare.unbreakable,map);
			xmlBoxes = xmlBoxes.replaceFirst("<Unbreakable>".concat(box).concat("</Unbreakable>"), "");
		}
		while (xmlBoxes.contains("Empty")){
			box = getData(xmlBoxes, "Empty");
			loadBox(box,TypeSquare.empty,map);
			xmlBoxes = xmlBoxes.replaceFirst("<Empty>".concat(box).concat("</Empty>"), "");
		}
	}
	
	/**
	 * <p>Loads the box on the map.</p>
	 * @param box The position of the box in the map.  
	 * @param type The type of the box that is gonna be loaded.
	 * @param map The map that will load the box.
	 */
	
	private void loadBox(String box, TypeSquare type,Map map){
		int dash = box.indexOf("-");
		int x = Integer.parseInt(box.substring(0,dash));
		int y = Integer.parseInt(box.substring(dash+1, box.length()));
		map.setBasicMatrixSquare(x,y,type);
	}

	/** 
	 * Gets the name of the map.
	 * @return The map name in the XML.
	 */
	
	public String getMapName() {
		return mapName;
	}
	
	/**
	 * Sets the name of the map.
	 * @param mapName The new name of the map.
	 */
	
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	/**
	 * Loads the name in the content of the XML.
	 */
	
	private void loadName(){
		mapName = getData(xmlMap, "Name");
	}
	
	/**
	 * Reads the contains of the elements of given alias.
	 * @param xml The content of the xml.
	 * @param data The alias that is want to be read.
	 * @return The content of the element that has been read.
	 */
	
	private String getData(String xml, String data){
		int begin;
		int end;
		begin = xml.indexOf("<".concat(data).concat(">")) + data.length() + 2;
		end = xml.indexOf("</".concat(data).concat(">"));
		return xml.substring(begin, end);
	}

}
