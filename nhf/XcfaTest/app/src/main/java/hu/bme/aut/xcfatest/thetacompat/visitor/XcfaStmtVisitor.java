package hu.bme.aut.xcfatest.thetacompat.visitor;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import hu.bme.aut.xcfatest.thetacompat.JniCompat;
import hu.bme.mit.theta.core.decl.Decls;
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
import hu.bme.mit.theta.core.type.inttype.IntType;
import hu.bme.mit.theta.xcfa.XCFA;

public class XcfaStmtVisitor implements hu.bme.mit.theta.core.stmt.xcfa.XcfaStmtVisitor<XCFA.Process.Procedure.Edge, String> {

    private final JniCompat jniCompat;
    private final Map<VarDecl<?>, Integer> lut;
    private final MutableValuation mutableValuation;
    private static XcfaStmtVisitor instance = new XcfaStmtVisitor();
    private XcfaStmtVisitor() {
        jniCompat = new JniCompat();
        lut = new HashMap<>();
        mutableValuation = new MutableValuation();
    }
    public static XcfaStmtVisitor getVisitor() {
        return instance;
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
        StringBuilder stringBuilder = new StringBuilder();
        switch(storeStmt.getOrdering()) {
            case "relaxed": stringBuilder.append("STR "); break;
            case "acq_rel":
            case "release": stringBuilder.append("STLR "); break;
            case "seq_cst": stringBuilder.append("STXR "); break;
        }
        Optional<? extends LitExpr<?>> opt = mutableValuation.eval(storeStmt.getLhs());
        if(!opt.isPresent() && lut.get(storeStmt.getLhs()) == null) throw new NoSuchElementException();
        jniCompat.add(stringBuilder.
                    append(opt.isPresent() ?
                            ((IntLitExpr)opt.get()).getValue() :
                            " #" + (lut.get(storeStmt.getLhs()))).
                    append(", #").
                    append(regnum).
                toString());
        return "";
    }

    @Override
    public String visit(LoadStmt loadStmt, XCFA.Process.Procedure.Edge edge) {
        mutableValuation.remove(loadStmt.getLhs());
        if(!lut.containsKey(loadStmt.getLhs())) lut.put(loadStmt.getLhs(), lut.size());
        //noinspection ConstantConditions
        int regnum = lut.get(loadStmt.getLhs());
        StringBuilder stringBuilder = new StringBuilder();
        switch(loadStmt.getOrdering()) {
            case "relaxed": stringBuilder.append("LDR #"); break;
            case "acq_rel":
            case "acquire": stringBuilder.append("LDAR #"); break;
            case "seq_cst": stringBuilder.append("LDXR #"); break;
        }
        jniCompat.add(stringBuilder.
                append(regnum).
                append(", #").
                append(lut.get(loadStmt.getRhs())).
                toString());
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
        lut.put(Decls.Var("_" + lut.size(), IntType.getInstance()), lut.size());
        jniCompat.add("MOV #" + (lut.size() - 1) + ", " + getString(assumeStmt.getCond()));
        jniCompat.add("JNZ " + edge.getTarget().getName());
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
            return "MOV #" + regnum + ", " + getString(expr);
        }
        return "";
    }

    private <DeclType extends Type> String getString(Expr<DeclType> expr) {
        return getString(expr, 1);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private <DeclType extends Type> String getString(Expr<DeclType> expr, int depth) {
        if(isEvaluable(expr)){
            LitExpr<?> litExpr = expr.eval(mutableValuation);
            if(litExpr instanceof  IntLitExpr) {
                jniCompat.add("MOV #" + depth + ", " + ((IntLitExpr) litExpr).getValue());
            }
            else if(litExpr instanceof BoolLitExpr){
                jniCompat.add("MOV #" + depth + ", " + (((BoolLitExpr)litExpr).getValue() ? 1 : 0));
            }
            return "#" + depth;
        }
        switch(expr.getArity()) {
            case 0:
                if(!lut.containsKey(((RefExpr) expr).getDecl())) throw new NoSuchElementException();
                jniCompat.add("MOV #" + depth + ", #" + lut.get(((RefExpr) expr).getDecl()));
                return "#" + depth;
            case 1:
                jniCompat.add("MOV #" + depth + ", " + ((UnaryExpr<?, ?>) expr).getOperatorLabel() + " " + getString(((UnaryExpr<?, ?>) expr).getOp(), depth+1));
                return "#" + depth;
            default:
                String operatorLabel = (expr instanceof BinaryExpr) ?
                        ((BinaryExpr<?, ?>) expr).getOperatorLabel() :
                        ((MultiaryExpr<?, ?>) expr).getOperatorLabel();
                StringBuilder builder = new StringBuilder();
                builder.append("MOV #").append(depth).append(",");
                for(int i = 0; i < expr.getArity(); ++i) {
                    builder.
                            append(" ").
                            append(getString(expr.getOps().get(i), depth+1+i)).
                            append(" ").
                            append(i == expr.getArity()-1 ? "" : operatorLabel);
                }
                jniCompat.add(builder.toString());
                return "#" + depth;
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
