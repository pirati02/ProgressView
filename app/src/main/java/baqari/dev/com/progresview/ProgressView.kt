package baqari.dev.com.progresview

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class ProgressView(mContext: Context, attrs: AttributeSet) : View(mContext, attrs) {
    var mPercent = 50f
    var mStrokeWidth: Float = 0.toFloat()
    var mBgColor = Color.parseColor("#e1e1e1")
    var mStartAngle = 0f
    var mFgColorStart = 0xffffe400.toInt()
    var mFgColorEnd = 0xffff4800.toInt()

    var mShader: Shader? = null
    var mOval: RectF? = null
    val mPaint: Paint by lazy{
        Paint(Paint.ANTI_ALIAS_FLAG)
    }
    var mGradient: Gradient? = Gradient.LINEAR
    init {
        val a = mContext.theme.obtainStyledAttributes(attrs, R.styleable.ProgressView, 0, 0)
        try {
            mBgColor = a.getColor(R.styleable.ProgressView_bgColor, 0xffe1e1e1.toInt())
            mFgColorEnd = a.getColor(R.styleable.ProgressView_fgColorEnd, 0xffff4800.toInt())

            mFgColorStart = a.getColor(R.styleable.ProgressView_fgColorStart, 0xffffe400.toInt())
            mPercent = a.getFloat(R.styleable.ProgressView_percent, 75f)
            mStartAngle = a.getFloat(R.styleable.ProgressView_startAngle, 0f) + 270
            mStrokeWidth = a.getDimension(R.styleable.ProgressView_strokeWidth, 20.toDp())
            val gradient = a.getInt(R.styleable.ProgressView_gradient, 1)
            mGradient = Gradient.valueOf(gradient)
        } finally {
            a.recycle()
        }
        init()
    }

    private fun init() {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeWidth
        mPaint.strokeCap = Paint.Cap.ROUND
    }

    fun Float.toPx(): Int = (context.resources.displayMetrics.density * 0.5f).toInt()

    fun Int.toDp(): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mPaint.shader = null
        mPaint.color = mBgColor
        mPaint.strokeWidth = 10.toDp()
        canvas.drawArc(mOval!!, 0f, 360f, false, mPaint)

        mPaint.strokeWidth = mStrokeWidth

        mPaint.shader = mShader
        canvas.drawArc(mOval!!, mStartAngle, mPercent * 3.6f, false, mPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        updateOval()

        when(mGradient){
            Gradient.LINEAR -> {
                mShader = LinearGradient(mOval!!.left, mOval!!.top, mOval!!.left, mOval!!.bottom, mFgColorStart, mFgColorEnd, Shader.TileMode.CLAMP)
            }
            Gradient.SWEEP -> {
                mShader = SweepGradient(mOval!!.left, (height / 2).toFloat(), mFgColorStart, mFgColorEnd)
            }
            Gradient.RADIAL ->{
                mShader = RadialGradient(mOval!!.left, mOval!!.top, mStrokeWidth, mFgColorStart,mFgColorEnd, Shader.TileMode.MIRROR)
            }
        }
    }

    var percent: Float
        get() = mPercent
        set(mPercent) {
            this.mPercent = mPercent
            refreshTheLayout()
        }

    private fun updateOval() {
        val xp = paddingLeft + paddingRight
        val yp = paddingBottom + paddingTop
        mOval = RectF(paddingLeft + mStrokeWidth, paddingTop + mStrokeWidth,
                paddingLeft + (width - xp) - mStrokeWidth,
                paddingTop + (height - yp) - mStrokeWidth)
    }

    fun refreshTheLayout() {
        invalidate()
        requestLayout()
    }

    enum class Gradient(val rgb: Int) {
        LINEAR(1),
        SWEEP(2),
        RADIAL(3),;


        companion object{
            fun valueOf(value: Int): Gradient {
                var result = Gradient.LINEAR
                when(value){
                    1 -> result = Gradient.LINEAR
                    2 -> result = Gradient.SWEEP
                    3 -> result = Gradient.RADIAL
                }
                return result
            }
        }
    }
}