package editck.naaccr;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * In addition to implementing this interface, all implementors should also have
 * a {@code public} no-arg constructor.
 * 
 * @author Gopinathan Balaji
 *
 */
public interface EngineDataObject extends Comparable<EngineDataObject> {
	/**
	 * Set field values.
	 * @param fieldName - name
	 * @param fieldValue - value
	 */
	void set(final String fieldName, final String fieldValue);

	/**
	 * @return {@code true} if all the field values are blank. {@code false} otherwise.
	 */
	boolean isBlank();

	/**
	 * Factory method.
	 * @param clazz - a class implementing the {@link EngineDataObject} interface 
	 * @return - an instance of the class.
	 */
	public static <I extends EngineDataObject> I create(final Class<I> clazz) {
		try {
			final Constructor<I> constructor = clazz.getConstructor((Class[]) null);
			return constructor.newInstance((Object[]) null);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}

}
