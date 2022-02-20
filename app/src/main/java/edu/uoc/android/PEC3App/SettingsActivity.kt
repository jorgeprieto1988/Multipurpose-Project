package edu.uoc.android.PEC3App

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.File
import java.io.IOException
import androidx.core.net.toUri

/**
 * Settings Activity
 * ACtivity that opens the camara and saves the photo
 */
class SettingsActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    var currentPhotoPath: String = ""
    val REQUEST_TAKE_PHOTO = 1

    /**
     * onCreate
     * Checks if there is already a photo in the path, elsewhere shows a text stating there is no photo
     * Button to take a photo
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        currentPhotoPath = getExternalFilesDir(null)?.path.toString() + File.separator + "UOCImageApp" + File.separator + "imageapp.jpg"
        val file= File(currentPhotoPath)
        if(file.exists()) {
            imagen_foto_uploaded.setImageURI(file.toUri())
        }
        else
            text_foto.text = getString(R.string.no_photo)
        //Listener for the button to take a photo
        button_foto.setOnClickListener(View.OnClickListener {
            dispatchTakePictureIntent()
        })
    }


    /**
     * Intent to try to take the photo
     */
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    val toast = Toast.makeText(this, getString(R.string.img_error), Toast.LENGTH_SHORT)
                    toast.show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    /**
     * After taking photo will try to save it into memory
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
                val f = File(currentPhotoPath)
                mediaScanIntent.data = Uri.fromFile(f)
                sendBroadcast(mediaScanIntent)

            }

            val targetW: Int = imagen_foto_uploaded.width
            val targetH: Int = imagen_foto_uploaded.height

            val bmOptions = BitmapFactory.Options().apply {
                // Get the dimensions of the bitmap
                inJustDecodeBounds = true

                val photoW: Int = outWidth
                val photoH: Int = outHeight

                // Determine how much to scale down the image
                val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

                // Decode the image file into a Bitmap sized to fill the View
                inJustDecodeBounds = false
                inSampleSize = scaleFactor
                inPurgeable = true
            }
            BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
                imagen_foto_uploaded.setImageBitmap(bitmap)
                val toast = Toast.makeText(this, getString(R.string.img_ok), Toast.LENGTH_SHORT)
                toast.show()
            }
            text_foto.text = ""
        }
    }

    /**
     * Returns a file with the valid path
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        //val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(null)
        val storagePath = storageDir?.path + File.separator + "UOCImageApp"
        val endFile = File(storagePath)
        endFile.mkdirs()
        return File(endFile ,"imageapp.jpg").apply{
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }

    }




}
