package com.github.ga.domain;

import java.io.PrintStream;

import com.github.ga.domain.IFunctionFitness;
import com.github.ga.util.Utility;

public class GenAlg {

	// constants
	static int MAXPOP = 1000;
	static int MAXSTRING = 300;
	static int MAXNVAR = 20;

	public int popSize; // size of population
	public int lChrom; // length of chrmomossome
	public int maxGen; // maximum number of generations
	public double pMutation; // mutation rate
	public double pCrossover; // crossover rate
	public double pSelection; // selection rate
	public double precision; // fitness precision

	public int fGen;
    private Population oldPop, newPop;   // Two non-overlapping populations 
    private double sumFitness;
    public double sumFitnessNorm; // Real global variables 
    public double maxFitness, minFitness; // Fitness M�nimo e M�ximo 
    int nMutation, nCross, jCross;  // Integer statistics 
    double savg, smax, smin; // Real statistics 
    private Individual indFitMax, indFitMin;
    public Individual indGlobalFitMax, indGlobalFitMin;
    double[] xMin, xMax; // Normalize X Input
    double[] xFMax, xFMin; // Genotype normalized 

    private boolean fRandomSelection = false;
    private int nVar, lVar;
    private double fitness;

    private boolean canExit = false;
    private int elitism = 0;

    IFunctionFitness ff;
    
    //static FitnessFunction fitnessFunction; 
    static Utility utility;
    
	class Chromossome {
		boolean[] chromossome;

		public Chromossome() {
			this.chromossome = new boolean[MAXSTRING];
		}

		public Chromossome(boolean[] chromossome) {
			this.chromossome = chromossome;
		}
		
	}
	
    public class Individual {
		Chromossome chrom;
		double[] x;
		public double[] xNorm;
		double fitness;
		double fitnessNorm;
		int parent1, parent2, xSite;
		
		public Individual() {
			chrom = new Chromossome();
			x = new double[2];
			xNorm = new double[2];
		}

		public Individual(int n, int l) {
			chrom = new Chromossome(new boolean[l*n]);
			x = new double[n];
			xNorm = new double[n];
		}

	}
	
	class Population {
		Individual[] population;
		
		public Population() {
			this.population = new Individual[MAXPOP];
		}

		public Population(Individual[] population) {
			this.population = population;
		}
		
	}
	
	public GenAlg(int popSize, 
			      int lVar, 
			      int nVar, 
			      int maxGen, 
			      double pMutation, 
			      double pCrossover, 
			      double pSelection, 
			      double precision, 
			      int elitism,
			      boolean verbose, 
			      IFunctionFitness ff){

		this.maxGen = maxGen;
		this.lVar = lVar;
		this.nVar = nVar;
		this.popSize = popSize;
		this.pMutation = pMutation;
		this.pCrossover = pCrossover;
		this.pSelection = pSelection;
		this.precision = precision;
		this.elitism = elitism;
		this.ff = ff;
		
		// Instantiate fitness function
		// fitnessFunction = new FitnessFunction();
		// Lenght of chromossome is number of variables vs. the length of a variable 
		lChrom = lVar * nVar;
		
		xMin = new double[lVar];
		xMax = new double[lVar];
		
		Individual[] oldInd = new Individual[popSize];
		Individual[] newInd = new Individual[popSize];
		for(int i=0; i<popSize; i++) {
			oldInd[i] = new Individual(nVar, lVar); 
			newInd[i] = new Individual(nVar, lVar); 
		}
		oldPop = new Population(oldInd);
		newPop = new Population(newInd);
		
		indFitMax = new Individual(nVar, lVar);
		indFitMin = new Individual(nVar, lVar);
		indGlobalFitMax = new Individual(nVar, lVar);
		indGlobalFitMin = new Individual(nVar, lVar);
		
		Initialize();
		do {
			Generation();
			Statistics();
			if(verbose) {
				Report(System.out);
			}
			Replace();
		} while(fGen < maxGen && !canExit);
		
	}
	
	private void Initialize() {
		int j = 0;

		// MinMax definitions
		ff.setMinMax(xMin, xMax);
			
		do {
			for(int i=0; i < lChrom; i++) {
				oldPop.population[j].chrom.chromossome[i] = (Math.random() < 0.5);
			}
			Decode(oldPop.population[j].chrom, oldPop.population[j].x);
			Normalize(oldPop.population[j].x, oldPop.population[j].xNorm);
			
			oldPop.population[j].fitnessNorm = ff.process(oldPop.population[j].xNorm, true);
			oldPop.population[j].fitness = ff.process(oldPop.population[j].xNorm, false);
			
			if(oldPop.population[j].fitness > precision) {
				break;
			}
			j++;			
		} while(j < popSize);
		Statistics();
		fGen = 0;
		maxFitness = -1E9;
		minFitness = 1E9;
	}

