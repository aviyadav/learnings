package editck.naaccr;

/**
 * An object representation of an edit set in the SMF file.
 * 
 * @author Gopinathan Balaji
 *
 */
public final class EditSetDataObject implements EngineDataObject {
	private String _editSetPKey;
	private String _editSetTag;
	private String _editSetName;
	private int _editCount;

	/**
	 * Constructor.
	 */
	public EditSetDataObject() {
		super();
	}

	/**
	 * @see EngineDataObject#set(java.lang.String, java.lang.String)
	 */
	@Override
	public final void set(final String fieldName, final String fieldValue) {
		switch (fieldName) {
		case "EditSetPKey":
			this._editSetPKey = fieldValue;
			break;
		case "EditSetTag":
			this._editSetTag = fieldValue;
			break;
		case "EditSetName":
			this._editSetName = fieldValue;
			break;
		case "EditCount":
			try {
				this._editCount = Integer.parseInt(fieldValue);
			} catch (final NumberFormatException nfe) {
				this._editCount = 0;
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown field name: " + fieldName);
		}
	}

	/**
	 * @see EngineDataObject#isBlank()
	 */
	@Override
	public final boolean isBlank() {
		boolean blank = this._editSetPKey == null || this._editSetPKey.trim().length() == 0;
		blank = blank && (this._editSetTag == null || this._editSetTag.trim().length() == 0);
		blank = blank && (this._editSetName == null || this._editSetName.trim().length() == 0);
		blank = blank && this._editCount == 0;
		return blank;
	}

	/**
	 * @return the editSetPKey
	 */
	final String getEditSetPKey() {
		return this._editSetPKey;
	}

	/**
	 * @return the editSetTag
	 */
	final String getEditSetTag() {
		return this._editSetTag;
	}

	/**
	 * @return the editSetName
	 */
	final String getEditSetName() {
		return this._editSetName;
	}

	/**
	 * @return the editCount
	 */
	final int getEditCount() {
		return this._editCount;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this._editCount;
		result = prime * result + ((this._editSetName == null) ? 0 : this._editSetName.hashCode());
		result = prime * result + ((this._editSetPKey == null) ? 0 : this._editSetPKey.hashCode());
		result = prime * result + ((this._editSetTag == null) ? 0 : this._editSetTag.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final EditSetDataObject other = (EditSetDataObject) obj;
		if (this._editSetPKey == null) {
			if (other._editSetPKey != null)
				return false;
		} else if (!this._editSetPKey.equals(other._editSetPKey))
			return false;
		if (this._editSetTag == null) {
			if (other._editSetTag != null)
				return false;
		} else if (!this._editSetTag.equals(other._editSetTag))
			return false;
		if (this._editSetName == null) {
			if (other._editSetName != null)
				return false;
		} else if (!this._editSetName.equals(other._editSetName))
			return false;
		return this._editCount == other._editCount;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(final EngineDataObject o) {
		int c = this.getClass().getName().compareTo(o.getClass().getName());
		if (c != 0)
			return c;
		final EditSetDataObject esi = (EditSetDataObject) o;
		// XXX robust compareTo that accommodates nulls, see adse-common
		c = this._editSetPKey.compareTo(esi._editSetPKey);
		if (c != 0)
			return c;
		c = this._editSetTag.compareTo(esi._editSetTag);
		if (c != 0)
			return c;
		c = this._editSetName.compareTo(esi._editSetName);
		if (c != 0)
			return c;
		return this._editCount - esi._editCount;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder("EditSet [PKey='");
		sb.append(this._editSetPKey);
		sb.append("', Tag='").append(this._editSetTag);
		sb.append("', Name='").append(this._editSetName);
		sb.append("', editsCount=").append(this._editCount).append("]");
		return sb.toString();
	}
}
