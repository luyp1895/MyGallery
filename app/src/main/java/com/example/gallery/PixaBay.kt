package com.example.gallery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class PixaBay(
    val total:Int,
    val totalHits:Int,
    val hits:Array<PhotoItem>
) {
}

@Parcelize data class PhotoItem(
    @SerializedName("webformatURL") val previewUrl:String,
    @SerializedName("largeImageURL") val fullUrl:String,
    val id:Int
):Parcelable