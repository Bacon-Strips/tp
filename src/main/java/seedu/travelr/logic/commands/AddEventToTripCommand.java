package seedu.travelr.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.travelr.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.travelr.logic.parser.CliSyntax.PREFIX_TRIP;
import static seedu.travelr.model.trip.TripComparators.DO_NOTHING;

import java.util.HashSet;

import seedu.travelr.logic.commands.exceptions.CommandException;
import seedu.travelr.model.Model;
import seedu.travelr.model.component.Description;
import seedu.travelr.model.component.Title;
import seedu.travelr.model.event.Event;
import seedu.travelr.model.trip.Trip;

/**
 * Represents the AddEventToTripCommand. Extends the Command class.
 */
public class AddEventToTripCommand extends Command {

    public static final String COMMAND_WORD = "add-et";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an event to specified trip. "
            + "Parameters: "
            + PREFIX_TITLE + "Event TITLE "
            + PREFIX_TRIP + "TRIP "
            + "\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TITLE + "Swim "
            + PREFIX_TRIP + "Honeymoon ";

    public static final String MESSAGE_SUCCESS = "Event added to trip: %1$s "
            + "\nThe specified event has been removed "
            + "from the bucket list. \nCurrent bucket list:";
    public static final String MESSAGE_DUPLICATE_TRIP = "This event already exists in the specified trip";

    private final Title eventToAdd;
    private final Title tripToAddInto;

    /**
     * Creates an AddCommand to add the specified {@code Trip}
     */
    public AddEventToTripCommand(Title event, Title trip) {
        requireNonNull(event);
        requireNonNull(trip);
        eventToAdd = event;
        tripToAddInto = trip;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasEvent(new Event(eventToAdd))) {
            throw new CommandException("Please enter a valid event");
        }

        if (!model.hasTrip(new Trip(tripToAddInto, new Description("random"), new HashSet<>()))) {
            throw new CommandException("Please enter a valid Trip");
        }

        if (!model.hasEventInBucketList(new Event(eventToAdd))) {
            throw new CommandException("This event is no longer in the bucket list!");
        }

        Event event = model.getEvent(new Event(eventToAdd));
        Trip toAddInto = model.getTrip(new Trip(tripToAddInto, new Description("random"), new HashSet<>()));
        model.removeFromBucketList(event);
        toAddInto.addEvent(event);
        model.updateFilteredEventList(model.getBucketPredicate());
        model.resetSelectedTrip();
        model.sortTripsByComparator(DO_NOTHING);
        return new CommandResult(String.format(MESSAGE_SUCCESS, event));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddEventToTripCommand // instanceof handles nulls
                && eventToAdd.equals(((AddEventToTripCommand) other).eventToAdd)
                && tripToAddInto.equals(((AddEventToTripCommand) other).tripToAddInto));
    }
}
