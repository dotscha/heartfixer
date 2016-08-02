public class Vector
{
	private double x,y,z;

	public Vector(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() { return x; }

	public double getY() { return y; }

	public double getZ() { return z; }

	public double get(int coord)
	{
		return coord==0 ? x : (coord==1 ? y : z);
	}


	public Vector add(Vector p)
	{
		return new Vector(x+p.x,y+p.y,z+p.z);
	}

	public Vector sub(Vector p)
	{
		return new Vector(x-p.x,y-p.y,z-p.z);
	}

	public double dist(Vector p)
	{
		return Math.sqrt(sqr(x-p.x)+sqr(y-p.y)+sqr(z-p.z));
	}

	public double length()
	{
		return Math.sqrt(x*x+y*y+z*z);
	}

	public double skal(Vector v)
	{
		return x*v.x + y*v.y + z*v.z;
	}

	public Vector norm()
	{
		return mul(1/length());
	}

	public Vector mul(double m)
	{
		return new Vector(x*m, y*m, z*m);
	}

	public Vector mul(Vector v)
	{
		return new Vector(y*v.z-z*v.y, z*v.x-x*v.z, x*v.y-y*v.x);
	}

	private static final double sqr(double x)
	{
		return x*x;
	}

	public int hashCode()
	{
		return  new Double(x*1000).intValue() ^
			new Double(y*1000).intValue() ^
			new Double(z*1000).intValue();
	}

	public boolean equals(Object o)
	{
		try
		{
			Vector p = (Vector)o;
			return dist(p)<0.00001;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
