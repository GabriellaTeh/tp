package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DATE_TIME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DATE_TIME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_TIME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TYPE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TYPE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TYPE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_TIME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_TIME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TYPE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TYPE_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_TASK;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.DateTime;
import seedu.address.model.task.Description;
import seedu.address.model.task.Title;
import seedu.address.model.task.Type;
import seedu.address.testutil.EditTaskDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_TITLE_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + TITLE_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + TITLE_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_TITLE_DESC, Title.MESSAGE_CONSTRAINTS); // invalid title
        assertParseFailure(parser, "1" + INVALID_DATE_TIME_DESC, DateTime.MESSAGE_CONSTRAINTS); // invalid dateTime
        assertParseFailure(parser, "1" + INVALID_DESCRIPTION_DESC, Description.MESSAGE_CONSTRAINTS); // invalid desc
        assertParseFailure(parser, "1" + INVALID_TYPE_DESC, Type.MESSAGE_CONSTRAINTS); // invalid type
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid dateTime followed by valid description
        assertParseFailure(parser, "1" + INVALID_DATE_TIME_DESC + DESCRIPTION_DESC_AMY, DateTime.MESSAGE_CONSTRAINTS);

        // valid dateTime followed by invalid dateTime. The test case for invalid dateTime followed by valid dateTime
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + DATE_TIME_DESC_BOB + INVALID_DATE_TIME_DESC, DateTime.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Task} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_TITLE_DESC + INVALID_DESCRIPTION_DESC + VALID_TYPE_AMY
                        + VALID_DATE_TIME_AMY, Title.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_TASK;
        String userInput = targetIndex.getOneBased() + DATE_TIME_DESC_BOB + TAG_DESC_HUSBAND
                + DESCRIPTION_DESC_AMY + TYPE_DESC_AMY + TITLE_DESC_AMY + TAG_DESC_FRIEND;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withTitle(VALID_TITLE_AMY)
                .withDateTime(VALID_DATE_TIME_BOB).withDescription(VALID_DESCRIPTION_AMY).withType(VALID_TYPE_AMY)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + DATE_TIME_DESC_BOB + DESCRIPTION_DESC_AMY;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withDateTime(VALID_DATE_TIME_BOB)
                .withDescription(VALID_DESCRIPTION_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // title
        Index targetIndex = INDEX_THIRD_TASK;
        String userInput = targetIndex.getOneBased() + TITLE_DESC_AMY;
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withTitle(VALID_TITLE_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // dateTime
        userInput = targetIndex.getOneBased() + DATE_TIME_DESC_AMY;
        descriptor = new EditTaskDescriptorBuilder().withDateTime(VALID_DATE_TIME_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // description
        userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_AMY;
        descriptor = new EditTaskDescriptorBuilder().withDescription(VALID_DESCRIPTION_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // type
        userInput = targetIndex.getOneBased() + TYPE_DESC_AMY;
        descriptor = new EditTaskDescriptorBuilder().withType(VALID_TYPE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditTaskDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + DATE_TIME_DESC_AMY + TYPE_DESC_AMY + DESCRIPTION_DESC_AMY
                + TAG_DESC_FRIEND + DATE_TIME_DESC_AMY + TYPE_DESC_AMY + DESCRIPTION_DESC_AMY + TAG_DESC_FRIEND
                + DATE_TIME_DESC_BOB + TYPE_DESC_BOB + DESCRIPTION_DESC_BOB + TAG_DESC_HUSBAND;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withDateTime(VALID_DATE_TIME_BOB)
                .withDescription(VALID_DESCRIPTION_BOB).withType(VALID_TYPE_BOB)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + INVALID_DATE_TIME_DESC + DATE_TIME_DESC_BOB;
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withDateTime(VALID_DATE_TIME_BOB).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_BOB + INVALID_DATE_TIME_DESC + TYPE_DESC_BOB
                + DATE_TIME_DESC_BOB;
        descriptor = new EditTaskDescriptorBuilder().withDateTime(VALID_DATE_TIME_BOB)
                .withDescription(VALID_DESCRIPTION_BOB)
                .withType(VALID_TYPE_BOB).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_TASK;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
