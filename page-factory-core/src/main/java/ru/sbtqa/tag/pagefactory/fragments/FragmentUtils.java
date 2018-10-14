package ru.sbtqa.tag.pagefactory.fragments;

import gherkin.ast.Step;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FragmentUtils {

    private static final String FRAGMENT_STEP_REGEX_KEY = "^(?:пользователь |он |)вставляет фрагмент \\\"([^\\\"]*)\\\"$";

    private FragmentUtils() {}

    /**
     * Find the name of the scenario (fragment) to substitute for this step
     *
     * @param step step to substitute
     * @param language step's language
     * @return name of the scenario (fragment) to substitute
     */
    public static String getFragmentName(Step step, String language) {
        String regex = getFragmentStepRegex(language);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(step.getText());
        matcher.find();
        return matcher.group(1);
    }

    /**
     * Check the step for the need to replace the fragment
     *
     * @param step step to check
     * @param language step's language
     * @return returns true if the step needs to be replaced with a fragment
     */
    public static boolean isStepFragmentRequire(Step step, String language) {
        String regex = getFragmentStepRegex(language);
        return Pattern.matches(regex, step.getText());
    }

    /**
     * Get regex of steps in need of replacement. Language required
     *
     * @param language language of regex
     * @return regex of steps in need of replacement
     */
    private static String getFragmentStepRegex(String language) {
        return FRAGMENT_STEP_REGEX_KEY;
    }
}
