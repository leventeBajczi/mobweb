package hu.bme.aut.xcfatest.thetacompat;

import java.io.IOException;
import java.io.InputStream;

import hu.bme.aut.xcfatest.thetacompat.visitor.XcfaStmtVisitor;
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
        for (XCFA.Process process : xcfa.getProcesses()) {
            for (XCFA.Process.Procedure procedure : process.getProcedures()) {
                for (XCFA.Process.Procedure.Edge edge : procedure.getEdges()) {
                    for(Stmt stmt : edge.getStmts()) {
                        stmt.accept(XcfaStmtVisitor.getVisitor(), 0);
                    }
                }
            }
        }

    }

}
