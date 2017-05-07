package com.github.ga.domain;

import java.io.PrintStream;
import java.util.Vector;

import com.github.ga.util.Utility;

public class GenAlgOrder {

	public final int CROSSOVER_SIMPLE = 1;
	public final int CROSSOVER_EDGE_RECOMBINATION = 2;
	public final int CROSSOVER_UNIFORM = 3;
	public final int CROSSOVER_PARTIAL_MAPPING = 4;
	
	public final int MUTATION_MIXING_SUBLIST = 1;
	public final int MUTATION_INVERSION_NODE = 2;
	public final int MUTATION_INVERSION_LIST = 3;

	private int len;
	private int popSize;
	private int maxGen, gen;
	private double pCrossover, pMutation;
	private int elitism;
	private double precision;
    private boolean verbose = true;

	private long nMutation, nCrossover;
	
	private Population oldPop, newPop;
	private Chromossome indFitMin, indFitMax;
	public Chromossome indGlobalFitMax, indGlobalFitMin;

	private int typeCrossover;
	private int typeMutation; 
	
	// Posi��o (x,y) de cada n�
	private double[] x;
	private double[] y;

	// Estat�stica
    private double sumFitness;
    double savg, smax, smin; // Real statistics 
    public double maxFitness, minFitness; // Fitness M�nimo e M�ximo
    
    private boolean canExit = false;

    static Utility utility;
    
	// Construtor da classe com o tamanho
	public GenAlgOrder(double[] x, 
					   double[] y, 
					   int len, 
					   int popSize, 
					   int maxGen, 
					   double pCrossover, 
					   double pMutation, 
					   int elitism, 
					   boolean verbose, 
					   double precision,
					   int typeCrossover,
					   int typeMutation) {
		this.x = x;
		this.y = y;
		this.len = len;
		this.popSize = popSize;
		this.maxGen = maxGen;
		this.pCrossover = pCrossover;
		this.pMutation = pMutation;
		this.elitism = elitism;
		this.precision = precision;
		this.verbose = verbose;
		if(typeCrossover != 0) {
			this.typeCrossover = typeCrossover;
		} else {
			this.typeCrossover = utility.CROSSOVER_SIMPLE;
		}
		
		if(typeMutation != 0) {
			this.typeMutation = typeMutation;
		} else {
			this.typeMutation = utility.MUTATION_INVERSION_NODE;
		}

		// Cria a antiga e a nova popula��o
		Chromossome[] oldChrom = new Chromossome[popSize]; 
		Chromossome[] newChrom = new Chromossome[popSize]; 
		for(int i=0; i<popSize; i++) {
			oldChrom[i] = new Chromossome(len); 
			newChrom[i] = new Chromossome(len); 
		}
		oldPop = new Population(oldChrom);
		newPop = new Population(newChrom);

		// Cria indiv�duo para o m�nimo fitness
		indFitMin = new Chromossome(len);
		indFitMax = new Chromossome(len);
		indGlobalFitMax = new Chromossome(len);
		indGlobalFitMin = new Chromossome(len);
		
		utility = new Utility();
		
		Initialize();
		do {
			Generation(); 
			Statistics(); 
			if(verbose) {
				Report(System.out);
			}
			Replace();
		} while(gen < maxGen && !canExit);
		if(canExit) {
			Statistics();
		}
		
	}
	
	private void Initialize() {
		int j = 0;

		for(int i=0; i < popSize; i++) {
			oldPop.chromossome[i] = newChromossome();
			oldPop.chromossome[i].calcFitness();
		}
		gen = 0;
		nCrossover = 0;
		nMutation = 0;
		maxFitness = -1E9;
		minFitness = 1E9;
		Statistics();
	}

