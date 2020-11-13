package com.example.simpleshootinggame_v401

import android.view.View
import android.widget.ImageView

class Bullet(val imageView: ImageView, val xs:Float, val ys:Float, val screenwidth: Int, val screenheight:Int) {

    public var state = "stop"  // 弾の状態を保存する。
                               //"move"：弾は動いている。  "stop"：弾は動いていない。
    init {
        imageView.x = xs;
        imageView.y = ys;
        //imageView.visibility = View.INVISIBLE  // 弾を不可視にする
    }

    public fun move(y:Int){  // yは移動量

        imageView.y = imageView.y - y  // yだけ、上に移動する

        if(imageView.y <= 0){  //画面の上端になったら
            state = "stop"
            imageView.x = xs // 位置を初期位置にする
            imageView.y = ys // 位置を初期位置にする
            imageView.visibility = View.INVISIBLE  // 弾を不可視にする
        }

    }
}