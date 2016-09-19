package lecip.co.jp.ddudisplay6;

import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
//import java.util.Iterator;

public class SerialCommOnGo {
    private static final String LOG_TAG = "SerialCommOnGo";

    // JNI定義
    static {
        System.loadLibrary("java-native-serialcomm");
    }
	public native int    lcpSerialOpenJni (String deviceName);
//	public native int[]  lcpSerialIoctlJni(int fd, int mode, int[] settingData);
    public native int    lcpSerialIoctlJni(int fd, int mode);
	public native byte[] lcpSerialReadJni (int fd, int size);
	public native int    lcpSerialWriteJni(int fd, byte[] writeData);
	public native void   lcpSerialCloseJni(int fd);

    /**
     * シリアル通信デバイス
     **/
    public static final String SERIAL_DEVICE  = "/dev/ttySC0";

	// 処理結果ステータス
    public static final int SERIAL_OK           = 0;    // 処理成功
    public static final int SERIAL_NG           = 1;    // 処理失敗
    public static final int SERIAL_ACCESS_ERROR = -1;   // シリアルデバイスアクセスエラー

    // IOCTL要求モード
    public static final int SERIAL_IOCTL_GET     = 0;	// 設定値読み込み要求
    public static final int SERIAL_IOCTL_SET     = 1;   // 設定値書き込み要求
    public static final int SERIAL_IOCTL_SET_FLG = 2;   // 設定値FLAG情報書き込み

    // IOCTLデータ位置
    public static final int SERIAL_IOCTL_BAUDRATE = 0;  // 通信速度
    public static final int SERIAL_IOCTL_STOPBIT  = 1;  // ストップビット
    public static final int SERIAL_IOCTL_PARITY   = 2;  // バリティチェック
    public static final int SERIAL_IOCTL_IFLAG    = 3;  // c_iflag
    public static final int SERIAL_IOCTL_OFLAG    = 4;  // c_oflag
    public static final int SERIAL_IOCTL_CFLAG    = 5;  // c_cflag
    public static final int SERIAL_IOCTL_LFLAG    = 6;  // c_lflag

    // ストップビット
    public static final int SERIAL_STOPBIT_1     = 1;   // ストップビット：１
    public static final int SERIAL_STOPBIT_2     = 2;   // ストップビット：２

    // パリティチェック
    public static final int SERIAL_PARITY_OFF    = 0;   // パリティチェック無
    public static final int SERIAL_PARITY_EVEN   = 1;   // 偶数パリティチェック
    public static final int SERIAL_PARITY_ODD    = 2;   // 奇数パリティチェック

    // ポート設定値
    public static final int    SERIAL_SPEED   = 9600;
    public static final int    SERIAL_STOPBIT = SERIAL_STOPBIT_1;
    public static final int    SERIAL_PARITY  = SERIAL_PARITY_OFF;

    // データ開始／終了判定データ
    public static final byte SERIAL_DATA_DELIMITER = 0x10;   // データ開始デリミタ
    public static final byte SERIAL_DATA_TERIMITER = 0x0d;   // データ終了ターミネータ

    // コマンド命令位置
    public static final int SERIAL_COMMAND_POS   = 3;   // 奇数パリティチェック


    // ファイルディスクリプタ
    public int     mFileDiscripter = SERIAL_ACCESS_ERROR;
    public boolean mSuspendRequest = false;

    // 系統停留所情報通知情報
    public int mKeitoNo        = -1;
    public int mTeiryujyo      = -1;
    public int mTeiryujyoCode  = -1;
    public int mLEDKeito       = -1;
    public int mLEDEdaban      = -1;

