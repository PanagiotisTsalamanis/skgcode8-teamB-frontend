package com.example.skgcode_teamb.utils

import com.afollestad.vvalidator.assertion.Assertion
import com.google.android.material.textfield.TextInputLayout

class ScheduleTimeAssertion : Assertion<TextInputLayout, ScheduleTimeAssertion>() {

    override fun defaultDescription(): String {
        return "Please select time."
    }

    override fun isValid(view: TextInputLayout): Boolean {
        return view.editText!!.text!!.isNotEmpty()
    }





}