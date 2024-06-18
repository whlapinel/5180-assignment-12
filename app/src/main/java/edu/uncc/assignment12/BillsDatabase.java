package edu.uncc.assignment12;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Bill.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class BillsDatabase extends RoomDatabase {

    public abstract BillsDao billsDao();

}
