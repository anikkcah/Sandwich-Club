package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.FontsContractCompat;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;
import com.udacity.sandwichclub.utils.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "DetailActivity.java ";

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    //defining the variables used in this class---------------------------

    private Handler mHandler = null;

    ImageView ingredientsIv;
    TextView mainNameTv;
    TextView alsoKnownAsTv;
    TextView descriptionTv;
    TextView ingredientsTv;
    TextView placeOfOriginTv;

    String strDesc = "";
    String strOrigin = " ";
    String strPlaceOfOrigin = "";
    List<String> lstrAlsoKnownAs = new ArrayList<>();
    List<String> lstrIngredients = new ArrayList<>();

    //---------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ingredientsIv = findViewById(R.id.image_iv);


        mainNameTv = findViewById(R.id.origin_tv);
        alsoKnownAsTv = findViewById(R.id.also_known_tv);
        descriptionTv = findViewById(R.id.description_tv);
        ingredientsTv = findViewById(R.id.ingredients_tv);
        placeOfOriginTv = findViewById(R.id.place_of_origin_tv);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }


        assert intent != null;
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }


        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());

        //calling the getter methods on sandwichObject
        strDesc = sandwich.getDescription();
        strOrigin = sandwich.getMainName();
        lstrAlsoKnownAs = sandwich.getAlsoKnownAs();
        lstrIngredients = sandwich.getIngredients();
        strPlaceOfOrigin = sandwich.getPlaceOfOrigin();


        requestDownload();
        requestDownloadNew();


        populateUI();

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {

        mainNameTv.setText(strOrigin);
        descriptionTv.setText(strDesc);
        alsoKnownAsTv.setText(listToString(lstrAlsoKnownAs));
        ingredientsTv.setText(listToString(lstrIngredients));
        placeOfOriginTv.setText(strPlaceOfOrigin);

    }

    //This is a utility function to change  list values into a String with
    // a delimiter (" , ")

    private String listToString(List<String> list) {
        String res;
        String delim = " , ";

        StringBuilder sb = new StringBuilder();
        int listSize = list.size();

        //check if the list is empty, then return a null string
        if (listSize == 0) {
            return "";
        }

        //Now listSize >= 1
        int i = 0;
        while (i < listSize - 1) {
            sb.append(list.get(i));
            sb.append(delim);
            i++;
        }
        sb.append(list.get(i));

        res = sb.toString();
        return res;
    }


    private Handler getHandlerThreadHandler() {
        if (mHandler == null) {
            HandlerThread handlerThread = new HandlerThread("fonts");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
        }
        return mHandler;
    }


    //This method does  FontStyling to the textView referring to "MainName"
    // or typically the Header of our Layout.
    //
    private void requestDownload() {

        //defining the "Main Name" TextView as 'final' as we want it to be unchanged
        //after applying new text styling
        final TextView originalTv;
        originalTv = findViewById(R.id.origin_tv);
        originalTv.setText(strOrigin);

        //FontFamily
        String familyName = "Lobster";


        /* It is an object of the QueryBuilder class.
         * Used to make Query for FontRequest class specifying the
         * familyName(required),
         * width(optional),
         * weight(optional),
         * italic(optional),
         * bestEffort(optional.
         *
         * Syntax: QueryBuilder(familyName)
         *              .withWidth()
         *              .withWeight();
         *              .withItalic();
         *              .withBestEffort();
         *
         */
        QueryBuilder queryBuilder = new QueryBuilder(familyName);
           /*     .withWidth()
                .withWeight()
                .withItalic()
                .withBestEffort();*/


        String query = queryBuilder.build();      //generated the query

        Log.d(LOG_TAG, "Requesting a font. Query: " + familyName);


        //For the first Request
        FontRequest request = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                query,
                R.array.com_google_android_gms_fonts_certs
        );


        FontsContractCompat.FontRequestCallback callback = new FontsContractCompat
                .FontRequestCallback() {

            @Override
            public void onTypefaceRetrieved(Typeface typeface) {

                originalTv.setTypeface(typeface);


            }

            @Override
            public void onTypefaceRequestFailed(int reason) {
                Toast.makeText(DetailActivity.this, getString(R.string.request_failed, reason), Toast.LENGTH_LONG).show();
            }


        };

        FontsContractCompat.requestFont(DetailActivity.this, request, callback, getHandlerThreadHandler());


    }


    //This method does FontStyling to the other textViews() displaying information
    // about the selected Sandwich.

    private void requestDownloadNew() {

        //Here defining the other TextViews(containing data from the parsed JSON) as final.
        //descriptionTv,
        //alsoKnownAsTv,
        //ingredientsTv,
        //placeOfOriginTv

        final TextView descriptionTv;
        descriptionTv = findViewById(R.id.description_tv);
        descriptionTv.setText(strDesc);

        final TextView alsoKnownAsTv;
        alsoKnownAsTv = findViewById(R.id.also_known_tv);
        alsoKnownAsTv.setText(listToString(lstrAlsoKnownAs));

        final TextView ingredientsTv;
        ingredientsTv = findViewById(R.id.ingredients_tv);
        ingredientsTv.setText(listToString(lstrIngredients));

        final TextView placeOfOriginTv;
        placeOfOriginTv = findViewById(R.id.place_of_origin_tv);
        placeOfOriginTv.setText(strPlaceOfOrigin);


        //defined the fontFamily
        String familyName = "Roboto Slab";


        /* It is an object of the QueryBuilder class.
         * Used to make Query for FontRequest class specifying the
         * familyName(required),
         * width(optional),
         * weight(optional),
         * italic(optional),
         * bestEffort(optional)
         *
         * Syntax: QueryBuilder(familyName)
         *              .withWidth()
         *              .withWeight();
         *              .withItalic();
         *              .withBestEffort();
         *
         * For specifying the Width, Weight, Italic and BestEffort parameter values
         * check the "Constants" class.
         *
         */

        QueryBuilder queryBuilder = new QueryBuilder(familyName);
           /*     .withWidth()
                .withWeight()
                .withItalic()
                .withBestEffort();*/


        String query = queryBuilder.build();                                //generated the query

        Log.d(LOG_TAG, "Requesting a font. Query: " + familyName);


        FontRequest request = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                query,
                R.array.com_google_android_gms_fonts_certs
        );


        FontsContractCompat.FontRequestCallback callback = new FontsContractCompat
                .FontRequestCallback() {

            @Override
            public void onTypefaceRetrieved(Typeface typeface) {

                descriptionTv.setTypeface(typeface);
                ingredientsTv.setTypeface(typeface);
                placeOfOriginTv.setTypeface(typeface);
                alsoKnownAsTv.setTypeface(typeface);


            }

            @Override
            public void onTypefaceRequestFailed(int reason) {
                Toast.makeText(DetailActivity.this, getString(R.string.request_failed, reason), Toast.LENGTH_LONG).show();
            }


        };

        FontsContractCompat.requestFont(DetailActivity.this, request, callback, getHandlerThreadHandler());

    }


}

