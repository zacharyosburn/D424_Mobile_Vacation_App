package com.example.d308_app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308_app.entities.Excursion;

import java.util.Collection;
import java.util.List;

@Dao
public interface ExcursionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM EXCURSIONS ORDER BY excursionId ASC")
    List<Excursion> getAllExcursions();

    @Query("SELECT * FROM EXCURSIONS WHERE vacationId=:vaca ORDER BY excursionId ASC")
    List<Excursion> getAssociatedExcursions(int vaca);

    @Query("SELECT IFNULL(SUM(price), 0) FROM excursions WHERE vacationId= :vacationId")
    double getTotalExcursionCost(int vacationId);
}
