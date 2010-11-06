/**
 * This file is part of the Java Machine Learning Library
 * 
 * The Java Machine Learning Library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * The Java Machine Learning Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Java Machine Learning Library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Copyright (c) 2006-2009, Thomas Abeel
 * 
 * Project: http://java-ml.sourceforge.net/
 * 
 */
package fom.clustering.algorithms.kmedoids.jmlcore;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;

/**
 * Interface for a data set.
 * 
 * {@jmlSource}
 * 
 * @version 0.1.5
 * 
 * @author Thomas Abeel
 * 
 */
public interface Dataset extends List<Instance> {

    /**
     * Returns a set containing all different classes in this data set. If no
     * classes are available, this will return the empty set.
     * 
     * @return
     */
    public SortedSet<Object> classes();

    /**
     * Add an instance to this data set. The compatibility of the new item with
     * the items in the data set should be checked by the implementation.
     * Incompatible items should not be added to the data set.
     * 
     * @param i
     *            the instance to be added
     * @return true if the instance was added, otherwise false
     */
    public boolean add(Instance i);

    /**
     * Get the instance with a certain index.
     * 
     * @param index
     *            the index of the instance you want to retrieve.
     * @return
     */
    public Instance instance(int index);

    /**
     * Create a number of folds from the data set and return them. The supplied
     * random generator is used to determine which instances are assigned to
     * each of the folds.
     * 
     * @param numFolds
     *            the number of folds to create
     * @param rg
     *            the random generator
     * @return an array of data sets that contains <code>numFolds</code> data
     *         sets.
     */
    public Dataset[] folds(int numFolds, Random rg);

    /**
     * The number of attributes in each instance. This value can be off when
     * instances have different number of attributes. When the data set contains
     * no instances, this method should return 0.
     * 
     * @return
     */
    public int noAttributes();

    /**
     * Returns the index of the class value in the supplied data set. This
     * method will return -1 if the class value of this instance is not set.
     * 
     * @param data
     *            the data set to give the index for
     * @return the index of the class value
     */
    public int classIndex(Object clazz);

    /**
     * Returns the class value of the supplied class index.
     * 
     * @param index
     *            the index to give the class value for
     * @return the class value of the index
     */
    public Object classValue(int index);

    /**
     * Create a deep copy of the data set. This method should also create deep
     * copies of the instances in the data set.
     * 
     * @return deep copy of this data set.
     */
    public Dataset copy();

    /**
     * Returns the k closest instances.
     * 
     * @param k
     *            the number of neighbors to select
     * @param instance
     *            the instance to determine the neighbors for
     * @param dm
     *            the distance metric to use
     * @return a set of closest neighbors
     */
    public Set<Instance> kNearest(int k, Instance instance, DistanceMeasure dm);
}
