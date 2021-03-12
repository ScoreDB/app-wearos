package tech.scoredb.app.wearos.roller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import tech.scoredb.app.wearos.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roller_activity_main);
    }

    public void roll(View v) {
        int answerN = new Random().nextInt(4);
        String answer = String.valueOf((char) ('A' + answerN));
        TextView resultView = this.findViewById(R.id.result);
        resultView.setText(answer);
    }
}
