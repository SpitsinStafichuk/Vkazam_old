
package com.github.spitsinstafichuk.vkazam.daos;

import android.database.sqlite.SQLiteDatabase;

public class FingerprintTable {

    public static final String FINGERPRINTS = "Fingerprints";

    public static final String _ID = "_id";

    public static final String FINGERPRINT = "Fingerprint";

    public static final String DATE = "Date";

    public static final String DELETION_DATE = "DeletionDate";

    @SuppressWarnings("unused")
    public static void create(SQLiteDatabase database) {
        final String createStatement = "CREATE TABLE " + FINGERPRINTS + "(" +
                _ID + " integer primary key, " +
                FINGERPRINT + " text, " +
                DATE + " integer, " +
                DELETION_DATE + " integer)";
        database.execSQL(createStatement);
    }

}
