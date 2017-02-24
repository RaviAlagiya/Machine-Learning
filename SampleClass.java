
import java.util.ArrayList;



public class SampleClass {
	public Integer sampleCount=0;
	public ArrayList<ArrayList> samples=new ArrayList<ArrayList>();
	public ArrayList<ArrayList> testData=new ArrayList<ArrayList>();
	public ArrayList<ArrayList> gaussians=new ArrayList<ArrayList>();
	//sample > dim > pij   > s>d>j
	public ArrayList<ArrayList<ArrayList<Double>>> pijList=new ArrayList<ArrayList<ArrayList<Double>>>() ;
	public ArrayList<Double> minArray=new ArrayList<Double>();
	public ArrayList<Double> maxArray=new ArrayList<Double>();
	
	//public ArrayList<ArrayList> gaussians=new ArrayList<ArrayList>();
	//
	// 2D array first is dimension and second gaussian
	
	public ArrayList<ArrayList<Double>> meanList=new ArrayList<ArrayList<Double>>();
	public ArrayList<ArrayList<Double>> sdList=new ArrayList<ArrayList<Double>>();
	public ArrayList<ArrayList<Double>> wList=new ArrayList<ArrayList<Double>>();
	//For histrogram
	public ArrayList<ArrayList<Double>> binList=new ArrayList<ArrayList<Double>>();
	//For 1D gaussians
	//first element is mean and second is standard deviation 
	public ArrayList<ArrayList<Double>> gaussians1D=new ArrayList<ArrayList<Double>>();
	
	
	public void add(ArrayList x)
	{
		samples.add(x);
		sampleCount++;
	}
	
	
	public void updatePijList(int N,int d)
	{

		int tempDimLength=samples.get(0).size();
		
		for (int s = 0; s < sampleCount; s++) {
			//for (int d = 0; d < tempDimLength; d++) {
				for (int i = 0; i < N; i++) {
					
					pijList.get(s).get(d).set(i,naive_bayes.Pij(i, (Double)samples.get(s).get(d), this, d, N));
				}
			//}
		
		}
	}
	public void initializeGaussiansMixture(int n)
	{
		/*Suppose that you are building a mixture of N Gaussians for the j-th dimension of the data and for the c-th class. 
		 * Let S be the smallest and L be the largest value in the j-th dimension among all training data belonging to the c-th class.
		 * Let G = (L-S)/N. Then, you should initialize all standard deviations of the mixture to 1, 
		 * you should initialize all weights to 1/N, and you should initialize the means as follows:

			For the first Gaussian, the initial mean should be S + G/2.
			For the second Gaussian, the initial mean should be S + G + G/2.
			For the third Gaussian, the initial mean should be S + 2G + G/2.
			...
			For the N-th Gaussian, the initial mean should be S + (N-1)G + G/2.*/
		findMinMax();
		
		int   tempDimensionLength=samples.get(0).size();
		for (int i = 0; i < tempDimensionLength; i++) {
			ArrayList<Double> mean=new ArrayList<Double>();
			ArrayList<Double> sd=new ArrayList<Double>();
			ArrayList<Double> w=new ArrayList<Double>();
			double small=minArray.get(i);
			double large=maxArray.get(i);
			double g=(large-small)/(double)n;
			for(int j=0;j<n;j++)
			{
				sd.add((double) 1);
				w.add( (double)1/n);
				mean.add(small + ((j) * g) + (g/2));
				
			}
			meanList.add(mean);
			sdList.add(sd);
			wList.add(w);
		}
		
		int tempDimLength=samples.get(0).size();
		
		for (int s = 0; s < sampleCount; s++) {
			ArrayList<ArrayList<Double>> temp1= new ArrayList<ArrayList<Double>>();
			for (int d = 0; d < tempDimLength; d++) {
				ArrayList<Double> temp2= new ArrayList<Double>();
				for (int i = 0; i < n; i++) {
					temp2.add((double)0);
					//pijList.get(s).get(d).add((double)0);
				}
				temp1.add(temp2);
			}
			pijList.add(temp1);
		}
		
		//updatePijList(n);
	}
	@SuppressWarnings("null")
	public void findMinMax()
		{
			int   sampleSize=samples.size();
			int tempDimensionLength=samples.get(0).size();
			Double[] minMax = new Double[2];
		
			
			for (int j = 0; j < tempDimensionLength; j++) {
				//System.out.println(sampleSize+""+samples.get(0).get(j));
				minMax[0]=(Double) samples.get(0).get(j);
				minMax[1]=(Double) samples.get(0).get(j);
				for (int i = 1; i < sampleSize; i++) {
					if(minMax[0] > (Double) samples.get(i).get(j))
					{
						minMax[0] =(Double) samples.get(i).get(j);
					}
					if(minMax[1] < (Double) samples.get(i).get(j))
					{
						minMax[1] =(Double) samples.get(i).get(j);
					}
					//samples.get(i).get(x);
					
				}
				minArray.add(minMax[0]);
				maxArray.add(minMax[1]);
				
			}
			
			//return minMax;
		}
	public void initializeHistogram(int n) {
		// TODO Auto-generated method stub
		//int   sampleSize=samples.size();
		int tempDimensionLength=samples.get(0).size();
		for (int j = 0; j < tempDimensionLength; j++) {
			ArrayList<Double> bin=new ArrayList<Double>();
			for(int i=0;i<n;i++)
			{
				bin.add((double) 0);
				
			}
			binList.add(bin);
		}
		
	}

	public void calculateBinProbability(int N)
	{
		this.findMinMax();
		int   tempDimensionLength=samples.get(0).size();
		for (int d = 0; d < tempDimensionLength; d++) {
			int tempSampleSize=samples.size();
			
			double s=minArray.get(d);
			double g=maxArray.get(d);
			g=(g-s)/(double)(N-3);
			if(g < 0.0001) g=0.0001;
			
			for (int j = 0; j < tempSampleSize; j++) {
				int temp =(int) (((Double)samples.get(j).get(d) - s + g/2)/g) ;
				
				temp++;
			//	System.out.println(temp + "< temp | " +(Double)samples.get(j).get(d) +" | s: " +s+" | l: " +g );
				//if(temp == 0 || temp == N) System.out.println("samples in boundary bins");
				if(temp >= N) 
					binList.get(d).set(N-1, binList.get(d).get(N-1)+1);	
				else if(temp < 0)
				{
					binList.get(d).set(0, binList.get(d).get(0)+1);
					
				}
				else 	
					binList.get(d).set(temp, binList.get(d).get(temp)+1);
				
			}
			
			
			for (int n = 0; n < N; n++) {
				if(g == 0) System.out.println("g zero found");
				if(samples.size() == 0) System.out.println("sample zero found");
			//	if(d ==0) System.out.println("bin count :"+binList.get(d).get(n));
				
				binList.get(d).set(n, binList.get(d).get(n) / (g*samples.size()));
			}
			
		
		}
		
	}
	

	
	
	
	

}
              