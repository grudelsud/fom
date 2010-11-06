package fom.clustering.algorithms.kmedoids.metrics;


public abstract class AbstractSimilarity<ObjectType> extends AbstractMetric<ObjectType> {

	private static final long serialVersionUID = 2981187345405737179L;

	@Override
	public boolean compare(double arg0, double arg1) {
		return arg0 > arg1;
	}

}