package com.example.manageapp.http

import android.app.Activity
import com.example.manageapp.utils.LogU
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.internal.Util.UTF_8
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.*
import java.lang.reflect.Type
import java.nio.Buffer
import java.nio.charset.Charset


class GsonConverterFactory1(activity: Activity) : Converter.Factory() {
    private var act = activity
    private var gson:Gson = Gson()
    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody> {
        var adapter:TypeAdapter<Any> = gson.getAdapter(TypeToken.get(type)) as TypeAdapter<Any>
        return GsonRequestBodyConverter1<Any>(gson, adapter)
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *> {
        var adapter:TypeAdapter<Any> = gson.getAdapter(TypeToken.get(type)) as TypeAdapter<Any>
        return GsonResponseBodyConverter1<Any>(gson, adapter,act)
    }
}
/*请求数据规范*/
class GsonRequestBodyConverter1<Any>(gsons: Gson, adapters: TypeAdapter<Any>) : Converter<Any, RequestBody> {
    private var MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
    private var UTF_8 = Charset.forName("UTF-8")
    private var gson = gsons
    private var adapter = adapters
    open var buffer = Buffer()
    override fun convert(value: Any): RequestBody?{
//        val buffer = object :Buffer(){}
        var writer = OutputStreamWriter(buffer.outputStream(), UTF_8);
        var jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
        return RequestBody.create(MEDIA_TYPE, value.toString())
    }
}
/*返回数据规范*/
class GsonResponseBodyConverter1<Any>(gsons: Gson, adapters: TypeAdapter<Any>, activity: Activity): Converter<ResponseBody,Any> {
    private var act = activity
    private var gson = gsons
    private var adapter = adapters

    override fun convert(value: ResponseBody?): Any {
        var response = value.toString()
        var httpStatus = gson.fromJson(response, HttpStatus::class.java)
        LogU.i("state",response)
        if (httpStatus.isCode20000) {
            value!!.close()
            throw ApiException1(httpStatus.code,act)
        }
        var contentType = value!!.contentType()
        var charset = if (contentType != null) contentType.charset(UTF_8) else UTF_8
        var inputStream = ByteArrayInputStream(response.toByteArray())
        var reader = InputStreamReader(inputStream, charset)
        var jsonReader = gson.newJsonReader(reader)
        LogU.d("GsonRespons",response)
        value.use { _ ->
            return adapter.read(jsonReader)
        }
    }
}
