package eisenwave.torrens.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ANSI {
    
    private ANSI() {}
    
    private final static char ESC = '\u001B';
    
    public static final String
        SAVE_CURSOR_POS = ESC + "[s",
        RESTORE_CURSOR_POS = ESC + "[u",
        CLEAR_SCREEN = ESC + "[2J",
        CLEAR_LINE = ESC + "[K";
    
    /**
     * An SGR (Select Graphic Rendition) sequence.
     */
    public static final String
        RESET = sgr(0),
        BOLD = sgr(1),
        FAINT = sgr(2),
        ITALIC = sgr(3),
        UNDERLINE = sgr(4),
        SLOW_BLINK = sgr(5),
        RAPID_BLINK = sgr(6),
        REVERSE_VIDEO = sgr(7),
        CONCEAL = sgr(8),
        STRIKETHROUGH = sgr(9),
        BOLD_OFF = sgr(21),
        BOLD_FAINT_OFF = sgr(22),
        ITALIC_OFF = sgr(23),
        UNDERLINE_OFF = sgr(24),
        BLINK_OFF = sgr(25),
        INVERSE_OFF = sgr(27),
        CONCEAL_OFF = sgr(28),
        STRIKETHROUGH_OFF = sgr(29),
        FG_BLACK = sgr(30),
        FG_RED = sgr(31),
        FG_GREEN = sgr(32),
        FG_YELLOW = sgr(33),
        FG_BLUE = sgr(34),
        FG_PURPLE = sgr(35),
        FG_CYAN = sgr(36),
        FG_WHITE = sgr(37),
        FG_RESET = sgr(39),
        BG_BLACK = sgr(40),
        BG_RED = sgr(41),
        BG_GREEN = sgr(42),
        BG_YELLOW = sgr(43),
        BG_BLUE = sgr(44),
        BG_PURPLE = sgr(45),
        BG_CYAN = sgr(46),
        BG_WHITE = sgr(47),
        BG_RESET = sgr(49),
        FRAMED = sgr(51),
        ENCIRCLED = sgr(52),
        OVERLINED = sgr(53),
        FRAMED_ENCIRCLED_OFF = sgr(54),
        OVERLINED_OFF = sgr(55);
    
    /**
     * Converts an SGR (Select Graphic Rendition) code into an ANSI escape sequence string.
     *
     * @param code the code
     * @return the escape sequence
     */
    @NotNull
    @Contract(pure = true)
    private static String sgr(int code) {
        return ESC + "[" + code + "m";
    }
    
}
