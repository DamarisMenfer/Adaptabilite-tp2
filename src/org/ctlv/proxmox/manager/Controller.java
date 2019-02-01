package org.ctlv.proxmox.manager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import org.ctlv.proxmox.api.ProxmoxAPI;
import org.json.JSONException;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class Controller {

	ProxmoxAPI api;
	public Controller(ProxmoxAPI api){
		this.api = api;
	}
	
	// migrer un conteneur du serveur "srcServer" vers le serveur "dstServer"
	public void migrateFromTo(String srcServer, String dstServer, String ctID) throws LoginException, JSONException, IOException  {
		
		api.stopCT(srcServer, ctID);
		
		boolean done = false;
		while (!done) {
		    try {
		    	api.migrateCT(srcServer, ctID, dstServer);
		        done = true;
		    } catch (Exception e) {
		    	System.out.println("Trying to migrate CT.");
		    }
		}
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		done = false;
		while (!done) {
		    try {
		    	api.startCT(dstServer, ctID);
		    	System.out.println("CT has been started.");
		        done = true;
		    } catch (Exception e) {
		    	System.out.println("Trying to start CT.");
		    }
		}
		
		System.out.println("CT complete migrate to " + dstServer);
		
		
	}

	// arr�ter le plus vieux conteneur sur le serveur "server"
	public void offLoad(String server) throws LoginException, JSONException, IOException {
		java.util.List<String> list = api.getCTList(server);
		String node = list.get(0);
		String ctID = list.get(0).substring(node.length()-5, node.length()-1);
		
		api.stopCT(node, ctID);
		
		boolean done = false;
		while (!done) {
		    try {
		    	api.deleteCT(node, ctID);
		        done = true;
		    } catch (Exception e) {
		    	System.out.println("Oldest CT could not be deleted.");
		    }
		}

	}

}
