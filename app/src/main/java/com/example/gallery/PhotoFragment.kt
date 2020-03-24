package com.example.gallery


import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.photo_full_cell.view.*
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 */
class PhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val photoList = arguments?.getParcelableArrayList<PhotoItem>("photolist")
        ViewPagerAdapter().apply {
            ViewPager.adapter = this
            submitList(photoList)
        }



        ViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                textViewPhoto.text =
                    getString(R.string.photo_number_of_total, position + 1, photoList?.size)
            }
        })
        ViewPager.setCurrentItem(arguments?.getInt("currentNumber") ?: 0, false)
        savePhotoBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    WRITEEXTERNALSTORAGE_CODE
                )
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    savePhoto()
                }
            }
        }
    }

        private suspend fun savePhoto() {
        withContext(Dispatchers.IO){
            val holder = ((ViewPager[0] as RecyclerView).findViewHolderForAdapterPosition(
                ViewPager.currentItem
            )) as FullPagerViewHolder
            val bitmap = holder.itemView.photoView.drawable.toBitmap()
            val saveUri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , ContentValues()
            )?: kotlin.run {
                MainScope().launch {
                    Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show()
                }
                return@withContext
            }
            requireContext().contentResolver.openOutputStream(saveUri).use {
                if(bitmap.compress(Bitmap.CompressFormat.JPEG,90,it)){
                    MainScope().launch {
                        Toast.makeText(requireContext(), "存储成功", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    MainScope().launch {
                        Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
//    private suspend fun savePhoto() {
//        val holder = ((ViewPager[0] as RecyclerView).findViewHolderForAdapterPosition(
//            ViewPager.currentItem
//        )) as FullPagerViewHolder
//        val bitmap = holder.itemView.photoView.drawable.toBitmap()
//        val saveUri = requireContext().contentResolver.insert(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            , ContentValues()
//        ) ?: kotlin.run {
//            MainScope().launch {
//                Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show()
//            }
//            return
//        }
//        requireContext().contentResolver.openOutputStream(saveUri).use {
//            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)) {
//                    GlobalScope.launch (Dispatchers.Main){
//                        Toast.makeText(requireContext(), "存储成功", Toast.LENGTH_SHORT).show()
//                    }
//
//
//            } else {
//                GlobalScope.launch(Dispatchers.Main) {
//                    Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITEEXTERNALSTORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GlobalScope.launch(Dispatchers.IO) {
                        savePhoto()
                    }
                } else {
                    Toast.makeText(requireContext(), "请求权限失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
