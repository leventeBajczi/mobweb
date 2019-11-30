package hu.bme.aut.xcfatest.thetacompat;

public class JniCompat {

    static {
        System.loadLibrary("xcfa");
    }

    public JniCompat(){
        deinitVixl();
        initVixl();
    }

    public native int getRegisterValue(int i);

    public native void addLabel(String label);

    public native void bindLabel(String label);

    public native void run();

    private native void initVixl();

    private native void deinitVixl();

    public native void calcBinary(int i, int i1, String operatorLabel, int string) ;

    public native void calcUnary(int depth, String operatorLabel, int string) ;

    public native void mov(int depth, int integer) ;

    public native void movLit(int depth, int i) ;

    public native void branch(int reg, String name) ;

    public native void ldr(int regnum, int integer) ;

    public native void ldar(int regnum, int integer) ;

    public native void ldxr(int regnum, int integer) ;

    public native void strLit(int value, int regnum) ;

    public native void stlrLit(int value, int regnum) ;

    public native void stxrLit(int value, int regnum) ;

    public native void str(int integer, int regnum) ;

    public native void stlr(int integer, int regnum) ;

    public native void stxr(int integer, int regnum) ;
}
