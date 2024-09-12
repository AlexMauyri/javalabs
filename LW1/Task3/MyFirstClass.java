public class MyFirstClass {
	public static void main(String[] s) {
		MySecondClass o = new MySecondClass(3, 5);
		System.out.println(o.doSomeOperation());
		for (int i = 1; i <= 8; ++i) {
			for (int j = 1; j <= 8; ++j) {
				o.setFirstInteger(i);
				o.setSecondInteger(j);
				System.out.print(o.doSomeOperation());
				System.out.print(" ");
			}
			System.out.println();
		}	
	} 
}




class MySecondClass {
	private int firstInteger;
	private int secondInteger;

	public MySecondClass(int firstInteger, int secondInteger) {
		this.firstInteger = firstInteger;
		this.secondInteger = secondInteger;
	}

	public int doSomeOperation() {
		return firstInteger + secondInteger;
	}

	public int getFirstInteger() {
		return firstInteger;
	}

	public int getSecondInteger() {
		return secondInteger;
	}

	public void setFirstInteger(int firstInteger) {
		this.firstInteger = firstInteger;
	}

	public void setSecondInteger(int secondInteger) {
		this.secondInteger = secondInteger;
	}
}
