package volovyk.thenullpointer.util

import java.util.Date
import java.util.concurrent.TimeUnit

fun Date.getDaysDifference(otherDate: Date): Long {
    // Calculate the time difference in milliseconds
    val timeDifferenceInMillis = otherDate.time - this.time

    // Convert the time difference from milliseconds to days
    return TimeUnit.MILLISECONDS.toDays(timeDifferenceInMillis)
}
