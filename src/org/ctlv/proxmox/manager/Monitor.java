package org.ctlv.proxmox.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.ctlv.proxmox.api.Constants;
import org.ctlv.proxmox.api.ProxmoxAPI;
import org.ctlv.proxmox.api.data.LXC;
import org.json.JSONException;

public class Monitor implements Runnable {

	Analyzer analyzer;
	ProxmoxAPI api;
	
	public Monitor(ProxmoxAPI api, Analyzer analyzer) {
		this.api = api;
		this.analyzer = analyzer;
	}
	

	@Override
	public void run() {
		
		while(true) {
			
			// R�cup�rer les donn�es sur les serveurs
			java.util.List<LXC> listServer1 = null;
			java.util.List<LXC> listServer2 = null;
			try {
				listServer1 = api.getCTs(Constants.SERVER1);
				listServer2 = api.getCTs(Constants.SERVER2);
			} catch (LoginException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			Map<String, java.util.List<LXC>> myCTsPerServer = new HashMap<String, java.util.List<LXC>>();
			
			myCTsPerServer.put(Constants.SERVER1, listServer1);
			myCTsPerServer.put(Constants.SERVER2, listServer2);
			
			
			// Lancer l'analyse
			try {
				analyzer.analyze(myCTsPerServer);
			} catch (LoginException | JSONException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
			// attendre une certaine p�riode
			try {
				Thread.sleep(Constants.MONITOR_PERIOD * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
