package org.gogoup.utilities.misc;

import java.util.regex.Pattern;

public class AttributeFormat {
    
    private String attributeName;
    private int minLength;
    private int maxLength;
    private Pattern pattern;
    private String patternDescription;
    
    public AttributeFormat(String attributeName, int minLength, int maxLength,
            String regex, String patternDescription) {
        this(attributeName, minLength, maxLength, Pattern.compile(regex), patternDescription);
    }
    
    public AttributeFormat(String attributeName, int minLength, int maxLength,
            Pattern pattern, String patternDescription) {
        this.attributeName = attributeName;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.pattern = pattern;
        this.patternDescription = patternDescription;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getPatternDescription() {
        return patternDescription;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public Pattern getPattern() {
        return pattern;
    }
    
    public void validate(String attribute) {
        int length = attribute.trim().length();
        if (null == attribute || 0 == length) {
            throw new NullPointerException(attributeName + " cannot be null or empty.");
        }
        if (length < minLength
                || length > maxLength) {
            String message = "Length of " + attributeName + " need to be greater than" + minLength
                    + " and less " + maxLength + " characters";
            throw new IllegalAttributeLengthException(attributeName,
                    attribute, minLength, maxLength, message);
        }
        if (!pattern.matcher(attribute).matches()) {
            throw new IllegalAttributeFormatException(attributeName, attribute, patternDescription);
        }
    }
    /*
    public AttributeFormat createEmailSpecification() {
        return new AttributeFormat(
                6, 255, Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"));
    }*/
    
}
