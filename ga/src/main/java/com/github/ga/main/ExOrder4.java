package com.github.ga.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.github.ga.domain.GenAlgOrder;
import com.github.ga.util.Utility;

public class ExOrder4 {

	public static int TAMANHO = 15;
	public static int TAMPOP = 30;
	public static int NGEN = 100000;
	public static int NEXEC = 1000;
	
	public static GenAlgOrder g;

	public static Utility utility;

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		utility = new Utility();

		// Exemplo de n�s
		double[] x = new double[]{12, 15, 20, 25, 25, 23, 20, 18, 15, 13, 10,  8,  8,  9, 11}; 
		double[] y = new double[]{10, 10, 12, 15, 20, 25, 27, 27, 25, 22, 18, 15, 12, 10, 10}; 

		PrintStream ps = new PrintStream(new FileOutputStream("ExOrder4.txt", true));
		utility.printHeaderOrder(System.out, TAMANHO);
		//utility.printHeader(ps);

		for(int j=0; j<NEXEC; j++) {
			g = new GenAlgOrder(x, y, TAMANHO, TAMPOP, NGEN, 0.9, 0.9, 6, false, 54.2596, utility.CROSSOVER_EDGE_RECOMBINATION, utility.MUTATION_INVERSION_LIST);
			utility.printLine(System.out, j, g.getGen(), g.indGlobalFitMax.getFitness(), g.indGlobalFitMax.getString());
			utility.printLine(ps, j, g.getGen(), g.indGlobalFitMax.getFitness(), g.indGlobalFitMax.getString());
		}
		ps.close();
	}

}
