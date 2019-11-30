package hu.bme.aut.xcfatest.thetacompat.visitor;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import hu.bme.aut.xcfatest.thetacompat.JniCompat;
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
import hu.bme.mit.theta.core.type.LitExpr;
import hu.bme.mit.theta.core.type.MultiaryExpr;
import hu.bme.mit.theta.core.type.Type;
import hu.bme.mit.theta.core.type.UnaryExpr;
import hu.bme.mit.theta.core.type.anytype.RefExpr;
import hu.bme.mit.theta.core.type.booltype.BoolLitExpr;
import hu.bme.mit.theta.core.type.inttype.IntExprs;
import hu.bme.mit.theta.core.type.inttype.IntLitExpr;
import hu.bme.mit.theta.xcfa.XCFA;

public class XcfaStmtVisitor implements hu.bme.mit.theta.core.stmt.xcfa.XcfaStmtVisitor<XCFA.Process.Procedure.Edge, String> {

    private final JniCompat jniCompat;
    private final Map<VarDecl<?>, Integer> lut;
    private final MutableValuation mutableValuation;

    public XcfaStmtVisitor(Map<VarDecl<?>, Integer> lut, JniCompat jniCompat) {
        this.jniCompat = jniCompat;
        this.lut = new HashMap<>(lut);
        mutableValuation = new MutableValuation();
    }

    @Override
    public String visit(XcfaCallStmt xcfaCallStmt, XCFA.Process.Procedure.Edge edge) {
        System.err.println("XcfaCallStmt not implemented!");
        return "";
    }

    @Override
    public String visit(StoreStmt storeStmt, XCFA.Process.Procedure.Edge edge) {
        if(!lut.containsKey(storeStmt.getRhs())) lut.put(storeStmt.getRhs(), lut.size());
        //noinspection ConstantConditions
        int regnum = lut.get(storeStmt.getRhs());
        Optional<? extends LitExpr<?>> opt = mutableValuation.eval(storeStmt.getLhs());
        if(!opt.isPresent() && lut.get(storeStmt.getLhs()) == null) throw new NoSuchElementException();
        if(opt.isPresent()) {
            switch(storeStmt.getOrdering()) {
                case "relaxed": jniCompat.strLit(((IntLitExpr)opt.get()).getValue(), regnum); break;
                case "acq_rel":
                case "release": jniCompat.stlrLit(((IntLitExpr)opt.get()).getValue(), regnum); break;
                case "seq_cst": jniCompat.stxrLit(((IntLitExpr)opt.get()).getValue(), regnum); break;
            }
        }
        else {
            switch(storeStmt.getOrdering()) {
                case "relaxed": jniCompat.str(lut.get(storeStmt.getLhs()), regnum); break;
                case "acq_rel":
                case "release": jniCompat.stlr(lut.get(storeStmt.getLhs()), regnum); break;
                case "seq_cst": jniCompat.stxr(lut.get(storeStmt.getLhs()), regnum); break;
            }
        }

        return "";
    }

    @Override
    public String visit(LoadStmt loadStmt, XCFA.Process.Procedure.Edge edge) {
        mutableValuation.remove(loadStmt.getLhs());
        if(!lut.containsKey(loadStmt.getLhs())) lut.put(loadStmt.getLhs(), lut.size());
        //noinspection ConstantConditions
        int regnum = lut.get(loadStmt.getLhs());
        switch(loadStmt.getOrdering()) {
            case "relaxed": jniCompat.ldr(regnum, lut.get(loadStmt.getRhs())); break;
            case "acq_rel":
            case "acquire": jniCompat.ldar(regnum, lut.get(loadStmt.getRhs())); break;
            case "seq_cst": jniCompat.ldxr(regnum, lut.get(loadStmt.getRhs())); break;
        }
        return "";
    }

    @Override
    public String visit(AtomicBeginStmt atomicBeginStmt, XCFA.Process.Procedure.Edge edge) {
        System.err.println("AtomicBeginStmt not implemented!");
        return "";
    }

    @Override
    public String visit(AtomicEndStmt atomicEndStmt, XCFA.Process.Procedure.Edge edge) {
        System.err.println("AtomicEndStmt not implemented!");
        return "";
    }

