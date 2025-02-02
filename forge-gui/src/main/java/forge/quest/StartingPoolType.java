package forge.quest;

public enum StartingPoolType {
    Complete("Unrestricted"),
    Sanctioned("Sanctioned format"),
    Casual("Casual/Historic format"),
    CustomFormat("Custom format"),
    Precon("Event or starter deck"),
    SealedDeck("My sealed deck"),
    DraftDeck("My draft deck"),
    Cube("Predefined cube");

    private final String caption;

    StartingPoolType(String caption0) {
        caption = caption0;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return caption;
    }
}
