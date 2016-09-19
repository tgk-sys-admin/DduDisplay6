package lecip.co.jp.ddudisplay6;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpRecive {
    private static final String TAG = "UdpRecive";
    static BufferedReader br = new BufferedReader( new InputStreamReader(System.in) );

    private String reipadd = "";
    private int report = 0;

    SocketAddress sockAddress;//接続してきた送信元

    // 中吊り表示器(前側)受信ソケット
    DatagramSocket recSocket = null;

    public UdpRecive(String ipadd, int port) throws Exception {//コンストラクタ
        this.reipadd = ipadd;
        this.report = port;
    }

    private void receive() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte []buf = new byte[256];
                DatagramPacket packet= new DatagramPacket(buf, buf.length);

                try {
                    Log.d(TAG,"!!!!!!recSocket OPEN");
                    // フェールセーフ オープン中はクローズ
                    if(null != recSocket){
                        Log.d(TAG,"!!!!!!recSocket opened");
                        recSocket.disconnect();
                        recSocket.close();
                        recSocket = null;
                    }
                    // 中吊り表示器(前側)受信ソケット生成
                    Log.d(TAG,"!!!!!!report : " + report);
                    Log.d(TAG,"!!!!!!reipadd : " + reipadd);
                    try {
                        recSocket = new DatagramSocket(report, InetAddress.getByName(reipadd)); //実機ソケット OK
                    }catch (BindException e){
                        Log.e(TAG,"!!!!!e : "+ e.getMessage());
                        if(null != recSocket) {
                            recSocket.close();
                            recSocket = new DatagramSocket(report, InetAddress.getByName(reipadd)); //実機ソケット OK
                        }
                    }
                    Log.d(TAG,"!!!!!!recSocket : " + recSocket);

                    if(recSocket != null) {
                        Log.d(TAG,"!!!!!!receive Waiting...");
                        MainActivity.RxTextSet("Receive Wating...");

                        recSocket.receive(packet);//受信 & wait
                        MainActivity.RxTextSet(Util.bin2hex(packet.getData()));
                        Log.d(TAG,"!!!!!!packet : "+ packet);
                        recSocket.close();
                        recSocket=null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG,"!!!!!!e Message: " + e.getMessage());
                    MainActivity.RxTextSet("Exception : " + e.getMessage());
                }
                return;
            }
        }).start();
    }

    public void receiveStart(){
        Log.d(TAG,"receiveStart S");
//        while(test.receive() )//受信の繰り返し
//            ;
        receive();
        try {
            br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"receiveStart E");
    }

    public void receiveStopt(){
        Log.d(TAG,"!!!!!receiveStopt S");
        Log.d(TAG,"!!!!!recSocket :"+ recSocket);
        if(null != recSocket) {
            Log.d(TAG,"!!!!!isClosed() :"+ recSocket.isClosed());
            if(!recSocket.isClosed()) {
                recSocket.close();
                MainActivity.RxTextSet("CLOSE");
            }
//            recSocket = null;
        }
        Log.d(TAG,"!!!!!receiveStopt E");
    }
}
