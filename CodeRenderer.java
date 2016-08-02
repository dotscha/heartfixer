import c64.*;
import java.util.*;

public class CodeRenderer
{
	private static int sizex = 80, sizey = 100;
	private static double
		x0 = 0.5,
		x1 = sizex-0.5,
		y0 = 0.5,
		y1 = sizey/2-0.5;


	static double qang = 0;
	static double cutang = Math.cos(Math.PI/180*qang);

	public static void main(String[] args)
	{
		//p(";"+getNormalIndex(new Vector(0,1,100)));
		//makePaletteMap();
        	//makeCodeOpt1(getObjects4(args[0]));
        	//make2x2CodeOpt2(getObjects4(args[0]));
        	//make2x2CodeOpt2(getObjects4(args[0]));
        	//make2x2x16OptCode(getObjects4(args[0]));

        	if(args[0].equals("-2x2"))
        	{
        		make2x2CodeOpt2(getObjects4(args[1]));
        	}
        	else
        	if(args[0].equals("-2x2tok"))
        	{
        		make2x2CodeTokOpt2(getObjects4(args[1]));
        	}
        	else
        	if(args[0].equals("-4x2"))
        	{
	        	make4x2CodeOpt1(getObjects4(args[1]));
        	}
        	else
        	if(args[0].equals("-2x2x16"))
        	{
	        	make2x2x16OptCode2(getObjects4(args[1]));
        	}
	}


        public static void makePaletteMap()
        {
        	for(int x = 0; x<16; x++)
        	{
        		for(int y = 0; y<16; y++)
        		{
        			p("lda hi_bits+"+(2*x+0+32*y));
        			p("ora lo_bits+"+(2*x+1+32*y));
        			p("sta Bitmap+"+((y/4)*40*8+(y%4)*2+x*8));
        		}
        	}
        }


	public static void make4x2Code(Collection objects)
	{
		Vector[][] norm = getNormals(objects);
                for(int l = 0; l<sizey; l++)
                {
                	int y = sizey-1-l;
                	Vector[] line = norm[y];
                	for(int c = 0; 2*c<sizex; c++)
                	{
                		String read = "lda";
                		Vector n0 = line[2*c+0];
                		Vector n1 = line[2*c+1];
                		if(n0!=null)
                		{
                			p(read+" hi_bits+"+getNormalIndex(n0));
                			read = "ora";
                		}
                		if(n1!=null)
                			p(read+" lo_bits+"+getNormalIndex(n1));
                		if(n0!=null || n1!=null)
                			p("sta Bitmap+"+((l/4)*40*8+(l%4)*2+c*8)+",x");
                	}
                }
	}


	public static void make2x2Code(Collection objects)
	{
		sizex = 160;
		Vector[][] norm = getNormals(objects);
                for(int l = 0; l<sizey; l++)
                {
                	int y = sizey-1-l;
                	Vector[] line = norm[y];
                	for(int c = 0; 4*c<sizex; c++)
                	{
                		String rc = "lda";
                		for(int k = 0; k<4; k++)
                		{
                			Vector n = line[4*c+k];
	                		if(n!=null)
        	        		{
                				p(rc+" tup"+(3-k)+"bits+"+getNormalIndex(n));
                				rc = "ora";
                			}
                		}
                		if(rc.equals("ora"))
                			p("sta Bitmap+"+((l/4)*40*8+(l%4)*2+c*8)+",x");
                	}
                }
	}


	public static void make2x2CodeOpt1(Collection objects)
	{
		sizex = 160;
		Vector[][] norm = getNormals(objects);
		Map code = new HashMap();
                for(int l = 0; l<sizey; l++)
                {
                	int y = sizey-1-l;
                	Vector[] line = norm[y];
                	for(int c = 0; 4*c<sizex; c++)
                	{
                		//prepare vectors
                		for(int k = 0; k<2; k++)
                		{
                			if(line[4*c+2*k]!=null && line[4*c+2*k+1]!=null && line[4*c+2*k].skal(line[4*c+2*k+1])>cutang)
                				line[4*c+2*k] = line[4*c+2*k+1] = line[4*c+2*k].add(line[4*c+2*k+1]).norm();
                		}

                		String read = "";
                		String rc = "lda";
                		for(int k = 0; k<4; k++)
                		{
                			Vector n = line[4*c+k];
	                		if(n!=null)
        	        		{
                				read += rc+" tup"+(3-k)+"bits+"+getNormalIndex(n);
                				rc = "\r\nora";
                			}
                		}
                		if(!rc.equals("lda"))
                		{
                			String write = "sta Bitmap+"+((l/4)*40*8+(l%4)*2+c*8)+",x";
                			String wc = (String)code.get(read);
                			if(wc==null)
                				wc = write;
                			else
                				wc = wc + "\r\n" + write;
                			code.put(read,wc);
                		}
                	}
                }
                List rl = new ArrayList(code.keySet());
                Collections.sort(rl);
                //Collections.shuffle(rl);
                Iterator it = rl.iterator();
                int c = 0;
                while(it.hasNext())
                {
                	Object r = it.next();
                	p(r.toString());
                	p(code.get(r).toString());
                	c++;
                }
                p(";"+c);
	}


