package fom.clustering.algorithms.kmedoids.metrics;


public abstract class AbstractDistance<ObjectType> extends AbstractMetric<ObjectType> {

	private static final long serialVersionUID = 9022478812308333985L;
	
	@Override
	public boolean compare(double arg0, double arg1) {
		return arg0 < arg1;
	}

}