package hu.bme.aut.xcfatest.thetacompat;

public class JniCompat {

    static {
        System.loadLibrary("xcfa");
    }

    public native void calcBinary(int i, int i1, String operatorLabel, int string) ;

    public native void calcUnary(int depth, String operatorLabel, int string) ;

    public native void mov(int depth, Integer integer) ;

    public native void movLit(int depth, int i) ;

    public native void jnz(String name) ;

    public native void tst(int string) ;

    public native void ldr(int regnum, Integer integer) ;

    public native void ldar(int regnum, Integer integer) ;

    public native void ldxr(int regnum, Integer integer) ;

    public native void strLit(int value, int regnum) ;

    public native void stlrLit(int value, int regnum) ;

    public native void stxrLit(int value, int regnum) ;

    public native void str(Integer integer, int regnum) ;

    public native void stlr(Integer integer, int regnum) ;

    public native void stxr(Integer integer, int regnum) ;
}
