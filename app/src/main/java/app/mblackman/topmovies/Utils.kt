package app.mblackman.topmovies

/**
 * Splits a comma separated string into a trimmed list of strings.
 */
fun String?.splitInput(delimiter: Char = ',') =
    this?.split(delimiter)?.map { it.trim() } ?: emptyList()