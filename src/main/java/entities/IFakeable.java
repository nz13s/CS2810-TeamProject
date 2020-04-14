package entities;

/**
 * A class that implements IFakeable means that it can be used to send data to the database,
 * having incomplete data.
 *
 * <p>Objects that are fake will not have a unique ID, for instance.
 *
 * <p>A fake object is one that has no relation to the database (yet)
 *
 * @author Oliver Graham
 */
public interface IFakeable {
  boolean isFake();
}
