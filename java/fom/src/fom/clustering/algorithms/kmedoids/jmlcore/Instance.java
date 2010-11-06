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

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * The interface for instances in a data set.
 * 
 * {@jmlSource}
 * 
 * @see Dataset
 * 
 * @version 0.1.5
 * 
 * @author Thomas Abeel
 * 
 */
public interface Instance extends Map<Integer, Double>, Iterable<Double>, Serializable {
    /**
     * Returns the class value for this instance.
     * 
     * @return class value of this instance, or null if the class is not set
     */
    public Object classValue();

    public void setClassValue(Object value);

    /**
     * Returns the number of attributes this instance has.
     * 
     * @return number of attributes
     */
    public int noAttributes();

    @Override
    @Deprecated
    public int size();

    public double value(int pos);

    /**
     * Subtract an instance from this instance and returns the results.
     * 
     * This method does not modify this instance, but returns the result.
     * 
     * @return result of the subtraction
     */
    public Instance minus(Instance min);

    @Override
    public SortedSet<Integer> keySet();

    /**
     * Subtract a scalar from this instance and returns the results.
     * 
     * This method does not modify this instance, but returns the result.
     * 
     * @return result of the subtraction
     */
    public Instance minus(double value);

    /**
     * Add an instance to this instance and returns the results.
     * 
     * This method does not modify this instance, but returns the result.
     * 
     * @return result of the addition
     */
    public Instance add(Instance max);

    /**
     * Divide each value of this instance by a scalar value and returns the
     * results.
     * 
     * This method does not modify this instance, but returns the result.
     * 
     * @return result of the division
     */
    public Instance divide(double value);

    /**
     * Divide each value in this instance with the corresponding value of the
     * other instance and returns the results.
     * 
     * This method does not modify this instance, but returns the result.
     * 
     * @return result of the division
     */
    public Instance divide(Instance currentRange);

    /**
     * Add a scalar value to this instance and returns the results.
     * 
     * This method does not modify this instance, but returns the result.
     * 
     * @param value
     *            value to add
     * @return result of the addition
     */
    public Instance add(double value);

    /**
     * Multiply each value of this instance with a scalar value and return the
     * result.
     * 
     * @param value
     *            scalar to multiply with
     * @return result of multiplication
     */
    public Instance multiply(double value);

    /**
     * Multiply each value in this instance with the corresponding value in
     * provide instance.
     * 
     * @param value
     *            instance to multiply with
     * @return result of multiplication.
     */
    public Instance multiply(Instance value);

    /**
     * Removes an attribute from the instance.
     * 
     * @param i
     *            the index of the attribute to remove
     */
    public void removeAttribute(int i);

    /**
     * Take square root of all attributes.
     * 
     * @return square root of attribute values
     */
    public Instance sqrt();

    /**
     * Returns a unique identifier for this instance.
     * 
     * @return unique identifier
     */
    public int getID();

    /**
     * Create a deep copy of this instance
     * 
     * @return a deep copy of this instance
     */
    public Instance copy();

    /**
     * Removes a set of attributes from the instance.
     * 
     * @param indices
     *            set of indices that should be removed
     */
    public void removeAttributes(Set<Integer> indices);

}
