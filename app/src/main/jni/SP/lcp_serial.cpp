/*============================================
ヘッダファイルインクルード宣言
============================================*/
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <termios.h>
#include <unistd.h>
#include "lcp_serial.h"

#ifdef __cplusplus
extern "C" {
#endif

/*============================================
定数定義
============================================*/
#define SERIAL_LAST_BAUDRATE -1  /* ボーレートテーブル最終指標  */

/*============================================
SRAM利用宣言
============================================*/

/*============================================
固定テーブル宣言
============================================*/
typedef struct {
    int baudRateData;
    int baudRate;
} lcp_serial_baudRate_t;

/* ボーレート変換テーブル */
lcp_serial_baudRate_t baudRateTbl[] = {
    { B0,       0 },
    { B50,      50 },
    { B75,      75 },
    { B110,     110 },
    { B134,     134 },
    { B150,     150 },
    { B200,     200 },
    { B300,     300 },
    { B600,     600 },
    { B1200,    1200 },
    { B1800,    1800 },
    { B2400,    2400 },
    { B4800,    4800 },
    { B9600,    9600 },
    { B19200,   19200 },
    { B38400,   38400 },

    { SERIAL_LAST_BAUDRATE, 0 }
};

/*============================================
プロトタイプ宣言
============================================*/
static void lcp_serialGetBaudRate( struct termios* tio, int* ipbaudRate);
static int  lcp_serialSetBaudRate( struct termios* tio, int  ibaudRate);
static void lcp_serialGetStopBit ( struct termios* tio, int* ipstopBit );
static void lcp_serialSetStopBit ( struct termios* tio, int  istopBit );
static void lcp_serialGetParity  ( struct termios* tio, int* ipparity );
static void lcp_serialSetParity  ( struct termios* tio, int  iparity );



//-------------------------------------------
//  シリアルポートオープン処理
//-------------------------------------------
int lcp_serialOpen( char* cpDeviceName )
{
    int fd = LCP_SERIAL_ACCESS_ERROR;           /* ファイルディスクリプタ */

    /* パラメータチェック */
    if( cpDeviceName != NULL ) {

        /* シリアルポートオープン */
        fd = open (cpDeviceName, O_RDWR);

        /* エラー発生時はログに残す */
        if( fd == LCP_SERIAL_ACCESS_ERROR ) {
//          エラーログを入れる
        }
    }

    return fd;
}

//-------------------------------------------
//  シリアポートIOCTL
//-------------------------------------------
int lcp_serialIoctl( int fd, int mode, lcp_serial_ioctl_t* settingData)
{
    struct termios tio;    /* シリアル通信設定 */
    int ret      = LCP_SERIAL_NG;
    int ioctlret;
int i;

    /* パラメータチェック */
    if( ( fd != LCP_SERIAL_ACCESS_ERROR ) &&            /* ファイルディスクリプタ有効 */
        ( settingData != NULL         ) ) {         /* 設定値格納エリア有効 */

        /* エラーチェック必要 */
        ioctlret = ioctl(fd, TCGETS, &tio);
        if( ioctlret != LCP_SERIAL_ACCESS_ERROR ) {
            /* 設定値読み込み正常 */

            /* 要求モードにより分岐 */
            switch( mode ) {
                case LCP_SERIAL_IOCTL_GET:
                    /* 読み込み要求 */

                    /* 読み込み値を返却 */
                    settingData->iflag = tio.c_iflag;
                    settingData->oflag = tio.c_oflag;
                    settingData->cflag = tio.c_cflag;
                    settingData->lflag = tio.c_lflag;

                    /* ボーレート取得 */
                    lcp_serialGetBaudRate( &tio, &settingData->baudRate );

                    /* ストップビット */
                    lcp_serialGetStopBit( &tio, &settingData->stopBit );

                    /* パリティチェック */
                    lcp_serialGetParity( &tio, &settingData->parity );

                    ret = LCP_SERIAL_OK;

                    break;
                case LCP_SERIAL_IOCTL_SET:
#if 0
                    /* 書き込み要求 */
                    /* ボーレート設定 */
                    ret = lcp_serialSetBaudRate(&tio, settingData->baudRate );

                    /* ボーレート設定結果およびパラメータチェック */
                    if( (ret == LCP_SERIAL_OK) &&
                        /* ストップビットパラメータチェック */
                        (settingData->stopBit == LCP_SERIAL_STOPBIT_1 || settingData->stopBit == LCP_SERIAL_STOPBIT_2) &&
                        /* パリティチェックパラメータチェック */
                        (settingData->parity == LCP_SERIAL_PARITY_OFF || settingData->parity == LCP_SERIAL_PARITY_EVEN ||
                         settingData->parity == LCP_SERIAL_PARITY_ODD ) ) {

                        /* ストップビット */
                        lcp_serialSetStopBit( &tio, settingData->stopBit );

                        /* パリティチェック */
                        lcp_serialSetParity( &tio, settingData->parity );

                        /* タイムアウト設定(100ms) */
                        tio.c_cc[VTIME] = 1;
                        tio.c_cc[VMIN] = 0;
                        tio.c_lflag &= ~(ICANON);

                        /* 設定値書き込み */
                        ioctlret = ioctl(fd, TCSETS, &tio);

                        if( ioctlret == LCP_SERIAL_ACCESS_ERROR ) {
                            /* 設定書き込み異常終了 */
                            ret = LCP_SERIAL_NG;
                        }
                    }
#else
                    memset(&tio, 0, sizeof(tio));
                    tio.c_cflag = CS8 | CLOCAL | CREAD;
//                    tio.c_iflag = IGNPAR | ICRNL;
                    tio.c_iflag = IGNPAR ;

                    /* タイムアウト設定(100ms) */
                    tio.c_cc[VTIME] = 1;
                    tio.c_cc[VMIN] = 0;
                    tio.c_lflag &= ~(ICANON);

//                    cfsetispeed(&tio, B115200);
//                    cfsetospeed(&tio, B115200);
                    cfsetispeed(&tio, B9600);
                    cfsetospeed(&tio, B9600);


                    tcsetattr(fd, TCSANOW, &tio);

                    ret = LCP_SERIAL_OK;
#endif
                    break;
                case LCP_SERIAL_IOCTL_SET_FLG:
                    /* フラグ情報書き込み要求(元に戻す) */

                    /* 要求値を設定 */
                    tio.c_iflag = settingData->iflag;
                    tio.c_oflag = settingData->oflag;
                    tio.c_cflag = settingData->cflag;
                    tio.c_lflag = settingData->lflag;

                    /* タイムアウト設定解除 */
                    tio.c_cc[VTIME] = 0;
                    tio.c_cc[VMIN] = 1;
                    tio.c_lflag |= ICANON;

                    /* 設定値書き込み */
                    ioctlret = ioctl(fd, TCSETS, &tio);

                    if( ioctlret != LCP_SERIAL_ACCESS_ERROR ) {
                        /* 設定書き込み正常終了 */
                        ret = LCP_SERIAL_OK;
                    }

                    break;
                default :
//                  エラーログを入れる
                    break;
            }
        } else {
//          エラーログを入れる
        }
    } else {
//      エラーログを入れる
    }

    return ret ;
}

//-------------------------------------------
//  シリアルポートからの読み出し
//-------------------------------------------
int lcp_serialRead( int fd, int size, char* buff )
{
    int readSize = 0;
    int ret;
    char* pBuff = buff;

    /* パラメータチェック */
    if( ( fd != LCP_SERIAL_ACCESS_ERROR ) &&
        ( size > 0                      ) &&
        ( buff != NULL                  ) ) {

        /* 指定サイズ読み込みまで読み込み継続 */
        while ( readSize < size ) {
            /* エラーチェック必要：TBD */
            ret = read(fd, pBuff, 1);
            if( ret == LCP_SERIAL_ACCESS_ERROR ) {
                /* READエラー */
//              エラーログを入れる
                readSize = LCP_SERIAL_ACCESS_ERROR;
                break;
            }

            /* タイムアウト時は処理を抜ける */
            if( ret == 0 ) {
                break;
            }
            readSize++;
            pBuff++;
        }
    } else {
        /* パラメータエラーはアクセスエラー */
//      エラーログを入れる
        readSize = LCP_SERIAL_ACCESS_ERROR;
    }

    return readSize;
}

//-------------------------------------------
// シリアルポートへ書き出し
//-------------------------------------------
int lcp_serialWrite( int fd, int size, char* buff )
{
    int ret = LCP_SERIAL_ACCESS_ERROR;

    /* パラメータチェック */
    if( ( fd != LCP_SERIAL_ACCESS_ERROR ) &&
        ( size > 0                      ) &&
        ( buff != NULL                  ) ) {

        /* データ書き込み */
        ret = write(fd, buff, size);
        if( ret == LCP_SERIAL_ACCESS_ERROR ) {
            /* Writeエラー */
//          エラーログを入れる
        }
    } else {
        /* パラメータエラーはアクセスエラー */
//      エラーログを入れる
    }

    return ret;
}

//-------------------------------------------
//シリアルポートClose
//-------------------------------------------
void lcp_serialClose( int fd )
{

    /* FDが有効な場合のみCloseの要求 */
    if( fd != LCP_SERIAL_ACCESS_ERROR ) {
        close(fd);
    }

}



//-------------------------------------------
//ローカル関数群
//-------------------------------------------
//-------------------------------------------
//  ボーレート取得処理
//-------------------------------------------
static void lcp_serialGetBaudRate( struct termios* tio, int* ipbaudRate)
{
    int cBoaud;
    int i;

    /* ボーレート初期化 */
    *ipbaudRate  = 0 ;

    /* 設定値取得 */
    cBoaud = (tio->c_cflag  & CBAUD);

    /* ボーレートテーブル全検索 */
    for( i=0; baudRateTbl[i].baudRateData != SERIAL_LAST_BAUDRATE; i++) {
        /* 設定値がテーブルに存在 */
        if( cBoaud == baudRateTbl[i].baudRateData ) {
            /* ボーレート設定 */
            *ipbaudRate  = baudRateTbl[i].baudRate ;
            break ;
        }
    }
}

//-------------------------------------------
//  ボーレート設定処理
//-------------------------------------------
static int lcp_serialSetBaudRate( struct termios* tio, int ibaudRate)
{
    int i;
    int ret = LCP_SERIAL_NG;

    /* ボーレートテーブル全検索 */
    for( i=0; baudRateTbl[i].baudRateData != SERIAL_LAST_BAUDRATE; i++) {
        /* 設定値がテーブルに存在 */
        if( ibaudRate == baudRateTbl[i].baudRate ) {
            /* ボーレート設定 */
            tio->c_cflag  = (tio->c_cflag  & ~(CBAUD))|(baudRateTbl[i].baudRateData) ;
            ret = LCP_SERIAL_OK;
            break ;
        }
    }
    return ret;
}

//-------------------------------------------
//  ストップビット取得処理
//-------------------------------------------
static void lcp_serialGetStopBit ( struct termios* tio, int* ipstopBit )
{
    /* ストップビット１で初期化 */
    *ipstopBit = LCP_SERIAL_STOPBIT_1;

    if( (tio->c_cflag & CSTOPB) == CSTOPB ) {
        /* ストップビット：２ */
        *ipstopBit = LCP_SERIAL_STOPBIT_2;
    }
}

//-------------------------------------------
//  ストップビット設定処理
//-------------------------------------------
static void lcp_serialSetStopBit ( struct termios* tio, int  istopBit )
{

    if( istopBit == LCP_SERIAL_STOPBIT_2 ) {
        /* ストップビット：２ */
        tio->c_cflag |= CSTOPB;
    } else {
        tio->c_cflag = (tio->c_cflag & ~(CSTOPB));
    }
}

//-------------------------------------------
//  パリティチェック取得処理
//-------------------------------------------
static void lcp_serialGetParity  ( struct termios* tio, int* ipparity )
{
    /* パリティなしで初期化 */
    *ipparity = LCP_SERIAL_PARITY_OFF;

    if( (tio->c_cflag & PARENB) == PARENB ) {
        /* パリティあり */
        if( (tio->c_cflag & PARODD) == PARODD ) {
            /* 奇数パリティ */
            *ipparity = LCP_SERIAL_PARITY_ODD;
        } else {
            /* 偶数パリティ */
            *ipparity = LCP_SERIAL_PARITY_EVEN;
        }
    }
}

//-------------------------------------------
//  パリティチェック設定処理
//-------------------------------------------
static void lcp_serialSetParity  ( struct termios* tio, int  iparity )
{
    if( iparity == LCP_SERIAL_PARITY_OFF ) {
        /* パリティなし */
        tio->c_cflag = (tio->c_cflag & ~(PARENB|PARODD));
    } else {
        /* パリティあり */
        tio->c_cflag |= PARENB;

        if( iparity == LCP_SERIAL_PARITY_ODD ) {
            /* 奇数パリティ */
            tio->c_cflag = (tio->c_cflag & ~(PARODD));
        } else {
            /* 偶数パリティ */
            tio->c_cflag |= PARODD;
        }
    }
}
#ifdef __cplusplus
}
#endif
