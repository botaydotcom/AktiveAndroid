package im.aktive.aktive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

import im.aktive.aktive.api_requester.ATAPICallWrapper;
import im.aktive.aktive.manager.ATTagManager;
import im.aktive.aktive.manager.ATUserManager;
import im.aktive.aktive.model.ATWrappedModelRequestCallback;


public class ATStartupActivity extends ActionBarActivity {
    private ATAPICallWrapper mCallWrapper = null;
    private String mFbAccessToken = null;
    private RadioGroup mIndicatorGroup = null;

    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallWrapper = new ATAPICallWrapper();
        tryRetrieveFacebookSession(savedInstanceState);
        setContentView(R.layout.activity_startup);
        Button loginEmailButton = (Button)findViewById(R.id.button_loggin_email);
        loginEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSigninWithEmailClicked();
            }
        });
        Button loginFbButton = (Button)findViewById(R.id.button_loggin_fb);
        loginFbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSigninWithFacebookClicked();
            }
        });
    }

    public void onSigninWithEmailClicked()
    {
        signInWithEmailPassword("test@aktive.im", "testtest");
    }

    public void onSigninWithFacebookClicked()
    {
        signInWithFacebook();
    }
    
    public void signInWithEmailPassword(String email, String password) {
        //Activity self = this;
        final ProgressDialog dialog = ProgressDialog.show(this, "Logging in", "Logging in, please wait!");

        ATUserManager.getInstance().login(email, password, new ATWrappedModelRequestCallback(mCallWrapper) {
            @Override
            public void onSuccess(Object object) {
                dialog.dismiss();
                ATStartupActivity.this.transitionToHomeActivity();
            }

            @Override
            public void onFailed(String message) {
                dialog.dismiss();
                Toast.makeText(ATStartupActivity.this, "Cannot sign in because: " + message.trim(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signInWithFacebook() {
        Session session = Session.getActiveSession();
        if (session.isOpened() && mFbAccessToken != null)
        {
            processAccessTokenReceived();
        } else if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }
    }

    private void processAccessTokenReceived() {
        performSigninWithExternalId("facebook", mFbAccessToken);
    }

    private void performSigninWithExternalId(String externalId, String accessToken)
    {
        final ProgressDialog dialog = ProgressDialog.show(this, "Logging in", "Logging in, please wait!");
        ATUserManager.getInstance().loginExternal(externalId, accessToken,
                new ATWrappedModelRequestCallback(mCallWrapper) {
                    @Override
                    public void onSuccess(Object object) {
                        dialog.dismiss();
                        ATStartupActivity.this.transitionToHomeActivity();
                    }

                    @Override
                    public void onFailed(String msg) {
                        dialog.dismiss();
                        Toast.makeText(ATStartupActivity.this,
                                "Cannot sign in because: " + msg.trim(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void transitionToHomeActivity() {
        boolean completedOnboarding = ATUserManager.getInstance().getCurrentUser().isCompletedOnboarding();
        Toast.makeText(this, "Login successfully... " +
                ATUserManager.getInstance().getCurrentUser().getFullName(), Toast.LENGTH_LONG).show();
        if (!completedOnboarding)
        {
            ATTagManager.getInstance().fetchTagsForFirstTime(new ATWrappedModelRequestCallback(mCallWrapper) {
                @Override
                public void onSuccess(Object object) {
                    Intent i = new Intent(ATStartupActivity.this, ATOnboardingActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onFailed(String msg) {
                    Toast.makeText(ATStartupActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Intent i = new Intent(this, ATHomeActivity.class);
            this.startActivity(i);
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.startup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /***
     * FACEBOOK
     */

    public void tryRetrieveFacebookSession(Bundle savedInstanceState)
    {
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        mCallWrapper.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if (session.isOpened()) {
                mFbAccessToken = session.getAccessToken();
                processAccessTokenReceived();
            }
        }
    }
}
