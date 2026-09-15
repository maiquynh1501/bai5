package ptit.e23cnpm01.nguyenmaiquynh.bai05

import android.content.ContentValues
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Locale

class UserDAO(context: Context) {

    // DBHelper quản lý SQLite
    private val dbHelper = DBHelper(context)

    // Thêm User vào CSDL
    fun add(u: User): Boolean {

        val db = dbHelper.writableDatabase

        val values = ContentValues()

        values.put(
            "username",
            u.username
        )

        values.put(
            "password",
            u.password
        )

        values.put(
            "fullname",
            u.fullname
        )

        values.put(
            "dob",
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).format(u.dob)
        )

        val result =
            db.insert(
                "User",
                null,
                values
            )

        return result != -1L
    }

    // Sửa thông tin User
    fun edit(
        u: User,
        oldUsername: String
    ): Boolean {

        val db = dbHelper.writableDatabase

        val values = ContentValues()

        values.put(
            "username",
            u.username
        )

        values.put(
            "password",
            u.password
        )

        values.put(
            "fullname",
            u.fullname
        )

        values.put(
            "dob",
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).format(u.dob)
        )

        val result =
            db.update(
                "User",
                values,
                "username = ?",
                arrayOf(oldUsername)
            )

        return result > 0
    }

    // Xóa User theo id
    fun delete(id: Int): Boolean {

        val db = dbHelper.writableDatabase

        val result =
            db.delete(
                "User",
                "id = ?",
                arrayOf(id.toString())
            )

        return result > 0
    }

    // Tìm kiếm User
    fun search(
        key: String
    ): ArrayList<User> {

        val listUser =
            ArrayList<User>()

        val db =
            dbHelper.readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT username, password, fullname, dob
                FROM User
                WHERE username LIKE ?
                   OR fullname LIKE ?
                """.trimIndent(),
                arrayOf(
                    "%$key%",
                    "%$key%"
                )
            )

        while (cursor.moveToNext()) {

            val username =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "username"
                    )
                )

            val password =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "password"
                    )
                )

            val fullname =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "fullname"
                    )
                )

            val dob =
                SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                ).parse(
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "dob"
                        )
                    )
                )!!

            listUser.add(
                User(
                    username,
                    password,
                    fullname,
                    dob
                )
            )
        }

        // Đóng Cursor
        cursor.close()

        return listUser
    }

    // Lấy toàn bộ User
    fun getAll(): ArrayList<User> {

        val listUser =
            ArrayList<User>()

        val db =
            dbHelper.readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT username, password, fullname, dob
                FROM User
                """.trimIndent(),
                null
            )

        while (cursor.moveToNext()) {

            val username =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "username"
                    )
                )

            val password =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "password"
                    )
                )

            val fullname =
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        "fullname"
                    )
                )

            val dob =
                SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                ).parse(
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            "dob"
                        )
                    )
                )!!

            listUser.add(
                User(
                    username,
                    password,
                    fullname,
                    dob
                )
            )
        }

        // Đóng Cursor
        cursor.close()

        return listUser
    }

    // Kiểm tra thông tin đăng nhập
    fun checkLogin(
        u: User
    ): Boolean {

        val db =
            dbHelper.readableDatabase

        val cursor =
            db.rawQuery(
                """
                SELECT *
                FROM User
                WHERE username = ?
                  AND password = ?
                  AND username = 'B23DCAT256'
                """.trimIndent(),
                arrayOf(
                    u.username,
                    u.password
                )
            )

        val result =
            cursor.moveToFirst()

        // Đóng Cursor
        cursor.close()

        return result
    }
}