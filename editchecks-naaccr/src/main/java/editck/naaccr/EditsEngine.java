package editck.naaccr;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

/**
 * Exposes the GenEDITS engine functions in a friendly, safe and accessible
 * manner.
 * 
 * @author Gopinathan Balaji
 *
 */
public class EditsEngine implements AutoCloseable {
	private static final Logger LOG = LoggerFactory.getLogger(EditsEngine.class);
	private static final HashMap<String, EditsEngine> ALREADY_BUILT_ENGINES = new HashMap<>();

	/**
	 * If an engine is already built, returns it, else, returns a new engine
	 * initialized for use. The caller is responsible for {@link #close() closing}
	 * the engine after use.
	 * 
	 * @param metafilePath
	 *            - path to the SMF metafie
	 * @return - an initialized engine
	 * @throws EditsEngineException
	 */
	public static final synchronized EditsEngine initialize(final String metafilePath) throws EditsEngineException {
		final File metafile = new File(metafilePath);
		final String metafileAbsolutePath = metafile.getAbsolutePath();
		final EditsEngine existingEngine = ALREADY_BUILT_ENGINES.get(metafileAbsolutePath);
		if (existingEngine != null) {
			LOG.debug("Existing engine found for metafileAbsolutePath = '{}'.", metafileAbsolutePath);
			return existingEngine;
		}
		if (LOG.isDebugEnabled()) {
			System.setProperty("jna.debug_load", "true");
			System.setProperty("jna.debug_load.jna", "true");
		}
		if (!(metafile.isFile() && metafile.canRead()))
			throw new IllegalArgumentException(
					"supplied metafile '" + metafileAbsolutePath + "' not found, or is not readable");
		final EditsEngine newEngine = new EditsEngine(metafileAbsolutePath);
		ALREADY_BUILT_ENGINES.put(metafileAbsolutePath, newEngine);
		LOG.info("New engine initialized for metafileAbsolutePath = '{}'.", metafileAbsolutePath);
		return newEngine;
	}

	private final NativeLibrary _lib;
	private final String _engineVersion;
	private final String _mfStructureVersion;
	private final String _mfAbsolutePath;
	private final int _mfID;
	private final MetafileDataObject _mfInfo;
	private final Function _fnRunEdits;
	private int _versionComparisionResult;
	private final TreeSet<EditSetDataObject> _editSetDataObjects = new TreeSet<>();
	private final TreeSet<LayoutDataObject> _layoutDataObjects = new TreeSet<>();

	private EditsEngine(final String metafileAbsolutePath) throws EditsEngineException {
		this._mfAbsolutePath = metafileAbsolutePath;
		this._lib = NativeLibrary.getInstance("edits50");
		this._engineVersion = fnGetEngineMetafileInfo("Edit_GetEngineVersion");
		this._mfStructureVersion = fnGetEngineMetafileInfo("Edit_GetMetafileStructureVersion");
		this._mfID = fnInitialize(metafileAbsolutePath);
		this._mfInfo = new MetafileDataObject(metafileAbsolutePath, this._mfID);
		final String metafileVersion = fnGetEngineMetafileInfo("Edit_GetMetafileVersion");
		final String metafileComment = fnGetEngineMetafileInfo("Edit_GetMetafileComment");
		this._mfInfo.set("metafileVersion", metafileVersion);
		this._mfInfo.set("metafileComment", metafileComment);
		this._fnRunEdits = this._lib.getFunction("Edit_RunEdits");
	}

	private void collectErrorsAndThrowException() throws EditsEngineException {
		final Function edit_GetEngineErrorsCount = this._lib.getFunction("Edit_GetEngineErrorsCount");
		int[] howManyErrors = new int[1];
		final Object[] args = { this._mfID, howManyErrors };
		int returnValue = edit_GetEngineErrorsCount.invokeInt(args);
		switch (returnValue) {
		case 0:
			final int errorCount = howManyErrors[0];
			final Function edit_GetEngineErrorByIndex = this._lib.getFunction("Edit_GetEngineErrorByIndex");
			EditsEngineException ele = new EditsEngineException("exception in edits library");
			for (int errorIndex = 1; errorIndex <= errorCount; errorIndex++) {
				byte[] errorMessage = new byte[10000];
				int errorMessageLength = 0;
				final Object[] args2 = { this._mfID, errorIndex, errorMessage, errorMessageLength };
				int returnValue2 = edit_GetEngineErrorByIndex.invokeInt(args2);
				switch (returnValue2) {
				case 0:
					ele.addErrorMessage(Native.toString(errorMessage));
					break;
				case -1:
					ele.addErrorMessage("System error encountered when collecting library errors");
					break;
				case -2:
					ele.addErrorMessage("metafile id " + this._mfID
							+ " not known to the edits engine when collecting library errors");
					break;
				default:
					ele.addErrorMessage("unknown exception when collecting library errors");
				}
			}
			break;
		case -1:
			throw new EditsEngineException("System error encountered when collecting library errors");
		case -2:
			throw new EditsEngineException(
					"metafile id " + this._mfID + " not known to the edits engine when collecting library errors");
		default:
			throw new EditsEngineException("unknown exception when collecting library errors");
		}
	}

