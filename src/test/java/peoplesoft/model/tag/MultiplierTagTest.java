package peoplesoft.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static peoplesoft.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class MultiplierTagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new MultiplierTag(null, 1.0));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new MultiplierTag(invalidTagName, 1.0));
    }

    @Test
    public void constructor_invalidTagMultiplier_throwsIllegalArgumentException() {
        String validTagName = "intern";
        double invalidTagMultiplier = -1;
        assertThrows(IllegalArgumentException.class, () -> new MultiplierTag(validTagName, invalidTagMultiplier));
    }

    @Test
    public void isValidTagMultiplier() {
        // negative tag multiplier
        assertFalse(MultiplierTag.isValidTagMultiplier(-1));
    }
}
