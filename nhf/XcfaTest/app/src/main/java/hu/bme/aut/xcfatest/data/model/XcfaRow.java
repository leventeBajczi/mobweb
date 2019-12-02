package hu.bme.aut.xcfatest.data.model;

/**
 * A class representing a single row in the RecyclerView declared in content_main.xml
 */
public class XcfaRow {
    private final String name;
    private final boolean ok;
    private final int vars;
    private final int threads;

    /**
     * Creating a row from the data of an XCFA.
     *
     * @param name    The filename of the XCFA.
     * @param ok      A flag which is true when the XCFA is well-formed (meaning no exception is
     *                thrown when parsed) and false otherwise.
     * @param vars    The number of global variables in the XCFA. Zero, when the XCFA could not be
     *                parsed.
     * @param threads The number of threads in the XCFA. Zero, when the XCFA could not be parsed.
     */
    public XcfaRow(String name, boolean ok, int vars, int threads) {
        this.name = name;
        this.ok = ok;
        this.vars = vars;
        this.threads = threads;
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
}
