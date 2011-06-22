package algorithm;

import geomobjects.Point;

public class MathTools {
	/**
	 * AB.CD
	 */
	public static int vectorDotProduct(Point A, Point B, Point C, Point D) {
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
	
}
