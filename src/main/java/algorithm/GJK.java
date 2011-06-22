package algorithm;


import geomobjects.MovableObject;
import geomobjects.Point;

import java.util.ArrayList;
import java.util.Iterator;

public class GJK {
	
	/**
	 * Searching for an intersection
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
	public static Point supportMapping (ArrayList<Point> convexHull, Point D1, Point D2) {
		return null;
	}

}
