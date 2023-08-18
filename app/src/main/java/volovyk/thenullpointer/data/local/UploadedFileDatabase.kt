package volovyk.thenullpointer.data.local

interface UploadedFileDatabase {
    fun getUploadedFileDao(): UploadedFileDao
}