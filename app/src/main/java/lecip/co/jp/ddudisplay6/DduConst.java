package lecip.co.jp.ddudisplay6;

public class DduConst {

//    protected static final Class<?> ServiceClassName = DduService.class;

    // DDU
//  protected static final String DDU_IPADDR = "192.168.254.29";      // IPアドレス
    protected static final String DDU_IPADDR = "192.168.11.3";      // IPアドレス DDU実機
//    protected static final String DDU_IPADDR = "10.0.2.15";      // IPアドレス //Androidエミュレータ
    protected static final int DDU_PORTNUM_FRONT = 50001;                     // ポート番号(対OBC-UW(前)
    protected static final int DDU_PORTNUM_BACK = 50002;                     // ポート番号(対OBC-UW(後))
    // 中吊表示器 OBC-UW(前)
//    protected static final String OBC_IPADDR_FRONT = "192.168.254.240";      // IPアドレス
//    protected static final int OBC_PORTNUM_FRONT = 50001;                     // ポート番号
    // TEST CODE 中吊表示器 OBC-UW(前)
//    protected static final String OBC_IPADDR_FRONT = "192.168.254.240";      // IPアドレス
//    protected static final String OBC_IPADDR_FRONT = "192.168.11.4";      // IPアドレス //中吊り 実機
//    protected static final String OBC_IPADDR_FRONT = "10.0.2.15";      // IPアドレス // Androidエミュレータ
    protected static final String OBC_IPADDR_FRONT = "192.168.11.2";      // IPアドレス //DDU用エミュレータ
    protected static final int OBC_PORTNUM_FRONT = 50001;                     // ポート番号

    // 中吊表示器 OBC-UW(後)
    protected static final String OBC_IPADDR_BACK = "192.168.254.239";      // IPアドレス
    protected static final int OBC_PORTNUM_BACK = 50002;                     // ポート番号

    // DDU⇒中吊り表示器コマンド
    public static final int CMD_STATUSINFO = 0;    // ステータス情報コマンド（コマンド番号：00）
    public static final int CMD_VERSIONINFO = 1;   // バージョン情報取得コマンド（コマンド番号：01) //TODO 非対応;9月末
    public static final int CMD_ROUTELINE = 2;        // 系統設定コマンド（コマンド番号：02）
    public static final int CMD_TIMEINFO = 3;        // 時計情報設定コマンド（コマンド番号：03）        //TODO 非対応;9月末
    public static final int CMD_TELOPCODE = 4;        // テロップコード設定コマンド（コマンド番号：04） //TODO 非対応;9月末

}
