eoprint
=======

auto print for jag onsite contest

やってること
------------

1. ときどき apache のディレクトリ内容を表示するページをポーリングする
2. 差分を見て新しい .ps ファイルが増えているように見えたらダウンロードする
3. lpr コマンドに投げる
4. するとプログラムを動かしている手元の PC につながっているデフォルトプリンタで印刷が始まる

以上。

* .ps ファイルの生成は a2ps を使って別の cgi でやっている (eoprint は何もしない)

必要なもの
----------

* java (version 要件忘れた)
* javafx を使っているので GUI 環境
  * CUI 用に改造するのは難しくないはず

動かし方
---------

Mac なら USB で接続後，以下の通りに実行すれば動くはず．
Win は LPD ポートの設定などがあるため，少し面倒．

* git clone
```
git clone https://github.com/icpc-jag/eoprint.git
```

* compile (in)
```
cd eoprint
javac print/*.java
```

* build jar
```
jar cvf print.jar print
```

* Run
```
java -cp print.jar print.Main
```
