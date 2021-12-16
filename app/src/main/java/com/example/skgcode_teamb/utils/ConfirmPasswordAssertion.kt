package com.example.skgcode_teamb.utils

import com.afollestad.vvalidator.assertion.Assertion
import com.afollestad.vvalidator.field.input.InputLayoutField
import com.google.android.material.textfield.TextInputLayout

class ConfirmPasswordAssertion : Assertion<TextInputLayout, ConfirmPasswordAssertion>() {

    private var passwordView: TextInputLayout? = null

    override fun defaultDescription(): String {
        return "Password does not match."
    }

    fun compareTo(view: TextInputLayout): ConfirmPasswordAssertion {
        passwordView = view
        return this
    }

    override fun isValid(view: TextInputLayout): Boolean {
        val passwordText = passwordView?.editText?.text.toString()
        val confirmPasswordText = view.editText?.text.toString()
        return passwordText == confirmPasswordText
    }
}

fun InputLayoutField.compareWith(view: TextInputLayout): ConfirmPasswordAssertion {
    return assert(ConfirmPasswordAssertion().compareTo(view))
}