	private boolean Generation() {
		int mate1, mate2, j;

		gen++;
		if(elitism > 0) {
			j = elitism;
			for(int i=0; i<elitism; i += 2) {
				newPop.chromossome[i].setChromossome(indFitMax.getChromossome());
				newPop.chromossome[i].fitness = indFitMax.fitness;
				newPop.chromossome[i].fitnessNorm = indFitMax.fitnessNorm;
				newPop.chromossome[i+1].setChromossome(indFitMax.getChromossome());
				newPop.chromossome[i+1].setChromossome(indFitMax.getChromossome());
				newPop.chromossome[i+1].fitness = indFitMax.fitness;
				newPop.chromossome[i+1].fitnessNorm = indFitMax.fitnessNorm;
			}
		} else {
			j = 0;
		}
		do {
			// Pick pairs of mates 
			mate1 = Select(oldPop);
			mate2 = Select(oldPop);

			// Crossover and mutation
			Crossover(oldPop.chromossome[mate1], oldPop.chromossome[mate2], newPop.chromossome[j], newPop.chromossome[j+1]);

			newPop.chromossome[j].calcFitness();
			newPop.chromossome[j+1].calcFitness();
			
			if(newPop.chromossome[j].fitness <= precision) {
				canExit = true;
			}

			newPop.chromossome[j].parent1 = mate1;
			newPop.chromossome[j].parent2 = mate2;
			newPop.chromossome[j+1].parent1 = mate1;
			newPop.chromossome[j+1].parent2 = mate2;

			// Jump 2 individuals
			j += 2;
			
		} while(j < popSize);

		// if made for all population, return true
		return(j == popSize);

	}

	private void Replace() {
		for(int j=0; j<popSize; j++) {
			oldPop.chromossome[j].setChromossome(newPop.chromossome[j].getChromossome());  
			oldPop.chromossome[j].fitness = newPop.chromossome[j].fitness;
			oldPop.chromossome[j].fitnessNorm = newPop.chromossome[j].fitnessNorm;
		}
	}

	private void Statistics() {
		sumFitness = Math.abs(oldPop.chromossome[0].fitnessNorm);
		smin = oldPop.chromossome[0].fitnessNorm;
		smax = oldPop.chromossome[0].fitnessNorm;
		for(int j=1; j < popSize; j++) {

			sumFitness += Math.abs(oldPop.chromossome[j].fitnessNorm);
			
			if(oldPop.chromossome[j].fitnessNorm > smax) {
				smax = oldPop.chromossome[j].fitnessNorm;
				//indFitMax = oldPop.chromossome[j];
				indFitMax.setChromossome(oldPop.chromossome[j].getChromossome());
				indFitMax.fitness = oldPop.chromossome[j].fitness;
				indFitMax.fitnessNorm = oldPop.chromossome[j].fitnessNorm;
			}
			if(oldPop.chromossome[j].fitnessNorm < smin) {
				smin = oldPop.chromossome[j].fitnessNorm;
				indFitMin = oldPop.chromossome[j];
				indFitMin.setChromossome(oldPop.chromossome[j].getChromossome());
				indFitMin.fitness = oldPop.chromossome[j].fitness;
				indFitMin.fitnessNorm = oldPop.chromossome[j].fitnessNorm;
			}
		}
		savg = sumFitness / popSize;

		if(smax > maxFitness) {
			maxFitness = smax;
			indGlobalFitMax.setChromossome(indFitMax.getChromossome());
			indGlobalFitMax.fitness = indFitMax.fitness;
			indGlobalFitMax.fitnessNorm = indFitMax.fitnessNorm;
		}
		if(smin < minFitness) {
			minFitness = smin;
			indGlobalFitMin.setChromossome(indFitMin.getChromossome());
			indGlobalFitMin.fitness = indFitMin.fitness;
			indGlobalFitMin.fitnessNorm = indFitMin.fitnessNorm;
		}

	}

	private void writeChrom(PrintStream sOut, Chromossome chrom) {
		sOut.print(chrom.nodes[0]);
		for(int j=1; j < len; j++) {
			sOut.print("," + chrom.nodes[j]);
		}
	}

