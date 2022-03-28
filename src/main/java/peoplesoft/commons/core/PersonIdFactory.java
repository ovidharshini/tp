package peoplesoft.commons.core;

/**
 * Class to generate unique {@code PersonIds}.
 */
public class PersonIdFactory {
    private static int id = 0;

    /**
     * Returns a unique {@code PersonId}.
     *
     * @return PersonId.
     */
    // TODO: currently missing functionality with serdes
    public static String nextId() {
        return String.valueOf(++id);
    }

    /**
     * Sets the current id.
     *
     * @param id To set.
     */
    public static void setId(int id) {
        PersonIdFactory.id = id;
    }

    /**
     * Returns the current id.
     *
     * @return Id.
     */
    public static int getId() {
        return id;
    }
}
