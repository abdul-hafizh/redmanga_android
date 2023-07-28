package com.redmanga.apps.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.redmanga.apps.R
import com.redmanga.apps.databinding.DialogProgressBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


fun showToast(context: Context,message: String){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}
fun displayImage(
    ctx: Context,
    img: ImageView,
    url: String = "",
    type: Int = 0
) {
    if (type == 0) {
        Glide.with(ctx)
            .load("https://redmangaonline.000webhostapp.com/api/public/cover/$url")
            .centerCrop()
            .transition(DrawableTransitionOptions().crossFade(2000))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .into(img)
    } else {
        Glide.with(ctx)
            .load("https://redmangaonline.000webhostapp.com/api/public/chapters/$url")
            .centerInside()
            .transition(DrawableTransitionOptions().crossFade(2000))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .into(img)
    }

}

fun convertDate(tanggal: String, type: Int): String? {
    val formatSatu = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formatDua = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val formatTiga = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val dateTime: String?
    dateTime = when (type) {
        1 -> {
            val date: Date = formatSatu.parse(tanggal)!!
            formatDua.format(date)
        }
        2 -> {
            val date: Date = formatSatu.parse(tanggal)!!
            formatTiga.format(date)
        }
        3 -> {
            val date: Date = formatDua.parse(tanggal)!!
            formatSatu.format(date)
        }
        else -> ""
    }
    return dateTime
}

private var dialog: Dialog? = null
fun showDialog(activity: Activity) {
    dialog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Dialog(activity, R.style.Dialog)
    } else {
        Dialog(activity)
    }
    dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
    val binding: DialogProgressBinding = DialogProgressBinding.inflate(activity.layoutInflater)
    dialog!!.setContentView(binding.root)
    dialog!!.setCancelable(false)
    dialog!!.setCanceledOnTouchOutside(false)
    val lp = WindowManager.LayoutParams()
    lp.copyFrom(dialog!!.window!!.attributes)
    lp.width = WindowManager.LayoutParams.WRAP_CONTENT
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
    dialog!!.window!!.attributes = lp
    dialog!!.show()
}

fun hideDialog() {
    if (dialog!!.isShowing) {
        dialog!!.dismiss()
    }
}

fun showInputError(inputLayout: TextInputLayout, data: String, message: String): Boolean {
    if (data.isEmpty()) {
        inputLayout.error = "$message harap diisi"
        return false
    }
    return true
}

fun showEditextError(
    inputLayout: TextInputLayout,
    inputEditText: TextInputEditText,
    message: String
) {
    inputEditText.addTextChangedListener(object : TextWatcher {
        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
        }

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (inputEditText.text.toString().isEmpty()) {
                inputLayout.error = "$message harap diisi"
            } else {
                inputLayout.error = null
            }
        }
    })
}

fun openCalender(
    context: Context?,
    inputEditText: TextInputEditText
) {
    val myCalendar = Calendar.getInstance()
    val date =
        OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            val sdf =
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            inputEditText.setText(sdf.format(myCalendar.time))
        }
    inputEditText.setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            val dialog = DatePickerDialog(
                context!!,
                R.style.DatePickerTheme,
                date,
                myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }
    inputEditText.setOnClickListener {
        val dialog = DatePickerDialog(
            context!!,
            R.style.DatePickerTheme,
            date,
            myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}

fun displayImageOriginal(
    ctx: Context?,
    img: ImageView?,
    path: String?,
    width: Int,
    height: Int
) {
    Glide.with(ctx!!)
        .load(path)
        //.error(R.drawable.ic_error)
        .centerInside()
        .transition(DrawableTransitionOptions().crossFade(2000))
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .override(width, height)
        .into(img!!)
}

fun requestStatus(activity: Activity, spinner: AppCompatSpinner) {
//    val dataStatus = activity.resources.getStringArray(R.array.status_manga)
    val myList: Array<String> = activity.resources.getStringArray(R.array.status_manga)
    val arrayAdapterStatus: ArrayAdapter<String> =
        ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, myList)
    arrayAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapterStatus
}

fun saveBitmapToFile(file: File): File? {
    return try {
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        o.inSampleSize = 6
        var inputStream = FileInputStream(file)
        BitmapFactory.decodeStream(inputStream, null, o)
        inputStream.close()
        val requiredSize = 75
        var scale = 1
        while (o.outWidth / scale / 2 >= requiredSize &&
            o.outHeight / scale / 2 >= requiredSize
        ) {
            scale *= 2
        }
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        inputStream = FileInputStream(file)
        val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
        inputStream.close()
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        assert(selectedBitmap != null)
        selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        file
    } catch (e: Exception) {
        null
    }
}

fun getPathFromUri(context: Context, uri: Uri): String? {
    val isLolipop: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    if (isLolipop && DocumentsContract.isDocumentUri(context, uri)) {
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageState()
                    .toString() + "/" + split[1]
            }

            // TODO handle non-primary volumes
        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(id)
            )
            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "audio" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(
                split[1]
            )
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
            context,
            uri,
            null,
            null
        )
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }

    return null
}

fun getDataColumn(
    context: Context, uri: Uri?, selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
        column
    )

    try {
        cursor = context.contentResolver.query(
            uri!!, projection, selection, selectionArgs,
            null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}


fun getDateNow(): String? {
    val calendar = Calendar.getInstance()
    val dateFormat =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(calendar.time)
}


