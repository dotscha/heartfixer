package c64;

import java.util.*;

public class SpeedcodeUtils
{

	public static void makeCode(Map code)
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
                        System.out.println(code.get(next));
                        code.remove(next);
                        last = next;
		}
	}
}
