import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Predicate implements Cloneable {
	public String name;
	public ArrayList<Element> args;
	public boolean isNegated;

	public Predicate(String name, ArrayList<Element> args) {
		this.args = new ArrayList<Element>(args);
		this.name = name;
		this.isNegated = false;
	}

	public Predicate(String name, ArrayList<Element> args, boolean isNegated) {
		this.args = new ArrayList<Element>(args);
		this.name = name;
		this.isNegated = isNegated;
	}

	@Override
	public String toString() {
		String neg = "";
		if (this.isNegated) {
			neg = "~";
		}
		return neg + this.name + "(" + this.args + ")";
	}

	public static boolean canCancelOut(Predicate testPredicate1, Predicate testPredicate2) {
		if (testPredicate1.name.equals(testPredicate2.name)) {
			if (testPredicate1.isNegated != testPredicate2.isNegated) {
				return true;
			}
		}
		return false;
	}

	public static HashMap<String, String> unifyA(Predicate p1, Predicate p2) {

		HashMap<String, String> subst = new HashMap<String, String>();
		if (!p1.name.equals(p2.name)) {
			return null;
		} else {
			ArrayList<Element> args1 = p1.args;
			ArrayList<Element> args2 = p2.args;
			for (int i = 0; i < p1.args.size(); i++) {
				Element elem1 = args1.get(i);
				Element elem2 = args2.get(i);
				if(elem1.isConst() && elem2.isConst()) {
					if(!(elem1.name.equals(elem2.name))) {
						return null;
					}
				}
				if(elem1.isConst() && elem2.isVar()) {
					if(subst.containsKey(elem2.name)) {
						if(!(elem1.name.equals(subst.get(elem2.name)))) {
							return null;
						}
					}
				}
				if(elem1.isVar() && elem2.isConst()) {
					if(subst.containsKey(elem1.name)) {
						if(!(elem2.name.equals(subst.get(elem1.name)))) {
							return null;
						}
					}else {
						subst.put(elem1.name,elem2.name);
					}					
				}
				if(elem1.isVar() && elem2.isVar()) {
					subst.put(elem1.name,elem2.name);
					/*
					if(subst.containsKey(elem1.name)) {
						if(! (subst.get(elem1.name).equals(elem2.name))) {
							return null;
						}
					}
					else {
						if(!(elem1.name.equals(elem2.name))) {
							subst.put(elem1.name,elem2.name);
						}
					}
					*/
				}
			}
		}

		///////////////////////////////
		/*
		 * boolean allVars = true; for(String val:subst.values()) {
		 * if(Character.isUpperCase(val.charAt(0))) { allVars = false; break; } }
		 * if(allVars) { return null; }
		 */
		////////////////////////////////

		return subst;
	}
	
	public static HashMap<String, String> unify(Predicate p1, Predicate p2) {

		HashMap<String, String> subst = new HashMap<String, String>();
		if (!p1.name.equals(p2.name)) {
			return null;
		} else {
			ArrayList<Element> args1 = p1.args;
			ArrayList<Element> args2 = p2.args;
			for (int i = 0; i < p1.args.size(); i++) {
				Element elem1 = args1.get(i);
				Element elem2 = args2.get(i);
				if (elem1.isConst()) {
					if (elem2.isConst()) {
						if (!elem1.equals(elem2)) {
							return null;
						}
					} else {
						// elem1 is var

						if (elem2.isVar()) {
							if (!(elem2.name.equals(elem1.name))) {
								subst.put(elem2.name, elem1.name);
							}
						} else {
							if (subst.containsKey(elem2.name)) {
								if(!(subst.get(elem2.name).equals(elem1.name))){
									return null;
								}
							} else {
								subst.put(elem2.name, elem1.name);
							}
						}

					}
				} else {
					if (elem2.isConst()) {
						subst.put(elem1.name, elem2.name);
					} else {
						// elem2 is var

						if (elem1.isVar()) {
							if (!(elem1.name.equals(elem2.name))) {
								subst.put(elem1.name, elem2.name);
							}
						} else {
							if (subst.containsKey(elem1.name)) {
								if(!(subst.get(elem1.name).equals(elem2.name))){
									return null;
								}
							} else {
								subst.put(elem1.name, elem2.name);
							}
						}
					}
				}
			}
		}

		///////////////////////////////
		/*
		 * boolean allVars = true; for(String val:subst.values()) {
		 * if(Character.isUpperCase(val.charAt(0))) { allVars = false; break; } }
		 * if(allVars) { return null; }
		 */
		////////////////////////////////

		return subst;
	}

	@Override
	public boolean equals(Object obj) {
		Predicate p2 = (Predicate) obj;

		if (!(p2.name.equals(this.name))) {
			return false;
		}

		if (p2.isNegated == this.isNegated) {
			for (int i = 0; i < this.args.size(); i++) {
				Element arg1 = this.args.get(i);
				Element arg2 = p2.args.get(i);
				if (!arg1.equals(arg2)) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

//	public static boolean isEqual(Predicate predicate1, Predicate predicate2) {
//		if (!(predicate1.name.equals(predicate2.name))) {
//			return false;
//		}
//		if (predicate1.isNegated != predicate2.isNegated) {
//			return false;
//		}
//
//		for (int i = 0; i < predicate1.args.size(); i++) {
//			Element arg1 = predicate1.args.get(i);
//			Element arg2 = predicate2.args.get(i);
//			if (!(Element.isEqual(arg1, arg2))) {
//				return false;
//			}
//		}
//		return true;
//	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Predicate clone = new Predicate(this.name, new ArrayList<Element>(), this.isNegated);
		// clone = (Predicate) super.clone();
		// clone.args.clear();
		Iterator<Element> iterator = this.args.iterator();

		while (iterator.hasNext()) {
			clone.args.add((Element) iterator.next().clone());
		}
		return clone;
	}

}
