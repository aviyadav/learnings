package editck.naaccr;

/**
 * An object representation of known layouts in the SMF metafile.
 * 
 * @author Gopinathan Balaji
 *
 */
public final class LayoutDataObject implements EngineDataObject {

	private String _layoutPKey;
	private String _layoutTag;
	private String _layoutName;

	/**
	 * @see EngineDataObject#set(java.lang.String, java.lang.String)
	 */
	@Override
	public void set(String fieldName, String fieldValue) {
		switch (fieldName) {
		case "PKey":
			this._layoutPKey = fieldValue;
			break;
		case "LayoutTag":
			this._layoutTag = fieldValue;
			break;
		case "LayoutName":
			this._layoutName = fieldValue;
			break;
		default:
			throw new IllegalArgumentException("unknown field name '" + fieldName + "'");
		}
	}

	/**
	 * @return the layoutPKey
	 */
	public final String getLayoutPKey() {
		return this._layoutPKey;
	}

	/**
	 * @return the layoutTag
	 */
	public final String getLayoutTag() {
		return this._layoutTag;
	}

	/**
	 * @return the layoutName
	 */
	public final String getLayoutName() {
		return this._layoutName;
	}

	/**
	 * @see EngineDataObject#isBlank()
	 */
	@Override
	public boolean isBlank() {
		boolean blank = this._layoutPKey == null || this._layoutPKey.trim().length() == 0;
		blank = blank && (this._layoutTag == null || this._layoutTag.trim().length() == 0);
		blank = blank && (this._layoutName == null || this._layoutName.trim().length() == 0);
		return blank;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(EngineDataObject o) {
		int c = this.getClass().getName().compareTo(o.getClass().getName());
		if (c != 0)
			return c;
		LayoutDataObject ldo = (LayoutDataObject) o;
		// XXX to handle nulls on either side; see adse-common
		c = this._layoutPKey.compareTo(ldo._layoutPKey);
		if (c != 0)
			return c;
		c = this._layoutTag.compareTo(ldo._layoutTag);
		if (c != 0)
			return c;
		return this._layoutName.compareTo(ldo._layoutName);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this._layoutName == null) ? 0 : this._layoutName.hashCode());
		result = prime * result + ((this._layoutPKey == null) ? 0 : this._layoutPKey.hashCode());
		result = prime * result + ((this._layoutTag == null) ? 0 : this._layoutTag.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LayoutDataObject other = (LayoutDataObject) obj;
		if (this._layoutName == null) {
			if (other._layoutName != null)
				return false;
		} else if (!this._layoutName.equals(other._layoutName))
			return false;
		if (this._layoutPKey == null) {
			if (other._layoutPKey != null)
				return false;
		} else if (!this._layoutPKey.equals(other._layoutPKey))
			return false;
		if (this._layoutTag == null) {
			if (other._layoutTag != null)
				return false;
		} else if (!this._layoutTag.equals(other._layoutTag))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder("Layout [PKey='");
		sb.append(this._layoutPKey);
		sb.append("', Tag='").append(this._layoutTag);
		sb.append("', Name='").append(this._layoutName);
		sb.append("']");
		return sb.toString();
	}

}
