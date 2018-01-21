package homenet.arduino;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by 박주현 on 2017-09-05.
 */

public class SplashActivity extends Activity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Thread.sleep(2000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

}
