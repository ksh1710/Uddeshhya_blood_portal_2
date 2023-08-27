package com.example.uddeshhyabloodportal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uddeshhyabloodportal.databinding.ActivityFaqsBinding
import com.example.uddeshhyabloodportal.models.faqList
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds

class faqs : AppCompatActivity() {

    lateinit var binding: ActivityFaqsBinding
    lateinit var faqRv:RecyclerView
    lateinit var faqAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val FaqsList = mutableListOf<faqList>()
        binding= ActivityFaqsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}

        faqAdView = findViewById(R.id.faqAd)
        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
        faqAdView.loadAd(adRequest)

        faqAdView.adListener = object: AdListener() {
            override fun onAdClicked() {
                super.onAdClicked()
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                super.onAdFailedToLoad(adError)
                faqAdView.loadAd(adRequest)
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                super.onAdOpened()
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }




        FaqsList.add(faqList("Q1: How much blood is taken in one donation?","Ans: For a whole blood donation, approximately 0.5 L of blood is collected"))
        FaqsList.add(faqList("Q2: Are the history questions necessary every time I donate?","Ans: Yes, since this helps to ensure the safest possible blood supply. All donors must be asked the screening questions at each donation. Both AABB and FDA regulations specifically require that all blood donors complete the donor history questionnaire on the day of donation and prior to donating."))
        FaqsList.add(faqList("Q3: How often can I give blood?","Ans: Men can give blood every 12 weeks and women can give blood every 16 weeks. This is because men generally have higher iron levels than women. If you have genetic hemochromatosis, you may be able to donate as often as every six weeks."))
        FaqsList.add(faqList("Q4: How would i benefit from donationg blood?","Ans:" +
                "1.May reveal health problems(if any)\n" +
                "\n" +
                "2. Prevents Hemochromatosis\n" +
                "\n3. Maintain Cardiovascular health\n" +
                "\n4. May reduce the risk of developing cancer\n" +
                "\n5. Stimulates blood cell production\n" +
                "\n6. Help improve your mental state \n" +
                "\n"))
        FaqsList.add(faqList("Q5: I’m having my period. Can I donate?","Ans: Yes. Menstruating doesn’t affect your ability to donate. Enjoy your relaxing time on the donation couch and a tasty snack afterwards."))
        FaqsList.add(faqList("Q6: I am pregnant,can i donate?","Ans: Not right now, no. This is to protect your health and avoid causing stress to you and your baby’s circulation.  \n" +
                "\n" +
                "After you give birth, you’ll need to wait another nine months from delivery date to allow your body enough time to replenish its iron."))
        FaqsList.add(faqList("Q7: I have a tattoo or piercing. Can I donate?\n","Ans: Yes, but if it was recent you may need to wait for a bit. It depends what you got, where and when.   \n" +
                "\n" +
                "Tattoos: You can donate plasma straight away after a tattoo, as long as it was done in a licensed tattoo parlour. But, you’ll need to wait four months to give blood or platelets, no matter how big or small the tattoo is.  \n" +
                "Ear piercing: You can only donate plasma for the first 24 hours after having it done. After that, you can donate blood or platelets too.  \n" +
                "Body piercing: You can only donate plasma for the next 4 months after having it done. After that, you’re good to give blood or platelets.  \n" +
                "Whether it’s your ear or anywhere else, the piercing should be done with clean, single-use equipment. If it wasn’t or you aren’t sure, you’ll need to wait at least four months before you can donate anything."))
        FaqsList.add(faqList("Q8: I had several alcoholic drinks before going to give blood. Can I donate?","Ans: No, blood from anyone under the influence of alcohol is not preferred. Being intoxicated affects your ability to understand and answer the donor questionnaire and declaration, as well as your body’s ability to tolerate blood being taken."))
        FaqsList.add(faqList("Q9: Does my job affect whether I can donate?\n","Ans: For most people, not at all.  \n" +
                "\n" +
                "You can still donate blood if you work in an abattoir, or as a healthcare worker. But, if you're a professional athlete there are some things to consider before you donate. \n" +
                "\n" +
                "We don't recommend giving blood during a competitive period or season. It could have a minor, temporary impact on performance — it’s common to feel a bit tired after donating…\n" +
                "\n"))
        FaqsList.add(faqList("Q10: What age do I need to be to donate?","Ans: For your safety, there are minimum and maximum ages for blood donation. The minimum age is 18 and the maximum age is 75 for first-time donors."))



        faqRv= binding.FAQrv
        faqRv.adapter=faqAdapter(FaqsList)
        faqRv.layoutManager= LinearLayoutManager(this)

    }
}