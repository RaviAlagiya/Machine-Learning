import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class naive_bayes {
	public static Hashtable<Double, SampleClass> classSampleMap;

	public static int traningSamplesCount=0; 

	public static File trainingFile = null;
	public static File testFile = null;
	public static Integer N = null;
	@SuppressWarnings({ "resource", "unchecked", "rawtypes" })
	public static void main(String[] args) {
		
		if(args.length >= 3)
		{
			 trainingFile=new File(args[0]);
			 testFile=new File(args[1]);
		}
		else
		{
			System.out.println("Please input file path as an argument");
			return ;
		}

		//read training data
		
		readTrainingData();
		
		if(args.length > 3)
		{
			N=new Integer(Integer.parseInt(args[3]));
			
		 if(args[2].equalsIgnoreCase("histograms"))
			 calculateHistogram(N);
		if(args[2].equalsIgnoreCase("mixtures"))	
			calculateMixture(N);
		}
		else
		{	
			if(args[2].equalsIgnoreCase("gaussians"))	
				calculateGaussians();
			
		}
	
	
		

	}
	
	@SuppressWarnings("resource")
	public static void readTrainingData()
	{

		Scanner sc = null;
		try {
			sc = new Scanner(trainingFile);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			return;
		}
		
		
		ArrayList<Double> dimensions;//=new ArrayList<Double>(); // a sample
		 classSampleMap = new Hashtable<Double, SampleClass>(); // class samples table
		
		
		double temp=0;
		SampleClass c;

		//reading input
		
		while(sc.hasNextLine()) // for new sample
		{
			traningSamplesCount++;
			
			Scanner line=new Scanner(sc.nextLine());
			dimensions=new ArrayList<Double>();
			while(line.hasNext()) // dimensions of a sample
			{
				temp=Double.parseDouble(line.next());
				dimensions.add(temp);
	
			}
			//dimensions.
			dimensions.remove(dimensions.size()-1); // removing class label from dimension list
			
			 c=(SampleClass) classSampleMap.get(temp);
			
			if(c != null) //existing class
			{
				
				c.add(dimensions);
				
			}
			else //new class
			{
				c=new SampleClass();
				c.add(dimensions);
				classSampleMap.put(temp, c);
				c=null;
			}
		
		}
		
	}
	
	@SuppressWarnings("resource")
	private static void calculateGaussians() {
		
		Enumeration<Double> enumeration = classSampleMap.keys();

		List<Double> list = Collections.list(enumeration);
        Collections.sort(list);
		
		SampleClass classX;
		double mean,sd;
		
		for(int i=0;i<list.size();i++) // for every class value
		{
			classX=(SampleClass) classSampleMap.get(list.get(i));
			
			if(classX == null) continue;
			
			int tempDimensionLength=classX.samples.get(0).size();
			int tempSampleCount=classX.samples.size();
			for(int j=0;j<tempDimensionLength;j++) // for every sample in class
			{
				
				mean=0;
				sd=0;
				for(int k=0;k<tempSampleCount;k++)  // for every dimension in a sample
				{
				
					double temp_value=(Double) classX.samples.get(k).get(j);
					
					mean=mean +temp_value ;
					
				}
				mean=mean/(double)tempSampleCount;
				for(int k=0;k<tempSampleCount;k++)  // for every dimension in a sample
				{
					double temp_value=(Double) classX.samples.get(k).get(j);
					
					sd=sd + ((temp_value - mean) * (temp_value - mean));
					
				}
				sd=(double)sd/(double)(tempSampleCount-1);
				sd=Math.sqrt(sd);
				ArrayList<Double> temp=new ArrayList<Double>();
				temp.add(mean);
				if(sd<0.01) sd=0.01;
				temp.add(sd);
				classX.gaussians1D.add(temp);
				//Class %d, attribute %d, mean = %.2f, std = %.2f
				System.out.printf("Class %.0f, attribute %d, mean = %.2f, std = %.2f \n",list.get(i),j,mean,sd);
			
				
			}
			
		
		}
		
		//Testing Test data
		
		Scanner sc = null;
		try {
			sc = new Scanner(testFile);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			return;
		}
		
		
		ArrayList<Double> dimensions;//=new ArrayList<Double>(); // a sample
		
		double temp=0;double true_class=-1;int object_id=-1;double predicted_class=-1;double accuracy=0;double classification_Accuracy=0;
		
		//reading input
		
		while(sc.hasNextLine()) // for new test sample
		{
			accuracy=0;
			object_id++;
			Scanner line=new Scanner(sc.nextLine());
			dimensions=new ArrayList<Double>();
			while(line.hasNext()) // dimensions of a test sample
			{
				temp=Double.parseDouble(line.next());
				dimensions.add(temp);
	
			}
			true_class=temp;
			
			double tempDimClassProbability=1.0;
			
			ArrayList<Double> classProbabilitiesArray=new ArrayList<Double>();
			for(int c=0;c<list.size();c++) // for every class value
			{
				tempDimClassProbability=1.0;
				classX=(SampleClass) classSampleMap.get(list.get(c));
				for (int d = 0; d < dimensions.size()-1; d++) {
					
					tempDimClassProbability=(tempDimClassProbability)*(N(dimensions.get(d),classX,d));
					
				}
				tempDimClassProbability=tempDimClassProbability*((double)classX.sampleCount/traningSamplesCount)/Px(dimensions);
				classProbabilitiesArray.add(tempDimClassProbability);
			}
			double temp_max_prob=Collections.max(classProbabilitiesArray);
			ArrayList<Integer> temp_index;
			temp_index=tieCheck(temp_max_prob,classProbabilitiesArray);
			if(temp_index.get(1) > 1)
			{
				if( list.get(temp_index.get(0)) == true_class)
				{
					accuracy=(double)1/(temp_index.get(1));
				}
					
				else
					accuracy=0;
			}
			else
			{
				if( list.get(temp_index.get(0)) == true_class)
					accuracy=1;
				else
					accuracy=0;
			}
		
			classification_Accuracy=classification_Accuracy+accuracy;
		
			System.out.printf("ID=%5d, predicted=%3.0f, probability = %.4f, true=%3.0f, accuracy=%4.2f\n", 
	                  object_id, list.get(temp_index.get(0)),temp_max_prob, true_class, accuracy);

			
		}
		classification_Accuracy=classification_Accuracy/(object_id+1);
		System.out.printf("classification accuracy=%6.4f\n", classification_Accuracy);
		
		
	}

	private static ArrayList<Integer> tieCheck(double temp_max_prob, ArrayList<Double> tempDimClassProbability) {
		
		ArrayList<Integer> index = new ArrayList<Integer>();
		 Set<Double> set = new HashSet<Double>();
		 ArrayList<Integer> indexList=new ArrayList<Integer>();
		 set.add(temp_max_prob);
		 index.add(-1);
		 index.add(-1);
		 
		 for (int i = 0; i < tempDimClassProbability.size(); i++) {
			
			 if(!set.add(tempDimClassProbability.get(i)))
			 {
				 indexList.add(i);
			 }
				 
		}
		 
		 if(indexList.size() > 1)
		 {
			 Random r=new Random();
			 index.set(0, r.nextInt(indexList.size()));
			// index[0]=;
			 
		 }
		 else
		 {
			 index.set(0, indexList.get(0));
		 }
		 
		 index.set(1,indexList.size());
	
		
		
		return index;
	}

	private static double Px(ArrayList<Double> X) {
		
		double px=0.0;
		@SuppressWarnings("rawtypes")
		Enumeration enumeration = classSampleMap.keys();
        @SuppressWarnings("unchecked")
		List<Integer> list = Collections.list(enumeration);
        Collections.sort(list);
		
		SampleClass classX = classSampleMap.get(list.get(0));
		
		int tempDim=classX.samples.get(0).size();
		double temp=1.0;
		for(int c=0;c<list.size();c++) // for every class value
		{
			temp=1.0;
			classX=(SampleClass) classSampleMap.get(list.get(c));
			for (int d = 0; d < tempDim; d++) {
				temp=temp*N( X.get(d),classX,d);
				
				
			}
			
			px=px+temp*((double)classX.sampleCount/traningSamplesCount);
		}
		
		return px;
	}

	@SuppressWarnings("resource")
	private static void calculateMixture(int N) {
		
		
		Enumeration<Double> enumeration = classSampleMap.keys();
        List<Double> list = Collections.list(enumeration);
        Collections.sort(list);
   
       SampleClass classX;
		for(int c=0;c<list.size();c++) // for every class value
		{
			  classX=(SampleClass) classSampleMap.get(list.get(c));
			  
			  int tempDimensionLength=classX.samples.get(0).size();
			  int tempSampleSize=classX.samples.size();
			  classX.initializeGaussiansMixture(N); // initializing gaussian values for mean sd and weight and pijs
	
				for (int d = 0; d < tempDimensionLength; d++) { //for each dimension
				       
					for (int l = 0; l < 50; l++) {
						ArrayList<Double> mTempList=new ArrayList<Double>();
						ArrayList<Double> sdTempList=new ArrayList<Double>();
						ArrayList<Double> wTempList=new ArrayList<Double>();
			
						//e step
						classX.updatePijList(N,d);					
						for (int i = 0; i < N; i++) {
							
							Double w = 0.0,sd = 0.0,m = 0.0;
							Double spij=0.0,meanU=0.0,sU=0.0,WD=0.0;
							for (int s = 0; s < tempSampleSize; s++) {
								Double jx=(Double)classX.samples.get(s).get(d);
								Double pij=classX.pijList.get(s).get(d).get(i);
								spij=spij+pij;
								meanU=meanU+ (pij * jx);
								sU=sU+(pij*Math.pow((jx-(classX.meanList.get(d).get(i))), 2));
							}
							
							for (int n = 0; n < N; n++) {
								for (int s = 0; s < tempSampleSize; s++) {
								//	double jx=(Double)classX.samples.get(s).get(d);
									WD=WD+classX.pijList.get(s).get(d).get(n);
									
								}
							}
				
							
							w=(spij/WD);
							sd=Math.sqrt(sU/spij);
							m=(meanU/spij);
						
				
						if(sd< 0.01) 
								sd= 0.01;
						//M step
						mTempList.add(m);
						sdTempList.add(sd);
						wTempList.add(w);
					
							
					}
						classX.wList.set(d,wTempList);
						classX.meanList.set(d,mTempList);
						classX.sdList.set(d,sdTempList);
						
					
						
						
				}
					for (int g = 0; g < N; g++) {
						
						System.out.printf("Class %.0f, attribute %d, Gaussian %d, mean = %.2f, std = %.2f \n",
								list.get(c),d,g,classX.meanList.get(d).get(g),classX.sdList.get(d).get(g));
					}
					
			}
			
		}
		

		//Testing  data
		
		Scanner sc = null;
		try {
			sc = new Scanner(testFile);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			return;
		}
		
		
		ArrayList<Double> dimensions;
		
		double temp=0;double true_class=-1;int object_id=-1;double predicted_class=-1;double accuracy=0;double classification_Accuracy=0;
		//SampleClass c;


		while(sc.hasNextLine()) // for new test sample
		{
			accuracy=0;
			object_id++;
			Scanner line=new Scanner(sc.nextLine());
			dimensions=new ArrayList<Double>();
			while(line.hasNext()) // dimensions of a test sample
			{
				temp=Double.parseDouble(line.next());
				dimensions.add(temp);
	
			}
			true_class=temp;
		
			enumeration = classSampleMap.keys();
	        list = Collections.list(enumeration);
	        Collections.sort(list);
			//Test Sample is ready to test : last dimension is class true label
			double tempDimClassProbability=1.0;
			
			ArrayList<Double> classProbabilitiesArray=new ArrayList<Double>();
	
			double tempPx=Px_gaussian(dimensions,N);
			for(int c=0;c<list.size();c++) // for every class value
			{
				tempDimClassProbability=1.0;
				classX=(SampleClass) classSampleMap.get(list.get(c));
				for (int d = 0; d < dimensions.size()-1; d++) {
					
					tempDimClassProbability=(tempDimClassProbability)*( (M(dimensions.get(d),classX,d,N) )) ;
				
				}
				tempDimClassProbability=tempDimClassProbability*((double)classX.sampleCount/traningSamplesCount);
				
				tempDimClassProbability=tempDimClassProbability/tempPx;
			
				classProbabilitiesArray.add(tempDimClassProbability);
			}
			double temp_max_prob=Collections.max(classProbabilitiesArray);
			ArrayList<Integer> temp_index;
				temp_index=tieCheck(temp_max_prob,classProbabilitiesArray);
		
			if(temp_index.get(1) > 1)
			{
				if( list.get(temp_index.get(0)) == true_class)
					accuracy=(double)1.0/temp_index.get(1);
				else
					accuracy=0;
			}
			else
			{
				if( list.get(temp_index.get(0)) == true_class)
					accuracy=1;
				else
					accuracy=0;
			}
			
			classification_Accuracy=classification_Accuracy+accuracy;
		
			System.out.printf("ID=%5d, predicted=%3.0f, probability = %.4f, true=%3.0f, accuracy=%4.2f\n", 
	                  object_id, list.get(temp_index.get(0)), temp_max_prob, true_class, accuracy);

			
		
			line.close();	
		}
		classification_Accuracy=classification_Accuracy/(double)(object_id+1);
		System.out.printf("classification accuracy=%6.4f\n", classification_Accuracy);
		

	
	}

	


	private static double M(Double x, SampleClass classX, int d,int N) {
		double m=0.0;
		for (int j = 0; j < N; j++) {
			m=m+(N(j, x, classX,d)) *(Double) classX.wList.get(d).get(j);
			
		}

		return m;
	}

	private static void calculateHistogram(int N) {

		
		Enumeration<Double> enumeration = classSampleMap.keys();
      
		List<Double> list = Collections.list(enumeration);
        Collections.sort(list);
        SampleClass classX;
		for(int c=0;c<list.size();c++) // for every class value
		{
			classX=(SampleClass) classSampleMap.get(list.get(c));
			  int tempDimensionLength=classX.samples.get(0).size();
			
			  classX.initializeHistogram(N); // initializing gaussian
			  classX.calculateBinProbability(N);
			  
			  for (int d = 0; d < tempDimensionLength; d++) {
				  for (int n = 0; n < N; n++) {
					System.out.printf("Class %.0f, attribute "+d+", bin "+n+", P(bin | class) = %.2f",list.get(c),classX.binList.get(d).get(n));
					System.out.println("");
				}
				
			}
			 
		}
		
		
		
		//test Data

		Scanner sc = null;
		try {
			sc = new Scanner(testFile);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			return;
		}
		
		
		ArrayList<Double> dimensions;//=new ArrayList<Double>(); // a sample
		
		double temp=0;double true_class=-1;int object_id=-1;double predicted_class=-1;double accuracy=0;double classification_Accuracy=0;
		
		//reading input
		
		while(sc.hasNextLine()) // for new test sample
		{
			accuracy=0;
			object_id++;
			Scanner line=new Scanner(sc.nextLine());
			dimensions=new ArrayList<Double>();
			while(line.hasNext()) // dimensions of a test sample
			{
				temp=Double.parseDouble(line.next());
				dimensions.add(temp);
	
			}
			true_class=temp;
			
			double tempDimClassProbability=1.0;
			double sum=0.0;
			
			ArrayList<Double> classProbabilitiesArray=new ArrayList<Double>();
			for(int c=0;c<list.size();c++) // for every class value
			{
				tempDimClassProbability=1.0;
				classX=(SampleClass) classSampleMap.get(list.get(c));
				for (int d = 0; d < dimensions.size()-1; d++) {
					double tempTerm=PXbinClass(dimensions.get(d),classX,d);
					if(tempTerm == 0) tempTerm=0.0000000000000000001;//=(double)1/N;
					tempDimClassProbability=(tempDimClassProbability)*(tempTerm);
					
				}
				tempDimClassProbability=tempDimClassProbability*((double)classX.sampleCount/traningSamplesCount)/PXbin(dimensions);
				sum=sum+tempDimClassProbability;
				//
				classProbabilitiesArray.add(tempDimClassProbability);
			}
		double temp_max_prob=Collections.max(classProbabilitiesArray);
			ArrayList<Integer> temp_index;
			temp_index=tieCheck(temp_max_prob,classProbabilitiesArray);
		
			if(temp_index.get(1) > 1)
			{
				
				if( list.get(temp_index.get(0)) == true_class)
				{
					accuracy=(double)1/(temp_index.get(1));
					
				}
					
				else
					accuracy=0;
			}
			else
			{
			
				if( list.get(temp_index.get(0)) == true_class)
					accuracy=1;
				else
					accuracy=0;
			}
	
			
			classification_Accuracy=classification_Accuracy+accuracy;
		
			System.out.printf("ID=%5d, predicted=%3.0f, probability = %.4f, true=%3.0f, accuracy=%4.2f\n", 
	                  object_id, list.get(temp_index.get(0)),temp_max_prob, true_class, accuracy);

			
		}
		classification_Accuracy=classification_Accuracy/(object_id);
		System.out.printf("classification accuracy=%6.4f\n", classification_Accuracy);
		
		
	
	}
	
	private static double PXbin(ArrayList<Double> X) {
		double px=0.0;
		Enumeration<Double> enumeration = classSampleMap.keys();
		List<Double> list = Collections.list(enumeration);
        Collections.sort(list);
		
		SampleClass classX = classSampleMap.get(list.get(0));
		int tempDim=classX.samples.get(0).size();
		double temp=1.0;
		for(int c=0;c<list.size();c++) // for every class value
		{
			temp=1.0;
			classX=(SampleClass) classSampleMap.get(list.get(c));
			for (int d = 0; d < tempDim; d++) {
				double tempTerm=PXbinClass( X.get(d),classX,d);
				if(tempTerm == 0)tempTerm=0.0000000000000000001;
				temp=temp*tempTerm;
				
				
			}
			
			px=px+(temp*((double)classX.sampleCount/(double)traningSamplesCount));
		}
	//if(px==0) System.out.println("px zero found");
		return px;
	
	}
	private static double Px_gaussian(ArrayList<Double> X,int N) {
		double px=0.0;
		
		Enumeration<Double> enumeration = classSampleMap.keys();
		List<Double> list = Collections.list(enumeration);
        Collections.sort(list);
		
		SampleClass classX = classSampleMap.get(list.get(0));
		int tempDim=classX.samples.get(0).size();
		double tempDimClassProbability=1.0,finalProb=0.0;
		for(int c=0;c<list.size();c++) // for every class value
		{
			tempDimClassProbability=1.0;
			classX = classSampleMap.get(list.get(c));
			for (int dim = 0; dim < tempDim; dim++) {
				
				 tempDimClassProbability = (tempDimClassProbability)*( (M(X.get(dim),classX,dim,N) )) ;
			
			}
	
				finalProb=finalProb+tempDimClassProbability*((double)classX.sampleCount/(double)traningSamplesCount) ;
			
		}
	
		return finalProb;
	
	}

	private static double PXbinClass(Double x, SampleClass classX, int d) {
		double s=classX.minArray.get(d);
		double g=classX.maxArray.get(d);
		//System.out.println("s : " +s+ "l : " +g+"x : "+x);
		g=(g-s)/(N-3);
		if(g < 0.0001) g=0.0001;
		
		
			int temp =(int) ((x - s + g/2)/g) ;
			temp++;
			
			if(temp >= N) 
			{
				return classX.binList.get(d).get(N-1);
				//binList.get(d).set(N-1, binList.get(d).get(N-1)+1);	
			}
			else if(temp < 0)
			{
				return classX.binList.get(d).get(0);
			//	binList.get(d).set(0, binList.get(d).get(0)+1);
				
			}
			else 
			{
				return classX.binList.get(d).get(temp);
				//binList.get(d).set(temp, binList.get(d).get(temp)+1);
				
			}
		
	}

	private static double N(int i,double x,SampleClass c,int dimension)
	{
	
		double sigma=(Double) c.sdList.get(dimension).get(i);
		double mean=(Double) c.meanList.get(dimension).get(i);
	
		double temp =(1.0)/(Math.exp(((x-mean)*(x-mean))/(2*sigma * sigma))*(sigma*Math.sqrt(2*Math.PI)));
		
		return temp;
		
	}
	
	//used for 1-D gaussian
	private static double N(double x,SampleClass c,int dimension)
	{

		double sigma=(double) c.gaussians1D.get(dimension).get(1);
		double mean=(double) c.gaussians1D.get(dimension).get(0);
	
		return (Math.exp(-((x-mean)*(x-mean))/(2*sigma * sigma)))/(sigma*Math.sqrt(2*Math.PI));
	}
	
	public static double Pij(int i,double x,SampleClass c,int dimension,int N)
	{
		double px=0.0;
		for (int j = 0; j < N; j++) {
			px=px+ (N( j, x, c, dimension) *  c.wList.get(dimension).get(j));
		}
		
		double temp=(N( i, x, c, dimension) * c.wList.get(dimension).get(i))/ px;
		
		return temp;
		
		 
	}
	

}
