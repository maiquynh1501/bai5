package ptit.e23cnpm01.nguyenmaiquynh.bai05

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class UserhomeAct : AppCompatActivity() {

    private lateinit var txtWelcome: TextView
    private lateinit var btnAdd: Button
    private lateinit var listUser: ArrayList<User>
    private lateinit var user: User
    private lateinit var adapter: ArrayAdapter<User>
    private lateinit var userDAO: UserDAO

    private val addUserLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {

                // Lấy lại danh sách từ CSDL
                listUser.clear()
                listUser.addAll(
                    userDAO.getAll().filter {
                        !it.username.equals(
                            user.username,
                            ignoreCase = true
                        )
                    }
                )

                adapter.notifyDataSetChanged()
            }
        }

    private val editUserLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {

                // Lấy lại danh sách từ CSDL
                listUser.clear()
                listUser.addAll(
                    userDAO.getAll().filter {
                        !it.username.equals(
                            user.username,
                            ignoreCase = true
                        )
                    }
                )

                adapter.notifyDataSetChanged()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.userhome)

        txtWelcome =
            findViewById(R.id.lblWelcome)

        btnAdd =
            findViewById(R.id.btnAdd)

        val lwUsers =
            findViewById<ListView>(R.id.lwUsers)

        // Nhận User đăng nhập
        user =
            intent.getSerializableExtra("user") as User

        txtWelcome.text =
            "Xin chào ${user.fullname}"

        // Khởi tạo DAO
        userDAO =
            UserDAO(this)

        // Lấy danh sách người dùng từ CSDL
        listUser =
            ArrayList(
                userDAO.getAll().filter {
                    !it.username.equals(
                        user.username,
                        ignoreCase = true
                    )
                }
            )

        adapter =
            object : ArrayAdapter<User>(
                this,
                R.layout.showuser,
                listUser
            ) {

                override fun getView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {

                    val view =
                        convertView
                            ?: layoutInflater.inflate(
                                R.layout.showuser,
                                parent,
                                false
                            )

                    val currentUser =
                        listUser[position]

                    val lblUser =
                        view.findViewById<TextView>(
                            R.id.lblUser
                        )

                    lblUser.text =
                        "ND${position + 1}\n${currentUser.fullname}"

                    val lblMenu =
                        view.findViewById<TextView>(
                            R.id.lblMenu
                        )

                    lblMenu.setOnClickListener {

                        val menuView =
                            layoutInflater.inflate(
                                R.layout.menuitem,
                                null
                            )

                        val btnEdit =
                            menuView.findViewById<Button>(
                                R.id.btnEdit
                            )

                        val btnDel =
                            menuView.findViewById<Button>(
                                R.id.btnDel
                            )

                        val menuItemFrag =
                            MenuItemFrag()

                        menuItemFrag.onCreate(
                            btnEdit,
                            btnDel
                        )

                        val width =
                            (
                                    45 *
                                            resources.displayMetrics.density
                                    ).toInt()

                        val height =
                            (
                                    64 *
                                            resources.displayMetrics.density
                                    ).toInt()

                        val popupWindow =
                            PopupWindow(
                                menuView,
                                width,
                                height,
                                true
                            )

                        popupWindow.setBackgroundDrawable(
                            ColorDrawable(Color.WHITE)
                        )

                        popupWindow.isOutsideTouchable =
                            true

                        // Sửa User
                        menuItemFrag.onEdit = {

                            popupWindow.dismiss()

                            val intent =
                                Intent(
                                    this@UserhomeAct,
                                    EditUserAct::class.java
                                )

                            intent.putExtra(
                                "user",
                                currentUser
                            )

                            intent.putExtra(
                                "listUser",
                                listUser
                            )

                            editUserLauncher.launch(intent)
                        }

                        // Xóa User
                        menuItemFrag.onDelete = {

                            popupWindow.dismiss()

                            AlertDialog.Builder(
                                this@UserhomeAct
                            )
                                .setTitle("Xác nhận xóa")
                                .setMessage(
                                    "Bạn có chắc chắn muốn xóa người dùng này không?"
                                )
                                .setPositiveButton(
                                    "Có"
                                ) { _, _ ->

                                    // Mở CSDL
                                    val db =
                                        DBHelper(
                                            this@UserhomeAct
                                        ).readableDatabase

                                    // Lấy id của User được chọn
                                    val cursor =
                                        db.rawQuery(
                                            "SELECT id FROM User WHERE username = ?",
                                            arrayOf(
                                                currentUser.username
                                            )
                                        )

                                    if (cursor.moveToFirst()) {

                                        val id =
                                            cursor.getInt(
                                                cursor.getColumnIndexOrThrow(
                                                    "id"
                                                )
                                            )

                                        // Gọi delete(id)
                                        val success =
                                            userDAO.delete(id)

                                        if (success) {

                                            Toast.makeText(
                                                this@UserhomeAct,
                                                "Xóa thành công",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            // Cập nhật lại danh sách
                                            listUser.clear()

                                            listUser.addAll(
                                                userDAO.getAll().filter {
                                                    !it.username.equals(
                                                        user.username,
                                                        ignoreCase = true
                                                    )
                                                }
                                            )

                                            adapter.notifyDataSetChanged()

                                        } else {

                                            Toast.makeText(
                                                this@UserhomeAct,
                                                "Xóa thất bại",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    cursor.close()
                                }
                                .setNegativeButton(
                                    "Không",
                                    null
                                )
                                .show()
                        }

                        popupWindow.showAsDropDown(
                            lblMenu,
                            -width + lblMenu.width,
                            -height - 5
                        )
                    }

                    return view
                }
            }

        lwUsers.adapter =
            adapter

        btnAdd.setOnClickListener {
            onClick()
        }
    }

    private fun onClick() {

        val intent =
            Intent(
                this,
                AddUserAct::class.java
            )

        intent.putExtra(
            "listUser",
            listUser
        )

        addUserLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {

            // Lấy lại dữ liệu mới nhất từ CSDL
            listUser.clear()

            listUser.addAll(
                userDAO.getAll().filter {
                    !it.username.equals(
                        user.username,
                        ignoreCase = true
                    )
                }
            )

            adapter.notifyDataSetChanged()
        }
    }
}