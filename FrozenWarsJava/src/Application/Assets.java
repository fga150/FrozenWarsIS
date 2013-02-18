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
	    private static AtlasRegion mapBackground;
	    private static AtlasRegion lifeIcon;
	    private static AtlasRegion directionPanel;
	    private static AtlasRegion buttonLance;
	    private static AtlasRegion box;
	    private static AtlasRegion igloo;
	    private static AtlasRegion VerticalBarLeft;
	    private static AtlasRegion VerticalBarRigth;
	    private static AtlasRegion HorizontalBarUP;
	    private static AtlasRegion HorizontalBarDown;

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
		    setIgloo(atlas.findRegion("iglus"));
			mapBackground = atlas.findRegion("fondoJuego");
			lifeIcon = atlas.findRegion("IndicadorVidas");
			directionPanel=(atlas.findRegion("MandoDirecciones"));
			buttonLance=(atlas.findRegion("BotonLanza")); 
			box=((atlas.findRegion("casilla")));
			VerticalBarLeft=((atlas.findRegion("barraverticalIzq")));
			VerticalBarRigth=((atlas.findRegion("barraverticalDer")));
			HorizontalBarUP=((atlas.findRegion("barrahorizontalArriba")));
			HorizontalBarDown=((atlas.findRegion("barrahorizontalAbajo")));
	    }
	   
	    
	    public static AtlasRegion getLance() {
			return harpooon;
		}


		public static void setLance(AtlasRegion lance) {
			Assets.harpooon = lance;
		}


		public static AtlasRegion getButtonLance() {
			return buttonLance;
		}


		public static void setButtonLance(AtlasRegion buttonLance) {
			Assets.buttonLance = buttonLance;
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


		public static AtlasRegion getLifeIcon() {
			return lifeIcon;
		}


		public static void setLifeIcon(AtlasRegion lifeIcon) {
			Assets.lifeIcon = lifeIcon;
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
