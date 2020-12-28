package com.etf.os2.project.scheduler;

import java.util.Iterator;
import java.util.Vector;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.PcbData;

public class SJF extends Scheduler {
	
	public float alfa, tau0;
	public boolean preemptive;
	public Vector<Pcb> red;
	
	@Override
	public Pcb get(int cpuId) {
		
		if (red.isEmpty())return null;
		else {
			Pcb p = red.remove(0);
			//p.setAffinity(cpuId);
			p.getPcbData().setVrijeme(Pcb.getCurrentTime());
			return p;
		}
	}

	@Override
	public void put(Pcb pcb) {
		pcb.setTimeslice(0);
		PcbData data = pcb.getPcbData();
		if(data==null) {
			data = new PcbData();
			double tau =  alfa*pcb.getExecutionTime()+(1-alfa)*tau0;
			data.setTau(tau);
			pcb.setPcbData(data);
		}
		else {
			double tau = alfa*pcb.getExecutionTime()+(1-alfa)*data.getTau();
			data.setTau(tau);
			pcb.setPcbData(data);
		}
		
		double t = data.getTau();
		
		if (preemptive) {
			if((PcbData.getBrojPoziva()>=Pcb.RUNNING.length) && (pcb.getPreviousState()==Pcb.ProcessState.BLOCKED || pcb.getPreviousState()==Pcb.ProcessState.CREATED)) {//provjera sa brojem poziva je uradjena da se ne bi uopste vrsile provjere u slucaju da je put pozvan manje puta nego sto ima procesora, posto bi to znacilo da sigurno nisu svi procesori zauzeti
				double maks = 0 - Double.MAX_VALUE;
				int indeks = 0;
				for (int i=0; i < Pcb.RUNNING.length;i++) {
					if(Pcb.RUNNING[i].getPcbData() != null) {//idle nit nece imati pcb data. Mogla je i provjera Pcb.Running != Pcb.IDLE mada to ne pise u postavci projekta vec je dio javnog testa
						double temp = Pcb.RUNNING[i].getPcbData().getTau() - (alfa*(Pcb.getCurrentTime() - Pcb.RUNNING[i].getPcbData().getVrijeme()));
						if (temp > maks) {
							maks = temp;
							indeks = i;
						}
					}
					else {//ako je na nekom procesoru idle pcb, odmah je prekini
						indeks = i;
						t = 0 - Double.MAX_VALUE;//t se postavlja da je sigurno manje od maksimuma
						maks = maks + 1;//u slucaju da je maks ostao na 0 - double.max val
						break;
					}
				}
				if (t < maks) {
					Pcb.RUNNING[indeks].preempt();
				}
			}
			PcbData.setBrojPoziva(PcbData.getBrojPoziva()+1);
		}

		//System.out.println("Novi tau: "+data.getTau());
		Iterator<Pcb> iter = red.iterator();
		int i = 0;
		while(iter.hasNext()) {
			if (iter.next().getPcbData().getTau()>t) {
				red.add(i, pcb);
				break;
			}
			i++;
		}
		if (i == red.size()) red.add(pcb);
	}
	public SJF(float a, boolean preemptive) {
		this.alfa = a;
		this.preemptive = preemptive;
		tau0 = 20;
		red = new Vector<Pcb>();
	}

}
