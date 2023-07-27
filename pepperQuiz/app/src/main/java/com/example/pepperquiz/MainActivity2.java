package com.example.pepperquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.AutonomousReactionImportance;
import com.aldebaran.qi.sdk.object.conversation.AutonomousReactionValidity;
import com.aldebaran.qi.sdk.object.conversation.Bookmark;
import com.aldebaran.qi.sdk.object.conversation.BookmarkStatus;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.aldebaran.qi.sdk.object.locale.Language;
import com.aldebaran.qi.sdk.object.locale.Locale;
import com.aldebaran.qi.sdk.object.locale.Region;
import com.example.pepperquiz.databinding.ActivityMain2Binding;

import java.util.Map;

public class MainActivity2 extends RobotActivity implements RobotLifecycleCallbacks {

    private ActivityMain2Binding binding;

    private QiChatbot qiChatbot;
    private  Chat chat;


    //Bookmark status tanımlama alanı
    private BookmarkStatus bookmarkDegistirStatus;
    private  BookmarkStatus bookmarkDegistirStatus2;
    private  BookmarkStatus bookmarkDegisitrStatus3;
    private  BookmarkStatus bookmarkDegisitrStatus4;


    //Bookmark tanımlama alanı
    private Bookmark proposalBookmark;
    private Bookmark proposalBookmark2;
    private Bookmark proposalBookmark3;
    private Bookmark proposalBookmark4;
    private Bookmark proposalBookmark5;
    private Bookmark bookmarkDegistir;
    private Bookmark bookmarkDegistir2;
    private Bookmark bookmarkDegistir3;
    private Bookmark bookmarkDegistir4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain2Binding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);

        binding.imageview.setImageResource(R.drawable.soru1);

    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // The robot focus is gained.

        Locale locale = new Locale(Language.TURKISH, Region.TURKEY);

        // Create a topic.
        Topic topic = TopicBuilder.with(qiContext)
                .withResource(R.raw.sorular)
                .build();

// Create a new QiChatbot.
        qiChatbot = QiChatbotBuilder.with(qiContext)
                .withTopic(topic)
                .withLocale(locale)
                .build();

// Create a new Chat action.
         chat = ChatBuilder.with(qiContext)
                .withChatbot(qiChatbot)
                 .withLocale(locale)
                .build();



        Map<String, Bookmark> bookmarks=topic.getBookmarks();
        proposalBookmark=bookmarks.get("soruSonu");
        proposalBookmark2=bookmarks.get("soru2basi");
        proposalBookmark3=bookmarks.get("soru3basi");
        proposalBookmark4=bookmarks.get("soru4basi");
        proposalBookmark5=bookmarks.get("son");
        bookmarkDegistir=bookmarks.get("soruDegistir");
        bookmarkDegistir2=bookmarks.get("soruDegistir2");
        bookmarkDegistir3=bookmarks.get("soruDegistir3");
        bookmarkDegistir4=bookmarks.get("soruDegistir4");


        bookmarkDegistirStatus=qiChatbot.bookmarkStatus(bookmarkDegistir);
        bookmarkDegistirStatus2=qiChatbot.bookmarkStatus(bookmarkDegistir2);
        bookmarkDegisitrStatus3=qiChatbot.bookmarkStatus(bookmarkDegistir3);
        bookmarkDegisitrStatus4=qiChatbot.bookmarkStatus(bookmarkDegistir4);



        bookmarkDegistirStatus.addOnReachedListener(()->{
            runOnUiThread(()->{

                binding.imageview.setImageResource(R.drawable.soru2);


            });

            qiChatbot.goToBookmark(proposalBookmark2, AutonomousReactionImportance.HIGH, AutonomousReactionValidity.IMMEDIATE);
        });

        bookmarkDegistirStatus2.addOnReachedListener(()->{
            runOnUiThread(()->{
                binding.imageview.setImageResource(R.drawable.soru3);
            });

            qiChatbot.goToBookmark(proposalBookmark3,AutonomousReactionImportance.HIGH,AutonomousReactionValidity.IMMEDIATE);

        });

        bookmarkDegisitrStatus3.addOnReachedListener(()->{
            runOnUiThread(()->{
                binding.imageview.setImageResource(R.drawable.soru4);

            });
            qiChatbot.goToBookmark(proposalBookmark4,AutonomousReactionImportance.HIGH,AutonomousReactionValidity.IMMEDIATE);
        });


        bookmarkDegisitrStatus4.addOnReachedListener(()->{

            qiChatbot.goToBookmark(proposalBookmark5,AutonomousReactionImportance.HIGH,AutonomousReactionValidity.IMMEDIATE);
            Intent intent =new Intent(MainActivity2.this,MainActivity.class);
            startActivity(intent);
        });


        chat.addOnStartedListener(this::sayProposal);
                chat.async().run();
    }

    @Override
    public void onRobotFocusLost() {
        // The robot focus is lost.
        if (chat != null) {
            chat.removeAllOnStartedListeners();
        }

        if (bookmarkDegistirStatus != null) {
            bookmarkDegistirStatus.removeAllOnReachedListeners();
        }

        if (bookmarkDegistirStatus2 != null) {
            bookmarkDegistirStatus2.removeAllOnReachedListeners();
        }
        if (bookmarkDegisitrStatus3 != null) {
            bookmarkDegisitrStatus3.removeAllOnReachedListeners();
        }
        if (bookmarkDegisitrStatus4 != null) {
            bookmarkDegisitrStatus4.removeAllOnReachedListeners();
        }

    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }

    private void sayProposal() {
        qiChatbot.goToBookmark(proposalBookmark, AutonomousReactionImportance.HIGH, AutonomousReactionValidity.IMMEDIATE);
    }


}
