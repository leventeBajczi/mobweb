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