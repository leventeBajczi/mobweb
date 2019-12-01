#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wfor-loop-analysis"
#pragma ide diagnostic ignored "cppcoreguidelines-macro-usage"
#pragma ide diagnostic ignored "cppcoreguidelines-avoid-magic-numbers"
//
// Created by levente on 10/25/19.
//

#include <jni.h>
#include <android/log.h>
#include <thread>
#include <mutex>
#include <condition_variable>
#include "process.h"

class PointerContainer
{
private:
    std::list<uint64_t*> pointers;
public:
    void addPointer(uint64_t* ptr) { pointers.push_back(ptr); }
    ~PointerContainer()
    {
        for(uint64_t* ptr : pointers)
            delete ptr;
    }
};

const XRegister& xLut(int i) {
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

std::list<Process> processes;
PointerContainer* pointerContainer = nullptr;
Process* currentProcess = nullptr;

#define __ currentProcess->getMasm()->

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_initVixl(JNIEnv*, jobject) {
    processes.clear();
    delete pointerContainer;
    pointerContainer = new PointerContainer{};
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_newProcess(JNIEnv *, jobject) {
    processes.emplace_back();
    currentProcess = &processes.back();
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_addLabel(JNIEnv *env, jobject, jstring label) {
    jboolean isCopy;
    const char* chars = env->GetStringUTFChars(label, &isCopy);
    currentProcess->addLabel(chars);
    if(isCopy) env->ReleaseStringUTFChars(label, chars);
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_bindLabel(JNIEnv *env, jobject, jstring label) {
    jboolean isCopy;
    const char* chars = env->GetStringUTFChars(label, &isCopy);
    currentProcess->bindLabel(chars);
    __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "%s:", chars);
    if(isCopy) env->ReleaseStringUTFChars(label, chars);
}

extern "C"
JNIEXPORT jint JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_getRegisterValue(JNIEnv *, jobject, jint i) {
    return static_cast<jint>(currentProcess->getXReg(static_cast<unsigned int>(i)));
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_addGlobal(JNIEnv *, jobject, jint reg) {
    for(auto& proc : processes)
    {
        uint64_t* value = new uint64_t{0};
        proc.addGlobal((unsigned int)reg, reinterpret_cast<uint64_t>(value));
        pointerContainer->addPointer(value);
    }
}


extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_run(JNIEnv *, jobject) {
    std::condition_variable cv;
    bool ready = false;
    std::list<std::thread> threads;
    for(auto& proc : processes)
    {
        threads.emplace_back([&](int) {
                std::mutex mtx;
                std::unique_lock<std::mutex> lck{mtx};
                __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Ready");
                while(!ready) cv.wait(lck);
                __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Running");
                proc.run();
            }, 0);
    }
    ready = true;
    cv.notify_all();
    for(auto& th : threads)
    {
        th.join();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_calcBinary(JNIEnv *env, jobject, jint d,
                                                          jint s1, jstring operator_label,
                                                          jint s2) {
    jboolean isCopy;
    const char* chars = env->GetStringUTFChars(operator_label, &isCopy);
    switch(chars[0])
    {
        case '+': __ Add(xLut(d), xLut(s1), xLut(s2)); __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Add %d, %d, %d", d, s1, s2); break;
        case '=': __ Sub(xLut(d), xLut(s1), xLut(s2)); __ Mov(xLut(s1), (uint64_t) 0), __ Cinv(xLut(d), xLut(s1), ne);
        __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Sub %d, %d, %d", d, s1, s2); __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Mov, %d, imm0", s1); __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Cinv %d, %d ne", d, s1); break;
        case '-': __ Sub(xLut(d), xLut(s1), xLut(s2)); __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Sub %d, %d, %d", d, s1, s2); break;
        case '*': __ Mul(xLut(d), xLut(s1), xLut(s2)); __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Mul %d, %d, %d", d, s1, s2); break;
        case '/': __ Sdiv(xLut(d), xLut(s1), xLut(s2)); __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Div %d, %d, %d", d, s1, s2); break;
        default: __ Add(xLut(d), xLut(s1), xLut(s2)); __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Add %d, %d, %d", d, s1, s2); break;
    }
    if(isCopy) env->ReleaseStringUTFChars(operator_label, chars);
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_calcUnary(JNIEnv * env, jobject, jint d,
                                                         jstring operator_label, jint s) {
    jboolean isCopy;
    const char* chars = env->GetStringUTFChars(operator_label, &isCopy);
    switch(chars[0])
    {
        case '-': __ Neg(xLut(d), xLut(s)); __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Neg %d, %d", d, s); break;
        case 'n': __ Cmp(xLut(s), (uint64_t)0); __ Mov(xLut(s), (uint64_t) 0), __ Cinv(xLut(d), xLut(s), eq);
            __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Cmp %d, 0", s); __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Mov, %d, imm0", s); __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Cinv %d, %d eq", d, s); break;
        default: __ Neg(xLut(d), xLut(s)); __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Neg %d, %d", d, s); break;
    }
    if(isCopy) env->ReleaseStringUTFChars(operator_label, chars);
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_mov(JNIEnv *, jobject, jint d,
                                                   jint s) {
    __ Mov(xLut(d), xLut(s));
    __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Mov %d, %d", d, s);
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_movLit(JNIEnv *, jobject, jint d,
                                                      jint val) {
    __ Mov(xLut(d), static_cast<uint64_t>(val));
    __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Mov %d, imm%d", d, val);

}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_branch(JNIEnv *env, jobject, jint reg,
                                                      jstring label) {
    jboolean isCopy;
    const char* chars = env->GetStringUTFChars(label, &isCopy);
    __ Cbz(xLut(reg), currentProcess->getLabel(chars));
    __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Cbz %d, %s", reg, (char *) chars);

    if(isCopy) env->ReleaseStringUTFChars(label, chars);
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_ldr(JNIEnv *, jobject, jint d, jint s) {
    __ Ldr(xLut(d), MemOperand{xLut(s)});
    __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Ldr %d, %d", d, s);

}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_ldar(JNIEnv *, jobject , jint d, jint s) {
    __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Ldar %d, %d", d, s);
    __ Ldar(xLut(d), MemOperand{xLut(s)});
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_ldxr(JNIEnv *, jobject , jint d, jint s) {
    __ Ldxr(xLut(d), MemOperand{xLut(s)});
    __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Ldxr %d, %d", d, s);

}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_str(JNIEnv *, jobject, jint s, jint d) {
    __ Str(xLut(s), MemOperand{xLut(d)});
    __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Str %d, %d", d, s);

}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_stlr(JNIEnv *, jobject, jint s, jint d) {
    __ Stlr(xLut(s), MemOperand{xLut(d)});
    __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Stlr %d, %d", d, s);

}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_stxr(JNIEnv *, jobject, jint s, jint d) {
    __ Dmb(InnerShareable, BarrierAll);
    __ Str(xLut(s), MemOperand{xLut(d)});
    __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Stxr %d, %d", d, s);
    __ Dmb(InnerShareable, BarrierAll);
}

extern "C"
JNIEXPORT void JNICALL
Java_hu_bme_aut_xcfatest_thetacompat_JniCompat_ret(JNIEnv *, jobject) {
    __ ret();
    __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Ret");
}
#pragma clang diagnostic pop
