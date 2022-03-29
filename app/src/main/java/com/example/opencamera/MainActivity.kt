package com.example.opencamera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var cameraButton = findViewById<Button>(R.id.cameraButton)
        cameraButton.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
            val fileProvider = FileProvider.getUriForFile(this, "edu.standard.rkpandey.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            }
            else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDir = getExternalFilesDir((Environment.DIRECTORY_PICTURES))
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //val takenImage = data?.extras?.get("data") as Bitmap
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            var imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(takenImage)
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}