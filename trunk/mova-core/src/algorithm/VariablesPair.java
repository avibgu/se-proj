package algorithm;

public class VariablesPair {
	
	protected	int	_first;
	protected	int	_second;

	public VariablesPair(int first, int second) {
		setFirst(first);
		setSecond(second);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof VariablesPair))
			return false;
		
		return (((VariablesPair)obj).getFirst() == getFirst() &&
				((VariablesPair)obj).getSecond() == getSecond()	);
	}
	
	@Override
	public int hashCode() {
		
		return (_first * _second) % 100;
	}

	public void setSecond(int _second) {
		this._second = _second;
	}

	public int getSecond() {
		return _second;
	}

	public void setFirst(int _first) {
		this._first = _first;
	}

	public int getFirst() {
		return _first;
	}
}
