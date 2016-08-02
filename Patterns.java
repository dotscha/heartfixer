import java.util.*;

public class Patterns
{
	private static String[]
		colors1 = {"$07","$17","$27","$37","$47","$57","$67","$77"},
		colors2 = {"$77","$57","$37","$17","$07","$25","$45","$65"},
		colors3 = {"$76","$66","$56","$46","$36","$26","$16","$06",
			   "$05","$15","$25","$35","$45","$55","$65","$75"},
		colors4 = {"$75","$65","$55","$45","$35","$25","$15","$05",
			   "$06","$16","$26","$36","$46","$56","$66","$76"},
		colors7 = {"$0","$1","$2","$3","$4","$5","$6","$7",
			   "$8","$9","$a","$b","$c","$d","$e","$f"},
		//4x4 avr:
		colors5 = {"$10","$11","$12","$13","$14","$15","$16","$17",
			   "$18","$19","$1a","$1b","$1c","$1d","$1e","$1f"},
		colors6 = {"$10","$11","$13","$14","$16","$17","$19","$1a",
			   "$1c","$1c","$1d","$1d","$1e","$1e","$1f","$1f"};

	public static void main(String[] args)
	{
		//Set points = thetraedron();
		//Set points = dodekoedron();
		//Set points = filotaxis(20);

		Set points = icosaedron();
		int[][] patt = pointSetDistPattern(128,64,points,25);
		//int[][] patt = simplePattern128x32();
		//int[][] patt = tunelPattern128x32();

		//postproc(patt);
		print(patt,colors7,4,2);

		//print(patt,colors5,false);
	}


	public static int[][] tunelPattern128x32()
	{
		int[] sin = new int[]{0,0,0,2,4,6,8,8,8,8,8,6,4,2,0,0};
		int[][] out = new int[32][];
		for(int y = 0; y<out.length; y++)
		{
			double dist = Math.tan((31.5-y)/32.0*Math.PI/2);
			double power = 1/(1+dist*dist/10);
			int d = (int)(dist/(2*Math.PI/128)+8);
			int[] line = out[y] = new int[128];
			for(int x = 0; x<line.length; x++)
			{
				line[x] = (int)(0.99*power*(sin[x%16]+sin[d%16]));
			}
		}
		return out;
	}


	public static int[][] simplePattern128x32()
	{
		int[] c = new int[]
		//	{15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0,
		//	 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
			{15,13,11,9,7,5,3,0,0,3,5,7,9,11,13,15,
			 15,13,11,9,7,5,3,0,0,3,5,7,9,11,13,15};
                /*
		for(int k = 0; k<c.length; k++)
		{
			c[k] = (int)(c[k]*c[k]/15.0);
		}
		/**/
		int[][] out = new int[32][];
		for(int y = 0; y<out.length; y++)
		{
			int[] line = out[y] = new int[128];
			for(int x = 0; x<line.length; x++)
			{
				line[x] = Math.max(c[x%32],c[y%32]);
			}
		}
		return out;
	}


	public static void postproc(int[][] p)
	{
		for(int y = 0; y<p.length; y++)
		{
			int[] line = p[y];
			for(int x = 0; x<line.length; x++)
			{
				line[x] = (int)(line[x]*line[x]/15.0);
			}
		}
	}


	public static void print(int[][] patt)
	{
		for(int y = 0; y<patt.length; y++)
		{
			System.out.print(";");
			for(int x = 0; x<patt[y].length; x++)
				System.out.print(patt[y][x]);
			System.out.println();
		}
	}


	public static void print(int[][] patt, String[] colors, int divx, int divy)
	{
		for(int y = 0; y<patt.length/divy; y++)
		{
			String line = ".byte ";
			for(int x = 0; x<patt[y].length/divx; x++)
				line+= (x>0 ? "," : "")+colors[patt[y][x]];

			System.out.println("patt"+y+": "+line);
		}
	}


