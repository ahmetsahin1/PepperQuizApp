package com.example.pepperquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
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
import com.example.pepperquiz.databinding.ActivityMainBinding;

import java.util.Map;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private ActivityMainBinding binding;
    private BookmarkStatus dogBookmarkStatus;
    private  Bookmark proposalBookmark;

    private Chat chat;
    private QiChatbot qiChatbot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);


        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);


        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  =new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });


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
                .withResource(R.raw.testi_baslat)
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


        Map<String, Bookmark> bookmarks = topic.getBookmarks();
        Bookmark bookmark=bookmarks.get("cevapBekle");
        proposalBookmark=bookmarks.get("giris");

        dogBookmarkStatus=qiChatbot.bookmarkStatus(bookmark);


        dogBookmarkStatus.addOnReachedListener(() -> {
            // React when the dog bookmark is reached.

           Intent intent =new Intent(MainActivity.this, MainActivity2.class);
           startActivity(intent);

            /*Animation myAnimation = AnimationBuilder.with(qiContext)
                    .withResources(R.raw.hello_a005)
                    .build();

         //Build the action.
            Animate animate = AnimateBuilder.with(qiContext)
                    .withAnimation(myAnimation)
                    .build();

       // Run the action asynchronously.
            animate.async().run();*/

        });

        chat.addOnStartedListener(this::sayfirstproposal);
        chat.async().run();




    }

    @Override
    public void onRobotFocusLost() {
        // The robot focus is lost.
        if (dogBookmarkStatus != null) {
            dogBookmarkStatus.removeAllOnReachedListeners();
        }
        if (chat != null) {
            chat.removeAllOnStartedListeners();
        }


    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.

    }
     void sayfirstproposal(){
        qiChatbot.goToBookmark(proposalBookmark, AutonomousReactionImportance.HIGH, AutonomousReactionValidity.IMMEDIATE);

    }
}
