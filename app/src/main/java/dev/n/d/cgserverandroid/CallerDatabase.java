package dev.n.d.cgserverandroid;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Caller.class}, version = 1)
public abstract class CallerDatabase extends RoomDatabase {

    public abstract CallerDao callerDao();

    private static volatile CallerDatabase INSTANCE;

    static CallerDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CallerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CallerDatabase.class, "caller_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
