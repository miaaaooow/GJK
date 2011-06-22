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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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


public class Scene extends JFrame implements Runnable {
	
	/** CONSTANTS **/
	private static final long serialVersionUID = 1L;
	
	public final static int BOARDX 		= 1000;
	public final static int BOARDY 		= 600;
	private final static int BUTTONY 		= 60;
	private final static int POINT_SIZE 	= 10;
	private final static int MAX_POINTS_NUMBER = 100;
	
	private final static int FRAME_TIME = 1000;
	//private final static int SLEEP_LONG = 100000;
	
	private final static String FINALIZE_OBJECT = "FIN OBJ";
	private final static String PLAY_MOTION = "PLAY";
	private final static String STOP = "STOP";
	private final static String CLEAR_SCENE = "CLEAR SCENE";
	private final static Color [] colors = 
			{Color.BLUE, Color.GREEN, Color.MAGENTA, Color.GRAY, Color.PINK, Color.ORANGE} ;
	private final static Color INTERSECTED = Color.RED;

	
	/** private fields **/
	private Graphics background;
	private Image 	 backbuffer;
	private JPanel   buttons;
	private JPanel	 scene;
	private JTextField entryX;
	private JTextField entryY;
	
	private boolean startMode = true;
	
	private Point [] points = new Point [MAX_POINTS_NUMBER];
	private ArrayList<MovableObject> bodies = new ArrayList<MovableObject>();
	private Thread thread;
	
	private int pointIndex; // the points index to be filled
	private int bodiesIndex;
	
	
	public Scene() {
		super("Алгоритъм GJK");
		setLocation(150, 100);
		pointIndex = 0;	 
		bodiesIndex = 0;
		
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
		
	    addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          System.exit(0);
	        }
	      });
	    
		setSize(BOARDX, BOARDY + BUTTONY);	
		setVisible(true);
		setFocusable(true);
		requestFocus(); 
	}
	
	
	public void finalizeObject(String object){
		int dX, dY;
		/*fix form **/ 
		try {
			dX = Integer.parseInt(entryX.getSelectedText());
			dY = -1 * Integer.parseInt(entryY.getSelectedText());
		} catch (NumberFormatException nfe) {
			dX = 12; 
			dY = 12;
		}
		// CONVEX HULL
		MovableObject body = new MovableObject(points, dX, dY, colors[bodiesIndex % colors.length]);
		bodies.add(body);
		drawMovableObject(body);
		repaint();
		
	    points = new Point [MAX_POINTS_NUMBER];
	    pointIndex = 0;
	    bodiesIndex++;
	}
	
	
	
	private void drawMovableObject(MovableObject mo) {
		if (mo.isIntersected()){
			background.setColor(INTERSECTED);
		} else {
			background.setColor(mo.getColor());
		}
		ArrayList<Point> ap = mo.points;
		Iterator<Point> it = ap.iterator();
		Point prev = null;
		if (it.hasNext()) {
			Point first = it.next();
			background.fillRect(first.x - POINT_SIZE/2, first.y - POINT_SIZE/2, POINT_SIZE, POINT_SIZE);
			prev = first;
		}		
		while (it.hasNext()) {
			Point some = it.next();
			background.fillRect(some.x - POINT_SIZE/2, some.y  - POINT_SIZE/2, POINT_SIZE, POINT_SIZE);	 
			background.drawLine(prev.x, prev.y, some.x, some.y);
			prev = some;
		}
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
		
		thread = new Thread(this);
		
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
					thread.start();
	            } else if (action.equals(CLEAR_SCENE)) {
	            	clearScreen();
	            } else if (action.equals(STOP)) {
	            	thread.stop();
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
		addButton("Спри!", STOP, listener, jpb);
		//addButton("Изчисти!", CLEAR_SCENE, listener, jpb);
		
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

	public void start() {
		thread = new Thread(this);
	    thread.setPriority(Thread.MIN_PRIORITY);
	    thread.run();
	}

	public void stop() {
	    if (thread != null)
	      thread.interrupt();
	    thread = null;
	}

	public void run() {
	    Thread me = Thread.currentThread();
	    while (thread == me) {
	    	Iterator<MovableObject> it = bodies.iterator();
			while (it.hasNext()) {
				MovableObject mo = it.next();
				System.out.println(mo.toString());
				mo.move();
				drawMovableObject(mo);
			}
			try {
				Thread.sleep(FRAME_TIME);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			redraw();
			repaint();
	    }
	    thread = null;
	}
}

