#ifndef LCP_SERIAL_H
#define LCP_SERIAL_H

#ifdef __cplusplus
extern "C" {
#endif

/* 処理結果ステータス */
#define LCP_SERIAL_OK				0				/* 処理結果：OK				*/
#define LCP_SERIAL_NG				1				/* 処理結果：NG				*/
#define LCP_SERIAL_ACCESS_ERROR		-1				/* デバイスアクセスエラー	*/

/* IOCTL要求モード */
#define LCP_SERIAL_IOCTL_GET		0				/* 設定値読み込み要求		*/
#define LCP_SERIAL_IOCTL_SET		1				/* 設定値書き込み要求		*/
#define LCP_SERIAL_IOCTL_SET_FLG	2				/* 設定値FLAG情報書き込み	*/

/* ストップビット */
#define LCP_SERIAL_STOPBIT_1		1				/* ストップビット：１		*/
#define LCP_SERIAL_STOPBIT_2		2				/* ストップビット：２		*/

/* パリティチェック */
#define LCP_SERIAL_PARITY_OFF		0				/* パリティチェック無		*/
#define LCP_SERIAL_PARITY_EVEN		1				/* 偶数パリティチェック		*/
#define LCP_SERIAL_PARITY_ODD		2				/* 奇数パリティチェック		*/

/* Ioctl用構造体 */
typedef struct {
	int baudRate;									/* 通信速度					*/
/*  int connect;	*/								/* 通信方式　　　：指定不可	*/
/*  int synchro;	*/								/* 同期方式　　　：指定不可	*/
/*  int startBit;	*/								/* スタートビット：指定不可	*/
/*  int datatBit;	*/								/* データビット　：指定不可	*/
	int stopBit;									/* ストップビット			*/
	int parity;										/* バリティチェック			*/

	int iflag;										/* ioctl設定のc_iflag		*/
	int oflag;										/* ioctl設定のc_oflag		*/
	int cflag;										/* ioctl設定のc_cflag		*/
	int lflag;										/* ioctl設定のc_lflag		*/
} lcp_serial_ioctl_t;

/* Serial Port オープン */
int lcp_serialOpen( char* );

/* Ioctl */
int lcp_serialIoctl( int, int, lcp_serial_ioctl_t* );

/* Read */
int lcp_serialRead( int, int, char* );

/* Write */
int lcp_serialWrite( int, int, char* );

/* Serial Port クローズ */
void lcp_serialClose( int );

#ifdef __cplusplus
}
#endif

#endif // LCP_SERIAL_H
