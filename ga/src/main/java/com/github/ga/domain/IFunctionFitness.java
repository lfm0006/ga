package com.github.ga.domain;

public interface IFunctionFitness {

	public double process(double[] x, boolean normalized);
	public void setMinMax(double[] xMin, double[] xMax);
	public double function(double[] x);
	public double getBias();
	public void setBias(double bias);

}
