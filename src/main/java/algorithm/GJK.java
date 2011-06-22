package algorithm;


import geomobjects.MovableObject;
import geomobjects.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GJK {
	
	private final static Point THE_ORIGIN = new Point(0,0);
	
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
		
		ArrayList<Point> minkowski = MinkowskiDifference(points1, points2);

		// We take only the convex hull for farthest points calculations 
		ArrayList<Point> MinkowskiConvHull = ConvexHull.findConvexHull(minkowski);
		
		ArrayList<Point> simplex = new ArrayList<Point>(3);
		
		Random rand = new Random();
		int size = minkowski.size();
		// We take a random point from the Minkowski Diff
		Point P = minkowski.get(rand.nextInt(size)); 
	
		return simplexMethod(simplex, MinkowskiConvHull, P);
	}
	
	public static boolean simplexMethod (ArrayList<Point> simplex, ArrayList<Point> MinkowskiConvHull, Point P) {
		Point A;
		
		/** 
		 * 0 - simplex
		 */
		if (simplex.size() == 0) {

			System.out.println(P);
			A = supportMapping(MinkowskiConvHull, P, P, THE_ORIGIN);
			
			// The "farthest" point on the origin direction is farther from the origin
			if (distAB(THE_ORIGIN, P) < distAB(THE_ORIGIN, A)) {
				return false;
			}
			
			// a.d < 0
			if (vectorDotProduct(THE_ORIGIN, A, P, THE_ORIGIN) < 0) {
				return false;
			}			
			simplex.add(A);
		}
		
		/**
		 * 1 - simplex
		 */
		if (simplex.size() == 1) {

			A = simplex.get(0);
			//  determine Region of the origin
			if(vectorDotProduct(A, P, A, THE_ORIGIN) <= 0) {
				// back to 0-simplex
				return simplexMethod(simplex, MinkowskiConvHull, A);	
				
			} else {
				Point D1 = findPerpendicularD(P, A, THE_ORIGIN); // d vector point
				Point newA = supportMapping(MinkowskiConvHull, A, D1, THE_ORIGIN);
				if (vectorDotProduct(THE_ORIGIN, A, THE_ORIGIN, D1) < 0) {
					return false;
				}
				else {
					simplex.add(newA);
				}
			}
		}
		
		/**
		 * 2 - simplex
		 */
		if (simplex.size() == 2) {
			Point C = P;
			Point B = simplex.get(0);
			Point A1 = simplex.get(1);
			
			// to the right of AC
			if (vectorCrossProduct(A1, THE_ORIGIN, C) > 0) {
				//if O is ahead of the point a on the line ac
				if (vectorDotProduct(A1, THE_ORIGIN, A1, C) > 0) {
//			        simplex = [a, c]
//			        d =-((ac.unit() dot aO) * ac + a)	
					return true;
				}
				else { // O is behind a on the line ac
					simplex = new ArrayList<Point>(2);
					simplex.add(A1);
					return simplexMethod(simplex, MinkowskiConvHull, P);
				}
				
			}
			// to the left of AB
			else if (vectorCrossProduct(A1, THE_ORIGIN, B) > 0) {
				//if O is ahead of the point a on the line ab
				if (vectorDotProduct(A1, B, A1, THE_ORIGIN) > 0) {
//			        simplex = [a, b]
//			        d =-((ab.unit() dot aO) * ab + a)	
					return true;
				}
				else {// O is behind a on the line ab
					simplex = new ArrayList<Point>(2);
					simplex.add(A1);
					return simplexMethod(simplex, MinkowskiConvHull, P);
				}
			}	
			else
				return true;
			
		}
		
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
			if (nextPoint!= null) {
				int dist = vectorDotProduct(start, nextPoint , D1, D2);
				if (dist > farthestDistance) {
					farthestDistance = dist;
					farthestPoint = nextPoint;
				}
			}
		}
		return farthestPoint;
	}
	
	/** 
	 * Find point D, from line AB, so that OD is perpendicular to AB
	 */	
	public static Point findPerpendicularD (Point A, Point B, Point O) {
		// AB: y = ax + b
		double a = (double) (A.y - B.y) / (double) (A.x - B.x);
		double b = (double) A.y - (double) a * A.x;
		
		// OD y = slopeOD.x + z
		double slopeOD = - (double) (A.x - B.x) / (double) (A.y - B.y);
		double z = (double) O.y - (double) slopeOD * O.x;
		
		int x = (int) ((z - b) / (a - slopeOD));
		int y = (int) (slopeOD * x + z);
		
		return new Point(x, y);
		
	}
	
	/**
	 * max(dA) - max(dB)
	 * This function finds a supporting(Extreme) point of a movable object in direction d = D1D2.
	 * @param convexHull - the list of points to be checked
	 * @param D1 - vector d's beginning
	 * @param D2 - vector d's end
	 * @return - the extreme point
	 */
	public static Point efficientSupportMapping(ArrayList<Point> pointsA, ArrayList<Point> pointsB,
			Point start, Point D1, Point D2) {
		return null;
	}
	
	
	/**
	 * AB.CD
	 */
	public static int vectorDotProduct(Point A, Point B, Point C, Point D) {
//		System.out.println("A" + A);
//		System.out.println("B" + B);
		int ABx = B.x - A.x;
		int ABy = B.y - A.y;
		int CDx = D.x - C.x;
		int CDy = D.y - C.y;
		return ABx * CDx + ABy * CDy;
	}
	
	
	/**
	 * AB x AC
	 */	
	public static double vectorCrossProduct(Point A, Point B, Point C) {
		double AB = distAB(A, B);
		double BC = distAB(B, C);
		double CA = distAB(C, A);
		double p = (AB + BC + CA) / 2;
		double Sabc = Math.sqrt(p * (p - AB) * (p - BC) * (p - CA));
		return Sabc * 2;	
	}
	
	/**
	 * @param A
	 * @param B
	 * @return distance between A and B
	 */	
	public static double distAB(Point A, Point B) {
		return Math.sqrt((A.x - B.x) * (A.x - B.x) + (A.y - B.y) * (A.y - B.y));
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
