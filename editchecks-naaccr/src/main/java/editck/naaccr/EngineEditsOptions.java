package editck.naaccr;

/**
 * Enumeration of edit set options supported by the {@code Edit_RunEdits}
 * function in the GenEDITS library.
 * 
 * @author Gopinathan Balaji
 *
 */
public enum EngineEditsOptions {
	EE_NOSKIP(0), EE_SKIPEMPTY(1), EE_SKIPFAIL(2), EE_SUPPRESSWARN(128);

	private final int _value;

	EngineEditsOptions(final int value) {
		this._value = value;
	}

	/**
	 * @return integer value to be used in the function invocation.
	 */
	public final int value() {
		return this._value;
	}

}
