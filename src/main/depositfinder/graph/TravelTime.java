package main.depositfinder.graph;

/**
 *  This class is used to enforce non-negative, non-zero travel times for
 *  paths between LocationNodes.
 */
class TravelTime {
    private final int value;

    /**
     * @param value a positive integer value.
     * @throws IllegalArgumentException if value is negative or zero
     */
    TravelTime(int value) throws IllegalArgumentException {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be a positive, non-zero number.");
        }
        this.value = value;
    }

    /**
     * @param value                     a positive integer value.
     * @throws IllegalArgumentException if value is negative or zero
     * @throws NumberFormatException    if value cannot be parsed by Integer class.
     * @see                             <code>Integer.parseInt();</code>
     */
    TravelTime(String value) throws IllegalArgumentException {
        this(Integer.parseInt(value));
    }

    int get() {
        return value;
    }
}
