package editck.naaccr;

import java.util.ArrayList;
import java.util.List;

/**
 * Any exception that's encountered by the {@link EditsEngine} when invoking the
 * library functions, and are usually those reported by the library itself.
 * 
 * @author Gopinathan Balaji
 *
 */
public class EditsEngineException extends Exception {
	private static final long serialVersionUID = -8880604613285235744L;
	private final List<String> _errorMessages = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param message
	 *            - error message
	 */
	public EditsEngineException(final String message) {
		super(message);
	}

	/**
	 * Add a related error message.
	 * 
	 * @param errorMessage
	 */
	final void addErrorMessage(final String errorMessage) {
		this._errorMessages.add(errorMessage);
	}

	/**
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		final StringBuilder sb = new StringBuilder(
				"Exception when invoking edits engine library function. error messages: [");
		for (final String em : this._errorMessages)
			sb.append("'").append(em).append("', ");
		if (!this._errorMessages.isEmpty())
			sb.replace(sb.length() - 2, sb.length(), "");
		sb.append("]");
		return sb.toString();
	}
}
