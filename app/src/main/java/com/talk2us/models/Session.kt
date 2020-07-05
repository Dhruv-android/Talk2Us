package com.talk2us.models

import com.talk2us.utils.Constants

data class Session(
    val clientId: String = Constants.NOT_DEFINED,
    val clientToken: String = Constants.NOT_DEFINED,
    val counsellorId: String = Constants.NOT_DEFINED,
    val counsellorToken: String = Constants.NOT_DEFINED,
    val sessionId: String = Constants.NOT_DEFINED
) {
    constructor() : this(
        clientId = Constants.NOT_DEFINED
    )
}