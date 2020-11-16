package org.nervos.gw

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.nervos.gw.utils.PrefUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddPassportActivity : AppCompatActivity() {

    private var passportNumberView: EditText? = null
    private var expirationDateView: EditText? = null
    private var birthDateView: EditText? = null
    private var prefUtil: PrefUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_passport)
        this.prefUtil = PrefUtil(this)
        initView()
    }

    private fun initView() {

        passportNumberView = findViewById(R.id.input_passport_number)
        expirationDateView = findViewById(R.id.input_passport_expiry_date)
        birthDateView = findViewById(R.id.input_passport_birth_date)

        passportNumberView?.setText(prefUtil?.getPassportNumber())
        expirationDateView?.setText(prefUtil?.getExpiryDate())
        birthDateView?.setText(prefUtil?.getBirthDate())

        passportNumberView?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                prefUtil?.putPassportNumber(s.toString())
            }
        })

        expirationDateView?.setOnClickListener {
            val c = loadDate(expirationDateView)
            DatePickerDialog(
                this, { _, year: Int, month: Int, dayOfMonth: Int ->
                    val value = String.format(Locale.US, "%d-%02d-%02d", year, month + 1, dayOfMonth)
                    prefUtil?.putPassportExpiryDate(value)
                    expirationDateView?.setText(value)
                }, c!![Calendar.YEAR], c[Calendar.MONTH], c[Calendar.DAY_OF_MONTH]
            ).show()
        }

        birthDateView?.setOnClickListener {
            val c = loadDate(birthDateView)
            DatePickerDialog(
                this, { _, year: Int, month: Int, dayOfMonth: Int ->
                    val value = String.format(Locale.US, "%d-%02d-%02d", year, month + 1, dayOfMonth)
                    prefUtil?.putPassportBirthDate(value)
                    birthDateView?.setText(value)
                }, c!![Calendar.YEAR], c[Calendar.MONTH], c[Calendar.DAY_OF_MONTH]
            ).show()
        }

        findViewById<Button>(R.id.action_connect).setOnClickListener{
            startActivity(Intent(this, ReadPassportActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.add_passport_close).setOnClickListener{
            startActivity(Intent(this, CredentialsActivity::class.java))
            finish()
        }

    }


    private fun loadDate(editText: EditText?): Calendar? {
        val calendar = Calendar.getInstance()
        if (editText?.text.toString().isNotEmpty()) {
            try {
                calendar.timeInMillis = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(editText?.text.toString()).time
            } catch (e: ParseException) {
                Log.w(MainActivity::class.java.simpleName, e)
            }
        }
        return calendar
    }

}