package editck.naaccr;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

/**
 * JNA Callback to handle call backs from the GenEDITS' {@code Edit_RunEdits}
 * function.
 * 
 * @author Gopinathan Balaji
 *
 */
public interface RunEditsCallback extends Callback {
	/**
	 * In compliance with the exported CallbakRunEdits callback function in GenEDITS
	 * library.
	 * 
	 * @param pointerToObject
	 *            - caller's pointer.
	 * @param editTag
	 *            - edit tag
	 * @param editName
	 *            - edit name
	 * @param adminCode
	 *            - admin code
	 * @param errorType
	 *            - error type - warning / error
	 * @param message
	 *            - message
	 */
	void callback(final Pointer pointerToObject, final String editTag, final String editName, final String adminCode,
			final String errorType, final String message);
}
