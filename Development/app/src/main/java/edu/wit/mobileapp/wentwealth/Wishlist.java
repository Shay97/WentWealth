package edu.wit.mobileapp.wentwealth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

public class Wishlist extends AppCompatActivity implements WishlistItemAdapter.OnItemClickListener {

    private RecyclerView rView;
    private FloatingActionButton addBtn;
    private WishlistItemAdapter adapter;
    private ArrayList<WishlistItemObject> listItems;
    private AlertDialog alertBox;

    private String WISHLIST_ITEMS = "wishlist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wishlist);

        // TOOLBAR ACTIONS
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.title_activity_wishlist));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (savedInstanceState == null)
        {
            listItems = new ArrayList<WishlistItemObject>();
        }
        else
        {
            listItems = savedInstanceState.getParcelableArrayList(WISHLIST_ITEMS);
        }

        rView = findViewById(R.id.rView);
        addBtn = findViewById(R.id.addBtn);
        adapter = new WishlistItemAdapter(this, listItems,this);
        rView.setAdapter(adapter);

        LinearLayoutManager man = new LinearLayoutManager(this);
        man.setOrientation(LinearLayoutManager.VERTICAL);
        rView.setLayoutManager(man);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBtn.hide();
                Intent i = new Intent(Wishlist.this, CreateWishlistItemActivity.class);
                startActivityForResult(i,1);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(WISHLIST_ITEMS, listItems);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addBtn.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            String name = data.getStringExtra("ITEM_NAME");
            int price = data.getIntExtra("ITEM_PRICE", 0);
            String image = data.getStringExtra("ITEM_IMAGE");
            WishlistItemObject hold = new WishlistItemObject(image,name,price,0);

            listItems.add(hold);
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onItemClicked(final int index, WishlistItemViewHolder viewHolder) {

        PopupMenu popup = new PopupMenu(Wishlist.this, viewHolder.itemView);
        popup.getMenuInflater().inflate(R.menu.wishlist_item_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.withdrawM:
                        showWithdrawMoneyDialog(index);
                        break;

                    case R.id.addM:
                        showAddMoneyDialog(index);
                        break;

                    case R.id.removeI:
                        listItems.remove(index);
                        adapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
        popup.show();

    }

    private void showAddMoneyDialog(final int index){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deposit Money");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int inputMoney = Integer.parseInt(input.getText().toString());
                WishlistItemObject current = listItems.get(index);

                if(inputMoney > (current.getValue()- current.getrBalance())){
                    inputMoney = (current.getValue()) - (current.getrBalance());
                }

                current.setrBalance(current.getrBalance() + inputMoney);
                adapter.notifyDataSetChanged();

                Bundle bundle = new Bundle();
                bundle.putDouble("WISHLIST_INPUT", inputMoney);
                bundle.putBoolean("ADDING", true);

                Intent intent = new Intent(Wishlist.this, MainActivity.class);
                intent.putExtras(bundle);

                setResult(RESULT_OK, intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertBox = builder.create();
        alertBox.show();
    }

    private void showWithdrawMoneyDialog(final int index){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Withdraw Money");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int inputMoney = Integer.parseInt(input.getText().toString());
                WishlistItemObject current = listItems.get(index);

                if(inputMoney > current.getrBalance()){
                    inputMoney = (current.getrBalance());
                }

                current.setrBalance(current.getrBalance() - inputMoney);
                adapter.notifyDataSetChanged();

                Bundle bundle = new Bundle();
                bundle.putDouble("WISHLIST_INPUT", inputMoney);
                bundle.putBoolean("ADDING", false);

                Intent intent = new Intent(Wishlist.this, MainActivity.class);
                intent.putExtras(bundle);

                setResult(RESULT_OK, intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertBox = builder.create();
        alertBox.show();
    }
    //on addBtn click - image, item name, item amount
    //add item

    //popout menu (on ListItemObject click) --- add money - remove money - remove item

}
