package edu.mit.compilers.IR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.Quad.IrQuad;
import edu.mit.compilers.IR.Quad.IrQuadForAssign;
import edu.mit.compilers.IR.Quad.IrQuadForFuncInvoke;
import edu.mit.compilers.IR.Quad.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.Quad.IrQuadWithLocation;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.UnaryExpression;
import edu.mit.compilers.IR.expr.operand.IrLiteral;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.expr.operand.IrOperand;
import edu.mit.compilers.IR.statement.FuncInvokeStatement;
import edu.mit.compilers.IR.statement.IrAssignment;
import edu.mit.compilers.IR.statement.IrStatement;
import edu.mit.compilers.IR.statement.LoopStatement;
import edu.mit.compilers.IR.statement.Return_Assignment;
import edu.mit.compilers.IR.statement.codeBlock.IfBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrForBlock;
import edu.mit.compilers.IR.statement.codeBlock.IrWhileBlock;
import edu.mit.compilers.SymbolTables.ImportTable;
import edu.mit.compilers.SymbolTables.MethodTable;
import edu.mit.compilers.SymbolTables.VariableTable;
import edu.mit.compilers.trees.EnvStack;
import edu.mit.compilers.trees.SemanticCheckerNode;

public class IrWithTemp implements IrNodeVistor {

	public static final String[] BinaryExprBothSidesAreInt = { "+", "-", "*", "/", "%", ">", "<", ">=", "<=", "!=",
			"==" };
	public static final String[] BinaryExprBothSidesAreBool = { "&&", "||" };
	public int currentSubScript = 0;
	public MethodDecl currentMethod = null;
	public IrBlock currentBlock = null;
	public SemanticCheckerNode semantics;

	public EnvStack envs;
	public String tempName = "$temp";
	
	public ImportTable importIr;
	public IrWithTemp() {
		envs = new EnvStack();
		semantics = new SemanticCheckerNode();
	}

	public static IrProgram newProgram(IrProgram p) {
		IrProgram program = (IrProgram) p.copy();
		IrWithTemp vistor = new IrWithTemp();
		program.accept(vistor);
		return program;
	}

	public IrLocation setNewTempVariable(IrType type, VariableTable v) {
		String id = tempName + currentSubScript;
		currentSubScript++;
		Variable_decl var = new Variable_decl(id, type);
		v.put(var);
		IrLocation loc = new IrLocation(id);
		return loc;
	}

	public IrLocation getVarCorrespondTemp(IrOperand opr, VariableTable v, MethodTable m) {
		IrType type = semantics.getIrOperandType(opr, v, m);
		IrLocation lhsTemp = setNewTempVariable(type, v);
		return lhsTemp;
	}

	public IrLocation getExprCorrespondTemp(IrExpression expr, VariableTable v, MethodTable m) {
		IrType type = semantics.getIrExpressionType(expr, v, m);
		IrLocation temp = setNewTempVariable(type, v);
		return temp;
	}

	public IrAssignment getTempAssignForArr(IrLocation opr, VariableTable v, MethodTable m) {
		IrType type = semantics.getIrOperandType(opr, v, m);
		IrLocation lhsTemp = setNewTempVariable(type, v);
		return new IrAssignment(opr, lhsTemp, "=");
	}

	@Override
	public boolean visit(IrProgram p) {
		// TODO Auto-generated method stub
		importIr = p.getImportTable();
		envs.pushMethods(p.globalMethodTable);
		envs.pushVariables(p.globalVariableTable);
		for (MethodDecl m : p.globalMethodTable) {
			// currentMethod = m;
			m.accept(this);
		}
		envs.popMethod();
		envs.popVariables();
		return false;
	}

	@Override
	public boolean visit(MethodDecl m) {
		// TODO Auto-generated method stub
		currentMethod = m;
		envs.pushVariables(currentMethod.getVariableTable());
		List<IrStatement> statements = currentMethod.statements;
		currentMethod.statements = new ArrayList<>();

		for (IrStatement s : statements) {
			s.accept(this);
		}
		envs.popVariables();
		return false;
	}