	public static void make2x2CodeOpt2(Collection objects)
	{
		sizex = 160;
		Vector[][] norm = getNormals(objects);
		Map code = new HashMap();
                for(int l = 0; l<sizey; l++)
                {
                	int y = sizey-1-l;
                	Vector[] line = norm[y];
                	for(int c = 0; 4*c<sizex; c++)
                	{
                		//prepare vectors
                		for(int k = 0; k<2; k++)
                		{
                			if(line[4*c+2*k]!=null && line[4*c+2*k+1]!=null && line[4*c+2*k].skal(line[4*c+2*k+1])>cutang)
                				line[4*c+2*k] = line[4*c+2*k+1] = line[4*c+2*k].add(line[4*c+2*k+1]).norm();
                		}

				FourPixels fp = new FourPixels();

                		for(int k = 0; k<4; k++)
                			fp.setNormal(k,getNormalIndex(line[4*c+k]));

                		if(!fp.empty())
                		{
                			String write = "sta Bitmap+"+((l/4)*40*8+(l%4)*2+c*8)+",x";
                			String wc = (String)code.get(fp);
                			if(wc==null)
                				wc = write;
                			else
                				wc = wc + "\r\n" + write;
                			code.put(fp,wc);
                		}
                	}
                }
                SpeedcodeUtils.makeCode(code);
	}


	public static void make2x2CodeTokOpt2(Collection objects)
	{
		sizex = 160;
		Vector[][] norm = getNormals(objects);
		Map code = new HashMap();
                for(int l = 0; l<sizey; l++)
                {
                	int y = sizey-1-l;
                	Vector[] line = norm[y];
                	for(int c = 0; 4*c<sizex; c++)
                	{
                		//prepare vectors
                		for(int k = 0; k<2; k++)
                		{
                			if(line[4*c+2*k]!=null && line[4*c+2*k+1]!=null && line[4*c+2*k].skal(line[4*c+2*k+1])>cutang)
                				line[4*c+2*k] = line[4*c+2*k+1] = line[4*c+2*k].add(line[4*c+2*k+1]).norm();
                		}

				FourPixelsTok fp = new FourPixelsTok();

                		for(int k = 0; k<4; k++)
                			fp.setNormal(k,getNormalIndex(line[4*c+k]));

                		if(!fp.empty())
                		{
                			String t = "(Bitmap+"+((l/4)*40*8+(l%4)*2+c*8)+")";
                			String write0 = ".byte >"+t;
                			String write1 = ".byte <"+t;
                			List wc = (List)code.get(fp);
                			if(wc==null)
                				wc = new LinkedList();
               				wc.add(write0);
               				wc.add(write1);
                			code.put(fp,wc);
                		}
                	}
                }
                makeCode_4_token(code);
	}


	public static void makeCode_4_token(Map code)
	{
		ReadCode last = null, next = null;
		while(!code.isEmpty())
		{
			Iterator rcIt = code.keySet().iterator();
			int min = Integer.MAX_VALUE;
			while(min>0 && rcIt.hasNext())
			{
				ReadCode rc = (ReadCode)rcIt.next();
				int d = rc.cost(last);
                                if(d<min)
                                {
                                	min = d;
                                	next = rc;
                                }
			}
                        next.printCode(last);
                        Iterator it = ((List)code.get(next)).iterator();
                        while(it.hasNext())
                        {
				System.out.println(it.next());
				System.err.println(it.next());
                        }
                        code.remove(next);
                        last = next;
		}
	}


