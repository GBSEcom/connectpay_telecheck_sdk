package com.firstdata.sdk.sample.repo


import com.firstdata.cpsdk.AllPagesResponse
import com.firstdata.cpsdk.TokenResponse
import com.firstdata.cpsdk.singleton.Base64Provider
import com.firstdata.cpsdk.singleton.CPApiClient
import com.firstdata.sdk.sample.utility.*
import com.firstdata.util.network.NetworkCallback
import com.firstdata.util.network.NetworkException
import com.firstdata.util.network.NetworkResult
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Application level Centralized repository to fetch & store data and Interact with SDK
 */
class CPSDKAppRepo {

    @Inject
    constructor() {
        APPLICATION?.appComponent?.inject(this)
    }

    @Inject
    lateinit var mApiService: CPApiClient

    @Inject
    lateinit var mBase64Provider: Base64Provider

    @Inject
    lateinit var gson: Gson

    /**
     * Method to get the Session Token
     *
     */
    fun generateSessionToken(aTestProfile: SDKTestProfileConfig) {

        mApiService.getToken(aTestProfile.apiKey, aTestProfile.secretKey, aTestProfile.fdCustomerID,
                aTestProfile.environment, mBase64Provider!!).enqueue(object : NetworkCallback<TokenResponse> {
            override fun error(ex: NetworkException) {
            }

            override fun success(networkResult: NetworkResult<TokenResponse>) {
                SESSION_TOKEN = networkResult.body.security?.tokenID!!
                aTestProfile.publicKey = networkResult.body.security?.publicKey!!

                postNotifyTokenGeneration()
                getAllPages(aTestProfile, SESSION_TOKEN)
            }
        })
    }

    /**
     * Get all the supported pages
     */
    fun getAllPages(aTestProfile: SDKTestProfileConfig, tokenId: String?) {
        mApiService!!.getPages(aTestProfile.apiKey, tokenId!!, aTestProfile.environment)
                .enqueue(object : NetworkCallback<AllPagesResponse> {
                    override fun error(ex: NetworkException) {
                    }

                    override fun success(networkResult: NetworkResult<AllPagesResponse>) {
                        CONFIG_PAGES = networkResult.body._links!!
                        postNotifyPagesGeneration()
                    }
                })
    }
}
