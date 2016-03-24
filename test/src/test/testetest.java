package test;

import java.util.Random;

public class testetest
{
	static Double[] b = new Double[10]; 
	
	public static void main(String[] args)
	{
		Random rand = new Random(); 
		for (int i = 0; i < b.length; i++)
		{
			b[i] = (Double) rand.nextDouble();
		}

//		System.out.println(b);
		Double[] a = b.clone();
		
		
		a[0] = 0.; 
		for (int i = 0; i < a.length; i++)
		{
			System.out.print(a[i] + "\t");
		}
		System.out.println();
			
		for (int i = 0; i < b.length; i++)
		{
			System.out.print(b[i] + "\t");
		}
		
	}
	
	
}
