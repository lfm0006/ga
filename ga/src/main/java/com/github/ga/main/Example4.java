package com.github.ga.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.github.ga.domain.GenAlg;
import com.github.ga.domain.IFunctionFitness;
import com.github.ga.function.FunctionFitness4;
import com.github.ga.util.Utility;

public class Example4 {

	public static int NVAR = 4;
	public static int NEXEC = 1000;
    static Utility utility;
    static IFunctionFitness ff;

	public static void main(String[] args) throws FileNotFoundException {
		utility = new Utility();

		PrintStream ps = new PrintStream(new FileOutputStream("Example4.txt", true));
		utility.printHeader(System.out, NVAR);
		//utility.printHeader(ps, NVAR);
		ff = new FunctionFitness4();
		
		for(int j=0; j<NEXEC; j++) {
			GenAlg g = new GenAlg(100, 20, 4, 100000, 0.005, 0.6, 1.0, 0.6500, 10, false, ff);
			utility.printLineInt(System.out, j, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
			//utility.printLineInt(ps, j, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
		}
		ps.close();
	}
}

