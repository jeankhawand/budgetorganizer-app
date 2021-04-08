package com.example.budgetorganizer.data;

import android.provider.BaseColumns;

public class PersonContract {
    // by default BaseColumns implement the _ID string
    public static final class PersonEntry implements BaseColumns {

        public static final String TABLE_NAME = "Person";
        public static final String COLUMN_PERSON_NAME = "Name";
        public static final String COLUMN_BUDGET = "Budget";

    }
}
