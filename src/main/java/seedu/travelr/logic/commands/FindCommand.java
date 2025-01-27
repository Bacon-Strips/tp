package seedu.travelr.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.travelr.commons.core.Messages;
import seedu.travelr.model.Model;
import seedu.travelr.model.trip.TitleContainsKeywordsPredicate;

/**
 * Finds and lists all trips in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all trips whose title contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final TitleContainsKeywordsPredicate predicate;

    public FindCommand(TitleContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTripList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_TRIPS_LISTED_OVERVIEW, model.getFilteredTripList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && predicate.equals(((FindCommand) other).predicate)); // state check
    }
}
