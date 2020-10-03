package app.mblackman.topmovies.data.database

import app.mblackman.topmovies.data.domain.Movie as DomainMovie
import app.mblackman.topmovies.data.domain.Rating as DomainRating

fun DomainMovie.toDatabaseObject(): MovieWithDetails =
    MovieWithDetails(
        Movie(
            this.id,
            this.title,
            this.year,
            this.rated,
            this.releaseDate,
            this.runtime,
            this.director,
            this.plotSummary,
            this.posterImgUrl,
            this.productionCompany,
            this.isFavorite
        ),
        this.genres.map { Genre(it, this.id) },
        this.ratings.map { it.toDatabaseObject(this.id) },
        this.writers.map { Writer(it, this.id) },
        this.actors.map { Actor(it, this.id) },
        this.languages.map { Language(it, this.id) },
        this.countries.map { Country(it, this.id) }
    )


fun DomainRating.toDatabaseObject(movieId: Long): Rating =
    Rating(
        this.source,
        this.value,
        movieId
    )

fun MovieWithDetails.toDomainObject() : DomainMovie =
    DomainMovie(
        id = this.movie.id,
        title = this.movie.title,
        year = this.movie.year,
        rated = this.movie.rated,
        genres = this.genres.map { it.name },
        languages = this.languages.map { it.name },
        countries = this.countries.map { it.name },
        ratings = this.ratings.map { it.toDomainObject() },
        releaseDate = this.movie.releaseDate,
        runtime = this.movie.runtime,
        director = this.movie.director,
        actors = this.actors.map { it.name },
        writers = this.writers.map { it.name },
        plotSummary = this.movie.plotSummary,
        posterImgUrl = this.movie.posterImgUrl,
        productionCompany = this.movie.productionCompany,
        isFavorite = this.movie.isFavorite
    )

fun Rating.toDomainObject(): DomainRating =
    DomainRating(
        this.source,
        this.value
    )