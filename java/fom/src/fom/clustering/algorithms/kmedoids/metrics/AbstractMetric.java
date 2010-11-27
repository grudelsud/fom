package fom.clustering.algorithms.kmedoids.metrics;

import java.util.List;

import fom.clustering.algorithms.kmedoids.jmlcore.DistanceMeasure;
import fom.clustering.algorithms.kmedoids.jmlcore.Instance;


/**
 * @author    Federico Frappi
 */
public abstract class AbstractMetric<ObjectType> implements DistanceMeasure {

	private static final long serialVersionUID = 5753727379216368524L;
	protected List<ObjectType> objects;
	
	public AbstractMetric(List<ObjectType> objects){
		this.objects = objects;
		initialize();
	}
	
	/**
	 * Allows any subclass to perform some sort of initialization, if needed.
	 * 
	 * @param objects The array of the objects to be clustered
	 */
	public abstract void initialize();
	
	/**
	 * 
	 * @param firstObjectIndex Index of the first object in objects[]
	 * @param secondObjectIndex Index of the second object in objects[]
	 * @param objects The array of the objects to be clustered
	 * @return The measure between the first and the second object
	 */
	public abstract double getMeasure(int firstObjectIndex, int secondObjectIndex);
	
	@Override
	public double measure(Instance arg0, Instance arg1) {
		return getMeasure((int)arg0.value(0), (int)arg1.value(0));
	}
	
}
