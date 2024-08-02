package com.arrowwould.womenhomeworkout.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.activity.MainActivity
import com.arrowwould.womenhomeworkout.activity.ProfileActivity
import com.arrowwould.womenhomeworkout.activity.ReminderActivity
import com.arrowwould.womenhomeworkout.common.AppControl
import com.google.android.gms.ads.AdView
import com.arrowwould.womenhomeworkout.ConstantAd
import com.utillity.db.DataHelper
import com.utillity.objects.*
import kotlinx.android.synthetic.main.fragment_my_training.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var rootView: View
    private lateinit var mContext: Context

    var boolIsSelectEngine = false

    private lateinit var tvCountDown: TextView
    private lateinit var tvRestTime: TextView
    private lateinit var swtKeepScreenOn: Switch
    private lateinit var rlTestVoice: RelativeLayout
    private lateinit var rlSelectTTSEngine: RelativeLayout
    private lateinit var rlDownloadTTSEngine: RelativeLayout
    private lateinit var rlDeviceTTSSetting: RelativeLayout
    private lateinit var rlHealthData: RelativeLayout
    private lateinit var rlReminder: RelativeLayout
    private lateinit var rlMetricImperialUnit: RelativeLayout
    private lateinit var rlShareWithFriend: RelativeLayout
    private lateinit var rlRateUs: RelativeLayout
    private lateinit var rlFeedBack: RelativeLayout
    private lateinit var rlPrivacyPolicy: RelativeLayout
    private lateinit var rlSoundOption: RelativeLayout
    private lateinit var rlCountDown: RelativeLayout
    private lateinit var rlRestSet: RelativeLayout
    private lateinit var rlRestartProgress: RelativeLayout
    private lateinit var adView: AdView
    private lateinit var llAdView: RelativeLayout
    private lateinit var llAdViewFacebook: LinearLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        mContext = rootView.context

        init()
        initAction()

        return rootView
    }

    private fun init() {

        tvCountDown = rootView.findViewById(R.id.tvCountDown)
        tvRestTime = rootView.findViewById(R.id.tvRestTime)
        swtKeepScreenOn = rootView.findViewById(R.id.swtKeepScreenOn)
        rlTestVoice = rootView.findViewById(R.id.rlTestVoice)
        rlSelectTTSEngine = rootView.findViewById(R.id.rlSelectTTSEngine)
        rlDownloadTTSEngine = rootView.findViewById(R.id.rlDownloadTTSEngine)
        rlDeviceTTSSetting = rootView.findViewById(R.id.rlDeviceTTSSetting)
        rlHealthData = rootView.findViewById(R.id.rlHealthData)
        rlReminder = rootView.findViewById(R.id.rlReminder)
        rlMetricImperialUnit = rootView.findViewById(R.id.rlMetricImperialUnit)
        rlShareWithFriend = rootView.findViewById(R.id.rlShareWithFriend)
        rlRateUs = rootView.findViewById(R.id.rlRateUs)
        rlFeedBack = rootView.findViewById(R.id.rlFeedBack)
        rlPrivacyPolicy = rootView.findViewById(R.id.rlPrivacyPolicy)
        rlSoundOption = rootView.findViewById(R.id.rlSoundOption)
        rlCountDown = rootView.findViewById(R.id.rlCountDown)
        rlRestSet = rootView.findViewById(R.id.rlRestSet)
        rlRestartProgress = rootView.findViewById(R.id.rlRestartProgress)

        ConstantAd.setBannerAdd(rootView.adMobView,requireContext())

        try {
            tvCountDown.text = LocalDB.getCountDownTime(mContext).toString().plus(" secs")
            tvRestTime.text = LocalDB.getRestTime(mContext).toString().plus(" secs")
            swtKeepScreenOn.isChecked = LocalDB.getKeepScreen(mContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initAction() {

        rlTestVoice.setOnClickListener {
            boolIsSelectEngine = false
            AppControl.speechText(mContext, getString(R.string.did_you_hear_test_voice))
            Thread.sleep(1000)
            confirmVoiceTest(mContext, "", getString(R.string.did_you_hear_test_voice), CommonString.CONFIRM_VOICE_TEST)
        }

        rlSelectTTSEngine.setOnClickListener {
            boolIsSelectEngine = true
            selectTTSEngine()
        }

        rlDownloadTTSEngine.setOnClickListener {
            CommonUtility.downloadTTSEngine(mContext)
        }

        rlDeviceTTSSetting.setOnClickListener {
            val intent = Intent("com.android.settings.TTS_SETTINGS")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        rlHealthData.setOnClickListener {
            val intent = Intent(mContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        rlReminder.setOnClickListener {
            val intent = Intent(mContext, ReminderActivity::class.java)
            startActivity(intent)
        }



        rlShareWithFriend.setOnClickListener {
            val link = "https://play.google.com/store/apps/details?id=${mContext.packageName}"
            val strSubject = ""
            val strText =
                "I'm training with ${getString(R.string.app_name)} and am getting great results." +
                        "\n\n" +
                        "Here are workout for all your main muscle groups to help you build and tone muscles - no equipment needed. Challenge yourself!" +
                        "\n\n" +
                        "Download the app:$link"

            CommonUtility.shareStringLink(mContext, strSubject, strText)
        }

        rlRateUs.setOnClickListener {
            CommonUtility.rateUs(mContext)
        }

        rlFeedBack.setOnClickListener {
            CommonUtility.contactUs(mContext)
        }

        rlPrivacyPolicy.setOnClickListener {
            CommonUtility.openUrl(mContext, getString(R.string.privacy_policy_link))
        }

        rlSoundOption.setOnClickListener { CommonDialog.soundOptionDialog(mContext) }

        swtKeepScreenOn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                LocalDB.setKeepScreen(mContext, true)
            } else {
                LocalDB.setKeepScreen(mContext, false)
            }
        }

        rlCountDown.setOnClickListener {
            try {
                CommonDialog.setDurationDialog(mContext, CommonString.DL_COUNT_DOWN_TIME, tvCountDown)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        rlRestSet.setOnClickListener {
            try {
                CommonDialog.setDurationDialog(mContext, CommonString.DL_REST_SET, tvRestTime)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        rlRestartProgress.setOnClickListener {
            confirmDialog(mContext)
        }

    }

    /* Todo Confirmation dialog */
    private fun confirmVoiceTest(content: Context, strTitle: String, strMsg: String, strType: String): Boolean {

        val builder = AlertDialog.Builder(content)
        builder.setMessage(strMsg)
        builder.setCancelable(true)

        builder.setPositiveButton("Yes") { dialog, id ->

            try {
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        builder.setNegativeButton("No") { dialog, id ->
            if (CommonString.CONFIRM_VOICE_TEST == strType) {
                testVoiceFailDialog()
            }
            try {
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val alertDialog = builder.create()
        alertDialog.show()

        return false
    }

    private fun testVoiceFailDialog() {

        val dialog = Dialog(mContext)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dl_test_voice_fail)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val btnDownloadTTSEngine = dialog.findViewById(R.id.btnDownloadTTSEngine) as Button
        val btnSelectTTSEngine = dialog.findViewById(R.id.btnSelectTTSEngine) as Button

        btnDownloadTTSEngine.setOnClickListener {
            CommonUtility.downloadTTSEngine(mContext)
            try {
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnSelectTTSEngine.setOnClickListener {
            selectTTSEngine()
            try {
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        dialog.show()
    }

    private fun selectTTSEngine() {
        val dialog = Dialog(mContext)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dl_select_tts_engine)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val rdoGoogleEngine = dialog.findViewById(R.id.rdoGoogleEngine) as RadioButton

        rdoGoogleEngine.setOnCheckedChangeListener { buttonView, isChecked ->
            dialog.cancel()
            if (boolIsSelectEngine) {
                AppControl.speechText(mContext, getString(R.string.did_you_hear_test_voice))

                Thread.sleep(1000)
                confirmVoiceTest(mContext, "", getString(R.string.did_you_hear_test_voice), CommonString.CONFIRM_VOICE_TEST)

                boolIsSelectEngine = false
            }
        }

        dialog.show()
    }

    private fun confirmDialog(content: Context): Boolean {

        val bDialog = AlertDialog.Builder(content)
        bDialog.setTitle(getString(R.string.restart_progress))
        bDialog.setCancelable(true)

        bDialog.setPositiveButton("Yes") { dialog, id ->
            DataHelper(mContext).restartProgress()
            (mContext as AppCompatActivity).finish()
            startActivity(Intent(mContext, MainActivity::class.java))

            dialog.cancel()
            return@setPositiveButton
        }

        bDialog.setNegativeButton("No") { dialog, id ->
            dialog.cancel()
            return@setNegativeButton
        }

        val conDialog = bDialog.create()
        conDialog.show()

        return false
    }

}
