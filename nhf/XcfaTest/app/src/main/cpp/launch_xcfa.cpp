//
// Created by levente on 10/25/19.
//

#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_hu_bme_aut_xcfatest_MainActivity_textFromNative( JNIEnv* env, jobject thiz)
{
    return env -> NewStringUTF("Hello from native code!");
}