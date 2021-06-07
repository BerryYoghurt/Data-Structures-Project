package eg.edu.alexu.csd.filestructure.redblacktree;


import java.util.AbstractMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.management.RuntimeErrorException;
public class TreeMap<T extends Comparable<T>,V extends Comparable<V>> implements ITreeMap<T,V>{
	 private IRedBlackTree<T,V> tree;
	
	public TreeMap(IRedBlackTree<T,V> tree) {
		this.tree=tree;
	}
	public TreeMap(){
		this.tree = new RedBlackTree<>();
	}
	/**
	 * Returns a key-value mapping associated with the least key greater than or equal to the given key, or null if there is no such key.
	 * @param key
	 * @return
	 */
	@Override
	public Entry<T, V> ceilingEntry(T key) {
		// TODO Auto-generated method stub
		T key_=ceilingKey(key);
		return key_ == null ? null : new AbstractMap.SimpleEntry<T, V>(key, tree.search(key_) );
	}

	@Override
	public T ceilingKey(T key) {
		// TODO Auto-generated method stub
		/*T target=null;
		INode<T,V> root=tree.getRoot();
		while(!root.isNull() && root.getKey().compareTo(key)>=0) 
			{root=root.getLeftChild();}
		if(root.isNull()) 
			{target=root.getParent().getKey();}*/
		if(key==null)
			throw new RuntimeErrorException(null);
		return ceilHelper(tree.getRoot(),key);
	}
	
	/*private T ceilHelper(INode<T,V> root,T key) {
		if(root.isNull()) 
			{return root.getParent().getKey();}
		else if(root.getKey().compareTo(key)==0)
			{return root.getKey();}
		else if(root.getKey().compareTo(key)>0) 
			{return ceilHelper(root.getLeftChild(),key);}
		else
			{
			if(!root.getRightChild().isNull()) 
			{
				return root.getRightChild().getKey().compareTo(key) < 0 ? 
					   root.getParent().getKey() : ceilHelper(root.getRightChild(),key);

			}
			return (root.getParent()).getKey();
			}
	}*/
	private T ceilHelper(INode<T, V>root,T key) {
		INode<T, V>node=root,ans=null;
		while(!node.isNull()) {
			if(node.getKey().compareTo(key)>=0) {
				ans=node;
				node=node.getLeftChild();
			}
			else
				node=node.getRightChild();
		}
		if(!ans.isNull())
			return ans.getKey();
		else
			return null;
	}
	
