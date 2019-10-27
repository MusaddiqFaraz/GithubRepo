package com.example.repo.githubapi
import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.repo.db.BuiltByConverter
import com.google.gson.annotations.SerializedName



@Entity(tableName = "repos",
    primaryKeys = ["author", "name"])
data class TrendingRepo(
    @NonNull
    @SerializedName("author")
    var author: String = "",
    @SerializedName("avatar")
    var avatar: String? = "",
    @TypeConverters(BuiltByConverter::class)
    @SerializedName("builtBy")
    var builtBy: List<BuiltBy?>? = emptyList(),
    @SerializedName("currentPeriodStars")
    var currentPeriodStars: Int? = 0,
    @SerializedName("description")
    var description: String? = "",
    @SerializedName("forks")
    var forks: Int? = 0,
    @SerializedName("language")
    var language: String? = "",
    @SerializedName("languageColor")
    var languageColor: String? = "",
    @NonNull
    @SerializedName("name")
    var name: String = "",
    @SerializedName("stars")
    var stars: Int? = 0,
    @SerializedName("url")
    var url: String? = "",
    var timeStamp: Long = System.currentTimeMillis()
) {
    constructor() : this("","", emptyList<BuiltBy>(),0,"",0,"","#000000",
        "",0,"")
}

data class BuiltBy(
    @SerializedName("avatar")
    var avatar: String? = "",
    @SerializedName("href")
    var href: String? = "",
    @SerializedName("username")
    var username: String? = ""
) {
    constructor(): this("","","")
}