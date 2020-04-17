package com.example.imkotlin.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.imkotlin.R
import kotlinx.android.synthetic.main.fragment_contacts.view.*
import org.jetbrains.anko.sp

class SlideBar(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {

    var sectionHeight = 0f

    var paint = Paint()

    var textbaseline = 0f

    //提供接口回调处，onSectionChangeListener可能为空，监听到就返回
    var onSectionChangeListener: OnSectionChangeListener? = null

    companion object {
        private val SECTIONS = arrayOf(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
        )
    }

    init {
        paint.apply {
            color = resources.getColor(R.color.qq_section_text_color)
            textSize = sp(12).toFloat()
            //设置绘制文字的对齐方向 - 对齐居中
            textAlign = Paint.Align.CENTER
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //计算每个字符分配的高度(每个格子的总高度)
        sectionHeight = h * 1.0f / SECTIONS.size  //h为总高度
        val fontMetrics = paint.fontMetrics //字体测量使用
        //计算绘制文本的高度(字体总高度)
        val textHeight =
            fontMetrics.descent - fontMetrics.ascent  //fontMetrics.descent是baseline之上至字符最高处的位移,是正值；fontMetrics.ascent是baseline之下至字符最低处的位移，是负值
        //计算基准线baseline
        textbaseline = sectionHeight / 2 + (textHeight / 2 - fontMetrics.descent)  //sectionHeight/2就是矩形中线的高度
    }

    override fun onDraw(canvas: Canvas) {
        //绘制所有字母
        val x = width * 1.0f / 2
        var baseline = textbaseline
        SECTIONS.forEach {
            canvas.drawText(it, x, baseline, paint)
            //更新y，绘制下一个字母
            baseline += sectionHeight
        }
    }

    //slidebar的触摸事件
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                setBackgroundResource(R.drawable.bg_slide_bar)
                //找到点击的字母
                val index = getTouchIndex(event)
                val firstLetter = SECTIONS[index]
                onSectionChangeListener?.onSectionChange(firstLetter,index)  //设置监听返回
            }
            MotionEvent.ACTION_MOVE -> {
                //找到点击的字母
                val index = getTouchIndex(event)
                val firstLetter = SECTIONS[index]
                onSectionChangeListener?.onSectionChange(firstLetter,index)  //设置监听返回
            }
            MotionEvent.ACTION_UP -> {
                setBackgroundColor(Color.TRANSPARENT)  //按下放回变回透明
                onSectionChangeListener?.onSlideFinish()  //设置监听返回
            }
        }
        return true //消费事件
    }

    private fun getTouchIndex(event: MotionEvent): Int {
        var index = (event.y / sectionHeight).toInt()  //slidebar总长度除以格子的高度
        //越界检查
        if (index < 0){
            index = 0  //A
        } else if (index >= SECTIONS.size){
            index = SECTIONS.size - 1  //Z
        }
        return index
    }

    //提供slidebar的接口回调给contactFragment显示对应字母图片
    interface OnSectionChangeListener{
        fun onSectionChange(firstLetter: String, index: Int)  //提供对应字母
        fun onSlideFinish()  //滑动结束的回调，结束要隐藏掉对应字母图片
    }
}