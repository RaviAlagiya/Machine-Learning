import java.util.ArrayList;

public class Layer {
	public ArrayList<Unit> unitList=new ArrayList<Unit>();
	public Layer(int units_per_layer)
	{
		for(int i=0;i<units_per_layer;i++)
		unitList.add(new Unit());
	}

}
