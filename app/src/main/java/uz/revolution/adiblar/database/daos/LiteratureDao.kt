package uz.revolution.adiblar.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.revolution.adiblar.database.entities.Literature

@Dao
interface LiteratureDao {

    @Insert
    fun insertLiterature(literature: Literature)

    @Query("select * from literature")
    fun getAllLiterature(): List<Literature>

    @Query("delete from literature where id=:id")
    fun deleteLiterature(id: Int)

    @Query("select * from literature where id=:id")
    fun getLiterature(id: Int): Literature

    @Query("update literature set isSaved=:isSaved where id=:id")
    fun updateLiterature(isSaved: Boolean, id: Int)

    @Query("select * from literature where name like :name")
    fun getLiteratureByName(name: String): Literature
}