package hu.bme.aut.xcfatest.thetacompat.visitor;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import hu.bme.mit.theta.core.decl.VarDecl;
import hu.bme.mit.theta.core.model.MutableValuation;
import hu.bme.mit.theta.core.stmt.AssignStmt;
import hu.bme.mit.theta.core.stmt.AssumeStmt;
import hu.bme.mit.theta.core.stmt.HavocStmt;
import hu.bme.mit.theta.core.stmt.SkipStmt;
import hu.bme.mit.theta.core.stmt.XcfaStmt;
import hu.bme.mit.theta.core.stmt.xcfa.AtomicBeginStmt;
import hu.bme.mit.theta.core.stmt.xcfa.AtomicEndStmt;
import hu.bme.mit.theta.core.stmt.xcfa.LoadStmt;
import hu.bme.mit.theta.core.stmt.xcfa.NotifyAllStmt;
import hu.bme.mit.theta.core.stmt.xcfa.NotifyStmt;
import hu.bme.mit.theta.core.stmt.xcfa.StoreStmt;
import hu.bme.mit.theta.core.stmt.xcfa.WaitStmt;
import hu.bme.mit.theta.core.stmt.xcfa.XcfaCallStmt;
import hu.bme.mit.theta.core.type.BinaryExpr;
import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.MultiaryExpr;
import hu.bme.mit.theta.core.type.Type;
import hu.bme.mit.theta.core.type.UnaryExpr;
import hu.bme.mit.theta.core.type.anytype.RefExpr;
import hu.bme.mit.theta.core.type.inttype.IntLitExpr;

public class XcfaStmtVisitor implements hu.bme.mit.theta.core.stmt.xcfa.XcfaStmtVisitor<Integer, String> {

    private final Map<VarDecl<?>, Integer> lut;
    private final MutableValuation mutableValuation;
    private static XcfaStmtVisitor instance = new XcfaStmtVisitor();
    private XcfaStmtVisitor() {
        lut = new HashMap<>();
        mutableValuation = new MutableValuation();
    }
    public static XcfaStmtVisitor getVisitor() {
        return instance;
    }

    @Override
    public String visit(XcfaCallStmt xcfaCallStmt, Integer integer) {
        System.out.println("Visiting XcfaCallStmt");
        return null;
    }

    @Override
    public String visit(StoreStmt storeStmt, Integer integer) {
        System.out.println("Visiting StoreStmt");
        return null;
    }

    @Override
    public String visit(LoadStmt loadStmt, Integer integer) {
        System.out.println("Visiting LoadStmt");
        return null;
    }

    @Override
    public String visit(AtomicBeginStmt atomicBeginStmt, Integer integer) {
        System.out.println("Visiting AtomicBeginStmt");
        return null;
    }

    @Override
    public String visit(AtomicEndStmt atomicEndStmt, Integer integer) {
        System.out.println("Visiting AtomicEndStmt");
        return null;
    }

    @Override
    public String visit(NotifyAllStmt notifyAllStmt, Integer integer) {
        System.out.println("Visiting NotifyAllStmt");
        return null;
    }

    @Override
    public String visit(NotifyStmt notifyStmt, Integer integer) {
        System.out.println("Visiting NotifyStmt");
        return null;
    }

    @Override
    public String visit(WaitStmt waitStmt, Integer integer) {
        System.out.println("Visiting WaitStmt");
        return null;
    }

    @Override
    public String visit(SkipStmt skipStmt, Integer integer) {
        System.out.println("Visiting SkipStmt");
        return null;
    }

    @Override
    public String visit(AssumeStmt assumeStmt, Integer integer) {
        System.out.println("Visiting AssumeStmt");
        return null;
    }

    @Override
    public <DeclType extends Type> String visit(AssignStmt<DeclType> assignStmt, Integer integer) {
        Expr<DeclType> expr = assignStmt.getExpr();
        VarDecl<?> varDecl = assignStmt.getVarDecl();
        if (isEvaluable(expr)) mutableValuation.put(assignStmt.getVarDecl(), expr.eval(mutableValuation));
        else {
            if(!lut.containsKey(varDecl)) lut.put(varDecl, lut.size());
            //noinspection ConstantConditions
            int regnum = lut.get(varDecl);
            return "#" + regnum + " = " + getString(expr);
        }
        return "";
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private <DeclType extends Type> String getString(Expr<DeclType> expr) {
        if(isEvaluable(expr)) return Integer.toString(((IntLitExpr)expr.eval(mutableValuation)).getValue());
        switch(expr.getArity()) {
            case 0:
                if(!lut.containsKey(((RefExpr) expr).getDecl())) throw new NoSuchElementException();
                return "#" + lut.get(((RefExpr) expr).getDecl());
            case 1:
                return ((UnaryExpr<?, ?>) expr).getOperatorLabel() + " (" + getString(((UnaryExpr<?, ?>) expr).getOp()) + ")";
            default:
                String operatorLabel = (expr instanceof BinaryExpr) ?
                        ((BinaryExpr<?, ?>) expr).getOperatorLabel() :
                        ((MultiaryExpr<?, ?>) expr).getOperatorLabel();
                StringBuilder builder = new StringBuilder();
                for(int i = 0; i < expr.getArity(); ++i) {
                    builder.
                            append("(").
                            append(getString(expr.getOps().get(i))).
                            append(")").
                            append(i == expr.getArity()-1 ? "" : operatorLabel);
                }
                return builder.toString();
        }
    }

    private <DeclType extends Type> boolean isEvaluable(Expr<DeclType> expr) {
        try {
            expr.eval(mutableValuation);
            return true;
        } catch(NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public <DeclType extends Type> String visit(HavocStmt<DeclType> havocStmt, Integer integer) {
        System.err.println("Havoc not implemented!");
        return "";
    }

    @Override
    public String visit(XcfaStmt xcfaStmt, Integer integer) {
        System.out.println("Visiting XcfaStmt");
        return xcfaStmt.accept(this, integer);
    }
}