	public static void print(int[][] patt, String[] colors, boolean opt)
	{
		Map lines = new HashMap();
		for(int y = 0; y<patt.length; y++)
		{
			String line = ".byte ";
			for(int x = 0; x<patt[y].length; x++)
				line+= (x>0 ? "," : "")+colors[patt[y][x]];

			if(opt && lines.containsKey(line))
			{
				System.out.println("patt"+y+"="+lines.get(line));
			}
			else
			{
				System.out.println("patt"+y+": "+line);
				lines.put(line,"patt"+y);
			}
		}
	}

	public static Set dodekoedron()
	{
		double f1 = (Math.sqrt(5)-1)/2.0;
		double f2 = (Math.sqrt(5)+1)/2.0;
		double m = Math.sqrt(3);
		double[][] c = {{1.0,1.0,1.0},{f1,f2,0.0},{0.0,f1,f2},{f2,0.0,f1}};
		Set out = new HashSet();
		for(int k = 0; k<c.length; k++)
		{
			double[] co = c[k];
			for(double i0 = -1; i0<2; i0+=2)
			for(double i1 = -1; i1<2; i1+=2)
			for(double i2 = -1; i2<2; i2+=2)
				out.add(new Point(co[0]*i0/m,co[1]*i1/m,co[2]*i2/m));
		}
		return out;
	}


	public static void printSphereCoords(Set s)
	{
		Iterator it = s.iterator();
		while(it.hasNext())
		{
			Point p = (Point)it.next();

		}
	}


	public static Set icosaedron()
	{
		double phi = (Math.sqrt(5)-1)/2.0;
		double f1 = 1/Math.sqrt(2-phi);
		double f2 = phi*f1;
		double[][] c = {{f1,f2,0.0},
				{0.0,f1,f2},
				{f2,0.0,f1}};
		Set out = new HashSet();
		for(int k = 0; k<c.length; k++)
		{
			double[] co = c[k];
			for(double i0 = -1; i0<2; i0+=2)
			for(double i1 = -1; i1<2; i1+=2)
			for(double i2 = -1; i2<2; i2+=2)
				out.add(new Point(co[0]*i0,co[1]*i1,co[2]*i2));
		}
		return out;
	}

	public static Set thetraedron()
	{
		double m = 1/Math.sqrt(3);
		Set out = new HashSet();
		out.add(new Point( m, m, m));
		out.add(new Point(-m,-m, m));
		out.add(new Point( m,-m,-m));
		out.add(new Point(-m, m,-m));
		return out;
	}

        public static Set filotaxis(int points)
        {
        	double phi = (Math.sqrt(5)-1)*Math.PI;
		Set out = new HashSet();
        	for(int z = points; z-->0;)
        	{
        		double zz = 2.0*(z+0.5)/points-1.0;
        		double r = Math.sqrt(1-zz*zz);
        		out.add(new Point(r*Math.sin(phi*z),r*Math.cos(phi*z),zz));
        	}
        	return out;
        }

	public static double dist(Point p, Set points)
	{
		double min = Double.POSITIVE_INFINITY;
		Iterator it = points.iterator();
		while(it.hasNext())
		{
			Point p2 = (Point)it.next();
			double d = p.dist(p2);
			if(min>d)
				min = d;
		}
		return min;
	}

	public static int[][] pointSetDistPattern(int sizex, int sizey, Set points, double res)
	{
		int[][] pattern = new int[sizey][];
		for(int y = sizey; y-->0; )
		{
			pattern[y] = new int[sizex];
			for(int x = sizex; x-->0; )
			{
				double r[] = threeaxrot.square2ball(
						new double[]
							{
							(x+0.5)/sizex,
							(y+0.5)/sizey
							}
						);
                                Point p = new Point(r[0],r[1],r[2]);
                                double d = dist(p,points);
                                pattern[y][x] = (int)(d*res);
			}
		}
		return pattern;
	}
}
