package com.xinlan.untils;

public class Randoms
{
	/**
	 * @param min
	 * @param max
	 * @return
	 */
	public static int genInt(int min,int max)
	{
		return (int)(Math.random()*(max-min))+min; 
	}
}//end class
