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
		AVLTree t = new AVLTree();
		double[] r0 = new double[2], r1 = new double[2];
		for (int i = 1; i < 6; i++) {
			Set<Integer> s = randomSet(5000 * i);
			for (int k : s)
				t.insert(k, rand.nextBoolean());
			List<Integer> l = new ArrayList<Integer>(s);
			Collections.sort(l);
			r0 = measurePref(t, l);
			System.out.println(Arrays.toString(r0));
			r1 = measureSucc(t, l);
			System.out.println(Arrays.toString(r1));
		}
	}
	
	public static Set<Integer> randomSet(int n) {
		Set<Integer> s = new HashSet<Integer>(n); 
		while (s.size() < n)
			s.add(rand.nextInt(n * n));
		return s;
	}
	
	public static double[] measurePref(AVLTree t, List<Integer> l) {
		double[] results = new double[2];
		int count = 0;
		long t0, t1, sum = 0;
		for (int k : l) {
			t0 = System.nanoTime();
			t.prefixXor(k);
			t1 = System.nanoTime();
			sum += t1 - t0;
			count++;
			if (count == 100)
				results[0] = (double) sum / count;
		}
		results[1] = (double) sum / count;
		return results;
	}
	
	public static double[] measureSucc(AVLTree t, List<Integer> l) {
		double[] results = new double[2];
		int count = 0;
		long t0, t1, sum = 0;
		for (int k : l) {
			t0 = System.nanoTime();
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

}
