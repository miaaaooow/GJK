package geomobjects;

/**
 * @author mateva
 * Simple object representing a point.
 */
public class Point {
	public int x;
	public int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "Point(" + this.x + ", " + this.y + ")";
	}
}
