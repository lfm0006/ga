package com.github.ga.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.github.ga.domain.GenAlg;
import com.github.ga.domain.IFunctionFitness;
import com.github.ga.function.FunctionFitness3;
import com.github.ga.util.Utility;

public class Example3 {

	public static int NVAR = 10;
	public static int NEXEC = 1000;
    static Utility utility;
    static IFunctionFitness ff;

	public static void main(String[] args) throws FileNotFoundException {
		utility = new Utility();

		PrintStream ps = new PrintStream(new FileOutputStream("Example3.txt", true));
		utility.printHeader(System.out, NVAR);
		//utility.printHeader(ps, NVAR);
		ff = new FunctionFitness3();
		
		for(int j=0; j<NEXEC; j++) {
			GenAlg g = new GenAlg(100, 15, 10, 200000, 0.005, 0.6, 1.0, 152.749, 50, false, ff);
			utility.printLine(System.out, j, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
			utility.printLine(ps, j, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
		}
		ps.close();
	}
}

