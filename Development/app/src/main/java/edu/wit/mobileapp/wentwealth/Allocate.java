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

public class Allocate extends AppCompatActivity {

    private double currentRollover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate);

        Bundle bundle = this.getIntent().getExtras();
        currentRollover = bundle.getDouble("ROLLOVER");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.background));

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final EditText addValue = ((EditText)findViewById(R.id.addValue));
        Button confirm = (Button)findViewById(R.id.confirmAdd);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addValue.getText().toString().matches("") == false
                        && currentRollover > Double.parseDouble(addValue.getText().toString()))
                {
                    confirmBalanceChange();
                }
                else if (addValue.getText().toString().matches(""))
                {
                    Toast.makeText(Allocate.this, "No Amount Entered", Toast.LENGTH_SHORT).show();
                }
                else if (currentRollover < Double.parseDouble(addValue.getText().toString()))
                {
                    Toast.makeText(Allocate.this, "Insufficient Rollover Funds", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void confirmBalanceChange() {
        int addType;

        if(((RadioButton)findViewById(R.id.savingOption)).isChecked())
        {
            addType = R.id.savingOption;
        }
        else
        {
            addType = R.id.budgetOption;
        }

        final double amount = Double.parseDouble(((EditText)findViewById(R.id.addValue)).getText().toString());

        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", addType);
        bundle.putDouble("AMOUNT", amount);

        Intent intent = new Intent(Allocate.this, MainActivity.class);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
