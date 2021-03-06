package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedTask.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalTasks.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.DateTime;
import seedu.address.model.task.Description;
import seedu.address.model.task.Title;
import seedu.address.model.task.Type;

public class JsonAdaptedTaskTest {
    private static final String INVALID_TITLE = "R@te movie";
    private static final String INVALID_DATE_TIME = "1-1-2020 12:00";
    private static final String INVALID_TYPE = " ";
    private static final String INVALID_DESCRIPTION = "@example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_TITLE = BENSON.getTitle().toString();
    private static final String VALID_DATE_TIME = BENSON.getDateTime().toString();
    private static final String VALID_DESCRIPTION = BENSON.getDescription().toString();
    private static final String VALID_TYPE = BENSON.getType().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final String VALID_STATUS = BENSON.getStatus().toString();

    @Test
    public void toModelType_validTaskDetails_returnsTask() throws Exception {
        JsonAdaptedTask task = new JsonAdaptedTask(BENSON);
        assertEquals(BENSON, task.toModelType());
    }

    @Test
    public void toModelType_invalidTitle_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(INVALID_TITLE, VALID_DATE_TIME, VALID_DESCRIPTION,
                        VALID_TYPE, VALID_TAGS, VALID_STATUS);
        String expectedMessage = Title.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullTitle_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(null, VALID_DATE_TIME, VALID_DESCRIPTION, VALID_TYPE, VALID_TAGS, VALID_STATUS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Title.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_invalidDateTime_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(VALID_TITLE, INVALID_DATE_TIME, VALID_DESCRIPTION,
                        VALID_TYPE, VALID_TAGS, VALID_STATUS);
        String expectedMessage = DateTime.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullDateTime_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(VALID_TITLE, null, VALID_DESCRIPTION, VALID_TYPE, VALID_TAGS, VALID_STATUS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, DateTime.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(VALID_TITLE, VALID_DATE_TIME, INVALID_DESCRIPTION,
                        VALID_TYPE, VALID_TAGS, VALID_STATUS);
        String expectedMessage = Description.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(VALID_TITLE, VALID_DATE_TIME, null, VALID_TYPE, VALID_TAGS, VALID_STATUS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_invalidType_throwsIllegalValueException() {
        JsonAdaptedTask task =
                new JsonAdaptedTask(VALID_TITLE, VALID_DATE_TIME, VALID_DESCRIPTION,
                        INVALID_TYPE, VALID_TAGS, VALID_STATUS);
        String expectedMessage = Type.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_nullType_throwsIllegalValueException() {
        JsonAdaptedTask task = new JsonAdaptedTask(VALID_TITLE, VALID_DATE_TIME,
                VALID_DESCRIPTION, null, VALID_TAGS, VALID_STATUS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Type.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, task::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedTask task =
                new JsonAdaptedTask(VALID_TITLE, VALID_DATE_TIME, VALID_DESCRIPTION,
                        VALID_TYPE, invalidTags, VALID_STATUS);
        assertThrows(IllegalValueException.class, task::toModelType);
    }

}
