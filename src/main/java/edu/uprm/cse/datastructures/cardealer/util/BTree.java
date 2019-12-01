package edu.uprm.cse.datastructures.cardealer.util;

import java.util.Comparator;


public abstract class BTree<K,V> implements Map<K,V>{

    int currentSize;
    int deleted;
    TreeNode root;
    Comparator<K> keyComparator;

    class MapEntry implements Comparable<MapEntry>{
        K key;
        V value;
        boolean deleted;
        Comparator<K> keyComparator;
        MapEntry(K key, V value, Comparator<K> keyComparator){
            this.keyComparator = keyComparator;
            this.key = key;
            this.value = value;
            deleted = false;
        }
        @Override
        public int compareTo(MapEntry o) {
            return keyComparator.compare(this.key, o.key);
        }
    }

    class MapEntryComparator implements Comparator<MapEntry>{

        @Override
        public int compare(MapEntry o1, MapEntry o2) {
            return o1.compareTo(o2);
        }
    }

    class TreeNode{
        SortedList<MapEntry> entries;
        TwoThreeTree.TreeNode left, right, center, parent, temp;
        Comparator<K> comparator;
        TreeNode(K keyA, K keyB, V a, V b, Comparator<K> comparator){
            this.comparator = comparator;
            this.entries = new CircularSortedDoublyLinkedList<>(new MapEntryComparator());
            this.entries.add(new MapEntry(keyA, a, comparator));
            if(keyB != null) {
                this.entries.add(new MapEntry(keyB, b, comparator));
            }
        }
        TreeNode(MapEntry entryA, MapEntry entryB, Comparator<K> comparator){
            this.comparator = comparator;
            this.entries = new CircularSortedDoublyLinkedList<>(new MapEntryComparator());
            this.entries.add(entryA);
            if(entryB != null && !entryB.deleted){
                this.entries.add(entryB);
            }
        }
    }

    public BTree(Comparator<K> keyComparator){
        this.currentSize = 0;
        this.deleted = 0;
        this.keyComparator = keyComparator;
    }

    abstract boolean isLeaf(TreeNode treeNode);

    /*
        splits a node with 3 keys
    */
    abstract void split(TreeNode treeNode);





}
