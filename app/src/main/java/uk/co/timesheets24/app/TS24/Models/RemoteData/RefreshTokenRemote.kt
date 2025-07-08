package uk.co.timesheets24.app.TS24.Models.RemoteData

data class RefreshTokenRemote(
    val grantType: String,
    val refreshToken: String
)
