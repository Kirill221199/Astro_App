package ru.kirill.astro_app.view.mars_picture

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import java.lang.Math.abs

class ButtonBehavior(
    context: Context,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<View>(context, attrs) {


    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return (dependency is AppBarLayout)
    }


    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {

        val bar = dependency as AppBarLayout
        child.alpha = 1 - kotlin.math.abs(bar.y) / parent.height.toFloat()
        child.y = (bar.height.toFloat() - child.height.toFloat()/2) + kotlin.math.abs(bar.y * 6)


        return super.onDependentViewChanged(parent, child, dependency)
    }

}