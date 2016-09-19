#ifndef JAVA_NATIVE_SERIALCOMM_H
#define JAVA_NATIVE_SERIALCOMM_H
#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

/* プロトタイプ */
JNIEXPORT jint       JNICALL Java_lecip_co_jp_ddudisplay6_SerialCommOnGo_lcpSerialOpenJni (JNIEnv *env, jobject instance, jstring deviceName);
//JNIEXPORT jintArray  JNICALL Java_lecip_co_jp_ddudisplay6_SerialCommOnGo_lcpSerialIoctlJni(JNIEnv *env, jobject instance, jint fd, jint mode, jintArray settingData);
JNIEXPORT jint       JNICALL Java_lecip_co_jp_ddudisplay6_SerialCommOnGo_lcpSerialIoctlJni(JNIEnv *env, jobject instance, jint fd, jint mode);
JNIEXPORT jbyteArray JNICALL Java_lecip_co_jp_ddudisplay6_SerialCommOnGo_lcpSerialReadJni (JNIEnv *env, jobject instance, jint fd, jint size);
JNIEXPORT jint       JNICALL Java_lecip_co_jp_ddudisplay6_SerialCommOnGo_lcpSerialWriteJni(JNIEnv *env, jobject instance, jint fd, jbyteArray writeData);
JNIEXPORT void      JNICALL Java_lecip_co_jp_ddudisplay6_SerialCommOnGo_lcpSerialCloseJni(JNIEnv *env, jobject instance, jint fd);

#ifdef __cplusplus
}
#endif
#endif //JAVA_NATIVE_SERIALCOMM_H
