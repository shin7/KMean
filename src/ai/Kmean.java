package ai;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * @author Ishin Vin
 * 
 * Sample Data:
 * =======================================================
 * || Object      || attribute 1 (X) || attribute 2 (Y) ||
 * ||=============||=================||=================||
 * ||Medicine A   || 1               || 1               ||
 * ||=============||=================||=================||
 * ||Medicine B   || 2               || 1               ||
 * ||=============||=================||=================||
 * ||Medicine C   || 4               || 3               ||
 * ||=============||=================||=================||
 * ||Medicine D   || 5               || 4               ||
 * =======================================================
 * 
 */
public class Kmean {
	
	private int k;
	private double data[][];
	
	public Kmean(int k, double data[][]) {
		this.k = k;
		this.data = data;
	}
	
	public double[][] cluster() {
		double[][] centroids = initialCentroids();
		double[][] newCentroids = new double[k][data[0].length];
		while(centroids != newCentroids) {
			double[][] distances = computeDistance(data, centroids);
			double[][] groupClosest = getClosestCentroid(distances);
			centroids = getNewCentroid(groupClosest);
		}
		
		
		return null;
	}
	
	private double[][] initialCentroids() {
		System.out.println("=====Initial Centroids=====");
		double[][] centroids = new double[k][data[0].length];
		for(int i=0; i < k; i++) {
			for(int j=0; j < data[0].length; j++) {
				centroids[i][j] = data[i][j];
			}
		}
		printCentroids(centroids);
		return centroids;
	}
	
	private double[][] computeDistance(double[][] data, double[][] centroid) {
		System.out.println("=====Compute Distance=====");
		double value = 0.0;
		double[][] distances = new double[centroid.length][data.length];
		for(int i=0; i < centroid.length; i++) {
			for(int m=0; m < data.length; m++) {
				for(int n=0; n < data[m].length; n++) {
					value += Math.pow(data[m][n] - centroid[i][n], 2);
				}
				distances[i][m] = round(Math.sqrt(value), 2);
				value = 0.0;
			}
			System.out.println("D" + (i+1) + Arrays.toString(distances[i]));
		}
		return distances;
	}
	
	private double[][] getClosestCentroid(double[][] distances) {
		System.out.println("=====Group Closest Centroid=====");
		int[][] index = new int[distances[0].length][2];
		for(int i=0; i < distances[0].length; i++) {
			double min = Double.MAX_VALUE;
			int ind1 = -1;
			int ind2 = -1;
			for(int j=0; j < distances.length; j++) {
				if(distances[j][i] < min) {
					min = distances[j][i];
					ind1 = i;
					ind2 = j;
				}
			}
			index[i][0] = ind1;
			index[i][1] = ind2;
		}
		double[][] group = new double[distances.length][distances[0].length];
		for(int i=0; i < group.length; i++) {
			for(int j=0; j < group[i].length; j++) {
				for(int m=0; m < index.length; m++) {
					if(j == index[m][0] && i == index[m][1]) {
						group[i][j] = 1;
					}
				}
			}
			System.out.println("G" + (i+1) + Arrays.toString(group[i]));
		}
		return group;
	}
	
	private double[][] getNewCentroid(double[][] groupClosest) {
		System.out.println("=====Compute New Centroid=====");
		double[][] newCentroid = new double[k][data[0].length];
		int[] count = new int[groupClosest.length];
		for(int i=0; i < groupClosest.length; i++) {
			for(int j=0; j < groupClosest[i].length; j++) {
				if(groupClosest[i][j] == 1) {
					count[i] += 1;
				}
			}
		}
		double[][] temp = new double[k][data[0].length];
		for(int i=0; i < groupClosest.length; i++) {
			for(int n=0; n < data[0].length; n++) {
				for(int j=0; j < groupClosest[i].length; j++) {
					if(groupClosest[i][j] == 1) {
						temp[i][n] += data[j][n];
					}
				}
			}
		}
		for(int i=0; i < temp.length; i++) {
			for(int j=0; j < temp[i].length; j++) {
				newCentroid[i][j] = round(temp[i][j]/count[i], 2);
			}
		}
		printCentroids(newCentroid);
		return newCentroid;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
	}
	
	private void printCentroids(double[][] centroids) {
		for(int i=0; i < centroids.length; i++) {
			System.out.print("C" + (i+1) + "(");
			for(int j=0; j < centroids[0].length; j++) {
				System.out.print(centroids[i][j]);
				if(j != centroids[0].length -1) {
					System.out.print(",");
				}
			}
			System.out.println(")");
		}
	}
	
	public static void main(String[] args) {
		
		int k = 2;
		double data[][] = { {1,1}, {2,1}, {4,3}, {5,4} };
		
		Kmean kmean = new Kmean(k, data);
		kmean.cluster();
	}
	
}
