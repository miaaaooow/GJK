package geomobjects;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import ui.Scene;

/**
 * This class represents a moving body.
 * @author mateva
 *
 */

public class MovableObject {
	
	private static int delta = 10;
	
	private int limitX;
	private int limitY;
	
	private int deltaX;
	private int deltaY;
	/* Boundaries relative to the visible part of the screen */
	private int minX = Integer.MAX_VALUE;
	private int minY = Integer.MIN_VALUE;
	private int maxX = Integer.MAX_VALUE;
	private int maxY = Integer.MIN_VALUE;
	
	private Color color;
	
	public ArrayList<Point> points;
	
	public MovableObject (Point [] pointsArr, int dX, int dY, Color color) {
		ArrayList<Point> p = new ArrayList<Point>();
		int index = 0;
		int len = pointsArr.length;
		while (index < len && pointsArr[index] != null) {
			p.add(pointsArr[index++]);
		}
		
		if (color != null)
			this.color = color;
		else 
			this.color = Color.BLACK;
		
		limitX = Scene.BOARDX;
		limitY = Scene.BOARDY;
		points = p;
	}
	
	
	public MovableObject (ArrayList<Point> points, int dX, int dY, Point limit) {
		this.points = points;
		this.deltaX = dX;
		this.deltaY = dY;	
	}
	
	public ArrayList<Point> getPoints() {
		return this.points;
	}
	
	private void turnX() {
		deltaX = deltaX * (-1);
	}
	
	private void turnY() {
		deltaY = deltaY * (-1);
	}

	public void initialMinMax() {
		Iterator<Point> it = points.iterator();
		while (it.hasNext()) {
			Point p = it.next();
			if (p.x < minX) minX = p.x; 
			if (p.x > maxX) maxX = p.x;
			if (p.y < minY) minY = p.y;
			if (p.y > maxY) maxY = p.y;
		}
	}
	
	public void move() {
		Iterator<Point> it = points.iterator();
		while (it.hasNext()) {
			Point p = it.next();
			p.x += deltaX;
			p.y += deltaY;
			if (p.x < minX) 
				minX = p.x; 
			else if (p.x > maxX) 
				maxX = p.x;
			if (p.y < minY) 
				minY = p.y;
			else if (p.y > maxY) 
				maxY = p.y;
		}	
		checkForDirectionUpdates();
	}
	
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
