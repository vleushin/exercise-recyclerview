package in.leush.exercise.recyclerview.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public interface Contract {
    String AUTHORITY = "in.leush.excerice";

    Uri AUTHORITY_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    String SQL_CREATE_TABLE_MESSAGES = "CREATE TABLE " + MessagesEntry.TABLE_NAME + " (" +
            MessagesEntry.COLUMN_NAME_MESSAGE + " TEXT NOT NULL," +
            MessagesEntry.COLUMN_NAME_CREATED_TIME + " INTEGER NOT NULL);";

    String[] SQL_CREATE_TABLES = new String[]{
            SQL_CREATE_TABLE_MESSAGES,
    };

    String SQL_DROP_TABLE_MESSAGES = "DROP TABLE IF EXISTS " + MessagesEntry.TABLE_NAME;
    String[] SQL_DROP_TABLES = new String[]{
            SQL_DROP_TABLE_MESSAGES
    };

    interface MessagesEntry {
        String TABLE_NAME = "messages";
        String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.in.leush.exercise." + TABLE_NAME;

        String COLUMN_NAME_MESSAGE = "message";
        String COLUMN_NAME_CREATED_TIME = "created_time";

        String ORDER_BY_CREATED_TIME_DESC = "created_time DESC";

        Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_CONTENT_URI, TABLE_NAME);
    }

    class DbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 2;
        public static final String DATABASE_NAME = "messages.db";

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public static void deleteDatabase(Context context) {
            context.deleteDatabase(DATABASE_NAME);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for (String sqlCreateTable : SQL_CREATE_TABLES) {
                db.execSQL(sqlCreateTable);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            for (String sqlDropTable : SQL_DROP_TABLES) {
                db.execSQL(sqlDropTable);
            }
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
