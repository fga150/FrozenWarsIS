package GameLogic;

import GameLogic.Map.TypeSquare;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class XMLMapReader {

	private static int numUpgrades=4;
	
	private String xmlMap;
	private String mapName;
	
	//Methods of XML
	
	public XMLMapReader(String mapPath){
		loadXML(mapPath);
		loadName();
	}
	
	private void loadXML(String mapPath) {
		try {
			FileHandle handle = Gdx.files.internal("data/".concat(mapPath));
			xmlMap = handle.readString();
		} catch (Exception e) {
			System.out.println("Error de carga de fichero");
		}
	}
		
	public int[] getUpgrades(){
		String xmlUpgrades = getData(xmlMap, "Upgrades");
		int[] upgrades = new int[numUpgrades];
		upgrades[0] = Integer.parseInt(getData(xmlUpgrades,"Boots"));
		upgrades[1] = Integer.parseInt(getData(xmlUpgrades,"MaxRange"));
		upgrades[2] = Integer.parseInt(getData(xmlUpgrades,"NumLances"));
		upgrades[3] = Integer.parseInt(getData(xmlUpgrades,"Throw"));		
		return upgrades;
	}
	
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
		
	private void loadBox(String box, TypeSquare type,Map map){
		int dash = box.indexOf("-");
		int x = Integer.parseInt(box.substring(0,dash));
		int y = Integer.parseInt(box.substring(dash+1, box.length()));
		map.setBasicMatrixSquare(x,y,type);
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	private void loadName(){
		mapName = getData(xmlMap, "Name");
	}
	
	private String getData(String xml, String data){
		int begin;
		int end;
		begin = xml.indexOf("<".concat(data).concat(">")) + data.length() + 2;
		end = xml.indexOf("</".concat(data).concat(">"));
		return xml.substring(begin, end);
	}

}
