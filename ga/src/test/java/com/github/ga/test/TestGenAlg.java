package com.github.ga.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import com.github.ga.domain.GenAlg;
import com.github.ga.domain.IFunctionFitness;
import com.github.ga.function.FunctionFitness1;
import com.github.ga.function.FunctionFitness2;
import com.github.ga.function.FunctionFitness3;
import com.github.ga.util.Utility;

import junit.framework.TestCase;

public class TestGenAlg extends TestCase {

    static IFunctionFitness ff;
    static Utility utility;
	PrintStream ps;

	@Test
	public void testExample1() {
		utility = new Utility();
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream("TestExample.txt", true));
			ff = new FunctionFitness1();
			GenAlg g = new GenAlg(60, 15, 2, 100000, 0.01, 0.6, 0.8, 1.9999, 40, false, ff);
			utility.printHeader(System.out, 2);
			utility.printHeader(ps, 2);
			utility.printLine(System.out, 0, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
			utility.printLine(ps, 0, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(ps != null) {
				ps.close();
			}
		}
	}
	
	@Test
	public void testExample2() {
		utility = new Utility();
		PrintStream ps = null;
		int j = 0;
		try {
			ps = new PrintStream(new FileOutputStream("TestExample.txt", true));
			ff = new FunctionFitness2();
			utility.printHeader(System.out, 5);
			utility.printHeader(ps, 5);
			GenAlg g = new GenAlg(60, 15, 5, 100000, 0.02, 0.6, 1.0, 54.9999, 42, false, ff);
			utility.printLine(System.out, j, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
			utility.printLine(ps, j, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(ps != null) {
				ps.close();
			}
		}
	}

	@Test
	public void testExample3() {
		utility = new Utility();
		PrintStream ps = null;
		int j = 0;
		try {
			ps = new PrintStream(new FileOutputStream("TestExample.txt", true));
			ff = new FunctionFitness3();
			utility.printHeader(System.out, 10);
			utility.printHeader(ps, 10);
			GenAlg g = new GenAlg(100, 15, 10, 200000, 0.005, 0.6, 1.0, 152.749, 50, false, ff);
			utility.printLine(System.out, j, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
			utility.printLine(ps, j, g.fGen, g.maxFitness, g.indGlobalFitMax.xNorm);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(ps != null) {
				ps.close();
			}
		}
	}


}
