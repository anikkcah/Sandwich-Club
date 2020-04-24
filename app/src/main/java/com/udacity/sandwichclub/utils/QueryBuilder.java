package com.udacity.sandwichclub.utils;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/*
 *Builder class for constructing a query for downloading a font
 *
 * Note: Downloading is a better approach as it keeps the apk as compact
 * as possible.
 */
public class QueryBuilder {

    @NonNull
    private String mFamilyName;

    @Nullable
    private Float mWidth = null;

    @Nullable
    private Integer mWeight = null;

    @Nullable
    private Float mItalic = null;

    @Nullable
    private Boolean mBestEffort = null;


    public QueryBuilder(@NonNull String familyName) {
        mFamilyName = familyName;
    }

    public QueryBuilder withFamilyName(@NonNull String familyName) {
        mFamilyName = familyName;
        return this;
    }

    public QueryBuilder withWidth(float width) {
        if (width <= Constants.WIDTH_MIN) {
            throw new IllegalArgumentException("Width must be more than 0");

        }
        mWidth = width;
        return this;
    }

    public QueryBuilder withWeight(int weight) {
        if (weight <= Constants.WEIGHT_MIN || weight >= Constants.WEIGHT_MAX) {
            throw new IllegalArgumentException(
                    "Weight must be between 0 and 1000(exclusive)");
        }
        mWeight = weight;
        return this;

    }

    public QueryBuilder withItalic(float italic) {
        if (italic < Constants.ITALIC_MIN || italic > Constants.ITALIC_MAX) {
            throw new IllegalArgumentException("italic must be between 0 and 1(inclusive).");

        }
        mItalic = italic;
        return this;
    }

    public QueryBuilder withBestEffort(boolean bestEffort) {
        mBestEffort = bestEffort;
        return this;
    }

    public String build() {
        if (mWeight == null && mWidth == null && mItalic == null && mBestEffort == null) {
            return mFamilyName;
        }

        StringBuilder builder = new StringBuilder();

        builder.append("name=").append(mFamilyName);
        if (mWeight != null) {
            builder.append("&weight=").append(mWeight);
        }

        if (mWidth != null) {
            builder.append("&width=").append(mWidth);
        }

        if (mItalic != null) {
            builder.append("&italic=").append(mItalic);
        }

        if (mBestEffort != null) {
            builder.append("&besteffort=").append(mBestEffort);
        }
        return builder.toString();
    }


}
