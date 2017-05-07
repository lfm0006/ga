package com.github.ga.main;

import com.github.ga.domain.GenAlgOrder;
import com.github.ga.util.Utility;

public class ExOrder2 {

	public static int TAMANHO = 8;
	public static int TAMPOP = 60;
	public static int NGEN = 5000;
	
	public static GenAlgOrder g;

	public static Utility utility;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		utility = new Utility();

		double[] x = new double[]{21, 23, 24, 23, 21, 19, 18, 19}; 
		double[] y = new double[]{11, 12, 15, 18, 19, 18, 15, 12}; 
		
		g = new GenAlgOrder(x, y, TAMANHO, TAMPOP, NGEN, 0.9, 0.9, 2, true, 21.59338, utility.CROSSOVER_EDGE_RECOMBINATION, utility.MUTATION_INVERSION_LIST);
		
	}
}
