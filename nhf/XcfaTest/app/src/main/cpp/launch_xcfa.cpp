//
// Created by levente on 10/25/19.
//

#include "aarch64/macro-assembler-aarch64.h"
#include "aarch64/simulator-aarch64.h"

#define __ masm->

using namespace vixl::aarch64;

void GenerateDemoFunction(MacroAssembler *masm) {
  __ Add(x0, x1, x2);
  __ Ret();
}


#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_hu_bme_aut_xcfatest_MainActivity_textFromNative( JNIEnv* env, jobject thiz)
{
    MacroAssembler masm;
    Decoder decoder;
    Simulator simulator(&decoder);

    Label demo_function;
    masm.Bind(&demo_function);
    GenerateDemoFunction(&masm);
    masm.FinalizeCode();

    simulator.WriteXRegister(1, 2);
    simulator.WriteXRegister(2, 3);
    simulator.RunFrom(masm.GetLabelAddress<Instruction *>(&demo_function));

    char str[100];
    sprintf(str, "2+3 = %d", static_cast<int>(simulator.ReadXRegister(0)));
    return env -> NewStringUTF(str);
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_calcBinary(JNIEnv *env, jobject thiz, jint i,
                                                          jint i1, jstring operator_label,
                                                          jint string) {
    // TODO: implement calcBinary()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_calcUnary(JNIEnv *env, jobject thiz, jint depth,
                                                         jstring operator_label, jint string) {
    // TODO: implement calcUnary()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_mov(JNIEnv *env, jobject thiz, jint depth,
                                                   jobject integer) {
    // TODO: implement mov()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_movLit(JNIEnv *env, jobject thiz, jint depth,
                                                      jint i) {
    // TODO: implement movLit()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_jnz(JNIEnv *env, jobject thiz, jstring name) {
    // TODO: implement jnz()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_tst(JNIEnv *env, jobject thiz, jint string) {
    // TODO: implement tst()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_ldr(JNIEnv *env, jobject thiz, jint regnum,
                                                   jobject integer) {
    // TODO: implement ldr()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_ldar(JNIEnv *env, jobject thiz, jint regnum,
                                                    jobject integer) {
    // TODO: implement ldar()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_ldxr(JNIEnv *env, jobject thiz, jint regnum,
                                                    jobject integer) {
    // TODO: implement ldxr()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_strLit(JNIEnv *env, jobject thiz, jint value,
                                                      jint regnum) {
    // TODO: implement strLit()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_stlrLit(JNIEnv *env, jobject thiz, jint value,
                                                       jint regnum) {
    // TODO: implement stlrLit()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_stxrLit(JNIEnv *env, jobject thiz, jint value,
                                                       jint regnum) {
    // TODO: implement stxrLit()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_str(JNIEnv *env, jobject thiz, jobject integer,
                                                   jint regnum) {
    // TODO: implement str()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_stlr(JNIEnv *env, jobject thiz, jobject integer,
                                                    jint regnum) {
    // TODO: implement stlr()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_stxr(JNIEnv *env, jobject thiz, jobject integer,
                                                    jint regnum) {
    // TODO: implement stxr()
}