	private void Report(PrintStream ps) {
		int shift = 20;
		
		ps.print(utility.spaces(12)); 
		ps.print("Gen " + (gen-1));
		ps.print(utility.spaces(25)); 
		ps.print("Gen " + gen);
		ps.println();
		ps.println(" #  chromossome  fitness   # parents  chromossome  fitness");
		ps.println(utility.slashes(51 + 2*len));
		
		for(int j=0; j < popSize; j++) {
			ps.printf("%2d) ", j);
			
			writeChrom(ps, oldPop.chromossome[j]);
			ps.print(utility.spaces(shift-len));

			ps.printf("%9.4f |", oldPop.chromossome[j].fitness);

			ps.printf("%2d)(", j);
			ps.printf("%2d,", newPop.chromossome[j].parent1);
			ps.printf("%2d) ", newPop.chromossome[j].parent2);
			
			writeChrom(ps, newPop.chromossome[j]);
			ps.print(utility.spaces(shift-len));
			
			ps.printf("%9.4f |", newPop.chromossome[j].fitness);
			ps.println();
		}
		
		ps.println(utility.slashes(51 + 2*len));

		ps.print("Note: Generation " + gen + " & Accumulated Statistics:");
		ps.print(" max= "); ps.printf("%6.4f\n", smax);
		ps.print("min= "); ps.printf("%6.4f", smin);
		ps.print(" avg= "); ps.printf("%6.4f", savg);
		ps.print(" sum= "); ps.printf("%6.4f", sumFitness);
		ps.print(" nMutation= "); ps.printf("%d\n", nMutation);
		ps.print("nCrossover= "); ps.printf("%d", nCrossover);
		ps.println();

		ps.println(utility.slashes(51 + 2*len));
		ps.print("max global fitness = "); ps.printf("%6.4f ", maxFitness);
		writeChrom(ps, indGlobalFitMax);

		ps.println();
		ps.print("min global fitness = "); ps.printf("%6.4f", minFitness);
		ps.println();
		ps.println(utility.slashes(51 + 2*len));

	}

	private int Select(Population pop) {
		//  Select a single individual via roulette wheel selecion }
		double rand, partSum = 0.0; //  { Random point on wheel, partial sum }
		int j = 0; //  { population index }

	   rand = Math.random() * Math.abs(sumFitness); //  Wheel point calc. uses random number [0,1] }

	   //  Find wheel slot }
	   do {
		      partSum += Math.abs(pop.chromossome[j].fitnessNorm);
		      j++;
	   } while(partSum < rand && j != popSize-1);
	   //  Return individual number }
	   return(j);
	}
	
	private void Crossover(Chromossome parent1, Chromossome parent2, Chromossome child1, Chromossome child2) {
		Chromossome c1 = new Chromossome(len);
		Chromossome c2 = new Chromossome(len);

		if(Flip(pCrossover)) {
			switch(typeCrossover) { 
				case CROSSOVER_SIMPLE:
					c1 = parent1.crossoverOrder(parent2);
					c2 = parent2.crossoverOrder(parent1);
					break;
				case CROSSOVER_EDGE_RECOMBINATION:
					c1 = parent1.edgeRecombination(parent2);
					c2 = parent2.edgeRecombination(parent1);
					break;
				case CROSSOVER_UNIFORM:
					c1 = parent1.crossoverUniformOrder(parent2);
					c2 = parent1.crossoverUniformOrder(parent1);
					break;
				case CROSSOVER_PARTIAL_MAPPING:
					c1 = parent1.crossoverPartialMapping(parent2);
					c2 = parent1.crossoverPartialMapping(parent1);
					break;
			}

			nCrossover++;
			if(Flip(pMutation)) {
				switch(typeMutation){
					case MUTATION_MIXING_SUBLIST:
						c1.mutationMixingSubList();
						c2.mutationMixingSubList();
						break;
					case MUTATION_INVERSION_NODE:
						c1.mutationInversionNode();
						c2.mutationInversionNode();
						break;
					case MUTATION_INVERSION_LIST:
						c1.mutationInversionList();
						c2.mutationInversionList();
						break;
				}
				nMutation++;
			}
			
		} else {
			c1.setChromossome(parent1.getChromossome());
			c2.setChromossome(parent2.getChromossome());
		}
		child1.setChromossome(c1.getChromossome());
		child2.setChromossome(c2.getChromossome());
	}
	
	private boolean Flip(double param) {
		// Bernouilli random variable generation
		if (param > 0 && param < 1) {
			return (Math.random() < param);
		} else {
			return false;
		}
	}
	
	// Gera um novo cromossomo
	public Chromossome newChromossome() {
		return new Chromossome(len);
	}
	
	public class Population {
		Chromossome[] chromossome;
		
		Population(Chromossome[] chromossome) {
			this.chromossome = chromossome;
		}
	}
	
	public class Chromossome {
		// Vari�vel interna para cada cromossomo
		private int[] nodes;
		
		// Fun��o dist�ncia total dos n�s do cromossomo (fitness)
		private double fitness;
		
		// Fun��o dist�ncia normalizada (invers�o para maximiza��o)
		private double fitnessNorm;
		
		// Pais que originaram
		private int parent1 = 0, parent2 = 0;
		
