package geomobjects;
import java.util.Comparator;

/** 
 * @author mateva
 * A point comparator for the convex hull calculations
 */

public class PointComparator implements Comparator<Point> {
	
	Point A;
	public PointComparator(Point somePoint) {
		this.A = somePoint;
	} 
	
	public double cosAN(Point N) throws IllegalArgumentException{
		double NAx = N.x - A.x;
		double NAy = N.y - A.y; // A has the lowest Y, so this is non-negative
		return NAx / Math.sqrt(NAx * NAx + NAy * NAy);
	}
	
	@Override
	public int compare(Point B, Point C) {
		double diff = cosAN(B) - cosAN(C);
		if (diff > 0)
			return 1;
		else if (diff < 0)
			return -1;		
		return 0;
	}

}
