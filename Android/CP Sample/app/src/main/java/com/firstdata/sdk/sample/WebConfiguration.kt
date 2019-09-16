package com.firstdata.sdk.sample

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WebConfiguration(

        @SerializedName("env")
        @Expose
        val env: String,

        @SerializedName("apiKey")
        @Expose
        val apiKey: String,

        @SerializedName("fdCustomerId")
        @Expose
        val fdCustomerId: String,

        @SerializedName("accessToken")
        @Expose
        val accessToken: String,

        @SerializedName("encryptionKey")
        @Expose
        var encryptionKey: String,

        @SerializedName("configId")
        @Expose
        var configId: String,

        @SerializedName("rel")
        @Expose
        val rel: String,

        @SerializedName("extraParams")
        @Expose
        val extraParams: Map<String, Any>? = null,

        @SerializedName("postUrl")
        @Expose
        val postUrl: String

)
