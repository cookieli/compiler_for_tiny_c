package edu.mit.compilers.IR.LowLevelIR;

import edu.mit.compilers.CFG.cfgNodeVistor;
import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.utils.OperandForm;

public class ReturnQuadWithLoc extends LowLevelIR{
	
	public OperandForm ret;
	
	public boolean is64bit;
	
	
	public boolean isIs64bit() {
		return is64bit;
	}

	public void setIs64bit(boolean is64bit) {
		this.is64bit = is64bit;
	}

	public ReturnQuadWithLoc(OperandForm quad) {
		setRetLoc(quad);
	}
	
	public void setRetLoc(OperandForm ret) {
		this.ret = ret;
	}
	
	public OperandForm getRetLoc() {
		return ret;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "return " + ret.toString()+"\n";
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		if(vistor instanceof cfgNodeVistor)
			accept((cfgNodeVistor)vistor);
		
	}
	
	public void accept(cfgNodeVistor vistor) {
		vistor.visit(this);
	}

}
