package edu.wit.mobileapp.wentwealth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {

    // Variable to hold database
    DatabaseHelper myDb;

    // Variables for handling requests
    public static int ADD_MENU_REQUEST = 1;
    public static int ROLLOVER_REQUEST = 2;
    public static int SETTINGS_REQUEST = 3;
    public static int WISHLIST_REQUEST = 4;

    // Global variables containing information main activity views
    private FloatingActionButton fab;
    private TextView budget;
    private TextView savings;
    private TextView balanceSign;
    private TextView savingsSign;
    private TextView savingsTotal;
    private TextView rollover;
    private double currentBudget;
    private double currentSavings;
    private int currentSavingsTotal;
    private double currentRollover;

    private ArrayList<WishlistItemObject> wishListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = new DatabaseHelper(this);


        // Get the current values of budget and savings.
        budget = ((TextView)findViewById(R.id.currentBalance));
        savings = ((TextView)findViewById(R.id.currentSavings));

        balanceSign = ((TextView)findViewById(R.id.balanceSign));
        savingsSign = ((TextView)findViewById(R.id.savingsSign));

        savingsTotal = ((TextView)findViewById(R.id.savingsGoal));

        rollover = ((TextView)findViewById(R.id.currentRollover));

        currentBudget = Double.parseDouble(budget.getText().toString());
        currentSavings = Double.parseDouble(savings.getText().toString());
        currentSavingsTotal = Integer.parseInt(savingsTotal.getText().toString());
        currentRollover = Double.parseDouble(rollover.getText().toString());

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


        final Button rolloverBtn = ((Button)findViewById(R.id.button));
        rolloverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putDouble("ROLLOVER", currentRollover);

                Intent intent = new Intent(getApplicationContext(), Allocate.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, ROLLOVER_REQUEST);
            }
        });

        // Load information from database
        loadDB();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList("wishlist", wishListItems);
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

                    // Remove from Budget
                    currentBudget = currentBudget - amount;

                    // Set the savings text to update current savings
                    setSavingsText(currentSavings);

                    // Update budget
                    setBudgetText(currentBudget);
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

                currentSavingsTotal = bundle.getInt("SAVINGS_TOTAL");
                double updatedBudget = bundle.getDouble("BUDGET");

                // Set Budget text
                currentBudget = currentBudget + updatedBudget;
                setBudgetText(currentBudget);

                // Set Savings Total Text
                savingsTotal.setText(String.valueOf(currentSavingsTotal));
            }
        }
        // Activity Results Condition for Wishlist activity
        else if (requestCode == WISHLIST_REQUEST && resultCode == RESULT_OK)
        {
            if (data != null)
            {
                Bundle bundle = data.getExtras();

                wishListItems = bundle.getParcelableArrayList("wishlist");

                double withdraw = bundle.getDouble("WISHLIST_WITHDRAW");
                double deposit = bundle.getDouble("WISHLIST_DEPOSIT");

                currentBudget = currentBudget + deposit - withdraw;
                setBudgetText(currentBudget);
            }
        }
        // Activity Results Conditions for Rollover activity
        else if (requestCode == ROLLOVER_REQUEST && resultCode == RESULT_OK)
        {
            if (data != null)
            {
                Bundle bundle = data.getExtras();
                double amount = bundle.getDouble("AMOUNT");
                int type = bundle.getInt("TYPE");

                if (type == R.id.savingOption)
                {
                    currentSavings = currentSavings + amount;
                    setSavingsText(currentSavings);
                }
                else
                {
                    currentBudget = currentBudget + amount;
                    setBudgetText(currentBudget);
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

            if (wishListItems == null)
            {
                wishListItems = wishListItems = new ArrayList<WishlistItemObject>();
            }

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("wishlist", wishListItems);

            Intent intent = new Intent(this, Wishlist.class);
            intent.putExtras(bundle);
            this.startActivityForResult(intent, WISHLIST_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveDB() {

        if (wishListItems == null)
        {
            wishListItems = wishListItems = new ArrayList<WishlistItemObject>();
        }


        Gson gson = new Gson();
        String json = gson.toJson(wishListItems);

        boolean isInserted = myDb.insertData(
                String.valueOf(currentBudget),
                String.valueOf(currentSavings),
                String.valueOf(currentSavingsTotal),
                String.valueOf(currentRollover),
                json
        );

        if (isInserted == true)
        {
            Toast.makeText(MainActivity.this, "Data Saved!", Toast.LENGTH_SHORT).show();
        }

    }

    public void loadDB() {
        Cursor res = myDb.getAllData();

        if(res.getCount() != 0)
        {
            while(res.moveToNext())
            {
                currentBudget = Double.parseDouble(res.getString(0));
                currentSavings = Double.parseDouble(res.getString(1));
                currentSavingsTotal = Integer.parseInt(res.getString(2));
                currentRollover = Double.parseDouble(res.getString(3));

                Gson gson = new Gson();
                String json = res.getString(4);
                Type type = new TypeToken<ArrayList<WishlistItemObject>>() {}.getType();
                wishListItems = gson.fromJson(json, type);

                if (wishListItems == null)
                {
                    wishListItems = new ArrayList<WishlistItemObject>();
                }

                setBudgetText(currentBudget);
                setSavingsText(currentSavings);
                savingsTotal.setText(String.valueOf(currentSavingsTotal));
                rollover.setText(String.valueOf(currentRollover));
            }
        }
    }

    /**
     * Helper function to set budget value text and text color
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
        if (budgetValue != 0)
        {
            budget.setText(String.valueOf(abs(budgetValue)));
        }
        else
        {
            budget.setText(getString(R.string.defaultZero));
        }

        // Save changes into shared preferences
        saveDB();
    }

    /**
     * Helper function to set the savings value text and text color
     * @param savingsValue the value to set the current savings budget text to
     */
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

        saveDB();
    }
}
