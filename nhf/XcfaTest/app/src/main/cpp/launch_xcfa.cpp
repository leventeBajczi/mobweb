#pragma clang diagnostic push
#pragma ide diagnostic ignored "cppcoreguidelines-macro-usage"
#pragma ide diagnostic ignored "cppcoreguidelines-avoid-magic-numbers"
//
// Created by levente on 10/25/19.
//

#include "aarch64/macro-assembler-aarch64.h"
#include "aarch64/simulator-aarch64.h"
#include <jni.h>
#include <map>

using namespace vixl::aarch64;

const XRegister& lut(int i) {
    switch (i) {
        case 0: return x0;
        case 1: return x1;
        case 2: return x2;
        case 3: return x3;
        case 4: return x4;
        case 5: return x5;
        case 6: return x6;
        case 7: return x7;
        case 8: return x8;
        case 9: return x9;
        case 10: return x10;
        default: return x11;
    }
}

std::map<std::string, Label*> labelLut;

class Assembler
{
private:
    MacroAssembler* masm;
    Decoder* decoder;
    Simulator* simulator;
    Label* firstLabel = nullptr;
public:
    Assembler()
    {
        masm = new MacroAssembler();
        decoder = new Decoder();
        simulator = new Simulator(decoder);
    }

    void run()
    {
        masm->FinalizeCode();
        simulator->RunFrom(masm->GetLabelAddress<Instruction *>(firstLabel));
    }

    int64_t getReg(unsigned int i)
    {
        return simulator->ReadXRegister(i);
    }

    void bind(Label* label)
    {
        if(firstLabel == nullptr) firstLabel = label;
        masm->Bind(label);
    }

    MacroAssembler* getMasm()
    {
        return masm;
    }

    ~Assembler()
    {
        delete simulator;
        delete decoder;
        delete masm;
    }

};

Assembler* assembler = nullptr;

#define __ assembler->getMasm()->

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_initVixl(JNIEnv*, jobject) {
    assembler = new Assembler();
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_deinitVixl(JNIEnv*, jobject) {
    delete assembler;
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_addLabel(JNIEnv *env, jobject, jstring label) {
    jboolean isCopy;
    const jchar* chars = env->GetStringChars(label, &isCopy);
    labelLut.insert(std::make_pair(std::string{(const char*)chars}, new Label()));
    if(isCopy) env->ReleaseStringChars(label, chars);
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_bindLabel(JNIEnv *env, jobject, jstring label) {
    jboolean isCopy;
    const jchar* chars = env->GetStringChars(label, &isCopy);
    assembler->bind(labelLut.at(std::string{(const char*)chars}));
    if(isCopy) env->ReleaseStringChars(label, chars);
}

extern "C"
JNIEXPORT jint JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_getRegisterValue(JNIEnv *, jobject, jint i) {
    return static_cast<jint>(assembler->getReg(static_cast<unsigned int>(i)));
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_run(JNIEnv *, jobject) {
    assembler->run();
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
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_mov(JNIEnv *, jobject, jint d,
                                                   jint s) {
    __ Mov(lut(d), lut(s));
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_movLit(JNIEnv *, jobject, jint d,
                                                      jint s) {
    __ Mov(lut(d), static_cast<uint64_t>(s));
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_branch(JNIEnv *env, jobject, jint reg,
                                                      jstring label) {
    jboolean isCopy;
    const jchar* chars = env->GetStringChars(label, &isCopy);
    __ Cbz(lut(reg), labelLut.at(std::string{(const char*)chars}));
    if(isCopy) env->ReleaseStringChars(label, chars);
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_ldr(JNIEnv *env, jobject thiz, jint regnum,
                                                   jint integer) {
    // TODO: implement ldr()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_ldar(JNIEnv *env, jobject thiz, jint regnum,
                                                    jint integer) {
    // TODO: implement ldar()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_ldxr(JNIEnv *env, jobject thiz, jint regnum,
                                                    jint integer) {
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
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_str(JNIEnv *env, jobject thiz, jint integer,
                                                   jint regnum) {
    // TODO: implement str()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_stlr(JNIEnv *env, jobject thiz, jint integer,
                                                    jint regnum) {
    // TODO: implement stlr()
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_stxr(JNIEnv *env, jobject thiz, jint integer,
                                                    jint regnum) {
    // TODO: implement stxr()
}
#pragma clang diagnostic pop