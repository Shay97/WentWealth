package edu.wit.mobileapp.wentwealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static int ADD_MENU_REQUEST = 1;

    FloatingActionButton fab;
    TextView budget;
    TextView savings;
    TextView balanceSign;
    TextView savingsSign;
    double currentBudget;
    double currentSavings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the current values of budget and savings.
        budget = ((TextView)findViewById(R.id.currentBalance));
        savings = ((TextView)findViewById(R.id.currentSavings));

        balanceSign = ((TextView)findViewById(R.id.balanceSign));
        savingsSign = ((TextView)findViewById(R.id.savingsSign));

        currentBudget = Double.parseDouble(budget.getText().toString());
        currentSavings = Double.parseDouble(savings.getText().toString());

        // Click listener for floating action button.
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.hide();
                Intent intent = new Intent(getApplicationContext(), AddMenu.class);
                startActivityForResult(intent, ADD_MENU_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_MENU_REQUEST && resultCode == RESULT_OK)
        {
            if (data != null)
            {
                Bundle bundle = data.getExtras();
                double amount = bundle.getDouble("amount");
                int type = bundle.getInt("type");

                if (type == R.id.savingsOption)
                {
                    currentSavings = currentSavings + amount;

                    if (currentSavings > 0)
                    {
                        savings.setTextColor(getResources().getColor(R.color.positive));
                        savings.setText(String.valueOf(currentSavings));
                        savingsSign.setTextColor(getResources().getColor(R.color.positive));
                    }
                    else
                    {
                        // If savings value is negative or 0, set it back to default 0.00
                        // User is not allowed to have a negative savings
                        savings.setTextColor(getResources().getColor(R.color.nil));
                        savings.setText(R.string.defaultZero);
                        savingsSign.setTextColor(getResources().getColor(R.color.nil));
                    }
                }
                else
                {
                    // Calculate new budget
                    currentBudget = currentBudget - amount;

                    // Update coloring based on balance
                    if(currentBudget < 0)
                    {
                        // Set budget card text color for negative case
                        budget.setTextColor(getResources().getColor(R.color.negative));
                        balanceSign.setText(R.string.negative);
                        balanceSign.setTextColor(getResources().getColor(R.color.negative));
                    }
                    else if(currentBudget > 0)
                    {
                        // Set budget card text color for positive case
                        budget.setTextColor(getResources().getColor(R.color.positive));
                        balanceSign.setText(R.string.positive);
                        balanceSign.setTextColor(getResources().getColor(R.color.positive));
                    }
                    else
                    {
                        // Set budget card text color for null case
                        budget.setTextColor(getResources().getColor(R.color.nil));
                        balanceSign.setText(R.string.positive);
                        balanceSign.setTextColor(getResources().getColor(R.color.nil));
                    }

                    // Set the budget text to update current budget
                    budget.setText(String.valueOf(currentBudget));
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // THIS IS A TEST
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,Settings.class);
            this.startActivity(intent);
            return true;
        }
        else if (id == R.id.action_wishlist) {
            Intent intent = new Intent(this, Wishlist.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
