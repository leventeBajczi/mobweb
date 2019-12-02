package hu.bme.aut.xcfatest.thetacompat;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.bme.aut.xcfatest.thetacompat.visitor.XcfaStmtVisitor;
import hu.bme.mit.theta.core.decl.VarDecl;
import hu.bme.mit.theta.core.stmt.Stmt;
import hu.bme.mit.theta.xcfa.XCFA;
import hu.bme.mit.theta.xcfa.dsl.XcfaDslManager;

public class XcfaAbstraction {
    private final XCFA xcfa;
    private final JniCompat jniCompat;
    private final Map<XCFA.Process.Procedure, Map<VarDecl<?>, Integer>> regs = new HashMap<>();


    private XcfaAbstraction(XCFA xcfa) {
        this.xcfa = xcfa;
        jniCompat = new JniCompat();
        Map<VarDecl<?>, Integer> lut = new HashMap<>();
        for(VarDecl<?> v : xcfa.getVars()) {
            lut.put(v, lut.size());
        }
        for (XCFA.Process process : xcfa.getProcesses()) {
            jniCompat.newProcess();
            for (XCFA.Process.Procedure procedure : process.getProcedures()) {
                for(XCFA.Process.Procedure.Location loc : procedure.getLocs()) {
                    jniCompat.addLabel(loc.getName());
                }
                XcfaStmtVisitor visitor = new XcfaStmtVisitor(lut, jniCompat);
                List<XCFA.Process.Procedure.Edge> edges = procedure.getEdges();
                for (int i = 0; i < edges.size(); i++) {
                    XCFA.Process.Procedure.Edge edge = edges.get(i);
                    jniCompat.bindLabel(edge.getSource().getName());
                    boolean branched = false;
                    for (Stmt stmt : edge.getStmts()) {
                        if (stmt.accept(visitor, edge)) branched = true;
                    }
                    if (!branched && (i == edges.size()-1 || edge.getTarget() != edges.get(i+1).getSource())) {
                        jniCompat.movLit(visitor.getLut().size(), 0);
                        jniCompat.branch(visitor.getLut().size(), edge.getTarget().getName());
                    }
                }
                jniCompat.bindLabel(procedure.getFinalLoc().getName());
                jniCompat.ret();
                regs.put(procedure, visitor.getLut());
            }
        }
        for(VarDecl<?> var : xcfa.getVars()) {
            jniCompat.addGlobal(lut.get(var));
        }
    }

    public static XcfaAbstraction fromStream(InputStream is) throws IOException {
        XCFA xcfa = XcfaDslManager.createXcfa(is);
        return new XcfaAbstraction(xcfa);
    }

    public static XcfaAbstraction fromString(String s) throws IOException {
        XCFA xcfa = XcfaDslManager.createXcfa(s);
        return new XcfaAbstraction(xcfa);
    }

    public Map<XCFA.Process.Procedure, Map<String, Integer>> run() {
        Map<XCFA.Process.Procedure, Map<String, Integer>> values = new HashMap<>();
        jniCompat.run();
        for(XCFA.Process proc : xcfa.getProcesses()) {
            for(XCFA.Process.Procedure procedure : proc.getProcedures()) {
                Map<VarDecl<?>, Integer> procRegs = regs.get(procedure);
                Map<String, Integer> procValues = new HashMap<>();
                procRegs.forEach((varDecl, integer) -> {
                    if(!xcfa.getVars().contains(varDecl)) procValues.put(varDecl.getName(), jniCompat.getRegisterValue(integer));
                });
                values.put(procedure, procValues);
            }
        }
        return values;
    }

    /**
     * @return The number of global variables
     */
    public int getVars() {
        return xcfa.getVars().size();
    }

    /**
     * @return The number of threads
     */
    public int getThreads() {
        return xcfa.getProcesses().size();
    }
}
