package com.example.win.easy.view.main;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.win.easy.R;
import com.example.win.easy.enumeration.LoginType;
import com.example.win.easy.web.callback.OnReadyFunc;
import com.example.win.easy.web.service.LoginService;
import com.qmuiteam.qmui.alpha.QMUIAlphaButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.win.easy.enumeration.LoginType.Email;
import static com.example.win.easy.enumeration.LoginType.Phone;

public class LoginFragment extends Fragment {

    @BindView(R.id.loginTopBar) QMUITopBar topBar;
    @BindView(R.id.accountEditText) EditText accountEditText;
    @BindView(R.id.passwordEditText) EditText passwordEditText;
    @BindView(R.id.loginButton) QMUIAlphaButton loginButton;
    @BindView(R.id.switchLoginTypeButton)QMUIAlphaButton switchLoginTypeButton;
    @BindString(R.string.phoneAccountHint) String phoneAccountHint;
    @BindString(R.string.emailAccountHint) String emailAccountHint;
    @BindString(R.string.switchToPhoneLoginHint) String switchToPhoneLoginHint;
    @BindString(R.string.switchToEmailLoginHint) String switchToEmailLoginHint;
    @BindString(R.string.loginingTipWord)String loginingTipWord;
    @BindString(R.string.successfulLoginTipWord) String successfulLoginTipWord;
    @BindString(R.string.unsuccessfulLoginTipWord) String unsuccessfulLoginTipWord;
    @BindString(R.string.loginFragmentTopBarTitle) String topBarTitle;
    @BindInt(R.integer.temporaryTipShowTimeInMillis)int tipShowTimeInMillis;

    private LoginService loginService;
    private MutableLiveData<LoginType> loginTypeLiveData=new MutableLiveData<>();
    private QMUITipDialog loginingDialog;
    private LoginType defaultLoginType;

    public LoginFragment(LoginService loginService,LoginType defaultLoginType){
        this.loginService=loginService;
        this.defaultLoginType=defaultLoginType;
    }

    /**
     *
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View thisView=inflater.inflate(R.layout.fragment_login,container,false);
        ButterKnife.bind(this,thisView);
        setTopBar(topBarTitle);
        setUpLoginButton();
        setUpSwitchLoginTypeButton();
        setUpLoginType();
        return thisView;
    }

    private void setUpLoginType() {
        loginTypeLiveData.setValue(defaultLoginType);
        loginTypeLiveData.observe(this,this::onLoginTypeChanged);
    }

    private void onLoginTypeChanged(LoginType loginType){
        if (loginType==Phone){
            accountEditText.setHint(phoneAccountHint);
            accountEditText.setInputType(InputType.TYPE_CLASS_PHONE);
            switchLoginTypeButton.setText(switchToEmailLoginHint);
        }else {
            accountEditText.setHint(emailAccountHint);
            accountEditText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
            switchLoginTypeButton.setText(switchToPhoneLoginHint);
        }
    }

    private void setTopBar(String title){
        topBar.setTitle(title);
    }

    private void setUpLoginButton(){
        loginButton.setOnClickListener(this::login);
    }

    private void login(View v){
        showLoginingDialog();
        login(getAccount(),getPassword(), loginTypeLiveData.getValue());
    }

    private void showLoginingDialog(){
        loginingDialog=buildDialog(loginingTipWord,QMUITipDialog.Builder.ICON_TYPE_LOADING);
        loginingDialog.show();
    }

    private QMUITipDialog buildDialog(String tipWord,int iconType){
        return new QMUITipDialog.Builder(getContext())
                .setIconType(iconType)
                .setTipWord(tipWord)
                .create();
    }

    private void login(String account,String password,LoginType loginType){
        switch (loginType){
            case Phone: loginService.loginByPhone(account,password, loginFinishedCallback());
                break;
            case Email: loginService.loginByEmail(account,password, loginFinishedCallback());
                break;
            default:
        }
    }

    private OnReadyFunc<Boolean> loginFinishedCallback(){
        return successful->{
            if (successful) onSuccessfulLogin();
            else onUnsuccessfulLogin();
        };
    }

    private String getAccount(){
        return accountEditText.getText().toString();
    }

    private String getPassword(){
        return passwordEditText.getText().toString();
    }

    private void onSuccessfulLogin(){
        dismissLoginingDialog();
        temporarilyShowSuccessfulLoginTip(successfulLoginTipWord,tipShowTimeInMillis);
        navigateToMainFragment();
    }
    private void onUnsuccessfulLogin(){
        dismissLoginingDialog();
        temporarilyShowUnsuccessfulLoginTip(unsuccessfulLoginTipWord,tipShowTimeInMillis);
    }

    private void dismissLoginingDialog(){
        loginingDialog.dismiss();
    }

    private void temporarilyShowSuccessfulLoginTip(String tipWord, int showTimeInMillis){
        temporarilyShowTipDialog(tipWord,QMUITipDialog.Builder.ICON_TYPE_SUCCESS,showTimeInMillis);
    }

    private void temporarilyShowUnsuccessfulLoginTip(String tipWord, int showTimeInMillis){
        temporarilyShowTipDialog(tipWord,QMUITipDialog.Builder.ICON_TYPE_FAIL,showTimeInMillis);
    }

    private void temporarilyShowTipDialog(String tipWord,int iconType,  int showTimeInMillis){
        QMUITipDialog tipDialog=buildDialog(tipWord,iconType);
        tipDialog.show();
        dismissAfter(tipDialog,showTimeInMillis);
    }

    private void dismissAfter(QMUITipDialog dialog, int showTimeInMillis){
        getView().postDelayed(dialog::dismiss,showTimeInMillis);
    }

    private void navigateToMainFragment(){
        Navigation.findNavController(getView()).navigate(LoginFragmentDirections.actionLoginFragmentToMainActivityFragment());
    }

    private void setUpSwitchLoginTypeButton() {
        switchLoginTypeButton.setOnClickListener(v -> {
            LoginType currentLoginType=loginTypeLiveData.getValue();
            if (currentLoginType==Phone)
                loginTypeLiveData.setValue(Email);
            else
                loginTypeLiveData.setValue(Phone);
        });
    }


}
