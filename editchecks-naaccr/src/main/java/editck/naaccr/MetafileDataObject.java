package editck.naaccr;

/**
 * Informatin about the metafile.
 * 
 * @author Gopinathan Balaji
 *
 */
public class MetafileDataObject implements EngineDataObject {
	private String _metafileAbsolutePath = "<not-set>";
	private int _metafileID = -1;
	private String _metafileVersion = "<not-set>";
	private String _metafileComment = "<not-set>";

	/**
	 * Constructor
	 */
	public MetafileDataObject() {
		// To satisfy contract.
	}

	/**
	 * Constructor
	 * @param metafileAbsolutePath
	 * @param metafileID
	 */
	public MetafileDataObject(final String metafileAbsolutePath, final int metafileID) {
		this.set("metafileAbsolutePath", metafileAbsolutePath);
		this._metafileID = metafileID;
	}

	/**
	 * @see EngineDataObject#set(java.lang.String, java.lang.String)
	 */
	@Override
	public void set(final String fieldName, final String fieldValue) {
		switch (fieldName) {
		case "metafileAbsolutePath":
			this._metafileAbsolutePath = fieldValue == null ? "<null>" : fieldValue;
			break;
		case "metafileID":
			try {
				this._metafileID = Integer.parseInt(fieldValue);
			} catch (final NumberFormatException nfe) {
				this._metafileID = -1;
			}
			break;
		case "metafileVersion":
			this._metafileVersion = fieldValue == null ? "<null>" : fieldValue;
			break;
		case "metafileComment":
			this._metafileComment = fieldValue == null ? "<null>" : fieldValue;
			break;
		default:
			throw new IllegalArgumentException("unknown field name '" + fieldName + "'");
		}
	}

	/**
	 * @return the metafileAbsolutePath
	 */
	public final String getMetafileAbsolutePath() {
		return this._metafileAbsolutePath;
	}

	/**
	 * @return the metafileID
	 */
	public final int getMetafileID() {
		return this._metafileID;
	}

	/**
	 * @return the metafileVersion
	 */
	public final String getMetafileVersion() {
		return this._metafileVersion;
	}

	/**
	 * @return the metafileComment
	 */
	public final String getMetafileComment() {
		return this._metafileComment;
	}

	/**
	 * @see EngineDataObject#isBlank()
	 */
	@Override
	public final boolean isBlank() {
		boolean blank = "<not-set>".equals(this._metafileAbsolutePath);
		blank = blank && this._metafileID == -1;
		blank = blank && "<not-set>".equals(this._metafileVersion);
		blank = blank && "<not-set>".equals(this._metafileComment);
		return blank;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(final EngineDataObject o) {
		int c = this.getClass().getName().compareTo(o.getClass().getName());
		if (c != 0)
			return c;
		MetafileDataObject mdo = (MetafileDataObject) o;
		// XXX robustness needed - to handle nulls on either side. see adse-common
		c = this._metafileAbsolutePath.compareTo(mdo._metafileAbsolutePath);
		if (c != 0)
			return c;
		c = this._metafileID - mdo._metafileID;
		if (c != 0)
			return c;
		c = this._metafileVersion.compareTo(mdo._metafileVersion);
		if (c != 0)
			return c;
		return this._metafileComment.compareTo(mdo._metafileComment);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this._metafileAbsolutePath == null) ? 0 : this._metafileAbsolutePath.hashCode());
		result = prime * result + this._metafileID;
		result = prime * result + ((this._metafileVersion == null) ? 0 : this._metafileVersion.hashCode());
		result = prime * result + ((this._metafileComment == null) ? 0 : this._metafileComment.hashCode());
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
		final MetafileDataObject other = (MetafileDataObject) obj;
		if (this._metafileAbsolutePath == null) {
			if (other._metafileAbsolutePath != null)
				return false;
		} else if (!this._metafileAbsolutePath.equals(other._metafileAbsolutePath))
			return false;
		if (this._metafileID != other._metafileID)
			return false;
		if (this._metafileVersion == null) {
			if (other._metafileVersion != null)
				return false;
		} else if (!this._metafileVersion.equals(other._metafileVersion))
			return false;
		if (this._metafileComment == null) {
			if (other._metafileComment != null)
				return false;
		} else if (!this._metafileComment.equals(other._metafileComment))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder("Metafile [AbsolutePath='");
		sb.append(this._metafileAbsolutePath);
		sb.append("', smfID=").append(this._metafileID);
		sb.append(", metafileVersion='").append(this._metafileVersion);
		sb.append("', metafileComment='").append(this._metafileComment);
		sb.append("']");
		return sb.toString();
	}

}
