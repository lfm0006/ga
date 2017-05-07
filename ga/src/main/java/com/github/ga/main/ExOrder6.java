package com.github.ga.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.github.ga.domain.GenAlgOrder;
import com.github.ga.util.Utility;

public class ExOrder6 {

	public static int TAMANHO = 20;
	public static int TAMPOP = 30;
	public static int NGEN = 100000;
	public static int NEXEC = 1000;
	
	public static GenAlgOrder g;

	public static Utility utility;

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		utility = new Utility();

		// Exemplo de n�s
		double[] x = new double[]{20, 21, 23, 25, 25, 23, 20, 18, 15, 13, 10,  8,  8,  9, 11, 12, 14, 15, 17, 19}; 
		double[] y = new double[]{ 6,  8, 10, 15, 20, 25, 27, 27, 25, 22, 18, 15, 12, 10,  8,  7,  5,  5,  5,  5}; 

		double[] precision = new double[]{0, 4.4721, 10.0645, 20.7453, 30.3158, 40.0703, 45.4404, 47.5354, 49.6929, 51.1158, 54.2720, 57.2571, 58.6735, 59.1978, 59.5411, 59.7980, 60.6470, 60.6632, 60.7265, 60.9784}; 
		
		
		//utility.printHeader(ps);
		for(int i=5; i<TAMANHO; i++) {
			PrintStream ps = new PrintStream(new FileOutputStream("ExOrder6_"+String.valueOf(i)+".txt", true));
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
 

 

 
