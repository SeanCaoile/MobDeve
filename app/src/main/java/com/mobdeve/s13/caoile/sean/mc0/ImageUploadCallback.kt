package com.mobdeve.s13.caoile.sean.mc0

interface ImageUploadCallback {
    fun onImageUploaded(imageURL: String)
    fun onImageUploadFailed(exception: Exception)
}