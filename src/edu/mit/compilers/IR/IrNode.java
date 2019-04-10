package edu.mit.compilers.IR;

public abstract class IrNode implements Named{
	
	public int lineNumber;
	public int columnNumber;
	public String filename;
	
	public IrNode() {
		lineNumber = -1;
		columnNumber = -1;
		filename = null;
	}
	
	public IrNode(int lineNumber, int columnNumber, String filename) {
		setLineNumber(lineNumber);
		setColumnNumber(columnNumber);
		setFilename(filename);
	}
	public int getLineNumber() {
		return lineNumber;
	}


	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}


	public int getColumnNumber() {
		return columnNumber;
	}


	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}


	public String getFilename() {
		return filename;
	}


	public void setFilename(String filename) {
		this.filename = filename;
	}


	public abstract void accept(IrNodeVistor vistor);
}
