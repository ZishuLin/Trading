package ca.dal.cs.csci3130.group16.courseproject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class StringChecker {
    //holds a set of mappings between conditions and an error strings
    //if a condition is false, the status will be set to the corresponding string
    private final LinkedHashMap<Predicate<String>, String> rules;

    private final String successMessage;

    private boolean valid;
    private String message;

    public StringChecker(LinkedHashMap<Predicate<String>, String> rules, String successMessage) {
        this.rules = rules;
        this.successMessage = successMessage;
    }

    public void check(String content) {
        //check if the string passes all of the rules, and stop after the first failure.
        //LinkedHashMap, so this will check in the order that rules are added to the map.
        for (Map.Entry<Predicate<String>, String> rule : rules.entrySet()) {

            boolean testPassed = rule.getKey().test(content);
            String failureMessage = rule.getValue();

            if (!testPassed) {
                valid = false;
                message = failureMessage;
                return;
            }

        }
        valid = true;
        message = successMessage;
    }

    public boolean checkedWasValid() {
        return valid;
    }

    public String checkedStatusMessage() {
        return message;
    }

}