	public void HandleNoCompoundSymbolAssign(IrAssignment assign, VariableTable v, MethodTable m) {
		IrLocation lhs = assign.getLhs();
		IrExpression rhs = assign.getRhs();
		if (rhs instanceof IrLiteral) {
			currentMethod.addIrStatement(assign);
			return;
		}
		if (rhs instanceof IrLocation) {
			HandleNoCompoundSymbolAssignRhsIsIrLocation(lhs, (IrLocation) rhs, assign, v, m);
			return;
		}
		if (rhs instanceof UnaryExpression) {
			rhs = setTempForUnaryExpr((UnaryExpression) rhs);
			assign = new IrAssignment(lhs, rhs, "=");
		}

		if (rhs instanceof BinaryExpression) {
			if(lhs.locationIsArray()) {
				handleLocation(lhs, v, m);
				IrLocation lhsTemp = getExprCorrespondTemp(lhs,v, m);
				IrAssignment newAssign = new IrAssignment(lhsTemp, rhs, "=");
				HandleNoCompoundSymbolAssignRhsIsBinaryExpression(newAssign, v, m);
				currentMethod.addIrStatement(new IrAssignment(lhs, lhsTemp, "="));
			}
			else
				HandleNoCompoundSymbolAssignRhsIsBinaryExpression(assign, v, m);
			return;
		}

	}

	public IrExpression setTempForUnaryExpr(UnaryExpression expr) {
		if (expr.getSymbol().equals("-"))
			return new BinaryExpression(new IrLiteral(0), expr.getIrExpression(), "-");
		return null;
	}
	

	private void HandleNoCompoundSymbolAssignRhsIsBinaryExpression(IrAssignment assign, VariableTable v, MethodTable m) {
		IrLocation lhs = assign.getLhs();
		BinaryExpression binary = (BinaryExpression) assign.getRhs();
		IrExpression binaryLhs = binary.getlhs();
		IrExpression binaryRhs = binary.getrhs();
		boolean binaryLhsNotNeedTemp = OperandNotNeedTemp(binaryLhs);
		boolean binaryRhsNotNeedTemp = OperandNotNeedTemp(binaryRhs);
		if(binaryLhsNotNeedTemp && binaryRhsNotNeedTemp) {
			currentMethod.addIrStatement(assign);
			return;
		}
		if(binaryLhs instanceof IrLocation && ((IrLocation) binaryLhs).locationIsArray()) {
			binaryLhs = assignLocationToArray((IrLocation) binaryLhs, v, m);
		}
		if(binaryRhs instanceof IrLocation && ((IrLocation) binaryRhs).locationIsArray())
			binaryRhs = assignLocationToArray((IrLocation) binaryRhs, v, m);
		if(binaryLhs instanceof UnaryExpression) {
			binaryLhs = setTempForUnaryExpr((UnaryExpression) binaryLhs);
		}
		if(binaryRhs instanceof UnaryExpression) {
			binaryRhs = setTempForUnaryExpr((UnaryExpression) binaryRhs);
		}
		
		if(binaryLhs instanceof BinaryExpression) {
			binaryLhs = assignLocationToBinary((BinaryExpression) binaryLhs, v, m);
		}
		
		if(binaryRhs instanceof BinaryExpression) {
			binaryRhs = assignLocationToBinary((BinaryExpression) binaryRhs, v, m);
		}
		BinaryExpression expr = new BinaryExpression(binaryLhs, binaryRhs, binary.getSymbol());
		IrAssignment newAssign = new IrAssignment(lhs, expr, "=");
		currentMethod.addIrStatement(newAssign);
		
	}
	
