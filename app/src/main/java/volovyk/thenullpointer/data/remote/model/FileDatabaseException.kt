package volovyk.thenullpointer.data.remote.model

sealed class FileDatabaseException : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)
    constructor() : super()

    data object UnsupportedMimeTypeException : FileDatabaseException()

    class UnsuccessfulRequestException : FileDatabaseException {
        constructor(throwable: Throwable) : super(throwable)
        constructor() : super()
    }
}