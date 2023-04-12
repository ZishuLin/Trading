package ca.dal.cs.csci3130.group16.courseproject;

import java.util.LinkedHashMap;
import java.util.function.Predicate;

public class StringCheckerBuilder {

    final String successMessage;
    private final LinkedHashMap<Predicate<String>, String> rules;

    public StringCheckerBuilder(String successMessage) {
        this.successMessage = successMessage;
        this.rules = new LinkedHashMap<>();
    }

    //tells the builder to add a rule when building the Checker.
    //rules are checked in the order they are added.
    public StringCheckerBuilder withRule(Predicate<String> test, String failureMessage) {
        rules.put(test, failureMessage);
        return this;
    }

    public StringCheckerBuilder withNotEmptyRule(String failureMessage) {
        Predicate<String> notEmpty = input -> !input.isEmpty();
        rules.put(notEmpty, failureMessage);
        return this;
    }

    public StringChecker build() {
        return new StringChecker(rules, successMessage);
    }
}
