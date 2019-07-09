package edu.mit.compilers.IR.LowLevelIR;

import edu.mit.compilers.IR.IrNode;
import edu.mit.compilers.IR.IrNodeVistor;
import edu.mit.compilers.utils.X86_64Register;
import edu.mit.compilers.utils.X86_64Register.Register;

public class StackZeroIR extends LowLevelIR{
	public String RegZero;
	public String stackZero;
	
	public String colon = ",";
	
	public StackZeroIR() {
		
	}
	
	public StackZeroIR(int size) {
		Register reg = X86_64Register.rax;
		RegZero = "movq " + "$0" + colon + reg.name_64bit;
		int num = size/8;
		int count = size%8;
		StringBuilder sb = new StringBuilder();
		
		for(int i = num; i > 0; i--) {
			sb.append(zeroStack(i, true, reg));
			sb.append("\n");
		}
		if(count != 0) {
			for(int i = size; i > size - count; i--) {
				sb.append(zeroStack(i, false, reg));
				sb.append("\n");
			}
		}
		stackZero = sb.toString();
		
	}
	
	public StackZeroIR(int size, boolean reverse) {
		Register reg = X86_64Register.rax;
		RegZero = "movq " + "$0" + colon + reg.name_64bit;
		int num = size/8;
		int count = size%8;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < num; i++) {
			sb.append(zeroStack(i, true, reg, reverse));
			sb.append("\n");
		}
		if(count != 0) {
			for(int i = size; i > size - count; i--) {
				sb.append(zeroStack(i, false, reg, reverse));
				sb.append("\n");
			}
		}
		stackZero = sb.toString();
	}
	
	
	
	private String zeroStack(int count, boolean is_64bit, Register reg) {
		if(is_64bit)
			return "movq " + reg.name_64bit + colon + count*(-8) + "(%rbp)";
		return "movb " + reg.name_8bit + colon + count*(-1) + "(%rbp)";
	}
	
	private String zeroStack(int count, boolean is_64bit, Register reg, boolean reverse) {
		if(count == 0)
			return "movq " + reg.name_64bit + colon  +"(%rsp)";
		if(is_64bit) {
			return "movq " + reg.name_64bit + colon + count*8 +"(%rsp)";
		}
		else
			return "movb " + reg.name_64bit + colon + count +"(%rsp)";
	}
	
	
	
	public static void main(String[] args) {
		int a = 7;
		StackZeroIR ir = new StackZeroIR(16);
		StackZeroIR ir2 = new StackZeroIR(16, true);
		System.out.println(ir.getName());
		System.out.println(ir2.getName());
	}



	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return RegZero + "\n" + stackZero ;
	}



	@Override
	public IrNode copy() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void accept(IrNodeVistor vistor) {
		// TODO Auto-generated method stub
		
	}
	
}
