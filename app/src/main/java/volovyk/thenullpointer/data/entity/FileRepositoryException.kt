package volovyk.thenullpointer.data.entity

sealed class FileRepositoryException : RuntimeException() {
    data object UnsupportedFileTypeException : FileRepositoryException()
    data object FileUploadException : FileRepositoryException()
}