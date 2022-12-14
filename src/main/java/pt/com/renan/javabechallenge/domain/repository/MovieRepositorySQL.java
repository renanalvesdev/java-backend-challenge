package pt.com.renan.javabechallenge.domain.repository;

public class MovieRepositorySQL {

	public static final String SUGESTED_MOVIES_QUERY = "WITH USER_FAVORITE_MOVIES AS (\r\n"
			+ "    (SELECT * FROM tb_favorite_movies where user_id = :userId)\r\n"
			+ "),\r\n"
			+ "\r\n"
			+ "USERS_WITH_COMMON_FAVORITES_AS_USER AS (\r\n"
			+ "    SELECT tbfm.user_id, tbfm.movie_id\r\n"
			+ "        FROM tb_favorite_movies tbfm\r\n"
			+ "        RIGHT JOIN  USER_FAVORITE_MOVIES tbfu\r\n"
			+ "        ON  tbfu.movie_id = tbfm.movie_id\r\n"
			+ "        INNER JOIN movie m\r\n"
			+ "        ON m.id = tbfu.movie_id\r\n"
			+ "        WHERE tbfm.user_id != :userId\r\n"
			+ "        ORDER BY movie_id ASC\r\n"
			+ "),\r\n"
			+ "\r\n"
			+ "USERS_COMMON_FAVORITES_NUMBER_AS_USER AS (\r\n"
			+ "    SELECT uwcfau.user_id, count(user_id) as total\r\n"
			+ "    FROM USERS_WITH_COMMON_FAVORITES_AS_USER uwcfau\r\n"
			+ "    GROUP BY uwcfau.user_id    \r\n"
			+ "),\r\n"
			+ "\r\n"
			+ "COMMON_FAVORITES_NUMBER_MAX AS (\r\n"
			+ "    SELECT MAX(total) FROM USERS_COMMON_FAVORITES_NUMBER_AS_USER\r\n"
			+ "),\r\n"
			+ "\r\n"
			+ "USERS_MOST_COMMON_FAVORITES_NUMBER_AS_USER AS (\r\n"
			+ "    SELECT * FROM USERS_COMMON_FAVORITES_NUMBER_AS_USER ucfnau\r\n"
			+ "    WHERE ucfnau.total = (SELECT * FROM COMMON_FAVORITES_NUMBER_MAX)\r\n"
			+ "),\r\n"
			+ "\r\n"
			+ "FAVORITE_MOVIES_CANDIDATES AS (\r\n"
			+ "    SELECT title,stars,id FROM USERS_MOST_COMMON_FAVORITES_NUMBER_AS_USER umcfnau\r\n"
			+ "    INNER JOIN tb_favorite_movies tbfm\r\n"
			+ "    ON tbfm.user_id = umcfnau.user_id\r\n"
			+ "    INNER JOIN movie\r\n"
			+ "    ON movie.id = tbfm.movie_id\r\n"
			+ "    WHERE tbfm.movie_id NOT IN (SELECT movie_id from USER_FAVORITE_MOVIES)\r\n"
			+ ")\r\n"
			+ "\r\n"
			+ "SELECT * FROM FAVORITE_MOVIES_CANDIDATES\r\n"
			+ "ORDER BY RAND()\r\n"
			+ "LIMIT 1";
	
	public static final String RANDOM_MOVIE_QUERY = "SELECT * FROM MOVIE\r\n"
			+ "WHERE MOVIE.ID \r\n"
			+ "NOT IN ((SELECT MOVIE_ID FROM tb_favorite_movies where user_id = :userId))\r\n"
			+ "ORDER BY RAND()\r\n"
			+ "LIMIT 1";
}
