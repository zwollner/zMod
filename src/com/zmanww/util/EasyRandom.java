/**
 * 
 */
package com.zmanww.util;

import java.util.Random;

/**
 * @author Zeb
 * 
 */
public class EasyRandom extends Random {
	private static final long serialVersionUID = 1L;

	public EasyRandom() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EasyRandom(long arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Passing 20 would return TRUE 20% of the time <br />
	 * Passing 0.5 would return TRUE 0.5% of the time <br />
	 * Etc....
	 * 
	 * @param odds Percent... 10 = 10%
	 * @return True/False
	 */
	public boolean isProbable(double odds) {
		boolean retVal = false;
		/*
		nextInt(int n) 	Returns random int >= 0 and < n.
		nextDouble() 	Returns random double >=0.0 and < 1.0.
		*/
		double rnd = this.nextDouble() + this.nextInt(100);
		if (rnd <= odds){
			retVal = true;
		}
		return retVal;
	}
}
