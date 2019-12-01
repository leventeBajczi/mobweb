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

    private XcfaAbstraction(XCFA xcfa) {
        this.xcfa = xcfa;
    }

    public static XcfaAbstraction fromStream(InputStream is) throws IOException {
        XCFA xcfa = XcfaDslManager.createXcfa(is);
        return new XcfaAbstraction(xcfa);
    }

    public static XcfaAbstraction fromString(String s) throws IOException {
        XCFA xcfa = XcfaDslManager.createXcfa(s);
        return new XcfaAbstraction(xcfa);
    }

    public void run() {
        JniCompat jniCompat = new JniCompat();
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
            }
        }
        for(VarDecl<?> var : xcfa.getVars()) {
            jniCompat.addGlobal(lut.get(var));
        }
        jniCompat.run();
    }

}
