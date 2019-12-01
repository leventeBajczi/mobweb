package hu.bme.aut.xcfatest.thetacompat;

import java.util.HashMap;
import java.util.Map;

import hu.bme.mit.theta.core.decl.VarDecl;

public class JniCompat {

    static {
        System.loadLibrary("xcfa");
    }

    public JniCompat(){
        initVixl();
    }

    public native int getRegisterValue(int i);

    public native void addLabel(String label);

    public native void bindLabel(String label);

    public native void addGlobal(int reg);

    public native void run();

    private native void initVixl();

    public native void newProcess();

    public native void calcBinary(int d, int s1, String operatorLabel, int s2) ;

    public native void calcUnary(int d, String operatorLabel, int s) ;

    public native void mov(int d, int s) ;

    public native void movLit(int d, int val) ;

    public native void branch(int reg, String label) ;

    public native void ldr(int d, int s) ;

    public native void ldar(int d, int s) ;

    public native void ldxr(int d, int s) ;

    public native void str(int s, int d) ;

    public native void stlr(int s, int d) ;

    public native void stxr(int s, int d) ;

    public native void ret();
}
