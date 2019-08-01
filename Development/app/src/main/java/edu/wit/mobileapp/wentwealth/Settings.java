package edu.wit.mobileapp.wentwealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    private EditText setBudget;
    private EditText setSavingsTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.SettingsPageTitle));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setBudget = ((EditText)findViewById(R.id.budgetInput));
        setSavingsTotal =((EditText)findViewById(R.id.savingsGoalInput));

        Button button = findViewById(R.id.ConfirmButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setBudget.getText().toString().matches("") == false
                        && setSavingsTotal.getText().toString().matches("") == false)
                {
                    confirmSettingsChanged();
                }
                else
                {
                    Toast.makeText(Settings.this, "No Amount Entered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void confirmSettingsChanged() {

        double budget = Double.parseDouble(setBudget.getText().toString());
        int savingsTotal = Integer.parseInt(setSavingsTotal.getText().toString());

        Bundle bundle = new Bundle();
        bundle.putDouble("BUDGET", budget);
        bundle.putInt("SAVINGS_TOTAL", savingsTotal);

        Intent intent = new Intent(Settings.this, MainActivity.class);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

}
