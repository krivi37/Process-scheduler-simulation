package com.etf.os2.project.scheduler;

import com.etf.os2.project.process.Pcb;

public abstract class Scheduler {
    public abstract Pcb get(int cpuId);

    public abstract void put(Pcb pcb);
    /*Format unosenja parametara:
      *br procesa i procesora a zatim specifikacija algoritma*
      1. SJF: "SJF" alfa boolean (preemptive - T/F)
      2. MFQS: "MFQS" broj_redova kvantovi_redova
      3. CFS: "CFS" */
    public static Scheduler createScheduler(String[] args) {
    	String par1 = args[0];
    	
    	if(args[0].compareTo("SJF") == 0) {
    		float a = Float.parseFloat(args[1]);
    		boolean p = Boolean.parseBoolean(args[2]);
    		return new SJF(a, p);
    	}
    	
    	else if(args[0].compareTo("MFQS") == 0) {
    		int a = Integer.parseInt(args[1]);
    		String[] str = new String[a];
    		for (int i = 2; i < args.length ;i++) {
    			str[i-2] = args[i];
    		}
    		return new MFQS(a , str);
    		
    	}
    	
    	else if(args[0].compareTo("CFS") == 0) {
    		return new CFS();
    	}
    	
    	else System.out.println("Lose podeseni parametri! Odabrati neki od algoritama SJF, MFQS, CFS");
    	
    	return null;
    }
}
