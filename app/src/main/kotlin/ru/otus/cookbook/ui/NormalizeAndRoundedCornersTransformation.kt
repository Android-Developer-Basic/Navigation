package ru.otus.cookbook.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.RectF
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import kotlin.math.min


class NormalizeAndRoundedCornersTransformation(
    private val context: Context,  // Контекст для доступа к ресурсам
    private val radius: Int,       // Радиус скругления в dp
    private val margin: Int,       // Отступ в dp
    private val cornerType: CornerType
) : BitmapTransformation() {

    enum class CornerType {
        ALL, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, LEFT, RIGHT
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val normalized = normalizeImage(toTransform, outWidth, outHeight)
        return roundedCorners(pool, normalized, outWidth, outHeight)
    }

    private fun normalizeImage(source: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val sourceWidth = source.width
        val sourceHeight = source.height

        // Рассчитываем соотношения сторон
        val widthRatio = outWidth.toFloat() / sourceWidth
        val heightRatio = outHeight.toFloat() / sourceHeight

        // Выбираем наименьшее соотношение, чтобы сохранить пропорции
        val scaleFactor = min(widthRatio, heightRatio)

        // Вычисляем новые размеры
        val targetWidth = (sourceWidth * scaleFactor).toInt()
        val targetHeight = (sourceHeight * scaleFactor).toInt()

        // Масштабируем изображение с учетом пропорций
        return Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true)
    }

    private fun roundedCorners(pool: BitmapPool, source: Bitmap, width: Int, height: Int): Bitmap {
        var output = pool[width, height, Bitmap.Config.ARGB_8888]
        if (output == null) {
            output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)
        paint.isAntiAlias = true

        // Преобразование радиуса и отступов из dp в пиксели
        val density = context.resources.displayMetrics.density
        val radiusPx = radius * density
        val marginPx = margin * density

        // Определяем радиусы углов для каждого CornerType
        val radii = when (cornerType) {
            CornerType.ALL -> floatArrayOf(
                radiusPx, radiusPx, // Top-left
                radiusPx, radiusPx, // Top-right
                radiusPx, radiusPx, // Bottom-right
                radiusPx, radiusPx  // Bottom-left
            )
            CornerType.TOP_LEFT -> floatArrayOf(
                radiusPx, radiusPx, // Top-left
                0f, 0f,             // Top-right
                0f, 0f,             // Bottom-right
                0f, 0f              // Bottom-left
            )
            CornerType.TOP_RIGHT -> floatArrayOf(
                0f, 0f,             // Top-left
                radiusPx, radiusPx, // Top-right
                0f, 0f,             // Bottom-right
                0f, 0f              // Bottom-left
            )
            CornerType.BOTTOM_LEFT -> floatArrayOf(
                0f, 0f,             // Top-left
                0f, 0f,             // Top-right
                0f, 0f,             // Bottom-right
                radiusPx, radiusPx  // Bottom-left
            )
            CornerType.BOTTOM_RIGHT -> floatArrayOf(
                0f, 0f,             // Top-left
                0f, 0f,             // Top-right
                radiusPx, radiusPx, // Bottom-right
                0f, 0f              // Bottom-left
            )
            CornerType.LEFT -> floatArrayOf(
                radiusPx, radiusPx, // Top-left
                0f, 0f,             // Top-right
                0f, 0f,             // Bottom-right
                radiusPx, radiusPx  // Bottom-left
            )
            CornerType.RIGHT -> floatArrayOf(
                0f, 0f,             // Top-left
                radiusPx, radiusPx, // Top-right
                radiusPx, radiusPx, // Bottom-right
                0f, 0f              // Bottom-left
            )
        }

        // Создаем путь с учетом радиусов углов и отступов
        val path = Path()
        path.addRoundRect(
            RectF(
                marginPx,
                marginPx,
                width.toFloat() - marginPx,
                height.toFloat() - marginPx
            ),
            radii,
            Path.Direction.CW
        )

        // Очищаем холст и применяем клип
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvas.clipPath(path)
        canvas.drawBitmap(source, null, RectF(0f, 0f, width.toFloat(), height.toFloat()), paint)

        return output
    }


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(("normalized_rounded_" + radius + "_" + margin + "_" + cornerType).toByteArray(CHARSET))
    }
}
