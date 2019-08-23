package com.firstdata.sdk.sample.utility

import android.content.Context
import com.firstdata.cpsdk.HPLink
import com.firstdata.sdk.sample.ConnectPayApplication


const val API_SECRET = "514b9cdfec20027047d24d5a8f139eb86549db06f3e004b101424bffabcd2e94"

const val SUBSCRIBER_ID = "44010067"

var APP_CONTEXT: Context? = null

var APPLICATION: ConnectPayApplication? = null

lateinit var SESSION_TOKEN: String

lateinit var SDK_TEST_PROFILE: SDKTestProfileConfig

var CONFIG_PAGES: List<HPLink>? = null

const val APP_WEB_BASE_URI = "https://qa.api.firstdata.com/connectpayapi/v1/static/v3/tool/internal/cp-sdk.html?apiKey=5e66b5d4iYHVK9ChqaFMnCrgfBBbIwiW&secretKey=iGMK11IfsGD00vEW"

//const val APP_WEB_BASE_URI = "https://qa.api.firstdata.com/connectpayapi/v1/static/v3/tool/internal/cp-sdk.html?apiKey=wd4KQx4qbFz1lu6XNfptBpnAjSRS2mF6&secretKey=ukwagYmEEswSNe8w"

const val APP_BASE_URI = "https://qa.api.firstdata.com/connectpayapi/v1/"

const val CREATE_SESSION_TOKEN_URI = "security/createsessiontoken"


