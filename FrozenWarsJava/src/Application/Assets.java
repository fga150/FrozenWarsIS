package Application;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Assets {

		/** Contiene el paquete de texturas. */
	    public static TextureAtlas atlas;
	   
	    /** Regiones dentro de la imagen de textura empaquetada */
	    //Fondos de pantalla
	    public static AtlasRegion initialBack;
	    public static AtlasRegion secondBack;
	    public static AtlasRegion backConf;

	    //Componentes de la pantalla de inicio
	    public static AtlasRegion play;
	    public static AtlasRegion settings;
	    public static AtlasRegion help;
	    public static AtlasRegion exit;

	    //Componentes de la pantalla de confirmacion
	    public static AtlasRegion panelConf;
	    public static AtlasRegion yesConf;
	    public static AtlasRegion noConf;
	    
	    //Componentes de la pantalla de carga
	    public static AtlasRegion cincoSec;
	    public static AtlasRegion cuatroSec;
	    public static AtlasRegion tresSec;
	    public static AtlasRegion dosSec;
	    public static AtlasRegion unSec;
	    public static Music music;
	    
	    //Componentes de la pantalla de opciones
	    public static AtlasRegion soundOn;
	    public static AtlasRegion soundOff;
	    public static AtlasRegion vibrationOn;
	    public static AtlasRegion vibrationOff;
	    public static AtlasRegion confirmedExitOn;
	    public static AtlasRegion confirmedExitOff;
	    public static AtlasRegion loggedIn;
	    public static AtlasRegion loggedOut;
	    public static AtlasRegion volver;
	    
	    private static AtlasRegion harpooon;
	    
	    private static AtlasRegion barrelWithFissure;
	    
	    private static AtlasRegion fissureCenter;
	    private static AtlasRegion fissureSideX;
	    private static AtlasRegion fissureSideY;
	    
	    private static AtlasRegion water1SideOpenE;
	    private static AtlasRegion water1SideOpenW;
	    private static AtlasRegion water1SideOpenN;
	    private static AtlasRegion water1SideOpenS;
	    
	    private static AtlasRegion water2SideOpenCornerEN;
	    private static AtlasRegion water2SideOpenCornerNW;
	    private static AtlasRegion water2SideOpenCornerSE;
	    private static AtlasRegion water2SideOpenCornerWS;
	    
	    private static AtlasRegion water2SideOpenBridgeX;
	    private static AtlasRegion water2SideOpenBridgeY;
	    
	    private static AtlasRegion water3SideEOpen;
	    private static AtlasRegion water3SideWOpen;
	    private static AtlasRegion water3SideNOpen;
	    private static AtlasRegion water3SideSOpen;
	    
	    private static AtlasRegion water4SideOpen;
	    
	    private static AtlasRegion mapBackground;
	    private static AtlasRegion lifeIconYellow;
	    private static AtlasRegion lifeIconRed;
	    private static AtlasRegion lifeIconGreen;
	    private static AtlasRegion lifeIconBlue;
	    private static AtlasRegion directionPanel;
	    private static AtlasRegion barrel;
		private static AtlasRegion buttonHarpoon;
	    private static AtlasRegion box;
	    private static AtlasRegion igloo;
	    private static AtlasRegion VerticalBarLeft;
	    private static AtlasRegion VerticalBarRigth;
	    private static AtlasRegion HorizontalBarUP;
	    private static AtlasRegion HorizontalBarDown;
	    private static AtlasRegion background;
	    
	    //Componentes de la pantalla de creacion de partida multiplayer
	    public static AtlasRegion backMultiplayer;
	    
	    public static AtlasRegion _1vsAllMode;
	    public static AtlasRegion battleRoyalMode;
	    public static AtlasRegion normalRoyalMode;
	    public static AtlasRegion survivalMode;
	    public static AtlasRegion teamPlayMode;
	    
	    public static AtlasRegion externalPlayerButton;
	    public static AtlasRegion externalPlayerText;
	    public static AtlasRegion externalPlayerTick;
	    
	    public static AtlasRegion inviteButton;
	    public static AtlasRegion playButton;
	    public static AtlasRegion backButton;
	    
	    public static AtlasRegion map;
	    public static AtlasRegion multiplayerGameTitle;
	    public static AtlasRegion pingu;
	    
	    public static AtlasRegion playerList;
	    public static AtlasRegion statusCancel;
	    public static AtlasRegion statusTick;
	    public static AtlasRegion statusInterrogation;
	    
	    public static AtlasRegion mapLeftArrow;
	    public static AtlasRegion mapRightArrow;
	    public static AtlasRegion modeLeftArrow;
	    public static AtlasRegion modeRightArrow;

	    /**
	     * Load.
	     */
	    public static void load(){
	    	//Se crea un Texture atlas con la imagen que contiene todos los pngs metidos en la carpeta data
	        atlas = new TextureAtlas(Gdx.files.internal("data/pack"));
	       
	        //Fondos de pantalla
	        initialBack = atlas.findRegion("initialBack");
	        secondBack = atlas.findRegion("secondBack");
	        backConf = atlas.findRegion("backConf");

	        //Componentes de la pantalla de inicio
	        play = atlas.findRegion("Play");
	        settings = atlas.findRegion("Settings");
	        help = atlas.findRegion("Help");
	        exit = atlas.findRegion("Exit");
	        
	        
		    //Componentes de la pantalla de confirmacion
		    panelConf = atlas.findRegion("Confirm");
		    yesConf = atlas.findRegion("YesConfirm");
		    noConf = atlas.findRegion("NoConfirm");
		        
		    //Componentes de la pantalla de carga
		    cincoSec = atlas.findRegion("cincoSec");
		    cuatroSec = atlas.findRegion("cuatroSec");
		    tresSec = atlas.findRegion("tresSec");
		    dosSec = atlas.findRegion("dosSec");
		    unSec = atlas.findRegion("unSec");
		    
	        //Musica
	        music = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/music.mp3", FileType.Internal));
	        
	        //Componentes de la pantalla de ajustes
	        soundOn = atlas.findRegion("SoundOn");
		    soundOff = atlas.findRegion("SoundOff");
		    vibrationOn = atlas.findRegion("VibrationOn");
		    vibrationOff = atlas.findRegion("VibrationOff");
		    confirmedExitOn = atlas.findRegion("ConfirmedExitOn");
		    confirmedExitOff = atlas.findRegion("ConfirmedExitOff");
		    loggedIn = atlas.findRegion("LoggedIn");
		    loggedOut = atlas.findRegion("LoggedOut");
		    volver = atlas.findRegion("Back");
		    
		    harpooon = atlas.findRegion("ArponParaBomba");
		    //Fissures
		    barrelWithFissure =  atlas.findRegion("barrelWithFissure");
		    fissureCenter =  atlas.findRegion("fissureCenter");
		    fissureSideX =  atlas.findRegion("fissureSideX");
		    fissureSideY =  atlas.findRegion("fissureSideY");
		    //Water 1 Open Side 4 positions
		    water1SideOpenE =  atlas.findRegion("Water1SideOpenEast");
		    water1SideOpenW =  atlas.findRegion("Water1SideOpenWest");
		    water1SideOpenS =  atlas.findRegion("Water1SideOpenSouth");
		    water1SideOpenN =  atlas.findRegion("Water1SideOpenNorth");
		   //Water 2 Open Sides Corner mode 4 positions
		    water2SideOpenCornerEN =  atlas.findRegion("Water2SideOpenCornerEN");
		    water2SideOpenCornerNW =  atlas.findRegion("Water2SideOpenCornerNW");
		    water2SideOpenCornerSE =  atlas.findRegion("Water2SideOpenCornerSE");
		    water2SideOpenCornerWS =  atlas.findRegion("Water2SideOpenCornerWS");
		    //Water 2 open sides bridge mode 2 positions
		    water2SideOpenBridgeX =  atlas.findRegion("Water2SideOpenBridgeX");
		    water2SideOpenBridgeY =  atlas.findRegion("Water2SideOpenBridgeY");
		    //Water 3 open side 4 position (the position is the unconnected side)
		    water3SideEOpen =  atlas.findRegion("Water3SideEastOpen");
		    water3SideWOpen =  atlas.findRegion("Water3SideWestOpen");
		    water3SideNOpen =  atlas.findRegion("Water3SideNorthOpen");
		    water3SideSOpen =  atlas.findRegion("Water3SideSouthOpen");
		    //water 4 open sides
		    water4SideOpen =  atlas.findRegion("Water4SideOpen");
		    //
		    
		    setIgloo(atlas.findRegion("iglus"));
			mapBackground = atlas.findRegion("fondoJuego");
			lifeIconYellow = atlas.findRegion("vidasJugadorAmarillo");
			lifeIconRed = atlas.findRegion("vidasJugadorRojo");
			lifeIconBlue = atlas.findRegion("vidasJugadorAzul");
			lifeIconGreen = atlas.findRegion("vidasJugadorVerde");
			directionPanel=(atlas.findRegion("MandoDirecciones"));
			buttonHarpoon = atlas.findRegion("BotonLanza"); 
			barrel = atlas.findRegion("barril");
			box=((atlas.findRegion("casilla")));
			VerticalBarLeft=((atlas.findRegion("barraverticalIzq")));
			VerticalBarRigth=((atlas.findRegion("barraverticalDer")));
			HorizontalBarUP=((atlas.findRegion("barrahorizontalArriba")));
			HorizontalBarDown=((atlas.findRegion("barrahorizontalAbajo")));
			background=atlas.findRegion("fondo");
			
			
			
		    //Componentes de la pantalla de creacion de partida multiplayer
			backMultiplayer = atlas.findRegion("BackgroundMultiplayer");
		    
		    _1vsAllMode = atlas.findRegion("1vsAllModeButton");
		    battleRoyalMode = atlas.findRegion("BattleRoyalButton");
		    normalRoyalMode = atlas.findRegion("NormalModeButton");
		    survivalMode = atlas.findRegion("SurvivalModeButton");
		    teamPlayMode = atlas.findRegion("TeamPlayButton");
		    
		    externalPlayerButton = atlas.findRegion("ExternalPlayersButton");
		    externalPlayerText = atlas.findRegion("ExternalPlayersText");
		    externalPlayerTick = atlas.findRegion("TickExternalPlayers");
		    
		    inviteButton = atlas.findRegion("InviteButton");
		    playButton = atlas.findRegion("PlayButton");
		    backButton = atlas.findRegion("BackButton");
		    
		    map = atlas.findRegion("Map");
		    multiplayerGameTitle = atlas.findRegion("MultiplayerGameTitle");
		    pingu = atlas.findRegion("Pingu");
		    
		    playerList = atlas.findRegion("PlayersList");
		    statusCancel = atlas.findRegion("StatusCancel");
		    statusTick = atlas.findRegion("StatusTick");
		    statusInterrogation = atlas.findRegion("StatusInterrogation");
		    
		    mapLeftArrow = atlas.findRegion("MapLeftArrow");
		    mapRightArrow = atlas.findRegion("MapRightArrow");
		    modeLeftArrow = atlas.findRegion("ModeLeftArrow");
		    modeRightArrow = atlas.findRegion("ModeRightArrow");
	    }
	    
	    public static AtlasRegion getBarrelWithFissure() {
			return barrelWithFissure;
		}

		public static AtlasRegion getBackground() {
			return background;
		}

		public static void setBackground(AtlasRegion background) {
			Assets.background = background;
		}

		public static AtlasRegion getHarpoon() {
			return harpooon;
		}


		public static void setHarpoon(AtlasRegion harpooon) {
			Assets.harpooon = harpooon;
		}

		


		public static AtlasRegion getFissureCenter() {
			return fissureCenter;
		}

		public static AtlasRegion getFissureSideX() {
			return fissureSideX;
		}

		public static AtlasRegion getFissureSideY() {
			return fissureSideY;
		}

		public static AtlasRegion getWater1SideOpenE() {
			return water1SideOpenE;
		}

		public static AtlasRegion getWater1SideOpenW() {
			return water1SideOpenW;
		}

		public static AtlasRegion getWater1SideOpenN() {
			return water1SideOpenN;
		}

		public static AtlasRegion getWater1SideOpenS() {
			return water1SideOpenS;
		}

		public static AtlasRegion getWater2SideOpenCornerEN() {
			return water2SideOpenCornerEN;
		}

		public static AtlasRegion getWater2SideOpenCornerNW() {
			return water2SideOpenCornerNW;
		}

		public static AtlasRegion getWater2SideOpenCornerSE() {
			return water2SideOpenCornerSE;
		}

		public static AtlasRegion getWater2SideOpenCornerWS() {
			return water2SideOpenCornerWS;
		}

		public static AtlasRegion getWater2SideOpenBridgeX() {
			return water2SideOpenBridgeX;
		}

		public static AtlasRegion getWater2SideOpenBridgeY() {
			return water2SideOpenBridgeY;
		}

		public static AtlasRegion getWater3SideEOpen() {
			return water3SideEOpen;
		}

		public static AtlasRegion getWater3SideWOpen() {
			return water3SideWOpen;
		}

		public static AtlasRegion getWater3SideNOpen() {
			return water3SideNOpen;
		}

		public static AtlasRegion getWater3SideSOpen() {
			return water3SideSOpen;
		}

		public static AtlasRegion getWater4SideOpen() {
			return water4SideOpen;
		}

		public static AtlasRegion getButtonHarpoon() {
			return buttonHarpoon;
		}

		public static void setButtonHarpoon(AtlasRegion buttonHarpoon) {
			Assets.buttonHarpoon = buttonHarpoon;
		}

		public static AtlasRegion getBarrel() {
			return barrel;
		}

		public static void setBarrel(AtlasRegion barrel) {
			Assets.barrel = barrel;
		}



		public static void dispose(){
	        atlas.dispose();
	    }


		public static AtlasRegion getDirectionPanel() {
			return directionPanel;
		}


		public static void setDirectionPanel(AtlasRegion directionPanel) {
			Assets.directionPanel = directionPanel;
		}


		public static AtlasRegion getMapBackground() {
			return mapBackground;
		}


		public static void setMapBackground(AtlasRegion mapBackground) {
			Assets.mapBackground = mapBackground;
		}


		public static AtlasRegion getLifeIconYellow() {
			return lifeIconYellow;
		}

		public static void setLifeIconYellow(AtlasRegion lifeIconYellow) {
			Assets.lifeIconYellow = lifeIconYellow;
		}

		public static AtlasRegion getLifeIconRed() {
			return lifeIconRed;
		}

		public static void setLifeIconRed(AtlasRegion lifeIconRed) {
			Assets.lifeIconRed = lifeIconRed;
		}

		public static AtlasRegion getLifeIconGreen() {
			return lifeIconGreen;
		}

		public static void setLifeIconGreen(AtlasRegion lifeIconGreen) {
			Assets.lifeIconGreen = lifeIconGreen;
		}

		public static AtlasRegion getLifeIconBlue() {
			return lifeIconBlue;
		}

		public static void setLifeIconBlue(AtlasRegion lifeIconBlue) {
			Assets.lifeIconBlue = lifeIconBlue;
		}

		public static AtlasRegion getBox() {
			return box;
		}

		

		public static AtlasRegion getVerticalBarLeft() {
			return VerticalBarLeft;
		}

		

		public static AtlasRegion getVerticalBarRigth() {
			return VerticalBarRigth;
		}

		

		public static AtlasRegion getHorizontalBarUP() {
			return HorizontalBarUP;
		}

		
		public static AtlasRegion getHorizontalBarDown() {
			return HorizontalBarDown;
		}


		public static AtlasRegion getIgloo() {
			return igloo;
		}


		public static void setIgloo(AtlasRegion igloo) {
			Assets.igloo = igloo;
		}

	
}
