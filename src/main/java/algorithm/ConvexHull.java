package algorithm;

import geomobjects.Point;
import geomobjects.PointComparator;

import java.util.ArrayList;
import java.util.Arrays;


public class ConvexHull {
	
	/**
	 * This method finds a convex hull; Graham scan.
	 * In the case of line or a point - it returns them back.
	 * @param points - the points representing the object's corners
	 * @return an array of the convex hull points
	 */
	
	public static Point [] findConvexHull(Point [] points) {
		int size = 0;
		while (points[size] != null) {
			size++;
		}
		
		if (size < 3) {
			return points;
		} 
		else {
			Point [] convexHull = new Point [size + 1];
			
			if (size == 3) {	
				for (int i = 0; i < 3; i++) {
					convexHull[i] = points[i];
				}
				convexHull[3] = convexHull[0];
			}
			else if (size > 3) {
				int minY = Integer.MAX_VALUE;
				int minIndex = 0;
				// finding the lowest Y point - with index minIndex
				for (int i = 0; i < size; i++) {
					if (points[i].y < minY || (points[i].y == minY && points[i].x < points[minIndex].x)) {
						minIndex = i;
						minY = points[i].y;
					}				
				}	
				PointComparator comparator = new PointComparator(points[minIndex]);
				
				convexHull[0] = points[minIndex]; 	//the first real point
				points[minIndex] = points[0];		//swap not to lose information
				points[0] = convexHull[0];			
				points[size] = points[0];		//close the points hull
				
				Arrays.sort(points, 1, size, comparator);
				
				convexHull[1] = points[1];
				convexHull[2] = points[2]; 
				
				int convHullInd = 1;  
				for (int i = 2; i <= size; i++) {
					while (leftTurn(convexHull[convHullInd - 1], convexHull[convHullInd], points[i]) >= 0 ){ 
						if (convHullInd == 1) {
							convexHull[convHullInd] = points[i]; 
							i++;
						} else {
							convHullInd--;
						}
					} 
					convHullInd++;
					convexHull[convHullInd] = points[i];
				}			
				
			}
			return convexHull;
		}		
	}
	
	public static ArrayList<Point> findConvexHull(ArrayList<Point> entrySet) {
		Point [] p = (Point[]) entrySet.toArray();
		Point [] result = findConvexHull(p);
		ArrayList<Point> al = new ArrayList<Point>(result.length);
		for (Point q : result) {
			al.add(q);
		}
		return al;		
	}

	public static int leftTurn(Point m1, Point m2, Point m3) {		
		return (m2.x - m1.x) * (m3.y - m1.y) - (m2.y - m1.y) * (m3.x - m1.x);
	}
	
	

}