		public Chromossome(int num) {
			Vector<Integer> aux = new Vector<Integer>();
			int i, j;
			nodes = new int[num];
			
			for(j=0; j<num; j++) {
				aux.add(new Integer(1+j));
			}
			
			j =0;
			while(aux.size()>0) {
				i = (int) Math.round(Math.random()*aux.size());
				if(i == aux.size()){
					i--;
				}
				nodes[j++] = aux.get(i).intValue();
				aux.remove(i);
			}
		}
		
		public int[] getChromossome() {
			return nodes;
		}

		public void setChromossome(int[] x) {
			nodes = x;
		}
		
		public Chromossome crossoverOrder(Chromossome chrom) {
			
			Chromossome result = new Chromossome(this.nodes.length);
			
			int i, j, start, end;
			Vector v = new Vector();
			start = (int) Math.round(Math.random()*nodes.length);
			if(start == nodes.length) {
				start--;
			}
			end = start + (int)Math.round(Math.random()*(nodes.length-start));
			if(end == nodes.length) {
				end--;
			}
			for(i=0; i<nodes.length; i++) {
				if((i<start)||(i>end)) {
					v.add(new Integer(this.nodes[i]));
				} else {
					result.nodes[i] = this.nodes[i];
				}
			}
			j=0;
			for(i=0; i<nodes.length; i++) {
				if(j == start) {
					j = end+1;
				}
				if(v.indexOf(new Integer(chrom.nodes[i]))>=0) {
					result.nodes[j]=chrom.nodes[i];
					j++;
				}
			}
			return(result);
		}
		
		public Chromossome crossoverUniformOrder(Chromossome chrom) {
			Chromossome result = new Chromossome(this.nodes.length);
			int i, j;
			
			Vector v = new Vector();
			for(i=0; i<nodes.length; i++) {
				if(Math.random() < 0.5) {
					v.add(new Integer(this.nodes[i]));
					result.nodes[i] = -1;
				} else {
					result.nodes[i] = this.nodes[i];
				}
			}
			j = 0;
			for(i=0; i<nodes.length; i++) {
				while((j<nodes.length)&&(result.nodes[j] != -1)) {
					j++;
				}
				if(v.indexOf(new Integer(chrom.nodes[i]))>=0) {
					result.nodes[j] = chrom.nodes[i];
					j++;
				}
			}			
			return(result);
		}
		
		public Chromossome edgeRecombination(Chromossome chrom) {
			
			Chromossome result = new Chromossome(this.nodes.length);
			
			int i, j, k, next_node, tam_prox;
			
			int tam = this.nodes.length;
			int node_actual;
			
			Vector visited = new Vector();
			// Cria os vetores que v�o armazenar as adjac�ncias
			Vector v[] = new Vector[tam];
			
			for(i=0; i<tam; i++) {
				v[i] = new Vector<Integer>();
			}
			
			// Cria lista de arestas
			for(i=0; i<tam; i++) {
				v[this.nodes[i]-1].add(new Integer(this.nodes[(i+1)%tam]));
				v[this.nodes[(i+1)%tam]-1].add(new Integer(this.nodes[i]));
				v[chrom.nodes[i]-1].add(new Integer(chrom.nodes[(i+1)%tam]));
				v[chrom.nodes[(i+1)%tam]-1].add(new Integer(chrom.nodes[i]));
			}
			
			node_actual = this.nodes[0];
			
			k = 0;
			while(visited.size()<tam && node_actual>0) {
				// corrente vai para posi��o correta e lista de usadas
				result.nodes[k++] = node_actual;
				visited.add(new Integer(node_actual));
				// remove a cidade corrente da lista de adjac�ncias
				for(i=0; i<tam; i++) {
					j = 0;
					while(j>=0) {
						j = v[i].indexOf(new Integer(node_actual));
						if(j>=0) {
							v[i].remove(j);
						}
					}
				}
			
				// escolhe a pr�xima cidade
				if(v[node_actual-1].size()>0) {
					next_node = ((Integer) v[node_actual-1].get(0)).intValue();
					tam_prox = v[next_node-1].size();
					for(i=1; i < v[node_actual-1].size(); i++) {
						j = ((Integer) v[node_actual-1].get(i)).intValue();
						if(v[j-1].size()<tam_prox) {
							next_node = j;
							tam_prox = v[j-1].size();
						}
					}
				} else {
					// N�o h� cidades na lista de adjac�ncias
					next_node = -1;
					for(i=1; (next_node<0) && (i<tam); i++) {
						if(visited.indexOf(new Integer(i))<0) {
							next_node = i;
						}
					}
					
				}
				node_actual = next_node;
			}
			return(result);		
		}
		
