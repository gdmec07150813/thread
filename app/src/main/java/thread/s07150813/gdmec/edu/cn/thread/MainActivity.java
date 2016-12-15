package thread.s07150813.gdmec.edu.cn.thread;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView tv1 ;
    private int Seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.tv1);
        Date theLastDay = new Date(117,5,23);
        Date today = new Date();
        Seconds = (int)(theLastDay.getTime()-today.getTime())/1000;
    }
    public void anr(View v){
        for(int i=0;i<100000;i++){
            //响应超时出现图片
            BitmapFactory.decodeResource(getResources(),R.drawable.android);
        }
    }
    public void threadclass(View v){
         class ThreadDemo extends Thread{
             Random rn;
             public ThreadDemo(String name){
                 super(name);
                 rn = new Random();
             }
             public void run(){
                 for(int i=0;i<10;i++){
                      System.out.println(i+"  "+getName());
                     try{
                        sleep(rn.nextInt(1000));
                     }catch (Exception e){
                       e.printStackTrace();
                     }
                 }
                 System.out.println(getName()+" 完成");
             }
         }
        ThreadDemo td = new ThreadDemo("线程一");
        td.start();
        ThreadDemo td2 = new ThreadDemo("线程二");
        td2.start();
    }
    public void runableinterface(View v){
        class RunableDemo implements Runnable{
             Random rn;
            String name;

            public RunableDemo(String name){
                 this.name = name;
                 rn = new Random();
            }

            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    System.out.println(i+"  "+name);
                    try{
                        Thread.sleep(rn.nextInt(1000));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                System.out.println(name+"完成");
            }
        }
        Thread td = new Thread(new RunableDemo("线程一"));
        Thread td2 = new Thread(new RunableDemo("线程二"));
        td.start();
        td2.start();
        /*   RunableDemo rb = new RunableDmoe("name");
        *    Thread td = new Thread(rb);
        *    td.start();
        * */
    }
    /*timer是一种定时器，有计划的执行任务
    * timertask 是一个抽象类，它的子类能被、timer执行*/
    public void timertask(View v){
        class Mytimer extends TimerTask{
            Random rn;
            String name;
            public Mytimer(String name){
                this.name = name;
                rn = new Random();
            }

            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    System.out.println(i+"  "+name);
                    try{
                        Thread.sleep(rn.nextInt(1000));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                System.out.println(name+"完成");
            }
        }
        Timer timer1 = new Timer();
        Timer timer2 = new Timer();
        Mytimer mytimer1 = new Mytimer("线程一");
        Mytimer mytimer2 = new Mytimer("线程二");
        timer1.schedule(mytimer1,0);
        timer2.schedule(mytimer2,0);
    }
    public void handlermessage(View v){
        //创建handler对象处理信息，更新ui
        final Handler myhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    //遇到消息类型为1的消息就更新界面
                    case 1:
                        showmsg(String.valueOf(msg.arg1+msg.getData().get("attach").toString()));
                }
            }
        };
        //创建Mytask继承于TimerTask 抽象类
        class MyTask extends TimerTask{
             int countdown;
             double achievement1 = 1,achievement2 = 1;
            //构造方法，传入倒计时秒数
            public MyTask(int seconds){
               this.countdown = seconds;
            }

            @Override
            public void run() {
                //obtain()方法直接在消息池中取回一个可用的消息对象，比new Message(）方法效率高
                Message msg = Message.obtain();
                //定义消息标识为1
                msg.what = 1;
                //每次运行时把countdown减1，arg1和arg2是消息传递信息的高效率方法，只能是in类型
                msg.what = countdown--;
                achievement1 = achievement1*1.01;
                achievement2 = achievement2*1.02;
                //用bundle传递效率低
                Bundle bundle = new Bundle();
                bundle.putString("attach","\n努力多1%:"+achievement1+"\n努力多2%"+achievement2);
                msg.setData(bundle);
                //用Handler发送消息到消息队列，在尾部
                myhandler.sendMessage(msg);
            }
        }
        //创建Timer对象，并把MyTadsk定时后台执行
         Timer timer = new Timer();
        //timer.sechdule()方法可以是无限次执行
        timer.schedule(new MyTask(Seconds),1,1000);
    }
 //显示消息
    public void showmsg(String msg){
     tv1.setText(msg);
    }
    public void asynctask(View v){
         class LearHard extends AsyncTask<Long,String,String>{
             private Context context;
             final int duration = 10;
             int count = 0;
             public LearHard(Activity context){
                  this.context = context;
             }
             //耗时操作，在后台运行，在非ui线程中运行;
             @Override
             protected String doInBackground(Long... params) {
                 long num = params[0].longValue();
                 while (count<duration){
                     num--;
                     count++;
                     String status = "离毕业还有"+num+"秒,努力学习"+count+"秒";
                     //调用pushLishProgress,触发onProgressUpdate
                     publishProgress(status);
                     try{
                         Thread.sleep(1000);
                     }catch (Exception e){
                         e.printStackTrace();
                     }
                 }
                 return "这"+duration+"秒有收获，没虚度";
             }
             //这个方法在ui线程中，可以更新ui

             @Override
             protected void onProgressUpdate(String... values) {
                 ((MainActivity)context).tv1.setText(values[0]);
                 super.onProgressUpdate(values);
             }
             //执行耗时操作后处理UI线程事件，接受doInBackground的返回值，这个方法在UI中工作

             @Override
             protected void onPostExecute(String s) {
                 showmsg(s);
                 super.onPostExecute(s);
             }
         }
        LearHard learHard = new LearHard(this);
        learHard.execute((long)Seconds);
    }

}