	private void Replace() {
		oldPop = newPop;
		
		for(int j=0; j<popSize; j++) {
			for(int i=0; i<lChrom; i++) {
				oldPop.population[j].chrom.chromossome[i] = newPop.population[j].chrom.chromossome[i];  
			}
			for(int i=0; i<nVar; i++) {
				oldPop.population[j].x[i] = newPop.population[j].x[i];
				oldPop.population[j].xNorm[i] = newPop.population[j].xNorm[i];
			}
			oldPop.population[j].fitness = newPop.population[j].fitness;
			oldPop.population[j].fitnessNorm = newPop.population[j].fitnessNorm;
			oldPop.population[j].parent1 = newPop.population[j].parent1;
			oldPop.population[j].parent2 = newPop.population[j].parent2;
			oldPop.population[j].xSite = newPop.population[j].xSite;
		}
		
	}
	
	private boolean Generation() {
		int mate1, mate2, j;
		fGen++;
		
		if(elitism > 0) {
			j = elitism;
			for(int i=0; i<elitism; i += 2) {
				newPop.population[i]= indFitMax;
				newPop.population[i+1]= indFitMax;
			}
		} else {
			j = 0;
		}
		do {
			// Pick pairs of mates 
			mate1 = Select(oldPop);
			mate2 = Select(oldPop);
			
			// Crossover and mutation
			Crossover(oldPop.population[mate1].chrom, oldPop.population[mate2].chrom, newPop.population[j].chrom, newPop.population[j+1].chrom);
			
			// newPop [j]
			Decode(newPop.population[j].chrom, newPop.population[j].x);
			Normalize(newPop.population[j].x, newPop.population[j].xNorm);
			
			newPop.population[j].fitnessNorm = ff.process(newPop.population[j].xNorm, true);
			newPop.population[j].fitness = ff.process(newPop.population[j].xNorm, false);
			if(newPop.population[j].fitness > precision) {
				canExit = true;
			}
			newPop.population[j].parent1 = mate1;
			newPop.population[j].parent2 = mate2;
			newPop.population[j].xSite = jCross;

			// newPop [j+1]
			Decode(newPop.population[j+1].chrom, newPop.population[j+1].x);
			Normalize(newPop.population[j+1].x, newPop.population[j+1].xNorm);
			
			newPop.population[j+1].fitnessNorm = ff.process(newPop.population[j+1].xNorm, true);
			newPop.population[j+1].fitness = ff.process(newPop.population[j+1].xNorm, false);
			if(newPop.population[j+1].fitness > precision) {
				canExit = true;
			}
			newPop.population[j+1].parent1 = mate1;
			newPop.population[j+1].parent2 = mate2;
			newPop.population[j+1].xSite = jCross;

			// Jump 2 individuals
			j += 2;
			
		} while(j < popSize);

		// if made for all population, return true
		return(j == popSize);
	}

	private int Select(Population pop) {
		//  Select a single individual via roulette wheel selecion }
		double rand, partSum = 0.0; //  { Random point on wheel, partial sum }
		int j = 0; //  { population index }

	   if(fRandomSelection) {
		   rand = Math.random() * Math.abs(sumFitnessNorm); //  Wheel point calc. uses random number [0,1] }
	   } else {
		   rand = pSelection * Math.random() * Math.abs(sumFitnessNorm); //  Wheel point calc. uses random number [0,1] }
	   }

	   //  Find wheel slot }
	   do {
		      partSum += Math.abs(pop.population[j].fitnessNorm);
		      j++;
	   } while(partSum < rand && j != popSize-1);
	   //  Return individual number }
	   return(j);
	}
	
	private boolean Flip(double param) {
		// Bernouilli random variable generation
		if (param > 0 && param < 1) {
			return (Math.random() < param);
		} else {
			return false;
		}
	}
	
	private void Crossover(Chromossome parent1, Chromossome parent2, Chromossome child1, Chromossome child2) {

		// Cross 2 parents strings, place in 2 child strings
		if(Flip(pCrossover)) {
		      jCross = (int) Math.round(Math.random() * lChrom); // cross between 1 and l-1
		      nCross++; // increment crossover counter
		} else {
			jCross = lChrom; 
		}

		// First step, 1 => 1,  2=> 2
		for(int j=0; j<jCross; j++) {
			child1.chromossome[j] = Mutation(parent1.chromossome[j]);
			child2.chromossome[j] = Mutation(parent2.chromossome[j]);
		}
		
		// Second step, 1 => 2, 2=> 1
		if(jCross != lChrom) {
			for(int j=jCross; j < lChrom; j++) {
				child1.chromossome[j] = Mutation(parent2.chromossome[j]);
				child2.chromossome[j] = Mutation(parent1.chromossome[j]);
			}
		}
	}

	private void Elitism(Chromossome parent1, Chromossome parent2, Chromossome child1, Chromossome child2) {
	}
	
	private boolean Mutation(boolean alleleVal) {
		// Mutate an allele with pmutation, count number of mutations
		if(Flip(pMutation)) {
		      nMutation++;
		      return !alleleVal; // Change the bit value
		} else {
		      return alleleVal; // No changes
		}
	}
	
