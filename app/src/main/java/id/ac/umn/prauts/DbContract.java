package id.ac.umn.prauts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class DbContract {
    static class Entry implements BaseColumns {
        static final String TABLE_NAME = "users";
        static final String COLUMN_NAME_USERNAME = "username";
        static final String COLUMN_NAME_PASSWORD = "password";
        static final String COLUMN_NAME_ROLE     = "role";
    }

    static final String SQL_CREATE_MEMBER = String.format(
            "CREATE TABLE %s(%s, %s, %s, %s)",
            Entry.TABLE_NAME,
            String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT", Entry._ID),
            String.format("%s VARCHAR(100)", Entry.COLUMN_NAME_USERNAME),
            String.format("%s VARCHAR(100)", Entry.COLUMN_NAME_PASSWORD),
            String.format("%s VARCHAR(100)", Entry.COLUMN_NAME_ROLE)
    );
    static final String SQL_DELETE_MEMBER = String.format(
            "DROP TABLE IF EXISTS %s",
            Entry.TABLE_NAME
    );

    private DbContract() {
    }

    public static class DbHelper extends SQLiteOpenHelper {
        static final int DATABASE_VERSION = 1;
        static final String DATABASE_NAME = "member.db";

        DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_MEMBER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_MEMBER);
            onCreate(db);
        }
    }
}
