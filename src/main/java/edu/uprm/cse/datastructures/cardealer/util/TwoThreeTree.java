package edu.uprm.cse.datastructures.cardealer.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TwoThreeTree<K, V> extends BTree<K, V> {

	private Comparator<V> valueComparator;
	public TwoThreeTree(Comparator<K> keyComparator,Comparator<V> valueComp) {
		super(keyComparator);
		this.valueComparator = valueComp;
	}

	/*Returns the number of elements in the HashTable.
	 */
	@Override
	public int size() {
		return this.currentSize;
	}

	/*Returns the state of the HashTable empty or not.
	 */
	@Override
	public boolean isEmpty() {
		return this.currentSize == 0;
	}
	
	/*Adds a new MapEntry to the node
	 */
	private void addtoNode(TreeNode N, BTree<K, V>.MapEntry newEntry) {
		N.entries.add(newEntry);
		this.currentSize++;
	}
	
	/*Adds the MapEntry with the specified key and values and if it exist one 
	 *with the same key change it's value and return the old value if not 
	 *returns null.
	 */
	@Override
	public V put(K key, V value) {
		if(key == null) {
			throw new IllegalArgumentException("The key of the value cannot be null.");
		}

		MapEntry newEntry = new MapEntry(key,value, this.keyComparator);
		if(this.root == null) {
			this.root = new TreeNode(newEntry,null,this.keyComparator);
			this.currentSize++;
			return null;
		}
		
		MapEntry target = this.getMapEntry(this.root, key);
		if(target != null) {
			V result = target.value;
			target.value = value;
			if(target.deleted == true) {
				target.deleted = false;
				this.currentSize++;
				return null;
			}
			return result;
		}

		this.putAux(this.root, newEntry, key);
		return null;
	}

	/*Auxiliary method for the method put searches the node to add the MapEntry if
	 * if that node has three maps calls split.
	 */
	private void putAux(BTree<K, V>.TreeNode N, BTree<K, V>.MapEntry newEntry, K key) {
		if(N == null) {
			return ;
		}

		if(this.isLeaf(N)) {
			this.addtoNode(N, newEntry);
			if(N.entries.size()==3) {
				this.split(N);
			}
			return;
		}

		if(N.left != null && N.comparator.compare(newEntry.key,N.entries.first().key) < 0) {
			this.putAux(N.left, newEntry, key);
			return;
		}
		if(N.center != null && N.comparator.compare(newEntry.key,N.entries.first().key) > 0 
				&& N.comparator.compare(newEntry.key,N.entries.last().key ) < 0 ||N.right==null){
			this.putAux(N.center, newEntry, key);
			return;
		}
		if(N.right != null && N.comparator.compare(key, N.entries.last().key ) > 0) {
			this.putAux(N.right, newEntry, key);
		}

		return;
	}
	
	/*Verifies that the node is a leaf (right,center and left are null).
	 */
	@Override
	boolean isLeaf(TreeNode treeNode) {
		return  treeNode.right == null && treeNode.center == null && treeNode.left == null ;
	}
	
	/*The split method rearranges the three to keep it balanced and in order.
	 */
	@Override
	void split(TreeNode N) { //split 
		TreeNode newleft = new TreeNode(N.entries.first(),null,this.keyComparator);
		TreeNode newcenter = new TreeNode(N.entries.last(),null,this.keyComparator);
		N.entries.remove(N.entries.last());
		N.entries.remove(N.entries.first());


		if(N == this.root) {//make new root
			TreeNode parent = new TreeNode(N.entries.first(),null,this.keyComparator);
			parent.center = newcenter;
			parent.left = newleft;
			parent.left.parent = parent;
			parent.center.parent = parent;
			if(N.left != null) {
				parent.left.left = N.left;
				parent.left.left.parent = parent.left;
			}if(N.center != null) {
				parent.left.center = N.center;
				parent.left.center.parent = parent.left;
			}if(N.right!= null) {
				parent.center.left = N.right;
				parent.center.left.parent = parent.center;		
			}if(N.temp != null) {
				parent.center.center = N.temp;
				parent.center.center.parent = parent.center;
				N.temp = null;
			}

			this.root = parent;

		}else {//other cases
			N.parent.entries.add(N.entries.get(0));
			N.entries.remove(0);
			if(N.parent.center == N || N.parent.center == null) {//creo que falta poner unas referencias
				N.parent.center = newleft;
				N.parent.center.parent = N.parent;
				N.parent.right = newcenter;
				N.parent.right.parent = N.parent;
				if(N.left != null) {
					N.parent.center.left = N.left;
					N.parent.center.left.parent = N.parent.center;
				}
				if(N.center != null) {
					N.parent.center.center = N.center;
					N.parent.center.center.parent = N.parent.center;
				}
				if(N.right!= null) {
					N.parent.right.left = N.right;
					N.parent.right.left.parent = N.parent.right;		
				}
				if(N.temp != null) {
					N.parent.right.center = N.temp;
					N.parent.right.center.parent = N.parent.right;
				}
			}
			if(N.parent.right == N) {
				N.parent.right = newleft;
				N.parent.right.parent = N.parent;
				N.parent.temp = newcenter;
				N.parent.temp.parent = N.parent;
				if(N.left != null) {
					N.parent.right.left = N.left;
					N.parent.right.left.parent = N.parent.right;
				}
				if(N.center != null) {
					N.parent.right.center = N.center;
					N.parent.right.center.parent = N.parent.right;
				}
				if(N.right!= null) {
					N.parent.temp.left = N.right;
					N.parent.temp.left.parent = N.parent.temp;		
				}
				if(N.temp != null) {
					N.parent.temp.center = N.temp;
					N.parent.temp.center.parent = N.parent.temp;
				}
			}
			if(N.parent.entries.size()==3) {
				this.split(N.parent);
			}
		}
	}
	
	/*Return the value of a MapEntry with the specified key.
	 */
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

	/*Removes MapEntry with the specified key.
	 */
	@Override
	public V remove(K key) {
		if(key == null) {
			throw new IllegalArgumentException("The key of the value cannot be null.");
		}
		MapEntry target = this.getMapEntry(this.root,key);
		if(target != null && target.deleted == false) {
			V result = target.value;
			target.deleted = true;
			this.currentSize--;
			return result;
		}
		return null;
	}
	
	/*Verifies if the TwoThreeTree has a MapEntry with the specified key 
	 */
	@Override
	public boolean contains(K key) {
		MapEntry target = this.getMapEntry(this.root, key);
		return target != null && target.deleted==false  ? true : false;
	}

	/*Returns a list with all the keys in order.
	 */
	@Override
	public List<K> getKeys() {
		SortedList<K> result = new CircularSortedDoublyLinkedList<K>(this.keyComparator);
		this.getKeysAux(this.root,result);
		List<K> result2 = new ArrayList<>();
		for(int i =0;i<result.size();++i) {
			result2.add(result.get(i));
		}

		return result2;
	}

	/*Auxiliary method for the getKeys method searches the tree recursively.
	 */
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

	/*Returns a list with all the values in order.
	 */
	@Override
	public List<V> getValues() {
		SortedList<V> result = new CircularSortedDoublyLinkedList<V>(this.valueComparator);
		this.getValuesAux(this.root,result);
		List<V> result2 = new ArrayList<>();
		for(int i =0;i<result.size();++i) {
			result2.add(result.get(i));
		}

		return result2;
	}
	
	/*Auxiliary method for the getValues method searches the tree recursively.
	 */
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

	/*Returns the MapEntry of the specified key.
	 */
	private MapEntry getMapEntry(TreeNode N, K key) {
		if(N == null) {
			return null;
		}
		for(MapEntry m: N.entries) {
			if(m.key.equals(key)) {
				return m;
			}
		}
		if(N.left != null && N.comparator.compare(key,N.entries.first().key) < 0) {
			return this.getMapEntry(N.left, key);
		}
		if(N.center != null && N.comparator.compare(key,N.entries.first().key) > 0 
				&& N.comparator.compare(key,N.entries.last().key ) < 0 ||N.right==null){
			return this.getMapEntry(N.center, key);
		}
		if(N.right != null && N.comparator.compare(key, N.entries.last().key ) > 0) {
			return this.getMapEntry(N.right, key);
		}
		return null;
	}
}
