package com.example.stopwatch

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer


class UpgradeMainActivity : AppCompatActivity(), View.OnClickListener {

    // 1. 변수 초기화 lateinit : 컴파일시 값을 지정하지 않아도 에러를 발생시키지 않는다.
    private lateinit var btn_start: Button
    private lateinit var btn_refresh: Button
    private lateinit var tv_millisecond: TextView
    private lateinit var tv_second: TextView
    private lateinit var tv_minute: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_upgrade)

        //2. 변수에 객체 아이디 연결
        btn_start = findViewById(R.id.btn_start)
        btn_refresh = findViewById(R.id.btn_refresh)
        tv_millisecond = findViewById(R.id.tv_millisecond)
        tv_second = findViewById(R.id.tv_second)
        tv_minute = findViewById(R.id.tv_minute)

        //
        btn_start.setOnClickListener(this)
        btn_refresh.setOnClickListener(this)
    }

    var isRunning = false

    // 4. 버튼클릭 골력 생성
    override fun onClick(v: View?) {
            when(v?.id){
                // 시작버튼은 -> 타이머가 작동중이면 일시정지 버튼으로, 타이머가 작동하지 않고 있다면 시작버튼으로 변경.
                R.id.btn_start -> {
                    if(isRunning){
                        pause()
                    }else{
                        start()
                    }
                }
                R.id.btn_refresh -> {
                    refresh()
                }
            }
    }
    
    // 3. 버튼 기능의 골격 생성
    var timer : Timer? = null // Timer 자바에서 제공하는 클래스
    var time = 0

    private fun start() {
        btn_start.text = getString(R.string.pause_eng)
        isRunning = true

        //스톱워치를 시작하는 로직, 타이머는 항상 백그라운드에서 실행
        timer = timer(period = 10) {
            // 타이머 단위 (ms) , 10ms -> 0.01s
            time++

            // 시간 계산
            val milli_second = time % 100
            val second = (time % 6000) / 100
            val minute = time / 6000

            // 메인스레드 자원에 접근해야 함으로 스레드 함수를 사용하여 처리해 준다.
            runOnUiThread {
                if (isRunning) {
                    tv_millisecond.text =
                        if (milli_second < 10) ".0${milli_second}" else ".${milli_second}"
                    tv_second.text = if (second < 10) ":0${second}" else ":${second}"
                    tv_minute.text = "${minute}"
                }
            };
        }

    }

    private fun pause(){
        btn_start.text = getString(R.string.start_eng)
        isRunning = false
        timer?.cancel()
    }

    private fun refresh(){
        timer?.cancel()	         // ❶

        btn_start.text = getString(R.string.start_eng)
        isRunning = false     // ❸

        time = 0
        tv_millisecond.text = ".00"
        tv_second.text = ":00"
        tv_minute.text = "00"
    }

}