import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.sun.xml.internal.ws.util.StringUtils;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import java.sql.*;

public class JavaWeka {
	
	public static void sql_test()
	{
		final String USER = "root";
		final String PASS = "password";
		
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
		final String DB_URL = "jdbc:mysql://localhost/BASE_DE_DADOS";
		
		try
		{
			Class.forName(JDBC_DRIVER);
			
			System.out.println("Connecting to Database...");
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			System.out.println("Creating statement...");
			Statement stmt = conn.createStatement();
			
			String sql = "SELECT * FROM Images;";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				String name = rs.getString("name");
				double d1 = rs.getDouble("d1");
				double d2 = rs.getDouble("d2");
				double d3 = rs.getDouble("d3");
				double d4 = rs.getDouble("d4");
				
				System.out.println(name + " " + d1 + " " + d2 + " " + d3 + " " + d4);
			}
			
			System.out.println();
		}
		catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void weka_test()
	{
		BufferedReader breader = null;
		try {
			breader = new BufferedReader(new FileReader ("/home/lucas/Downloads/Compressed/weka-3-7-13/data/diabetes.arff"));
			Instances train = new Instances (breader);
			train.setClassIndex(train.numAttributes() - 1);
			
			breader.close();
			
			J48 tree = new J48();
			tree.buildClassifier(train);
			Evaluation eval = new Evaluation(train);
			eval.crossValidateModel(tree, train, 10, new Random(1));
			

			//System.out.println(eval.toSummaryString("\nResults\n======\n", true));
			//System.out.println(eval.fMeasure(1)+ " "+ eval.precision(1)+ " " + eval.recall(1));
			
			String s = tree.toString();
			String[] lines = s.split("\\n");
			
			
			System.out.println(s);
			
			ArrayList<ArrayList<String>> conds = new ArrayList<ArrayList<String>>();
			for (int i = 0; i < lines.length; i++)
			{
				String linha = lines[i];
				int depth = 0;
				
				for (int j = 0; j < linha.length(); j++)
				{
					if (linha.charAt(j) == '|')
						depth++;
				}
				
				String[] words = linha.split(" +");
				
				
				int comparator_index = -1;
				boolean matched = false;
				for (int j = 0; j < words.length; j++)
				{
					if (words[j].matches("[<>=]+"))
					{
						comparator_index = j;
						matched = true;
						break;
					}
				}
				

				if (matched)
				{
					conds.ensureCapacity(depth + 1);
					if (conds.size() <= depth)
						conds.add(new ArrayList<String>());
					else
						conds.set(depth, new ArrayList<String>());			
					
					if (conds.get(depth).size() < 3)
					{
						conds.get(depth).add(0, words[comparator_index - 1]);
						conds.get(depth).add(1, words[comparator_index    ]);
						conds.get(depth).add(2, words[comparator_index + 1]);
					}
					else
					{
						conds.get(depth).set(0, words[comparator_index - 1]);
						conds.get(depth).set(1, words[comparator_index    ]);
						conds.get(depth).set(2, words[comparator_index + 1]);				
					}
				}
				
				if (matched && linha.matches(".*tested.*"))
				{
					System.out.println(conds.subList(0, depth + 1));
				}
				
				
				//System.out.println("Profundidade: " + depth + "\n\t" + linha);
				//System.out.println(linha + "\t\t" + linha.matches(".*tested_.*"));
			}
			
			
			//System.out.println(eval.toSummaryString());
			//System.out.println(eval.toClassDetailsString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) throws Exception{
		//weka_test();
		sql_test();
	}
}