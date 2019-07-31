package edu.wit.mobileapp.wentwealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {

    // Variables for handling requests
    public static int ADD_MENU_REQUEST = 1;
    public static int ROLLOVER_REQUEST = 2;
    public static int SETTINGS_REQUEST = 3;
    public static int WISHLIST_REQUEST = 4;

    private FloatingActionButton fab;
    private TextView budget;
    private TextView savings;
    private TextView balanceSign;
    private TextView savingsSign;
    private TextView savingsTotal;
    private double currentBudget;
    private double currentSavings;
    private double currentSavingsTotal;

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

        savingsTotal = ((TextView)findViewById(R.id.savingsGoal));

        currentBudget = Double.parseDouble(budget.getText().toString());
        currentSavings = Double.parseDouble(savings.getText().toString());
        currentSavingsTotal = Double.parseDouble(savingsTotal.getText().toString());

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

        // Activity Result Conditions for Add Menu Activity
        if (requestCode == ADD_MENU_REQUEST && resultCode == RESULT_OK)
        {
            if (data != null)
            {
                Bundle bundle = data.getExtras();
                double amount = bundle.getDouble("AMOUNT");
                int type = bundle.getInt("TYPE");

                if (type == R.id.savingsOption)
                {
                    // Calculate new savings
                    currentSavings = currentSavings + amount;

                    // Set the savings text to update current savings
                    setSavingsText(currentSavings);
                }
                else
                {
                    // Calculate new budget
                    currentBudget = currentBudget - amount;

                    // Set the budget text to update current budget
                    setBudgetText(currentBudget);
                }
            }
        }
        // Activity Results Conditions for Settings activity
        else if (requestCode == SETTINGS_REQUEST && resultCode == RESULT_OK)
        {
            if (data != null)
            {
                Bundle bundle = data.getExtras();

                currentSavingsTotal = bundle.getDouble("SAVINGS_TOTAL");
            }
        }
        // Activity Results Condition for Wishlist activity
        else if (requestCode == WISHLIST_REQUEST && resultCode == RESULT_OK)
        {
            if (data != null)
            {
                Bundle bundle = data.getExtras();

                double wishlistInput = bundle.getDouble("WISHLIST_INPUT");
                boolean addingToItem = bundle.getBoolean("ADDING");

                if (addingToItem)
                {
                    currentBudget = currentBudget - wishlistInput;
                }
                else
                {
                    currentBudget = currentBudget + wishlistInput;
                }

                setBudgetText(currentBudget);
            }
        }

        // Activity Results Conditions for Rollover activity
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Switches activity to get results
        if (id == R.id.action_settings) {

            // Send bundle to settings activity
            Bundle bundle = new Bundle();
            bundle.putDouble("SAVINGS_TOTAL", currentSavingsTotal);
            bundle.putDouble("BUDGET", currentBudget);

            // Build new intent with bundle to send to settings activity
            Intent intent = new Intent(this,Settings.class);
            intent.putExtras(bundle);

            this.startActivityForResult(intent, SETTINGS_REQUEST);
            return true;
        }
        else if (id == R.id.action_wishlist) {
            // Build new intent with bundle to send to wishlist activity
            Intent intent = new Intent(this, Wishlist.class);

            this.startActivityForResult(intent, WISHLIST_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Helper function to set budget value and text
     * @param budgetValue the value to set the current budget text to
     */
    private void setBudgetText(final double budgetValue)
    {
        // Update coloring based on balance
        if(budgetValue < 0)
        {
            // Set budget card text color for negative case
            budget.setTextColor(getResources().getColor(R.color.negative));
            balanceSign.setText(R.string.negative);
            balanceSign.setTextColor(getResources().getColor(R.color.negative));
        }
        else if(budgetValue > 0)
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
        budget.setText(String.valueOf(abs(budgetValue)));
    }

    private void setSavingsText(final double savingsValue)
    {
        if (savingsValue > 0)
        {
            savingsSign.setTextColor(getResources().getColor(R.color.positive));
            savings.setTextColor(getResources().getColor(R.color.positive));
            savings.setText(String.valueOf(savingsValue));
        }
        else
        {
            // If savings value is negative or 0, set it back to default 0.00
            // User is not allowed to have a negative savings
            savingsSign.setTextColor(getResources().getColor(R.color.nil));
            savings.setTextColor(getResources().getColor(R.color.nil));
            savings.setText(R.string.defaultZero);
        }
    }
}
