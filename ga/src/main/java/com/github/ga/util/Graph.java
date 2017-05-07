package com.github.ga.util;

public class Graph {
	
	public double r;
	public double shift;
	public int length;
	
	private double[] dss;
	private double[] dd;
	
	public Graph(int length, double r, double shift, double[] x, double[] y, double[] d) {
		
		this.length = length;
		this.r = r;
		this.shift = shift;
		
		dss = new double[length];
		dd = new double[length];
		
		for(int i=1; i<=length; i++){
			x[i-1] = (int) Math.round(r * Math.cos(i*2*Math.PI/length) + shift);
			y[i-1] = (int) Math.round(r * Math.sin(i*2*Math.PI/length) + shift);
		}
		
		int xs, ys;
		for(int m=1; m<=length; m++) {
			dss[m-1] = 0;
			for(int j=1; j<=m; j++) {
				xs = (int) Math.round(x[j%m]-x[(j-1)%m]);
				ys = (int) Math.round(y[j%m]-y[(j-1)%m]);
				dd[j%m] = Math.sqrt(xs*xs + ys*ys);
				dss[m-1] += dd[j%m]; 
			}
		}
		
		for(int i=0; i<length; i++){
			d[i] = Math.round(dss[i]*1E4+1)/1E4;
			//System.out.println(i + ": " + x[i]+ " " + y[i] + " " + dss[i]);
		}		
	}
	
	
	public static void main(String[] args) {
		int tam = 40;
		
		double[] x = new double[tam];
		double[] y = new double[tam];
		double[] d = new double[tam];
		
		Graph s = new Graph(tam, 10, 15, x, y, d);
	}
}
