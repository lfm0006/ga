package com.github.ga.function;

import com.github.ga.domain.IFunctionFitness;

public class FunctionFitness3 implements IFunctionFitness {
	private double bias = 0.0; // Para ajustar valores para o fitness maiores que zero
	private double f;
	
	public double process(double[] x, boolean normalized) {
		if(normalized) {
			return(function(x) + bias);
		} else {
			return(function(x));
		}
			
	}

	public void setMinMax(double[] xMin, double[] xMax) {
		double fmin, fmax;
		for(int i=0; i < xMin.length; i++) {
			xMin[i] = 0;
			xMax[i] = 6;
		}
		
		fmin = Math.abs(function(xMin));
		fmax = Math.abs(function(xMax));
		
		bias = fmin > fmax ? fmin : fmax;
	}

	public double function(double[] x) {
		f = 152.75
				- Math.pow(x[0] - 1, 2) 
				- Math.pow(x[1] - 3, 2) 
				- Math.pow(x[2] - 4, 2) 
				- Math.pow(x[3] - 5, 2) 
				- Math.pow(x[4] - 2, 2)
				- Math.pow(x[5] - 0.5, 2)
				- Math.pow(x[6] - 1.5, 2)
				- Math.pow(x[7] - 2, 2)
				- Math.pow(x[8] - 1, 2)
				- Math.pow(x[9] - 3.5, 2)
				;
		return(f);
	}

	/**
	 * @return the bias
	 */
	public double getBias() {
		return bias;
	}

	/**
	 * @param bias the bias to set
	 */
	public void setBias(double bias) {
		this.bias = bias;
	}


}
