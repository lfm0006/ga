package com.github.ga.function;

import com.github.ga.domain.IFunctionFitness;

public class FunctionFitness1 implements IFunctionFitness {
	private double bias = 0.0; // Para ajustar valores para o fitness maiores que zero
	private double f;
	
	public FunctionFitness1() {
	}
	
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
		f = 2 - Math.pow(x[0] - 3, 2) - Math.pow(x[1] - 2, 2);
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
