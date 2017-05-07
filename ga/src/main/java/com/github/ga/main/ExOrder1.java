package com.github.ga.main;

import com.github.ga.domain.GenAlgOrder;
import com.github.ga.domain.GenAlgOrder.Chromossome;
import com.github.ga.util.Utility;

public class ExOrder1 {

	public static int TAMANHO = 8;
	public static int TAMPOP = 20;
	public static int NGEN = 1000;
	
	public static GenAlgOrder g;
	public static Chromossome c1, c2, c3;
	public static Utility utility;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		utility = new Utility();

		// Exemplo de n�s
		double[] x = new double[]{21, 23, 24, 23, 21, 19, 18, 19}; 
		double[] y = new double[]{11, 12, 15, 18, 19, 18, 15, 12}; 
		
		g = new GenAlgOrder(x, y, TAMANHO, TAMPOP, NGEN, 0.9, 0.9, 2, false, 21.59338, utility.CROSSOVER_EDGE_RECOMBINATION, utility.MUTATION_INVERSION_LIST);
		
		System.out.print("Chromossome 1: ");
		c1 = g.newChromossome();
		c1.calcFitness();
		System.out.print(c1.getChromossome()[0]);
		for(int i=1; i<TAMANHO; i++) {
			System.out.print("," + c1.getChromossome()[i]);
		}
		System.out.printf(" = %3.2f\n", c1.getFitness());
		System.out.println();
		
		System.out.print("Chromossome 2: ");
		c2 = g.newChromossome();
		c2.calcFitness();
		System.out.print(c2.getChromossome()[0]);
		for(int i=1; i<TAMANHO; i++) {
			System.out.print("," + c2.getChromossome()[i]);
		}
		System.out.printf(" = %3.2f\n", c2.getFitness());
		System.out.println();
		
		c3 = c1.edgeRecombination(c2);
		c3.calcFitness();
		System.out.println();
		System.out.println("Edge Recombination: ");
		System.out.print("Chromossome 3: ");
		System.out.print(c3.getChromossome()[0]);
		for(int i=1; i<TAMANHO; i++) {
			System.out.print("," + c3.getChromossome()[i]);
		}
		System.out.printf(" = %3.2f\n", c3.getFitness());
		System.out.println();
		
		c3.mutationMixingSubList();
		c3.calcFitness();
		System.out.println();
		System.out.println("Mutation Sublist: ");
		System.out.print("Chromossome 3: ");
		System.out.print(c3.getChromossome()[0]);
		for(int i=1; i<TAMANHO; i++) {
			System.out.print("," + c3.getChromossome()[i]);
		}
		System.out.printf(" = %3.2f\n", c3.getFitness());
		System.out.println();
		
	}

}
