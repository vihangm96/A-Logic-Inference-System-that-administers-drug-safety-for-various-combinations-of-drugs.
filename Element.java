
public class Element implements Cloneable {
	public String name;

	public Element(String name) {
		this.name = name;
	}

	public boolean isVar() {
		if (Character.isLowerCase(name.charAt(0))) {
			return true;
		}
		return false;
	}

	public boolean isConst() {
		if (Character.isLowerCase(name.charAt(0))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean equals(Object obj) {
		Element elem2 = (Element) obj;
		if (this.isConst()) {
			if (elem2.isConst()) {
				if (this.name.equals(elem2.name)) {
					return true;
				}
			}
		} else {
			if (elem2.isVar()) {
				return true;
			}
		}
		return false;
	}
	
//	public static boolean isEqual(Element element1,Element element2) {
//		if(element1.isConst()) {
//			if(element2.isConst()) {
//				if(element1.name.equals(element2.name)) {
//					return true;
//				}
//			}
//		}
//		else {
//			if(element2.isVar()) {
//				return true;
//			}
//		}
//		return false;
//	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Element clone = null;
		clone = (Element) super.clone();
		return clone;
	}

}
