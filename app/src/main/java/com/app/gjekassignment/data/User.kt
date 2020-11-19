package com.app.gjekassignment.data

import androidx.room.*
import com.app.gjekassignment.utils.toJson
import com.app.gjekassignment.utils.toTypeOf

@Entity(tableName = "users")
data class User(@PrimaryKey(autoGenerate = true) val id: Int,
                @field:TypeConverters(NameConverter::class) val name: Name,
                @field:TypeConverters(LocationConverter::class) val location: Location,
                val email: String,
                val password: String,
                val dob: String,
                val phone: String?,
                @ColumnInfo(name = "cell_phone") val cellPhone: String?,
                val picture: String)

data class Name(var first: String, val last: String) {
   val fullName get() = first + last
}

data class Location(val street: String, val city: String)

class NameConverter {
   @TypeConverter
   fun fromNameToJson(name: Name?): String? = name?.toJson()
   
   @TypeConverter
   fun fromJsonToName(json: String?): Name? = json?.toTypeOf<Name>()
}

class LocationConverter {
   @TypeConverter
   fun fromLocationToJson(location: Location?): String? = location?.toJson()
   
   @TypeConverter
   fun fromJsonToLocation(json: String?): Location? = json?.toTypeOf()
}