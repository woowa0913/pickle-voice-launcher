package com.parksangu.picklevoice

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. 피클 실시간 음성 통화 딥링크 호출
        val callUri = Uri.parse("pickle://call")
        val deepLinkIntent = Intent(Intent.ACTION_VIEW, callUri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val launched = runCatching { startActivity(deepLinkIntent) }.isSuccess

        // 2. 딥링크 미지원 시 피클 메인 앱 실행 폴백
        if (!launched) {
            runCatching {
                val fallbackIntent = packageManager.getLaunchIntentForPackage(PICKLE_PKG)?.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                if (fallbackIntent != null) {
                    startActivity(fallbackIntent)
                }
            }
        }

        // 3. 화면 깜빡임 없이 즉시 종료
        finish()
    }

    companion object {
        private const val PICKLE_PKG = "com.picklemem.app"
    }
}
