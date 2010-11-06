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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;


/**
 * Provides a standard data set implementation.
 * 
 * @see Dataset
 * 
 * @author Thomas Abeel
 * 
 */
public class DefaultDataset extends Vector<Instance> implements Dataset {

    private int maxAttributes = 0;

    /**
     * 
     * Creates a data set that contains the provided instances
     * 
     * @param coll
     *            collection with instances
     */
    public DefaultDataset(Collection<Instance> coll) {
        this.addAll(coll);
    }

    /**
     * Creates an empty data set.
     */
    public DefaultDataset() {
        // nothing to do.
    }

    private void check(Collection<? extends Instance> c) {
        for (Instance i : c)
            check(i);
    }

    private void check(Instance i) {

        if (i.classValue() != null)
            classes.add(i.classValue());
        if (i.noAttributes() > maxAttributes)
            maxAttributes = i.noAttributes();
    }

    @Override
    public synchronized boolean addAll(Collection<? extends Instance> c) {
        check(c);
        return super.addAll(c);
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends Instance> c) {
        check(c);
        return super.addAll(index, c);
    }

    private static final long serialVersionUID = 8586030444860912681L;

    private TreeSet<Object> classes = new TreeSet<Object>();

    @Override
    public void clear() {
        classes.clear();
        super.clear();
    }

    @Override
    public synchronized boolean add(Instance e) {
        check(e);
        return super.add(e);
    }

    @Override
    public void add(int index, Instance e) {
        check(e);
        super.add(index, e);
    }

    @Override
    public synchronized void addElement(Instance e) {
        check(e);
        super.addElement(e);
    }

    @Override
    public synchronized void insertElementAt(Instance e, int index) {
        check(e);
        super.insertElementAt(e, index);
    }

    @Override
    public synchronized void setElementAt(Instance e, int index) {
        check(e);
        super.setElementAt(e, index);
    }

    @Override
    public Instance instance(int index) {
        return super.get(index);
    }

    @Override
    public SortedSet<Object> classes() {
        return classes;
    }

    /**
     * Returns the k instances of the given data set that are the closest to the
     * instance that is given as a parameter.
     * 
     * @param dm
     *            the distance measure used to calculate the distance between
     *            instances
     * @param inst
     *            the instance for which we need to find the closest
     * @return the instances from the supplied data set that are closest to the
     *         supplied instance
     * 
     */
    @Override
    public Set<Instance> kNearest(int k, Instance inst, DistanceMeasure dm) {
        Map<Instance, Double> closest = new HashMap<Instance, Double>();
        double max = Double.POSITIVE_INFINITY;
        for (Instance tmp : this) {
            double d = dm.measure(inst, tmp);
            if (dm.compare(d, max) && !inst.equals(tmp)) {
                closest.put(tmp, d);
                if (closest.size() > k)
                    max = removeFarthest(closest);
            }

        }
        return closest.keySet();
    }

    /*
     * Removes the element from the vector that is farthest from the supplied
     * element.
     */
    private double removeFarthest(Map<Instance, Double> vector) {
        Instance tmp = null;// ; = vector.get(0);
        double max = 0;
        for (Instance inst : vector.keySet()) {
            double d = vector.get(inst);
            if (d > max) {
                max = d;
                tmp = inst;
            }
        }
        vector.remove(tmp);
        return max;

    }

    @Override
    public Dataset[] folds(int numFolds, Random rg) {
        Dataset[] out = new Dataset[numFolds];
        List<Integer> indices = new Vector<Integer>();
        for (int i = 0; i < this.size(); i++)
            indices.add(i);
        int size = (this.size() / numFolds) + 1;
        int[][] array = new int[numFolds][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < numFolds; j++) {
                if (indices.size() > 0)
                    array[j][i] = indices.remove(rg.nextInt(indices.size()));
                else
                    array[j][i] = -1;
            }
        }
        for (int i = 0; i < numFolds; i++) {
            int[] indi;
            if (array[i][size - 1] == -1) {
                indi = new int[size - 1];
                System.arraycopy(array[i], 0, indi, 0, size - 1);
            } else {
                indi = new int[size];
                System.arraycopy(array[i], 0, indi, 0, size);
            }
            out[i] = new Fold(this, indi);

        }
        // System.out.println(Arrays.deepToString(array));
        return out;
    }

    @Override
    public int noAttributes() {
        if (this.size() == 0)
            return 0;
        return this.get(0).noAttributes();
    }

    @Override
    public int classIndex(Object clazz) {

        if (clazz != null)
            return this.classes().headSet(clazz).size();
        else
            return -1;

    }

    @Override
    public Object classValue(int index) {
        int i = 0;
        for (Object o : this.classes) {
            if (i == index)
                return o;
            i++;
        }
        return null;
    }

    @Override
    public Dataset copy() {
        DefaultDataset out = new DefaultDataset();
        for (Instance i : this) {
            out.add(i.copy());
        }
        return out;
    }
}
