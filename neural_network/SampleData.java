import java.util.ArrayList;

public class SampleData {
	public Integer sampleCount=0; // count of sample data in this class
	public ArrayList<ArrayList<Double>> samples=new ArrayList<ArrayList<Double>>(); 
	public Double maxD;


	public void add(ArrayList<Double> x)
	{
		samples.add(x);
		sampleCount++;
	}
	
	public Double getMax()
	{
		if(maxD != null) return maxD;
		Double max=samples.get(0).get(0);
		for (int s = 0; s < samples.size(); s++) {
			for (int d = 0; d < samples.get(0).size()-1; d++) {
				
				if(samples.get(s).get(d) > max)
					max=samples.get(s).get(d);
				
				
			}
		}
		this.maxD=max;
		return max;
	}	
	
	public void normalizeTrainingData()
	{
		Double max=getMax();
		
		for (int s = 0; s < samples.size(); s++) {
			for (int d = 0; d < samples.get(0).size()-1; d++) {
				
					samples.get(s).set(d,samples.get(s).get(d)/max);
				
			}
		}
		
		
	}
	


	
}
              