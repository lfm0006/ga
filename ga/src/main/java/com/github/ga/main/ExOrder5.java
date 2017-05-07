package com.github.ga.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.github.ga.domain.GenAlgOrder;
import com.github.ga.util.Utility;

public class ExOrder5 {

	public static int TAMANHO = 20;
	public static int TAMPOP = 30;
	public static int NGEN = 200000;
	public static int NEXEC = 1000;
	
	public static GenAlgOrder g;

	public static Utility utility;

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		utility = new Utility();

		double[] x = new double[]{20, 21, 23, 25, 25, 23, 20, 18, 15, 13, 10,  8,  8,  9, 11, 12, 14, 15, 17, 19}; 
		double[] y = new double[]{ 6,  8, 10, 15, 20, 25, 27, 27, 25, 22, 18, 15, 12, 10,  8,  7,  5,  5,  5,  5}; 

		PrintStream ps = new PrintStream(new FileOutputStream("ExOrder5.txt", true));
		utility.printHeaderOrder(System.out, TAMANHO);
		//utility.printHeader(ps);

		for(int j=0; j<NEXEC; j++) {
			g = new GenAlgOrder(x, 
								y, 
								TAMANHO, 
								TAMPOP, 
								NGEN, 
								0.9, 
								0.9, 
								2, 
								false, 
								60.9784, 
								utility.CROSSOVER_UNIFORM, 
								utility.MUTATION_INVERSION_LIST);

			utility.printLine(System.out, j, g.getGen(), g.indGlobalFitMax.getFitness(), g.indGlobalFitMax.getString());
			utility.printLine(ps, j, g.getGen(), g.indGlobalFitMax.getFitness(), g.indGlobalFitMax.getString());
		}
		ps.close();
	}

}
