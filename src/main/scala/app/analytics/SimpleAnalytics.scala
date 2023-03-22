package app.analytics

import org.apache.spark.HashPartitioner
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel.MEMORY_AND_DISK
import org.joda.time.DateTime


class SimpleAnalytics() extends Serializable {

  private var ratingsPartitioner: HashPartitioner = null
  private var moviesPartitioner: HashPartitioner = null
  private var usedRatings: RDD[(Int, Int, Option[Double], Double, Int)] = null
  private var usedMovies: RDD[(Int, String, List[String])] = null

  def init(
            ratings: RDD[(Int, Int, Option[Double], Double, Int)],
            movie: RDD[(Int, String, List[String])]
          ): Unit = {
    ratingsPartitioner = new HashPartitioner(ratings.getNumPartitions)
    moviesPartitioner = new HashPartitioner(movie.getNumPartitions)
    usedRatings = ratings
    usedMovies = movie
  }
  def getNumberOfMoviesRatedEachYear: RDD[(Int, Int)] = {
    val moviesByYear = usedRatings.map { rating =>
      val dateTime = new DateTime(rating._5 * 1000L)
      val year = dateTime.getYear
      (year, rating._2)
    }
    val numMoviesByYear = moviesByYear.distinct.groupBy(_._1).mapValues(_.size)
    numMoviesByYear
  }

  def getMostRatedMovieEachYear: RDD[(Int, String)] = {
    ???
  }

  def getMostRatedGenreEachYear: RDD[(Int, List[String])] = ???

  // Note: if two genre has the same number of rating, return the first one based on lexicographical sorting on genre.
  def getMostAndLeastRatedGenreAllTime: ((String, Int), (String, Int)) = ???

  /**
   * Filter the movies RDD having the required genres
   *
   * @param movies         RDD of movies dataset
   * @param requiredGenres RDD of genres to filter movies
   * @return The RDD for the movies which are in the supplied genres
   */
  def getAllMoviesByGenre(movies: RDD[(Int, String, List[String])],
                          requiredGenres: RDD[String]): RDD[String] = {
    //val requiredGenresSet = ???
    ???
  }

  /**
   * Filter the movies RDD having the required genres
   * HINT: use the broadcast callback to broadcast requiresGenres to all Spark executors
   *
   * @param movies            RDD of movies dataset
   * @param requiredGenres    List of genres to filter movies
   * @param broadcastCallback Callback function to broadcast variables to all Spark executors
   *                          (https://spark.apache.org/docs/2.4.8/rdd-programming-guide.html#broadcast-variables)
   * @return The RDD for the movies which are in the supplied genres
   */
  def getAllMoviesByGenre_usingBroadcast(movies: RDD[(Int, String, List[String])],
                                         requiredGenres: List[String],
                                         broadcastCallback: List[String] => Broadcast[List[String]]): RDD[String] = ???

}

