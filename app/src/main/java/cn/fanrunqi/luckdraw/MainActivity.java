package cn.fanrunqi.luckdraw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.fanrunqi.luckdrawlibrary.LuckDraw;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.luckdraw)
    LuckDraw luckdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        luckdraw.Init("$7000000",R.drawable.luckdraw_fg);
        luckdraw.setText("#dddddd",60);
        luckdraw.setStrokeWidth(50);
        luckdraw.setOnCompleteListener(new LuckDraw.CompleteListener() {
            @Override
            public void complete() {
                Toast.makeText(MainActivity.this,"Complete draw!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
