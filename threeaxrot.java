public class threeaxrot
{
	private static double Pi = Math.PI;
	private static double Pi2 = 2.0*Pi;

	public static void main(String[] args)
	{
		int[][] ballt = ballTable2(25,25,1.0);
		//int[][] ballt = ballTable3(25,25,2.0);
		makeSpeedCodeFor4x4Avr(ballt);
		//makeSpeedCodeFor4x4Avr2(ballt);
	}


/*
; u,v
	ldy shift1+u	;3
	lda RoyY+v*256,y;3

	sta ry1+2	;3
	sta rx1+2	;3
	ldx RoyX+v*256,y;3

	ldy shift2,x	;3
ry1:	ldx RotY,y	;3
	lda texture_y,x	;3
	sta ry2+2	;3
rx1:	ldx RotX,y	;3

	ldy shift3,x	;3
ry2:	lda texture,y	;3

	sta screen
*/

	public static void makeSpeedCodeFor4x4Avr(int[][] coord)
	{
		p("t = $02");
		for(int y = 0; y<coord.length; y++)
		{
			int[] line = coord[y];
			int xs = line.length/2;
			for(int x = 0; x<xs; x++)
			{
				int u = line[x];
				int v = line[x+xs];
				p("ldx <(shift1+"+u+")");
				p("lda RotY+"+v+"*256,x");
				p("ldy RotX+"+v+"*256,x");
				/**/
				p("sta :+  +2");
				p("sta :++ +2");
				p("ldx shift2,y");
				p(": ldy RotY,x");
				p("lda texture_y,y");
				p("sta t+1");
				p(": ldy RotX,x");
				p(" lda (t),y");
				/**/
				//p("jsr rest");
				p("sta converterLine"+(2*y)+"+deltaX"+(2*x));
				if(y>0)
				{
					p("adc converterLine"+(2*y-2)+"+deltaX"+(2*x));
					p("sta converterLine"+(2*y-1)+"+deltaX"+(2*x));
				}
			}
			if(y>0)
			{
				p("ldx #deltaYLine"+(2*y-1)+"*40");
				p("jsr converterLine"+(2*y-1));
			}
			p("ldx #deltaYLine"+(2*y)+"*40");
			p("jsr converterLine"+(2*y));
		}
		p("rts");

		p("rest:");
		p("sta :+  +2");
		p("sta :++ +2");
		p("ldx shift2,y");
		p(": ldy RotY,x");
		p("lda texture_y,y");
		p("sta t+1");
		p(": ldy RotX,x");
		p(" lda (t),y");
		p("rts");


		/*
		p("sta ry1+2");
		p("sta rx1+2");
		p("ldy shift2,x");
		p("ry1: ldx RotY,y");
		p("lda texture_y,x");
		p("sta ry2+2");
		p("rx1: ldx RotX,y");

		//p("ldy shift3,x");
		//p("ry2: lda texture,y");
		p("ry2: lda texture,x");
                */

	}

        /**
         * Ez nem jott be.
         */
	public static void makeSpeedCodeFor4x4AvrMod2(int[][] coord)
	{
		p("t = $02");

		p(": lda #0");
		p("eor #1");
		p("sta :- +1");
		p("beq sp_0");
		p("jmp sp_1");

		int xs = -1;
		for(int mod = 0; mod<2; mod++)
		{
		p("sp_"+mod+":");

		int dy = 1-2*mod;
		for(int y = mod; y<coord.length; y+=2)
		{
			int[] line = coord[y];
			xs = line.length/2;
			for(int x = 0; x<xs; x++)
			{
				int u = line[x];
				int v = line[x+xs];
				p("ldx <(shift1+"+u+")");
				p("lda RotY+"+v+"*256,x");
				p("ldy RotX+"+v+"*256,x");
				p("jsr rest");
				p("sta converterLine"+(2*y)+"+deltaX"+(2*x));
				if(y+dy<25)
				{
					p("adc converterLine"+(2*y+2*dy)+"+deltaX"+(2*x));
					p("sta converterLine"+(2*y+dy)+"+deltaX"+(2*x));
				}
			}
			if(y+dy<25)
			{
				p("ldx #deltaYLine"+(2*y+dy)+"*40");
				p("jsr converterLine"+(2*y+dy));
			}
			p("jsr converterLine"+(2*y));
			if(2*(y-mod)+3<49)
			{
				p("jsr avr_line"+(2*(y-mod)+3));
			}
		}
		p("rts");
		}
		p("rest:");
		p("sta :+  +2");
		p("sta :++ +2");
		p("ldx shift2,y");
		p(": ldy RotY,x");
		p("lda texture_y,y");
		p("sta t+1");
		p(": ldy RotX,x");
		p(" lda (t),y");
		p("rts");
		for(int y = 3 ; y<49; y+=4)
		{
			p("avr_line"+y+":");
			for(int x = 0; x<xs; x++)
			{
				p("lda converterLine"+(y-1)+"+deltaX"+(2*x));
				p("adc converterLine"+(y+1)+"+deltaX"+(2*x));
				p("sta converterLine"+(y)+"+deltaX"+(2*x));
			}
			p("ldx #deltaYLine"+y+"*40");
			p("jmp converterLine"+y);
		}
	}


	public static void makeSpeedCodeFor4x4Avr2(int[][] coord)
	{
		p("t = $02");

		p(": lda #0");
		p("eor #1");
		p("sta :- +1");
		p("beq sp_0");
		p("jmp sp_1");

		for(int mod = 0; mod<2; mod++)
		{
		p("sp_"+mod+":");

		for(int y = mod; y<coord.length; y+=2)
		{
			int[] line = coord[y];
			int xs = line.length/2;
			for(int x = 0; x<xs; x++)
			{
				int u = line[x];
				int v = line[x+xs];
				p("ldx <(shift1+"+u+")");
				p("lda RotY+"+v+"*256,x");
				p("ldy RotX+"+v+"*256,x");
				/*
				p("sta :+  +2");
				p("sta :++ +2");
				p("ldx shift2,y");
				p(": ldy RotY,x");
				p("lda texture_y,y");
				p("sta t+1");
				p(": ldy RotX,x");
				p(" lda (t),y");
				*/
				p("jsr rest");
				p("sta converterLine"+(2*y)+"+deltaX"+(2*x));
			}
			p("ldx #deltaYLine"+(2*y)+"*40");
			p("jsr converterLine"+(2*y));
		}
		p("rts");
		}
		p("rest:");
		p("sta :+  +2");
		p("sta :++ +2");
		p("ldx shift2,y");
		p(": ldy RotY,x");
		p("lda texture_y,y");
		p("sta t+1");
		p(": ldy RotX,x");
		p(" lda (t),y");
		p("rts");
	}

	public static void p(String s)
	{
		System.out.println(s);
	}

        /**
        * Gombot nezzuk tavolrol
        *
	private static String[][] ballTable1(double r, int sizey, String labely)
	{
		int R = (int)r;
		String[][] out = new String[2*R+1][];
		for(int y = -R; y<=R; y++)
		{
			double yy = y/r;
			double rx = Math.sqrt(r*r-y*y);
			int Rx = (int)rx;
			int width = 2 * Rx + 1;
			String[] line = out[y+R] = new String[2*width];
			for(int x = -Rx; x<=Rx; x++)
			{
				double xx = x/r;
				double c[] = new double[]{Math.sqrt(1-xx*xx-yy*yy),xx,yy};
				c = ball2square(c);
				line[x+Rx] = ""+
					(int)(trunc(c[0])*256);
				line[x+Rx + width] = ">"+labely+
					(int)(trunc(c[1])*sizey);
			}
		}
		return out;
	}
        */


	/**
	* Gomb kozeppontjabol nezunk a gomb falara
	* avagy panorama nezet
	*/
	private static int[][] ballTable2(int Dx, int Dy, double verang)
	{
		double RatioXY = ((double)Dx)/Dy;
		int[][] out = new int[Dy][];
		for(int y = 0; y<Dy; y++)
		{
			out[y] = new int[2*Dx];
			double yy = verang*(y-Dy/2.0)/(Dy/2.0);
			for(int x = 0; x<Dx; x++)
			{
				double xx = verang*RatioXY*(x-Dx/2.0)/(Dx/2.0);
				double t = 1/Math.sqrt(1+xx*xx+yy*yy);
				double[] r = ball2square(new double[]{t,-yy*t,-xx*t});
				out[y][x]   = (int)(trunc(r[0])*128);
				out[y][x+Dx]= (int)(trunc(r[1])*64);
			}
		}
		return out;
	}

	/**
	* Gomb inverzios kepet nezzuk a sikon
	*/
	private static int[][] ballTable3(int Dx, int Dy, double verang)
	{
		//double verang=3.0;
		double RatioXY = ((double)Dx)/Dy;
		int[][] out = new int[Dy][];
		for(int y = 0; y<Dy; y++)
		{
			out[y] = new int[2*Dx];
			double yy = verang*(y-Dy/2.0)/(Dy/2.0);
			for(int x = 0; x<Dx; x++)
			{
				double xx = verang*RatioXY*(x-Dx/2.0)/(Dx/2.0);
				double t = 1/Math.sqrt(1+xx*xx+yy*yy);
				double[] r = ball2square(plane2ball(xx,yy));
				out[y][x]   = (int)(trunc(r[0])*128);
				out[y][x+Dx]= (int)(trunc(r[1])*64);
			}
		}
		return out;
	}


	/**
	* Sik szogtarto vetitese egyseggombre inverzioval
	*/
	public static double[] plane2ball(double x, double y)
	{
		double r = 4/(4+x*x+y*y);
		return new double[]{x*r,y*r,1-2*r};
	}

	public static final double trunc(double x)
	{
		return x-Math.floor(x);
	}

	public static double[] square2ball(double[] c)
	{
		double m = Math.cos(c[1]*Pi);
		double r = Math.sqrt(1.0-m*m);
		return new double[]{Math.cos(c[0]*Pi2)*r,Math.sin(c[0]*Pi2)*r,m};
	}

	public static double[] ball2square(double[] c)
	{
		return new double[]{Math.atan2(c[1],c[0])/Pi2+0.5,Math.acos(c[2])/Pi};
	}
}
