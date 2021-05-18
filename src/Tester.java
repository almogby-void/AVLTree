public class Tester {

	public static void main(String[] args) {
		AVLTree t = new AVLTree();
		System.out.println(t.getRoot().getHeight());
		t.insert(5, true);
		System.out.println(t.getRoot().getHeight());
		t.insert(4, true);
		System.out.println(t.getRoot().getHeight());
		t.insert(3, true);
		System.out.println(t.getRoot().getHeight());
		t.insert(2, true);
		System.out.println(t.getRoot().getHeight());
		t.insert(1, true);
		System.out.println(t.getRoot().getHeight());
		t.insert(8, true);
		System.out.println(t.getRoot().getHeight());
		t.insert(7, true);
		System.out.println(t.getRoot().getHeight());
		t.insert(6, true);
		System.out.println(t.getRoot().getHeight());
		t.insert(11, true);
		System.out.println(t.getRoot().getHeight());
		t.insert(12, true);
		System.out.println(t.getRoot().getHeight());
		t.insert(9, true);
		System.out.println(t.getRoot().getHeight());
		t.insert(10, true);
		System.out.println(t.getRoot().getHeight());
	}

}