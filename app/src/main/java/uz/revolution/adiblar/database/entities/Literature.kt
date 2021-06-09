package uz.revolution.adiblar.database.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "literature")
class Literature : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var name: String? = null
    var years: String? = null
    var type: String? = null
    var text: String? = null
    var image: String? = null
    var isSaved: Boolean? = null

    constructor()

    constructor(
        id: Int?,
        name: String?,
        years: String?,
        type: String?,
        text: String?,
        image: String?,
        isSaved: Boolean?
    ) {
        this.id = id
        this.name = name
        this.years = years
        this.type = type
        this.text = text
        this.image = image
        this.isSaved = isSaved
    }

    constructor(
        name: String?,
        years: String?,
        type: String?,
        text: String?,
        image: String?,
        isSaved: Boolean?
    ) {
        this.name = name
        this.years = years
        this.type = type
        this.text = text
        this.image = image
        this.isSaved = isSaved
    }

}