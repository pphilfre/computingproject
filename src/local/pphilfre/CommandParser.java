package local.pphilfre;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Parses user input into structured commands.
 */
public class CommandParser {

    // Set of common prepositions used in adventure game commands
    private static final Set<String> PREPOSITIONS = new HashSet<>(Arrays.asList(
        "on", "to", "with", "in", "at", "from", "under", "behind", "inside", "about",
        "for", "through", "by", "into"
    ));
    
    // Set of special conjunctions used to combine objects
    private static final Set<String> CONJUNCTIONS = new HashSet<>(Arrays.asList(
        "and", "using", "then"
    ));

    /**
     * Parses a raw input string into a structured command.
     *
     * @param rawInput The raw input string
     * @return A ParsedCommand object representing the structured command
     */
    public ParsedCommand parse(String rawInput) {
        if (rawInput == null || rawInput.trim().isEmpty()) {
            return new ParsedCommand("", "", "", "");
        }
        
        // Convert to lowercase and split into tokens
        String normalizedInput = rawInput.toLowerCase().trim();
        String[] tokens = normalizedInput.split("\\s+");
        
        if (tokens.length == 0) {
            return new ParsedCommand("", "", "", "");
        }
        
        // First token is always the verb
        String verb = tokens[0];
        
        // Handle single-word commands
        if (tokens.length == 1) {
            return new ParsedCommand(verb, "", "", "");
        }
        
        // Look for prepositions and conjunctions
        int prepositionIndex = -1;
        int conjunctionIndex = -1;
        
        for (int i = 1; i < tokens.length; i++) {
            if (PREPOSITIONS.contains(tokens[i])) {
                prepositionIndex = i;
                break;
            } else if (CONJUNCTIONS.contains(tokens[i])) {
                conjunctionIndex = i;
            }
        }
        
        // Extract direct object, preposition, and indirect object
        String directObject = "";
        String preposition = "";
        String indirectObject = "";
        
        if (prepositionIndex > 1) {
            // There's a direct object and a preposition
            directObject = joinTokens(tokens, 1, prepositionIndex);
            preposition = tokens[prepositionIndex];
            
            if (prepositionIndex < tokens.length - 1) {
                // There's also an indirect object
                indirectObject = joinTokens(tokens, prepositionIndex + 1, tokens.length);
            }
        } else if (prepositionIndex == 1) {
            // There's just a preposition followed by an indirect object
            preposition = tokens[1];
            
            if (tokens.length > 2) {
                indirectObject = joinTokens(tokens, 2, tokens.length);
            }
        } else if (conjunctionIndex > 1) {
            // There's a direct object, conjunction, and another object (treat as multi-part direct object)
            directObject = joinTokens(tokens, 1, tokens.length);
        } else {
            // There's just a direct object
            directObject = joinTokens(tokens, 1, tokens.length);
        }
        
        // Handle special cases of verb+preposition as a compound verb
        if (verb.equals("talk") && preposition.equals("to") && !indirectObject.isEmpty()) {
            return new ParsedCommand("talk", indirectObject, "", "");
        } else if (verb.equals("look") && preposition.equals("at") && !indirectObject.isEmpty()) {
            return new ParsedCommand("examine", indirectObject, "", "");
        }
        
        return new ParsedCommand(verb, directObject, preposition, indirectObject);
    }
    
    /**
     * Joins tokens from start index (inclusive) to end index (exclusive) with spaces.
     *
     * @param tokens The array of tokens
     * @param start The start index (inclusive)
     * @param end The end index (exclusive)
     * @return The joined string
     */
    private String joinTokens(String[] tokens, int start, int end) {
        StringBuilder result = new StringBuilder();
        
        for (int i = start; i < end && i < tokens.length; i++) {
            result.append(tokens[i]);
            
            if (i < end - 1 && i < tokens.length - 1) {
                result.append(" ");
            }
        }
        
        return result.toString();
    }

    /**
     * Represents a parsed command with verb, direct object, preposition, and indirect object.
     */
    public static class ParsedCommand {
        private final String verb;
        private final String directObject;
        private final String preposition;
        private final String indirectObject;
        
        public ParsedCommand(String verb, String directObject, String preposition, String indirectObject) {
            this.verb = verb;
            this.directObject = directObject;
            this.preposition = preposition;
            this.indirectObject = indirectObject;
        }
        
        public String getVerb() {
            return verb;
        }
        
        public String getDirectObject() {
            return directObject;
        }
        
        public String getPreposition() {
            return preposition;
        }
        
        public String getIndirectObject() {
            return indirectObject;
        }
        
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("Command: [").append(verb).append("]");
            
            if (!directObject.isEmpty()) {
                result.append(" Direct Object: [").append(directObject).append("]");
            }
            
            if (!preposition.isEmpty()) {
                result.append(" Preposition: [").append(preposition).append("]");
            }
            
            if (!indirectObject.isEmpty()) {
                result.append(" Indirect Object: [").append(indirectObject).append("]");
            }
            
            return result.toString();
        }
    }
}
