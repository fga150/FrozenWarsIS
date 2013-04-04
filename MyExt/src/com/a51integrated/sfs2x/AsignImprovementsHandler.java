package com.a51integrated.sfs2x;

import java.util.Random;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class AsignImprovementsHandler extends BaseClientRequestHandler {

	public void handleClientRequest(User player, ISFSObject params) {
		int nBarriles=params.getInt("numBarriles");
		ISFSArray mejoras=params.getSFSArray("arraymejoras");
		int[] arrayAleatorio=obtieneNumAleatorios(nBarriles);
		int[] arrayBarriles=new int[nBarriles];
		for(int i=0;i<nBarriles;i++){
			arrayBarriles[i]=Integer.MIN_VALUE;
		}
		int j=0;
		int k;
		for(int i=0;i<mejoras.size();i++){
			int n =mejoras.getInt(i);
			while(n>0 && j<nBarriles){
				k=arrayAleatorio[j];
				arrayBarriles[k]=i;
				j++;
				n--;
			}
		}
		
		ISFSArray barriles = new SFSArray();
		for(int i=0;i<nBarriles;i++){
			barriles.addInt(arrayBarriles[i]);
		}
		
		ISFSObject rtn = new SFSObject();
		rtn.putInt("nBarriles", nBarriles);
		rtn.putSFSArray("arrayBarriles", barriles);
		this.send("asignaMejoras", rtn, player.getLastJoinedRoom().getPlayersList());
	}
	
	public int[] obtieneNumAleatorios(int n){
		int k = n;
		int[] resultado = new int[n];
		int[] numeros=new int[n];       
		Random rnd = new Random();
		int res;
		       
		for(int i=0;i<n;i++){
		    numeros[i]=i+1;
		}
		       
		for(int i=0;i<n;i++){
		    res = rnd.nextInt(k);           
		        resultado[i]=numeros[res]-1;
		        numeros[res]=numeros[k-1];
		        k--;           
		}

		return resultado;
	}

}
