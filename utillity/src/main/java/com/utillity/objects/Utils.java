package com.utillity.objects;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.util.ArrayList;

public class Utils {

    public static ArrayList<String> getAssetItems(Context mContext, String categoryName) {
        ArrayList<String> arrayList = new ArrayList<>();
        String[] imgPath;
        AssetManager assetManager = mContext.getAssets();
        try {
            imgPath = assetManager.list(ReplaceSpacialCharacters(categoryName));
            if (imgPath != null) {
                for (String anImgPath : imgPath) {
                    arrayList.add("///android_asset/" + categoryName + "/" + anImgPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static String ReplaceSpacialCharacters(String string) {
        return string.replace(" ", "").replace("&", "").replace("-", "").replace("'", "");
    }

}
