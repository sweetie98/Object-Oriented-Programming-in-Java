package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;


//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author sweetie98
 * Date: July 29, 2020
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// Processing.core.PApplet's color method to generate an integer that represents the respective colors .  
	int yellow = color(255, 255, 0);
    int blue = color(0, 0, 255);
    int red = color(255, 0, 0);

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500,  new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    //TODO: Add code here as appropriate
	    //Iterate over features of all earthquakes and adding respective markers to the list "markers"
	    if(earthquakes.size()>0) {
	    for (PointFeature pf : earthquakes) {
	    	markers.add(createMarker(pf));
	    }
	    // Add the markers to the map
	    map.addMarkers(markers);
	    }
	}
		
	// A suggested helper method that takes in an earthquake feature and 
    // returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{  
		// To print all of the features in a PointFeature 
		System.out.println(feature.getProperties());
		
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
	    
	    // Minor earthquakes (less than magnitude 4.0) will have blue markers and be small.
	    if (mag < THRESHOLD_LIGHT) {
	    	marker.setColor(blue);
	    	marker.setRadius(6);
	    }
	    // Light earthquakes (between 4.0-4.9) will have yellow markers and be medium size.
	    else if (mag >= THRESHOLD_LIGHT && mag <= 4.9) {
	    	marker.setColor(yellow);
	    	marker.setRadius(10);
	    }
	    // Moderate and higher earthquakes (5.0 and over) will have red markers and be largest.
	    else {
	    	marker.setColor(red);
	    	marker.setRadius(15);
	    }
	    return marker;
	}
	
	public void draw() {
	    background(30);
	    map.draw();
	    addKey();
	}
	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		fill(135,206,235);
		rect(20,50,170,200);
		fill(0,0,0);
		text("Earthquake Key", 50, 90);
		// Moderate and higher earthquakes (5.0 and over) red markers
		fill(255, 0, 0);
		ellipse(45, 120, 15, 15);
		fill(255);
		text("5.0+ Magnitude", 60, 125);
		// Light earthquakes (between 4.0-4.9) yellow markers
		fill(255, 255, 0);
		ellipse(45, 170, 10, 10);
		fill(255);
		text("4.0+ Magnitude", 60, 175);
		// Minor earthquakes (less than magnitude 4.0) blue markers
		fill(0, 0, 255);
		ellipse(46, 220, 6, 6);
		fill(255);
		text("Below 4.0", 60, 225);
		
	}
}