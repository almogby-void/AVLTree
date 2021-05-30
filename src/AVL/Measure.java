/**
 * 
 */
package AVL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author almog
 *
 */
public class Measure {
	private static Random rand = new Random();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		computeAverageMeasures(10);
		BST bs = new BST();
		Set<Integer> s = randomSet(20);
		for (int i : s)
			bs.insert(i, rand.nextBoolean());
		System.out.println(Arrays.toString(bs.keysToArray()));
		for (int j = 0; j < 10; j++) {
			s = randomSet(20);
			for (int i : s)
				bs.delete(i);
		}
	}
	
	public static void computeAverageMeasures(int n) {
		AVLTree t = new AVLTree();
		double[] r0 = {0, 0}, r1 = {0, 0};
		double[] s0 = {0, 0}, s1 = {0, 0};
		for (int i = 1; i < 6; i++) {			
			for (int j = 0; j < n; j++) {
				Set<Integer> s = randomSet(500 * i);
				for (int k : s)
					t.insert(k, rand.nextBoolean());
				List<Integer> l = new ArrayList<Integer>(s);
				Collections.sort(l);
				r0 = measureXor(t, l, true);
//				System.out.println(Arrays.toString(r0));
				for (int k = 0; k < 2; k++)
					s0[k] += r0[k];
				r1 = measureXor(t, l, false);
//				System.out.println(Arrays.toString(r1));
				for (int k = 0; k < 2; k++)
					s1[k] += r1[k];
			}
			for (int k = 0; k < 2; k++) {
				r0[k] = s0[k] / n;
				r1[k] = s1[k] / n;
			}
			printResults(r0, "PrefixXor", 500 * i);
			printResults(r1, "SuccPrefixXor", 500 * i);
		}
	}
	
	public static Set<Integer> randomSet(int n) {
		Set<Integer> s = new HashSet<Integer>(n); 
		while (s.size() < n)
			s.add(rand.nextInt(n * n));
		return s;
	}
	
	public static double[] measureXor(AVLTree t, List<Integer> l, boolean b) {
		double[] results = new double[2];
		int count = 0;
		long t0, t1, sum = 0;
		for (int k : l) {
			t0 = System.nanoTime();
			if (b)
				t.prefixXor(k);
			else
				t.succPrefixXor(k);
			t1 = System.nanoTime();
			sum += t1 - t0;
			count++;
			if (count == 100)
				results[0] = (double) sum / count;
		}
		results[1] = (double) sum / count;
		return results;
	}
	
	public static void printResults(double[] arr, String s, int n) {
		System.out.println(s + "'s average time for over first 100 keys – " + 
				arr[0] + "ns; and over all " + n + " keys – " + arr[1] + "ns.");
	}
}
