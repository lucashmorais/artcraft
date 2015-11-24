import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.sun.xml.internal.ws.util.StringUtils;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.gui.BrowserHelper;

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
	
	public static File weka_generate_random_relation(String file_name)
	{
		File training_file = new File(file_name);
		Random rnd = new Random();
		
		try
		{
			FileWriter writer = new FileWriter(training_file);
			writer.write("@relation art_training_set\n");
			for (int i = 0; i < 108; i++)
			{
				writer.write("@attribute 'd" + i + "' numeric\n");
			}
			writer.write("@attribute 'evaluation' { liked, disliked }\n");
			writer.write("@data\n");
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/BASE_DE_DADOS", "root", "password");
			Statement st = conn.createStatement();
			
			ResultSet all_descriptors = st.executeQuery("SELECT * FROM Image_Descriptors");
			
			while(all_descriptors.next())
			{
				for (int i = 2; i <= 109; i++)
				{
					writer.write("" + all_descriptors.getDouble(i) + ",");
				}
				
				if (rnd.nextBoolean())
				{
					writer.write("liked\n");
				}
				else
					writer.write("disliked\n");
			}
			
			writer.close();
		}
		catch (IOException | SQLException e)
		{
			e.printStackTrace();
		}
		
		return training_file;
	}
	
	public static String bootstrap()
	{
		File training_file = new File("training_file.arff");
		String select_string = null;
		
		try
		{
			FileWriter writer = new FileWriter(training_file);
			writer.write("@relation art_training_set\n");
			for (int i = 0; i < 108; i++)
			{
				writer.write("@attribute 'd" + i + "' numeric\n");
			}
			writer.write("@attribute 'evaluation' { liked, disliked }\n");
			writer.write("@data\n");
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/BASE_DE_DADOS", "root", "password");
			Statement st = conn.createStatement();
			
			writer.close();
		}
		catch (IOException | SQLException e)
		{
			e.printStackTrace();
		}
		
		return "SELECT name FROM Image_Descriptors";
	}
	
	public static void appendTest(String file_name)
	{
		File training_file = new File(file_name);
		
		try {
			FileWriter writer = new FileWriter(training_file, true);
			BufferedWriter bwriter = new BufferedWriter(writer);
			PrintWriter pwriter = new PrintWriter(bwriter);
			
			System.out.println("Entrou ");
			
			pwriter.println("Teste de append");
			
			pwriter.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static String feedWithInfo(boolean like, String imageName)
	{
		File training_file = new File("training_file.arff");
		
		try
		{
			FileWriter writer = new FileWriter(training_file, true);
			PrintWriter pwriter= new PrintWriter(new BufferedWriter(writer));
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/BASE_DE_DADOS", "root", "password");
			Statement st = conn.createStatement();
			
			ResultSet descriptor = st.executeQuery("SELECT * FROM Image_Descriptors WHERE name = \"" + imageName + "\";");
//			System.out.println("SELECT * FROM Image_Descriptors WHERE name = \"" + imageName + "\";");
			
			while(descriptor.next())
			{
				for (int i = 2; i <= 109; i++)
				{
					pwriter.write("" + descriptor.getDouble(i) + ",");
				}
				
				if (like)
				{
					pwriter.write("liked\n");
				}
				else
					pwriter.write("disliked\n");
			}
			
			pwriter.close();
		}
		catch (IOException | SQLException e)
		{
			e.printStackTrace();
		}
		
		return runTraining();
	}
	
	public static String runTraining()
	{
		File f = new File("training_file.arff");
		Instances training_set = null;
		J48 tree = null;
		
		try
		{
			FileReader reader = new FileReader(f);
			BufferedReader breader = new BufferedReader(reader);
			
			training_set = new Instances(breader);
			tree = new J48();
			training_set.setClassIndex(training_set.numAttributes() - 1);
			tree.buildClassifier(training_set);

			breader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		ArrayList<CondSet> conditions = print_conditions(tree);
		if (conditions.size() > 0)
		{
			Collections.sort(conditions);
//			for (CondSet s: conditions)
//				System.out.println(s.getQuery() + ", " + s.info);
			return conditions.get(0).getQuery();
		}
		else
			return "SELECT * FROM Image_Descriptors";
	}
	
	public static PredictionInfo getPredictionInfo(String linha)
	{		
		double hitCount = 0;
		double missCount = 0;
		double hitRatio = 0;
		
		String[] parts = linha.split("[ \ta-zA-Z_():<=>/|]+");
		
		int length = parts.length;

		
		if (!linha.contains("/"))
		{
			hitRatio = 999;
			hitCount = Double.parseDouble(parts[length - 1]);
			missCount = 0;
		}
		else
		{
			hitCount = Double.parseDouble(parts[length - 2]);
			missCount = Double.parseDouble(parts[length - 1]);
			hitRatio = hitCount / missCount;
		}
		
//		System.out.println("Hit ratio: " + hitRatio);
		return new PredictionInfo(hitRatio, hitCount, missCount);
	}
	
	public static ArrayList<CondSet> print_conditions(J48 tree)
	{		
		String s = tree.toString();
		String[] lines = s.split("\\n");
		ArrayList<CondSet> conditions = new ArrayList<CondSet>();
		
		ArrayList<ArrayList<String>> conds = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < lines.length; i++)
		{
			String linha = lines[i];
//			System.out.println(linha);
			if (linha.length() == 0 || linha.charAt(0) == ':')
				continue;
			
			int depth = 0;
			
			for (int j = 0; j < linha.length(); j++)
			{
				if (linha.charAt(j) == '|')
					depth++;
			}
			
			String[] words = linha.split("[ :]+");
			
			
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
			
			if (matched && linha.matches(".*tested_positive.*") || linha.matches(".*[^s]liked.*"))
			{

				CondSet set = new CondSet(new ArrayList<ArrayList<String>>(conds.subList(0, depth + 1)), getPredictionInfo(linha));
				conditions.add(set);
			}
		}
		
		return conditions;
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
			
			print_conditions(tree);
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
		//sql_test();
		
//		weka_generate_random_relation("training_file.arff");
//		runTraining();
		
//		appendTest("training_file.arff");
		
		bootstrap();
		System.out.println(feedWithInfo(true, "00battle.jpg"));
		System.out.println(feedWithInfo(true, "00acount.jpg"));
		System.out.println(feedWithInfo(false, "00beaun1.jpg"));
		System.out.println(feedWithInfo(true, "0calm.jpg"));
		System.out.println(feedWithInfo(false, "00flock.jpg"));
	}
}