	private IrLocation assignLocationToBinary(BinaryExpression expr, VariableTable v, MethodTable m) {
		IrLocation temp = getExprCorrespondTemp(expr, v ,m);
		IrAssignment newAssign = new IrAssignment(temp, expr, "=");
		HandleNoCompoundSymbolAssignRhsIsBinaryExpression(newAssign, v, m);
		return temp;
	}
	private IrLocation assignLocationToArray(IrLocation arr, VariableTable v, MethodTable m) {
		if(!arr.locationIsArray())
			throw new IllegalArgumentException("parameter not array");
		handleLocation(arr,v,m);
		IrLocation temp = getExprCorrespondTemp(arr, v, m);
		currentMethod.addIrStatement(new IrAssignment(temp, arr, "="));
		return temp;
	}

	private boolean OperandNotNeedTemp(IrExpression expr) {
		return expr instanceof IrLiteral || (expr instanceof IrLocation && ((IrLocation) expr).locationIsArray());
	}

	private void HandleNoCompoundSymbolAssignRhsIsIrLocation(IrLocation lhs, IrLocation rhs, IrAssignment assign,
			VariableTable v, MethodTable m) {
		handleLocation(lhs,v ,m);
		handleLocation(rhs, v, m);
		if (!((IrLocation) rhs).locationIsArray() || !lhs.locationIsArray()) {
			currentMethod.addIrStatement(assign);
			return;
		} else if (lhs.locationIsArray() && ((IrLocation) rhs).locationIsArray()) {
			IrType leftType = semantics.getIrOperandType(lhs, v, m);
			IrLocation lhsTemp = setNewTempVariable(leftType, v);
			IrAssignment assign1 = new IrAssignment(lhsTemp, rhs, "=");
			IrAssignment assign2 = new IrAssignment(lhs, lhsTemp, "=");
			currentMethod.addIrStatement(assign1);
			currentMethod.addIrStatement(assign2);
		}
	}
	
	private void handleLocation(IrLocation arr, VariableTable v, MethodTable m) {
		if(!arr.locationIsArray()) {
			return;
		} else {
			IrExpression size = arr.getSizeExpr();
			if(size instanceof IrLiteral)       return;
			if(size instanceof IrLocation && !((IrLocation) size).locationIsArray()) {
				return ;
			}
			IrLocation sizeLoc = getExprCorrespondTemp(size, v, m);
			arr.setSizeExpr(sizeLoc);
			IrAssignment assign = new IrAssignment(sizeLoc, size, "=");
			assign.accept(this);
		}
	}

	@Override
	public boolean visit(IrAssignment assign) {
		// TODO Auto-generated method stub
		IrLocation lhsTemp = null;
		IrLocation lhs = assign.getLhs();
		IrExpression rhs = assign.getRhs();
		String symbol = assign.getSymbol();
		if (symbol.equals("=")) {
			HandleNoCompoundSymbolAssign(assign, envs.peekVariables(), envs.peekMethod());
		}

		return false;
	}

	@Override
	public boolean visit(IrBlock block) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(IfBlock ifCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(IrWhileBlock whileBlock) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(IrForBlock forBlock) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(FuncInvokeStatement func) {
		if(importIr.contains(func.getFuncName()))
			func.setPLT(true);
		currentMethod.addIrStatement(func);
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(Return_Assignment r) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(LoopStatement l) {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
		List<StringBuilder> list = new ArrayList<>();
		StringBuilder sb1 = new StringBuilder("old");
		StringBuilder sb2 = new StringBuilder("new");
		list.add(sb1);
		sb1 = sb2;
		for (StringBuilder s : list)
			System.out.println(s.toString());
		System.out.println(sb1.toString());

	}

	@Override
	public boolean visit(IrQuad quad) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(IrQuadForAssign quad) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(IrQuadWithLocation quad) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(IrQuadForFuncInvoke quad) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean visit(IrQuadWithLocForFuncInvoke quad) {
		// TODO Auto-generated method stub
		return false;
	}

}
