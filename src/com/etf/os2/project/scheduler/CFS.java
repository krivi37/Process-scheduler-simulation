package com.etf.os2.project.scheduler;

import java.util.Vector;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.Pcb.ProcessState;
import com.etf.os2.project.process.PcbData;

public class CFS extends Scheduler {
	
	Vector<Pcb> red;
	
	@Override
	public Pcb get(int cpuId) {
		if (red.isEmpty())return null;
		else {
			Pcb pcb = red.remove(0);
			long timeslice = (Pcb.getCurrentTime() - pcb.getPcbData().getVrijeme())/Pcb.getProcessCount();
			//if(timeslice == 0) pcb.setTimeslice(0);//konstanta
			if(pcb.getPreviousState() != ProcessState.CREATED)pcb.setTimeslice(timeslice);//moze i bez ovog ifa
			return pcb;
		}
	}

	@Override
	public void put(Pcb pcb) {
		if (pcb.getPreviousState() == ProcessState.CREATED) {
			PcbData data = new PcbData();
			pcb.setTimeslice(0);//pocetni timeslice je neka konstanta
			data.setVrijeme(Pcb.getCurrentTime());//pamti se vrijeme kad je stavljen u red
			data.setExecTime(0);//pocetni exec time je jednak konstanti, posto ce se sigurno izvrsiti
			pcb.setPcbData(data);
			red.add(0, pcb);//novokreirani procesi se postavljaju na pocetak reda
		}
		
		else if(pcb.getPreviousState() == ProcessState.BLOCKED) {
			pcb.getPcbData().setVrijeme(Pcb.getCurrentTime());//pamti se vrijeme kad je stavljen u red
			pcb.getPcbData().setExecTime(0);
			int pret_kap = red.size();
			for (int i = 0; i < red.size(); i++) {
				if (red.get(i).getPcbData().getExecTime() != 0) {
					red.add(i, pcb);
					break;
					}
			}
			
			if (red.size() == pret_kap)red.add(pcb);//ako su svi imali exec time 0, kapacitet je ostao isti
		}
		
		else {
			pcb.getPcbData().setVrijeme(Pcb.getCurrentTime());//pamti se vrijeme kad je stavljen u red
			long temp = pcb.getPcbData().getExecTime();
			temp += pcb.getExecutionTime();
			pcb.getPcbData().setExecTime(temp);
			int pret_kap = red.size();
			for (int i = 0; i<red.size(); i++) {
				if (red.get(i).getPcbData().getExecTime() > temp) {
					red.add(i, pcb);
					break;
				}
			}
			
			if (red.size() == pret_kap)red.add(pcb);// ako je imao duzi ukupni exec time od svih
		}

	}
	
	public CFS() {
		red = new Vector<Pcb>();
	}

}
