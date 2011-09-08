import java.awt.*;
import java.applet.*;

public class LowPassFilter extends Applet{

	final int CYCLE = 3;
	final int DELTA = 100;
	final int NMAX  = CYCLE * DELTA;

	final int XORG     = 20;
	final int XLENGTH  = 600;
	final int XMARGIN  = 10;
	final float XCYCLE = XLENGTH / CYCLE;
		
	final int YLENGTH  = 50;
	final int YNOISE   = 20;
	final int YSTART0  = 20;
	final int YMARGIN  = 10;

	final String TITLE0  = "Sin curve & Noise";
	final String TITLE1  = "1: y[i] = ( x[i] Å{ x[i-1] + x[i-2] ) / 3";
	final String TITLE2  = "2: y[i] = 0.9*y[i-1] Å{ 0.1*x[i]";
		
	double [] xx = new double[NMAX+10];
	double [] yy = new double[NMAX+10];
	double [] nn = new double[NMAX+10];

	int [] x  = new int[NMAX+10];
	int [] y  = new int[NMAX+10];
	int [] y1 = new int[NMAX+10];
	int [] y2 = new int[NMAX+10];
		
	public void paint(Graphics g)
	{
		calcSinNoise( NMAX, XCYCLE, (float)YLENGTH, (float)YNOISE, (float)DELTA );
		paintSin(g, x, y, NMAX, XORG, YSTART0, TITLE0);

		calcFilter1( NMAX );
		int ystart1 = calcYend( YSTART0 ) + YSTART0;
		paintSin(g, x, y1, NMAX, XORG, ystart1, TITLE1);
			
		calcFilter2( NMAX );
		int ystart2 = calcYend( ystart1 ) + YSTART0;
		paintSin(g, x, y2, NMAX, XORG, ystart2, TITLE2);
	}

	private void paintSin(Graphics g, int x[], int y[], int nmax, int xorg, int ystart, String str )
	{
		int xend = calcXend( xorg );
		int yorg = calcYorg( ystart );
		int yend = calcYend( ystart );

		int xtitle = xend / 3;
		int ytitle = yend;
		
		int x0 = xorg;
		int y0 = yorg;
		int x1 = xorg;
		int y1 = yorg;

		// plot axia
		g.setColor( Color.black);
		g.drawLine(xorg, ystart, xorg, yend);
		g.drawLine(xorg, yorg,   xend, yorg);
			g.drawString(str, xtitle, ytitle);
			
		// plot graph
		g.setColor( Color.blue);

		for (int i = 0; i <= nmax; i++){
			x0 = x1; 
			y0 = y1;
			x1 = xorg + x[i];
			y1 = yorg + y[i];
			g.drawLine(x0, y0, x1, y1);
		}
	}

	private int calcXend( int xorg )
	{
		int x = xorg + XLENGTH + XMARGIN;
		return x;
	}

	private int calcYorg( int ystart )
	{
		int y = ystart + YMARGIN + YLENGTH + YNOISE;
		return y;
	}

	private int calcYend( int ystart )
	{
		int y = ystart + 2*( YMARGIN + YLENGTH + YNOISE );
		return y;
	}

	private void calcSinNoise( int nmax, float xcycle, float ylength, float ynoise, float delta )
	{
		calcSin( nmax, delta );
		calcNoise( nmax );

		for (int i = 0; i <= nmax; i++){
			x[i] = (int)(xcycle * xx[i]);
			y[i] = (int)(ylength * yy[i]) + (int)(ynoise * nn[i]);
		}
	}

	private void calcSin( int nmax, float delta )
	{
		double d = 0;
			
		for (int i = 0; i <= nmax; i++){
			d  = i / delta ;
			xx[i] = d;
			yy[i] = Math.sin( d * 2 * Math.PI );
		}
	}
		
	private void calcNoise( int nmax )
	{
		for (int i = 0; i <= nmax; i++){
			nn[i] = Math.random() - 0.5;
		}
	}

	// y[i] = ( x[i] Å{ x[i-1] + x[i-2] ) / 3
	private void calcFilter1( int nmax )
	{
		for (int i = 0; i <= nmax - 2; i++){
			y1[i] = (int)( (y[i] + y[i+1] + y[i+2] ) / 3 );
		}
	}

	// y[i] = 0.9*y[i-1] Å{ 0.1*x[i]
	private void calcFilter2( int nmax )
	{
		y2[0] = 0;
		for (int i = 0; i <= nmax - 1; i++){
			y2[i+1] = (int)( 0.9 * y2[i] + 0.1 * y[i] );
		}
	}

}
