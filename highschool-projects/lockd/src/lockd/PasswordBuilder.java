package lockd;

import java.util.*;

public class PasswordBuilder {
    // we keep our data in lists. Arrays would suffice as data never changes though.
    private final static List<Character> LOWER_CAPS, UPPER_CAPS, DIGITS, SPECIALS;
    // stores all templates
    private List <Template> templateList = new ArrayList<Template>();
    // indicates if we should shuffle the password
    private boolean doShuffle;

    /**
     * @purpose: Factory builder to create our instance.
     * @returns: New PasswordBuilder instance.
     */
    public static PasswordBuilder builder() {
        return new PasswordBuilder();
    }

    /**
     * @purpose: Adds lowercase letters to instance
     * @param: int count: Contains the number of lowercase letters to add.
     * @returns: This instance.
     */
    public PasswordBuilder lowercase(int count) {
        templateList.add(new Template(LOWER_CAPS, count));
        return this;
    }

    /**
     * @purpose: Adds uppercase letters to instance
     * @param: int count: Contains the number of uppercase letters to add.
     * @returns: This instance.
     */
    public PasswordBuilder uppercase(int count) {
        templateList.add(new Template(UPPER_CAPS, count));
        return this;
    }

    /**
     * @purpose: Adds digits to instance
     * @param: int count:  the number of digits to add.
     * @returns: This instance.
     */
    public PasswordBuilder digits(int count) {
        templateList.add(new Template(DIGITS, count));
        return this;
    }

    /**
     * @purpose: Adds special characters to instance
     * @param: int count: Contains the number of special characters to add.
     * @returns: This instance.
     */
    public PasswordBuilder specials(int count) {
        templateList.add(new Template(SPECIALS, count));
        return this;
    }

    /**
     * @purpose: Indicates that the password will be shuffled once
     *           it's been generated.
     * @returns: This instance.
     */
    public PasswordBuilder shuffle() {
        doShuffle = true;
        return this;
    }

    /**
     * @purpose: Builds the password.
     * @returns: The password as a String.
     */
    public String build() {
        StringBuilder passwordBuilder = new StringBuilder();
        List<Character> characters = new ArrayList<Character>();
        for (Template template : templateList) {
            characters.addAll(template.take());
        }
        if (doShuffle)
            Collections.shuffle(characters);
        for (char chr : characters) {
            passwordBuilder.append(chr);
        }
        return passwordBuilder.toString();
    }

    // initialize statics
    static {
        LOWER_CAPS = new ArrayList<Character>(26);
        UPPER_CAPS = new ArrayList<Character>(26);
        for (int i = 0; i < 26; i++) {
            LOWER_CAPS.add((char) (i + 'a'));
            UPPER_CAPS.add((char) (i + 'A'));
        }
        DIGITS = new ArrayList<Character>(10);
        for (int i = 0; i < 10; i++) {
            DIGITS.add((char) (i + '0'));
        }
        SPECIALS = new ArrayList<Character>() {{
            add('!');
            add('@');
            add('#');
            add('$');
            add('%');
            add('^');
            add('&');
            add('(');
            add(')');
            add('*');
            add('+');
        }};
    }
}