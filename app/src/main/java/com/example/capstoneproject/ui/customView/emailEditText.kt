package com.example.capstoneproject.ui.customVIew

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.util.regex.Pattern

class emailEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttributeSet: Int) : super(context, attributeSet, defStyleAttributeSet) {
        init()
    }

    private fun init() {
        addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(text: Editable?) {
                if (!isEmailValid(text.toString())) {
                    setError("Invalid email address")
                } else {
                    error = null
                }
            }
        })
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")
        return pattern.matcher(email).matches()
    }
}
