package org.ctlv.proxmox.manager;

import org.ctlv.proxmox.api.ProxmoxAPI;

public class ManagerMain {

	public static void main(String[] args) throws Exception {
		ProxmoxAPI api = new ProxmoxAPI();
		
		Controller controller = new Controller(api);
		
		Analyzer analizer = new Analyzer(api, controller);
		
		Monitor mon = new Monitor(api, analizer);
		
		mon.run();
	}

}