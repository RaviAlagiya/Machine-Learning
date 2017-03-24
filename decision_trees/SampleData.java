
import java.util.ArrayList;

public class SampleData {
	public static Integer ClassCount;
	public static Integer dimensionCount;
	public Integer sampleCount=0; // count of sample data in this class
	public ArrayList<ArrayList<Double>> samples=new ArrayList<ArrayList<Double>>(); 
	public Double maxD;


	public void add(ArrayList<Double> x)
	{
		samples.add(x);
		sampleCount++;
		dimensionCount=x.size()-1;
	}
	



	
}
              