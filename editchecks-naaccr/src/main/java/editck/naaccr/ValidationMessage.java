package editck.naaccr;

import java.util.Objects;

/**
 * A single validation message emitted from the library during
 * {@code Edit_RunEdits} invocation's {@link RunEditsCallback callback}. An edit
 * may emit multiple error messages. Multiple edits may be run against a given
 * record.
 * 
 * @author Gopinathan Balaji
 *
 */
public final class ValidationMessage {
	private final String _dataReference;
	private EditSetDataObject _editSet;
	private LayoutDataObject _layout;
	private String _editTag;
	private String _editName;
	private String _errorType;
	private String _adminCode;
	private String _message;

	/**
	 * Constructor
	 * 
	 * @param dataReference
	 */
	ValidationMessage(final String dataReference) {
		// XXX: rather than store the entire data record here, store a compact
		// reference to it. May need a UDT.
		Objects.requireNonNull(dataReference, "data reference cannot be null");
		this._dataReference = dataReference.length() > 20 ? dataReference.substring(0, 17) + "..." : dataReference;
	}

	/**
	 * @return the dataReference
	 */
	public final String getDataReference() {
		return this._dataReference;
	}

	/**
	 * @return the editSet
	 */
	public final EditSetDataObject getEditSet() {
		return this._editSet;
	}

	/**
	 * @param editSet
	 *            the editSet to set
	 */
	final ValidationMessage setEditSet(final EditSetDataObject editSet) {
		this._editSet = editSet;
		return this;
	}

	/**
	 * @return the layout
	 */
	public final LayoutDataObject getLayout() {
		return this._layout;
	}

	/**
	 * @param layout
	 *            the layout to set
	 */
	final ValidationMessage setLayout(final LayoutDataObject layout) {
		this._layout = layout;
		return this;
	}

	/**
	 * @return the editTag
	 */
	public final String getEditTag() {
		return this._editTag;
	}

	/**
	 * @param editTag
	 *            the editTag to set
	 */
	final ValidationMessage setEditTag(final String editTag) {
		this._editTag = editTag;
		return this;
	}

	/**
	 * @return the editName
	 */
	public final String getEditName() {
		return this._editName;
	}

	/**
	 * @param editName
	 *            the editName to set
	 */
	final ValidationMessage setEditName(final String editName) {
		this._editName = editName;
		return this;
	}

	/**
	 * @return the errorType
	 */
	public final String getErrorType() {
		return this._errorType;
	}

	/**
	 * @param errorType
	 *            the errorType to set
	 */
	final ValidationMessage setErrorType(final String errorType) {
		this._errorType = errorType;
		return this;
	}

	/**
	 * @return the adminCode
	 */
	public final String getAdminCode() {
		return this._adminCode;
	}

	/**
	 * @param adminCode
	 *            the adminCode to set
	 */
	final ValidationMessage setAdminCode(final String adminCode) {
		this._adminCode = adminCode;
		return this;
	}

	/**
	 * @return the message
	 */
	public final String getMessage() {
		return this._message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	final ValidationMessage setMessage(final String message) {
		this._message = message;
		return this;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder("ValidationMessage [dataRef='");
		sb.append(this._dataReference);
		sb.append("', editSetName='").append(this._editSet == null ? "<null>" : this._editSet.getEditSetName());
		sb.append("', layoutName='").append(this._layout == null ? "<null>" : this._layout.getLayoutName());
		sb.append("', editTag='").append(this._editTag);
		sb.append("', editName='").append(this._editName);
		sb.append("', errorType='").append(this._errorType);
		sb.append("', adminCode='").append(this._adminCode);
		sb.append("', message='").append(this._message);
		sb.append("']");
		return sb.toString();
	}

}
