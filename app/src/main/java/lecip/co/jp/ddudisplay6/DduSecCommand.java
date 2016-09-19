package lecip.co.jp.ddudisplay6;

/**
 * Created by hmitsuyasu on 2016/09/13.
 */
public class DduSecCommand {
    /**
     * コマンド番号ごとに送信用のバイト配列を生成するクラス
     */
    public static byte[] getCommand_StatusInfo (){
        // データ送信
//        DduStatus dduStatus = new DduStatus();
        byte[] responsedata = new byte[29];
        int index = 0;
        responsedata[index++] = 0x02;//ヘッダー
        responsedata[index++] = 00;//データサイズ(1byte目)
        responsedata[index++] = 0x17;//データサイズ(2byte目)
        //responsedata[index++] = (byte) 0xE9;//サイズSUM
        byte sumdata = (byte) (responsedata[1] + responsedata[2]);
        byte sizesum = (byte) (1 + ~sumdata);
        responsedata[index++] = sizesum;
        responsedata[index++] = 00;
        responsedata[index++] = DduStatus.getPsStatus();	//ステータス
        responsedata[index++] = DduStatus.getPsInputSignal();	//外部信号入力状態
        responsedata[index++] = DduStatus.getPsRoundTrip();	//往復
        responsedata[index++] = DduStatus.getPsKeitoNo();	//系統番号(1byte目)
        responsedata[index++] = DduStatus.getPsKeitoNo();	//系統番号(2byte目)
        responsedata[index++] = DduStatus.getPsKeitoNo();	//系統番号(3byte目)
        responsedata[index++] = DduStatus.getPsTeiNo();	//停留所連番号
        responsedata[index++] = DduStatus.getPsMsbLsb();	//データ有無 経度情報/緯度情報
        responsedata[index++] = DduStatus.getPsLatInf();	//経度情報(1byte目)
        responsedata[index++] = DduStatus.getPsLatInf();	//経度情報(2byte目)
        responsedata[index++] = DduStatus.getPsLatInf();	//経度情報(3byte目)
        responsedata[index++] = DduStatus.getPsLatInf();	//経度情報(4byte目)
        responsedata[index++] = DduStatus.getPsLonInf();	//緯度情報(1byte目)
        responsedata[index++] = DduStatus.getPsLonInf();	//緯度情報(2byte目)
        responsedata[index++] = DduStatus.getPsLonInf();	//緯度情報(3byte目)
        responsedata[index++] = DduStatus.getPsLonInf();	//緯度情報(4byte目)
        responsedata[index++] = DduStatus.getPsDirectionTravel();	//進行方向(1byte目)
        responsedata[index++] = DduStatus.getPsDirectionTravel();	//進行方向(2byte目)
        responsedata[index++] = DduStatus.getPsMovingSpeed();	//移動速度
        responsedata[index++] = DduStatus.getPsDistanceTravelled();	//始発からの走行距離(1byte目)
        responsedata[index++] = DduStatus.getPsDistanceTravelled();	//始発からの走行距離(2byte目)
        responsedata[index++] = DduStatus.getPsDistanceTravelled();	//始発からの走行距離(3byte目)
        //responsedata[index++] = 7;	//データSUM
        sumdata = 0;

        ;for (int i = 4 ; i < responsedata.length ; i++
             ) {
            sumdata = (byte) (sumdata + responsedata[i]);

        }
        byte checksum = (byte) (0x0100 - sumdata);
        responsedata[index++] = checksum; //サム値
//        responsedata[index++] = (byte) 0x00; //固定サム値
        responsedata[index++] = 0x03;	//フッター
        return responsedata;
    }

    public static byte[] getCommand_KeitoInfo (){
        byte[] responsedata = new byte[13];
        int index = 0;
        responsedata[index++] = 0x02;//ヘッダー
        responsedata[index++] = 00;//データサイズ(1byte目)
        responsedata[index++] = 0x07;//データサイズ(2byte目)
        //responsedata[index++] = (byte) 0xF9;//サイズSUM
        byte sumdata = (byte) (responsedata[1] + responsedata[2]);
        byte sizesum = (byte) (1 + ~sumdata);
        responsedata[index++] = sizesum;
        responsedata[index++] = 02;
        responsedata[index++] = DduStatus.getPsChangeReason();	//変更理由
        responsedata[index++] = DduStatus.getPsRoundTrip();	//往復

        responsedata[index++] = DduStatus.getPsKeitoNo();	//系統番号
        responsedata[index++] = 00;	//系統番号
        responsedata[index++] = 0x02;	//系統番号

        responsedata[index++] = DduStatus.getPsTeiNo();	//停留所連番号

        if(MainActivity.testflg){
            responsedata[10] = 5;
            MainActivity.testflg = false;
        }


        //responsedata[index++] = 7;	//データSUM
        sumdata = 0;
        ;for (int i = 4 ; i < responsedata.length ; i++
                ) {
            sumdata = (byte) (sumdata + responsedata[i]);

        }
        byte checksum = (byte) (0x0100 - sumdata);
//        responsedata[index++] = checksum; //サム値
        responsedata[index++] = checksum;
//        responsedata[index++] = (byte) 0xF4;

                responsedata[index++] = 0x03;	//フッター
        return responsedata;
    }

}

