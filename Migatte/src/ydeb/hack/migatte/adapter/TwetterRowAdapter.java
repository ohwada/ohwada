package ydeb.hack.migatte.adapter;

import twitter4j.Tweet;
import ydeb.hack.migatte.R;
import ydeb.hack.migatte.util.HttpClient;
import ydeb.hack.migatte.util.ImageCache;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Twitter情報を１項目単位で構造化する
 *
 */
public class TwetterRowAdapter extends ArrayAdapter<Tweet> {

	/** レイアウト・インフレータ */
	private LayoutInflater inflater;

    /**
     * コンストラクターにて、
	 * ArrayAdapter を初期化する
     * @param context コンテキスト
     * @param textViewResourceId リソースID
     */
	public TwetterRowAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		inflater = (LayoutInflater)super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

    /**
     * ビューを取得する
     * @param position 表示する位置
     * @param convertView ビュー
     * @param parent 親のビュー
     * @return ビュー
     */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		RowHolder holder;
		if (v == null) {
			v = inflater.inflate(R.layout.tweet_row, parent, false);
			holder = new RowHolder();

			holder.id = (TextView)v.findViewById(R.id.tweetRow_textView_id);
			holder.image = (ImageView)v.findViewById(R.id.tweetRow_imageView_image);
			holder.text = (TextView)v.findViewById(R.id.tweetRow_textView_text);
			holder.date = (TextView)v.findViewById(R.id.tweetRow_textView_date);

			v.setTag(holder);
		} else {
			holder = (RowHolder)v.getTag();
		}

		Tweet result = super.getItem(position);
		holder.id.setText("@" + result.getFromUser());	
		holder.date.setText(result.getCreatedAt().toLocaleString());
		holder.text.setText(result.getText().toString());

		// Icon Image
		String url = result.getProfileImageUrl();
		holder.runImageTask(url);

		return v;
	}
	
    /**
     * インナークラス：
     * Twetterの１項目を構造化する
     */	
	private class RowHolder {

		/** ユーザID */
		public TextView id;

		/** バナー画像 */
		public ImageView image;

		/** 本文 */
		public TextView text;

		/** 作成日 */
		public TextView date;

		/** タスク実行中のフラグ */
		private boolean isTask = false;

    	/**
    	 * urlよりバナー画像を取得する
		 * 取得できなければ、tw_dicon1 (黄色のアイコン) で代用する
    	 * @param url 画像のurl
    	 */
		public void runImageTask(String url) {
			image.setTag(url);
			if (ImageCache.isKey(url)) {
				// Get cache image.
				image.setImageBitmap(ImageCache.getImage(url));
		
			} else {
				/* 取得できなければ、tw_dicon1 (黄色のアイコン) で代用する */
				image.setImageDrawable(getContext().getResources().getDrawable(R.drawable.tw_dicon1));
				if (!isTask) {
					ImageTask task = new ImageTask(image.getTag().toString());
					task.execute(url);
				}
			}
		}

    	/**
    	 * インナークラス：
    	 * バナー画像を取得する非同期処理
    	 */	
		class ImageTask extends AsyncTask<String, Void, Bitmap> {

			/* キャッシュのキー */
			String tag;

			/**
			 * コンストラクタにて、
			 * キャッシュのキーを設定する
			 * @param _tag キャッシュのキー
			 */
			public ImageTask(String _tag) {
				tag = _tag;
			}

			/**
			 * バックグランド処理の前に、
			 * タスク実行中のフラグを立てる
			 */
			@Override
			protected void onPreExecute() {
				isTask = true;
			}

			/**
			 * バックブランドにて、
			 * 画像を取得する
			 * @param params 0:url
			 * @return ビットマップ
			 */
			@Override
			protected Bitmap doInBackground(String... params) {
				String url = params[0];
				return HttpClient.getImage(url);
			}

			/**
			 * バックグランド処理が終わると、
			 * 画像をキャッシュに保存する
			 * @param bitmap ビットマップ
			 */
			@Override
			protected void onPostExecute(Bitmap bitmap) {
				if (tag.equals(image.getTag())) {
					image.setImageBitmap(bitmap);
					// Put cache image.
					ImageCache.putImage(tag, bitmap);
				}
				isTask = false;
			}
		}
	}
}
