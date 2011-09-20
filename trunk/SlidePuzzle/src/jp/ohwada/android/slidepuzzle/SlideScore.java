package jp.ohwada.android.slidepuzzle;

import android.os.Parcel;
import android.os.Parcelable;

// Score Parcelable for slide pazzule
public class SlideScore 
	implements Parcelable 
{
	// setter & getter
	private int id;
	private int game;
	private int time;
	private int move;
	private String result;
	private String date;
	
	// === constracotr ===
	public SlideScore()
	{
		id = 0;
		game = 0;
		time = 0;
		move = 0;
		result = "";
		date = "";
	}

	// caller createFromParcel
	private SlideScore( Parcel in ) 
	{
		id = in.readInt();
		game = in.readInt();
		date = in.readString();
		time = in.readInt();
		move = in.readInt();
		result = in.readString();
	}
	
	// === setter & getter ===
	// --- id ---
	public void setId(int _id)
	{
		id = _id;
	}
	
	public int getIntId()
	{
		return id;
	}
	
	public String getStringId()
	{
		return Integer.toString( id );
	}

	// --- game ---
	public void setGame(int _game)
	{
		game = _game;
	}
	
	public int getIntGame()
	{		
		return game;
	}
	
	public String getStringGame()
	{
		return Integer.toString( game );
	}

	// --- date ---
	public void setDate(String _date)
	{
		date = _date;
	}
	
	public String getStringDate()
	{
		return date;
	}
	
	// --- time ---
	public void setTime(int _time)
	{
		time = _time;
	}
	
	public int getIntTime()
	{		
		return time;
	}
	
	public String getStringTime()
	{
		return Integer.toString( time );
	}
	
	// --- move ---
	public void setMove(int _move)
	{
		move = _move;
	}
	
	public int getIntMove()
	{		
		return move;
	}
	
	public String getStringMove()
	{
		return Integer.toString( move );
	}
	
	// --- result ---
	public void setResult(String _result)
	{
		result = _result;
	}
	
	public String getStringResult()
	{
		return result;
	}

	// --- csv format ---	
	public String getCsv()
	{
		String str = "";
		str += game+ "," ;
		str += time + "," ;
		str += move + "," ;
		str += result + "," ;
		str += date + "\n" ;
		return str;
     }
	             
	// === describe Contents ===
	@Override
	public int describeContents() 
	{
		return 0;
	}

	// === write To Parcel ===
	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeInt(id);
		dest.writeInt(game);
		dest.writeString(date);
		dest.writeInt(time);
		dest.writeInt(move);
		dest.writeString(result);		
	}

	// ===  Parcelable Creator ===	
	public static final Parcelable.Creator<SlideScore> CREATOR = 
		new Parcelable.Creator<SlideScore>() 
	{
		public SlideScore createFromParcel( Parcel in ) 
		{
			return new SlideScore( in );
		}

		public SlideScore[] newArray( int size ) 
		{
			return new SlideScore[ size ];
		}
	};

}
