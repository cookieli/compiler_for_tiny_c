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
	public SymbolTable(K parent) {
		this.parent = parent;
		idList = new ArrayList<>();
		table = new LinkedHashMap<>();
	}
	public K getParent() {return parent;}
	
	public List<V> idList() {return idList;}
	
	public Collection<V> values() {return table.values();}
	
	public boolean hasKey(String key) {
		return idList.contains(key);
	}
	public V get(String key) {
		return table.get(key);
	}
	
	public void put(String key, V v) {
		idList.add(v);
		table.put(key, v);
	}
	
	public abstract void put(V v);
}
