import c64.*;

public class FourPixelsTok implements ReadCode
{
	private int[] norm = new int[] {-1,-1,-1,-1};

	static int ORA_W = 4;
	static int AND_B = 2;

	public FourPixelsTok()
	{
	}

	public static void main(String[] args)
	{
		FourPixelsTok a = new FourPixelsTok();
		FourPixelsTok b = new FourPixelsTok();
		FourPixelsTok c = new FourPixelsTok();
		a.setNormal(3,1);
		b.setNormal(0,2);
		c.setNormal(0,3);
		a.printCode(null);
		b.printCode(a);
		c.printCode(b);
	}

	public void setNormal(int i, int n)
	{
		norm[i] = n;
	}

	public boolean empty()
	{
		return norm[0]<0 && norm[1]<0 && norm[2]<0 && norm[3]<0;
	}

	private static class Analysis
	{
		public int ora, mask;
		public boolean[] write;

		public Analysis(int[] l, int[] c)
		{
			ora = mask = 0;
			write = new boolean[4];
			for (int i=0; i<4; ++i)
			{
				mask *= 2;
				write[i] = false;
				if (l[i]==-1 && c[i]!=-1)
				{
					ora++;
					write[i] = true;
				}
				else if (l[i]!=-1 && c[i]==-1)
				{
					mask|=1;
				}
				else if (l[i]!=c[i])
				{
					ora++;
					write[i] = true;
					mask|=1;
				}
			}
			mask = 15-mask;
			if (write[0] && write[1] && c[0]==c[1])
				ora--;
			if (write[2] && write[3] && c[2]==c[3])
				ora--;
		}
	}

	public int cost(ReadCode l)
	{
		FourPixelsTok p = l==null ? new FourPixelsTok() : (FourPixelsTok)l;
		Analysis a = new Analysis(p.norm,norm);
		int and = a.mask!=0 && a.mask!=15 ? 1 : 0;
		return a.ora*ORA_W + and*AND_B;
	}

	public void printCode(ReadCode l)
	{
		FourPixelsTok p = l==null ? new FourPixelsTok() : (FourPixelsTok)l;
		Analysis a = new Analysis(p.norm,norm);
		tok1(";"+norm[0]+"\t"+norm[1]+"\t"+norm[2]+"\t"+norm[3]);
		tok1(".byte "+(a.mask + 16*a.ora));
		for (int nib = 0; nib<2; ++nib)
		{
			int i = 2*nib;
			if (a.write[i] && a.write[i+1] && norm[i]==norm[i+1])
			{
				addr((nib==0 ? "hi" : "lo")+"_bits+"+norm[i]);
			}
			else
			{
				for (int j=i; j<i+2; ++j)
				{
					if (a.write[j])
					{
						addr("tup"+(3-j)+"bits+"+norm[j]);
					}
				}
			}
		}
	}

	private static void addr(String a)
	{
		tok1(".byte >("+a+")");
		tok2(".byte <("+a+")");
	}

	private static void tok1(String s)
	{
		System.out.println(s);
	}

	private static void tok2(String s)
	{
		System.err.println(s);
	}

	public int hashCode()
	{
		return ((norm[0]*31+norm[1])*31+norm[2])*31+norm[3];
	}

	public boolean equals(Object o)
	{
		if (o==null || !(o instanceof FourPixelsTok))
		{
			return false;
		}
		FourPixelsTok p = (FourPixelsTok)o;
		return p.norm[0]==norm[0] && p.norm[1]==norm[1] && p.norm[2]==norm[2] && p.norm[3]==norm[3];
	}
}
