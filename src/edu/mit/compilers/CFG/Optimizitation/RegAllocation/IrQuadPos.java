package edu.mit.compilers.CFG.Optimizitation.RegAllocation;

import edu.mit.compilers.CFG.CFGNode;

public class IrQuadPos {
	
	public CFGNode node;
	public int cursor;
	
	public IrQuadPos(CFGNode node, int cursor) {
		this.node = node;
		this.cursor = cursor;
	}
	
	public CFGNode getNode() {
		return node;
	}
	
	public int getCursor() {
		return cursor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cursor;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IrQuadPos other = (IrQuadPos) obj;
		if (cursor != other.cursor)
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return node.statements.get(cursor).getName();
	}

}
