package ui;

/**
 * GJK
 * 
 * Main UI class and program start point.
 * 
 * @author Maria Mateva, FMI, Sofia University
 */

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import geomobjects.MovableObject;
import geomobjects.Point;


public class Scene extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public final static int BOARDX 		= 1000;
	public final static int BOARDY 		= 600;
	private final static int BUTTONY 		= 60;
	private final static int POINT_SIZE 	= 10;
	private final static int MAX_POINTS_NUMBER = 100;
	
	private final static int timeFrame = 10;
	
	private final static String FINALIZE_OBJECT = "FIN OBJ";
	private final static String PLAY_MOTION = "PLAY";
	private final static String PAUSE = "PAUSE";
	private final static String CLEAR_SCENE = "CLEAR SCENE";
	private final static Color [] colors = 
			{Color.BLUE, Color.GREEN, Color.RED, Color.BLACK, Color.PINK, Color.ORANGE} ;

	private Graphics background;
	private Image 	 backbuffer;
	private JPanel   buttons;
	private JPanel	 scene;
	private JTextField entryX;
	private JTextField entryY;
	
	private boolean startMode = true;
	
	private Point [] points = new Point [MAX_POINTS_NUMBER];
	private ArrayList<MovableObject> bodies = new ArrayList<MovableObject>();
	
	private int pointIndex; // the points index to be filled
	private int bodiesIndex;
	private boolean movement;
	
	public Scene() {
		super("Алгоритъм GJK");
		setLocation(150, 100);
		pointIndex = 0;	 
		bodiesIndex = 0;
		movement = false;
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) { 
	            int x1 = me.getX();
	            int y1 = me.getY() - BUTTONY;     
	            
	    	    background.setColor(colors[bodiesIndex % colors.length]);
            	Point p = new Point(x1, y1);
            	background.fillRect(x1 - POINT_SIZE/2, y1 - POINT_SIZE/2, POINT_SIZE, POINT_SIZE);	            	
            	points[pointIndex] = p;            
            	pointIndex++;
            
            	repaint();
            	me.consume();
	        }	            
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(BOARDX, BOARDY + BUTTONY);	
		setVisible(true);
		setFocusable(true);
		requestFocus(); 
	}
	
	
	public void finalizeObject(String object){
		int dX = 120;
		int dY = 120;
//		try {
//			dX = Integer.parseInt(entryX.getSelectedText());
//			dY = Integer.parseInt(entryY.getSelectedText());
//		} catch (NumberFormatException nfe) {
//			dX = 120; 
//			dY = 120;
//		}
		// CONVEX HULL
		MovableObject body = new MovableObject(points, dX, dY, colors[bodiesIndex % colors.length]);
		bodies.add(body);
	    points = new Point [MAX_POINTS_NUMBER];
	    pointIndex = 0;
	    bodiesIndex++;
	}
	
	public void performMovement() {
		movement = true;
		 
		while (movement) {
			Iterator<MovableObject> it = bodies.iterator();
			while (it.hasNext()) {
				MovableObject mo = it.next();
				System.out.println(mo.toString());
				mo.move();
				drawMovableObject(mo);
			}
//			try {
//				Thread.sleep(1700);
//			} catch (InterruptedException ie) {
//				ie.printStackTrace();
//			}
			redraw();
		}		
	}
	
	public void pause () {
		movement = false;
	}
	
	private void drawMovableObject(MovableObject mo) {
		background.setColor(mo.getColor());
		ArrayList<Point> ap = mo.points;
		Iterator<Point> it = ap.iterator();
		Point prev = null;
		if (it.hasNext()) {
			Point first = it.next();
			background.fillRect(first.x - POINT_SIZE/2, first.y + BUTTONY - POINT_SIZE/2, POINT_SIZE, POINT_SIZE);
			prev = first;
		}		
		while (it.hasNext()) {
			Point some = it.next();
			background.fillRect(some.x - POINT_SIZE/2, some.y + BUTTONY - POINT_SIZE/2, POINT_SIZE, POINT_SIZE);	 
			background.drawLine(prev.x, prev.y + BUTTONY, some.x, some.y + BUTTONY);
			prev = some;
		}
		System.out.println("draw movable end");
	}
	
	
	public void clearScreen () {
		System.out.println("CLEAR!");
	    redraw();
	    repaint();
	    points = new Point [MAX_POINTS_NUMBER];
	    pointIndex = 0;
	    bodies = new ArrayList<MovableObject>();	
	}

	protected void redraw() {
	    background.setColor(Color.WHITE);
	    background.fillRect(0, 0, BOARDX, BOARDY + BUTTONY);
	    background.setColor(Color.BLACK);
	}
	
	public void paint(Graphics g) {
	    if (startMode)
	    	initGraphics(this.getContentPane());
	    g.drawImage(backbuffer, 0, BUTTONY, scene);
	}
	
	
	private void initGraphics (Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));		
		backbuffer = createImage(BOARDX, BOARDY);
		background = backbuffer.getGraphics();
		redraw();
		
	    buttons = getButtonPanel();	    
	    scene = new JPanel();
		scene.setSize(BOARDX, BUTTONY);		
		pane.add(buttons);
		pane.add(scene);
		
		startMode = false;
	}
	
	private JPanel getButtonPanel() {
		JPanel jpb = new JPanel();
		jpb.setSize(BOARDX, BUTTONY);
		ActionListener listener = new ActionListener() {						
			@Override
			public void actionPerformed(ActionEvent e) {
				String action = e.getActionCommand();
				if (action.equals(FINALIZE_OBJECT) ) {
					finalizeObject(action);   	
				} else if (action.equals(PLAY_MOTION)) {
					performMovement();
	            } else if (action.equals(CLEAR_SCENE)) {
	            	clearScreen();
	            } else if (action.equals(PAUSE)) {
	            	pause();
	            }
			}
		};		
		entryX = new JTextField();
		entryX.setColumns(5);
		entryY = new JTextField();
		entryY.setColumns(5);
		JLabel jl1 = new JLabel("dX= ");
		JLabel jl2 = new JLabel("dY= ");
		jpb.add(jl1); 
		jpb.add(entryX);
		jpb.add(jl2);
		jpb.add(entryY);
		
		addButton("Завърши обект", FINALIZE_OBJECT, listener, jpb);
		addButton("Раздвижи!", PLAY_MOTION, listener, jpb);
		addButton("Пауза", PAUSE, listener, jpb);
		addButton("Изчисти!", CLEAR_SCENE, listener, jpb);
		
		return jpb;
	}
	
	private void addButton(String name, String caughtAction, ActionListener listener, JPanel container) {
		JButton button = new JButton(name);
		button.setActionCommand(caughtAction);
		button.addActionListener(listener);
		container.add(button);		
	}
	
	public static void main(String[] args) {
		new Scene();	
	}
}

