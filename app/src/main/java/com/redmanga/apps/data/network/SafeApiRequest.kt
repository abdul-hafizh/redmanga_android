package com.redmanga.apps.data.network

import com.google.gson.Gson
import com.redmanga.apps.utils.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.Reader
import java.io.StringWriter


abstract class SafeApiRequest {

    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>) : T{
        val response = call.invoke()

        if(response.isSuccessful){
            return response.body()!!
        }else{
            val error = response.errorBody()?.charStream()
            val message = StringBuilder()
            error.let{
                try{
                    message.append(error?.readText())
//                    message.append(JSONObject(it).getString("message"))
                }catch(e: JSONException){ }
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            throw ApiException(message.toString())
        }
    }

    private fun Reader.readText(): String {
        val buffer = StringWriter()
        copyTo(buffer)
        return buffer.toString()
    }
}