package hu.bme.aut.xcfatest.data.model;

import java.util.HashMap;
import java.util.Map;

import hu.bme.aut.xcfatest.thetacompat.XcfaAbstraction;

/**
 * A class representing a single row in the RecyclerView declared in content_main.xml
 */
public class XcfaRow {
    private static final Map<String, XcfaRow> LUT = new HashMap<>();

    private final String name;
    private final boolean ok;
    private final int vars;
    private final int threads;
    private final XcfaAbstraction xcfaAbstraction;

    /**
     * Creating a row from the data of an XCFA.
     *  @param name    The filename of the XCFA.
     * @param ok      A flag which is true when the XCFA is well-formed (meaning no exception is
     *                thrown when parsed) and false otherwise.
     * @param vars    The number of global variables in the XCFA. Zero, when the XCFA could not be
     *                parsed.
     * @param threads The number of threads in the XCFA. Zero, when the XCFA could not be parsed.
     * @param xcfaAbstraction The XcfaAbstraction object representing the XCFA
     */
    public XcfaRow(String name, boolean ok, int vars, int threads, XcfaAbstraction xcfaAbstraction) {
        this.name = name;
        this.ok = ok;
        this.vars = vars;
        this.threads = threads;
        this.xcfaAbstraction = xcfaAbstraction;
        if (xcfaAbstraction != null) LUT.put(name, this);
    }

    /**
     * Returns if the given filename already has a valid XcfaRow.
     *
     * @param fileName The filename to check against
     * @return true when it exists in the LUT
     */
    public static boolean exists(String fileName) {
        return LUT.containsKey(fileName);
    }

    /**
     * @param fileName The filename of the XCFA
     * @return the XcfaRow for the given XCFA
     */
    public static XcfaRow get(String fileName) {
        return LUT.get(fileName);
    }

    /**
     * Removes the entry from the LUT
     *
     * @param fileToModify The filename for which the old XcfaAbstraction became obsolete
     */
    public static void remove(String fileToModify) {
        LUT.remove(fileToModify);
    }

    public String getName() {
        return name;
    }

    public boolean isOk() {
        return ok;
    }

    public int getVars() {
        return vars;
    }

    public int getThreads() {
        return threads;
    }

    public XcfaAbstraction getAbstraction() {
        return xcfaAbstraction;
    }
}
