package ydeb.hack.migatte.http;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * twitter4j を使用して、キーワードから Twitter を検索する
 *
 */
public class TwitterUtils {

	/**
	 * twitter4j を使用して、キーワードから Twitter を検索する
	 * @param keyword キーワード
	 * @param latitude 緯度
	 * @param longitude 経度
	 * @return Twitter情報のリスト
	 *
	 */
	public static List<Tweet> getTwitter(String keyword, String latitude, String longitude)
	throws TwitterException {
		
		Twitter twitter = new TwitterFactory().getInstance();
		QueryResult result = twitter.search(new Query(keyword));
		List<Tweet> tweets = result.getTweets();
		return tweets;
	}
}
