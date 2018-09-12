package util;

public class Random {
	
	public static int getInt(int max){
		return (int)getLong(max);
	}
	
	public static int getInt(int min, int max){
		return (int)getLong(min, max);
	}
	
	public static long getLong(long max){
		long n = System.nanoTime();
		long div = n / (max + 1);
		long res = n / div;
		long mul = res * div;
		return n - mul;
	}
	
	public static long getLong(long min, long max){
		return min + getLong(max - min);
	}
	
	public static double getDouble(){
		return getLong(1000000) / 1000000D;
	}
	
	public static double getDouble(long max){
		return getDouble() * max;
	}
	
	public static boolean chance(double chance){
		return canDivine(System.nanoTime(), chance);
	}
	
	public static boolean canDivine(long number, double diviner){
		double dn = number / diviner;
		long ln = (long)(dn);
		return dn == ln;
	}
}
