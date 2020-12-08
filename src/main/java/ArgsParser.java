public class ArgsParser {
    public static boolean parseIsObfMode(String arg) {
        final String mode = arg.replace("-mode=", "");
        return !mode.equals("unobf");
    }

    public static String parseFilename(String arg) {
        return arg.replace("-file=", "").replaceAll("\"", "");
    }
}
