package hu.bme.aut.xcfatest.thetacompat;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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

    public void serialize() {
        Map<VarDecl<?>, Integer> lut = new HashMap<>();
        for(VarDecl<?> v : xcfa.getVars()) {
            lut.put(v, lut.size());
        }
        JniCompat jniCompat = new JniCompat();
        for (XCFA.Process process : xcfa.getProcesses()) {
            for (XCFA.Process.Procedure procedure : process.getProcedures()) {
                XcfaStmtVisitor visitor = new XcfaStmtVisitor(lut, jniCompat);
                for (XCFA.Process.Procedure.Edge edge : procedure.getEdges()) {
                    for(Stmt stmt : edge.getStmts()) {
                        stmt.accept(visitor, edge);
                    }
                }
            }
        }

    }

}
