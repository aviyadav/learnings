package editck.naaccr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * Run edits for a given stream of data records, and an engine.
 * 
 * Not thread-safe.
 * 
 * @author Gopinathan Balaji
 *
 */
final class EditsRunner implements RunEditsCallback {
	private static final Logger LOG = LoggerFactory.getLogger(EditsRunner.class);

	private static final ThreadLocal<Pointer> OWNER = ThreadLocal.withInitial(() -> new Memory(Pointer.SIZE));
	private final EditsEngine _engine;
	private final Stream<String> _data;
	private final ArrayList<ValidationMessage> _vmList = new ArrayList<>();
	private final ReadWriteLock _vmListRWLock = new ReentrantReadWriteLock();
	private int _smfID;
	private Set<EditSetDataObject> _editSets;
	private Set<LayoutDataObject> _layouts;
	private String _currentDataRecordReference;
	private EditSetDataObject _currentEditSetDataObject;
	private LayoutDataObject _currentLayoutDataObject;

	/**
	 * Constructor
	 * 
	 * @param engine
	 *            - an initialized engine. Non-null.
	 * @param data
	 *            - a stream of data records. Non-null.
	 * @throws EditsEngineException
	 */
	EditsRunner(final EditsEngine engine, final Stream<String> data) throws EditsEngineException {
		Objects.requireNonNull(engine, "engine cannot be null");
		Objects.requireNonNull(data, "data stream cannot be null");
		this._engine = engine;
		this._data = data;
		this._smfID = engine.getMetafileID();
		this._editSets = engine.getEditSetDataObjects();
		this._layouts = engine.getLayoutDataObjects();
	}

	private void addValidationMessage(final ValidationMessage vm) {
		final Lock writeLock = this._vmListRWLock.writeLock();
		writeLock.lock();
		try {
			this._vmList.add(vm);
		} finally {
			writeLock.unlock();
		}
	}

	private List<ValidationMessage> getValidationMessages() {
		ArrayList<ValidationMessage> vmList = null;
		final Lock readLock = this._vmListRWLock.readLock();
		readLock.lock();
		try {
			vmList = new ArrayList<>(this._vmList.size() + 4);
			vmList.addAll(this._vmList);
		} finally {
			readLock.unlock();
		}
		return Collections.unmodifiableList(vmList);
	}

	private void processRecord(final String record) throws EditsEngineException {
		for (final EditSetDataObject editSet : this._editSets) {
			this._currentEditSetDataObject = editSet;
			for (final LayoutDataObject layout : this._layouts) {
				this._currentLayoutDataObject = layout;
				// XXX: Use a specialized UDT.
				this._currentDataRecordReference = record;
				int[] errorsCount = new int[1];
				final Object[] args = { this._smfID, editSet.getEditSetTag(), layout.getLayoutTag(), record,
						EngineEditsOptions.EE_NOSKIP.value(), errorsCount, OWNER.get(), this };
				this._engine.runEditsForRecord(args);
			}
		}
	}

	/**
	 * Run edits on all records in the stream, and return the accummulated list of
	 * validation messages
	 * 
	 * @return - an unmodifiable list of validation messages.
	 * @throws EditsEngineException
	 */
	final List<ValidationMessage> runEdits() throws EditsEngineException {
		final Iterator<String> recordIt = this._data.iterator();
		while (recordIt.hasNext()) {
			final String record = recordIt.next();
			LOG.debug("--------> Processing record: '{}'", record);
			processRecord(record);
		}
		return this.getValidationMessages();
	}

	/**
	 * @see RunEditsCallback#callback(com.sun.jna.Pointer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public final void callback(final Pointer pointerToObject, final String editTag, final String editName,
			final String adminCode, final String errorType, final String message) {
		assert OWNER.get().equals(pointerToObject) : "Unsafe thread usage.";
		final ValidationMessage vm = new ValidationMessage(this._currentDataRecordReference);
		vm.setEditSet(this._currentEditSetDataObject).setLayout(this._currentLayoutDataObject).setEditTag(editTag)
				.setEditName(editName).setAdminCode(adminCode).setErrorType(errorType).setMessage(message);
		this.addValidationMessage(vm);
		LOG.info("In callback: {}.", vm);
	}

}
