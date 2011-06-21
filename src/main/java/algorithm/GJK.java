package algorithm;


import geomobjects.Point;

import java.util.ArrayList;
import java.util.Iterator;

public class GJK {
	
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

}
