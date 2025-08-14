package com.omega.ui.widget.celebrate

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import java.util.Random
import kotlin.math.abs

class CelebrateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs,defStyleAttr){

    class TimerIntegration {
        private var previousTime: Long = -1L

        fun reset() {
            previousTime = -1L
        }

        fun getDeltaTime(): Float {
            if (previousTime == -1L) previousTime = System.nanoTime()

            val currentTime = System.nanoTime()
            val dt: Long = (currentTime - previousTime) / 1000000
            previousTime = currentTime
            return dt.toFloat() / 1000
        }

        fun getTotalTimeRunning(startTime: Long): Long = System.currentTimeMillis() - startTime
    }

    private val systems: MutableList<ParticleSystem> = mutableListOf()
    private var timer: TimerIntegration = TimerIntegration()

    fun build(): ParticleSystem = ParticleSystem(this)

    fun start(particleSystem: ParticleSystem) {
        systems.add(particleSystem)
        invalidate()
    }

    // ------------------------------------------------------------------------
    // location
    // ------------------------------------------------------------------------
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    // ------------------------------------------------------------------------
    // draw
    // ------------------------------------------------------------------------
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val deltaTime = timer.getDeltaTime()
        for (i in systems.size - 1 downTo 0) {
            val particleSystem = systems[i]

            val totalTimeRunning = timer.getTotalTimeRunning(particleSystem.renderSystem.createdAt)
            if (totalTimeRunning >= particleSystem.getDelay()) {
                particleSystem.renderSystem.render(canvas, deltaTime)
            }

            if (particleSystem.doneEmitting()) systems.removeAt(i)
        }

        if (systems.size != 0) {
            invalidate()
        } else {
            timer.reset()
        }
    }
}

abstract class Emitter {

    // ------------------------------------------------------------------------
    // Call this function to tell the RenderSystem to render a particle
    // ------------------------------------------------------------------------
    var addConfettiFunc: (() -> Unit)? = null

    /** ------------------------------------------------------------------------
    // This function is called on each update when the [RenderSystem] is active
    // Keep this function as light as possible otherwise you'll slow down the render system
    // ------------------------------------------------------------------------ **/
    abstract fun createConfetti(deltaTime: Float)

    abstract fun isFinished(): Boolean
}

class StreamEmitter : Emitter() {

    companion object {
        /**
         * Start an endless stream of particles by using this property in combination with
         * emittingTime. The stream of particles can only be stopped manually by calling reset or
         * stopGracefully on KonfettiView.
         */
        @JvmField
        val INDEFINITE = -2L
    }

    /** Max amount of particles allowed to be created */
    private var maxParticles = -1

    /** Keeping count of how many particles are created */
    private var particlesCreated = 0

    /** Max time allowed to emit in milliseconds */
    private var emittingTime: Long = 0

    /** Elapsed time in milliseconds */
    private var elapsedTime: Float = 0f

    /** Amount of time needed for each particle creation in milliseconds */
    private var amountPerMs: Float = 0f

    /** Amount of time elapsed since last particle creation in milliseconds */
    private var createParticleMs: Float = 0f

    fun build(
        particlesPerSecond: Int,
        emittingTime: Long = 0L,
        maxParticles: Int = -1
    ): StreamEmitter {
        this.maxParticles = maxParticles
        this.emittingTime = emittingTime
        this.amountPerMs = 1f / particlesPerSecond
        return this
    }

    /**
     * If timer isn't started yet, set initial start time
     * Create the first confetti immediately and update the last emitting time
     */
    override fun createConfetti(deltaTime: Float) {
        createParticleMs += deltaTime

        // Check if particle should be created
        if (createParticleMs >= amountPerMs && !isTimeElapsed()) {
            // Calculate how many particle  to create in the elapsed time
            val amount: Int = (createParticleMs / amountPerMs).toInt()
            (1..amount).forEach { createParticle() }
            // Reset timer and add left over time for the next cycle
            createParticleMs %= amountPerMs
        }

        elapsedTime += deltaTime * 1000
    }

    private fun createParticle() {
        if (reachedMaxParticles()) {
            return
        }
        particlesCreated++
        addConfettiFunc?.invoke()
    }

    /**
     * If the [emittingTime] is 0 it's not set and not relevant
     * If the emitting time is set check if [elapsedTime] exceeded the emittingTime
     */
    private fun isTimeElapsed(): Boolean {
        return when (emittingTime) {
            0L -> false
            INDEFINITE -> false
            else -> elapsedTime >= emittingTime
        }
    }

