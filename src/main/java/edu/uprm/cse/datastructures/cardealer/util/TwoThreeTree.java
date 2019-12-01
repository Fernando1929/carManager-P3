package edu.uprm.cse.datastructures.cardealer.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TwoThreeTree<K, V> extends BTree<K, V> {
	
	public TwoThreeTree(Comparator keyComparator) {
		super(keyComparator);
		
	}

	@Override
	public int size() {
		return this.currentSize;
	}

	@Override
	public boolean isEmpty() {
		return this.currentSize == 0;
	}

	@Override
	public V put(K key, V value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V get(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V remove(K key) {
		ArrayList l = new ArrayList<>();
		return null;//this.removeAux(this.root,key,l);
	}

	@Override
	public boolean containsKey(K key) {
		return this.containsKeyAux(this.root,key);
	}

	private boolean containsKeyAux(TreeNode N, K key) {
		if(N == null) {
			return false;
		}
		if(N.center.equals(key)||N.left.equals(key)||N.right.equals(key)) {
			return true;
		}
		if(N.left != null) {
			return this.containsKeyAux(N.left, key);
		}
		if(N.center != null) {
			return this.containsKeyAux(N.center, key);
		}
		if(N.right != null) {
			return this.containsKeyAux(N.right, key);
		}
		return false;
	}

	@Override
	public SortedList getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedList getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	boolean isLeaf(TreeNode treeNode) {
		return  treeNode.right == null && treeNode.center == null  && treeNode.left == null ;
	}

	@Override
	void split(TreeNode treeNode) {
		// TODO Auto-generated method stub
		
	}

}