	private String fnGetEngineMetafileInfo(final String functionName) throws EditsEngineException {
		final Function metafileInfoFunction = this._lib.getFunction(functionName);
		byte[] infoBuffer = new byte[2048];
		int infoBufferLength = infoBuffer.length;
		final Object[] args = { infoBuffer, infoBufferLength };
		final int rvMetafileInfoFunction = metafileInfoFunction.invokeInt(args);
		if (rvMetafileInfoFunction == 0)
			return Native.toString(infoBuffer);
		collectErrorsAndThrowException();
		return null;
	}

	private int fnInitialize(final String metafileAbsolutePath) throws EditsEngineException {
		final Function edit_Initialize = this._lib.getFunction("Edit_Initialize");
		int[] metafileID = new int[1];
		byte[] metafileInitializationError = new byte[100];
		int errorBufferSize = 0;
		int[] versionCompare = new int[1];
		final Object[] args = { metafileID, metafileAbsolutePath, metafileInitializationError, errorBufferSize,
				versionCompare };
		int returnValue = edit_Initialize.invokeInt(args);
		if (errorBufferSize > 0) {
			String mfInitializationError = Native.toString(metafileInitializationError);
			throw new EditsEngineException(mfInitializationError);
		}
		this._versionComparisionResult = versionCompare[0];
		final int mfID = metafileID[0];
		if (returnValue == -1)
			collectErrorsAndThrowException();
		return mfID;
	}

	private <T extends EngineDataObject> Set<T> getObjectsUsingCursor(final String sql, final String[] fieldNames,
																	  final Class<T> clazz) throws EditsEngineException {
		final TreeSet<T> edoSet = new TreeSet<>();
		final Function edit_OpenCursor = this._lib.getFunction("Edit_OpenCursor");
		final Function edit_GetCursorDataSizeByName = this._lib.getFunction("Edit_GetCursorDataSizeByName");
		final Function edit_GetCursorDataByName = this._lib.getFunction("Edit_GetCursorDataByName");
		final Function edit_CursorNext = this._lib.getFunction("Edit_CursorNext");
		final Function edit_CloseCursor = this._lib.getFunction("Edit_CloseCursor");
		final StringBuilder sb = new StringBuilder("Cursor-");
		for (final String f : fieldNames)
			sb.append(f).append("-");
		sb.replace(sb.length() - 1, sb.length(), "");
		final String cursorName = sb.toString();
		boolean errorsEncountered = true;
		try {
			final Object[] argsOpenCursor = { this._mfID, cursorName, sql };
			int rvOpenCursor = edit_OpenCursor.invokeInt(argsOpenCursor);
			if (rvOpenCursor == -1)
				collectErrorsAndThrowException();
			int rvCursorNext = 0;
			final Object[] argsCursorNext = { this._mfID, cursorName };
			while (rvCursorNext == 0) {
				final T edo = EngineDataObject.create(clazz);
				for (final String fieldName : fieldNames) {
					int[] fieldValueSize = new int[1];
					final Object[] argsGetCursorDataSizeByName = { this._mfID, cursorName, fieldName, fieldValueSize };
					int rvGetCursorDataSizeByName = edit_GetCursorDataSizeByName.invokeInt(argsGetCursorDataSizeByName);
					if (rvGetCursorDataSizeByName == -1)
						collectErrorsAndThrowException();

					byte[] fieldValue = new byte[fieldValueSize[0]];
					final Object[] argsGetCursorDataByName = { this._mfID, cursorName, fieldName, fieldValue,
							fieldValueSize[0] };
					int rvGetCursorDataByName = edit_GetCursorDataByName.invokeInt(argsGetCursorDataByName);
					if (rvGetCursorDataByName == -1)
						collectErrorsAndThrowException();
					edo.set(fieldName, Native.toString(fieldValue));
				}
				if (!edo.isBlank())
					edoSet.add(edo);
				rvCursorNext = edit_CursorNext.invokeInt(argsCursorNext);
			}
			errorsEncountered = false;
		} finally {
			final Object[] argsCloseCursor = { this._mfID, cursorName };
			final int rvCloseCursor = edit_CloseCursor.invokeInt(argsCloseCursor);
			if (rvCloseCursor == -1 && (!errorsEncountered))
				collectErrorsAndThrowException();
		}
		return edoSet;
	}

