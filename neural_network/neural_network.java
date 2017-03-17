import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class neural_network {
	
	public  ArrayList<Double> classList=new ArrayList<Double>();
	public SampleData trainingData=new SampleData();
	public Double max=0.0;
	public Double classificationAccuracy=0.0;
	public Hashtable<String, Double> weights= new Hashtable<String, Double>();
	 public  String training_file; 
	 public  String test_file;
	 public int layers; // number of layer including input and output : always greater than 2
	 public  int units_per_layer; 
	 public  int rounds;
	 public  ArrayList<Layer> hiddenLayerList=new ArrayList<Layer>();
	 public Layer inputLayer;
	 public Layer outputLayer;
	 
	 public neural_network(int layers,int units_per_layer, String training_file,String test_file, int rounds)
	 {	  
		 this.training_file=training_file; 
		 this.test_file=test_file;
		 this.layers=layers;
		 this.units_per_layer=units_per_layer; 
		 this.rounds=rounds;
		
		
		 for(int i=0;i<layers-2;i++)
			 hiddenLayerList.add(new Layer(units_per_layer));
	 }
	 
	 public void initializeInputOutputLayer(int inputLayer,int outputLayer)
	 {
		 this.inputLayer=new Layer(inputLayer);
		 this.outputLayer=new Layer(outputLayer);
	 }
	 public void initializeWeights()
	 {
		//random number generator
		Double rangeMin=-0.05;
		Double rangeMax=0.05;
		Random r = new Random();
		//Double tempRandom= rangeMin + (rangeMax - rangeMin) * r.nextDouble();
	
		ArrayList<Layer> tempLayerList=new ArrayList<Layer>();
		tempLayerList.add(inputLayer);
		tempLayerList.addAll(hiddenLayerList);
		tempLayerList.add(outputLayer);
			
			for (int l=0;l<tempLayerList.size()-1;l++) 
			{	
				Layer nL=tempLayerList.get(l+1); int nLayerUnitCount=nL.unitList.size();
				Layer cL=tempLayerList.get(l);int cLayerUnitCount=cL.unitList.size();
				
				for (int c = 0; c < cLayerUnitCount; c++) 
				{
					for (int n = 0; n < nLayerUnitCount; n++) 
					{
						weights.put((l+1)+"|"+n+"-"+c,rangeMin + ((rangeMax - rangeMin) * r.nextDouble()));
					
					}
				}
				
			}
			
				for (int l=1;l<tempLayerList.size();l++) 
				{
					Layer cL=tempLayerList.get(l);
					int cLayerUnitCount=cL.unitList.size();
					for (int c = 0; c < cLayerUnitCount; c++) 
					{
					
							cL.unitList.get(c).W0= rangeMin + ((rangeMax - rangeMin) * r.nextDouble());
					
					}
					
				}
		
		
	 }
	 
	 public void updateWeights(Double learningRate)
	 {
		 
			ArrayList<Layer> tempLayerList=new ArrayList<Layer>();
			tempLayerList.add(inputLayer);
			tempLayerList.addAll(hiddenLayerList);
			tempLayerList.add(outputLayer);
				
				for (int l=0;l<tempLayerList.size()-1;l++) {
					
					Layer nL=tempLayerList.get(l+1); int nLayerUnitCount=nL.unitList.size();
					Layer cL=tempLayerList.get(l);int cLayerUnitCount=cL.unitList.size();
					
					for (int c = 0; c < cLayerUnitCount; c++) {
						for (int n = 0; n < nLayerUnitCount; n++) {
							Double error= nL.unitList.get(n).error; 
							Double Z=cL.unitList.get(c).z;
							weights.put((l+1)+"|"+n+"-"+c,weights.get((l+1)+"|"+n+"-"+c) - (learningRate* error* Z) );
						}
					}
					
				}
		 
	 }
	
	public static void main(String[] args) {
		
		if(args.length < 4 || args.length >5) 
		{
			System.out.println("follow the input format :neural_network <training_file> <test_file> <layers> <units_per_layer> <rounds>");
			return;	
		}
		
		neural_network nn=null;
		if(args.length == 4 && args[2].equals("2"))
		{
			//	  neural_network(int layers,int units_per_layer, String training_file,String test_file, int rounds)

			 nn=new neural_network(Integer.parseInt(args[2]),0,args[0],args[1],Integer.parseInt(args[3]));

		}
		else if(args.length == 5)
		{
			 // neural_network(int layers,int units_per_layer, String training_file,String test_file, int rounds)

			 nn=new neural_network(Integer.parseInt(args[2]),Integer.parseInt(args[3]),args[0],args[1],Integer.parseInt(args[4]));
		}
		else
		{
			System.out.println("follow the input format :neural_network <training_file> <test_file> <layers> <units_per_layer> <rounds>");
			return;
		}
	
		
	
		try {
			nn.readTrainingData(); // reads training samples and set's input output layer of NN
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		
		nn.trainNeuralNetwork();
	
		
		try {
			nn.classifyTestData();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		

	}
	
	public void trainNeuralNetwork()
	{
		this.initializeWeights();
		
		SampleData sampleData=this.trainingData;
		
		//normalize trining data
		sampleData.normalizeTrainingData();
		
		int sampleCount=sampleData.sampleCount;
		
		for (int r = 0; r < this.rounds; r++) 
		{	
			
			Double learningRate=Math.pow(0.98, r);
			for(int s=0;s<sampleCount;s++) // for every class 
			{
				
					ArrayList<Double> sample=sampleData.samples.get(s);
					int DimensionCount=sample.size()-1; //last element is class label
					
					
					//Assign input Unit value from sample normalized value---------------------------------
					
						for (int d = 0; d < DimensionCount; d++) {
							Unit temp=this.inputLayer.unitList.get(d);	
							temp.z=sample.get(d);
							
						}
						
					//Calculating Unit output for all layers-----------------------------------------------
					
							ArrayList<Layer> tempLayerList=new ArrayList<Layer>();
							tempLayerList.add(inputLayer);
							tempLayerList.addAll(hiddenLayerList);
							tempLayerList.add(outputLayer);
							
							for (int l=1;l<tempLayerList.size();l++) {
								
								Layer previousLayer=tempLayerList.get(l-1);
								Layer currentLayer=tempLayerList.get(l);
								
								
								for (int j = 0; j < currentLayer.unitList.size(); j++) {
									
									Double temp=0.0;
									for (int i = 0; i < previousLayer.unitList.size(); i++) {
									temp=temp+  (previousLayer.unitList.get(i).z * weights.get(l+"|"+j+"-"+i));
									}
									temp=temp+currentLayer.unitList.get(j).W0;	
									currentLayer.unitList.get(j).z=sigmoid(temp);
									
									
								}
								
							}
					
					//Error calculation==========================================================================================
							
							//output layer unit error
							int tempClassLabelIndex=classList.indexOf(sample.get(sample.size()-1));
								
							if(tempClassLabelIndex == -1) System.out.println("Error -1 index not found");
							for (int i = 0; i < outputLayer.unitList.size(); i++) 
							{
								Double tempZ=outputLayer.unitList.get(i).z;
								
								
								if(tempClassLabelIndex == i)
									outputLayer.unitList.get(i).error=tempZ * (1-tempZ) *(tempZ - 1);
								else
									outputLayer.unitList.get(i).error=tempZ * (1-tempZ) *(tempZ - 0);
								
								
								//bias update for output layer
								outputLayer.unitList.get(i).W0= outputLayer.unitList.get(i).W0 - (learningRate*outputLayer.unitList.get(i).error);
								
							}
							
						
							/*Layer nL=outputLayer; 
								int nLayerUnitCount=nL.unitList.size();
								Layer cL=tempLayerList.get(tempLayerList.size()-2);
								int cLayerUnitCount=cL.unitList.size();
							
							for (int c = 0; c < cLayerUnitCount; c++) {
							
								for (int n = 0; n < nLayerUnitCount; n++) {
									
									Double error= nL.unitList.get(n).error; 
									Double Z=cL.unitList.get(c).z;
									weights.put((tempLayerList.size()-1)+"|"+n+"-"+c,weights.get((tempLayerList.size()-1)+"|"+n+"-"+c	) - (learningRate * error * Z) );

								}
							}*/
							
							
							// hidden layer error calculations and bias update
							for (int l=tempLayerList.size()-2;l>0;l--) 
							{ // l: L-1 to 2
								
								Layer nextLayer=tempLayerList.get(l+1);
								Layer currentLayer=tempLayerList.get(l);
								
								
								
								//error at node update
								for (int c = 0; c < currentLayer.unitList.size(); c++) 
								{	
									Double temp=0.0;
									for (int n = 0; n < nextLayer.unitList.size(); n++) 
									{
										temp=temp+  nextLayer.unitList.get(n).error * weights.get((l+1)+"|"+n+"-"+c);
									}
				
									currentLayer.unitList.get(c).error=temp * (1.0-currentLayer.unitList.get(c).z) * (currentLayer.unitList.get(c).z);
									
									currentLayer.unitList.get(c).W0= currentLayer.unitList.get(c).W0 - (learningRate*currentLayer.unitList.get(c).error);
									
									//weight update of hidden layer
								/*	for (int p = 0; p < previousL.unitList.size(); p++) {
										Double error= currentLayer.unitList.get(c).error; 
										Double Z=previousL.unitList.get(p).z;
										
										weights.put((l)+"|"+c+"-"+p,weights.get((l)+"|"+c+"-"+p) - (learningRate* error* Z) );
										// System.out.println(r+" >w="+ (l)+"|"+c+"-"+p	 +" > " + weights.get((l)+"|"+c+"-"+p));

									}*/
									
								}
								
								
							}
							
							
						//all weight update at once with updated error	
						this.updateWeights(learningRate);
	
				
			}
		
			
				
				
		}
		
	
	
		
/*		ArrayList<Layer> tempLayerList=new ArrayList<Layer>();
		tempLayerList.add(inputLayer);
		tempLayerList.addAll(hiddenLayerList);
		tempLayerList.add(outputLayer);
		
		for (int l=1;l<tempLayerList.size();l++) {
			
			Layer c=tempLayerList.get(l);// int nLayerUnitCount=c.unitList.size();
			//Layer cL=tempLayerList.get(l);int cLayerUnitCount=cL.unitList.size();
			
			for (int i = 0; i < c.unitList.size(); i++) 
			{
				//System.out.println(l);
				//System.out.println(l+">"+i+ " > W0 ->"+ c.unitList.get(i).W0);
				//System.out.println(l+">"+i+ " 				> error ->"+ c.unitList.get(i).error);
				//System.out.println(l+">"+i+ "								 > z ->"+ c.unitList.get(i).z);
				
			}
		}*/
		//displayWeights();
	
	}
/*	private void displayWeights() {
		
		ArrayList<Layer> tempLayerList=new ArrayList<Layer>();
		tempLayerList.add(inputLayer);
		tempLayerList.addAll(hiddenLayerList);
		tempLayerList.add(outputLayer);
			
			for (int l=0;l<tempLayerList.size()-1;l++) {
				
				Layer nL=tempLayerList.get(l+1); int nLayerUnitCount=nL.unitList.size();
				Layer cL=tempLayerList.get(l);int cLayerUnitCount=cL.unitList.size();
				
				for (int c = 0; c < cLayerUnitCount; c++) {
					for (int n = 0; n < nLayerUnitCount; n++) {
						System.out.println((l+1)+"|"+n+"-"+c +">"+ weights.get((l+1)+"|"+n+"-"+c));
						//weights.put(, rangeMin + (rangeMax - rangeMin) * r.nextDouble());
					}
				}
				
				
				
				
			}
		
	}*/



	public void classifyTestData() throws FileNotFoundException
	{
		int id=0;
		File trainingFile=new File(this.test_file);
		Scanner sc = null;
		sc = new Scanner(trainingFile);
		
		ArrayList<Double> dimensions = null;
		double temp=0.0;
		
		
		
		while(sc.hasNextLine()) // for new test data
		{
			
			Scanner line=new Scanner(sc.nextLine());
			dimensions=new ArrayList<Double>();
	
			while(line.hasNext()) // dimensions of a test data
			{
			
				temp=Double.parseDouble(line.next());
				dimensions.add(temp);
	
			}
			
			
			
			classifyTestRecord(dimensions,id);
			id++;
			//break;
			
			line.close();
		}
		classificationAccuracy=classificationAccuracy/(id-1);
		
		System.out.printf("classification accuracy=%6.4f\n", classificationAccuracy);
	
		sc.close();
	}
	
	public void classifyTestRecord(ArrayList<Double> dimensions,int object_id) {
		
		int DimensionCount=dimensions.size()-1; //last element is class label
		Double maxTemp=trainingData.getMax();
		//Assign input Unit value from sample
		
			for (int d = 0; d < DimensionCount; d++) { // for every dimension in the class
				Unit temp=this.inputLayer.unitList.get(d);	
				temp.z=dimensions.get(d)/maxTemp;
				
			}
			
	
		
				ArrayList<Layer> tempLayerList=new ArrayList<Layer>();
				tempLayerList.add(inputLayer);
				tempLayerList.addAll(hiddenLayerList);
				tempLayerList.add(outputLayer);
				
				for (int l=1;l<tempLayerList.size();l++) {
					
					Layer p=tempLayerList.get(l-1);
					Layer c=tempLayerList.get(l);
					
					
					for (int j = 0; j < c.unitList.size(); j++) {
						
						Double temp=0.0;
						for (int i = 0; i < p.unitList.size(); i++) {
						temp=temp+  (p.unitList.get(i).z * weights.get(l+"|"+j+"-"+i));
						}
						temp=temp+c.unitList.get(j).W0;	
						c.unitList.get(j).z=sigmoid(temp);
						
						
					}
					
				}
				
				int outputCount=outputLayer.unitList.size();
				Double tempMax=outputLayer.unitList.get(0).z;
				ArrayList<Integer> maxIndexList=new ArrayList<Integer>();
				maxIndexList.add(0);
				int tempIndex =0;
				Double accuracy = 0.0;
				for (int i = 1; i < outputCount; i++) {
					if(outputLayer.unitList.get(i).z > tempMax)
					{
						tempMax=outputLayer.unitList.get(i).z;
						//tempIndex=i;
						maxIndexList.clear();
						maxIndexList.add(i);
					}
					else if(outputLayer.unitList.get(i).z == tempMax)
					{
						maxIndexList.add(i);
						
					}
				
				}
				
				double trueClass=dimensions.get(dimensions.size()-1);
			
				
				if(maxIndexList.size() == 1)
				{
					tempIndex=maxIndexList.get(0);
					double y=classList.get(tempIndex);
					
					if( trueClass == y)
					{
						classificationAccuracy++;
						
							accuracy=1.0;
						
					}
					else
					{
						accuracy=0.0;
					}
				}
				else if(maxIndexList.size() > 1)
				{
					ArrayList<Double> tempTieClasses=new ArrayList<Double>();
					for(int i=0;i<maxIndexList.size();i++)
					{
						tempTieClasses.add(classList.get(maxIndexList.get(i)));
					}
					Random r = new Random();
					tempIndex=r.nextInt(maxIndexList.size());
					
					if(tempTieClasses.contains(trueClass))
					{
						accuracy=1.0/maxIndexList.size();
					}
					else
					{
						accuracy=0.0;
					}
					
					
					
				}
				
				
				
				
				
				System.out.printf("ID=%5d, predicted=%3.0f, true=%3.0f, accuracy=%4.2f\n",object_id, classList.get(tempIndex), dimensions.get(dimensions.size()-1), accuracy);
				
	}

	@SuppressWarnings("resource")
	public void readTrainingData() throws FileNotFoundException
	{
		
		Set<Double> classLabelSet = new HashSet<Double>();
		File trainingFile=new File(this.training_file);
		Scanner sc = null;
		sc = new Scanner(trainingFile);
		
		ArrayList<Double> dimensions = null;
		double temp=0;
		
		
		while(sc.hasNextLine()) // for new sample
		{
			//traningSamplesCount++;
			
			Scanner line=new Scanner(sc.nextLine());
			dimensions=new ArrayList<Double>();
	
			while(line.hasNext()) // dimensions of a sample
			{
			
				temp=Double.parseDouble(line.next());
				dimensions.add(temp);
	
			}
			classLabelSet.add(temp);

			
			if(trainingData != null) //existing class
			{
				
				trainingData.add(dimensions);
				
			}
			else //new class
			{
				this.trainingData=new SampleData();
				trainingData.add(dimensions);
				
			}
		
		}
		initializeInputOutputLayer(dimensions.size()-1,classLabelSet.size());
		this.classList.addAll(classLabelSet);
	}
	 public static double sigmoid(double x) {
		    return (1/( 1 + Math.pow(Math.E,(-1*x))));
		  }
}
