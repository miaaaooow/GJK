package algorithm;


import geomobjects.MovableObject;
import geomobjects.Point;

import java.util.ArrayList;
import java.util.Iterator;

public class GJK {
	
	/**
	 * Searching for an intersection between two objects
	 * Relies on the fact that two objects intersect when their Minkowski difference covers the origin.
	 * Explanation on: http://physics2d.com/content/gjk-algorithm
	 * @param objectA
	 * @param objectB
	 * @return
	 */
	public static boolean findIntersection(MovableObject objectA, MovableObject objectB) {
		ArrayList<Point> points1 = objectA.getPoints();
		ArrayList<Point> points2 = objectB.getPoints();
		ArrayList<Point> minkowski = 
			MinkowskiDifference((ArrayList<Point>)points1.subList(0, points1.size() - 1), 
								(ArrayList<Point>)points2.subList(0, points2.size() - 1));
		
		
		return false;
	}
	
	/**
	 * Minkowski difference A(-)B
	 * @param set1 = A
	 * @param set2 = B
	 * @return
	 */
	public static ArrayList<Point> MinkowskiDifference(ArrayList<Point> set1, ArrayList<Point> set2) {
		
		int size1 = set1.size();
		int size2 = set2.size();
		int newSize = size1 * size2;
		if (newSize == 0) {
			return null;
		} 
		else {
			ArrayList<Point> result = new ArrayList<Point>(size1 * size2);
			Iterator<Point> it1 = set1.iterator();
			Iterator<Point> it2 = set2.iterator();
			
			while(it1.hasNext()) {
				Point A = it1.next();
				while (it2.hasNext()) {
					Point B = it2.next();
					Point C = new Point (A.x - B.x, A.y - B.y);
					result.add(C);
				}
			}			
			return result;
		}	
	}
	
	/**
	 * This function finds a supporting(Extreme) point of a movable object in direction d = D1D2.
	 * @param convexHull - the list of points to be checked
	 * @param D1 - vector d's beginning
	 * @param D2 - vector d's end
	 * @return - the extreme point
	 */
	public static Point supportMapping (ArrayList<Point> convexHull, Point start, Point D1, Point D2) {
		Iterator<Point> it = convexHull.iterator();
		Point farthestPoint = null;
		int farthestDistance = Integer.MIN_VALUE;
		while (it.hasNext()) {
			Point nextPoint = it.next();
			int dist = MathTools.vectorDotProduct(start, nextPoint , D1, D2);
			if (dist > farthestDistance) {
				farthestDistance = dist;
				farthestPoint = nextPoint;
			}
		}
		return farthestPoint;
	}
	
	/**
	 * Some dummy tests
	 */
	public static void main(String[] args) {
		Point A = new Point(2, 1);
		Point B = new Point(3, 1);
		Point C = new Point(5, 3);
		Point D = new Point(4, 6);
		Point E = new Point(2, 7);
		Point F = new Point(1, 4);
		ArrayList<Point> ap = new ArrayList<Point>(5);
		ap.add(A);
		ap.add(B);
		ap.add(C);
		ap.add(D);
		ap.add(E);
		
		System.out.println(supportMapping(ap, F, C, A));
		System.out.println(supportMapping(ap, F, A, B));
		System.out.println(supportMapping(ap, E, D, C));
	}

}
