package com.github.ga.util;

import java.io.PrintStream;
import java.nio.CharBuffer;

public class Utility {

	public final int CROSSOVER_SIMPLE = 1;
	public final int CROSSOVER_EDGE_RECOMBINATION = 2;
	public final int CROSSOVER_UNIFORM = 3;
	public final int CROSSOVER_PARTIAL_MAPPING = 4;
	
	public final int MUTATION_MIXING_SUBLIST = 1;
	public final int MUTATION_INVERSION_NODE = 2;
	public final int MUTATION_INVERSION_LIST = 3;
	
	public String spaces(int spaces) {
		  return CharBuffer.allocate(spaces).toString().replace( '\0', ' ' );
	}
	
	public String slashes(int slashes) {
		  return CharBuffer.allocate(slashes).toString().replace( '\0', '-' );
	}

	public void printHeader(PrintStream ps, int nVar) {
		ps.println(slashes(41 + nVar*5));
		ps.print("#      Data       Hora     Gen   Fitness  ");
		for(int i=0; i<nVar; i++) {
			ps.printf("x[%d] ", i);
		}
		ps.println();
		ps.println(slashes(41 + nVar*5));
	}

	public void printHeaderOrder(PrintStream ps, int len) {
		ps.println(slashes(41 + len*2));
		ps.print("#      Data       Hora     Gen   Fitness  Cromossomo");
		ps.println();
		ps.println(slashes(41 + len*2));
	}

	public void printLine(PrintStream ps, int number, int gen, double maxFitness, double[] x) {
		ps.printf("%6d ", number);
		java.util.Date d = new java.util.Date();
		ps.printf("%1$te/%1$tm/%1$tY ", d);
		ps.printf("%1$tH:%1$tM:%1$tS ", d);
		ps.printf("%6d ", gen);
		ps.printf("%6.4f ", maxFitness);
		for(int i=0; i<x.length; i++) {
			ps.printf("%3.2f ", x[i]);
		}
		ps.println();
	}

	public void printLine(PrintStream ps, int number, int gen, double maxFitness, String s) {
		ps.printf("%6d ", number);
		java.util.Date d = new java.util.Date();
		ps.printf("%1$te/%1$tm/%1$tY ", d);
		ps.printf("%1$tH:%1$tM:%1$tS ", d);
		ps.printf("%6d ", gen);
		ps.printf("%-7.4f ", maxFitness);
		ps.printf("%s", s);
		ps.println();
	}

	public void printLineInt(PrintStream ps, int number, int gen, double maxFitness, double[] x) {
		ps.printf("%6d ", number);
		java.util.Date d = new java.util.Date();
		ps.printf("%1$te/%1$tm/%1$tY ", d);
		ps.printf("%1$tH:%1$tM:%1$tS ", d);
		ps.printf("%6d ", gen);
		ps.printf("%6.4f ", maxFitness);
		for(int i=0; i<x.length; i++) {
			ps.printf("%4d ", Math.round(x[i]));
		}
		ps.println();
	}

}
