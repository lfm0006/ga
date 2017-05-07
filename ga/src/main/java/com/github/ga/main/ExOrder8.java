package com.github.ga.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.github.ga.domain.GenAlgOrder;
import com.github.ga.util.Graph;
import com.github.ga.util.Utility;

public class ExOrder8 {

	public static int TAMANHO = 40;
	public static int TAMPOP = 30;
	public static int NGEN = 400000;
	public static int NEXEC = 1000;
	
	public static GenAlgOrder g;

	public static Utility utility;
	public static Graph s;

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		utility = new Utility();
		double[] x = new double[TAMANHO]; 
		double[] y = new double[TAMANHO]; 
		double[] precision = new double[TAMANHO];

		s = new Graph(TAMANHO, 10, 15, x, y, precision);

		//utility.printHeader(ps);
		for(int i=39; i<TAMANHO; i++) {
			PrintStream ps = new PrintStream(new FileOutputStream("./examples/exorder8/ExOrder8_"+String.valueOf(i)+".txt", true));
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
