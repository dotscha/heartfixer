package c64;

import java.util.*;

public class SpeedcodeUtils
{

	//Greedy opt of [code].
	public static void makeCode(Map code)
	{
		System.out.println(";Optimizer: makeCode(code)");
		int cost = 0;
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
			cost += min;
                        printWriteCode(code.get(next));
                        code.remove(next);
                        last = next;
		}
		System.out.print(";Read cost: "+cost);
	}

	//Optimizes every [blocks] reads in [phases] phases.
	public static void makeCode(Map code, int blocks, int phases)
	{
		makeCode(code,blocks,0,phases);
	}
	//Optimizes every [blocks] reads in [phases] phases, considering the next [tail] reads too.
	public static void makeCode(Map code, int blocks, int tail, int phases)
	{
		System.out.println(";Optimizer: makeCode(code,"+blocks+","+tail+","+phases+")");
		List<ReadCode> reads = new ArrayList<ReadCode>();
		ReadCode last = null;
		Map _code = new HashMap(code);
		while (reads.size()<code.size())
		{
			List<ReadCode> best = null;;
			int cost = Integer.MAX_VALUE;
			for (int p=0; p<phases; ++p)
			{
				List<ReadCode> rs = new ArrayList<ReadCode>();
				Map cd = new HashMap(_code);
				int c = makeCodeRand(blocks+tail,last,cd,rs);
				if (c<cost)
				{
					cost = c;
					best = rs.subList(0,Math.min(blocks,rs.size()));
				}
			}
			if (best.size()>0) last = best.get(best.size()-1);
			reads.addAll(best);
			_code.keySet().removeAll(best);
		}
		printCode(reads,code);
	}

	//Optimizes [code] in level*(level-1)/2 steps.
	public static void makeCode(Map code, int level)
	{
		System.out.println(";Optimizer: makeCode(code,"+level+")");
		Map _code = new HashMap(code);
		List<ReadCode> reads = new ArrayList<ReadCode>();
		ReadCode last = null;
		for (int i = 0; i<level; ++i)
		{
			List<ReadCode> best = null;
			int cost = Integer.MAX_VALUE;
			for (int j = i; j<level; ++j)
			{
				List<ReadCode> rs = new ArrayList<ReadCode>();
				Map cd = new HashMap(_code);
				int c = makeCodeRand(_code.size()/(level-i),last,cd,rs);
				if (c<cost)
				{
					cost = c;
					best = rs;
				}
			}
			if (best.size()>0) last = best.get(best.size()-1);
			reads.addAll(best);
			_code.keySet().removeAll(best);
		}
		printCode(reads,code);
	}

	//Prints the reads and corresponding writes.
	private static void printCode(List<ReadCode> reads, Map code)
	{
		int cost = 0;
		ReadCode last = null;
		for (ReadCode next : reads)
		{
			cost += next.cost(last);
			next.printCode(last);
			printWriteCode(code.get(next));
			last = next;
		}
		System.out.println(";Read cost: "+cost);
	}

	//Modifies code and out, returns cost.
	//Creates a randomized greedy best read sequence of length [reads].
	private static int makeCodeRand(int reads, ReadCode last, Map code, List<ReadCode> out)
	{
		int cost = 0;
		List<ReadCode> next = new ArrayList<ReadCode>();
		while (!code.isEmpty() && reads>0)
		{
			int min = Integer.MAX_VALUE;
			if (last!=null)
			{
				Iterator rcIt = code.keySet().iterator();
				while (rcIt.hasNext())
				{
					ReadCode rc = (ReadCode)rcIt.next();
					int d = rc.cost(last);
                                	if (d<min)
                                	{
                                		min = d;
						next.clear();
						next.add(rc);
                                	}
					else if (d==min)
					{
						next.add(rc);
					}

				}
			}
			else
			{
				next.addAll(code.keySet());
			}
			ReadCode n = next.get((int)(Math.random()*next.size()));
			out.add(n);
			cost += n.cost(last);
                        code.remove(n);
                        last = n;
		}
		return cost;
	}

	//Prints write code in all supported formats.
	private static void printWriteCode(Object c)
	{
		if (c instanceof String)
		{
			System.out.println(c);
		}
		else if (c instanceof WriteCode)
		{
			((WriteCode)c).printCode();
		}
		else if (c instanceof Collection)
		{
			for (Object o : (Collection)c)
			{
				printWriteCode(o);
			}
		}
		else
		{
			throw new RuntimeException("Unsupported write code type");
		}
	}
}
