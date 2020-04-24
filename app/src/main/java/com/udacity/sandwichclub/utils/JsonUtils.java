package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String LOG_TAG = "JsonUtils.java: ";


    public static Sandwich parseSandwichJson(String json) {

        //creating new sandwich object
        Sandwich sandwichObject = new Sandwich();

        String strMain = " ";
        String strDesc = " ";
        String strPlaceOfOrigin = " ";
        String strImage = " ";

        List<String> IsIngredients = new ArrayList<String>();
        List<String> IsAlsoKnownAs = new ArrayList<String>();

        try {
            JSONObject joMain = new JSONObject(json);
            JSONObject joSandwich = joMain.getJSONObject("name");

            strMain = joSandwich.getString("mainName");
            strDesc = joMain.getString("description");
            strPlaceOfOrigin = joMain.getString("placeOfOrigin");
            strImage = joMain.getString("image");


            JSONArray jaAlsoKnownAs = joSandwich.getJSONArray("alsoKnownAs");

            for (int i = 0; i < jaAlsoKnownAs.length(); i++) {
                String strAlsoKnownAs = jaAlsoKnownAs.getString(i);
                IsAlsoKnownAs.add(strAlsoKnownAs);
            }

            JSONArray jaIngredients = joMain.getJSONArray("ingredients");

            for (int i = 0; i < jaIngredients.length(); i++) {
                String strIngredients = jaIngredients.getString(i);
                IsIngredients.add(strIngredients);
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the sandwich JSON results", e);
        }

        //Store the JSON value into new Sandwich object created
        sandwichObject.setMainName(strMain);
        sandwichObject.setIngredients(IsIngredients);
        sandwichObject.setAlsoKnownAs(IsAlsoKnownAs);
        sandwichObject.setDescription(strDesc);
        sandwichObject.setImage(strImage);
        sandwichObject.setPlaceOfOrigin(strPlaceOfOrigin);

        return sandwichObject;
    }
}
