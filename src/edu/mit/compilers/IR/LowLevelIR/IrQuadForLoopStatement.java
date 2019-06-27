package edu.mit.compilers.IR.LowLevelIR;

import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.IR.IrResolveNameToLocationVistor;

public class IrQuadForLoopStatement extends IrQuad{
	
	public IrQuadForLoopStatement(String symbol) {
		this.symbol = symbol;
	}
	
	public boolean isBreak() {
		return symbol.equals("break");
	}
	
	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		//vistor.visit(this);
		if(vistor instanceof IrResolveNameToLocationVistor)
			accept(((IrResolveNameToLocationVistor)vistor));
		
	}
	
	public void accept(IrResolveNameToLocationVistor visitor) {
		//throw new IllegalArgumentException("visiting this");
		visitor.visit(this);
	}
	
}
