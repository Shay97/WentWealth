package edu.wit.mobileapp.wentwealth;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateWishlistItemActivity extends AppCompatActivity {

    private ImageView iView;
    private EditText price;
    private EditText itemName;
    private Button submitButton;
    private String imageSelected = null;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wishlist_item);

        iView = findViewById(R.id.itemImage);
        price = findViewById(R.id.price);
        itemName = findViewById(R.id.name);
        submitButton = findViewById(R.id.submit);
        fab = findViewById(R.id.wishItemClear);

        iView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageGallery();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.hide();
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemName.getText().toString().matches("") && price.getText().toString().matches(""))
                {
                    Toast.makeText(CreateWishlistItemActivity.this, "No Item Entered", Toast.LENGTH_SHORT).show();
                }
                else if (itemName.getText().toString().matches(""))
                {
                    Toast.makeText(CreateWishlistItemActivity.this, "Item Requires Name", Toast.LENGTH_SHORT).show();
                }
                else if (price.getText().toString().matches(""))
                {
                    Toast.makeText(CreateWishlistItemActivity.this, "Item Requires Price", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    submitNewItem();
                }
            }
        });
    }

    private void submitNewItem() {
        Intent i = new Intent();
        String name = itemName.getText().toString();
        int priceP = Integer.parseInt(price.getText().toString());
        i.putExtra("ITEM_NAME",name);
        i.putExtra("ITEM_PRICE",priceP);
        i.putExtra("ITEM_IMAGE",imageSelected);
        setResult(RESULT_OK,i);
        finish();
    }

    private void openImageGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});



        startActivityForResult(chooserIntent, 5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 5) {

        }
    }
}
