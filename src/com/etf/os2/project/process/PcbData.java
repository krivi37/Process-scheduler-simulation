package com.etf.os2.project.process;

public class PcbData {
	int cpuid;
	long vrijeme;
	long exec_time;
	int priority;
	double tau;
	static int broj_poziva;
	
	public static int getBrojPoziva() {
		return broj_poziva;
	}
	
	public static void setBrojPoziva(int b) {
		broj_poziva=b;
	}
	
	public void setTau(double t) {
		this.tau = t;
	}
	
	public double getTau() {
		return this.tau;
	}
	
	public void setVrijeme(long v) {
		this.vrijeme = v;
	}
	
	public long getVrijeme() {
		return this.vrijeme;
	}
	
	public void setPriority(int p) {
		this.priority = p;
	}
	
	public int getPriority() {
		return this.priority;
	}
	
	public void setExecTime(long e) {
		this.exec_time = e;
	}
	
	public long getExecTime () {
		return this.exec_time;
	}
}
