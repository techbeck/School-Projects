// CS 0401 Fall 2015
// Outline of the MyPoly class that you must implement for Assignment 5.
// I have provided some data and a couple of methods for you, plus some method headers
// for the methods called from Assig5.java.  You must implement all of those methods but 
// you will likely want to add some other "helper" methods as well, and also some new
// instance variables (esp. for Assig5B.java).

import java.util.*;
import java.awt.*;
import java.awt.geom.*;

public class MyPoly extends Polygon
{
	 // This ArrayList is how we will "display" the points in the MyPoly.  The idea is
	 // that a circle will be created for every (x,y) point in the MyPoly.  To give you
	 // a good start on this, I have implemented the constructors below.
	 private ArrayList<Ellipse2D.Double> thePoints;
	 private ArrayList<Boolean> pointsHighlighted;
	 private Color myColor;
	 private boolean highlighted;
	 
	 // Constructors.  These should work with Assig5.java but you may want to modify
	 // them for Assig5B.java.
	MyPoly()
	{
		super();
		myColor = Color.BLACK;
		thePoints = new ArrayList<Ellipse2D.Double>();
		pointsHighlighted = new ArrayList<Boolean>();
	}

	// This constructor should be a lot of help to see the overall structure of the
	// MyPoly class and how both the inherited Polygon functionality as well as the
	// additional functionality are incorporated.  Note that the first thing done is
	// a call to super to set up the points in the "regular" Polygon.  Then the color
	// is set and the point circles are created to correspond with each point in the
	// Polygon.
	MyPoly(int [] xpts, int [] ypts, int npts, Color col)
	{
		super(xpts, ypts, npts);
		myColor = col;
		thePoints = new ArrayList<Ellipse2D.Double>();
		pointsHighlighted = new ArrayList<Boolean>();
		for (int i = 0; i < npts; i++)
		{
			int x = xpts[i];
			int y = ypts[i];
			addCircle(x, y);
		}
	}
	
	// The setFrameFromCenter() method in Ellipse2D.Double allows the circles to be
	// centered on the points in the MyPoly
	public void addCircle(int x, int y)
	{
		Ellipse2D.Double temp = new Ellipse2D.Double(x, y, 8, 8);
		temp.setFrameFromCenter(x, y, x+4, y+4);
		thePoints.add(temp);
		pointsHighlighted.add(false);
	}
    
	// Must override this method to translate point circles along with Polygon
	public void translate(int deltaX, int deltaY)
	{
		// translate the superclass polygon vertices
		super.translate(deltaX,deltaY);
		for (Ellipse2D.Double e : thePoints)
		{
			// add deltaX to the correct x coordinate for each point circle
			e.setFrame(e.getX() + deltaX, e.getY() + deltaY, e.getWidth(), e.getHeight());
		}
	}
    
    // This method is so simple I just figured I would give it to you. 	   
	public void setHighlight(boolean b)
	{
		highlighted = b;
	}
    
    // Must override this method to add a new point circle along with new point on Polygon
	public void addPoint(int x, int y)
	{
		super.addPoint(x,y);
		addCircle(x,y);
	}
     
    // Return a new MyPoly containing new point (x,y) inserted between the two points
	// in the MyPoly that it is "closest" to.
	public MyPoly insertPoint(int x, int y)
	{
		int[] tempX = new int[npoints+1];
		int[] tempY = new int[npoints+1];
		int index = getClosest(x,y);
		int from = 0;
		for (int to = 0; to < npoints + 1; to++)
		{
			if (to == index + 1)
			{
				tempX[to] = x;
				tempY[to] = y;
			} 
			else
			{
				tempX[to] = xpoints[from];
				tempY[to] = ypoints[from];
				from++;
			}
		}
		MyPoly tempPoly = new MyPoly(tempX, tempY, npoints + 1, myColor);
		// to keep polygon selected, set newly created MyPoly highlighted before returning.
		tempPoly.setHighlight(true);
		return tempPoly;
	}
	
	// This method will return the index of the first point of the line segment that is
	// closest to the argument (x, y) point.  It uses some methods in the Line2D.Double
	// class and will be very useful when adding a point to the MyPoly.  Read through it
	// and see if you can figure out exactly what it is doing.
	public int getClosest(int x, int y)
	{
		if (npoints == 1)
			return 0;
		else
		{
			Line2D currSeg = new Line2D.Double(xpoints[0], ypoints[0], xpoints[1], ypoints[1]);
			double currDist = currSeg.ptSegDist(x, y);
			double minDist = currDist;
			int minInd = 0;
			for (int ind = 1; ind < npoints; ind++)
			{
				currSeg = new Line2D.Double(xpoints[ind], ypoints[ind],
								xpoints[(ind+1)%npoints], ypoints[(ind+1)%npoints]);
				currDist = currSeg.ptSegDist(x, y);
				if (currDist < minDist)
				{
					minDist = currDist;
					minInd = ind;
				}
			}
			return minInd;
		}
	}

