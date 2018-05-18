public class Tables
{
	public static void main(String[] args)
	{
		if(args[0].equals("-stereog"))
		{
			for(double k = 0 ; k<52; k+=0.5)
			{
				System.out.println(".byte "+(int)(Math.atan(k/16.0)/Math.PI*128)+"+>RotY");
			}
		}
		if(args[0].equals("-sphere"))
		{
			double r = 34;
			for(int k = 0 ; k<52; k+=1)
			{
				double ra = k/r;
				String out;
				if(ra<1)
				{
					out = ""+(int)(Math.asin(ra)/Math.PI*2*32)+"+>RotY";
				}
				else
				{
					out = "0";
				}
				System.out.println(".byte " + out);
			}
		}
		if(args[0].equals("-light"))
		{
			for(int k = 0; k<64; k++)
			{
				int p = (int)(13*Math.cos((k+0.5)/64*Math.PI));
				System.out.println(".byte "+(p>=0 ? p : 0));
			}
		}
		if(args[0].equals("-rotTab"))
		{
			sphereRotTable(
				Integer.parseInt(args[1]),
				Integer.parseInt(args[2]),
				Boolean.parseBoolean(args[3])
			);
		}
	}

	private static void sphereRotTable(int lat, int lon, boolean pack)
	{
		System.out.println(";lati: "+lat);
		System.out.println(";long: "+lon);
		int to_lat = pack ? lat/2 : lat;
		int to_lon = pack ? lon/4 : lon;
		for(int j = 0; j<to_lat; ++j)
		{
			String la = "", lo = "";
			double y = (j+.5)/lat;
			for(int i = 0; i<to_lon; ++i)
			{
				double x = (i+.5)/lon + 0.25;
				double[] s = threeaxrot.square2ball(new double[]{x,y});
				//xyz -> zxy
				double[] sr= new double[]{s[2],s[0],s[1]};
				double[] xy = threeaxrot.ball2square(sr);
				lo+= i==0 ? ".byte " : ",";
				la+= ",";
				la+= ""+(int)(xy[0]*lon);
				lo+= ""+(int)(xy[1]*lat);
			}
			System.out.println(lo+la);
		}
	}
}

