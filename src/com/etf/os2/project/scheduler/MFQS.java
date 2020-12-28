package com.etf.os2.project.scheduler;

import java.util.Vector;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.Pcb.ProcessState;
import com.etf.os2.project.process.PcbData;

public class MFQS extends Scheduler {
	
	int br_redova;
	long[] kvantovi;
	Vector<Pcb>[]redovi;
	
	

	@Override
	public Pcb get(int cpuId) {
		Pcb ret = null;
		for (int i = 0; i < br_redova; i++) {
			if (!redovi[i].isEmpty()) {
				ret = redovi[i].remove(0);
				break;
			}
		}
		return ret;
	}

	@Override
	public void put(Pcb pcb) {
		if (pcb.getPreviousState() == ProcessState.CREATED) {
			int priority = pcb.getPriority();
			PcbData data = new PcbData();
			pcb.setPcbData(data);
			if (priority > br_redova - 1) {//prioriteti pocinju od nule
				pcb.getPcbData().setPriority(br_redova - 1);
				pcb.setTimeslice(kvantovi[br_redova - 1]);//niz kvantova je iste velicine kao niz redova
				redovi[br_redova - 1].add(pcb);
			}
			else {
				pcb.getPcbData().setPriority(priority);
				pcb.setTimeslice(kvantovi[priority]);
				redovi[priority].add(pcb);
			}
		}
		
		else {
			int priority = pcb.getPcbData().getPriority();
			
			if (pcb.getPreviousState() == ProcessState.RUNNING) {
				if (priority < br_redova - 1) priority++;//veci priority znaci manji prioritet (0 najveci prioritet)
			}
			
			else if(pcb.getPreviousState() == ProcessState.BLOCKED) {
				if (priority > 0) priority--;
			}
			
			pcb.setTimeslice(kvantovi[priority]);
			pcb.getPcbData().setPriority(priority);
			redovi[priority].add(pcb);
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public MFQS(int br_red, String[] kvantovi) {
		br_redova = br_red;
		this.kvantovi = new long[br_redova];
		for (int i = 0; i< br_redova; i++) {
			this.kvantovi[i] = Long.parseLong(kvantovi[i]);
		}
		redovi = new Vector[br_redova];
		for (int i = 0; i < br_redova; i++)
			redovi[i] = new Vector<Pcb>();
	}

}
