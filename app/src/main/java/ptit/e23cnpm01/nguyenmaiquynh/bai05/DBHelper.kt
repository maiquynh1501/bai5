package ptit.e23cnpm01.nguyenmaiquynh.bai05

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        "UserDB",
        null,
        2
    ) {

    // Tạo database và bảng User
    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE User (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE,
                password TEXT,
                fullname TEXT,
                dob TEXT
            )
            """.trimIndent()
        )

        // Tài khoản quản lý
        val admin = ContentValues()

        admin.put("username", "B23DCAT256")
        admin.put("password", "15012005")
        admin.put("fullname", "Nguyễn Mai Quỳnh")
        admin.put("dob", "15/01/2005")

        db.insert(
            "User",
            null,
            admin
        )

        // Người dùng 1
        val user1 = ContentValues()

        user1.put("username", "ND001")
        user1.put("password", "01012005")
        user1.put("fullname", "Nguyễn Văn A")
        user1.put("dob", "01/01/2005")

        db.insert(
            "User",
            null,
            user1
        )

        // Người dùng 2
        val user2 = ContentValues()

        user2.put("username", "ND002")
        user2.put("password", "02022005")
        user2.put("fullname", "Trần Thị B")
        user2.put("dob", "02/02/2005")

        db.insert(
            "User",
            null,
            user2
        )

        // Người dùng 3
        val user3 = ContentValues()

        user3.put("username", "ND003")
        user3.put("password", "03032005")
        user3.put("fullname", "Lê Văn C")
        user3.put("dob", "03/03/2005")

        db.insert(
            "User",
            null,
            user3
        )

        // Người dùng 4
        val user4 = ContentValues()

        user4.put("username", "ND004")
        user4.put("password", "04042005")
        user4.put("fullname", "Phạm Thị D")
        user4.put("dob", "04/04/2005")

        db.insert(
            "User",
            null,
            user4
        )

        // Người dùng 5
        val user5 = ContentValues()

        user5.put("username", "ND005")
        user5.put("password", "05052005")
        user5.put("fullname", "Hoàng Văn E")
        user5.put("dob", "05/05/2005")

        db.insert(
            "User",
            null,
            user5
        )
    }

    // Bắt buộc vì SQLiteOpenHelper yêu cầu
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        // Xóa bảng cũ
        db.execSQL(
            "DROP TABLE IF EXISTS User"
        )

        // Tạo lại database theo cấu trúc mới
        onCreate(db)
    }
}