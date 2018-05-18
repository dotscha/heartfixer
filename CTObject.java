import java.util.*;

public interface CTObject
{
	Collection getIntersections(double x, double y);
	double[] getMinMax(int coord);
	Vector getNormal(double x, double y, double z);
}
