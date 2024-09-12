package myfirstpackage;

public class MyFirstPackage {
	private int firstInteger;
	private int secondInteger;

	public MyFirstPackage(int firstInteger, int secondInteger) {
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
