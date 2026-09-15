package ptit.e23cnpm01.nguyenmaiquynh.bai05

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Date

class LoginAct : AppCompatActivity() {

    private lateinit var txtUN: EditText
    private lateinit var txtPW: EditText
    private lateinit var btnLogin: Button

    // UserDAO dùng để làm việc với CSDL
    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login)

        txtUN = findViewById(R.id.txtUN)
        txtPW = findViewById(R.id.txtPW)
        btnLogin = findViewById(R.id.btnLogin)

        // Khởi tạo UserDAO
        userDAO = UserDAO(this)

        btnLogin.setOnClickListener {
            onClick()
        }
    }

    private fun onClick() {

        // Lấy username
        val username = txtUN.text.toString().trim()

        // Lấy password
        val password = txtPW.text.toString().trim()

        // Kiểm tra bỏ trống
        if (
            username.isEmpty() ||
            password.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Vui lòng nhập đầy đủ thông tin",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // Đóng gói username và password thành User
        val user = User(
            username,
            password,
            "",
            Date()
        )

        // Kiểm tra đăng nhập trong CSDL
        val check =
            userDAO.checkLogin(user)

        // Nếu đăng nhập sai
        if (!check) {

            Toast.makeText(
                this,
                "Sai mã sinh viên hoặc ngày sinh",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // Lấy danh sách User từ CSDL
        val listUser =
            userDAO.getAll()

        // Tìm User vừa đăng nhập
        val userLogin =
            listUser.firstOrNull {

                it.username.equals(
                    username,
                    ignoreCase = true
                )
            }

        // Nếu tìm thấy User
        if (userLogin != null) {

            // Mở UserhomeAct
            val intent = Intent(
                this,
                UserhomeAct::class.java
            )

            // Truyền User sang UserhomeAct
            intent.putExtra(
                "user",
                userLogin
            )

            startActivity(intent)
        }
    }
}