	public static void make2x2x16OptCode(Collection objects)
	{
		sizex = 160;
		Vector[][] norm = getNormals(objects);
		Map[] code = new Map[]{new HashMap(),new HashMap()};
                for(int l = 0; l<sizey; l++)
                {
                	int mod = l%2;
                	int y = sizey-1-l;
                	Vector[] line = norm[y];
               		//prepare vectors
                	for(int c = 0; 2*c<sizex; c++)
                	{
                		if(line[2*c]!=null && line[2*c+1]!=null && line[2*c].skal(line[2*c+1])>cutang)
                			line[2*c] = line[2*c+1] = line[2*c].add(line[2*c+1]).norm();
                	}

                	for(int c = 0; 4*c<sizex; c++)
                	{
                		for(int k = 0; k<2; k++)
                		{
                			if(line[4*c+k]!=null || line[4*c+k+2]!=null)
                			{
                				PixelPair16c pp = new PixelPair16c(
                					getNormalIndex(line[4*c+k]),
                					getNormalIndex(line[4*c+k+2])
                					);

                				String write = "sta SCREENS"+k+"+"+((1024*(l%4))+40*(l/4)+c);
                				//String write = "sta color_x"+(4*c+k)+"_y"+y;

	                			String wc = (String)code[mod].get(pp);
        	        			if(wc==null)
                					wc = write;
                				else
                					wc = wc + "\r\n" + write;
                				code[mod].put(pp,wc);
                			}
                		}
                	}
                }

                System.out.println("speedcode0:");
                SpeedcodeUtils.makeCode(code[0]);
                System.out.println("rts");

                System.out.println("speedcode1:");
                SpeedcodeUtils.makeCode(code[1]);
                System.out.println("rts");
	}


	public static void make2x2x16OptCode2(Collection objects)
	{
		sizex = 160;
		Vector[][] norm = getNormals(objects);
		Map[] code = new Map[]{new HashMap(),new HashMap(),new HashMap(),new HashMap()};
                for(int l = 0; l<sizey; l++)
                {
                	int mod = l%2;
                	int y = sizey-1-l;
                	Vector[] line = norm[y];
               		//prepare vectors
                	for(int c = 0; 2*c<sizex; c++)
                	{
                		if(line[2*c]!=null && line[2*c+1]!=null && line[2*c].skal(line[2*c+1])>cutang)
                			line[2*c] = line[2*c+1] = line[2*c].add(line[2*c+1]).norm();
                	}

                	for(int c = 0; 4*c<sizex; c++)
                	{
                		for(int k = 0; k<2; k++)
                		{
                			if(line[4*c+k]!=null || line[4*c+k+2]!=null)
                			{
                				PixelPair16c pp = new PixelPair16c(
                					getNormalIndex(line[4*c+k]),
                					getNormalIndex(line[4*c+k+2])
                					);

                				String write = "sta SCREENS"+k+"+"+((1024*(l%4))+40*(l/4)+c);
                				//String write = "sta color_x"+(4*c+k)+"_y"+y;

	                			String wc = (String)code[2*mod+k].get(pp);
        	        			if(wc==null)
                					wc = write;
                				else
                					wc = wc + "\r\n" + write;
                				code[2*mod+k].put(pp,wc);
                			}
                		}
                	}
                }

		/**
		p("speedcode0:");
		SpeedcodeUtils.makeCode(code[0]);
		SpeedcodeUtils.makeCode(code[3]);
                p("rts");
		p("speedcode1:");
		SpeedcodeUtils.makeCode(code[1]);
		SpeedcodeUtils.makeCode(code[2]);
                p("rts");
		/**/
		p("speedcode0:");
		p("mod0mod: lda #0");
		p("eor #1");
		p("sta mod0mod+1");
		p("beq speedcode_0");
		p("jmp speedcode_1");

                p("speedcode_0:");
                SpeedcodeUtils.makeCode(code[0]);
                p("rts");

                p("speedcode_1:");
                SpeedcodeUtils.makeCode(code[1]);
                p("rts");

		p("speedcode1:");
		p("mod1mod: lda #1");
		p("eor #1");
		p("sta mod1mod+1");
		p("beq speedcode_2");
		p("jmp speedcode_3");

                p("speedcode_2:");
                SpeedcodeUtils.makeCode(code[2]);
                p("rts");

                p("speedcode_3:");
                SpeedcodeUtils.makeCode(code[3]);
                p("rts");
                /**/
	}

/*
	private static void makeCode(Map code)
	{
                List rl = new ArrayList(code.keySet());
                Collections.sort(rl);
                //Collections.shuffle(rl);
                Iterator it = rl.iterator();
                FourPixels last = (FourPixels)it.next();
                last.printCode();
                p(code.get(last).toString());
                int c = 0;
                while(it.hasNext())
                {
                	FourPixels next = (FourPixels)it.next();
                	String write = code.get(next).toString();
                	next.printCode(last);
                	p(write);
                	last = next;
                	c++;
                }
                p(";"+c);
	}


	private static void makeCodeOpt1(Map code)
	{
		FourPixels last = new FourPixels();
		while(!code.isEmpty())
		{
			Iterator it = code.keySet().iterator();
			int max = Integer.MAX_VALUE;
			FourPixels next = null;
			while(it.hasNext() && max>1)
			{
				FourPixels c = (FourPixels)it.next();
				int d = last.dist(c);
				if(d<=max)
				{
					max = d;
					next = c;
				}
			}
			next.printCode(last);
			p(code.get(next).toString());
			code.remove(next);
			last = next;
		}
	}
*/

