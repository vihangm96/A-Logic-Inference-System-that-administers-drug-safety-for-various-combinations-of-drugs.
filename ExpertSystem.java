import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class ExpertSystem {
	
	class SortByLength implements Comparator<Sentence>{

		@Override
		public int compare(Sentence o1, Sentence o2) {
			return o1.predicates.size()-o2.predicates.size();
		}
		
	}

	/*
	 * class KB{ ArrayList<Sentence> facts;
	 * 
	 * public KB(ArrayList<Sentence> allFacts) { facts = new
	 * ArrayList<Sentence>(allFacts); }
	 * 
	 * public KB(KB allFacts) { this.facts = new
	 * ArrayList<Sentence>(allFacts.facts); }
	 * 
	 * public KB() { facts = new ArrayList<Sentence>(); }
	 * 
	 * public KB deepcopy() { KB copy = new KB(); for (Sentence sentence : facts) {
	 * ArrayList<Predicate> p = new ArrayList<Predicate>(); for (Predicate predicate
	 * : sentence.predicates) { ArrayList<Element> elist = new ArrayList<Element>();
	 * for(Element e : predicate.args) { elist.add(new Element(e.name)); } p.add(new
	 * Predicate(predicate.name, elist)); } copy.facts.add(new Sentence(p)); }
	 * return copy; } }
	 */

	public ArrayList<Sentence> kb;

	// public KB kb;
	public ExpertSystem() {
		kb = new ArrayList<Sentence>();
		// kb = new KB();
	}

	public void tell(Sentence sentence) {
		// System.out.println(sentence);
		kb.add(sentence);
		// kb.facts.add(sentence);
	}

	public String ask(Sentence query) throws CloneNotSupportedException {
		// System.out.println(query);
		ArrayList<Sentence> testKB = new ArrayList<Sentence>();

		// KB testKB = this.kb.deepcopy();

		System.out.println(testKB.hashCode());
		System.out.println(kb.hashCode());

		query.predicates.get(0).isNegated = !(query.predicates.get(0).isNegated);
		testKB.add(query);
		
		Iterator<Sentence> iterator = kb.iterator();

		while (iterator.hasNext()) {
			testKB.add((Sentence) iterator.next().clone());
		}
		
		boolean result = resolveAll(testKB);
		// boolean result = resolve(query, testKB);
		System.out.println(result);
		if (result) {
			return "TRUE";
		}
		return "FALSE";
	}

	private boolean resolveAll(ArrayList<Sentence> testKB) throws CloneNotSupportedException {

		ArrayList<Sentence> newTestKB = new ArrayList<Sentence>();
		
//		try {
			while (true) {
				
				newTestKB.clear();
				
				System.out.println("================KB=================");
				for (Sentence s : testKB) {
					System.out.println(s);
				}
				System.out.println("===================================");

				Iterator<Sentence> iterator = testKB.iterator();

				while (iterator.hasNext()) {
					Sentence toUnify = iterator.next();
					for (Sentence sentence : testKB) {
						if (!sentence.equals(toUnify)) {

							ArrayList<Predicate> toUnifyPredicates = toUnify.predicates;
							ArrayList<Predicate> sentencePredicates = sentence.predicates;

							for (Predicate p1 : toUnifyPredicates) {
								for (Predicate p2 : sentencePredicates) {
									if (Predicate.canCancelOut(p1, p2)) {
										HashMap<String, String> subst = Predicate.unify(p1, p2);
										if (subst != null) {
											
											
											boolean allVars = true;
											if(subst.isEmpty()) {
												allVars = false;
											}
											else {
												for(String val: subst.values()) {
													if(Character.isUpperCase(val.charAt(0))) {
														allVars = false;
														break;
													}
												}
											}

											if(allVars) {
												continue;
											}
											
											
											Sentence unifiedSentence = new Sentence(new ArrayList<Predicate>());

											Iterator<Predicate> iterator1 = sentence.predicates.iterator();
											while (iterator1.hasNext()) {
												unifiedSentence.predicates.add((Predicate) iterator1.next().clone());
											}

											Iterator<Predicate> iterator2 = toUnify.predicates.iterator();
											while (iterator2.hasNext()) {
												unifiedSentence.predicates.add((Predicate) iterator2.next().clone());
											}

											unifiedSentence.predicates.remove(p1);
											unifiedSentence.predicates.remove(p2);

											unifiedSentence = Sentence.substitute(unifiedSentence, subst);

											if (unifiedSentence.predicates.size() == 0) {
												
												System.out.println("*****************************");
												System.out.println(p1);
												System.out.println(p2);
												System.out.println("*****************************");
												
												return true;
											} else {

												// new_testKB.remove(testSentence);

												if (!testKB.contains(unifiedSentence) && !(newTestKB.contains(unifiedSentence))) {
													System.out.println("+++++++++++SUBST++++++++++++");
													System.out.println(subst);
													System.out.println("++++++++++++++++++++++++++++");
													
													System.out.println("******");
													System.out.println(sentence);
													System.out.println("COMBINED WITH");
													System.out.println(toUnify);
													System.out.println("GIVES");
													System.out.println(unifiedSentence);
													System.out.println("******");
													
													newTestKB.add((Sentence) unifiedSentence.clone());
													// testKB.facts.add(unifiedSentence);
//											boolean result = resolveAll(testKB);
//											if (result) {
//												return result;
//											}
												}

											}

										}
									}
								}
							}

						}
					}

				}
				if(newTestKB.isEmpty()) {
					return false;
				}
				else {
					testKB.addAll(0,newTestKB);
					testKB.sort(new SortByLength());
				}
			}
//		} catch (Exception e) {
//			System.out.println(e);
//			return false;
//		}
		//	return false;
	}

	/*
	 * private boolean resolve(Sentence query, ArrayList<Sentence> testKB) { //
	 * private boolean resolve(Sentence query, KB testKB) {
	 * System.out.println("============== TEST KB ================"); for (Sentence
	 * s : testKB) { System.out.println(s); }
	 * System.out.println("=======================================");
	 * 
	 * try {
	 * 
	 * Sentence new_query = (Sentence) query.clone(); ArrayList<Sentence> new_testKB
	 * = new ArrayList<Sentence>(); Iterator<Sentence> iterator = testKB.iterator();
	 * while(iterator.hasNext()) { new_testKB.add((Sentence)
	 * iterator.next().clone()); }
	 * 
	 * for (int sent_idx = 0; sent_idx < new_testKB.size(); sent_idx++) { ////
	 * Sentence testSentence = new_testKB.get(sent_idx); Sentence new_testSentence =
	 * new Sentence(testSentence.predicates); for (int p_idx = 0; p_idx <
	 * testSentence.predicates.size(); p_idx++) { Predicate sentencePredicate =
	 * testSentence.predicates.get(p_idx); for (int q_idx = 0; q_idx <
	 * query.predicates.size(); q_idx++) { Predicate queryPredicate =
	 * query.predicates.get(q_idx);
	 * 
	 * if (Predicate.canCancelOut(sentencePredicate, queryPredicate)) {
	 * 
	 * HashMap<String, String> subst = Predicate.unify(sentencePredicate,
	 * queryPredicate);
	 * 
	 * if (subst != null) { System.out.println("++++++++++++++");
	 * System.out.println(subst); System.out.println("++++++++++++++"); Sentence
	 * unifiedSentence = new Sentence(new ArrayList<Predicate>());
	 * unifiedSentence.predicates.addAll(new_testSentence.predicates);
	 * unifiedSentence.predicates.addAll(new_query.predicates);
	 * unifiedSentence.predicates.remove(sentencePredicate);
	 * unifiedSentence.predicates.remove(queryPredicate); unifiedSentence =
	 * Sentence.substitute(unifiedSentence, subst);
	 * 
	 * if (unifiedSentence.predicates.size() == 0) { return true; } else {
	 * System.out.println("******"); System.out.println(testSentence);
	 * System.out.println("COMBINED WITH"); System.out.println(query);
	 * System.out.println("GIVES"); System.out.println(unifiedSentence);
	 * System.out.println("******"); // new_testKB.remove(testSentence);
	 * 
	 * if (!new_testKB.contains(unifiedSentence)) { new_testKB.add((Sentence)
	 * unifiedSentence.clone()); // testKB.facts.add(unifiedSentence); boolean
	 * result = resolve(unifiedSentence, new_testKB); if (result) { return result; }
	 * }
	 * 
	 * } } } } } } /* Sentence new_query = new Sentence(query.predicates);
	 * ArrayList<Sentence> new_testKB = new ArrayList<Sentence>(testKB); //KB
	 * new_testKB = new KB(testKB);//.deepcopy(); ////
	 * //System.out.println(new_testKB.hashCode());
	 * 
	 * for (Sentence testSentence : new_testKB) { //// Sentence new_testSentence =
	 * new Sentence(testSentence.predicates); for (Predicate sentencePredicate :
	 * testSentence.predicates) { for (Predicate queryPredicate : query.predicates)
	 * { if (Predicate.canCancelOut(sentencePredicate, queryPredicate)) {
	 * 
	 * HashMap<String, String> subst = Predicate.unify(sentencePredicate,
	 * queryPredicate);
	 * 
	 * if (subst != null) { System.out.println("++++++++++++++");
	 * System.out.println(subst); System.out.println("++++++++++++++"); Sentence
	 * unifiedSentence = new Sentence(new ArrayList<Predicate>());
	 * unifiedSentence.predicates.addAll(new_testSentence.predicates);
	 * unifiedSentence.predicates.addAll(new_query.predicates);
	 * unifiedSentence.predicates.remove(sentencePredicate);
	 * unifiedSentence.predicates.remove(queryPredicate); unifiedSentence =
	 * Sentence.substitute(unifiedSentence, subst);
	 * 
	 * if (unifiedSentence.predicates.size() == 0) { return true; } else {
	 * System.out.println("******"); System.out.println(testSentence);
	 * System.out.println("COMBINED WITH"); System.out.println(query);
	 * System.out.println("GIVES"); System.out.println(unifiedSentence);
	 * System.out.println("******"); //new_testKB.remove(testSentence);
	 * new_testKB.add(unifiedSentence); //testKB.facts.add(unifiedSentence); boolean
	 * result = resolve(unifiedSentence, new_testKB); if(result) { return result; }
	 * } } } } }
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * catch (Exception e) { System.out.println("E"); return false; } return false;
	 * }
	 */

}


