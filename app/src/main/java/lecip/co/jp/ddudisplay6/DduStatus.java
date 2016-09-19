package lecip.co.jp.ddudisplay6;

/**
 * Created by hmitsuyasu on 2016/09/13.
 */
public class DduStatus {

    private static byte PS_STATUS = 0;		//ステータス
    private static byte PS_INPUT_SIGNAL = 0;		//外部信号入力状態
    private static byte PS_ROUND_TRIP = 0;	//往復
    private static byte PS_KEITO_NO = 0;		//系統番号
    private static byte PS_TEI_NO = 0;		//停留所連番号
    private static byte PS_MSB_LSB = 0;		//データ有無 経度情報/緯度情報
    private static byte PS_LAT_INF = 0;		//経度情報
    private static byte PS_LON_INF = 0;        //緯度情報
    private static byte PS_DIRECTION_TRAVEL = 0;		//進行方向
    private static byte PS_MOVING_SPEED = 0;		//移動速度
    private static byte PS_DISTANCE_TRAVELLED = 0;		//始発からの走行距離
    private static byte PS_CHANGE_REASON = 0;		//変更理由

    public static byte getPsStatus() {
        return PS_STATUS;
    }

    private static void setPsStatus(byte psStatus) {
        PS_STATUS = psStatus;
    }

    public static byte getPsInputSignal() {
        return PS_INPUT_SIGNAL;
    }

    private static void setPsInputSignal(byte psInputSignal) {
        PS_INPUT_SIGNAL = psInputSignal;
    }

    public static byte getPsRoundTrip() {
        return PS_ROUND_TRIP;
    }

    private static void setPsRoundTrip(byte psRoundTrip) {
        PS_ROUND_TRIP = psRoundTrip;
    }

    public static byte getPsKeitoNo() {
        return PS_KEITO_NO;
    }

//    private static void setPsKeitoNo(byte psKeitoNo) {
    public static void setPsKeitoNo(byte psKeitoNo) {
        PS_KEITO_NO = psKeitoNo;
    }

    public static byte getPsTeiNo() {
        return PS_TEI_NO;
    }

//    private static void setPsTeiNo(byte psTeiNo) {
    public static void setPsTeiNo(byte psTeiNo) {
        PS_TEI_NO = psTeiNo;
    }

    public static byte getPsMsbLsb() {
        return PS_MSB_LSB;
    }

    private static void setPsMsbLsb(byte psMsbLsb) {
        PS_MSB_LSB = psMsbLsb;
    }

    public static byte getPsLatInf() {
        return PS_LAT_INF;
    }

    private static void setPsLatInf(byte psLatInf) {
        PS_LAT_INF = psLatInf;
    }

    public static byte getPsLonInf() {
        return PS_LON_INF;
    }

    private static void setPsLonInf(byte psLonInf) {
        PS_LON_INF = psLonInf;
    }

    public static byte getPsDirectionTravel() {
        return PS_DIRECTION_TRAVEL;
    }

    private static void setPsDirectionTravel(byte psDirectionTravel) {
        PS_DIRECTION_TRAVEL = psDirectionTravel;
    }

    public static byte getPsMovingSpeed() {
        return PS_MOVING_SPEED;
    }

    private static void setPsMovingSpeed(byte psMovingSpeed) {
        PS_MOVING_SPEED = psMovingSpeed;
    }

    public static byte getPsDistanceTravelled() {
        return PS_DISTANCE_TRAVELLED;
    }

    private static void setPsDistanceTravelled(byte psDistanceTravelled) {
        PS_DISTANCE_TRAVELLED = psDistanceTravelled;
    }

    public static byte getPsChangeReason() {
        return PS_CHANGE_REASON;
    }

    private static void setPsChangeReason(byte psChangeReason) {
        PS_CHANGE_REASON = psChangeReason;
    }
}
