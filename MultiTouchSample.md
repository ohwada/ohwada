# 概要 #

マルチタッチのサンプル<br>
画像のタッチすると色が変化する。<br>

下記の６つの状態を認識する<br>
(1) off 緑色 タッチしていない状態<br>
(2) on 赤色 一度タッチした状態<br>
タッチする毎に (1)緑色 と (2)赤色 に変化する<br>
(3) storng 黄色 強くタッチした状態<br>
(4) weak 薄い黄色 弱くタッチした状態<br>
(5) out 白色 画像からタッチが外れた状態<br>
(6) in 灰色 画像にタッチが当たった状態<br>

<h1>クラスの説明</h1>

MultiTouchEvent イベントのアクション名を取得する<br>
MultiTouchView ビューがタッチされたを判定する<br>
MultiTouchImageView 画像がタッチされたときの処理<br>

<h1>ソース</h1>

<a href='http://code.google.com/p/ohwada/source/browse/MultiTouchSample/'>http://code.google.com/p/ohwada/source/browse/MultiTouchSample/</a>

<h1>スクリーンショット</h1>

<h2>ショット１</h2>
２つタッチしたところ。<br>
１つ目は強く(黄色)、２つ目は弱く(薄い黄色)<br>
<br>
<img src='http://ohwada.googlecode.com/files/20110711two_down.png' />

<h2>ショット２</h2>
２つタッチした後で。<br>
２つ目を画像から外した(白色)<br>
<br>
<img src='http://ohwada.googlecode.com/files/20110711down_out.png' />