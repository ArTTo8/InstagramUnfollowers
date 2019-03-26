package com.artto.unfollowers.data.local

import java.util.*

data class UserEntity(
        var username: String,
        var password: String,
        var firstOpen: Date,
        var rateDialogShowed: Boolean
)