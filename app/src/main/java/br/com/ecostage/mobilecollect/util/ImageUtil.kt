package br.com.ecostage.mobilecollect.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

/**
 * Created by cmaia on 7/28/17.
 */
class ImageUtil {
    companion object {
        fun compress(bitmap: Bitmap, format: Bitmap.CompressFormat, qualityLevel: Int) : ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(format, qualityLevel, stream)
            return stream.toByteArray()
        }

        fun compress(imagePath: String, format: Bitmap.CompressFormat, qualityLevel: Int) : ByteArray {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            val bufferStream = ByteArrayOutputStream()

            bitmap.compress(format, qualityLevel, bufferStream)

            return bufferStream.toByteArray()
        }

        fun decompress(compressedSnapshot: ByteArray) : Bitmap {
            return BitmapFactory.decodeByteArray(compressedSnapshot, 0, compressedSnapshot.size)
        }

        fun convertToBitmap(img: ByteArray) : Bitmap {
            return BitmapFactory.decodeByteArray(img, 0, img.size)
        }

    }
}