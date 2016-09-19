#include <string.h>
#include <stdlib.h>
#include <memory.h>
#include <lcp_serial.h>
#include "java-native-serialcomm.h"
#include "lcp_serial.h"
//#include "../Lib/lcp_serial.h"

#ifdef __cplusplus
extern "C" {
#endif

// #define SERIAL_JNIDEBUG
//#ifdef  SERIAL_JNIDEBUG /* Debug Start */
//static int  debugReadSize = 0;
//static char debugReadData[30] = { 0x10, 0x39, 0x32, 0x41, 0x30, 0x32, 0x30, 0x30, 0x30, 0x30,
//                                  0x30, 0x30, 0x31, 0x30, 0x32, 0x30, 0x30, 0x30, 0x30, 0x30,
//                                  0x33, 0x30, 0x30, 0x30, 0x30, 0x30, 0x34, 0x30, 0x38, 0x0a };
//#endif /* Debug End */
/****************************************/
/* ファイルオープン						*/
/****************************************/
JNIEXPORT jint JNICALL
		Java_lecip_co_jp_ddudisplay6_SerialCommOnGo_lcpSerialOpenJni(JNIEnv * env, jobject instance , jstring deviceName )
{

    const char *dName = env->GetStringUTFChars(deviceName, 0);
//    int devNameLen = (strlen(dName) + 1);
//    char reqDevName[256];
    int fd;

    /* デバイス名をchar型配列に変換 */
//    jbyte *workDev = env->GetByteArrayElements(deviceName, 0);
//    for ( int i = 0; i<devNameLen; i ++ ) {
//        reqDevName [ i ] = workDev [ i ] ;
//    }

#ifndef SERIAL_JNIDEBUG /* Debug Start */
    /* ポートオープン要求 */
    fd = lcp_serialOpen((char*)dName);
#else
    fd = 1;
    debugReadSize=0;
#endif /* Debug End */

    /* エレメント解放 */
    env->ReleaseStringUTFChars(deviceName, dName);

//    free( cpreqDevName );

    return fd;
}

/****************************************/
/* IOCTL								*/
/****************************************/
JNIEXPORT jint JNICALL
//		Java_lecip_co_jp_ddudisplay6_SerialCommOnGo_lcpSerialIoctlJni(JNIEnv * env, jobject instance, jint fd, jint mode, jintArray settingData )
        Java_lecip_co_jp_ddudisplay6_SerialCommOnGo_lcpSerialIoctlJni(JNIEnv * env, jobject instance, jint fd, jint mode)
{

//    jintArray ret;
//    jint *rData = env->GetIntArrayElements(ret, 0);

    lcp_serial_ioctl_t ioctlData;
    int *ipioctlData = (int *) &ioctlData;
    int ioctlRet = LCP_SERIAL_NG;
    int ioctlDatasize = (sizeof(lcp_serial_ioctl_t) / sizeof(int));
    int i;

    switch( mode ) {
        /* 設定値読み込み要求		*/
        case LCP_SERIAL_IOCTL_GET:
#ifndef SERIAL_JNIDEBUG /* Debug Start */
            /* 設定値読み込み */
//            ioctlRet = lcp_serialIoctl(fd, mode, &ioctlData);
#else
    ioctlRet =0;
    ioctlData.baudRate = 9600;
    ioctlData.stopBit = LCP_SERIAL_STOPBIT_1;
    ioctlData.parity = LCP_SERIAL_PARITY_OFF;
    ioctlData.iflag = 0;
    ioctlData.oflag = 0;
    ioctlData.cflag = 0;
    ioctlData.lflag = 0;
#endif /* Debug End */
            break;

        /* 設定値書き込み要求		*/
        case LCP_SERIAL_IOCTL_SET:
        /* 設定値FLAG情報書き込み	*/
        case LCP_SERIAL_IOCTL_SET_FLG:
        /* 設定要求データ取得 */
//        jint *sData = env->GetIntArrayElements(settingData, 0);

        /* 設定データを変換 */
//        for( i = 0; i<ioctlDatasize; i++ ) {
//            ipioctlData[i] = sData[i];
//        }

//#ifndef SERIAL_JNIDEBUG /* Debug Start */
        /* 設定値書き込み */
        ioctlRet = lcp_serialIoctl(fd, LCP_SERIAL_IOCTL_GET, &ioctlData);
        ioctlData.baudRate = 9600;
        ioctlData.parity = 0;
        ioctlData.stopBit = 1;
        ioctlRet = lcp_serialIoctl(fd, LCP_SERIAL_IOCTL_SET, &ioctlData);

        ioctlRet = lcp_serialIoctl(fd, LCP_SERIAL_IOCTL_GET, &ioctlData);

//#endif /* Debug End */

        /* 設定成功時は再度設定値の読み込みを行う */
//        if( ioctlRet == LCP_SERIAL_OK ) {
#ifndef SERIAL_JNIDEBUG /* Debug Start */
        /* 設定値読み込み */
//            (void)lcp_serialIoctl( fd, LCP_SERIAL_IOCTL_GET, &ioctlData );
#endif /* Debug End */
//        }

        /* エレメント解放 */
//        env->ReleaseIntArrayElements(settingData, sData,0);

        break;
    }

#if 0
    jint *rData = env->GetIntArrayElements(ret, 0);
    /* 要求成功時の返却値設定 */
    if( ioctlRet == LCP_SERIAL_OK ) {
        /* データサイズ設定 */
        ret = env->NewIntArray(ioctlDatasize);
        /* 読み込み値をjintarray型に変換 */
        for( i = 0; i<ioctlDatasize; i++) {
            rData[i] = ipioctlData[i];
        }
    } else {
        /* エラー終了 */
        return NULL;
    }
#endif
//    return ret;
    return ioctlData.baudRate;
}

/****************************************/
/* READ									*/
/****************************************/
JNIEXPORT jbyteArray JNICALL
		Java_lecip_co_jp_ddudisplay6_SerialCommOnGo_lcpSerialReadJni(JNIEnv * env, jobject instance,jint fd, jint size)
{

    char readData[size];
    int read_ret;
    int i;
    jbyteArray ret;
//    jbyte *rData = env->GetByteArrayElements(ret, 0);

#ifndef SERIAL_JNIDEBUG /* Debug Start */
    /* データ読み込み */
    read_ret = lcp_serialRead(fd, size, readData);
#else
    if( debugReadSize >= 30 ) {
        debugReadSize=0;
    }
    memcpy( readData, &debugReadData[debugReadSize], size );
    read_ret = size;
#endif /* Debug End */

    jbyteArray rData = (*env).NewByteArray(read_ret)  ;
    /* 読み込み成功 */
    /* サイズ「0」も読み込み成功とする */
    if( read_ret != LCP_SERIAL_ACCESS_ERROR ) {
        /* データサイズ設定 */
//        ret = env->NewByteArray(read_ret);
        /* 読み込みデータをjbytearray型に変換 */
//        for( i = 0; i<read_ret; i++) {
//            rData[i] = readData[i];
//        }
        (*env).SetByteArrayRegion(rData,0,read_ret,(const jbyte *)readData);
    } else {
        /* エラー終了 */
        return NULL;
    }

    return rData;
}

/****************************************/
/* WRITE								*/
/****************************************/
JNIEXPORT jint JNICALL
		Java_lecip_co_jp_ddudisplay6_SerialCommOnGo_lcpSerialWriteJni(JNIEnv * env, jobject instance, jint fd, jbyteArray writeData)
{
    int ret;
    int i;
    jbyte *wData = (*env).GetByteArrayElements(writeData, 0);
    int wLen = env->GetArrayLength(writeData);
    char *cpWriteData;

    cpWriteData = (char *) malloc(wLen);
    if( cpWriteData == NULL ) {
        return  LCP_SERIAL_NG;
    }

//    (*env).GetByteArrayRegion(writeData,0,wLen,(jbyte *)cpWriteData);

    /* 書き込みデータをchar型に変換 */
    for( i = 0; i<wLen; i++ ) {
//        if(wData[i] == 0x0a ) {         // なんか変換されてしまうので暫定処理
//            cpWriteData[i] = 0x0d;
//        } else {
            cpWriteData[i] = wData[i];
//        }
    }

#ifndef SERIAL_JNIDEBUG /* Debug Start */
    /* ポートへ出力 */
    ret = lcp_serialWrite(fd, wLen, cpWriteData);
#else
    ret = wLen;
#endif /* Debug End */

    /* エレメント解放 */
    (*env).ReleaseByteArrayElements(writeData, wData, 0);

    free( cpWriteData );

    return ret;
}

/****************************************/
/* CLOSE								*/
/****************************************/
JNIEXPORT void JNICALL
		Java_lecip_co_jp_ddudisplay6_SerialCommOnGo_lcpSerialCloseJni(JNIEnv *env, jobject instance, jint fd)
{

#ifndef SERIAL_JNIDEBUG /* Debug Start */
    lcp_serialClose( fd );
#endif /* Debug End */

}

#ifdef __cplusplus
}
#endif
