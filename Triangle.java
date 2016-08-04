import java.util.*;

public class Triangle implements CTObject
{
	private Vector o,a,b;
	private double d;

	public Triangle(Vector va, Vector vb, Vector vc)
	{
		o = va;
		a = vb.sub(va);
		b = vc.sub(va);
		d = a.getX()*b.getY()-a.getY()*b.getX();
	}

	public Collection getIntersections(double x, double y)
	{
		x = x-o.getX();
		y = y-o.getY();
		if(d==0)
                	return Collections.EMPTY_LIST;
                double ca = (x*b.getY()-y*b.getX())/d;
                double cb = (a.getX()*y-a.getY()*x)/d;
                if(ca>=0 && cb>=0 && ca+cb<=1)
                	return Collections.singletonList(new Double(o.getZ()+ca*a.getZ()+cb*b.getZ()));
                else
                	return Collections.EMPTY_LIST;
	}

	public Vector getNormal()
	{
		return a.mul(b);
	}

	public double[] getMinMax(int coord)
	{
		double min = Math.min(o.get(coord),Math.min(o.get(coord)+a.get(coord),o.get(coord)+b.get(coord)));
		double max = Math.max(o.get(coord),Math.max(o.get(coord)+a.get(coord),o.get(coord)+b.get(coord)));
		return new double[]{min,max};
	}
}
