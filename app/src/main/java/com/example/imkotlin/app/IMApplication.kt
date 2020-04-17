package com.example.imkotlin.app

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import com.hyphenate.chat.EMOptions
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.media.AudioManager
import android.media.SoundPool
import cn.bmob.v3.Bmob
import com.example.imkotlin.R
import com.example.imkotlin.adapter.EMMessageListenerAdapter
import com.example.imkotlin.ui.activity.ChatActivity
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody

//由于应用可能在后台，所以在application里进行设置监听
class IMApplication : Application(){

    //后面完成实例初始化
    companion object {
        lateinit var instance: IMApplication
    }

    //使用SoundPool播放音频，第一个参数是多少条音频，第二个是类型，第三个质量
    val soundPool = SoundPool(2, AudioManager.STREAM_MUSIC, 0)

    val duan by lazy {
        soundPool.load(instance, R.raw.duan, 0) //instance就是context,0代表优先级
    }

    val yulu by lazy {
        soundPool.load(instance, R.raw.yulu, 0)
    }

    //appliaction没有onDestroy()方法，不能自己摧毁，收到消息后播放声音
    val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            //如果是在前台，则播放短的声音
            if (isForeground()){
                soundPool.play(duan, 1f,1f, 0, 0, 1f)
                //public final int play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
                //leftVolume/rightVolume左右声道，1f表示最大，loop循环，rate采样率
            } else {
                //如果是在后台，则播放长的声音
                soundPool.play(duan, 1f,1f, 0, 0, 1f)
                //后台显示通知栏消息
                showNotification(p0)
            }
        }
    }

    //后台收到消息弹出通知
    private fun showNotification(p0: MutableList<EMMessage>?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        p0?.forEach {
            var contentText = getString(R.string.no_text_message)
            if (it.type == EMMessage.Type.TXT){
                contentText = (it.body as EMTextMessageBody).message  //文本类型就替换消息内容
            }
            val intent = Intent(this, ChatActivity::class.java)  //由这里this跳转到->ChatActivity::class.java
            intent.putExtra("username", it.conversationId())
//            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)  //不是StartActivity(),PendingIntent.FLAG_UPDATE_CURRENT主要是跳转更新

            //使用TaskStackBuilder防止直接退出，退回到app
            val taskStackBuilder = TaskStackBuilder.create(this).addParentStack(ChatActivity::class.java).addNextIntent(intent)
            val pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            //还要设置
            // <activity android:name=".ui.activity.ChatActivity"
            //                  android:parentActivityName=".ui.activity.MainActivity"/>

            val notification = Notification.Builder(this)
                .setContentTitle(getString(R.string.receive_new_message))
                .setContentText(contentText)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.avatar1))
                .setSmallIcon(R.mipmap.ic_contact)
                .setContentIntent(pendingIntent)  //点击通知进入聊天界面
                .setAutoCancel(true)  //点击后自动消失
                .notification
            notificationManager.notify(1, notification) //1随便输的
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val options = EMOptions()  //默认好友请求是自动同意的，如果要手动同意需要在初始化SDK时调用 opptions.setAcceptInvitationAlways(false)
        // 默认添加好友时，是不需要验证的，改成需要验证
        //options.acceptInvitationAlways = false
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        //options.autoTransferMessageAttachments = true
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        //options.setAutoDownloadThumbnail(true)
        //环信初始化
        EMClient.getInstance().init(applicationContext, options)
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true)
        //Bmob初始化
        Bmob.initialize(this,"b4115d3b4633985540400b7391f57f67")

        EMClient.getInstance().chatManager().addMessageListener(messageListener)
    }

    //判断是否在前台
    private fun isForeground(): Boolean{
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //遍历所有app进程
        for (runningAppProgress in activityManager.runningAppProcesses){
            //如果进程的名字是包名
            if (runningAppProgress.processName == packageName){
                //找到了app的进程，判断是否在前台
                return runningAppProgress.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false //否则就是后台
    }
}