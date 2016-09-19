package lecip.co.jp.ddudisplay6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;

public class MainActivity extends Activity {

    private static final String TAG = "DduService";
    // ハンドラ
    private Handler mHandler = null;
    // ハンドラメッセージ定数定義
    public static final int                  MESSAGE_STATUSINFO_CMD            = 0; // ステータス情報コマンド[00]
    public static final int                  MESSAGE_LINEROUTE_CMD             = 1; // 系統設定コマンド[02]
    public static final int                  MESSAGE_STATUS_REQ                  = 2; // ステータス要求
    public static final int                  MESSAGE_CMD_REQ                     = 3; // コマンド命令
    public static final int                  MESSAGE_SIGNAL_CHG                  = 4; // 外部信号変化通

    // 中吊り向けソケット
    public static Socket mSocket_obc_front_re = null;     // OBC-UW(前側)受信ソケット
    public static Socket mSocket_obc_front_send = null;  // OBC-UW(前側)送信ソケット
    public static Socket mSocket_obc_back_re = null;      // OBC-UW(後側)受信ソケット
    public static Socket mSocket_obc_back_send = null;   // OBC-UW(後側)送信ソケット

    private Handler mPollingHandler = new Handler();
    public static UdpSend mUdpSend = null;
    public static SerialCommOnGo  mSerialConnector = null;

    // ポーリングオブジェクト
    private Runnable mpolling;
    final int POLLING_TIME = 500;

    // デバッグ表示用
    // Text_SendData
    private static TextView mtxDatatextview = null;
    private static String mtxstr;
    static final Handler txhandler = new Handler();
    // Text_ReceiveData
    private static TextView mrxDatatextview = null;
    private static String mrxtxt;
    static final Handler rxhandler = new Handler();

    public static boolean testflg=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // デバッグ表示用
        mtxDatatextview = (TextView) findViewById(R.id.Text_SendData);
        mrxDatatextview = (TextView) findViewById(R.id.Text_ReceiveData);

        /* ハンドラ生成(内部向け) */
        mHandler = new DduHandler();

        mUdpSend = new UdpSend();

        // ボタンを設定
        Button button = (Button)findViewById(R.id.buttonA);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("!!!!!","!!!!!onClick");
                /* ソケットオープン(中吊り表示器(前側/中吊)向け) */
                // ステータス情報コマンド用ポーリング処理開始(500msec)
                mpolling = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "polling run s");
                        // 内部ハンドラへメッセージ送信 ステータス情報コマンド[00]
                        Message msg = mHandler.obtainMessage(MESSAGE_STATUSINFO_CMD);
                        mHandler.sendMessage(msg);

                        mPollingHandler.removeCallbacks(mpolling);
                        mPollingHandler.postDelayed(mpolling, POLLING_TIME);
                    }
                };
                mPollingHandler.postDelayed(mpolling, POLLING_TIME);
            }
        });

        // ボタンBを設定
        Button buttonb = (Button)findViewById(R.id.buttonB);
        buttonb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 内部ハンドラへメッセージ送信 経路設定コマンド[02]
                Message msg = mHandler.obtainMessage(MESSAGE_LINEROUTE_CMD);
                mHandler.sendMessage(msg);
           }
        });


        // ボタンDを設定(Close)
        Button buttond = (Button)findViewById(R.id.buttonD);
        buttond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Close ");
                if(null != mPollingHandler) {
                    mPollingHandler.removeCallbacks(mpolling);
                }
                if(null != mUdpSend) {
                    mUdpSend.stopSocketConnection();
                }
            }
        });

        // ボタンXを設定(DDU表示部)
        Button buttonz = (Button)findViewById(R.id.buttonZ);
        buttonz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FullscreenActivity.class));
            }
        });

        // testボタンを設定
        Button buttonkeito = (Button)findViewById(R.id.buttontest);
        buttonkeito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testflg = true;
            }
        });

        // ボタンOを設定(音声合成装置開始)
        Button buttonO = (Button)findViewById(R.id.buttonO);
        buttonO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mSerialConnector = new SerialCommOnGo();
                        if(null != mSerialConnector) {
                            mSerialConnector.startConnection();
                        }
                    }
                }).start();
            }
        });

        // ボタンPを設定(音声合成装置終了)
        Button buttonP = (Button)findViewById(R.id.buttonP);
        buttonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mSerialConnector) {
                    mSerialConnector.stopConnection();
                }
            }
        });

        // ボタンRを設定(音声合成装置コマンド疑似送信)
        Button buttonR = (Button)findViewById(R.id.buttonR);
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mSerialConnector) {
                    mSerialConnector.sendCommand();
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("!!!!!","onResume");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // マルチスレッドにしたい処理 ここから
//                socketopen();
//                // マルチスレッドにしたい処理 ここまで
//            }
//        }).start();

        return;
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d("!!!!!","onPause");
        if(null != mPollingHandler) {
            mPollingHandler.removeCallbacks(mpolling);
        }

        if(null != mUdpSend) {
            mUdpSend.stopSocketConnection();
        }
    }

    // デバッグ表示
    public static void TxTextSet(String txstr){
        mtxstr = txstr;

        txhandler.post(new Runnable() {

            public void run() {
                mtxDatatextview.setText(mtxstr);
            }

        });
    }

    // デバッグ表示
    public static void RxTextSet(String txstr){
        mrxtxt = txstr;

        rxhandler.post(new Runnable() {

            public void run() {
                mrxDatatextview.setText(mrxtxt);
            }

        });
    }
}

