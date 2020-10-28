package com.example.simpleshootinggame_v401

import android.widget.ImageView

public class Enemy(val imageView:ImageView, x:Float, y:Float, val screenwidth: Int, val screenheight:Int) {
    // x,yは表示する座標の初期値。  screewidthはスクリーンの横幅。screeheightはスクリーンの高さ。

    private var dir = +1;
    public var state = "move"  // Enemyインスタンスの状態を保存する。
                               //"move"：Enemyインスタンスは動いている。  "stop"：Enemyインスタンスは動いていない。
    init {
        imageView.x = x;
        imageView.y = y;
    }

    fun move(x :Int): Int{  // xは移動量

        imageView.x = imageView.x + x * dir  // xだけ移動する

        if(screenwidth - imageView.width < imageView.x ){  // 移動した後に、右端を越えたら
            dir = -1  // 移動を左向きにする
        }
        else if(imageView.x < 0 ){  // 移動した後に、左端を越えたら
            dir = +1  // 移動右向きにする
        }
        return 1;
    }
}