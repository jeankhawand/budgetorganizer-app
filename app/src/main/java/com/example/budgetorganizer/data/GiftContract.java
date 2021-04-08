package com.example.budgetorganizer.data;

import android.provider.BaseColumns;

public class GiftContract {
    // by default BaseColumns implement the _ID string
    public static final class GiftEntry implements BaseColumns {

        public static final String TABLE_NAME = "Gift";
        public static final String COLUMN_GIFT_NAME_NAME = "Name";
        public static final String COLUMN_PRICE_NAME = "Price";
        public static final String COLUMN_PERSON_ID_NAME = "PersonID";
        public static final String COLUMN_PHOTOPATH_NAME = "PhotoPath";
        public static final String COLUMN_DATE_NAME = "Date";

    }
}
