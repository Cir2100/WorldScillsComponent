package com.kurilov.worldscillscomponent.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import com.kurilov.worldscillscomponent.R
import com.kurilov.worldscillscomponent.databinding.ListComponentBinding
import com.squareup.picasso.Picasso
import kotlin.math.abs

enum class ListComponentActions {
    SWIPED_LEFT, SWIPED_RIGHT, URLS_IS_EMPTY
}

typealias OnListComponentActionsListener = (ListComponentActions) -> Unit

class ListComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr : Int = 0,
    defStyleRes : Int = 0
    ) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {


    /**
     * Список url для загрузки и отображения картинок
     */

    var urls : List<String> = listOf()
        set(value) {
            field = value
            index = 0
            updateView()
        }

    /**
     * Drawable для отображения в случае невозможности загрузки картинки
     */

    var placeholder : Drawable? = null
        set(value) {
            field = value
            updateView()
        }

    /**
     * Drawable для изображения свайпа вправо
     */

    var leftSwipeImage : Drawable? = null

    /**
     * Drawable для изображения свайпа влево
     */

    var rightSwipeImage : Drawable? = null

    /**
     * Скругление у карточки
     */

    var radius = 0f
        set(value) {
            if (field != value)
                binding.mainImageView.cornerRadius = value
            field = value
        }

    /**
     * Используется для установки слушателя событий: SWIPED_LEFT, SWIPED_RIGHT, URLS_IS_EMPTY
     */

    fun setListener(listener : OnListComponentActionsListener?) {
        _listener = listener
    }

    /**
     * Используйте этот метод для самостоятельного вызова свайпа влево из кода
     */

    fun swipeLeft() {
        if (index < urls.size - 1)
            index++
        _listener?.invoke(ListComponentActions.SWIPED_LEFT)
        updateView()
    }

    /**
     * Используйте этот метод для самостоятельного вызова свайпа вправо из кода
     */

    fun swipeRight() {
        if (index > 0)
            index--
        _listener?.invoke(ListComponentActions.SWIPED_RIGHT)
        updateView()
    }

    /**
     * Возвращает url текущей отображаемой картинки, если список изображений пуст, вернет null
     */

    fun getInfo() : String? {
        if (urls.isEmpty())
            return null
        return urls[index]
    }



    private var index = 0
    private val binding : ListComponentBinding
    private var _listener : OnListComponentActionsListener? = null

    private var eventStartX = 0f
    private var eventEndX = 0f
    private var eventStartY = 0f

    private var mainImageStartX = 0f
    private var swipeImageStartX = 0f


    init {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.list_component, this, true)
        binding = ListComponentBinding.bind(this)

        initializeAttributes(attrs, defStyleAttr, defStyleRes)
        updateView()
    }

    private fun initializeAttributes(attrs: AttributeSet? = null, defStyleAttr : Int = 0, defStyleRes : Int = 0) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListComponent, defStyleAttr, defStyleRes)

        placeholder = typedArray.getDrawable(R.styleable.ListComponent_placeholder_image)
        leftSwipeImage = typedArray.getDrawable(R.styleable.ListComponent_left_swipe_image)
        rightSwipeImage = typedArray.getDrawable(R.styleable.ListComponent_right_swipe_image)

        typedArray.recycle()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                eventStartX = event.x
                eventStartY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                updateImage(event)
                return true
            }
            MotionEvent.ACTION_UP -> {
                eventEndX = event.x
                if (eventEndX > eventStartX)
                    swipeRight()
                else
                    swipeLeft()
            }
        }
        return false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mainImageStartX = paddingLeft.toFloat()
        swipeImageStartX = (w - paddingLeft - paddingRight) / 2f - 15f
    }

    private fun updateImage(e: MotionEvent) {
        val diffX : Float = e.x - eventStartX
        val diffY : Float = e.y - eventStartY
        with(binding) {
            if (abs(diffX) > abs(diffY)) {

                swipeImageView.visibility = VISIBLE

                val angle : Float

                if (diffX > 0) {
                    angle = 5f
                    swipeImageView.setImageDrawable(rightSwipeImage)
                }
                else {
                    angle = -5f
                    swipeImageView.setImageDrawable(leftSwipeImage)
                }

                mainImageView.animate()
                    .x(mainImageStartX + diffX)
                    .rotation(angle)
                    .setDuration(0)
                    .start()

                swipeImageView.animate()
                    .x(swipeImageStartX + diffX)
                    .rotation(angle)
                    .setDuration(0)
                    .start()

            }
        }

    }

    private fun updateView() {
        binding.swipeImageView.visibility = INVISIBLE
        binding.mainImageView.rotation = 0f

        binding.mainImageView.animate()
            .x(mainImageStartX)
            .rotation(0f)
            .setDuration(0)
            .start()

        binding.swipeImageView.x = swipeImageStartX
        binding.swipeImageView.rotation = 0f

        when {
            urls.isEmpty() -> _listener?.invoke(ListComponentActions.URLS_IS_EMPTY)
            placeholder != null -> {
                Picasso
                    .get()
                    .load(urls[index])
                    .error(placeholder!!)
                    .into(binding.mainImageView)
            }
            else -> {
                Picasso
                    .get()
                    .load(urls[index])
                    .into(binding.mainImageView)
            }
        }

    }
}