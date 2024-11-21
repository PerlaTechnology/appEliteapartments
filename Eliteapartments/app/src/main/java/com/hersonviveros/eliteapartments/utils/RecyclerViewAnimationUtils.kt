package com.hersonviveros.eliteapartments.utils

import android.annotation.SuppressLint
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.hersonviveros.eliteapartments.R

class RecyclerViewAnimationUtils {
    companion object {
        @SuppressLint("NotifyDataSetChanged")
        fun runLayoutAnimation(recyclerView: RecyclerView) {
            val context = recyclerView.context
            val controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

            recyclerView.layoutAnimation = controller
            recyclerView.adapter!!.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        }
    }
}