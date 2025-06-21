package com.ianindratama.newsapp.presentation.utils

import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ianindratama.newsapp.R

fun TextView.cleanNewsContent(mainContent: String, optionalContent: String, seeMoreLink: String) {
    val seeMore = "See more"
    val cleanedContent =
        Regex("""(.*?[.!?])(?=\s+[A-Z]|…|\s*\[\+\d+ chars])""")
            .findAll(mainContent)
            .map { it.value }
            .joinToString(" ")
            .ifEmpty {
                optionalContent
            }
            .plus(" ")
            .plus(seeMore)
    val finalCleanedContent = SpannableString(cleanedContent)

    val startIndex = cleanedContent.indexOf(seeMore)
    val endIndex = startIndex + seeMore.length

    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(seeMoreLink))
            widget.context.startActivity(intent)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = ContextCompat.getColor(context, R.color.colorPrimary)
        }
    }

    finalCleanedContent.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


    text = finalCleanedContent
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = ContextCompat.getColor(context, R.color.colorTransparent)
}