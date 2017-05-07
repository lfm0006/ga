package com.github.ga.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.github.ga.domain.GenAlg;
import com.github.ga.domain.IFunctionFitness;
import com.github.ga.function.FunctionFitness2;
import com.github.ga.util.Utility;

public class Example2 {

	public static int NVAR = 5;
	public static int NEXEC = 1000;
    static Utility utility;
    static IFunctionFitness ff;

	public static void main(String[] args) throws FileNotFoundException {
		utility = new Utility();

		PrintStream ps = new PrintStream(new FileOutputStream("Example2.txt", true));
		utility.printHeader(System.out, NVAR);
		//utility.printHeader(ps, NVAR);
		ff = new FunctionFitness2();
		
		for(int j=0; j<NEXEC; j++) {
			GenAlg g = new GenAlg(60, 15, 5, 100000, 0.02, 0.6, 1.0, 54.9999, 42, false, ff);
			utility.printLine(System.out, j, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
			utility.printLine(ps, j, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
		}
		ps.close();
	}

	
}