	private void fnExit() throws EditsEngineException {
		final Function edit_Exit = this._lib.getFunction("Edit_Exit");
		final Object[] args = { this._mfID };
		final int returnValue = edit_Exit.invokeInt(args);
		if (returnValue == -1)
			collectErrorsAndThrowException();
	}

	/**
	 * @return the absolute path of the SMF metafile configured for this engine.
	 */
	public final String getMetafileAbsolutePath() {
		return this._mfAbsolutePath;
	}

	/**
	 * @return the engine version, as reported by the library.
	 */
	public final String getEngineVersion() {
		return this._engineVersion;
	}

	/**
	 * @return the metafile structure version, as reported by the library.
	 */
	public final String getMetafileStructureVersion() {
		return this._mfStructureVersion;
	}

	/**
	 * @return the metafile ID (SMF_ID) for the SMF file configured in the engine.
	 */
	public final int getMetafileID() {
		return this._mfID;
	}

	/**
	 * @return information about the metafile
	 */
	public final MetafileDataObject getMetafileInformation() {
		return this._mfInfo;
	}

	/**
	 * @return result of comparison of engine version and metafile version.
	 */
	public final String getVersionComparisonResult() {
		switch (this._versionComparisionResult) {
		case 0:
			return "0 = metafile is the right version for this version of the Engine.";
		case 1:
			return "1 = metafile is out of date, but the Engine can update it; see Edit_UpdateMetafile.";
		case -1:
			return "-1 = metafile version is newer than Engine version; update the Engine!";
		default:
			return this._versionComparisionResult + " = unkown version comparison result.";
		}
	}

	/**
	 * @return an unmodifiable set of all edit sets configured in the metafile.
	 * @throws EditsEngineException
	 */
	public final SortedSet<EditSetDataObject> getEditSetDataObjects() throws EditsEngineException {
		if (this._editSetDataObjects.isEmpty())
			synchronized (this._editSetDataObjects) {
				if (this._editSetDataObjects.isEmpty()) {
					final String sql = "SELECT esep.EditSetPKey, es.EditSetTag, es.EditSetName, COUNT(esep.EditPKey) AS EditCount "
							+ "FROM EE_EditSetEditsPivot esep JOIN EE_EditSets es ON esep.EditSetPKey = es.PKey "
							+ "GROUP BY esep.EditSetPKey, es.EditSetTag, es.EditSetName";
					final String[] fieldNames = { "EditSetPKey", "EditSetTag", "EditSetName", "EditCount" };
					this._editSetDataObjects.addAll(getObjectsUsingCursor(sql, fieldNames, EditSetDataObject.class));
				}
			}
		return Collections.unmodifiableSortedSet(this._editSetDataObjects);
	}

	/**
	 * @return an unmodifiable set of all layouts configured in the metafile.
	 * @throws EditsEngineException
	 */
	public final SortedSet<LayoutDataObject> getLayoutDataObjects() throws EditsEngineException {
		if (this._layoutDataObjects.isEmpty())
			synchronized (this._layoutDataObjects) {
				if (this._layoutDataObjects.isEmpty()) {
					final String sql = "SELECT eel.PKey, eel.LayoutTag, eel.LayoutName FROM EE_Layouts eel";
					final String[] fieldNames = { "PKey", "LayoutTag", "LayoutName" };
					this._layoutDataObjects.addAll(getObjectsUsingCursor(sql, fieldNames, LayoutDataObject.class));
				}
			}
		return Collections.unmodifiableSortedSet(this._layoutDataObjects);
	}

	/**
	 * Run edit checks for all configured {@link #getEditSetDataObjects() edit set}
	 * and {@link #getLayoutDataObjects() layout) combinations known to the
	 * metafile, and return an unmodifiable list of all accummulated validation
	 * messages.
	 * 
	 * @param data
	 *            - a stream of data records
	 * @return - list of validation messages.
	 * @throws EditsEngineException
	 */
	public final List<ValidationMessage> runEdits(final Stream<String> data) throws EditsEngineException {
		return new EditsRunner(this, data).runEdits();
	}

	/**
	 * For callback from the {@link EditsRunner}.
	 * 
	 * @param runEditsArgs
	 *            - arguments to be passed to the {@code Edit_RunEdits} engine
	 *            function.
	 * @throws EditsEngineException
	 */
	final void runEditsForRecord(final Object[] runEditsArgs) throws EditsEngineException {
		int rvRunEdits = this._fnRunEdits.invokeInt(runEditsArgs);
		if (rvRunEdits == -1)
			this.collectErrorsAndThrowException();
	}

	/**
	 * Exit the engine and dispose the native library. This engine instance is
	 * unusable after this method is called.
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public final void close() throws EditsEngineException {
		try {
			this.fnExit();
			LOG.info("Engine exit called.");
		} finally {
			this._lib.dispose();
			LOG.info("Library disposed.");
		}
	}

}
