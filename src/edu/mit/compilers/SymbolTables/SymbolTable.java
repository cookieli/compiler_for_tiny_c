package edu.mit.compilers.SymbolTables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import edu.mit.compilers.IR.Named;

public abstract class SymbolTable<K extends SymbolTable<K, V>, V extends Named> implements Iterable<V>{
	public K parent;
	
	public List<V> idList;
	public LinkedHashMap<String, V> table;
	
	public SymbolTable() {
		this(null);
		
	}
	public int size() {
		return idList.size();
	}
	public SymbolTable(K parent) {
		this.parent = parent;
		idList = new ArrayList<>();
		table = new LinkedHashMap<>();
	}
	public void addParent(K parent) {
		this.parent = parent;
		
	}
	public K getParent() {return parent;}
	
	public List<V> idList() {return idList;}
	
	public Collection<V> values() {return table.values();}
	
	public V get(String key) {
		if(table.get(key) != null)
			return table.get(key);
		else if(parent != null)
			return parent.get(key);
		else
			return null;
	}
	
	
	
	public void put(String key, V v) {
		idList.add(v);
		table.put(key, v);
	}
	
	public abstract void put(V v);
}
