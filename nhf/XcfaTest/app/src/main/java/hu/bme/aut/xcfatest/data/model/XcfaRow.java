package hu.bme.aut.xcfatest.data.model;

import java.util.HashMap;
import java.util.Map;

import hu.bme.aut.xcfatest.thetacompat.XcfaAbstraction;

public class XcfaRow {
    private static final Map<String, XcfaRow> LUT = new HashMap<>();

    private final String name;
    private final boolean ok;
    private final int vars;
    private final int threads;
    private final XcfaAbstraction xcfaAbstraction;

    public XcfaRow(String name, boolean ok, int vars, int threads, XcfaAbstraction xcfaAbstraction) {
        this.name = name;
        this.ok = ok;
        this.vars = vars;
        this.threads = threads;
        this.xcfaAbstraction = xcfaAbstraction;
        if (xcfaAbstraction != null) LUT.put(name, this);
    }

    public static boolean exists(String fileName) {
        return LUT.containsKey(fileName);
    }

    public static XcfaRow get(String fileName) {
        return LUT.get(fileName);
    }

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
