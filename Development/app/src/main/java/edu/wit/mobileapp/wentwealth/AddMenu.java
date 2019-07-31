package edu.wit.mobileapp.wentwealth;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddMenu extends AppCompatActivity {

    FloatingActionButton clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        clearButton = (FloatingActionButton) findViewById(R.id.addFab);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearButton.hide();
                finish();
            }
        });

        final EditText addValue = ((EditText)findViewById(R.id.addValue));
        Button confirm = (Button) findViewById(R.id.confirmAdd);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addValue.getText().toString().matches("") == false)
                {
                    confirmBalanceChange();
                }
                else
                {
                    Toast.makeText(AddMenu.this, "No Amount Entered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void confirmBalanceChange() {
        int addType;

        if(((RadioButton)findViewById(R.id.savingsOption)).isChecked())
        {
            // Check if savings option is set, if so set type to savings
            addType = R.id.savingsOption;
        }
        else
        {
            // Default is expense option because it is already checked by default
            addType = R.id.expenseOption;
        }

        final double amount = Double.parseDouble(((EditText)findViewById(R.id.addValue)).getText().toString());

        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", addType);
        bundle.putDouble("AMOUNT", amount);

        Intent intent = new Intent(AddMenu.this, MainActivity.class);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
