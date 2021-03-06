package com.example.simpleshootinggame_v401

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var screenWidth = 0  //スクリーンの幅を格納する変数の宣言
    private var screenHeight = 0   //スクリーンの高さ格納する変数の宣言
    private lateinit var enemy :Enemy  //Enemyクラスの変数を宣言しておく
    val bmax = 20 // 同時発射できる弾の数
    var bulletArray = arrayOfNulls<Bullet?>(bmax)  // Bulletクラスの配列を宣言しておく
    var bi = 0  // これから発射する 弾 bulletArray[]　の添え字


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 爆発gifアニメーションの imageView を用意する
        Glide.with(this).load(R.drawable.daibakuhatsu).into(imageViewGif)
        imageViewGif.visibility = View.INVISIBLE

        // スクリーンの幅と高さを取得する
        val dMetrics = DisplayMetrics()  //DisplayMetrics のインスタンスを生成する
        windowManager.defaultDisplay.getMetrics(dMetrics)  //スクリーンサイズを取得しているらしい
        screenWidth = dMetrics.widthPixels  //スクリーンの幅を取得
        screenHeight = dMetrics.heightPixels  //スクリーンの高さを取得

        // Enemyクラスのインスタンスを生成する
        var x = 50F;
        var y = screenHeight.toFloat() * 0.1F
        enemy = Enemy(imageViewEnemy, x, y, screenWidth, screenHeight)

        // imageViewPlayer の初期位置の設定
        imageViewPlayer.x = 50F
        imageViewPlayer.y = screenHeight.toFloat() * 0.6F

        // Bulletクラス　インスタンスの配列を作る
        var imageView : ImageView
        for (i in bulletArray.indices){
            imageView = ImageView(this)  // ImageViewのインスタンスを生成
            imageView.setImageResource(R.drawable.arw02up)  //画像を設定する
            x = i * 25F
            y = screenHeight.toFloat() * 0.75F
            bulletArray[i] = Bullet(imageView, x, y, screenWidth, screenHeight)  // Bulletクラスのインスタンス生成
            layout.addView(bulletArray[i]?.imageView)  // 画面（layout）に追加する
            bulletArray[i]?.imageView?.visibility = View.VISIBLE  // 弾を可視にする
        }

        // タイマのインスタンスの生成
        val timer = MyCountDownTimer(150 * 60 * 1000, 10)
        timerText.text = "150:00"  // ←↑今は150分

        // タイマのスタート
        timer.start()

   }

    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {

        private var explosion_millisUntilFinished = 0L // 爆発したときの時刻（のようなもの）を保存するための変数
        private var auto = 1F  //オートプレイ用の変数

        override fun onTick(millisUntilFinished: Long) {

            val minute = millisUntilFinished / 1000L / 60L
            val second = millisUntilFinished / 1000 % 60
            timerText.text = "%1d:%2$02d".format(minute, second)

                if (enemy.state == "move") {  // 敵が動いているときは
                    enemy.move(3);  // 敵が左右に移動する
                }

            for (i in bulletArray.indices) {

                if (bulletArray[i]?.state == "move") {  // 弾が動いているときは

                    bulletArray[i]?.move(5)  // 弾が上に移動する

                    if (hit(enemy.imageView, bulletArray[i]!!.imageView) == true) { // 弾が敵に当たったか？

                        enemy.state = "stop"  // 敵の移動を止める

                        //enemy.imageView.setImageResource(R.drawable.s5z8k0g6)  //画像を爆発の画像に変える

                        // 敵を不可視にし、爆発gifアニメーション画像をその場所に表示する
                        enemy.imageView.visibility = View.INVISIBLE
                        imageViewGif.x = enemy.imageView.x
                        imageViewGif.y = enemy.imageView.y
                        imageViewGif.visibility = View.VISIBLE

                        explosion_millisUntilFinished =
                            millisUntilFinished// 爆発したときの時刻（のようなもの）を保存しておく

                        //弾を初期位置に戻す
                        bulletArray[i]?.imageView!!.y =
                            -100F  // y座標を表示範囲外にすると、Bulletクラスのmoveメソッドで、初期位置に戻される
                    }
                }
            }

            if (enemy.state == "stop") {  //敵が止まっているときは（爆発中のときは）
                if (explosion_millisUntilFinished - millisUntilFinished >= 4000) {  // 爆発の時間が経過したら

                    // imageViewEnemyの画像をロケットの画像に変える
                    //enemy.imageView.setImageResource(R.drawable.rocket)

                    // 爆発gifアニメーション画像を不可視にし、敵の画像を可視にする
                    imageViewGif.visibility = View.INVISIBLE
                    enemy.imageView.visibility = View.VISIBLE

                    enemy.state = "move"  // imageViewEnemy が移動する
                }
            }

//オートプレイ
/*            if ( millisUntilFinished / 1000 % 5 == 0L && bulletArray[0]?.state == "stop"){  //一定時間ごとに
                bulletArray[0]?.imageView!!.visibility = View.VISIBLE
                bulletArray[0]?.state = "move" //クラスBulletの実験  //弾を発射する
                bulletArray[0]?.imageView!!.x =imageViewPlayer.x + imageViewPlayer.width/2 -bulletArray[0]?.imageView!!.width/2
                bulletArray[0]?.imageView!!.y = imageViewPlayer.y
            }
            imageViewPlayer.x = imageViewPlayer.x + auto
            if (500 < imageViewPlayer.x){
                auto = -1.5F
            }
            else if (imageViewPlayer.x < 100){
                auto = 3F
            }
*/
        }

        override fun onFinish() {
            //timerText.text = "0:00"
            timerText.text = "--:--"                                                         //デバッグ用

        }
    }

    //当たり判定のメソッド　当たったら、trueを返す、当たっていなければFalseを返す
    fun hit(enemy: ImageView, bullet: ImageView): Boolean {
        if (enemy.y <= bullet.y && bullet.y <= enemy.y + enemy.height) {
            if (enemy.x <= bullet.x + bullet.width / 2 && bullet.x + bullet.width / 2 <= enemy.x + enemy.width) {
                return true
            }
        }
        return false
    }

    //画面タッチのメソッドの定義
    override fun onTouchEvent(event: MotionEvent): Boolean {

        textViewTouch.text = "X座標：${event.x}　Y座標：${event.y}"

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                textViewTouch.append("　ACTION_DOWN")
                imageViewPlayer.x = event.x
            }

            MotionEvent.ACTION_UP -> {
                textViewTouch.append("　ACTION_UP")

                bulletArray[bi]?.state = "move"
                bulletArray[bi]?.imageView?.x = imageViewPlayer.x + imageViewPlayer.width/2 - bulletArray[bi]?.imageView!!.width/2
                bulletArray[bi]?.imageView?.y = imageViewPlayer.y
                bulletArray[bi]?.imageView?.visibility = View.VISIBLE  // 弾を可視にする

                bi = ++bi % bmax
            }

            MotionEvent.ACTION_MOVE -> {
                textViewTouch.append("　ACTION_MOVE")
                imageViewPlayer.x = event.x
            }

            MotionEvent.ACTION_CANCEL -> {
                textViewTouch.append("　ACTION_CANCEL")
            }
        }

        return true

    }
}