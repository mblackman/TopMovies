package app.mblackman.topmovies.data.database

import androidx.room.TypeConverter
import app.mblackman.topmovies.data.common.RatingSource
import java.time.LocalDate

/**
 * Converts used by Room to convert between types.
 */
class Converters {
    /**
     * Converts a [String] into a [LocalDate].
     *
     * @param value The [String] to convert.
     * @return The [LocalDate] from the conversion, null if conversion not possible.
     */
    @TypeConverter
    fun fromString(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    /**
     * Converts a [LocalDate] into a [String].
     *
     * @param date The [LocalDate] to convert.
     * @return The [String] from the conversion, null if conversion not possible.
     */
    @TypeConverter
    fun dateToString(date: LocalDate?): String? {
        return date?.toString()
    }

    /**
     * Converts a [Int] into a [RatingSource].
     *
     * @param value The [Int] to convert.
     * @return The [RatingSource] from the conversion, null if conversion not possible.
     */
    @TypeConverter
    fun fromInt(value: Int?): RatingSource? = value?.toEnum<RatingSource>()

    /**
     * Converts a [RatingSource] into a [Int].
     *
     * @param value The [RatingSource] to convert.
     * @return The [Int] from the conversion, null if conversion not possible.
     */
    @TypeConverter
    fun ratingSourceToInt(value: RatingSource?): Int? = value?.toInt()

    /**
     * Converts an enum into its [Int] representation.
     */
    private fun <T : Enum<T>> T.toInt(): Int = this.ordinal

    /**
     * Converts an [Int] into an enum value.
     */
    private inline fun <reified T : Enum<T>> Int.toEnum(): T = enumValues<T>()[this]
}