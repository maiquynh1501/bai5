package ptit.e23cnpm01.nguyenmaiquynh.bai05

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditUserAct : AppCompatActivity() {

    private lateinit var txtUN: EditText
    private lateinit var txtPW: EditText
    private lateinit var txtName: EditText
    private lateinit var txtDob: EditText

    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private lateinit var user: User
    private lateinit var listUser: ArrayList<User>

    // UserDAO dùng để làm việc với CSDL
    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.edituser)

        txtUN = findViewById(R.id.txtUN)
        txtPW = findViewById(R.id.txtPW)
        txtName = findViewById(R.id.txtName)
        txtDob = findViewById(R.id.txtDob)

        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        // Nhận User cần sửa
        user =
            intent.getSerializableExtra("user") as User

        // Nhận danh sách User
        listUser =
            intent.getSerializableExtra("listUser")
                    as? ArrayList<User>
                ?: ArrayList()

        // Khởi tạo DAO
        userDAO = UserDAO(this)

        // Hiển thị thông tin cũ
        txtUN.setText(user.username)
        txtPW.setText(user.password)
        txtName.setText(user.fullname)

        txtDob.setText(
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).format(user.dob)
        )

        // Chọn ngày sinh
        txtDob.setOnClickListener {

            val calendar =
                Calendar.getInstance()

            calendar.time = user.dob

            val datePickerDialog =
                DatePickerDialog(
                    this,
                    { _, year, month, day ->

                        txtDob.setText(
                            String.format(
                                "%02d/%02d/%04d",
                                day,
                                month + 1,
                                year
                            )
                        )
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

            // Không cho chọn ngày trong tương lai
            datePickerDialog.datePicker.maxDate =
                System.currentTimeMillis()

            datePickerDialog.show()
        }

        // Lưu
        btnSave.setOnClickListener {
            onClick()
        }

        // Hủy
        btnCancel.setOnClickListener {

            setResult(
                Activity.RESULT_CANCELED
            )

            finish()
        }
    }

    private fun onClick() {

        // Lấy dữ liệu mới
        val username =
            txtUN.text.toString().trim()

        val password =
            txtPW.text.toString().trim()

        val fullname =
            txtName.text.toString().trim()

        val dob =
            txtDob.text.toString().trim()

        // Kiểm tra bỏ trống
        if (
            username.isEmpty() ||
            password.isEmpty() ||
            fullname.isEmpty() ||
            dob.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Vui lòng nhập đầy đủ thông tin",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // Kiểm tra username trùng
        val usernameExists =
            listUser.any {

                it.username.equals(
                    username,
                    ignoreCase = true
                ) &&
                        !it.username.equals(
                            user.username,
                            ignoreCase = true
                        )
            }

        if (usernameExists) {

            Toast.makeText(
                this,
                "Username đã tồn tại",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // Chuyển ngày sinh thành Date
        val parts =
            dob.split("/")

        val calendar =
            Calendar.getInstance()

        calendar.set(
            parts[2].toInt(),
            parts[1].toInt() - 1,
            parts[0].toInt()
        )

        // Tạo User mới
        val userMoi =
            User(
                username,
                password,
                fullname,
                calendar.time
            )

        // Cập nhật User vào CSDL
        val success =
            userDAO.edit(
                userMoi,
                user.username
            )

        if (!success) {

            Toast.makeText(
                this,
                "Cập nhật người dùng thất bại",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // Cập nhật thành công
        Toast.makeText(
            this,
            "Cập nhật thành công",
            Toast.LENGTH_SHORT
        ).show()

        setResult(
            Activity.RESULT_OK
        )

        finish()
    }
}