		public Chromossome crossoverPartialMapping(Chromossome chrome) {
			Chromossome result = new Chromossome(this.nodes.length);
			result.setChromossome(this.getChromossome());
			int i, j, start, end;
			
			int mapping[] = new int[this.nodes.length];
			start = (int) Math.round(Math.random()*nodes.length);
			if(start == nodes.length) {
				start--;
			}
			end = start + (int) Math.round(Math.random()*nodes.length-start);
			if(end == nodes.length) {
				end--;
			}
			for(i=0; i<this.nodes.length; i++) {
				mapping[i] = -1;
			}
			for(i=start; i<=end; i++) {
				mapping[this.nodes[i]-1] = chrome.nodes[i];
				mapping[chrome.nodes[i]-1] = this.nodes[i]; 
			}
			for(i=0; i<nodes.length; i++){
				if(mapping[result.nodes[i]-1] != -1) {
					j = mapping[result.nodes[i]-1];
					if(mapping[j-1] == result.nodes[i]) {
						result.nodes[i] = mapping[result.nodes[i]-1];
					}
				}
			}
			return(result);
		}
		
		// M�todos para muta��o
		public void mutationMixingSubList() {
			int i, j, start, end;
			start = (int) Math.round(Math.random()*nodes.length);
			
			if(start == nodes.length) {
				start--;
			}
			
			end = start + (int)Math.round(Math.random()*(nodes.length-start));
			if(end == nodes.length) {
				end--;
			}
			Vector aux = new Vector();
			for(i=start; i<end; i++) {
				aux.add(new Integer(nodes[i]));
			}
			j = start;
			while(aux.size()>0) {
				i = (int)Math.round(Math.random()*aux.size());
				if(i == aux.size()) {
					i--;
				}
				nodes[j] = ((Integer)aux.get(i)).intValue();
				aux.remove(i);
				j++;
			}
		}
		
		public void mutationInversionNode() {
			int i, start, end;
			start = (int) Math.round(Math.random()*nodes.length);
			if(start == nodes.length) {
				start--;
			}
			end = start;
			i=0;
			while((end == start) && (i<3)) {
				end = (int)Math.round(Math.random()*nodes.length);
				if(end == nodes.length) {
					end--;
				}
				i++;
			}
			i = nodes[start];
			nodes[start] = nodes[end];
			nodes[end] = i;
		}
		
		public void mutationInversionList() {
			int i, start, end;
			start = (int) Math.round(Math.random()*nodes.length);
			if(start == nodes.length) {
				start--;
			}
			end = start + (int) Math.round(Math.random()*(nodes.length-start));
			if(end == nodes.length) {
				end--;
			}
			int aux[] = new int[end-start+1];
			for(i=start; i<=end; i++) {
				aux[i-start] = nodes[end-i+start];
			}
			for(i=start; i<=end; i++) {
				nodes[i] = aux[i-start];
			}			
		}
		
		public void calcFitness() {
			double f = 0;
			for(int i=0; i<len; i++) {
				f += Math.sqrt((x[nodes[(i+1)%len]%len] - x[nodes[i]%len])*(x[nodes[(i+1)%len]%len] - x[nodes[i]%len]) 
				   + (y[nodes[(i+1)%len]%len] - y[nodes[i]%len])*(y[nodes[(i+1)%len]%len] - y[nodes[i]%len]));
				//System.out.print(f + " + ");
			}
			fitness = f;
			//System.out.println(" = " + fitness);
			fitnessNorm = 150 - fitness;
		}

		public String getString() {
			String s = String.valueOf(nodes[0]);
			for(int j=1; j < len; j++) {
				s += "," + String.valueOf(nodes[j]);
			}
			return(s);
		}

		/**
		 * @return the fitness
		 */
		public double getFitness() {
			return fitness;
		}

		/**
		 * @param fitness the fitness to set
		 */
		public void setFitness(double fitness) {
			this.fitness = fitness;
		}
		
	}

	/**
	 * @return the gen
	 */
	public int getGen() {
		return gen;
	}

	/**
	 * @param gen the gen to set
	 */
	public void setGen(int gen) {
		this.gen = gen;
	}


}