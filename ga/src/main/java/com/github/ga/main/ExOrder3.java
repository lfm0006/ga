package com.github.ga.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.github.ga.domain.GenAlgOrder;
import com.github.ga.util.Utility;

public class ExOrder3 {
	public static int TAMANHO = 8;
	public static int TAMPOP = 30;
	public static int NGEN = 3000;
	public static int NEXEC = 1000;
	
	public static GenAlgOrder g;
	public static Utility utility;

	public static void main(String[] args) throws FileNotFoundException {
		utility = new Utility();

		double[] x = new double[]{21, 23, 24, 23, 21, 19, 18, 19}; 
		double[] y = new double[]{11, 12, 15, 18, 19, 18, 15, 12}; 

		PrintStream ps = new PrintStream(new FileOutputStream("ExOrder3.txt", true));
		utility.printHeaderOrder(System.out, TAMANHO);

		for(int j=0; j<NEXEC; j++) {
			g = new GenAlgOrder(x, y, TAMANHO, TAMPOP, NGEN, 0.9, 0.9, 4, false, 21.59338, 
					utility.CROSSOVER_EDGE_RECOMBINATION, utility.MUTATION_INVERSION_LIST);
			utility.printLine(System.out, j, g.getGen(), g.indGlobalFitMax.getFitness(), g.indGlobalFitMax.getString());
			utility.printLine(ps, j, g.getGen(), g.indGlobalFitMax.getFitness(), g.indGlobalFitMax.getString());
		}
		ps.close();
	}
}
