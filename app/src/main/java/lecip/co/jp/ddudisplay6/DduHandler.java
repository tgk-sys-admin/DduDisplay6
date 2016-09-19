package lecip.co.jp.ddudisplay6;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class DduHandler extends Handler {
    private static final String TAG = "DduHandler";

    @Override
    public void handleMessage(Message msg) {
        Log.d(TAG, "#####handleMessage msg : "+ msg);
        switch(msg.what) {
            // ステータス情報コマンド
            case MainActivity.MESSAGE_STATUSINFO_CMD:
                Log.d(TAG, "send CMD_STATUSINFO : "+ MainActivity.mUdpSend);
                if(null != MainActivity.mUdpSend) {
                    MainActivity.mUdpSend.startSocketConnection(DduConst.CMD_STATUSINFO);
                }
                break;

            // 系統経路設定コマンド
            case MainActivity.MESSAGE_LINEROUTE_CMD:
                Log.d(TAG, "send CMD_ROUTELINE : "+ MainActivity.mUdpSend);
                if(null != MainActivity.mUdpSend) {
                    MainActivity.mUdpSend.startSocketConnection(DduConst.CMD_ROUTELINE);
                }
                break;

            // ステータス要求
            case MainActivity.MESSAGE_STATUS_REQ:
                // TODO
                break;

            // コマンド命令
            case MainActivity.MESSAGE_CMD_REQ:
                // TODO
                break;
        }
        return;
    }
}
