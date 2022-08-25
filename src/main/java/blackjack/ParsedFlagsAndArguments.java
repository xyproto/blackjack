package blackjack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedFlagsAndArguments {

    private final ArrayList<String> args; // non-flag arguments
    private final HashSet<String> flags; // flag arguments (starting with "-")

    /**
     * Parse the arguments and flags in the given command line argument string list. This
     * constructor does not deal with unrecognized flags.
     *
     * @param args can be the same args as the main function is given
     */
    ParsedFlagsAndArguments(final String[] args) {
        ArrayList<String> parsedArgs = new ArrayList<String>();
        HashSet<String> parsedFlags = new HashSet<String>();
        for (final String arg : args) {
            if (arg.startsWith("-")) {
                parsedFlags.add(arg);
            } else {
                parsedArgs.add(arg);
            }
        }
        this.args = parsedArgs;
        this.flags = parsedFlags;
    }

    /**
     * Parse the command line arguments and check that all given flags are in the usage text.
     *
     * @param args is the command line flags
     * @param usageText is the full usage text that contains all valid flags as words
     * @throws UnrecognizedFlagException if one of the given flags are not recognized
     */
    ParsedFlagsAndArguments(final String[] args, final String usageText)
            throws UnrecognizedFlagException {
        this(args);
        Optional<String> maybeUnrecognizedFlag = findAnUnrecognizedFlag(usageText);
        if (maybeUnrecognizedFlag.isPresent()) {
            throw new UnrecognizedFlagException(
                    "unrecognized flag: " + maybeUnrecognizedFlag.get());
        }
    }

    /**
     * Look for an unrecognized flag in the usage text
     *
     * @param usageText is the help text that contains a description of all flags
     * @return an Optional String if an unrecognized flag was found
     */
    public final Optional<String> findAnUnrecognizedFlag(final String usageText) {
        ArrayList<String> recognizedFlags = allRecognizedFlags(usageText);
        // Check if one of the parsed flags are unrecognized
        for (String flag : flags) {
            if (!recognizedFlags.contains(flag)) {
                return Optional.of(flag);
            }
        }
        // All parsed flags were recognized
        return Optional.empty();
    }

    /**
     * Find a list of all recognized flags, by looking for -+[\w]+ in the usage text.
     *
     * @param usageText can be the same help text that is presented to the user when the --help flag
     *     is given
     * @return a list of all recognized flags, based on the contents of the given usage string
     */
    private final ArrayList<String> allRecognizedFlags(final String usageText) {
        // Look for any word starting with "-" in the given usage text
        Pattern pattern = Pattern.compile("-+[\\w]+");
        Matcher matcher = pattern.matcher(usageText);
        ArrayList<String> recognizedFlags = new ArrayList<String>();
        while (matcher.find()) {
            recognizedFlags.add(matcher.group());
        }
        return recognizedFlags;
    }

    /**
     * Check if a flag was given.
     *
     * @param flag is the name of the flag, including leading dashes.
     * @return true if the flag exists
     */
    public final boolean hasFlag(final String flag) {
        return flags.contains(flag);
    }

    /**
     * Check if one of the two given flags were passed to the application.
     *
     * @param shortFlag is the name of the flag, including a leading dash.
     * @param longFlag is the name of the flag, including leading dashes.
     * @return true if the flag exists
     */
    public final boolean hasFlags(final String shortFlag, final String longFlag) {
        return flags.contains(shortFlag) || flags.contains(longFlag);
    }

    /**
     * Get the first argument, if it has been given.
     *
     * @return the first non-flag argument as an optional string
     */
    public final Optional<String> firstArg() {
        if (args.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(args.get(0));
    }

    /**
     * Get all given non-flag arguments.
     *
     * @return an ArrayList containing the arguments
     */
    public final ArrayList<String> args() {
        return args;
    }

    /**
     * Get a count of all non-flag arguments.
     *
     * @return the count
     */
    public int count() {
        return args.size();
    }
}