    /**
     * If [maxParticles] is set in the configuration of this emitter check if the emitter
     * reached the max amount of particles created.
     *
     * @return boolean true if [particlesCreated] exceeded [maxParticles]
     *         boolean false if maxParticles is not set (-1) or if it's still allowed
     *         to create particles if maxParticles is set.
     */
    private fun reachedMaxParticles(): Boolean = maxParticles in 1..(particlesCreated)

    /**
     * If the [emittingTime] is set tell the [RenderSystem] the emitter is finished creating
     * particles when the elapsed time exceeded the emitting time.
     * If the [emittingTime] is not set tell the [RenderSystem] that the emitter is finished
     * creating particles when [particlesCreated] exceeded [maxParticles]
     */
    override fun isFinished(): Boolean {
        return when {
            emittingTime > 0L -> elapsedTime >= emittingTime
            emittingTime == INDEFINITE -> false
            else -> particlesCreated >= maxParticles
        }
    }
}

// ------------------------------------------------------------------------
// 渲染系统
// ------------------------------------------------------------------------
class RenderSystem(
    private val location: LocationModule,
    private val velocity: VelocityModule,
    private val sizes: Array<Size>,
    private val shapes: Array<Shape>,
    private val colors: IntArray,
    private val config: ConfettiConfig,
    private val emitter: Emitter,
    val createdAt: Long = System.currentTimeMillis()
) {

    // ---- 是否允许继续渲染 ------
    var enabled = true

    private val random = Random()
    private var gravity = Vector(0f, 0.01f)
    private val particles: MutableList<Confetti> = mutableListOf()

    init {
        emitter.addConfettiFunc = this::addConfetti
    }

    private fun addConfetti() {
        particles.add(
            Confetti(
                location = Vector(location.x, location.y),
                size = sizes[random.nextInt(sizes.size)],
                shape = getRandomShape(),
                color = colors[random.nextInt(colors.size)],
                lifespan = config.timeToLive,
                fadeOut = config.fadeOut,
                velocity = this.velocity.getVelocity(),
                rotate = config.rotate,
                maxAcceleration = velocity.maxAcceleration,
                accelerate = config.accelerate,
                rotationSpeedMultiplier = velocity.getRotationSpeedMultiplier()
            )
        )
    }

    /**
     * When the shape is a DrawableShape, mutate the drawable so that all drawables
     * have different values when drawn on the canvas.
     */
    private fun getRandomShape(): Shape {
        return when (val shape = shapes[random.nextInt(shapes.size)]) {
            is Shape.DrawableShape -> {
                val mutatedState = shape.drawable.constantState?.newDrawable()?.mutate() ?: shape.drawable
                shape.copy(drawable = mutatedState)
            }
            else -> shape
        }
    }

    fun render(canvas: Canvas, deltaTime: Float) {
        if (enabled) emitter.createConfetti(deltaTime)

        for (i in particles.size - 1 downTo 0) {
            val particle = particles[i]
            particle.applyForce(gravity, deltaTime)
            particle.render(canvas, deltaTime)
            if (particle.isDead()) particles.removeAt(i)
        }
    }

    fun getActiveParticles(): Int = particles.size

    fun isDoneEmitting(): Boolean = (emitter.isFinished() && particles.size == 0) || (!enabled && particles.size == 0)
}

