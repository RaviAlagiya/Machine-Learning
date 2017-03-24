
import java.util.ArrayList;

import java.util.LinkedList;

public class Node {
	

	public ArrayList<Double> distribution;
	public Integer ID;
	public Integer feature;
	public Double threshold;
	public Double gain;
	public Node left=null;
	public Node right=null;

	
	public SampleData getDataForRightNode(SampleData data) {
		SampleData newSampleData=new SampleData();
		ArrayList<Double> sample;
		for (int i = 0; i < data.sampleCount; i++) {
			sample=data.samples.get(i);
			if(sample.get(feature) >= threshold)
				newSampleData.add(sample);
		}
	
		return newSampleData;
	}
	
	public SampleData getDataForLeftNode(SampleData data) {
		
		SampleData newSampleData=new SampleData();
		ArrayList<Double> sample;
		for (int i = 0; i < data.sampleCount; i++) {
			sample=data.samples.get(i);
			if(sample.get(feature) < threshold)
				newSampleData.add(sample);
		}
	

		return newSampleData;
	}
	

	
	public static void visitBFS(Node root,Integer tree_id)
	{
		LinkedList<Node> queue = new LinkedList<Node>();
		
		queue.add(root);
		
		while(!queue.isEmpty())
		{
			Node current=queue.poll();
			current.displayNode(tree_id);
			
			if(current.left != null)
				queue.add(current.left);
			if(current.right != null)
				queue.add(current.right);

				
			
		}

	}
	public void visitDFS(int tree_id)
	{
		
		if(this.left != null)
			this.left.visitDFS(tree_id);
		
		this.displayNode(tree_id);

	//	System.out.println(this.distribution);
		if(this.right != null)
			this.right.visitDFS(tree_id);
		
		
		
	}
	
	public void displayNode(int tree_id)
	{
		System.out.printf("tree=%2d, node=%3d, feature=%2d, thr=%6.2f, gain=%f\n", tree_id, this.ID, this.feature, this.threshold, this.gain);

	}


	
	public ArrayList<Double> parseRecord(ArrayList<Double> testRecord) {
	
		ArrayList<Double> distribution;
			if(this.distribution == null)
			{
				if(testRecord.get(this.feature) < this.threshold)
					distribution=this.left.parseRecord(testRecord);
				else
					distribution=this.right.parseRecord(testRecord);
					
				
			}
			else
			{
				distribution=  new ArrayList<Double>();
				distribution.addAll(this.distribution);
			}
			
			return distribution;
			
	
		
		
	}

	

}

