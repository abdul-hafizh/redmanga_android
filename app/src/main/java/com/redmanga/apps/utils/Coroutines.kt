package com.redmanga.apps.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutines {

    var context = MutableLiveData<Context>()

    fun main(work: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            try {
                work()
            } catch (ex: Exception) {
                Log.e("Corountines", "Error ${ex.message}")
                Toast.makeText(context.value, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    fun io(work: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                work()
            } catch (ex: Exception) {
                Log.e("Corountines", "Error ${ex.message}")
                Toast.makeText(context.value, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

}