import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class homework {
	//public ExpertSystem expertSystem;
	public static int counter;

	static {
		
		counter = 0;
	}

	public static void main(String[] args) {
		
		ExpertSystem expertSystem = new ExpertSystem();
		
		try {

			// Read file
			BufferedReader br = new BufferedReader(new FileReader(new File(".\\src\\input.txt")));

			////////////////////// STORE QUERIES///////////////////////////////////////
			int queryCount = Integer.parseInt(br.readLine());
			ArrayList<Sentence> queries = new ArrayList<Sentence>();
			ArrayList<Sentence> facts = new ArrayList<Sentence>();
			for (int i = 0; i < queryCount; i++) {
				ArrayList<Predicate> sentence = new ArrayList<Predicate>();
				String queryString = br.readLine().replaceAll("\\s+", "");
				String premise = queryString;
				Boolean isNegated = false;
				if (premise.startsWith("~")) {
					isNegated = true;
					premise = premise.substring(1);
				}
				int tillIndex = premise.indexOf("(");
				String predicateName = premise.substring(0, tillIndex);
				String premisePredicateArguments = premise.substring(tillIndex + 1, premise.length() - 1);
				String arguments[] = premisePredicateArguments.split(",");
				ArrayList<Element> elem_args = new ArrayList<Element>();
				for (String argument : arguments) {
					elem_args.add(new Element(argument));
				}
				Predicate predicate = new Predicate(predicateName, elem_args, isNegated);
				sentence.add(predicate);
				queries.add(new Sentence(sentence));
			}

			///////////////////////////////// TELL ES///////////////////////////////////////
			//System.out.println("**********KB************");

			Map<String, String> mapper = new HashMap<String, String>();

			int factCount = Integer.parseInt(br.readLine());
			// ArrayList<Sentence> facts = new ArrayList<Sentence>();
			for (int i = 0; i < factCount; i++) {
				ArrayList<Predicate> sentence = new ArrayList<Predicate>();
				String factString = br.readLine().replaceAll("\\s+", "");
				if (factString.contains("=>")) {
					// System.out.println(factString);
					String split[] = factString.split("=>", 2);
					String premise = split[0];
					String consequence = split[1];

					if (premise.contains("&")) {
						String splitPremise[] = premise.split("&");
						for (String premisePredicate : splitPremise) {
							Boolean isNegated = false;
							if (premisePredicate.startsWith("~")) {
								isNegated = true;
								premisePredicate = premisePredicate.substring(1);
							}
							int tillIndex = premisePredicate.indexOf("(");
							String predicateName = premisePredicate.substring(0, tillIndex);
							String premisePredicateArguments = premisePredicate.substring(tillIndex + 1,
									premisePredicate.length() - 1);
							String arguments[] = premisePredicateArguments.split(",");
							ArrayList<Element> elem_args = new ArrayList<Element>();
							for (String argument : arguments) {								
								if(Character.isLowerCase(argument.charAt(0)))
								{
									if (!mapper.containsKey(argument)) {
										mapper.put(argument, "var" + (counter++));
									}
									elem_args.add(new Element((String) mapper.get(argument)));
								}
								else {
									elem_args.add(new Element(argument));
								}
							}
							Predicate predicate = new Predicate(predicateName, elem_args, !(isNegated));
							sentence.add(predicate);
						}
					} else {
						Boolean isNegated = false;
						if (premise.startsWith("~")) {
							isNegated = true;
							premise = premise.substring(1);
						}
						int tillIndex = premise.indexOf("(");
						String predicateName = premise.substring(0, tillIndex);
						String premisePredicateArguments = premise.substring(tillIndex + 1, premise.length() - 1);
						String arguments[] = premisePredicateArguments.split(",");
						ArrayList<Element> elem_args = new ArrayList<Element>();
						for (String argument : arguments) {
							if(Character.isLowerCase(argument.charAt(0)))
							{
								if (!mapper.containsKey(argument)) {
									mapper.put(argument, "var" + (counter++));
								}
								elem_args.add(new Element((String) mapper.get(argument)));
							}
							else {
								elem_args.add(new Element(argument));
							}
						}
						Predicate predicate = new Predicate(predicateName, elem_args, !(isNegated));
						sentence.add(predicate);
					}

					Boolean isNegated = false;
					if (consequence.startsWith("~")) {
						isNegated = true;
						consequence = consequence.substring(1);
					}
					int tillIndex = consequence.indexOf("(");
					String consName = consequence.substring(0, tillIndex);
					String consPredicateArguments = consequence.substring(tillIndex + 1, consequence.length() - 1);
					String arguments[] = consPredicateArguments.split(",");
					ArrayList<Element> elem_args = new ArrayList<Element>();
					for (String argument : arguments) {
						if(Character.isLowerCase(argument.charAt(0)))
						{
							if (!mapper.containsKey(argument)) {
								mapper.put(argument, "var" + (counter++));
							}
							elem_args.add(new Element((String) mapper.get(argument)));
						}
						else {
							elem_args.add(new Element(argument));
						}
					}
					Predicate predicate = new Predicate(consName, elem_args, isNegated);
					sentence.add(predicate);

				} else {
					String premise = factString;
					Boolean isNegated = false;
					if (premise.startsWith("~")) {
						isNegated = true;
						premise = premise.substring(1);
					}
					int tillIndex = premise.indexOf("(");
					String predicateName = premise.substring(0, tillIndex);
					String premisePredicateArguments = premise.substring(tillIndex + 1, premise.length() - 1);
					String arguments[] = premisePredicateArguments.split(",");
					ArrayList<Element> elem_args = new ArrayList<Element>();
					for (String argument : arguments) {
						if(Character.isLowerCase(argument.charAt(0)))
						{
							if (!mapper.containsKey(argument)) {
								mapper.put(argument, "var" + (counter++));
							}
							elem_args.add(new Element((String) mapper.get(argument)));
						}
						else {
							elem_args.add(new Element(argument));
						}
					}
					Predicate predicate = new Predicate(predicateName, elem_args, isNegated);
					sentence.add(predicate);
				}
				facts.add(new Sentence(sentence));
				
				expertSystem.tell(new Sentence(sentence));
				mapper.clear();
			}
			///////////////////////////////////////////////////////////////////////
			br.close();

			//////////////////////////////// ASK ES//////////////////////////////////////
			//System.out.println("**********QUERIES************");
			BufferedWriter writer = new BufferedWriter(new FileWriter(".\\src\\output.txt"));
			for (int i = 0; i < queryCount; i++) {
				
				System.out.println("############NEW QUERY#################");
				writer.write(expertSystem.ask(queries.get(i))+"\n");
			}
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
