package edu.mit.compilers.IR.LowLevelIR;

import java.util.ArrayList;
import java.util.List;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.utils.OperandForm;

public class IrQuadWithLocForFuncInvoke extends LowLevelIR {

	public List<OperandForm> parameters = null;

	public String opr = "call";
	public String funcName;
	
	public boolean hasRetValue;
	public OperandForm retLoc;
	
	
	public boolean is64bit = false;
	
	
	public boolean isIs64bit() {
		return is64bit;
	}

	public void setIs64bit(boolean is64bit) {
		this.is64bit = is64bit;
	}

	public OperandForm getRetLoc() {
		return retLoc;
	}

	public void setRetLoc(OperandForm retLoc) {
		this.retLoc = retLoc;
	}

	public void setHasRetValue(boolean boo) {
		this.hasRetValue = boo;
	}
	
	public boolean getHasRetValue() {
		return hasRetValue;
	}

	public IrQuadWithLocForFuncInvoke(String funcName) {
		this.funcName = funcName;

	}

	public void addParameter(OperandForm para) {
		if (parameters == null)
			parameters = new ArrayList<>();
		parameters.add(para);
	}

	public int getParaNum() {
		if (parameters == null)
			return 0;
		return parameters.size();
	}

	public OperandForm getParameter(int i) {
		return parameters.get(i);
	}

	public List<OperandForm> getParameters() {
		return parameters;
	}

	public void setFuncName(String name) {
		this.funcName = name;
	}

	public String getFuncName() {
		return this.funcName;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(opr + " " + funcName);
		if (parameters != null) {
			sb.append("parameters: ");
			for (OperandForm o : parameters)
				sb.append(o.toString() + " ");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		vistor.visit(this);

	}

}
