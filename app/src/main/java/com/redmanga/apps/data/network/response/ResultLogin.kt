package com.redmanga.apps.data.network.response

data class ResultLogin(
    val code: Int = 0,
    val message: String = "",
    val result: Login = Login()
)

data class Login(
    val token: String = ""
)