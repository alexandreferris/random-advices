package br.com.alexandreferris.randomadvice.ui

import android.arch.lifecycle.Observer
import android.content.ClipData
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import br.com.alexandreferris.randomadvice.App
import br.com.alexandreferris.randomadvice.R
import br.com.alexandreferris.randomadvice.viewmodel.MainVM
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import br.com.alexandreferris.randomadvice.utils.loadAdBanner

class MainActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var mainVM: MainVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadAdBanner(adView)

        (application as App).appComponent.inject(this)

        buttonCopyText.setOnClickListener(this)
        buttonShareText.setOnClickListener(this)
        fabRefreshAdvice.setOnClickListener(this)

        observableViewModel()
    }

    private fun observableViewModel() {
        mainVM.getLoading().observe(this,  Observer {
            loadingBar.visibility = if (it!!) View.VISIBLE else View.GONE
        })

        mainVM.getError().observe(this, Observer {
            if (it!!) {
                textViewAdvice.text = StringUtils.EMPTY
                linearLayoutButtons.visibility = View.GONE
                Snackbar.make(
                    constraintLayoutMain,
                    "Sorry!\nWe couldn't load the advice. Please Try Again.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })

        mainVM.getAdvice().observe(this,  Observer {
            linearLayoutButtons.visibility = View.VISIBLE
            textViewAdvice.text = it
        })
    }

    private fun copyAdviceToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("advice_text", textViewAdvice.text)
        clipboard.primaryClip = clip
        Snackbar.make(
            constraintLayoutMain,
            "Advice copied!",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun shareAdvice() {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Advice")
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Advice: " + textViewAdvice.text + "\n\nWant more advices? Check it out: http://play.google.com/store/apps/details?id=" + applicationContext.packageName)
        startActivity(Intent.createChooser(sharingIntent, "Share the Advice"))
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fabRefreshAdvice -> mainVM.fetchRandomAdvice()
            R.id.buttonCopyText -> copyAdviceToClipboard()
            R.id.buttonShareText -> shareAdvice()
        }
    }
}
