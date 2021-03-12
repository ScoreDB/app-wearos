package tech.scoredb.app.wearos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goStudentDB(View v) {
        Intent intent = new Intent(this, tech.scoredb.app.wearos.studentdb.MainActivity.class);
        startActivity(intent);
    }

    public void goRoller(View v) {
        Intent intent = new Intent(this, tech.scoredb.app.wearos.roller.MainActivity.class);
        startActivity(intent);
    }
}
