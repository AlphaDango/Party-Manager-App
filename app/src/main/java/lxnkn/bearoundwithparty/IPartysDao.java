package lxnkn.bearoundwithparty;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.sql.Date;

import java.util.List;

@Dao
public interface IPartysDao {
    @Query("select * from CPartys WHERE standort_uid = (:standort_uid) order by datum asc")
    List<CPartys> getAll(int standort_uid);

    @Insert
    void insert(CPartys party);

    @Query("Delete from CPartys")
    void delete();

}
