package activity.example.chaosxu.switchbutton.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import activity.example.chaosxu.switchbutton.R;
import activity.example.chaosxu.switchbutton.view.SwitchButton;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

        @Bind(R.id.switchButton)
        SwitchButton switchButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                ButterKnife.bind(this);
                switchButton.setOnCheckChangeListener(new SwitchButton.OnCheckChangeListener() {
                        @Override
                        public void onCheckChanged(View view, boolean isOpen) {
                                Toast.makeText(MainActivity.this, "滑动开关当前的状态：" + isOpen, Toast.LENGTH_SHORT).show();
                        }
                });
        }
}