    @Override
    public String visit(NotifyAllStmt notifyAllStmt, XCFA.Process.Procedure.Edge edge) {
        System.err.println("NotifyAllStmt not implemented!");
        return "";
    }

    @Override
    public String visit(NotifyStmt notifyStmt, XCFA.Process.Procedure.Edge edge) {
        System.err.println("NotifyStmt not implemented!");
        return "";
    }

    @Override
    public String visit(WaitStmt waitStmt, XCFA.Process.Procedure.Edge edge) {
        System.err.println("WaitStmt not implemented!");
        return "";
    }

    @Override
    public String visit(SkipStmt skipStmt, XCFA.Process.Procedure.Edge edge) {
        return "";
    }

    @Override
    public String visit(AssumeStmt assumeStmt, XCFA.Process.Procedure.Edge edge) {
        jniCompat.branch(getString(assumeStmt.getCond()), edge.getTarget().getName());
        return "";
    }

    @Override
    public <DeclType extends Type> String visit(AssignStmt<DeclType> assignStmt, XCFA.Process.Procedure.Edge edge) {
        Expr<DeclType> expr = assignStmt.getExpr();
        VarDecl<?> varDecl = assignStmt.getVarDecl();
        if (isEvaluable(expr)) mutableValuation.put(assignStmt.getVarDecl(), expr.eval(mutableValuation));
        else {
            if(!lut.containsKey(varDecl)) lut.put(varDecl, lut.size());
            //noinspection ConstantConditions
            int regnum = lut.get(varDecl);
            jniCompat.mov(regnum,
                    getString(expr));
        }
        return "";
    }

    private <DeclType extends Type> int getString(Expr<DeclType> expr) {
        return getString(expr, lut.size());
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private <DeclType extends Type> int getString(Expr<DeclType> expr, int depth) {
        if(isEvaluable(expr)){
            LitExpr<?> litExpr = expr.eval(mutableValuation);
            if(litExpr instanceof  IntLitExpr) {
                jniCompat.movLit(depth,
                        ((IntLitExpr) litExpr).getValue());
            }
            else if(litExpr instanceof BoolLitExpr){
                jniCompat.movLit(depth,
                        (((BoolLitExpr)litExpr).getValue() ? 1 : 0));
            }
        }
        else {
            switch (expr.getArity()) {
                case 0:
                    if (!lut.containsKey(((RefExpr) expr).getDecl()))
                        throw new NoSuchElementException();
                    jniCompat.mov(depth,
                            lut.get(((RefExpr) expr).getDecl()));
                    break;
                case 1:
                    jniCompat.calcUnary(depth,
                            ((UnaryExpr<?, ?>) expr).getOperatorLabel(),
                            getString(((UnaryExpr<?, ?>) expr).getOp(), depth + 1));
                    break;
                default:
                    String operatorLabel = (expr instanceof BinaryExpr) ?
                            ((BinaryExpr<?, ?>) expr).getOperatorLabel() :
                            ((MultiaryExpr<?, ?>) expr).getOperatorLabel();
                    jniCompat.calcBinary(depth + expr.getArity() - 2,
                            getString(expr.getOps().get(0), depth + 1),
                            operatorLabel,
                            getString(expr.getOps().get(1), depth + 2));
                    for (int i = 2; i < expr.getArity() - 1; ++i) {
                        jniCompat.calcBinary(expr.getArity() - 2 + depth - i,
                                expr.getArity() - 1 + depth - i,
                                operatorLabel,
                                getString(expr.getOps().get(i), depth + 1 + i));
                    }
                    break;
            }
        }
        return depth;
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
    public <DeclType extends Type> String visit(HavocStmt<DeclType> havocStmt, XCFA.Process.Procedure.Edge edge) {
        System.err.println("Havoc not implemented! Using constant 0.");
        mutableValuation.put(havocStmt.getVarDecl(), IntExprs.Int(0));
        return "";
    }

    @Override
    public String visit(XcfaStmt xcfaStmt, XCFA.Process.Procedure.Edge edge) {
        return xcfaStmt.accept(this, edge);
    }
}
