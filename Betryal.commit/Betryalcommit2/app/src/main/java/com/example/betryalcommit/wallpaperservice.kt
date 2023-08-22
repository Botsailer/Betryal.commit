import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.IOException

object wallpaperset {
    @SuppressLint("ResourceType")
    fun setWallpaper(applicationContext: Context, data: String) {
        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
        val imageBytes = Base64.decode(data, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        try {
            wallpaperManager.setBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}