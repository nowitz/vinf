package cz.zcu.sp;

import java.io.IOException;

/**
 * Main class
 * @author Jan Novák & Marek Šimůnek 
 */
public class App {

	/**
	 * Check parameters
	 * 
	 * @param args - input parameters
	 * @return true/false
	 */
	private static boolean checkParametrs(String[] args) {
		if (args.length == 0) {
			System.out.println("Program musí být spouštěn s parametry!");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Main method
	 * 
	 * @param args input parameters
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String[] args2 = { "tady pak bude string na vstupni soubor" };

		if (checkParametrs(args2)) {
			GraphicComponents.createFullyFrame();
		} else {
			System.out.println("The selected parameters are correct! Try it again!");
		}

	}

}