// ------------------------------------------------------------------------
// 渲染系统 - 属性
// ------------------------------------------------------------------------
class Confetti(
    var location: Vector,
    val color: Int,
    val size: Size,
    val shape: Shape,
    var lifespan: Long = -1L,
    val fadeOut: Boolean = true,
    private var acceleration: Vector = Vector(0f, 0f),
    var velocity: Vector = Vector(),
    val rotate: Boolean = true,
    val accelerate: Boolean = true,
    val maxAcceleration: Float = -1f,
    val rotationSpeedMultiplier: Float = 1f
) {

    private val mass = size.mass
    private var width = size.sizeInPx
    private val paint: Paint = Paint()

    private var rotationSpeed = 0f
    private var rotation = 0f
    private var rotationWidth = width

    // Expected frame rate
    private var speedF = 60f

    private var alpha: Int = 255

    init {
        val minRotationSpeed = 0.29f * Resources.getSystem().displayMetrics.density
        val maxRotationSpeed = minRotationSpeed * 3
        if (rotate) {
            rotationSpeed = (maxRotationSpeed * kotlin.random.Random.nextFloat() + minRotationSpeed) * rotationSpeedMultiplier
        }
        paint.color = color
    }

    private fun getSize(): Float = width

    fun isDead(): Boolean = alpha <= 0f

    fun applyForce(force: Vector, deltaTime: Float) {
        val f = force.copy()
        f.div(mass)
        f.mult(deltaTime * speedF)
        acceleration.add(f)
    }

    fun render(canvas: Canvas, deltaTime: Float) {
        update(deltaTime)
        display(canvas)
    }

    private fun update(deltaTime: Float) {
        if (accelerate && (acceleration.y < maxAcceleration || maxAcceleration == -1f)) {
            velocity.add(acceleration)
        }

        val v = velocity.copy()
        v.mult(deltaTime * speedF)
        location.add(v)

        if (lifespan <= 0) updateAlpha(deltaTime)
        else lifespan -= (deltaTime * 1000).toLong()

        val rSpeed = (rotationSpeed * deltaTime) * speedF
        rotation += rSpeed
        if (rotation >= 360) rotation = 0f

        rotationWidth -= rSpeed
        if (rotationWidth < 0) rotationWidth = width
    }

    private fun updateAlpha(deltaTime: Float) {
        if (fadeOut) {
            val interval = 5 * deltaTime * speedF
            if (alpha - interval < 0) alpha = 0
            else alpha -= (5 * deltaTime * speedF).toInt()
        } else {
            alpha = 0
        }
    }

    private fun display(canvas: Canvas) {
        // if the particle is outside the bottom of the view the lifetime is over.
        if (location.y > canvas.height) {
            lifespan = 0
            return
        }

        // Do not draw the particle if it's outside the canvas view
        if (location.x > canvas.width || location.x + getSize() < 0 || location.y + getSize() < 0) {
            return
        }

        paint.alpha = alpha

        val scaleX = abs(rotationWidth / width - 0.5f) * 2
        val centerX = scaleX * width / 2

        val saveCount = canvas.save()
        canvas.translate(location.x - centerX, location.y)
        canvas.rotate(rotation, centerX, width / 2)
        canvas.scale(scaleX, 1f)

        shape.draw(canvas, paint, width)
        canvas.restoreToCount(saveCount)
    }
}

// ------------------------------------------------------------------------
// 渲染系统 - 属性 - 图形
// ------------------------------------------------------------------------
interface Shape {

    fun draw(canvas: Canvas, paint: Paint, size: Float)

    object Square : Shape {
        override fun draw(canvas: Canvas, paint: Paint, size: Float) {
            canvas.drawRect(0f, 0f, size, size, paint)
        }
    }

    class Rectangle(private val heightRatio: Float) : Shape {
        init {
            require(heightRatio in 0f..1f)
        }

        override fun draw(canvas: Canvas, paint: Paint, size: Float) {
            val height = size * heightRatio
            val top = (size - height) / 2f
            canvas.drawRect(0f, top, size, top + height, paint)
        }
    }

    object Circle : Shape {
        private val rect = RectF()

        override fun draw(canvas: Canvas, paint: Paint, size: Float) {
            rect.set(0f, 0f, size, size)
            canvas.drawOval(rect, paint)
        }
    }

    data class DrawableShape(val drawable: Drawable,private val tint: Boolean = true) : Shape {
        private val heightRatio =
            if (drawable.intrinsicHeight == -1 && drawable.intrinsicWidth == -1) 1f
            else if (drawable.intrinsicHeight == -1 || drawable.intrinsicWidth == -1) 0f
            else drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth

        override fun draw(canvas: Canvas, paint: Paint, size: Float) {
            if (tint) drawable.setColorFilter(paint.color, PorterDuff.Mode.SRC_IN)
            else drawable.alpha = paint.alpha

            val height = (size * heightRatio).toInt()
            val top = ((size - height) / 2f).toInt()

            drawable.setBounds(0, top, size.toInt(), top + height)
            drawable.draw(canvas)
        }
    }
}

// ------------------------------------------------------------------------
// 渲染系统 - 属性 - 大小
// ------------------------------------------------------------------------
data class Size(val sizeInDp: Int, val mass: Float = 5f) {

    internal val sizeInPx: Float
        get() = sizeInDp * Resources.getSystem().displayMetrics.density

    init {
        require(mass != 0F) { "mass=$mass must be != 0" }
    }
}

// ------------------------------------------------------------------------
// 渲染系统 - 属性 - 大小
// ------------------------------------------------------------------------
data class Vector(var x: Float = 0f, var y: Float = 0f) {
    fun add(v: Vector) {
        x += v.x
        y += v.y
    }