    public void startConnection() {
        // 起動ログ
        Log.d(LOG_TAG, "!!!!!startConnection : ");

        // Suspend要求クリア
        mSuspendRequest = false;

Log.d(LOG_TAG, "SerialPotOpenStart!!!");
        // ポートオープン
        mFileDiscripter = lcpSerialOpenJni( SERIAL_DEVICE );
        if( mFileDiscripter <= 0 ) {
            Log.d(LOG_TAG, "SerialPotOpenError!!! DevName=[" + SERIAL_DEVICE + "] ErrorCode = " + mFileDiscripter);
            return;
        }
Log.d(LOG_TAG, "SerialPotOpenOK!!! fd = "+mFileDiscripter);

//Log.d(LOG_TAG, "SerialPotIoctlGetStart!!!");
//        // デバイス設定情報取得
//        int[] ioctlOrgData = lcpSerialIoctlJni( mFileDiscripter, SERIAL_IOCTL_GET, null );
//Log.d(LOG_TAG, "SerialPotIoctlGetEnd!!! ioctldata= "+ioctlOrgData);

        // デバイス設定値保存
//        int[] ioctlNewData = ioctlOrgData;

        // 設定があっていない場合は再設定
//        if( ( ioctlOrgData[SERIAL_IOCTL_BAUDRATE] != SERIAL_SPEED   ) ||
//            ( ioctlOrgData[SERIAL_IOCTL_STOPBIT]  != SERIAL_STOPBIT ) ||
//            ( ioctlOrgData[SERIAL_IOCTL_PARITY]   != SERIAL_PARITY  ) ) {
//            ioctlNewData[SERIAL_IOCTL_BAUDRATE] = SERIAL_SPEED;
//            ioctlNewData[SERIAL_IOCTL_STOPBIT]  = SERIAL_STOPBIT;
//            ioctlNewData[SERIAL_IOCTL_PARITY]   = SERIAL_PARITY;
Log.d(LOG_TAG, "SerialPotIoctlSetStart!!!");
//            int[] ioctlRetData = lcpSerialIoctlJni( mFileDiscripter, SERIAL_IOCTL_SET, ioctlNewData );
        int baurate = lcpSerialIoctlJni( mFileDiscripter, SERIAL_IOCTL_SET);
//Log.d(LOG_TAG, "SerialPotIoctlSetEnd!!! ioctldata= "+ioctlRetData);
Log.d(LOG_TAG, "SerialPotIoctlSetEnd!!! Baurate = " + baurate );
//        }
// debug Start
        int testWrite = 0;     // 開始処理でいきなりwriteコマンドの実行するとき：1
        if( testWrite != 1) {
// debug End
            // 停止指示を受けるまで処理継続
            while ((mSuspendRequest == false) &&
                    (mFileDiscripter != SERIAL_ACCESS_ERROR)) {

                // ポートデータ読み込み
                byte[] commandData = readSerial();

                // Suspend要求がない場合処理継続
                if ((mSuspendRequest == false) &&
                        (commandData != null)) {
                    // 要求コマンド解析
                    boolean ret = commandAnalyse(commandData);
                    // コマンドは正常なコマンド
                    if (ret == true) {
                        // 応答コマンド送信
                        requestResponce();
//                    } else {
//                        Log.d(LOG_TAG, "Illigal Comannd : " + commandData);
                    }
                }
            }
// debug Start
        } else {
            // 応答コマンド送信
            requestResponce();
        }
// debug End

        // 設定を元に戻す
//        int[] ioctlRetData = lcpSerialIoctlJni( mFileDiscripter, SERIAL_IOCTL_SET_FLG, ioctlOrgData );

        // ポートClose
        lcpSerialCloseJni( mFileDiscripter );
        // ファイルディスクリプタ初期値に戻す
        mFileDiscripter = SERIAL_ACCESS_ERROR;
        Log.d(LOG_TAG, "!!!!!startConnection : Exit ");
    }

    public void stopConnection() {
        // Suspend要求設定
        mSuspendRequest = true;
        Log.d(LOG_TAG, "!!!!!Suspend Request ");
    }

    public void sendCommand() {
        byte[] requestData = new byte[30];
        {
            requestData[0] = SERIAL_DATA_DELIMITER;
            requestData[1] = 0x39;
            requestData[2] = 0x32;
            requestData[3] = 0x41;
            requestData[4] = 0x30;
            requestData[5] = 0x32;
            requestData[6] = 0x30;
            requestData[7] = 0x30;  // 系統番号
            requestData[8] = 0x30;  // 系統番号
            requestData[9] = 0x30;  // 系統番号
            requestData[10] = 0x30; // 系統番号
            requestData[11] = 0x30; // 系統番号
            requestData[12] = 0x30; // 系統番号
            requestData[13] = 0x30; // 駒番号(停留所連番号)
            requestData[14] = 0x35; // 駒番号(停留所連番号)
            requestData[15] = 0x30; // 停留所コード
            requestData[16] = 0x30; // 停留所コード
            requestData[17] = 0x30; // 停留所コード
            requestData[18] = 0x30; // 停留所コード
            requestData[19] = 0x30; // 停留所コード
            requestData[20] = 0x33; // 停留所コード
            requestData[21] = 0x30; // LEDコード
            requestData[22] = 0x30; // LEDコード
            requestData[23] = 0x30; // LEDコード
            requestData[24] = 0x30; // LEDコード
            requestData[25] = 0x30; // LEDコード
            requestData[26] = 0x34; // LEDコード
            requestData[27] = 0x30;
            requestData[28] = 0x38;
            requestData[29] = SERIAL_DATA_TERIMITER;
        }

        // ACK応答
        Log.d(LOG_TAG, "Command Request Start!!!");
        int ret = lcpSerialWriteJni( mFileDiscripter, requestData );
        if( ret == SERIAL_ACCESS_ERROR ) {
            Log.d(LOG_TAG, "Command Request Error : ");
        }
        Log.d(LOG_TAG, "Command Request OK!!!");
    }

