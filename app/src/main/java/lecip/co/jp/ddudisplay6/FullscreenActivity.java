package lecip.co.jp.ddudisplay6;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends Activity {

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 1000;
    private final Handler mChangeHandler = new Handler();
    private final int MESSAGE_EVT = 999;

    private Runnable mShowPart2Runnable = null;

    private int mCounter;
    private ImageView mimageView = null;
    private imageHandler mimageviewhandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_fullscreen);

        ImageView imageView = (ImageView)findViewById(R.id.image_view);
        if(null != imageView) {
            imageView.setImageResource(R.drawable.wvga1);
        }

        mimageviewhandler = new imageHandler();

        mCounter = 0;

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
    @Override
    public void onResume(){
        super.onResume();
        mShowPart2Runnable = new Runnable() {
            @Override
            public void run() {

                mCounter ++;
                if(5<mCounter){
                    mCounter = 0;
                }

                Message msg = mimageviewhandler.obtainMessage(MESSAGE_EVT);
                mimageviewhandler.sendMessage(msg);

                mChangeHandler.removeCallbacks(mShowPart2Runnable);
                mChangeHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
            }
        };

        mChangeHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(null != mChangeHandler){
            mChangeHandler.removeCallbacks(mShowPart2Runnable);
        }
    }

    private class imageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if(MESSAGE_EVT == msg.what) {

                mimageView = null;
                mimageView = (ImageView)findViewById(R.id.image_view);
                switch (mCounter){
                    case 0:
                        if(null != mimageView) {
                            mimageView.setImageResource(R.drawable.wvga1);
                        }
                        break;
                    case 1:
                        if(null != mimageView) {
                            mimageView.setImageResource(R.drawable.wvga2);
                        }
                        break;
                    case 2:
                        if(null != mimageView) {
                            mimageView.setImageResource(R.drawable.wvga3);
                        }
                        break;
                    case 3:
                        if(null != mimageView) {
                            mimageView.setImageResource(R.drawable.wvga4);
                        }
                        break;
                    case 4:
                        if(null != mimageView) {
                            mimageView.setImageResource(R.drawable.wvga5);
                        }
                        break;
                    default:
                        if(null != mimageView) {
                            mimageView.setImageResource(R.drawable.wvga1);
                        }
                        break;
                }
            }
        }
    }
}
