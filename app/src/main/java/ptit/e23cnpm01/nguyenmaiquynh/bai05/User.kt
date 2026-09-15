package ptit.e23cnpm01.nguyenmaiquynh.bai05

import java.io.Serializable
import java.util.Date

class User(
    var username: String,
    var password: String,
    var fullname: String,
    var dob: Date
) : Serializable