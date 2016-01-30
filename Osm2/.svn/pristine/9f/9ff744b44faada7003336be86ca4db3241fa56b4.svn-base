package jp.ohwada.android.osm2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

/*
 * GpsItemizedOverlay
 */
public class GpsItemizedOverlay extends ItemizedOverlay<OverlayItem> {
		
	// Longitude 0.01 degree is equivalent to 1.0 km. 
	// A small value influence the drawing performance
	// A large value must be satisfactory. 
    private final static double LONGITUDE_RADIUS = 0.01;

	// color
    private final static int COLOR_FILL = Color.argb( 0x22, 0x33, 0x99, 0xFF );
    private final static int COLOR_STROKE = Color.argb( 0xAA, 0x00, 0x66, 0xFF );

	// variable
	private Location mLocation;
	private int mAccuracy = 0;
		
	/**
	 * === Constructor ===
	 * @param Drawable marker
	 */
	public GpsItemizedOverlay( Drawable marker ) {
        super( boundCenter( marker ) );
		populate();
	}

	/**
	 * --- create the actual Items ---
	 * @param int : The number of a point_loc 
	 */
	@Override
	protected OverlayItem createItem( int i ) {
		GeoPoint point = new GeoPoint( 
			doubleToE6( mLocation.getLatitude() ), 
			doubleToE6( mLocation.getLongitude() ) );
		return new OverlayItem( point, null, null );
	}

	/**
	 * --- size ---
	 * @return int : the number of point_locs
	 */
	@Override public int size() { 
		return mLocation == null ? 0 : 1; 
	}

	/**
	 * draw the circle showing an accuracy 
	 * @param Canvas canvas
	 * @param MapView map
	 * @param boolean shadow
	 */
	@Override
	public void draw( Canvas canvas, MapView map, boolean shadow ) {
		if ( shadow ) return;
		if ( mLocation != null ) {
			float accuracy = 0;
			if ( mLocation.hasAccuracy() ) {
				accuracy = mLocation.getAccuracy();
			} else {
				if ( mAccuracy > 0 ) {
					accuracy = (float) mAccuracy ;
				} else {	
					return ;
				}	
			}	
			//	calculate the radius distance from the present location
			double loc_lat = mLocation.getLatitude();
			double loc_lng = mLocation.getLongitude();			
			double circle_lng = loc_lng + LONGITUDE_RADIUS;
			float[] results = new float[1];
			Location.distanceBetween( loc_lat, loc_lng, loc_lat, circle_lng, results );
			float radius_distance = results[0];
			// calculate the dadius pixels from present location
			int loc_lat_e6 = doubleToE6( loc_lat );
			GeoPoint geo_loc = new GeoPoint( loc_lat_e6, doubleToE6( loc_lng ) );
			GeoPoint geo_circle = new GeoPoint( loc_lat_e6, doubleToE6( circle_lng ) );
			Projection projection = map.getProjection();
			Point point_loc = projection.toPixels( geo_loc, null );
			Point point_circle = projection.toPixels( geo_circle, null );
			int radius_pixels = Math.abs( point_loc.x - point_circle.x );
			if ( radius_pixels > 0 ) {
				// calculate the radius to draw 
				float radius_draw = accuracy * radius_pixels / radius_distance ;
				// draw circle
				Paint paint = new Paint( Paint.ANTI_ALIAS_FLAG );
				paint.setStyle( Paint.Style.FILL );
				paint.setColor( COLOR_FILL );
				canvas.drawCircle( point_loc.x, point_loc.y, radius_draw, paint );
				paint.setStyle( Paint.Style.STROKE );
				paint.setColor( COLOR_STROKE );
				canvas.drawCircle( point_loc.x, point_loc.y, radius_draw, paint );
			}
		}
		super.draw( canvas, map, shadow );
	}

	/**
	 * setDefaultAccuracy
	 * @param  int accuracy
	 */
	public void setDefaultAccuracy( int accuracy ) {
		mAccuracy = accuracy;
	}
	
	/**
	 * setLocation
	 * @param Location location
	 */
	public void setLocation( Location location ) {
		mLocation = location;
		populate();
	}

	/**
	 * convert real number to integer
	 * @param Double : location( floating point_loc format )
	 * @return int : location( E6 format )
	 */
    private int doubleToE6( Double d1 ) {
		Double d2 = d1 * 1E6;
		return d2.intValue();
	}
	
}
