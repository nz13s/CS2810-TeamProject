package entities;

/**
 * A class that implements IFakeable means that it can be used to send data to the database,
 * having incomplete data.
 *
 * Objects that are fake will not have a unique ID, for instance.
 *
 * A fake object is one that has no relation to the database (yet)
 */
public interface IFakeable {
    public boolean isFake();
}
