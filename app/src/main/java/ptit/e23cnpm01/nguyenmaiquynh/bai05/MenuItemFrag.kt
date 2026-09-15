package ptit.e23cnpm01.nguyenmaiquynh.bai05

import android.view.View
import android.widget.Button

class MenuItemFrag : View.OnClickListener {

    private lateinit var btnEdit: Button
    private lateinit var btnDel: Button

    var onEdit: (() -> Unit)? = null
    var onDelete: (() -> Unit)? = null

    fun onCreate(
        btnEdit: Button,
        btnDel: Button
    ) {
        this.btnEdit = btnEdit
        this.btnDel = btnDel

        btnEdit.setOnClickListener(this)
        btnDel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.btnEdit -> {
                onEdit?.invoke()
            }

            R.id.btnDel -> {
                onDelete?.invoke()
            }
        }
    }
}