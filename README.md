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

* java（`brew install openjdk@17` でインストールできるもので動作が確認できた）
* javafx を使っているので GUI 環境
  * CUI 用に改造するのは難しくないはず
* [OpenJFX](https://gluonhq.com/products/javafx/)（Java 11 から JavaFX が同梱されなくなってしまったので）

動かし方
---------

Mac なら USB で接続後，以下の通りに実行すれば動くはず．
Win は LPD ポートの設定などがあるため，少し面倒．

* Clone repository
```
git clone https://github.com/icpc-jag/eoprint.git
```

* Download OpenJFX

 [OpenJFX のダウンロードリンクから辿れるサイト](https://gluonhq.com/products/javafx/) から利用している OS、アーキテクチャの SDK をダウンロードする。

展開したものを eoprint ディレクトリ内に javafx という名前で配置する

```
cd eoprint
mv ~/Downloads/openjfx-20.0.2_osx-x64_bin-sdk.zip .
unzip openjfx-20.0.2_osx-x64_bin-sdk.zip
mv javafx-sdk-20.0.2/lib javafx

```

* Compile
```
javac --module-path javafx --add-modules=javafx.controls,javafx.fxml print/*.java
```

* Build jar
```
jar cvf print.jar print
```

* Run
```
java --module-path javafx --add-modules=javafx.controls,javafx.fxml -cp print.jar print.Main
```
