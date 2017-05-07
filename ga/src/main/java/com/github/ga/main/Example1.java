package com.github.ga.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.github.ga.domain.GenAlg;
import com.github.ga.domain.IFunctionFitness;
import com.github.ga.function.FunctionFitness1;
import com.github.ga.util.Utility;

public class Example1 {
	
	public static int NVAR = 2;
	public static int NEXEC = 1000;
    static Utility utility;
    static IFunctionFitness ff;

	public static void main(String[] args) throws FileNotFoundException {
		utility = new Utility();

		PrintStream ps = new PrintStream(new FileOutputStream("Example1.txt", true));
		utility.printHeader(System.out, NVAR);
		ff = new FunctionFitness1();
		
		for(int j=0; j<NEXEC; j++) {
			GenAlg g = new GenAlg(60, 15, 2, 100000, 0.01, 0.6, 0.8, 1.9999, 40, false, ff);
			utility.printLine(System.out, j, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
			utility.printLine(ps, j, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
		}
		ps.close();
	}

}
