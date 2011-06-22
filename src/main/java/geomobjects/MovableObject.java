package geomobjects;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import algorithm.ConvexHull;

import ui.Scene;

/**
 * This class represents a moving body.
 * @author mateva
 *
 */

public class MovableObject {
	
	private int limitX;
	private int limitY;
	
	private int deltaX;
	private int deltaY;
	/* Boundaries relative to the visible part of the screen */
	private int minX = Integer.MAX_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int maxY = Integer.MIN_VALUE;
	
	private Color color;
	private boolean intersected;
	
	public ArrayList<Point> points;

	
	public MovableObject (Point [] pointsArr, int dX, int dY, Color color) {
		Point [] temp = ConvexHull.findConvexHull(pointsArr);
		ArrayList<Point> p = new ArrayList<Point>();
		int index = 0;
		int len = temp.length;
		while (index < len && temp[index] != null) {
			p.add(temp[index++]);
		}
		
		if (color != null)
			this.color = color;
		else 
			this.color = Color.BLACK;
		
		deltaX = dX;
		deltaY = dY;
		limitX = Scene.BOARDX;
		limitY = Scene.BOARDY;
		points = p;
	}
	
	
	
	public ArrayList<Point> getPoints() {
		return this.points;
	}
	
	public Color getColor () {
		return this.color;
	}
	
	public boolean isIntersected() {
		return intersected;
	}

	public void setIntersected(boolean intersected) {
		this.intersected = intersected;
	}
	
	private void turnX() {
		deltaX = deltaX * (-1);
	}
	
	private void turnY() {
		deltaY = deltaY * (-1);
	}

	private void initializeMinMax() {
		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		maxY = Integer.MIN_VALUE;
	}
	
	/**
	 * Recalculates the object's position on the scene
	 */
	public void move() {
		initializeMinMax();
		Iterator<Point> it = points.iterator();
		Point p = null;
		while (it.hasNext()) {
			p = it.next();
			p.x += deltaX;
			p.y += deltaY;
			if (p.x < minX) 
				minX = p.x; 
			if (p.x > maxX) 
				maxX = p.x;
			if (p.y < minY) 
				minY = p.y;
			if (p.y > maxY) 
				maxY = p.y;
		}	
		// This is not very beautiful but the first and the last point in the convex hull are the same, 
		// which makes some tricks easy, in the price of the tiny trick.
		if (p != null) {
			p.x -= deltaX;
			p.y -= deltaY;
		}
		checkForDirectionUpdates();
	}
	
	/**
	 * Prevents the object from going out of the scene
	 */
	private void checkForDirectionUpdates () {
		if (minX + deltaX < 0 || maxX + deltaX > limitX)
			turnX();
		if (minY + deltaY < 0 || maxY + deltaY > limitY) {
			turnY();
		}
	}
	
	public String toString() {
		return "Movable Object @[" + minX + ", " + minY + "] and deltas dX = " + deltaX + ", dY = " + deltaY; 
	}
		
}
