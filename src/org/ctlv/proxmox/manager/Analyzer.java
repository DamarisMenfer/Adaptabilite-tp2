package org.ctlv.proxmox.manager;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.security.auth.login.LoginException;

import org.ctlv.proxmox.api.Constants;
import org.ctlv.proxmox.api.ProxmoxAPI;
import org.ctlv.proxmox.api.data.LXC;
import org.json.JSONException;

public class Analyzer {
	ProxmoxAPI api;
	Controller controller;
	
	public Analyzer(ProxmoxAPI api, Controller controller) {
		this.api = api;
		this.controller = controller;
	}
	
	public void analyze(Map<String, List<LXC>> myCTsPerServer) throws LoginException, JSONException, IOException  {

		// Calculer la quantit� de RAM utilis�e par mes CTs sur chaque serveur
		long memOnServer1 = api.getNode(Constants.SERVER1).getMemory_used();
		
		long memOnServer2 = api.getNode(Constants.SERVER2).getMemory_used();
		
		// M�moire autoris�e sur chaque serveur
		float memRatioOnServer1 =  api.getNode(Constants.SERVER1).getMemory_total();
		float memRatioOnServer2 = api.getNode(Constants.SERVER2).getMemory_total(); 
		
		System.out.println("Ratio:");
		float mem = ((float)memOnServer1/(float)memRatioOnServer1)*100;
		System.out.println(mem);

		
		// Analyse et Actions
		
		if ((float)memOnServer1 > (memRatioOnServer1*0.012) && (float)memOnServer2 > (memRatioOnServer2*0.12))
		{
			Random rndServer = new Random(new Date().getTime());
			// choisir un serveur al�atoirement
			String serverName;
			if (rndServer.nextFloat() < Constants.CT_CREATION_RATIO_ON_SERVER1) {
				serverName = Constants.SERVER1;
			}
			else {
				serverName = Constants.SERVER2;
			}
			controller.offLoad(serverName);
			
			
		}
		else { 
			if ((float)memOnServer1 > (memRatioOnServer1*0.08)) {
				List<LXC> listCTs = myCTsPerServer.get(Constants.SERVER1);
				
				int ctID = -1;
				
				for(LXC ct : listCTs) {
					int id = Integer.parseInt(ct.getVmid());	
					if(id >= 2400 && id < 2500) {
						ctID = id;
					}
				}
				if(ctID == -1)
					System.out.println("Could not found ct available to migrate");
				else {
					System.out.println("CT to migrate to server2: " + ctID);
					controller.migrateFromTo(Constants.SERVER1, Constants.SERVER2, Integer.toString(ctID));
				}
			}
			
			
			
			if((float)memOnServer2 > (memRatioOnServer2*0.8)) {
				
			}
		}
		
	}

}