    private byte[] readSerial() {
        byte[]  commandData = new byte[256];
        int     dataCount = 0;
        boolean readContinue = true;

        Log.d(LOG_TAG, "SerialPotReadStart!!!");
        // コマンド受信処理
        while ( readContinue == true ) {
            byte[] readData = lcpSerialReadJni( mFileDiscripter, 1 );

            // エラーは処理終了
            if( readData == null ) {
                Log.d(LOG_TAG, "PortRead Illigal : ");
                readContinue = false;
                break;
            }

            // タイムアウトでなければ処理実施
            if( readData.length != 0 ) {
                // 初回データの場合は開始データ判定
                if( dataCount == 0 ) {
                    // 開始データ判定
                    if( readData[0] == SERIAL_DATA_DELIMITER ) {
Log.d(LOG_TAG, "DataRead Start Get :"+readData[0]);
                        // データ保存
                        commandData[dataCount] = readData[0];
                        dataCount++;
                    } else {
                        // 開始デリミタでない場合は何もしない
                        Log.i(LOG_TAG, "DataRead Not DERIMITER :"+readData[0]);
                    }
                } else {
                    // データ保存
                    commandData[dataCount] = readData[0];
                    dataCount++;
Log.d(LOG_TAG, "DataRead Count= " + dataCount + " ReadData = " + readData[0]);

                    // 終了データ判定
//                    if( readData[0] == SERIAL_DATA_TERIMITER ||
//                        readData[0] == 0x0a ) {
                    if( readData[0] == SERIAL_DATA_TERIMITER ) {
                        // ターミネータ受信のため読み込みを終了する
                        readContinue = false;
                    }
                }
            } else {
                // タイムアウト
                if( mSuspendRequest == true ) {
                    //サスペンド要求があったら終了する
                    readContinue = false;
                }
            }
        }
        Log.d(LOG_TAG, "SerialPotReadEnd!!! ReadSize = " +dataCount+" SuspenFlag = "+mSuspendRequest);
        // エラー／終了指示の場合はnullリターン
        if( ( dataCount == 0 ) ||
            ( mSuspendRequest == true ) ) {
            return null;
        }
        return commandData;
    }

    public boolean commandAnalyse( byte[] commandData) {
        boolean ret = false;

        // 系統停留所情報初期化
        mKeitoNo        = -1;
        mTeiryujyo      = -1;
        mTeiryujyoCode  = -1;
        mLEDKeito       = -1;
        mLEDEdaban      = -1;

        // コマンド解析
        switch (commandData[SERIAL_COMMAND_POS]) {
            // ステータス要求コマンド
            case '*':
            // 信号状態通知
            case 'B':
            // 日時設定
            case 'C':
            // 停留所コード＋標柱コード送信
            case 'D':
Log.d(LOG_TAG, "Command :"+commandData[SERIAL_COMMAND_POS]);
                ret = true;
                break;
            // 系統停留所情報通知
            case 'A':
                // データ長確認(以下の処理は暫定：後できれいにする)
                if( commandData.length >= 30 ) {
                    mKeitoNo =(((commandData[12] - 0x30) * 1) +
                               ((commandData[11] - 0x30) * 10)+
                               ((commandData[10] - 0x30) * 100)+
                               ((commandData[ 9] - 0x30) * 1000)+
                               ((commandData[ 8] - 0x30) * 10000)+
                               ((commandData[ 7] - 0x30) * 100000));

                    mTeiryujyo =(((commandData[14] - 0x30) * 1) +
                                 ((commandData[13] - 0x30) * 10));

                    mTeiryujyoCode =(((commandData[20] - 0x30) * 1) +
                                     ((commandData[19] - 0x30) * 10)+
                                     ((commandData[18] - 0x30) * 100)+
                                     ((commandData[17] - 0x30) * 1000)+
                                     ((commandData[16] - 0x30) * 10000)+
                                     ((commandData[15] - 0x30) * 100000));
                    ret = true;
Log.d(LOG_TAG, "Command A: KEITO = " + mKeitoNo + " TEIRYUJO = "+ mTeiryujyo + " TEIRYUJO CODE = "+ mTeiryujyoCode);
                    DduStatus.setPsKeitoNo((byte)mKeitoNo);
                    DduStatus.setPsTeiNo((byte)mTeiryujyo);
                    // 中吊り表示器へ経路設定コマンド[02]送信
                    if(null != MainActivity.mUdpSend) {
                        MainActivity.mUdpSend.startSocketConnection(DduConst.CMD_ROUTELINE);
                    }
                }
                break;
            default:
Log.d(LOG_TAG, "Command Unknown!!! : "+commandData[SERIAL_COMMAND_POS]);
                break;
        }
        return ret;
    }

    public void requestResponce() {
        // 暫定コード　後で書き直し
        byte[] responceData = new byte[10];
        {
            responceData[0] = SERIAL_DATA_DELIMITER;
            responceData[1] = 0x39;
            responceData[2] = 0x32;
            responceData[3] = 0x21;
            responceData[4] = 0x30;
            responceData[5] = 0x30;
            responceData[6] = 0x30;
            responceData[7] = 0x31;
            responceData[8] = 0x43;
            responceData[9] = SERIAL_DATA_TERIMITER;
        }

        // ACK応答
Log.d(LOG_TAG, "Command Responce Start!!!");
        int ret = lcpSerialWriteJni( mFileDiscripter, responceData );
        if( ret == SERIAL_ACCESS_ERROR ) {
            Log.d(LOG_TAG, "Write Command Error : ");
        }
Log.d(LOG_TAG, "Command Responce OK!!!");

    }

}
