package techreborn.util;

/**
 * @author TeamC4
 */

public enum Color {
    BLACK("\u00A70"), //
    DARK_BLUE("\u00A71"), //
    DARK_GREEN("\u00A72"), //
    DARK_AQUA("\u00A73"), //
    DARK_RED("\u00A74"), //
    DARK_PURPLE("\u00A75"), //
    GOLD("\u00A76"), //
    GRAY("\u00A77"), //
    DARK_GRAY("\u00A78"), //
    BLUE("\u00A79"), //
    GREEN("\u00A7a"), //
    AQUA("\u00A7b"), //
    RED("\u00A7c"), //
    LIGHT_PURPLE("\u00A7d"), //
    YELLOW("\u00A7e"), //
    WHITE("\u00A7f"), //

    STRIKE_THROUGH("\u00A7m"), //
    UNDERLINE("\u00A7n"), //
    BOLD("\u00A7l"), //
    RANDOM("\u00A7k"), //
    ITALIC("\u00A7o"), //
    RESET("\u00A7r"); //

    public String code = "";

    private Color(String code) {

        this.code = code;
    }

    @Override
    public String toString() {

        return code;
    }
}