	// Return a new MyPoly without the selected point. If selected point is not within
	// a point circle, original MyPoly will be returned. If selected point is last point,
	// null object will be returned.
	public MyPoly removePoint(int x, int y)
	{
		boolean contained = false;
		int index = 0;
		for (int i = 0; i < npoints; i++)
		{
			if (thePoints.get(i).contains(x,y))
			{
				if (npoints == 1)
				{
					return null;
				}
				contained = true;
				index = i;
				break;
			}
		}
		if (!contained)
		{
			return this;
		}
		int[] tempX = new int[npoints-1];
		int[] tempY = new int[npoints-1];
		int to = 0;
		boolean found = false;
		for (int from = 0; from < npoints; from++)
		{
			if (to == index && !found)
			{
				found = true;
			} 
			else
			{
				tempX[to] = xpoints[from];
				tempY[to] = ypoints[from];
				to++;
			}
		}
		MyPoly tempPoly = new MyPoly(tempX, tempY, npoints - 1, myColor);
		// to keep polygon selected, set newly created MyPoly highlighted before returning.
		tempPoly.setHighlight(true);
		return tempPoly;
	}

	// If 3 or more points, super method works. If only 1 point, must be within point
	// circle. If 2 points, must be within threshold distance of the line.
	public boolean contains(int x, int y)
	{
		if (npoints >=3)
		{
			return super.contains(x,y);
		}
		if (npoints == 2)
		{
			Line2D tempSeg = new Line2D.Double(xpoints[0],ypoints[0],xpoints[1],ypoints[1]);
			// Threshold chosen to be 4.0 because it is radius of point circles.
			double threshold = 4.0;
			if (tempSeg.ptSegDist(x,y) < threshold)
			{
				return true;
			}
		}
		if (npoints == 1)
		{
			for (Ellipse2D.Double e : thePoints)
			{
				if (e.contains(x,y))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	// Draw the MyPoly onto the Graphics2D argument g. When polygon is selected, 
	// the point circles will be drawn.
	public void draw(Graphics2D g)
	{
		// Implement this method to draw the MyPoly onto the Graphics2D argument g.
		// See MyRectangle2D.java for a simple example of doing this.  In the case of
		// this MyPoly class the method is more complex, since you must handle the
		// special cases of 1 point (draw only the point circle), 2 points (drow the
		// line) and the case where the MyPoly is selected.  You must also use the
		// color of the MyPoly in this method.

		// Both outline and filled in Polygon will be set color
		g.setColor(myColor);
		// If mouse is over point circle, it will be filled in.
		for (int i = 0; i < npoints; i++)
		{	
			if (pointsHighlighted.get(i))
			{
				g.fill(thePoints.get(i));
			}
		}
		// If only one point present, point circle will always be displayed
		if (npoints == 1) {
			g.draw(thePoints.get(0));
			return;
		}
		// If polygon selected, point circles will be displayed and outline will be drawn
		if (highlighted)
		{
			for (Ellipse2D.Double e : thePoints)
			{
				g.draw(e);
			}
		}
		else
		{
			g.fill(this);
		}
		g.draw(this);
	}

	public int checkIfXYInPointCircle(int x, int y)
	{
		for (int i = 0; i < npoints; i++)
		{
			if (thePoints.get(i).contains(x,y))
			{
				return i;
			}
		}
		return -1;
	}

	public void highlightPointCircle(int index)
	{
		if (index >= 0)
		{
			pointsHighlighted.set(index, true);
		}
		else
		{
			for (int i = 0; i < npoints; i++)
			{
				pointsHighlighted.set(i, false);
			}
		}
	}
	  
	public String fileData()
	{
		// Implement this method to return a String representation of this MyPoly
		// so that it can be saved into a text file.  This should produce a single
		// line that is formatted in the following way:
		// x1:y1,x2:y2,x3:y3, ... , |r,g,b
		// Where the points and the r,g,b values are separated by a vertical bar.
		// For two examples, see A5snap.htm and A5Bsnap.htm.
		// Look at the Color class to see how to get the r,g,b values.
		StringBuilder data = new StringBuilder();
		for (int i = 0; i < npoints - 1; i++)
		{
			data.append(xpoints[i] + ",");
			data.append(ypoints[i] + ":");
		}
		data.append(xpoints[npoints - 1] + ",");
		data.append(ypoints[npoints - 1] + "|");
		data.append(myColor.getRed() + ",");
		data.append(myColor.getGreen() + ",");
		data.append(myColor.getBlue());
		return data.toString();
	}

	// These methods are also so simple that I have implemented them.
	public void setColor(Color newColor)
	{
		myColor = newColor;
	}	
	
	public Color getColor()
	{
		return myColor;
	}		
}