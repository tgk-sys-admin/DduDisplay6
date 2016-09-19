package lecip.co.jp.ddudisplay6;


import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

public class UdpSend {
    private static final String TAG = "UdpSend";

    public static final int OBC_DISCONNECT = 0;
    public static final int OBC_PROCESSING = 1;
    public static final int OBC_CONNECT = 2;
    private int mConnectStatus = OBC_DISCONNECT;

    private DatagramSocket sendSocket = null;   //UDP送信用ソケット
    private UdpRecive udpRecive = null;         //UDP受信用ソケット

    public void startSocketConnection(int cmd) {
        Log.d(TAG, "startSocketConnection : s");
        Log.d(TAG, "cmd : "+ cmd);
        byte[] senddata = new byte[0];
        switch (cmd){
            // ステータス情報コマンド送信
            case DduConst.CMD_STATUSINFO:
                senddata = new byte[29];
                senddata = DduSecCommand.getCommand_StatusInfo();
                Log.d(TAG, "senddata data : " + Util.bin2hex(senddata));
                break;

            // 経路設定コマンド送信
            case DduConst.CMD_ROUTELINE:
                senddata = new byte[13];
                senddata = DduSecCommand.getCommand_KeitoInfo();
                Log.d(TAG, "senddata data : " + Util.bin2hex(senddata));
                break;
            case DduConst.CMD_TELOPCODE:
            case DduConst.CMD_TIMEINFO:
            case DduConst.CMD_VERSIONINFO:
            default:
                Log.d(TAG, "Do Nothing");
                break;
        }

        final byte[] finalSenddata = senddata;
        // 送信処理
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(null == sendSocket) {
                    try {
                        sendSocket = new DatagramSocket();
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }
                InetAddress inetAddress = null;//送信先
                try {
                    inetAddress = InetAddress.getByName(DduConst.OBC_IPADDR_FRONT);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                DatagramPacket packet = new DatagramPacket(finalSenddata, finalSenddata.length, inetAddress, DduConst.OBC_PORTNUM_FRONT);//IPアドレス、ポート番号も指定
                try {
                    sendSocket.send(packet);//送信
                    MainActivity.TxTextSet(":"+ Util.bin2hex(finalSenddata));
                    // 送信成功時に受信クラス生成
                    try {
                        udpRecive= new UdpRecive(DduConst.DDU_IPADDR, DduConst.DDU_PORTNUM_FRONT);
                        udpRecive.receiveStart();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                 } catch (IOException e) {
                    e.printStackTrace();
                    MainActivity.TxTextSet("Exception : "+ e.getMessage());
                 }
            }
        }).start();
    }

    public void stopSocketConnection() {
        Log.d(TAG, "closeAll S");
        Log.d(TAG, "!!!!!sendSocket : "+ sendSocket);
        // 送信ソケットクローズ
        if(null != sendSocket){
            sendSocket.close();
            sendSocket = null;
            MainActivity.TxTextSet("Close");
        }
        Log.d(TAG, "!!!!!udpRecive : "+ udpRecive);
        if(null != udpRecive) {
            // 受信ソケットクローズ
            udpRecive.receiveStopt();
            udpRecive = null;
        }
        MainActivity.RxTextSet("Close");
    }
}
