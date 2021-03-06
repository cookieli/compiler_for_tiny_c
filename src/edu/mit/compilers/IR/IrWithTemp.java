package edu.mit.compilers.IR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.mit.compilers.IR.IR_decl_Node.ArrayDecl;
import edu.mit.compilers.IR.IR_decl_Node.MethodDecl;
import edu.mit.compilers.IR.IR_decl_Node.Variable_decl;
import edu.mit.compilers.IR.LowLevelIR.IrIfBlockQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForAssign;
import edu.mit.compilers.IR.LowLevelIR.IrQuadForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocForFuncInvoke;
import edu.mit.compilers.IR.LowLevelIR.IrQuadWithLocation;
import edu.mit.compilers.IR.LowLevelIR.IrWhileBlockQuad;
import edu.mit.compilers.IR.expr.BinaryExpression;
import edu.mit.compilers.IR.expr.IrExpression;
import edu.mit.compilers.IR.expr.MultiStatementExpr;
import edu.mit.compilers.IR.expr.RealUnaryExpression;
import edu.mit.compilers.IR.expr.TernaryExpression;
import edu.mit.compilers.IR.expr.UnaryExpression;
import edu.mit.compilers.IR.expr.operand.IrFuncInvocation;
import edu.mit.compilers.IR.expr.operand.IrLenExpr;
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
	public List<IrStatement> currentList = null;
	public SemanticCheckerNode semantics;

	public boolean retValue64bit = false;

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
		vistor.semantics.importIr = program.importIR;
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
		if (type.equals(IrType.notKnownType))
			type = IrType.IntType;
		IrLocation lhsTemp = setNewTempVariable(type, v);
		return lhsTemp;
	}

	public IrLocation getExprCorrespondTemp(IrExpression expr, VariableTable v, MethodTable m) {
		IrType type = semantics.getIrExpressionType(expr, v, m);
		if (!type.equals(IrType.BoolType) && !type.equals(IrType.IntType)) {
			if (type.equals(IrType.notKnownType)) {
				type = IrType.IntType;
				// throw new IllegalArgumentException("it's type is unknown" + expr.getName());
			}
		}
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
		semantics.importIr = importIr;
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
		
		if (currentMethod.getMethodType().equals(IrType.IntType))
			retValue64bit = true;
		else
			retValue64bit = false;
		List<IrStatement> statements = currentMethod.statements;
		currentMethod.statements = new ArrayList<>();
		for (IrStatement s : statements) {
			s.accept(this);
		}
		envs.popVariables();
		return false;
	}

	public void addIrStatement(IrStatement s) {
		if (currentList != null) {
			currentList.add(s);
			return;
		}
		if (currentBlock == null)
			currentMethod.addIrStatement(s);
		else
			currentBlock.addIrStatement(s);
	}

	public void addIrStatement(IrStatement s, List<IrStatement> lst) {
		lst.add(s);
	}

	public void HandleNoCompoundSymbolAssign(IrAssignment assign, VariableTable v, MethodTable m) {
		IrLocation lhs = assign.getLhs();
		handleLocation(lhs, v, m);
		IrExpression rhs = assign.getRhs();
		if (rhs instanceof IrLiteral) {
			addIrStatement(assign);
			return;
		}

		if (rhs instanceof IrLenExpr) {
			addIrStatement(assign);
		}
		if (rhs instanceof IrLocation) {
			HandleNoCompoundSymbolAssignRhsIsIrLocation(lhs, (IrLocation) rhs, assign, v, m);
			return;
		}
		if (rhs instanceof IrFuncInvocation) {
			HandleNoCompoundSymbolAssignRhsIsFuncInvoke((IrFuncInvocation) rhs, assign, v, m);
			return;
		}

		if (rhs instanceof UnaryExpression) {
			//throw new IllegalArgumentException("assign " + assign.getName());
			UnaryExpression unary = (UnaryExpression) rhs;
			
			//throw new IllegalArgumentException("unary name:" + unary.getName());
			if (unary.isBool()) {
				RealUnaryExpression rUnary = unary.convertToRealUnary();
				//System.out.println(rUnary.getName());
				if(rUnary.getSymbol() == null) {
					new IrAssignment(lhs, rUnary.getIrExpression(), "=").accept(this);
					return;
				}
				
				changeAssignRhsIsBoolToIfBlock(assign, v).accept(this);
				return;
			}
			rhs = setTempForMathUnaryExpr(unary);
			assign = new IrAssignment(lhs, rhs, "=");
		}

		if (rhs instanceof BinaryExpression) {
			if (lhs.locationIsArray()) {
				// handleLocation(lhs, v, m);
				IrLocation lhsTemp = getExprCorrespondTemp(lhs, v, m);
				IrAssignment newAssign = new IrAssignment(lhsTemp, rhs, "=");
				HandleNoCompoundSymbolAssignRhsIsBinaryExpression(newAssign, v, m);
				addIrStatement(new IrAssignment(lhs, lhsTemp, "="));
			} else
				HandleNoCompoundSymbolAssignRhsIsBinaryExpression(assign, v, m);
			return;
		}
		
		if(rhs instanceof TernaryExpression) {
			TernaryExpression ternary = (TernaryExpression) rhs;
			HandleNoCompoundSymbolAssignRhsIsTernaryExpr(ternary, lhs, v, m);
		}

	}
	
	private void HandleNoCompoundSymbolAssignRhsIsTernaryExpr(TernaryExpression ternary, IrLocation lhs, VariableTable v, MethodTable m) {
		IrAssignment trueAssign = new IrAssignment(lhs, ternary.getFirstExpr(), "=");
		IrAssignment falseAssign = new IrAssignment(lhs, ternary.getSecondExpr(), "=");
		IrBlock trueBlock = new IrBlock();
		trueBlock.addIrStatement(trueAssign);
		IrBlock falseBlock = new IrBlock();
		falseBlock.addIrStatement(falseAssign);
		trueBlock.addLocalVarParent(envs.peekVariables());
		falseBlock.addLocalVarParent(envs.peekVariables());
		IfBlock ifCode = new IfBlock(ternary.getCondExpr(), trueBlock, falseBlock);
		ifCode.accept(this);
	}

	private void HandleNoCompoundSymbolAssignRhsIsFuncInvoke(IrFuncInvocation rhs, IrAssignment assign, VariableTable v,
			MethodTable m) {
		resetFuncInvokePara(rhs, v, m);
		rhs.setHasRetValue(true);
		setFuncPLT(rhs);
		addIrStatement(assign);
	}

	private IfBlock changeAssignRhsIsBoolToIfBlock(IrAssignment assign, VariableTable vtb) {
		IfBlock block = new IfBlock(assign.getRhs(), vtb);
		block.addTrueStatement(new IrAssignment(assign.getLhs(), IrLiteral.getTrueLiteral(), "="));
		block.addFalseStatement(new IrAssignment(assign.getLhs(), IrLiteral.getFalseLiteral(), "="));
		return block;
	}

	public IrExpression setTempForMathUnaryExpr(UnaryExpression expr) {
		if (expr.getSymbol().equals("-"))
			return new BinaryExpression(new IrLiteral(0), expr.getIrExpression(), "-");
		return null;
	}
	
	public IrExpression setTempForBoolUnaryExpr(UnaryExpression expr, VariableTable vtb, MethodTable mtb) {
		if(expr.isBool()) {
			IrLocation temp = setNewTempVariable(IrType.BoolType, vtb);
			new IrAssignment(temp, expr, "=").accept(this);
			return temp;
		}
		throw new IllegalArgumentException("not bool: " +expr.getName());
	}

	private void HandleNoCompoundSymbolAssignRhsIsBinaryExpression(IrAssignment assign, VariableTable v,
			MethodTable m) {
		IrLocation lhs = assign.getLhs();
		BinaryExpression binary = (BinaryExpression) assign.getRhs();
		if (binary.isBoolExpr() || binary.isCmpExpr()) {
			changeAssignRhsIsBoolToIfBlock(assign, v).accept(this);
			return;
		}

		IrExpression binaryLhs = binary.getlhs();
		IrExpression binaryRhs = binary.getrhs();
		boolean binaryLhsNotNeedTemp = OperandNotNeedTemp(binaryLhs);
		boolean binaryRhsNotNeedTemp = OperandNotNeedTemp(binaryRhs);
		if (binaryLhsNotNeedTemp && binaryRhsNotNeedTemp) {
			addIrStatement(assign);
			return;
		}

		binaryLhs = assignLocationToIrExpression(binaryLhs, v, m);
		binaryRhs = assignLocationToIrExpression(binaryRhs, v, m);
		BinaryExpression expr = new BinaryExpression(binaryLhs, binaryRhs, binary.getSymbol());
		IrAssignment newAssign = new IrAssignment(lhs, expr, "=");
		addIrStatement(newAssign);

	}

	private IrExpression assignLocationToIrExpression(IrExpression expr, VariableTable vtb, MethodTable mtb) {
		if (expr instanceof IrLocation && ((IrLocation) expr).locationIsArray())
			return assignLocationToArray((IrLocation) expr, vtb, mtb);
		if (expr instanceof IrFuncInvocation) {
			return assignLocationToFunction((IrFuncInvocation) expr, vtb, mtb);
		}
		if (expr instanceof UnaryExpression) {
			UnaryExpression unary = (UnaryExpression) expr;
			if(unary.isBool()) {
				return setTempForBoolUnaryExpr(unary, vtb, mtb);
			}
			expr = setTempForMathUnaryExpr((UnaryExpression) expr);
			
		}
		if (expr instanceof BinaryExpression) {
			expr = assignLocationToBinary((BinaryExpression) expr, vtb, mtb);
			return expr;
		}
		
		if(expr instanceof TernaryExpression) {
			expr = assignLocationToTernary((TernaryExpression) expr, vtb, mtb);
		}
		
		
		return expr;
	}

	private IrLocation assignLocationToFunction(IrFuncInvocation func, VariableTable v, MethodTable m) {
		IrLocation temp = getExprCorrespondTemp(func, v, m);
		IrAssignment newAssign = new IrAssignment(temp, func, "=");
		HandleNoCompoundSymbolAssignRhsIsFuncInvoke(func, newAssign, v, m);
		return temp;
	}
	
	private IrLocation assignLocationToTernary(TernaryExpression ternary, VariableTable v, MethodTable m) {
		IrLocation temp = getExprCorrespondTemp(ternary, v, m);
		//IrAssignment newAssign = new IrAssignment(temp, ternary, "=");
		HandleNoCompoundSymbolAssignRhsIsTernaryExpr(ternary, temp, v, m);
		return temp;
	}

	private IrLocation assignLocationToBinary(BinaryExpression expr, VariableTable v, MethodTable m) {
		IrLocation temp = getExprCorrespondTemp(expr, v, m);
		IrAssignment newAssign = new IrAssignment(temp, expr, "=");
		HandleNoCompoundSymbolAssignRhsIsBinaryExpression(newAssign, v, m);
		return temp;
	}

	private IrLocation assignLocationToArray(IrLocation arr, VariableTable v, MethodTable m) {
		if (!arr.locationIsArray())
			throw new IllegalArgumentException("parameter not array");
		handleLocation(arr, v, m);
		IrLocation temp = getExprCorrespondTemp(arr, v, m);
		addIrStatement(new IrAssignment(temp, arr, "="));
		return temp;
	}

	private boolean OperandNotNeedTemp(IrExpression expr) {
		return expr instanceof IrLiteral || (expr instanceof IrLocation && !((IrLocation) expr).locationIsArray())
				|| expr instanceof IrLenExpr;
	}

	private void HandleNoCompoundSymbolAssignRhsIsIrLocation(IrLocation lhs, IrLocation rhs, IrAssignment assign,
			VariableTable v, MethodTable m) {
		handleLocation(lhs, v, m);
		handleLocation(rhs, v, m);
		if (!((IrLocation) rhs).locationIsArray() || !lhs.locationIsArray()) {
			addIrStatement(assign);
			return;
		} else if (lhs.locationIsArray() && ((IrLocation) rhs).locationIsArray()) {
			IrType leftType = semantics.getIrOperandType(lhs, v, m);
			IrLocation lhsTemp = setNewTempVariable(leftType, v);
			IrAssignment assign1 = new IrAssignment(lhsTemp, rhs, "=");
			IrAssignment assign2 = new IrAssignment(lhs, lhsTemp, "=");
			addIrStatement(assign1);
			addIrStatement(assign2);
		}
	}

	private void handleLocation(IrLocation arr, VariableTable v, MethodTable m) {
		if (!arr.locationIsArray()) {
			return;
		} else {
			IrExpression size = arr.getSizeExpr();
			if (size instanceof IrLiteral)
				return;
			if (size instanceof IrLocation && !((IrLocation) size).locationIsArray()) {
				return;
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
		VariableTable vtb = envs.peekVariables();
		MethodTable mtb = envs.peekMethod();
		if (symbol.equals("=")) {
			//throw new IllegalArgumentException("name: " + assign.getName());
		HandleNoCompoundSymbolAssign(assign, vtb, mtb);
		} else if (symbol.equals("++") || symbol.equals("--")) {
			handleIncOrDec(assign, vtb, mtb);
		} else {
			handleComboundOp(assign, vtb, mtb);
		}

		return false;
	}

	public void handleComboundOp(IrAssignment assign, VariableTable vtb, MethodTable mtb) {
		Pattern comboundOp = Pattern.compile("(\\+|\\-|\\*|\\/)(=)");
		Matcher m = comboundOp.matcher(assign.getSymbol());
		String symbol;
		if (m.matches())
			symbol = m.group(1);
		else
			throw new IllegalArgumentException(assign.getSymbol() + "not right");
		expandComboundAssign(assign.getLhs(), assign.getRhs(), symbol, vtb, mtb);

	}

	private void expandComboundAssign(IrLocation lhs, IrExpression rhs, String symbol, VariableTable vtb,
			MethodTable mtb) {
		handleLocation(lhs, vtb, mtb);
		IrAssignment newAssign;
		if (lhs.locationIsArray()) {
			IrLocation lhsTemp = getExprCorrespondTemp(lhs, vtb, mtb);
			addIrStatement(new IrAssignment(lhsTemp, lhs, "="));
			BinaryExpression binary = new BinaryExpression(lhsTemp, rhs, symbol);
			newAssign = new IrAssignment(lhsTemp, binary, "=");
			newAssign.accept(this);
			addIrStatement(new IrAssignment(lhs, lhsTemp, "="));
		} else {
			BinaryExpression binary = new BinaryExpression(lhs, rhs, symbol);
			newAssign = new IrAssignment(lhs, binary, "=");
			newAssign.accept(this);
		}
	}

	public void handleIncOrDec(IrAssignment assign, VariableTable v, MethodTable m) {
		IrLocation lhs = assign.getLhs();
		String symbol = "-";
		if (assign.getSymbol().equals("++"))
			symbol = "+";
		expandComboundAssign(assign.getLhs(), IrLiteral.getLiteral(1), symbol, v, m);
	}

	@Override
	public boolean visit(IrBlock block) {
		// TODO Auto-generated method stub
		List<IrStatement> tempList = currentList;
		currentList = null;
		IrBlock tempBlock = currentBlock;
		currentBlock = block;
		// currentBlock.setLocalVarTableParent(envs.peekVariables());
		envs.pushVariables(block.localVars);
		List<IrStatement> statements = block.statements;
		block.statements = new ArrayList<>();
		for (IrStatement s : statements) {
			s.accept(this);
		}
		envs.popVariables();
		currentBlock = tempBlock;
		currentList = tempList;
		return false;
	}

	@Override
	public boolean visit(IfBlock ifCode) {
		// TODO Auto-generated method stub

		ifCode.setBoolExpr(handleBoolExpr(ifCode.getBoolExpr(), envs.peekVariables(), envs.peekMethod()));
		ifCode.getTrueBlock().accept(this);
		if (ifCode.getFalseBlock() != null) {
			ifCode.getFalseBlock().accept(this);
		}

		addIrStatement(ifCode);
		return false;
	}

	private IrExpression handleBoolExpr(IrExpression expr, VariableTable vtb, MethodTable mtb) {
		if (expr instanceof IrLiteral || expr instanceof IrLocation || expr instanceof IrFuncInvocation || expr instanceof TernaryExpression) {
			List<IrStatement> tempLst = currentList;
			currentList = new ArrayList<>();
			IrExpression ret = assignLocationToIrExpression(expr, vtb, mtb);
			MultiStatementExpr mul = new MultiStatementExpr(ret, currentList);
			currentList = tempLst;
			return mul;
		}
		if (expr instanceof BinaryExpression) {
			BinaryExpression binary = (BinaryExpression) expr;
			if (binary.isCmpExpr())
				return handleCmpExpr(binary, vtb, mtb);
			if (binary.isBoolExpr()) {
				binary.setLhs(handleBoolExpr(binary.getlhs(), vtb, mtb));
				binary.setRhs(handleBoolExpr(binary.getrhs(), vtb, mtb));
				return binary;
			}
		}
		if (expr instanceof UnaryExpression) {
			return setUnaryExpr((UnaryExpression) expr, vtb, mtb);
		}
		//return null;
		throw new IllegalArgumentException(expr.getName());
	}
	
	public RealUnaryExpression setUnaryExpr(UnaryExpression expr, VariableTable vtb, MethodTable mtb) {
		RealUnaryExpression unary;
		if(!(expr instanceof RealUnaryExpression))
			unary = ((UnaryExpression) expr).convertToRealUnary();
		else
			unary  = (RealUnaryExpression) expr;
		
		unary.setExpr(handleBoolExpr(unary.getIrExpression(), vtb, mtb));
		//System.out.println(unary.getName());
		return unary;
	}

	private IrExpression handleCmpExpr(BinaryExpression expr, VariableTable vtb, MethodTable mtb) {
		IrExpression binaryLhs;
		IrExpression binaryRhs;
		List<IrStatement> tempList = currentList;
		currentList = new ArrayList<>();
		binaryLhs = assignLocationToIrExpression(expr.getlhs(), vtb, mtb);
		binaryRhs = assignLocationToIrExpression(expr.getrhs(), vtb, mtb);
		expr.setLhs(binaryLhs);
		expr.setRhs(binaryRhs);
		MultiStatementExpr ret = new MultiStatementExpr(expr, currentList);
		currentList = tempList;
		return ret;
	}

	@Override
	public boolean visit(IrWhileBlock whileBlock) {
		// TODO Auto-generated method stub
		whileBlock.getCodeBlock().accept(this);
		//currentList = whileBlock.getPreTempStat();
		whileBlock.setBoolExpr(handleBoolExpr(whileBlock.getBoolExpr(), envs.peekVariables(), envs.peekMethod()));
		//currentList = null;
		addIrStatement(whileBlock);
		return false;
	}

	@Override
	public boolean visit(IrForBlock forBlock) {
		// TODO Auto-generated method stub
		forBlock.getInitialAssign().accept(this);
		forBlock.getBlock().accept(this);
		currentList = forBlock.getPreTempStat();
		forBlock.setBoolExpression(handleBoolExpr(forBlock.getBoolExpr(), envs.peekVariables(), envs.peekMethod()));
		currentList = null;
		currentList = forBlock.getAfterBlockStat();
		forBlock.getStepFunction().accept(this);
		currentList = null;
		addIrStatement(forBlock);
		return false;
	}

	@Override
	public boolean visit(FuncInvokeStatement func) {
		if (importIr.contains(func.getFuncName()))
			func.setPLT(true);
		resetFuncInvokePara(func.getFunc(), envs.peekVariables(), envs.peekMethod());
		addIrStatement(func);
		// TODO Auto-generated method stub
		return false;
	}

	public void setFuncPLT(IrFuncInvocation func) {
		if (importIr.contains(func.getId()))
			func.setPLT(true);
	}

	private void resetFuncInvokePara(IrFuncInvocation func, VariableTable v, MethodTable m) {
		if (func.getFuncArgs() != null) {
			List<IrExpression> lst = new ArrayList<>();
			for (IrExpression e : func.getFuncArgs()) {
				lst.add(assignLocationToIrExpression(e, v, m));
			}
			func.setFuncArgs(lst);
		}
	}

	@Override
	public boolean visit(Return_Assignment r) {
		// TODO Auto-generated method stub
		IrExpression expr = r.getExpr();
		r.setIs64bit(retValue64bit);
		r.setReturnExpr(assignLocationToIrExpression(expr, envs.peekVariables(), envs.peekMethod()));
		addIrStatement(r);
		return false;
	}

	@Override
	public boolean visit(LoopStatement l) {
		// TODO Auto-generated method stub
		addIrStatement(l);
		return false;
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

	public static void main(String[] args) {
		Pattern comboundOp = Pattern.compile("(\\+|\\-|\\*|\\/)(=)");
		String s = "/=";
		Matcher m = comboundOp.matcher(s);
		if (m.matches()) {
			System.out.println(m.group(1) + " " + m.group(2));
		}

	}

	@Override
	public void visit(IrIfBlockQuad irIfBlockQuad) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IrWhileBlockQuad whileQuad) {
		// TODO Auto-generated method stub

	}

}