    fun mult(n: Float) {
        x *= n
        y *= n
    }

    fun div(n: Float) {
        x /= n
        y /= n
    }
}

// ------------------------------------------------------------------------
// 物理系统
// ------------------------------------------------------------------------
class ParticleSystem(val celebrateView: CelebrateView){
    private val random = Random()

    // ------------------------------------------------------------------------
    // 物理模块
    // ------------------------------------------------------------------------
    private var location = LocationModule(random)
    private var velocity = VelocityModule(random)

    // ------------------------------------------------------------------------
    // UI配置
    // ------------------------------------------------------------------------
    private var colors = intArrayOf(Color.RED)
    private var sizes = arrayOf(Size(16))
    private var shapes: Array<Shape> = arrayOf(Shape.Square)
    private var confettiConfig = ConfettiConfig()

    fun getDelay() = confettiConfig.delay

    internal lateinit var renderSystem: RenderSystem

    fun setPosition(minX: Float, maxX: Float? = null, minY: Float, maxY: Float? = null): ParticleSystem {
        location.betweenX(minX, maxX)
        location.betweenY(minY, maxY)
        return this
    }

    // ------------------------------------------------------------------------
    // 发射频率- 多少个/s ,持续时间
    // ------------------------------------------------------------------------
    fun streamFor(particlesPerSecond: Int, emittingTime: Long) {
        val stream = StreamEmitter().build(particlesPerSecond = particlesPerSecond, emittingTime = emittingTime)
        startRenderSystem(stream)
    }

    // ------------------------------------------------------------------------
    // 开始渲染
    // ------------------------------------------------------------------------
    private fun startRenderSystem(emitter: Emitter) {
        renderSystem = RenderSystem(location, velocity, sizes, shapes, colors, confettiConfig, emitter)
        start()
    }

    private fun start() {
        celebrateView.start(this)
    }

    // ------------------------------------------------------------------------
    // 是否结束
    // ------------------------------------------------------------------------
    fun doneEmitting(): Boolean = renderSystem.isDoneEmitting()
}

// ------------------------------------------------------------------------
// 物理系统 - 坐标
// ------------------------------------------------------------------------
class LocationModule(private val random: Random) {

    private var minX: Float = 0f
    private var maxX: Float? = null

    private var minY: Float = 0f
    private var maxY: Float? = null

    val x: Float; get() {
        return if (maxX == null) {
            minX
        } else {
            random.nextFloat().times(maxX!!.minus(minX)) + minX
        }
    }

    val y: Float; get() {
        return if (maxY == null) {
            minY
        } else {
            random.nextFloat().times(maxY!!.minus(minY)) + minY
        }
    }

    fun betweenX(minX: Float, maxX: Float?) {
        this.minX = minX
        this.maxX = maxX
    }

    fun betweenY(minY: Float, maxY: Float?) {
        this.minY = minY
        this.maxY = maxY
    }
}

// ------------------------------------------------------------------------
// 物理系统 - 系数
// ------------------------------------------------------------------------
class VelocityModule(private val random: Random) {

    private var minAngle: Double = Math.toRadians(0.0)

    private var maxAngle: Double? = Math.toRadians(359.0)

    private var minSpeed: Float = 1f
        set(value) {
            field = if (value < 0) 0f else value
        }

    private var maxSpeed: Float = 5f
        set(value) {
            field = if (value < 0) 0f else value
        }

    var maxAcceleration: Float = -1f

    private var baseRotationMultiplier: Float = 1f

    private var rotationVariance: Float = 0.2f

    private fun getSpeed(): Float {
        return ((maxSpeed - minSpeed) * random.nextFloat()) + minSpeed
    }

    private fun getRadian(): Double {
        return if (maxAngle == null) {
            minAngle
        } else {
            ((maxAngle!! - minAngle) * random.nextDouble()) + minAngle
        }
    }

    fun getVelocity(): Vector {
        val speed = getSpeed()
        val radian = getRadian()
        val vx = speed * Math.cos(radian).toFloat()
        val vy = speed * Math.sin(radian).toFloat()
        return Vector(vx, vy)
    }

    fun getRotationSpeedMultiplier(): Float {
        val randomValue = random.nextFloat() * 2f - 1f
        return baseRotationMultiplier + (baseRotationMultiplier * rotationVariance * randomValue)
    }
}

data class ConfettiConfig(
    var fadeOut: Boolean = true,
    var timeToLive: Long = 2000L,
    var rotate: Boolean = true,
    var accelerate: Boolean = true,
    var delay: Long = 0L
)
