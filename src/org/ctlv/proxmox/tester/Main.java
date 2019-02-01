package org.ctlv.proxmox.tester;

import java.io.IOException;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.ctlv.proxmox.api.Constants;
import org.ctlv.proxmox.api.ProxmoxAPI;
import org.ctlv.proxmox.api.data.LXC;
import org.json.JSONException;


public class Main {

	public static void main(String[] args) throws LoginException, JSONException, IOException {

		ProxmoxAPI api = new ProxmoxAPI();		
		
		
		// Listes les CTs par serveur
		for (int i=1; i<=10; i++) {
			String srv ="srv-px"+i;
			System.out.println("CTs sous "+srv);
			List<LXC> cts = api.getCTs(srv);
			
			for (LXC lxc : cts) {
				System.out.println("\t" + lxc.getName());
			}
		}
		
		
		
		
		List<LXC> listCTs = api.getCTs(Constants.SERVER1);
		
		
		for(LXC ct : listCTs) {
			int id = Integer.parseInt(ct.getVmid());	
			if(id >= 2400 && id < 2500) {
				/*boolean done = false;
				while (!done) {
				    try {
				    	api.stopCT(Constants.SERVER2, Integer.toString(id));
				        done = true;
				    } catch (Exception e) {
				    	System.out.println("Trying to stop CT.");
				    }
				}*/
				System.out.println("Delete CT" + Integer.toString(id));
				api.deleteCT(Constants.SERVER1, Integer.toString(id));
			}
		}
		
		

		/* Cr�er un CT
		api.createCT("srv-px7", "400", "ct-tpiss-virt-B4-ct3", 512);
		
		boolean done = false;
		while (!done) {
		    try {
		    	api.startCT("srv-px7", "400");
		        done = true;
		    } catch (Exception e) {
		    	System.out.println("CT not started yet.");
		    }
		}
		
		
		done = false;
		while (!done) {
		    try {
		    	api.stopCT("srv-px7", "400");
		        done = true;
		    } catch (Exception e) {
		    	System.out.println("CT not started yet.");
		    }
		}

		
		// Supprimer un CT
		api.deleteCT("srv-px7", "400");*/
		
		
		
		
		
	}
	
	public static void printContainersInfos(ProxmoxAPI api) throws LoginException, JSONException, IOException {
		System.out.println("\n\nNode: ");
		
		
		long diskOnContainer1 = api.getCT("srv-px7", "400").getDisk();
		long diskTotalContainer1 = api.getCT("srv-px7", "400").getMaxdisk();
		
		float diskRatioOnContainer1 = ((float)diskOnContainer1/(float)diskTotalContainer1)*100;
		System.out.println("	Disk Usage: "+diskRatioOnContainer1);
		
		long memOnContainer1 = api.getCT("srv-px7", "400").getMem();
		long memTotalContainer1 = api.getCT("srv-px7", "400").getMaxmem();
		
		float memRatioOnContainer1 = ((float)memOnContainer1/(float)memTotalContainer1)*100;
		System.out.println("	Memory Usage: "+ memRatioOnContainer1);
		
		long cpuOnContainer1 = api.getCT("srv-px7", "400").getCpu()*100;
		System.out.println("	CPU Usage: "+(float)cpuOnContainer1);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static void printServersInfos(ProxmoxAPI api) throws LoginException, JSONException, IOException {
		
		long memOnServer1 = api.getNode(Constants.SERVER1).getMemory_used();		
		long memOnServer2 = api.getNode(Constants.SERVER2).getMemory_used();
		
		long memTotalServer1 = api.getNode(Constants.SERVER1).getMemory_total();
		long memTotalServer2 = api.getNode(Constants.SERVER2).getMemory_total();
		
		float memRatioOnServer1 = ((float)memOnServer1/(float)memTotalServer1)*100;
		float memRatioOnServer2 = ((float)memOnServer2/(float)memTotalServer2)*100;
		
		long diskOnServer1 = api.getNode(Constants.SERVER1).getRootfs_free();
		long diskOnServer2 = api.getNode(Constants.SERVER2).getRootfs_free();
		
		long diskTotalServer1 = api.getNode(Constants.SERVER1).getRootfs_total();
		long diskTotalServer2 = api.getNode(Constants.SERVER2).getRootfs_total();
		
		float diskRatioOnServer1 = 100-(((float)diskOnServer1/(float)diskTotalServer1)*100);
		float diskRatioOnServer2 = 100-(((float)diskOnServer2/(float)diskTotalServer2)*100);
		
		float cpuOnServer1 = api.getNode(Constants.SERVER1).getCpu()*100;
		float cpuOnServer2 = api.getNode(Constants.SERVER2).getCpu()*100;
		
		System.out.println("\n\nServer 1:");
		System.out.println("	Memory Usage: "+memRatioOnServer1);
		System.out.println("	CPU Usage: "+cpuOnServer1);
		System.out.println("	Disk Usage: "+diskRatioOnServer1);
		
		System.out.println("Server 2:");
		System.out.println("	Memory Usage: "+memRatioOnServer2);
		System.out.println("	CPU Usage: "+cpuOnServer2);
		System.out.println("	Disk Usage: "+diskRatioOnServer2);
		
	}

}