	public static void make4x2CodeOpt1(Collection objects)
	{
		Vector[][] norm = getNormals(objects);
		Map code = new HashMap();
                for(int l = 0; l<sizey; l++)
                {
                	int y = sizey-1-l;
                	Vector[] line = norm[y];
                	String read;
                	for(int c = 0; 2*c<sizex; c++)
                	{
                		read = "";
                		Vector n0 = line[2*c+0];
                		Vector n1 = line[2*c+1];
                		if(n0!=null)
                			read += "lda hi_bits+"+getNormalIndex(n0);
                		if(n1!=null)
                			read += (n0==null ? "lda " : "\r\nora ")+"lo_bits+"+getNormalIndex(n1);
                		if(read.length()>0)
                		{
                			String write = "sta Bitmap+"+((l/4)*40*8+(l%4)*2+c*8)+",x";
                			String wc = (String)code.get(read);
                			if(wc==null)
                				wc = write;
                			else
                				wc = wc+"\r\n"+write;
                			code.put(read,wc);
                		}
                	}
                }
                List rl = new ArrayList(code.keySet());
                Collections.sort(rl);
                //Collections.shuffle(rl);
                Iterator it = rl.iterator();
                while(it.hasNext())
                {
                	Object r = it.next();
                	p(r.toString());
                	p(code.get(r).toString());
                }
	}


	public static int getNormalIndex(Vector n)
	{
		if(n==null)
			return -1;
		else
		{
	        	int i1 = (int)((Math.atan2(n.getX(),n.getY())+Math.PI)/2/Math.PI*32);
        		int i2 = (int)(Math.acos(Math.abs(n.getZ()))*2/Math.PI*16);
        		return i1+i2*32;
        	}
	}


	public static Collection getObjects4(String file)
	{
		try
		{
		VRLMObject o = new VRLMObject(file);
                /**/
		o.transform(Matrix.rot(0,2,Math.PI*0.9).mul(Matrix.rot(0,1,0)));
		o.fitIntoBox(0,80,-20,50,0,1000);

		/**
		o.transform(Matrix.rot(0,2,Math.PI/2.2).mul(Matrix.rot(1,2,Math.PI/16)));
		o.fitIntoBox(0,80,0,50,0,1000);
		/**/
		return o.getTriangles();
		}
		catch(Exception e)
		{
		return null;
		}
	}



	public static Vector[][] getNormals(Collection objects)
	{
		Vector[][] out = new Vector[sizey][];
		double[][] zdist = new double[sizey][];
		for(int y = 0; y<sizey; y++)
		{
			out[y] = new Vector[sizex];
			double[] line = zdist[y] = new double[sizex];
			for(int k = sizex; k-->0; )
				line[k] = Double.POSITIVE_INFINITY;
		}

		Iterator it = objects.iterator();
		double dx = (x1-x0)/sizex;
		double dy = (y1-y0)/sizey;
		while(it.hasNext())
		{
			Triangle o = (Triangle)it.next();
			double[] mm;
			mm = o.getMinMax(0);
			int ox0 = Math.max(xbeg(mm[0]),0);
			int ox1 = Math.min(xend(mm[1]),sizex-1);
			mm = o.getMinMax(1);
			int oy0 = Math.max(ybeg(mm[0]),0);
			int oy1 = Math.min(yend(mm[1]),sizey-1);
			for(int y = oy0; y<=oy1; y++)
			for(int x = ox0; x<=ox1; x++)
			{
				Iterator zit = o.getIntersections(x0+dx*(x+0.5),y0+dy*(y+0.5)).iterator();
				if(zit.hasNext())
				{
					double z = ((Double)zit.next()).doubleValue();
					if(z<zdist[y][x])
					{
						zdist[y][x] = z;
						out[y][x] = o.getNormal();
					}
				}
                        }
		}
		return out;
	}


	private static int xbeg(double x)
	{
		return (int)((x-x0)/(x1-x0)*sizex);
	}

	private static int xend(double x)
	{
		return -(int)(-(x-x0)/(x1-x0)*sizex);
	}

	private static int ybeg(double y)
	{
		return (int)((y-y0)/(y1-y0)*sizey);
	}

	private static int yend(double y)
	{
		return -(int)(-(y-y0)/(y1-y0)*sizey);
	}

	public static void p(String s)
	{
		System.out.println(s);
	}
}
