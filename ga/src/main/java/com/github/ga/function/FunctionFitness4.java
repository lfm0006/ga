package com.github.ga.function;

import com.github.ga.domain.IFunctionFitness;

public class FunctionFitness4 implements IFunctionFitness {
	private double bias = 0.0; // Para ajustar valores para o fitness maiores que zero
	private double f;
	
	public FunctionFitness4() {
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
			xMin[i] = 10;
			xMax[i] = 100000;
		}
		
		fmin = Math.abs(function(xMin));
		fmax = Math.abs(function(xMax));
		
		bias = fmin > fmax ? fmin : fmax;
	}

	private double Vbe = 0.65;
	private double Vcc = 12;
	private double gain = 5;
	private double ic = 0.04;
	private double beta = 100;

	// 0 - R1  1 - R2  2 - Re  3 - Rc
	
	public double function(double[] x) {
		f = Vbe - Math.max(Vcc * (x[1]/(x[0]+x[1]) - x[2]/(2*x[3])),0.0) 
				- Math.pow(gain - x[3]/x[2], 2)
				- Math.pow(x[3] - Vcc/(2*ic), 2)
				- Math.pow(x[1] - (Vbe + x[2]*ic)/(ic/beta), 2)
				//- Math.pow(x[0] - Vcc/(0.01*ic) + x[1], 2)
				//- Math.pow(x[0] - 10000, 2)
				//- Math.pow(0.1*ic - Vcc/(x[0] + x[1]), 2)
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
