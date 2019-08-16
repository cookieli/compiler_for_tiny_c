package edu.mit.compilers.CFG.Optimizitation.RegAllocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.IntStream;

import edu.mit.compilers.CFG.CFG;
import edu.mit.compilers.CFG.CFGNode;
import edu.mit.compilers.IR.LowLevelIR.IrQuad;
import edu.mit.compilers.IR.expr.operand.IrLocation;
import edu.mit.compilers.IR.statement.IrStatement;

public class RegisterAllocation {
	public HashSet<IrLocation> useVars;
	public CFG cfg;

	public Map<IrLocation, DefUseUnion> unions;

	public List<SingleValueWeb> webs;

	public Map<Integer, List<Integer>> inferenceVectors;

	public int[][] inferenceMatrix;

	public RegisterAllocation(CFG cfg) {
		this.cfg = cfg;
		useVars = new HashSet<>();
		unions = new HashMap<>();
		addVar();
		setUnion();
	}

	public void printInfVecs() {
		for (Integer i : inferenceVectors.keySet()) {
			System.out.println("key: ");
			System.out.println(webs.get(i).toString());
			System.out.println("values: ");
			for (Integer e : inferenceVectors.get(i)) {
				System.out.println(webs.get(e).toString());
			}
		}
	}

	public void addVar() {
		for (CFGNode n : cfg.nodes) {
			addVar(n);
		}
	}

	public void setUnion() {
		for (IrLocation loc : useVars) {
			unions.put(loc, new DefUseUnion(loc));
		}
	}

	public void union() {
		for (CFGNode n : cfg.nodes) {
			unionNode(n);
		}

		// HashMap<IrLocation, List<SingleValueWeb>> webs = new HashMap<>();
		webs = new ArrayList<>();
		for (IrLocation loc : unions.keySet()) {
			if(!loc.locationIsArray())
				webs.addAll(unions.get(loc).getWeb());
		}
		setInferenceMatrix();
		setInferenceVector();
	}

	public void coloring(int size) {
		Stack<Integer> stack = new Stack<>();
		Map<Integer, Integer> infSizes = new HashMap<>();
		int[][] matrix = initializeMatrix();
		setWebInfs(matrix, infSizes);
		while (stack.size() < webs.size()) {
			int cnt = 0;
			int push = -1;
			for (Integer e : infSizes.keySet()) {
				if (infSizes.get(e) < size && infSizes.get(e) >= 0) {
					stack.push(e);
					resetInference(e, matrix, infSizes);
					break;
				} else if (infSizes.get(e) >= size) {
					cnt++;
					push = e;
				}
			}

			if (cnt != 0 && cnt == webs.size() - stack.size()) {
//				System.out.println(cnt);
//				System.out.println(infSizes.size());
//				throw new IllegalArgumentException("error");
				stack.push(push);
				webs.get(push).setUseMem(true);
				resetInference(push, matrix, infSizes);

			}
		}

		while (!stack.isEmpty()) {
			setColor(stack.pop(), size);
		}
		setReg();
	}

	public void setReg() {
		for (SingleValueWeb web : webs) {
			web.setStatReg();
		}
	}

	public void setColor(int i, int size) {
		boolean arr[] = new boolean[size];
		int reg = 0;
		if (webs.get(i).isUseMem())
			return;
		if (inferenceVectors.containsKey(i)) {
			System.out.println("inference " + webs.get(i).loc.getName());
			for (int e : inferenceVectors.get(i)) {
				if (webs.get(e).getRegNumbering() != -1) {
					arr[webs.get(e).getRegNumbering()] = true;
				}
			}
			for (int j = 0; j < arr.length; j++) {
				if (arr[j] == false) {
					reg = j;
					break;
				}
			}
		}

		webs.get(i).setRegNumbering(reg);
	}

	public void setWebInfs(int[][] matrix, Map<Integer, Integer> infSizes) {
		for (int i = 0; i < matrix.length; i++) {
			infSizes.put(i, IntStream.of(matrix[i]).sum());
		}
	}

	public int[][] initializeMatrix() {
		int[][] ret = new int[inferenceMatrix.length][inferenceMatrix[0].length];
		for (int i = 0; i < inferenceMatrix.length; i++) {
			for (int j = 0; j < inferenceMatrix[0].length; j++) {
				ret[i][j] = inferenceMatrix[i][j];
			}
		}
		return ret;
	}

	public void resetInference(int e, int[][] inferenceMatrix, Map<Integer, Integer> infSizes) {
		for (int i = 0; i < inferenceMatrix.length; i++) {
			inferenceMatrix[i][e] = 0;
			inferenceMatrix[e][i] = -1;
		}
		setWebInfs(inferenceMatrix, infSizes);
		infSizes.remove(e);
	}

	public void setInferenceMatrix() {
		inferenceMatrix = new int[webs.size()][webs.size()];
		for (int i = 0; i < webs.size(); i++) {
			for (int j = i + 1; j < webs.size(); j++) {
				if (SingleValueWeb.inference(webs.get(i), webs.get(j))) {
					inferenceMatrix[i][j] = 1;
					inferenceMatrix[j][i] = 1;
				}
			}
		}
	}

	public void setInferenceVector() {
		inferenceVectors = new HashMap<>();
		for (int i = 0; i < inferenceMatrix.length; i++) {
			int[] sub = inferenceMatrix[i];
			if (IntStream.of(sub).sum() != 0) {
				List<Integer> lst = new ArrayList<>();
				for (int j = 0; j < sub.length; j++) {
					if (sub[j] == 1) {
						lst.add(j);
					}
				}
				inferenceVectors.put(i, lst);
			}
		}
	}

	public void unionNode(CFGNode n) {
		Map<IrLocation, IrQuadPos> latestUnion = new HashMap<>();
		if (n.statements != null) {
			for (int i = 0; i < n.statements.size(); i++) {
				IrQuadPos pos = new IrQuadPos(n, i);
				addUnion(n.statements.get(i), latestUnion, pos);
			}
		}
	}

	public void addUnion(IrStatement s, Map<IrLocation, IrQuadPos> latestUnion, IrQuadPos pos) {
		if (s instanceof IrQuad) {
			IrQuad quad = (IrQuad) s;
			for (IrLocation loc : quad.use) {
				if (!loc.locationIsArray()) {
					unions.get(loc).makeSet(pos);
					if (latestUnion.containsKey(loc)) {
						unions.get(loc).union(latestUnion.get(loc), pos);
					} else {
						latestUnion.put(loc, pos);
					}
				}
			}
			if (quad.getRealDst() != null) {
				IrLocation dst = (IrLocation) quad.getRealDst();
				if (!dst.locationIsArray()) {
					unions.get(quad.getRealDst()).makeSet(pos);
					latestUnion.put((IrLocation) quad.getRealDst(), pos);
				}
			}
		}
	}

	public void addVar(CFGNode n) {
		if (n.statements != null) {
			for (IrStatement s : n.statements) {
				addVar(s);
			}
		}
	}

	public void addVar(IrStatement s) {
		if (s instanceof IrQuad) {
			IrQuad quad = (IrQuad) s;
			useVars.addAll(quad.use);
			if (quad.getRealDst() != null)
				useVars.add((IrLocation) quad.getRealDst());
		}
	}

}
