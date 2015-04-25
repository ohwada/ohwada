# 概要 #

1/10秒周期で更新する Chronometer のデモ<br>

Android 標準の Chronometer クラス (android.widget.Chronometer) だと、１秒周期で更新する。<br>
標準の Chronometer クラスをコピーして、1/10秒周期の変更する。<br>
普通は既存クラスを継承するのだが、<br>
標準の Chronometer クラスには、private なメソッドが多くて、<br>
さほどコード量が減らなかったので、コピーして変更した。<br>

<h1>クラスの説明</h1>

MyChronometer : 新規作成した Chronometer クラス<br>
　1/10秒周期で更新する。<br>
RemotableViewMethod : android/view/RemotableViewMethod.java 	と同じもの<br>
　 import 出来なかったので、コピーした。<br>
　MyChronometer で必要となる。<br>

<h1>ソース</h1>

<a href='http://code.google.com/p/ohwada/source/browse/trunk/Chronometer/'>http://code.google.com/p/ohwada/source/browse/trunk/Chronometer/</a>

<h1>スクリーンショット</h1>

<img src='http://ohwada.googlecode.com/files/20110822chronometer.png' />