package lxnkn.bearoundwithparty;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IStandorteDao {
    @Query("Select * From CStandorte")
    List<CStandorte> getAll();

    @Query("Delete From CStandorte")
    void delete();

    @Insert
    void insert(CStandorte standorte);
}
