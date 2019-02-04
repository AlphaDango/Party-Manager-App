package lxnkn.bearoundwithparty;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {CPartys.class,CStandorte.class},version =1)
@TypeConverters(Converters.class)
public abstract class Database_bearound extends RoomDatabase {
    public abstract IPartysDao partyDao();
    public abstract IStandorteDao standorteDao();
}
