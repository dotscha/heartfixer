import java.io.*;
import java.util.*;

public class VRLMObject
{
	public static void main(String[] args)
		throws IOException
	{
		VRLMObject out = new VRLMObject(args[0]);
		out.getTriangles();
	}


	private ArrayList points, facets;

	public VRLMObject(String file)
		throws IOException
	{

		InputStream is = new FileInputStream(file);
		if(file.toLowerCase().endsWith("z"))
			is = new java.util.zip.GZIPInputStream(is);
		StreamTokenizer st = new StreamTokenizer(is);

		st.parseNumbers();
		st.lowerCaseMode(true);
		st.wordChars(']',']');

		points = new ArrayList();
		facets = new ArrayList();

		int mode = 0;
		double[] coords = new double[3];
		int ci = 0;
		ArrayList vList = new ArrayList();
                while(st.nextToken()!=st.TT_EOF)
                {
			if(st.ttype==st.TT_WORD)
			{
				if(st.sval.equals("point"))
					mode = 1;
				if(st.sval.equals("coordindex"))
					mode = 2;
				if(st.sval.equals("]"))
					mode = 0;
			}
			if(st.ttype==st.TT_NUMBER)
			{
				if(mode==1)
				{
					coords[ci++] = st.nval;
					if(ci==3)
					{
						points.add(coords);
						coords = new double[3];
						ci = 0;
					}
				}
				if(mode==2)
				{
					int vi = (int)st.nval;
					if(vi==-1)
					{
						vList.trimToSize();
						facets.add(vList);
						vList = new ArrayList();
					}
					else
						vList.add(new Integer(vi));
				}
			}
                }
                points.trimToSize();
                facets.trimToSize();
	}


	public List getTriangles()
	{
		ArrayList out = new ArrayList();
		Iterator it = facets.iterator();
		while(it.hasNext())
		{
			List fl = (List)it.next();
			Vector v0 = getVector(fl,0);
			Vector v1 = getVector(fl,1), v2;
			for(int k = 2; k<fl.size(); k++)
			{
                        	v2 = getVector(fl,k);
                        	out.add(new Triangle(v0,v1,v2));
                        	v1 = v2;
			}
		}
		out.trimToSize();
		return out;
	}


	private Vector getVector(List l, int i)
	{
		int vi = ((Integer)l.get(i)).intValue();
		double c[] = getVector(vi);
		return new Vector(c[0],c[1],c[2]);
	}


	public void transform(Matrix m)
	{
		for(int k = points(); k-->0; )
		{
			double c[] = getVector(k);
                        double x = c[0]*m.get(0,0)+c[1]*m.get(0,1)+c[2]*m.get(0,2);
                        double y = c[0]*m.get(1,0)+c[1]*m.get(1,1)+c[2]*m.get(1,2);
                        double z = c[0]*m.get(2,0)+c[1]*m.get(2,1)+c[2]*m.get(2,2);
                        c[0] = x;
                        c[1] = y;
                        c[2] = z;
		}
	}


	public double[] getVector(int i)
	{
		return (double[])points.get(i);
	}


	public int points()
	{
		return points.size();
	}


	public int facets()
	{
		return facets.size();
	}


	public void fitIntoBox(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax)
	{
		double[] min = new double[3];
		double[] max = new double[3];
		for(int c = 0; c<3; c++)
		{
			min[c] = Double.POSITIVE_INFINITY;
			max[c] = Double.NEGATIVE_INFINITY;
		}
		for(int k = points(); k-->0; )
		{
			double[] p = getVector(k);
			for(int i = 0; i<3; i++)
			{
				min[i] = Math.min(p[i],min[i]);
				max[i] = Math.max(p[i],max[i]);
			}
		}
		double c = Math.min((xmax-xmin)/(max[0]-min[0]),(ymax-ymin)/(max[1]-min[1]));
		c = Math.min(c,(zmax-zmin)/(max[2]-min[2]));
		double cx = (xmin+xmax-c*(min[0]+max[0]))/2;
		double cy = (ymin+ymax-c*(min[1]+max[1]))/2;
		double cz = (zmin+zmax-c*(min[2]+max[2]))/2;
		for(int k = points(); k-->0; )
		{
			double[] p = getVector(k);
			p[0] = cx + c*p[0];
			p[1] = cy + c*p[1];
			p[2] = cz + c*p[2];
			//p[2] = (p[2]-min[2])/(max[2]-min[2])*(zmax-zmin)+zmin;
		}
	}
}