	// return !root.getRightChild().isNull() ? 
	// root.getRightChild().getKey().compareTo(key) < 0 ?
	// ceilHelper(root.getParent(),key) : ceilHelper(root.getRightChild(),key) : (root.getParent()).getKey();
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.tree=new RedBlackTree<T,V>();
	}

	@Override
	public boolean containsKey(T key) {
		// TODO Auto-generated method stub
		return tree.contains(key);
	}

	@Override
	public boolean containsValue(V value) {
		// TODO Auto-generated method stub
		if(value==null)
			throw new RuntimeErrorException(null);
		System.out.println(contains(value, this.tree.getRoot()));
		return contains(value, this.tree.getRoot());
	}
	
	private boolean contains(V value,INode<T,V> root) {
		/*Set<Entry<T, V>>set=new LinkedHashSet<>();
		iterate(set,this.tree.getRoot());
		for(Entry<T, V>entry:set) {
			//System.out.println(entry.getValue()+"\t"+value);
			if(entry.getValue().compareTo(value)==0)
				return true;
		}
		return false;*/
		return root.isNull() ? false :
			   root.getValue().compareTo(value)==0 ? true :
					contains(value,root.getLeftChild()) || contains(value,root.getRightChild());
	}

	@Override
	public Set<Entry<T, V>> entrySet() {
		// TODO Auto-generated method stub
		return iterate(new LinkedHashSet<Entry<T,V>>(),this.tree.getRoot());
	}
	
	private Set<Entry<T, V>> iterate(Set<Entry<T, V>> source,INode<T,V> root){
		if(root.isNull()) return source;
		source.addAll(iterate(source,root.getLeftChild()));
		source.add(new AbstractMap.SimpleEntry<T, V>(root.getKey(),root.getValue()));
		return iterate(source,root.getRightChild());
	}

	@Override
	public Entry<T, V> firstEntry() {
		// TODO Auto-generated method stub
		/*T key=least(this.tree.getRoot());
		return key == null ? null : 
			new AbstractMap.SimpleEntry<T, V>(key,this.tree.search(key));*/
		return smallest(this.tree.getRoot());
	}
	
	/*private Entry<T, V> least(INode<T,V> root){
		if(root.isNull()) 
			{return new AbstractMap.SimpleEntry<T, V>(root.getParent().getKey(),root.getParent().getValue());}
		return least(root.getLeftChild());
	}*/

	@Override
	public T firstKey() {
		// TODO Auto-generated method stub
		//return this.tree.getRoot().isNull() ? null : smallest(this.tree.getRoot()) ;
		Entry<T, V>e=smallest(this.tree.getRoot());
		return e==null?null:e.getKey();
	}
	
	/*private T least(INode<T,V> root){
		return root.isNull() ? root.getParent().getKey() : least(root.getLeftChild());
	}*/
	private Entry<T, V> smallest(INode<T,V> root){
		INode<T, V>node=root;
		while(!node.isNull()&&!node.getLeftChild().isNull()) 
			node=node.getLeftChild();
		if(!node.isNull())
			return new AbstractMap.SimpleEntry<T,V>(node.getKey(),node.getValue());
		else
			return null;
	}
	@Override
	public Entry<T, V> floorEntry(T key) {
		T key_=floorKey(key);
		return key_ == null ? null : new AbstractMap.SimpleEntry<T, V>(key, tree.search(key_) );
	}
	@Override
	public T floorKey(T key) {
		if(key==null)
			throw new RuntimeErrorException(null);
		return floorHelper(this.tree.getRoot(), key);
	}
	private T floorHelper(INode<T, V>root,T key) {
		/*if(root.isNull())
			return null;
		else {
			T k=root.getKey();
			if(key.compareTo(root.getKey())<0) {
				k=floorHelper(root.getLeftChild(), key);
			}
			else if(key.compareTo(root.getKey())>0)
				k=floorHelper(root.getRightChild(), key);
			else
				return key;
			return k;
		}*/
		INode<T, V>node=root,ans=null;
		while(!node.isNull()) {
			if(node.getKey().compareTo(key)<=0) {
				ans=node;
				node=node.getRightChild();
			}
			else
				node=node.getLeftChild();
		}
		if(!ans.isNull())
			return ans.getKey();
		else
			return null;
	}
	@Override
	public V get(T key) {
		return this.tree.search(key);
	}

	@Override
	public ArrayList<Entry<T, V>> headMap(T toKey) {
		Set<Entry<T, V>>list=new LinkedHashSet<>();
		ArrayList<Entry<T, V>>new_list=new ArrayList<>();
		iterate(list, this.tree.getRoot());
		for(Entry<T, V>n:list) {
			if(n.getKey().compareTo(toKey)<0)
				new_list.add(n);
			else
				break;
		}
		return new_list;
	}
	@Override
	public ArrayList<Entry<T, V>> headMap(T toKey, boolean inclusive) {
		Set<Entry<T, V>>list=new LinkedHashSet<>();
		ArrayList<Entry<T, V>>new_list=new ArrayList<>();
		if(!inclusive)
			return headMap(toKey);
		iterate(list, this.tree.getRoot());
		for(Entry<T, V>n:list) {
			if(n.getKey().compareTo(toKey)<=0)
				new_list.add(n);
			else
				break;
		}
		return new_list;
	}
	@Override
	public Set<T> keySet() {
		Set<Entry<T, V>> set=iterate(new LinkedHashSet<>(),this.tree.getRoot());
		Set<T>key=new LinkedHashSet<>();
		for(Entry<T, V>entry:set)
			key.add(entry.getKey());
		return key;
	}
	private Entry<T, V> greatest(INode<T,V> root){
		INode<T, V>node=root;
		while(!node.isNull()&&!node.getRightChild().isNull()) 
			node=node.getRightChild();
		if(!node.isNull())
			return new AbstractMap.SimpleEntry<T,V>(node.getKey(),node.getValue());
		else
			return null;
	}
	@Override
	public Entry<T, V> lastEntry() {
		return greatest(this.tree.getRoot());
	}

	@Override
	public T lastKey() {
		Entry<T, V>e=lastEntry();
		return e==null?null:e.getKey();
	}
	@Override
	public Entry<T, V> pollFirstEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entry<T, V> pollLastEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(T key, V value) {
		this.tree.insert(key, value);
		
	}
	@Override
	public void putAll(Map<T, V> map) {
		Iterator<Map.Entry<T, V>>iterator=map.entrySet().iterator();
		while(iterator.hasNext())
			this.tree.insert(iterator.next().getKey(),iterator.next().getValue());
	}
	@Override
	public boolean remove(T key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

}
