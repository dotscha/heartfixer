public class Point
{
	private double x,y,z;

	public Point(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double dist(Point p)
	{
		return Math.sqrt(sqr(x-p.x)+sqr(y-p.y)+sqr(z-p.z));
	}

	public double getX() { return x; }
	public double getY() { return y; }
	public double getZ() { return z; }

	private static final double sqr(double x)
	{
		return x*x;
	}

	public int hashCode()
	{
		return new Double(x).hashCode() ^ new Double(y).hashCode() ^ new Double(z).hashCode();
	}

	public boolean equals(Object o)
	{
		try
		{
			Point p = (Point)o;
			return dist(p)==0.0;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
