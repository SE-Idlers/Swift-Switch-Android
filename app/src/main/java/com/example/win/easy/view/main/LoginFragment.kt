package com.example.win.easy.view.main

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import com.example.win.easy.R
import com.example.win.easy.enumeration.LoginType
import com.example.win.easy.network.LoginService
import com.google.android.material.snackbar.Snackbar
import com.qmuiteam.qmui.alpha.QMUIAlphaButton
import com.qmuiteam.qmui.widget.QMUITopBar
import kotlinx.coroutines.*

class LoginFragment(private val loginService: LoginService, private val defaultLoginType: LoginType)
    : Fragment(),
        CoroutineScope by CoroutineScope(Dispatchers.Main){
    @BindView(R.id.login_fragment_root_layout) lateinit var rootLayout: ConstraintLayout
    @BindView(R.id.loginTopBar) lateinit var topBar: QMUITopBar
    @BindView(R.id.accountEditText) lateinit var accountEditText: EditText
    @BindView(R.id.passwordEditText) lateinit var passwordEditText: EditText
    @BindView(R.id.loginProgressBar) lateinit var spinner: ProgressBar
    @BindView(R.id.loginButton) lateinit var loginButton: QMUIAlphaButton
    @BindView(R.id.switchLoginTypeButton) lateinit var switchLoginTypeButton: QMUIAlphaButton
    @BindString(R.string.phoneAccountHint) lateinit var phoneAccountHint: String
    @BindString(R.string.emailAccountHint) lateinit var emailAccountHint: String
    @BindString(R.string.switchToPhoneLoginHint) lateinit var switchToPhoneLoginHint: String
    @BindString(R.string.switchToEmailLoginHint) lateinit var switchToEmailLoginHint: String
    @BindString(R.string.loginingTipWord) lateinit var loginingTipWord: String
    @BindString(R.string.successfulLoginTipWord) lateinit var successfulLoginTipWord: String
    @BindString(R.string.unsuccessfulLoginTipWord) lateinit var unsuccessfulLoginTipWord: String
    @BindString(R.string.loginFragmentTopBarTitle) lateinit var topBarTitle: String
    private val loginTypeLiveData = MutableLiveData<LoginType>()
    private val account: String
        get() = accountEditText.text.toString()

    private val password: String
        get() = passwordEditText.text.toString()

    /**
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_login, container, false).apply {
                ButterKnife.bind(this@LoginFragment, this)
                topBar.setTitle(topBarTitle)
                loginButton.setOnClickListener { login(account, password, loginTypeLiveData.value!!) }
                switchLoginTypeButton.setOnClickListener {
                    loginTypeLiveData.value = if (loginTypeLiveData.value == LoginType.Email) LoginType.Phone else LoginType.Email
                }
                loginTypeLiveData.run {
                    value=defaultLoginType
                    observe(this@LoginFragment, Observer { onLoginTypeChanged(it) })
                }
            }

    private fun onLoginTypeChanged(loginType: LoginType): Unit =
        if (loginType == LoginType.Phone) {
            accountEditText.hint = phoneAccountHint
            accountEditText.inputType = InputType.TYPE_CLASS_PHONE
            switchLoginTypeButton.text = switchToEmailLoginHint
        } else {
            accountEditText.hint = emailAccountHint
            accountEditText.inputType = InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
            switchLoginTypeButton.text = switchToPhoneLoginHint
        }

    private fun login(account: String, password: String, loginType: LoginType) {
        launch {
            try {
                spinner.visibility = ProgressBar.VISIBLE
                withTimeout(5000){
                    loginService.login(account, password,loginType)
                }
                Snackbar.make(rootLayout, successfulLoginTipWord, Snackbar.LENGTH_SHORT).show()
                Navigation.findNavController(view!!).navigateUp()
            } catch (t: Throwable) {
                Snackbar.make(rootLayout, "登录失败：" + t.message, Snackbar.LENGTH_SHORT).show()
            } finally {
                spinner.visibility = ProgressBar.GONE
            }
        }
    }
}