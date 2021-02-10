import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class Sentence implements Cloneable{
	// all predicates are ORed together

	public ArrayList<Predicate> predicates;

	public Sentence(ArrayList<Predicate> predicates) {
		this.predicates = new ArrayList<Predicate>(predicates);
	}

	@Override
	public boolean equals(Object obj) {
		Sentence sentence2 = (Sentence) obj;

		if (sentence2.predicates.size() != this.predicates.size()) {
			return false;
		}

		for (Predicate predicate2 : sentence2.predicates) {
			if(!(this.predicates.contains(predicate2))) {
				return false;
			}
		}

		for (Predicate predicate2 : this.predicates) {
			if(!(sentence2.predicates.contains(predicate2))) {
				return false;
			}
		}

		return true;
	}
	
//	public static boolean isEqual(Sentence sentence1,Sentence sentence2) {
//		
//		ArrayList<Predicate> predicates1 = new ArrayList<Predicate>(sentence1.predicates);
//		ArrayList<Predicate> predicates2 = new ArrayList<Predicate>(sentence2.predicates);
//		
//		if(predicates1.size()!=predicates2.size()) {
//			return false;
//		}
//		
//		for(Predicate p1: predicates1) {
//			boolean found = false;
//			for(Predicate p2: predicates2) {
//				if(Predicate.isEqual(p1, p2))
//				{
//					found = true;
//					break;
//				}
//			}
//			if(!found) {
//				return false;
//			}
//		}
//		
//		for(Predicate p2: predicates2) {
//			boolean found = false;
//			for(Predicate p1: predicates1) {
//				if(Predicate.isEqual(p1, p2))
//				{
//					found = true;
//					break;
//				}
//			}
//			if(!found) {
//				return false;
//			}
//		}
//		return true;
//	}

	@Override
	public String toString() {

		String str = "";
		for (Predicate pred : predicates) {
			str += pred.toString() + "|";
		}
		return str.substring(0, str.length() - 1);

	}

	public boolean canBeSimplified() {

		for (Predicate predicate : predicates) {
			for (Element arg : predicate.args) {
				if (arg.isVar()) {
					return true;
				}
			}
		}
		return false;
	}

	public static ArrayList<HashMap<String, String>> unify(Sentence sentence1, Sentence sentence2) {

		ArrayList<HashMap<String, String>> allSubst = new ArrayList<HashMap<String, String>>();

		for (Predicate predicate1 : sentence1.predicates) {
			for (Predicate predicate2 : sentence2.predicates) {
				HashMap<String, String> subst = Predicate.unify(predicate1, predicate2);
				if (subst != null) {
					allSubst.add(subst);
				}
			}
		}
		return allSubst;

	}

	public static Sentence substitute(Sentence s, HashMap<String, String> map) throws CloneNotSupportedException {
		Sentence newSentence = new Sentence(new ArrayList<Predicate>());

		Iterator<Predicate> iterator = s.predicates.iterator();

		while (iterator.hasNext()) {
			newSentence.predicates.add((Predicate) iterator.next().clone());
		}

		for (Predicate predicate : newSentence.predicates) {
			for (Element arg : predicate.args) {

				String resolver = map.get(arg.name);
				if (resolver != null) {
					arg.name = resolver;
				}
			}
		}
		return newSentence;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		Sentence clone = new Sentence(new ArrayList<Predicate>());
		// Sentence clone = (Sentence) super.clone();
		// clone.predicates.clear();

		Iterator<Predicate> iterator = this.predicates.iterator();

		while (iterator.hasNext()) {
			clone.predicates.add((Predicate) iterator.next().clone());
			// System.out.println("pred CL");
		}

		return clone;
	}


	
}
