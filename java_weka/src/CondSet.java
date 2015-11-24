import java.util.ArrayList;
import java.util.List;


public class CondSet {
	PredictionInfo info;
	List<ArrayList<String>> conds;
	
	CondSet (List<ArrayList<String>> newConds, PredictionInfo newInfo)
	{
		conds = newConds;
		info = newInfo;
	}
	
	@Override
	public String toString()
	{
		return conds.toString();
	}
	
	public String elementaryCondition(int index)
	{
		ArrayList<String> condition = conds.get(index);
		
		String s = condition.get(0) + " " + condition.get(1) + " " + condition.get(2);
		
		return s;
	}
	
	public String getQuery()
	{
		String query = "SELECT name FROM Image_Descriptors WHERE ";
		int size = conds.size();
		for (int i = 0; i < size - 1; i++)
		{
			query = query + elementaryCondition(i) + " AND ";
		}
		query = query + elementaryCondition(size - 1);
		
		return query;
	}
}
