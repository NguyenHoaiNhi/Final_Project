package com.example.translate

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var mHandler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mHandler = Handler()
        translate.setOnClickListener{
            if(English.text == "English"){
                English.text = "Vietnamese"
                text2.text = "English"
                tvEnglish.text = "Vietnamese"
                tvVietnamese.text = "English"
            }else{
                English.text = "English"
                text2.text = "Vietnamese"
                tvEnglish.text = "English"
                tvVietnamese.text = "Vietnamese"
            }
        }
        btTranslate.setOnClickListener{
            if(tvEnglish.text == "English"){
                var language1 = "en-vi"
                translation(language1)
            }else{
                var language2 = "vi-en"
                translation(language2)
            }


        }

    }
    private fun translation(lang: String){

        var text = edEnglish.text
        var link= "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20190523T053835Z.0b61113b08a5ff52.f4eccc205a7332836a2a4a2444997625d673ad3a&lang="+lang+"&text="+text
        okhttp(link)
    }
    fun okhttp( a: String){

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(a)
            .build()
        client.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    print("Fail load data")
                }
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful){
                        var json = response.body()!!.string()
                        Log.i("JSON", json.toString())
                        var jsObect = JSONObject(json)
                        var result = jsObect.getJSONArray("text").toString()
                        var collectionType = object : TypeToken<Collection<String>>() {}.type
                        var word: ArrayList<String> = Gson().fromJson(result, collectionType)
                        Log.i("Data: ", word.toString())
                        mHandler.post(Runnable {
                            edVietnam.setText(word.get(0).toString())
                        })

                    }
                }
            })

    }
}
