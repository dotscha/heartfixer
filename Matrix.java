public class Matrix
{
	private double[] m;

	public Matrix()
	{
		m = new double[9];
		for(int k = 0; k<9; k++)
			m[k] =0;
	}


	public Matrix(double[] m)
	{
		this.m = m;
	}


	public static Matrix Id()
	{
		Matrix out = new Matrix();
		for(int k = 0; k<3; k++)
			out.set(k,k,1.0);
		return out;
	}


	public static Matrix rot(int a, int b, double angle)
	{
		Matrix out = Matrix.Id();
		out.set(a,a, Math.cos(angle));
		out.set(a,b, Math.sin(angle));
		out.set(b,a,-Math.sin(angle));
		out.set(b,b, Math.cos(angle));
		return out;
	}


	public double set(int x, int y, double v)
	{
		int i = x+3*y;
		double out = m[i];
		m[i] = v;
		return out;
	}


	public double get(int x, int y)
	{
		return m[x+3*y];
	}


	public Matrix mul(Matrix b)
	{
		Matrix a = this;
                Matrix out = new Matrix();
                for(int x = 0; x<3; x++)
                for(int y = 0; y<3; y++)
                {
                	double v = 0.0;
                	for(int xy = 0; xy<3; xy++)
                		v+= a.get(xy,y)*b.get(x,xy);
                	out.set(x,y,v);
                }
                return out;
	}

	public Matrix mul(double x)
	{
		Matrix out = new Matrix();
		for(int i = 3; i-->0;)
		for(int j = 3; j-->0;)
			out.set(i,j,get(i,j)*x);
		return out;
	}
}
