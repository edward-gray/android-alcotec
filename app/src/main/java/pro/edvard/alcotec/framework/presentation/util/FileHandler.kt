package pro.edvard.alcotec.framework.presentation.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

class FileHandler(private val context: Context) {

    fun getFile(dirName: String, fileName: String): File {
        val cw = ContextWrapper(context)
        val dir = cw.getDir(dirName, Context.MODE_PRIVATE)
        return File(dir, fileName)
    }

    fun saveImage(dirName: String, fileName: String, uri: Uri) {
        val bitmap = getImageBitmap(uri)
        val cw = ContextWrapper(context)
        val dir = cw.getDir(dirName, Context.MODE_PRIVATE)
        val newFile = File(dir, fileName)

        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = FileOutputStream(newFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getImageBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    uri
                )
            )
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }

}