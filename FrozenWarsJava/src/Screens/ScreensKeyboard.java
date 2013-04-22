package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class ScreensKeyboard {
	
	public static char keyAndroid(){
		char aux = 0;

		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)){
			
			if(Gdx.input.isKeyPressed(Keys.A)){
				aux = 'A';	
	        } else if (Gdx.input.isKeyPressed(Keys.B)){
	        	aux = 'B';	
	        } else if (Gdx.input.isKeyPressed(Keys.C)){
	        	aux = 'C';	
	        } else if (Gdx.input.isKeyPressed(Keys.D)){
	        	aux = 'D';	
	        } else if (Gdx.input.isKeyPressed(Keys.E)){
	        	aux = 'E';	
	        } else if (Gdx.input.isKeyPressed(Keys.F)){
	        	aux = 'F';	
	        } else if (Gdx.input.isKeyPressed(Keys.G)){
	        	aux = 'G';	
	        } else if (Gdx.input.isKeyPressed(Keys.H)){
	        	aux = 'H';	
	        } else if (Gdx.input.isKeyPressed(Keys.I)){
	        	aux = 'I';	
	        } else if (Gdx.input.isKeyPressed(Keys.J)){
	        	aux = 'J';	
	        } else if (Gdx.input.isKeyPressed(Keys.K)){
	        	aux = 'K';	
	        } else if (Gdx.input.isKeyPressed(Keys.L)){
	        	aux = 'L';	
	        } else if (Gdx.input.isKeyPressed(Keys.M)){
	        	aux = 'M';	
	        } else if (Gdx.input.isKeyPressed(Keys.N)){
	        	aux = 'N';	
	        } else if (Gdx.input.isKeyPressed(Keys.O)){
	        	aux = 'O';	
	        } else if (Gdx.input.isKeyPressed(Keys.P)){
	        	aux = 'P';	
	        } else if (Gdx.input.isKeyPressed(Keys.Q)){
	        	aux = 'Q';	
	        } else if (Gdx.input.isKeyPressed(Keys.R)){
	        	aux = 'R';	
	        } else if (Gdx.input.isKeyPressed(Keys.S)){
	        	aux = 'S';	
	        } else if (Gdx.input.isKeyPressed(Keys.T)){
	        	aux = 'T';	
	        } else if (Gdx.input.isKeyPressed(Keys.U)){
	        	aux = 'U';	
	        } else if (Gdx.input.isKeyPressed(Keys.V)){
	        	aux = 'V';	
	        } else if (Gdx.input.isKeyPressed(Keys.W)){
	        	aux = 'W';	
	        } else if (Gdx.input.isKeyPressed(Keys.X)){
	        	aux = 'X';	
	        } else if (Gdx.input.isKeyPressed(Keys.Y)){
	        	aux = 'Y';	
	        } else if (Gdx.input.isKeyPressed(Keys.Z)){
	        	aux = 'Z';	
	        }
			
		} else {	
	       
			if(Gdx.input.isKeyPressed(Keys.A)){
				aux = 'a';	
	        } else if (Gdx.input.isKeyPressed(Keys.B)){
	        	aux = 'b';	
	        } else if (Gdx.input.isKeyPressed(Keys.C)){
	        	aux = 'c';	
	        } else if (Gdx.input.isKeyPressed(Keys.D)){
	        	aux = 'd';	
	        } else if (Gdx.input.isKeyPressed(Keys.E)){
	        	aux = 'e';	
	        } else if (Gdx.input.isKeyPressed(Keys.F)){
	        	aux = 'f';	
	        } else if (Gdx.input.isKeyPressed(Keys.G)){
	        	aux = 'g';	
	        } else if (Gdx.input.isKeyPressed(Keys.H)){
	        	aux = 'h';	
	        } else if (Gdx.input.isKeyPressed(Keys.I)){
	        	aux = 'i';	
	        } else if (Gdx.input.isKeyPressed(Keys.J)){
	        	aux = 'j';	
	        } else if (Gdx.input.isKeyPressed(Keys.K)){
	        	aux = 'k';	
	        } else if (Gdx.input.isKeyPressed(Keys.L)){
	        	aux = 'l';	
	        } else if (Gdx.input.isKeyPressed(Keys.M)){
	        	aux = 'm';	
	        } else if (Gdx.input.isKeyPressed(Keys.N)){
	        	aux = 'n';	
	        } else if (Gdx.input.isKeyPressed(Keys.O)){
	        	aux = 'o';	
	        } else if (Gdx.input.isKeyPressed(Keys.P)){
	        	aux = 'p';	
	        } else if (Gdx.input.isKeyPressed(Keys.Q)){
	        	aux = 'q';	
	        } else if (Gdx.input.isKeyPressed(Keys.R)){
	        	aux = 'r';	
	        } else if (Gdx.input.isKeyPressed(Keys.S)){
	        	aux = 's';	
	        } else if (Gdx.input.isKeyPressed(Keys.T)){
	        	aux = 't';	
	        } else if (Gdx.input.isKeyPressed(Keys.U)){
	        	aux = 'u';	
	        } else if (Gdx.input.isKeyPressed(Keys.V)){
	        	aux = 'v';	
	        } else if (Gdx.input.isKeyPressed(Keys.W)){
	        	aux = 'w';	
	        } else if (Gdx.input.isKeyPressed(Keys.X)){
	        	aux = 'x';	
	        } else if (Gdx.input.isKeyPressed(Keys.Y)){
	        	aux = 'y';	
	        } else if (Gdx.input.isKeyPressed(Keys.Z)){
	        	aux = 'z';	
	        } else if (Gdx.input.isKeyPressed(Keys.NUM_0)){
	        	aux = '0';	
	        }  else if (Gdx.input.isKeyPressed(Keys.NUM_1)){
	        	aux = '1';	
	        }  else if (Gdx.input.isKeyPressed(Keys.NUM_2)){
	        	aux = '2';	
	        }  else if (Gdx.input.isKeyPressed(Keys.NUM_3)){
	        	aux = '3';	
	        }  else if (Gdx.input.isKeyPressed(Keys.NUM_4)){
	        	aux = '4';	
	        }  else if (Gdx.input.isKeyPressed(Keys.NUM_5)){
	        	aux = '5';	
	        }   else if (Gdx.input.isKeyPressed(Keys.NUM_6)){
	        	aux = '6';	
	        }   else if (Gdx.input.isKeyPressed(Keys.NUM_7)){
	        	aux = '7';	
	        }   else if (Gdx.input.isKeyPressed(Keys.NUM_8)){
	        	aux = '8';	
	        }   else if (Gdx.input.isKeyPressed(Keys.NUM_9)){
	        	aux = '9';	
	        } else if (Gdx.input.isKeyPressed(Keys.AT)){
	        	aux = '@';
	        }
	        
		}
		return aux;
	}
	
	
	
	public static char keyPc(){
		char aux = 0;

		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)){
			
			if(Gdx.input.isKeyPressed(Keys.A)){
				aux = 'A';	
	        } else if (Gdx.input.isKeyPressed(Keys.B)){
	        	aux = 'B';	
	        } else if (Gdx.input.isKeyPressed(Keys.C)){
	        	aux = 'C';	
	        } else if (Gdx.input.isKeyPressed(Keys.D)){
	        	aux = 'D';	
	        } else if (Gdx.input.isKeyPressed(Keys.E)){
	        	aux = 'E';	
	        } else if (Gdx.input.isKeyPressed(Keys.F)){
	        	aux = 'F';	
	        } else if (Gdx.input.isKeyPressed(Keys.G)){
	        	aux = 'G';	
	        } else if (Gdx.input.isKeyPressed(Keys.H)){
	        	aux = 'H';	
	        } else if (Gdx.input.isKeyPressed(Keys.I)){
	        	aux = 'I';	
	        } else if (Gdx.input.isKeyPressed(Keys.J)){
	        	aux = 'J';	
	        } else if (Gdx.input.isKeyPressed(Keys.K)){
	        	aux = 'K';	
	        } else if (Gdx.input.isKeyPressed(Keys.L)){
	        	aux = 'L';	
	        } else if (Gdx.input.isKeyPressed(Keys.M)){
	        	aux = 'M';	
	        } else if (Gdx.input.isKeyPressed(Keys.N)){
	        	aux = 'N';	
	        } else if (Gdx.input.isKeyPressed(Keys.O)){
	        	aux = 'O';	
	        } else if (Gdx.input.isKeyPressed(Keys.P)){
	        	aux = 'P';	
	        } else if (Gdx.input.isKeyPressed(Keys.Q)){
	        	aux = 'Q';	
	        } else if (Gdx.input.isKeyPressed(Keys.R)){
	        	aux = 'R';	
	        } else if (Gdx.input.isKeyPressed(Keys.S)){
	        	aux = 'S';	
	        } else if (Gdx.input.isKeyPressed(Keys.T)){
	        	aux = 'T';	
	        } else if (Gdx.input.isKeyPressed(Keys.U)){
	        	aux = 'U';	
	        } else if (Gdx.input.isKeyPressed(Keys.V)){
	        	aux = 'V';	
	        } else if (Gdx.input.isKeyPressed(Keys.W)){
	        	aux = 'W';	
	        } else if (Gdx.input.isKeyPressed(Keys.X)){
	        	aux = 'X';	
	        } else if (Gdx.input.isKeyPressed(Keys.Y)){
	        	aux = 'Y';	
	        } else if (Gdx.input.isKeyPressed(Keys.Z)){
	        	aux = 'Z';	
	        } else if ((Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) && Gdx.input.isKeyPressed(Keys.NUM_2)){
	        	aux = '@';
	        }
			
		} else {	
	       
			if(Gdx.input.isKeyPressed(Keys.A)){
				aux = 'a';	
	        } else if (Gdx.input.isKeyPressed(Keys.B)){
	        	aux = 'b';	
	        } else if (Gdx.input.isKeyPressed(Keys.C)){
	        	aux = 'c';	
	        } else if (Gdx.input.isKeyPressed(Keys.D)){
	        	aux = 'd';	
	        } else if (Gdx.input.isKeyPressed(Keys.E)){
	        	aux = 'e';	
	        } else if (Gdx.input.isKeyPressed(Keys.F)){
	        	aux = 'f';	
	        } else if (Gdx.input.isKeyPressed(Keys.G)){
	        	aux = 'g';	
	        } else if (Gdx.input.isKeyPressed(Keys.H)){
	        	aux = 'h';	
	        } else if (Gdx.input.isKeyPressed(Keys.I)){
	        	aux = 'i';	
	        } else if (Gdx.input.isKeyPressed(Keys.J)){
	        	aux = 'j';	
	        } else if (Gdx.input.isKeyPressed(Keys.K)){
	        	aux = 'k';	
	        } else if (Gdx.input.isKeyPressed(Keys.L)){
	        	aux = 'l';	
	        } else if (Gdx.input.isKeyPressed(Keys.M)){
	        	aux = 'm';	
	        } else if (Gdx.input.isKeyPressed(Keys.N)){
	        	aux = 'n';	
	        } else if (Gdx.input.isKeyPressed(Keys.O)){
	        	aux = 'o';	
	        } else if (Gdx.input.isKeyPressed(Keys.P)){
	        	aux = 'p';	
	        } else if (Gdx.input.isKeyPressed(Keys.Q)){
	        	aux = 'q';	
	        } else if (Gdx.input.isKeyPressed(Keys.R)){
	        	aux = 'r';	
	        } else if (Gdx.input.isKeyPressed(Keys.S)){
	        	aux = 's';	
	        } else if (Gdx.input.isKeyPressed(Keys.T)){
	        	aux = 't';	
	        } else if (Gdx.input.isKeyPressed(Keys.U)){
	        	aux = 'u';	
	        } else if (Gdx.input.isKeyPressed(Keys.V)){
	        	aux = 'v';	
	        } else if (Gdx.input.isKeyPressed(Keys.W)){
	        	aux = 'w';	
	        } else if (Gdx.input.isKeyPressed(Keys.X)){
	        	aux = 'x';	
	        } else if (Gdx.input.isKeyPressed(Keys.Y)){
	        	aux = 'y';	
	        } else if (Gdx.input.isKeyPressed(Keys.Z)){
	        	aux = 'z';	
	        } else if (Gdx.input.isKeyPressed(Keys.NUM_0)){
	        	aux = '0';	
	        }  else if (Gdx.input.isKeyPressed(Keys.NUM_1)){
	        	aux = '1';	
	        }  else if (Gdx.input.isKeyPressed(Keys.NUM_2)){
	        	aux = '2';	
	        }  else if (Gdx.input.isKeyPressed(Keys.NUM_3)){
	        	aux = '3';	
	        }  else if (Gdx.input.isKeyPressed(Keys.NUM_4)){
	        	aux = '4';	
	        }  else if (Gdx.input.isKeyPressed(Keys.NUM_5)){
	        	aux = '5';	
	        }   else if (Gdx.input.isKeyPressed(Keys.NUM_6)){
	        	aux = '6';	
	        }   else if (Gdx.input.isKeyPressed(Keys.NUM_7)){
	        	aux = '7';	
	        }   else if (Gdx.input.isKeyPressed(Keys.NUM_8)){
	        	aux = '8';	
	        }   else if (Gdx.input.isKeyPressed(Keys.NUM_9)){
	        	aux = '9';	
	        }   else if (Gdx.input.isKeyPressed(Keys.PERIOD)){
	        	aux = '.';	
	        }
	        
		}
		return aux;
	}

	public static boolean delete() {
		return Gdx.input.isKeyPressed(Keys.DEL);
	}

}
