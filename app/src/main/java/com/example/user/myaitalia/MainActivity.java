package com.example.user.myaitalia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.example.user.myaitalia.modules.SignInActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.myaitalia.modules.PlanActivity.MY_PREFS_NAME;

public class MainActivity extends AhoyOnboarderActivity {

    private int iconWidth = 90;
    private int iconHeight = 90;
    private int marginTop = 20;
    private int marginBottom = 20;
    private int marginRight = 20;
    private int marginLeft = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int newComer = prefs.getInt("isNewComer", -1);
        if(newComer == 1) {
            startActivity(new Intent(this, SignInActivity.class));
        }

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Welcome",getResources().getString(R.string.onboard_page1), R.drawable.diet);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Our Belief", getResources().getString(R.string.onboard_page2), R.drawable.lunchbox512px);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Our Vision", getResources().getString(R.string.onboard_page3), R.drawable.ai_logo_circle_512px);
        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);
        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);

        int i = 0;
        for (AhoyOnboarderCard page : pages) {
            if(i%2 == 0)
            {
                page.setTitleColor(R.color.white);
                page.setDescriptionColor(R.color.white);
            }
            else{
                page.setTitleColor(R.color.black);
                page.setDescriptionColor(R.color.black);

            }
            i++;
        }

        setFinishButtonTitle("Finish");
        showNavigationControls(false);

        List<Integer> colorList = new ArrayList<>();
        colorList.add(R.color.solid_one);
        colorList.add(R.color.solid_two);
        colorList.add(R.color.solid_three);

        setColorBackground(colorList);


        setOnboardPages(pages);

    }

    @Override
    public void onFinishButtonPressed() {
        startActivity(new Intent(this, SignInActivity.class));
    }
}
