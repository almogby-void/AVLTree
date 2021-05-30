package AVL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import AVL.AVLTree.AVLNode;

public class Tester {

	public static void main(String[] args) {
		AVLTree t = new AVLTree();
//		int[] l = {5, 4, 3, 2, 1, 8, 7, 6, 11, 12, 9, 10};
//		int[] l = {5, 6, 4, 3, 2, 1};
//		int[] l = {8, 5, 11, 3, 7, 10, 12, 2, 4, 6, 9, 1};
		int[] l = {13, 8, 18, 5, 11, 16, 20, 3, 7, 10, 12,
				15, 17, 19, 2, 4, 6, 9, 14, 1};
		insertHeight(t, l);
		printKeys(t);
		printValues(t);
		int[] l2 = {20, 5, 3, 2, 1, 13, 15, 19, 11};
		deleteHeight(t, l2);
		printKeys(t);
	}
	
	public static void insertHeight(AVLTree t, int[] l) {
		for (int i : l) {
			int cnt = t.insert(i, (i % 2 == 0));
//			System.out.println(t.getRoot().getHeight());
			System.out.println(cnt);
		}
	}
	
	public static void deleteHeight(AVLTree t, int[] l) {
		for (int i : l) {
			int cnt = t.delete(i);
//			System.out.println(t.getRoot().getHeight());
			System.out.println(cnt);
		}
	}

	public static void printKeys(AVLTree t) {
		int[] keys = t.keysToArray();
		System.out.println(Arrays.toString(keys));
	}

	public static void printValues(AVLTree t) {
		boolean[] values = t.infoToArray();
		System.out.println(Arrays.toString(values));
	}
	
	public static void print(AVLNode root) {
		List<List<String>> lines = new ArrayList<List<String>>();
		List<AVLNode> level = new ArrayList<AVLNode>();
		List<AVLNode> next = new ArrayList<AVLNode>();
		level.add(root);
		int nn = 1;
		int widest = 0;
		while (nn != 0) {
			List<String> line = new ArrayList<String>();
			nn = 0;
			for (AVLNode n : level) {
				if (n == null || !n.isRealNode()) {
					line.add(null);
					next.add(null);
					next.add(null);
				} else {
					String aa = n.toString();
					line.add(aa);
					if (aa.length() > widest)
						widest = aa.length();
					next.add(n.getLeft());
					next.add(n.getRight());
					if (n.getLeft().isRealNode())
						nn++;
					if (n.getRight().isRealNode())
						nn++;
				}
			}
			if (widest % 2 == 1)
				widest++;
			lines.add(line);
			List<AVLNode> tmp = level;
			level = next;
			next = tmp;
			next.clear();
		}
		int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
		for (int i = 0; i < lines.size(); i++) {
			List<String> line = lines.get(i);
			int hpw = (int) Math.floor(perpiece / 2f) - 1;
			if (i > 0) {
				for (int j = 0; j < line.size(); j++) {
					// split node
					char c = ' ';
					if (j % 2 == 1) {
						if (line.get(j - 1) != null) {
							c = (line.get(j) != null) ? '┴' : '┘';
						} else {
							if (j < line.size() && line.get(j) != null) c = '└';
						}
					}
					System.out.print(c);
					// lines and spaces
					if (line.get(j) == null) {
						for (int k = 0; k < perpiece - 1; k++) {
							System.out.print(" ");
						}
					} else {
						for (int k = 0; k < hpw; k++) {
							System.out.print(j % 2 == 0 ? " " : "─");
						}
						System.out.print(j % 2 == 0 ? "┌" : "┐");
						for (int k = 0; k < hpw; k++) {
							System.out.print(j % 2 == 0 ? "─" : " ");
						}
					}
				}
				System.out.println();
			}
			// print line of numbers
			for (int j = 0; j < line.size(); j++) {
				String f = line.get(j);
				if (f == null)
					f = "";
				int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
				int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);
				// a number
				for (int k = 0; k < gap1; k++) {
					System.out.print(" ");
				}
				System.out.print(f);
				for (int k = 0; k < gap2; k++) {
					System.out.print(" ");
				}
			}
			System.out.println();
			perpiece /= 2;
		}
	}
}