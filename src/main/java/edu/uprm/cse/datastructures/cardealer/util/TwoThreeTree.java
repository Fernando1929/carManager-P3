package edu.uprm.cse.datastructures.cardealer.util;

import java.util.Comparator;

public class TwoThreeTree<K, V> extends BTree<K, V> {

	private Comparator<V> valueComparator;
	public TwoThreeTree(Comparator<K> keyComparator,Comparator<V> valueComp) {
		super(keyComparator);
		this.valueComparator = valueComp;
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
		if(key == null) {
			throw new IllegalArgumentException("The key of the value cannot be null.");
		}
		MapEntry target = this.getMapEntry(this.root, key);
		if(target != null) {
			V result = null;
			if(target.deleted == false) {
			  result = target.value;
			}
			target.deleted = false;
			target.value = value;
			this.currentSize++;
			return result;
		}
		target = new MapEntry(key,value,this.keyComparator);
		if(this.root == null) {
			this.root = new TreeNode(target,null,this.keyComparator);
			this.currentSize++;
			return null;
		}
		
		V result = this.putAux(this.root,target,key);
		this.currentSize++;
		
		return result;
	}

	private V putAux(BTree<K, V>.TreeNode N, BTree<K, V>.MapEntry target, K key) {
		if(N == null) {
			return null;
		}
		if(N.left != null && N.comparator.compare(N.entries.get(0).key, key) > 0) {//Needs to verify the key to know if has to go right or left
			return this.putAux(N.left, target, key);
		}
		if(N.center != null && N.comparator.compare(N.entries.get(N.entries.size()-1).key, key) > 0
							&& N.comparator.compare(N.entries.get(0).key, key) < 0) {
			
			return this.putAux(N.center, target, key);
		}
		if(N.right != null && N.comparator.compare(N.entries.get(N.entries.size()-1).key, key) < 0) {
			return this.putAux(N.right, target, key);
		}
		if(N.entries.size() < 2) {
			N.entries.add(target);
			return null;
		}
		N.entries.add(target);
		if(N.parent != null && N.parent.entries.size() < 2) {
			N.parent.entries.add(N.entries.get(1));
			N.entries.remove(1);
			N.center = new TreeNode(N.entries.get(0),null,this.keyComparator);
			N.entries.remove(0);
			N.right = new TreeNode(N.entries.get(0),null,this.keyComparator);
			N.entries.remove(0);
		}
		else {
			MapEntry m = N.entries.get(1);
			TreeNode temp = new TreeNode(m,null,this.keyComparator);
			temp.left = new TreeNode(N.entries.get(N.entries.size()-1),null,this.keyComparator);
			temp.right = new TreeNode(N.entries.get(0),null,this.keyComparator);
			N = temp;
			temp = null;
		}
		//se supone que use split pero estoy confundido
		return null;
	}

	@Override
	public V get(K key) {
		if(key == null) {
			throw new IllegalArgumentException("The key of the value cannot be null.");
		}
		MapEntry target = this.getMapEntry(this.root, key);
		if(target != null && target.deleted == false) {
			return target.value;
		}
		return null;
	}

	@Override
	public V remove(K key) {
		if(key == null) {
			throw new IllegalArgumentException("The key of the value cannot be null.");
		}
		MapEntry target = this.getMapEntry(this.root,key);
		if(target != null) {
			target.deleted = true;
			this.currentSize--;
			return target.value;
		}
		return null;
	}

	@Override
	public boolean containsKey(K key) {
		MapEntry target = this.getMapEntry(this.root, key);
		return target != null &&  target.deleted == false ? true : false;
	}

	@Override
	public SortedList<K> getKeys() {
		SortedList<K> result = new CircularSortedDoublyLinkedList<K>(this.keyComparator);
		this.getKeysAux(this.root,result);

		return result;
	}

	private void getKeysAux(BTree<K, V>.TreeNode N, SortedList<K> result) {
		if(N == null) {
			return;
		}
		for(MapEntry m:N.entries) {
			if(m.deleted == false) {
				result.add(m.key);
			}
		}
		if(N.left != null) {
			this.getKeysAux(N.left, result);
		}
		if(N.center != null) {
			this.getKeysAux(N.center, result);
		}
		if(N.right != null) {
			this.getKeysAux(N.right, result);
		}
		return;
	}

	@Override
	public SortedList<V>getValues() {
		SortedList<V> result = new CircularSortedDoublyLinkedList<V>(this.valueComparator);
		this.getValuesAux(this.root,result);

		return result;
	}

	private void getValuesAux(BTree<K, V>.TreeNode N, SortedList<V> result) {
		if(N == null) {
			return;
		}
		for(MapEntry m:N.entries) {
			if(m.deleted==false) {
				result.add(m.value);
			}
		}
		if(N.left != null) {
			this.getValuesAux(N.left, result);
		}
		if(N.center != null) {
			this.getValuesAux(N.center, result);
		}
		if(N.right != null) {
			this.getValuesAux(N.right, result);
		}
		return;
	}

	@Override
	boolean isLeaf(TreeNode treeNode) {
		return  treeNode.right == null && treeNode.center == null  && treeNode.left == null ;
	}

	@Override
	void split(TreeNode N) {
		//needs more work
		TreeNode Np = N.parent;
		for(MapEntry m:N.entries) {
			TreeNode l = new TreeNode(m,null,this.keyComparator);
			l.parent = Np;
		}
	}

	private MapEntry getMapEntry(TreeNode N, K key) {
		if(N == null) {
			return null;
		}
		for(MapEntry m: N.entries) {
			if(m.key.equals(key)) {
				return m;
			}
		}
		if(N.left != null) {
			return this.getMapEntry(N.left, key);
		}
		if(N.center != null) {
			return this.getMapEntry(N.center, key);
		}
		if(N.right != null) {
			return this.getMapEntry(N.right, key);
		}
		return null;
	}

}
