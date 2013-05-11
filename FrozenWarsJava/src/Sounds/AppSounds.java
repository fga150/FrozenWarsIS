//package Sounds;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.audio.Sound;
//
//public class AppSounds{
//		
//		public static boolean activated = true;
//		
//	//	private HashMap<String, Array<Sound>> sounds;	
//		
//	//	public AppSounds(){
//	//		sounds = new HashMap<String,Array<Sound>>(2);
//	//	}
//		
//		public void init(){
//			loadSound("drag","ficha_arrastre_1.wav");
//			loadSound("drag","ficha_arrastre_2.wav");
//			loadSound("drag","ficha_arrastre_3.wav");
//			loadSound("bounce","ficha_salto1.wav");
//			loadSound("bounce","ficha_salto2.wav");
//			loadSound("bounce","ficha_salto3.wav");
//			loadSound("bounce","ficha_salto4.wav");
//			loadSound("eat","bite.wav");
//			loadSound("shakeDice2","shake_and_roll_dice2.mp3");
////			Hexa.rendererGame.addListener(this);
//		}
//		
//		private boolean loadSound(String eventType,String name){
////			Sound sound;
////			try{
////				sound = Gdx.audio.newSound(Gdx.files.internal("sounds/"+name));
////				Array<Sound> eventSounds = sounds.get(eventType);
////				if (eventSounds == null ){
////					eventSounds = new Array<Sound>();
////					sounds.put(eventType, eventSounds);
////				}
////				eventSounds.add(sound);
////			}catch(GdxRuntimeException e){
////				System.err.print("Coudn't find the sound "+name);
////				return false;
////			}
//				return true;
//		}
//		
//		
////		private boolean playSound(String name){
////			Array<Sound> soundArray = sounds.get(name);
////			if (soundArray!=null){ 
//				//Sound found
////				int i = Hexa.random.nextInt(soundArray.size);
////				soundArray.get(i).play();
////				return true;
////			}else{
////				//Sound not found
////				return false;
////			}
////		}
//		
//
////		@SuppressWarnings({ "rawtypes", "unchecked" })
////		public void listen(String evt) {
////			if(activated){
////				String eventType = (String) map.get("event");
////				if(eventType.equalsIgnoreCase("pawnMoved")){
////					String type = (String)map.get("movementType");
////					pawnMoved(type);
////				}
////				if(eventType.equalsIgnoreCase("throwDice")){
////					playSound("shakeDice2");
////				}
////				if(eventType.equalsIgnoreCase("sound")){
////					String type = (String)map.get("type");
////					System.out.println("SOUND: "+type);
////					playSound(type);
////				}
////			}
////		}
//
////		public static String createSoundEvent(String type) {
////			StringWriter moveEvent = new StringWriter();
////			JsonWriter writer = new JsonWriter(moveEvent);
////			try {
////				writer.object()
////					.set("event", "sound")
////					.set("type",type)
////					.pop();
////				System.out.println(moveEvent.toString());
////			} catch (IOException e) {
////				e.printStackTrace();
////			}
////			return moveEvent.toString();
////		}
////		
////		private void pawnMoved(String type) {
////			if(type.equalsIgnoreCase("drag")){
////				playSound("drag");
////			}
////			else if (type.equalsIgnoreCase("bounce")){
////				playSound("bounce");
////			}	
////		}
//		
//}
//
