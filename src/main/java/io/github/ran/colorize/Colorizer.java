package io.github.ran.colorize;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import com.sun.jna.Function;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colorizer {
    static void windowsColorizer() {
        Function GetStdHandleFunc = Function.getFunction("kernel32", "GetStdHandle");
        WinDef.DWORD STD_OUTPUT_HANDLE = new WinDef.DWORD(-11);
        WinNT.HANDLE hOut = (WinNT.HANDLE) GetStdHandleFunc.invoke(WinNT.HANDLE.class, new Object[]{STD_OUTPUT_HANDLE});

        WinDef.DWORDByReference p_dwMode = new WinDef.DWORDByReference(new WinDef.DWORD(0));
        Function GetConsoleModeFunc = Function.getFunction("kernel32", "GetConsoleMode");
        GetConsoleModeFunc.invoke(WinDef.BOOL.class, new Object[]{hOut, p_dwMode});

        int ENABLE_VIRTUAL_TERMINAL_PROCESSING = 4;
        WinDef.DWORD dwMode = p_dwMode.getValue();
        dwMode.setValue(dwMode.intValue() | ENABLE_VIRTUAL_TERMINAL_PROCESSING);
        Function SetConsoleModeFunc = Function.getFunction("kernel32", "SetConsoleMode");
        SetConsoleModeFunc.invoke(WinDef.BOOL.class, new Object[]{hOut, dwMode});
    }

    public static String colorize(String mcText) {
        return Colors.colorize(mcText);
    }

    public static boolean containsColorCodes(String mcText) {
        return Pattern.compile("(?i)" + '\u00A7' + "[0-9A-FK-ORX]").matcher(mcText).results().findAny().isPresent();
    }

    private enum Colors {
        BLACK('\u00A7' + "0", Attribute.BLACK_TEXT()),
        DARK_GREEN('\u00A7' + "2", Attribute.GREEN_TEXT()),
        DARK_RED('\u00A7' + "4", Attribute.RED_TEXT()),
        GOLD('\u00A7' + "6", Attribute.YELLOW_TEXT()),
        DARK_GREY('\u00A7' + "8", Attribute.BRIGHT_BLACK_TEXT()),
        GREEN('\u00A7' + "a", Attribute.BRIGHT_GREEN_TEXT()),
        RED('\u00A7' + "c", Attribute.BRIGHT_RED_TEXT()),
        YELLOW('\u00A7' + "e", Attribute.BRIGHT_YELLOW_TEXT()),
        DARK_BLUE('\u00A7' + "1", Attribute.BLUE_TEXT()),
        DARK_AQUA('\u00A7' + "3", Attribute.CYAN_TEXT()),
        DARK_PURPLE('\u00A7' + "5", Attribute.MAGENTA_TEXT()),
        GREY('\u00A7' + "7", Attribute.BRIGHT_BLACK_TEXT()),
        BLUE('\u00A7' + "9", Attribute.BRIGHT_BLUE_TEXT()),
        AQUA('\u00A7' + "b", Attribute.BRIGHT_CYAN_TEXT()),
        LIGHT_PURPLE('\u00A7' + "d", Attribute.MAGENTA_TEXT()),
        WHITE('\u00A7' + "f", Attribute.WHITE_TEXT()),
        STRIKETHROUGH('\u00A7' + "m", Attribute.STRIKETHROUGH()),
        ITALIC('\u00A7' + "o", Attribute.ITALIC()),
        BOLD('\u00A7' + "l", Attribute.BOLD()),
        UNDERLINE('\u00A7' + "n", Attribute.UNDERLINE()),
        RESET('\u00A7' + "r", Attribute.CLEAR()),
        OBFUSCATE('\u00A7' + "k", Attribute.REVERSE()); // I don't know how to do this

        private final String colorCode;
        private final Attribute attribute;

        Colors(String colorCode, Attribute attribute) {
            this.colorCode = colorCode;
            this.attribute = attribute;
        }

        public String getColorCode() {
            return colorCode;
        }

        public Attribute getAttribute() {
            return attribute;
        }
        public static Colors getColor(String colorChar) {
            return switch (colorChar) {
                case '\u00A7' + "0" -> Colors.BLACK;
                case '\u00A7' + "1" -> Colors.DARK_BLUE;
                case '\u00A7' + "2" -> Colors.DARK_GREEN;
                case '\u00A7' + "3" -> Colors.DARK_AQUA;
                case '\u00A7' + "4" -> Colors.DARK_RED;
                case '\u00A7' + "5" -> Colors.DARK_PURPLE;
                case '\u00A7' + "6" -> Colors.GOLD;
                case '\u00A7' + "7" -> Colors.GREY;
                case '\u00A7' + "8" -> Colors.DARK_GREY;
                case '\u00A7' + "9" -> Colors.BLUE;
                case '\u00A7' + "a" -> Colors.GREEN;
                case '\u00A7' + "b" -> Colors.AQUA;
                case '\u00A7' + "c" -> Colors.RED;
                case '\u00A7' + "d" -> Colors.LIGHT_PURPLE;
                case '\u00A7' + "e" -> Colors.YELLOW;
                case '\u00A7' + "f" -> Colors.WHITE;
                case '\u00A7' + "k" -> Colors.OBFUSCATE;
                case '\u00A7' + "l" -> Colors.BOLD;
                case '\u00A7' + "m" -> Colors.STRIKETHROUGH;
                case '\u00A7' + "n" -> Colors.UNDERLINE;
                case '\u00A7' + "o" -> Colors.ITALIC;
                default -> Colors.RESET;
            };
        }

        public static String colorize(String mcText) {
            String[] matches = Pattern.compile("(?i)" + '\u00A7' + "[0-9A-FK-ORX]").matcher(mcText).results().map(MatchResult::group).toArray(String[]::new);
            for (String match : matches) {
                mcText = mcText.replaceAll(Pattern.quote(match), Matcher.quoteReplacement(Ansi.generateCode(Colors.getColor(match).getAttribute())));
            }
            return mcText + Ansi.generateCode(Attribute.CLEAR());
        }
    }
}
