import myfirstpackage.*;

public class MyFirstClass {
	public static void main(String[] s) {
		MyFirstPackage o = new MyFirstPackage(3, 5);
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