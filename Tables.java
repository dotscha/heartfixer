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
	}
}