	private void Decode(Chromossome chrom, double[] x) {
	    double accum, powerOf2;

	    // Must add in vector X the binary decodification of variables
	    for(int i=0; i < nVar; i++) {
	    	accum = 0;
	    	powerOf2 = 1;
	    	for(int j=i*lVar; j < i*lVar+lVar; j++) {
	    		if(chrom.chromossome[j]) {
	    			accum += powerOf2;
	    		}
    			powerOf2 *= 2;
	    	}
	    	x[i] = accum;
	    }
	}

	private void Normalize(double[] x, double[] xNorm) {
		for(int i=0; i< nVar; i++) {
			xNorm[i] = xMin[i] + (xMax[i] - xMin[i])*x[i]/(Math.pow(2, lVar)-1);
		}
	}
	
	private void writeChrom(PrintStream sOut, Chromossome chrom) {
		for(int j=lChrom-1; j >= 0; j--) {

			if(chrom.chromossome[j]) {
				sOut.print('1');
			} else {
				sOut.print('0');
			}

		}
	}
	
	public void Report(PrintStream ps) {
		
		ps.print(utility.spaces(23)); 
		ps.print("Generation " + (fGen-1));
		ps.print(utility.spaces(57)); 
		ps.print("Generation " + fGen);
		ps.println();
		ps.print(" #                chromossome                     ");
		for(int i=0; i<nVar; i++) {
			ps.print("x[" + i + "] ");
		}
		ps.print("  fitness   # parents  xsite        chromossome                        ");
		for(int i=0; i<nVar; i++) {
			ps.print("x[" + i + "] ");
		}
		ps.println("  fitness");
		ps.println(utility.slashes(140 + 6*nVar));
		
		for(int j=0; j < popSize; j++) {
			ps.printf("%2d) ", j);
			
			writeChrom(ps, oldPop.population[j].chrom);
			ps.print(utility.spaces(46-lChrom));

			for(int i=0; i<nVar; i++) {
				ps.printf("%-3.2f ", oldPop.population[j].xNorm[i]);
			}
			ps.printf("%9.4f |", oldPop.population[j].fitness);

			ps.printf("%2d)(", j);
			ps.printf("%2d,", newPop.population[j].parent1);
			ps.printf("%2d) ", newPop.population[j].parent2);
			ps.printf("%2d ", newPop.population[j].xSite);
			
			writeChrom(ps, newPop.population[j].chrom);
			ps.print(utility.spaces(46-lChrom));
			
			for(int i=0; i<nVar; i++) {
				ps.printf("%-3.2f ", newPop.population[j].xNorm[i]);
			}
			ps.printf("%9.4f |", newPop.population[j].fitness);
			ps.println();
		}
		
		ps.println(utility.slashes(140 + 6*nVar));

		ps.print("Note: Generation " + fGen + " & Accumulated Statistics:");
		ps.print(" max= "); ps.printf("%6.4f", smax);
		ps.print(" min= "); ps.printf("%6.4f", smin);
		ps.print(" avg= "); ps.printf("%6.4f", savg);
		ps.print(" sum= "); ps.printf("%6.4f", sumFitness);
		ps.print(" nMutation= "); ps.printf("%d", nMutation);
		ps.print(" nCross= "); ps.printf("%d", nCross);
		ps.println();

		ps.println(utility.slashes(140 + 6*nVar));
		ps.print(" max global fitness = "); ps.printf("%6.4f ", maxFitness);
		for(int i=0; i<nVar; i++) {
			ps.printf("x[%d] = %-3.2f ", i, indGlobalFitMax.xNorm[i]);
		}
		ps.print(" min global fitness = "); ps.printf("%6.4f", minFitness);
		ps.print(" bias = "); ps.printf("%6.4f", ff.getBias());
		ps.println();
		ps.println(utility.slashes(140 + 6*nVar));

	}

	
	private void Statistics() {
		
		sumFitness = Math.abs(oldPop.population[0].fitness);
		sumFitnessNorm = Math.abs(oldPop.population[0].fitnessNorm);
		smin = oldPop.population[0].fitness;
		smax = oldPop.population[0].fitness;
		
		for(int j=1; j < popSize; j++) {

			sumFitness += Math.abs(oldPop.population[j].fitness);
			sumFitnessNorm += Math.abs(oldPop.population[j].fitnessNorm);
			
			if(oldPop.population[j].fitness > smax) {
				smax = oldPop.population[j].fitness;
				indFitMax = oldPop.population[j];
				Normalize(indFitMax.x, indFitMax.xNorm);
			}
			if(oldPop.population[j].fitness < smin) {
				smin = oldPop.population[j].fitness;
				indFitMin = oldPop.population[j];
				Normalize(indFitMin.x, indFitMin.xNorm);
			}
		}
		savg = sumFitness / popSize;

		if(smax > maxFitness) {
			maxFitness = smax;
			for(int i=0; i<nVar; i++) {
				indGlobalFitMax.x[i] = indFitMax.x[i];
				indGlobalFitMax.xNorm[i] = indFitMax.xNorm[i];
			}
		}
		if(smin < minFitness) {
			minFitness = smin;
			for(int i=0; i<nVar; i++) {
				indGlobalFitMin.x[i] = indFitMin.x[i];
				indGlobalFitMin.xNorm[i] = indFitMin.xNorm[i];
			}
		}
	}
	
}
