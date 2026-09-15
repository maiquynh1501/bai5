package ptit.e23cnpm01.nguyenmaiquynh.bai05

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddUserAct : AppCompatActivity() {

    private lateinit var txtUN: EditText
    private lateinit var txtPW: EditText
    private lateinit var txtName: EditText
    private lateinit var txtDob: EditText

    private lateinit var btnAdd: Button
    private lateinit var btnCancel: Button

    private lateinit var listUser: ArrayList<User>

    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.adduser)

        txtUN =
            findViewById(R.id.txtUN)

        txtPW =
            findViewById(R.id.txtPW)

        txtName =
            findViewById(R.id.txtName)

        txtDob =
            findViewById(R.id.txtDob)

        btnAdd =
            findViewById(R.id.btnAdd)

        btnCancel =
            findViewById(R.id.btnCancel)

        // Khởi tạo DAO
        userDAO =
            UserDAO(this)

        // Lấy danh sách User hiện tại từ CSDL
        listUser =
            userDAO.getAll()

        txtDob.setOnClickListener {

            val calendar =
                Calendar.getInstance()

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

            // Không cho chọn ngày tương lai
            datePickerDialog.datePicker.maxDate =
                System.currentTimeMillis()

            datePickerDialog.show()
        }

        btnAdd.setOnClickListener {
            onClick()
        }

        btnCancel.setOnClickListener {

            setResult(
                Activity.RESULT_CANCELED
            )

            finish()
        }
    }

    private fun onClick() {

        val username =
            txtUN.text.toString().trim()

        val password =
            txtPW.text.toString().trim()

        val fullname =
            txtName.text.toString().trim()

        val dob =
            txtDob.text.toString().trim()

        // Kiểm tra nhập đầy đủ
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

        // Kiểm tra username đã tồn tại
        val usernameExists =
            listUser.any {
                it.username.equals(
                    username,
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

        // Chuyển ngày sinh sang Date
        val parts =
            dob.split("/")

        val calendar =
            Calendar.getInstance()

        calendar.set(
            parts[2].toInt(),
            parts[1].toInt() - 1,
            parts[0].toInt()
        )

        val user =
            User(
                username,
                password,
                fullname,
                calendar.time
            )

        // Lưu User vào CSDL
        val success =
            userDAO.add(user)

        if (success) {

            Toast.makeText(
                this,
                "Thêm User thành công",
                Toast.LENGTH_SHORT
            ).show()

            // Báo cho Userhome biết dữ liệu đã thay đổi
            setResult(
                Activity.RESULT_OK
            )

            finish()

        } else {

            Toast.makeText(
                this,
                "Thêm User thất bại",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}