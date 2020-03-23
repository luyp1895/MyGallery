package com.example.gallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val _photoListLive = MutableLiveData<List<PhotoItem>>()
    val photoListLive : LiveData<List<PhotoItem>>
        get()  = _photoListLive

    fun generateData(){
        val stringRequest = StringRequest(Request.Method.GET,gerUrl(),
            Response.Listener {
                 _photoListLive.value = Gson().fromJson(it, PixaBay::class.java).hits.toList()
            }
            ,Response.ErrorListener {
                Log.d("error",it.toString()) })
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }

    private fun gerUrl() = "https://pixabay.com/api/?key=15692633-b1ae233c38b5488a022a3c3fa&q=${keyWords.random()}&per_page=100"

    private val keyWords = arrayOf("cat","dog","pig","pet","space","DOTA2","beauty","sexy","cute","food","sad","building"
    ,"snow","flower")
}