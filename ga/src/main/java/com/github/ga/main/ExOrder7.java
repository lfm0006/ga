package com.github.ga.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.github.ga.domain.GenAlgOrder;
import com.github.ga.util.Utility;

public class ExOrder7 {

	public static int TAMANHO = 30;
	public static int TAMPOP = 30;
	public static int NGEN = 400000;
	public static int NEXEC = 1000;
	
	public static GenAlgOrder g;

	public static Utility utility;

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		utility = new Utility();

		// Exemplo de n�s
		double[] x = new double[]{ 25, 24, 23, 22, 20, 18, 16, 14, 12, 10,  8,  7,  6,  5,  5,  5,  6,  7,  8, 10, 12, 14, 16, 18, 20, 22, 23, 24, 25, 25}; 
		double[] y = new double[]{ 17, 19, 21, 22, 24, 25, 25, 25, 25, 24, 22, 21, 19, 17, 15, 13, 11,  9,  8,  6,  5,  5,  5,  5,  6,  8,  9, 11, 13, 15}; 

		double[] precision = new double[]{
				0, 
				4.4721, 
				8.9443, 
				11.7173, 
				17.3171, 
				21.5810, 
				24.9925, 
				28.5524, 
				32.2153, 
				35.7400, 
				39.7355, 
				41.8687, 
				44.7707, 
				47.9018, 
				50.0015, 
				52.2979, 
				54.0627, 
				56.0716, 
				57.0235, 
				59.2176, 
				60.5444, 
				61.1315, 
				61.8526, 
				62.7451, 
				63.1717, 
				63.4040, 
				63.5776, 
				63.6502, 
				63.8035, 
				63.8035}; 
		
		//int i=20; 
		//utility.printHeader(ps);
		for(int i=5; i<TAMANHO; i++) {
			PrintStream ps = new PrintStream(new FileOutputStream("ExOrder7_"+String.valueOf(i)+".txt", true));
			utility.printHeaderOrder(System.out, i);
		
			for(int j=0; j<NEXEC; j++) {
				g = new GenAlgOrder(x, 
									y, 
									(i+1), 
									TAMPOP, 
									NGEN, 
									0.9, 
									0.9, 
									2, 
									false, 
									precision[i], 
									utility.CROSSOVER_UNIFORM, 
									utility.MUTATION_INVERSION_LIST);
	
				utility.printLine(System.out, j, g.getGen(), g.indGlobalFitMax.getFitness(), g.indGlobalFitMax.getString());
				utility.printLine(ps, j, g.getGen(), g.indGlobalFitMax.getFitness(), g.indGlobalFitMax.getString());
			}
			ps.close();
		}
	}
}
