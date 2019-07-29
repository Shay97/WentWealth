package edu.wit.mobileapp.wentwealth;

<<<<<<< Updated upstream
public class Settings {
=======
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.SettingsPageTitle));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    }
>>>>>>> Stashed changes
}
