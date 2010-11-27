package fom.clustering.algorithms;

public interface DistanceMeasure<ObjectType> {
	public double getDistance(ObjectType obj1, ObjectType obj2);
}
