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

fun TextView.addTextAndHyperLinkToNewsContent(content: String, seeMoreLink: String) {
    val seeMore = "See more"
    val contentWithSeeMore = content
        .plus(" ")
        .plus(seeMore)

    val finalContent = SpannableString(contentWithSeeMore)

    val startIndex = contentWithSeeMore.indexOf(seeMore)
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

    finalContent.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


    text = finalContent
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = ContextCompat.getColor(context, R.color.colorTransparent)
}