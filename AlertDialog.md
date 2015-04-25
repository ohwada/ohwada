# 概要 #
Dialog に OnShowListener を実装する<br>

Dialog の <a href='http://developer.android.com/intl/ja/reference/android/content/DialogInterface.OnShowListener.html'>OnShowListener</a> は API 8 (v2.2) から公開されていますが。<br>
それ以前のバージョンでも @hide 属性でソースに入っています。<br>
Dialog のソースを見たところ、メッセージハンドラーで実現しています。<br>
v1.6 の Dialog に OnShowListener を実装してみました。<br>

<h1>ソースコード</h1>
<a href='http://code.google.com/p/ohwada/source/browse/#svn%2Ftrunk%2FAlertDialogSample'>http://code.google.com/p/ohwada/source/browse/#svn%2Ftrunk%2FAlertDialogSample</a>

<h1>スクリーンショット</h1>
Dialog が表示されると、onShow という Toast を表示する。<br>
<img src='http://ohwada.googlecode.com/files/20110927alertdialog.png' />