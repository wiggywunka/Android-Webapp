/*
    Quick Note:
    This won't run on it's own. It's meant to serve as a learning tool. 
    If you try to compile this it won't run as it's missing several items 
    necessary to run such as various XMLs and other files as such.
*/

/*
    Copyright 2014-2015 Fabrizio Martinez (KNPhoenix)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/
package com.knphoenix.webapp;


// Alert Dialog and EditText used for the search function.
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

// Intent for setting the share action.
import android.content.Intent;

// 
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

// This is the main menu component.
import android.view.Menu;
import android.view.MenuItem;

// TextUtils to take the user generated string and convert it to HTML.
import android.text.TextUtils;

// KeyEvent for the back button integration.
import android.view.KeyEvent;

// CookieManager and Sync Manager for obvious reasons.
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

// Websettings to adjust settings such as Zoom controls and Javascript.
import android.webkit.WebSettings;

// The webview parts are core in the sense that they are
// what displays the main content.
import android.webkit.WebView;
import android.webkit.WebViewClient;

// Toast notifications for letting the user know when an action is happening.
import android.widget.Toast;




// BEGIN ACTUAL CODE


// Start of the Main class.
public class Main extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // The Webview gets initialized as myWebView.
        WebView myWebView = (WebView) findViewById(R.id.webView);
        // The Webviewclient is initialized by the new webview.
        myWebView.setWebViewClient(new WebViewClient());
        // Settings are initialized webSettings.
        WebSettings webSettings = myWebView.getSettings();
        // Here we use myWebView.getsettings() because I'm an idiot and it worked apparently.
        // Here we initialize whether Built In Zoom Controls is allowed. Hint: it is.
        myWebView.getSettings().setBuiltInZoomControls(true);
        // Here we set whether to keep form data or discard it.
        myWebView.getSettings().setSaveFormData(true);
        /*Safety concern! setJavaScriptEnabled is sometimes sketchy and shouldn't be used
         but since the website we're using is trusted and doesn't contain any sensitive
         information, we can use it. */
        webSettings.setJavaScriptEnabled(true);


        // Here we define the URL which we're going to load / set the webView for.
        myWebView.loadUrl("http://undergroundbiology.com");

        // Telling the cookie manager to start up and do its thing.
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance();

    }



    // Here we override the setting for KeyDown so that if we CAN go back in the app,
    // we go back but if we CAN'T...well...we exit the app :(
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.setWebViewClient(new WebViewClient());


        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    // Here we set the main menu. derp.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);

    }



    // Here we get to the good stuff.  This is where we set the menu items.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Here we set to get the menu item by the ID, which we define in the case variables.
        switch (item.getItemId()) {

            // Here we set case as refresh_settings. This is referencing the variable
            // refresh_settings in the menu.xml (that you would need to run this).
            case R.id.refresh_settings:
                // Basically what this does is re-initialized webview and reloads it.
                WebView myWebView = (WebView) findViewById(R.id.webView);
                myWebView.setWebViewClient(new WebViewClient());
                myWebView.reload();
                // Here we show a toast notification to the user to let them know
                // that we are refreshing the page for them.
                Toast.makeText(this, R.string.status_refreshing, Toast.LENGTH_SHORT).show();
                return true;

            // Here we set case as menu_share. Same as before this is from the menu.xml file.
            case R.id.menu_share:
                
                // Here we define a new intent as shareIntent and set it to the ACTION_SEND which
                // is fancy talk for share button.
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                // Here we set the type of intent. In this instance we set it to plaintext.
                shareIntent.setType("text/plain");
                // Here we grab the URL of what we're looking at in the webview.
                WebView shareURL = (WebView) findViewById(R.id.webView);
                String shareBody = shareURL.getUrl();
                // Here we set the subject for share actions that need / want one.
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject of Sharing");
                // Here we define the text as the variable shareBody which was
                // defined earlier as the URL of the page we're looking at.
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                // Here we initialize the actual chooser for the user to choose
                // an app to share with. ex. Facebook, SMS, e-mail, etc.
                startActivity(Intent.createChooser(shareIntent, "Share Via"));
                return true;

            // Quick note about the search setting. It is a "hack" in the sense
            // of hack-n-slash where I threw it together in a weird way.
            // It's based off of the Wordpress search funtion. Can't say if it'll
            // work on other sites. YMMV.
            case R.id.action_search:
                
                // Here we build the main Alert dialog for the user to input the search.
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                // Defining the dialog dislay text.
                alert.setTitle("Search");
                // Giving the user a place to input their text.
                final EditText search_input = new EditText(this);
                alert.setView(search_input);

                // Here we take their search and implement the actual searching.
                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // We define a new string called search_input_2 as the String of the original search_input
                        String search_input_2 = search_input.getText().toString();
                        // Here we convert search_input_2 to HTML ready code.
                        String searchConvert = TextUtils.htmlEncode(search_input_2);
                        // Now we add it to the end of the default search URL and load the page.
                        WebView searchWebView = (WebView) findViewById(R.id.webView);
                        showToastSearching();
                        searchWebView.loadUrl("http://undergroundbiology.com/?s=" + searchConvert);

                    }
                });

                // Here we just cancel the dialog.
                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel the Search Dialog.
                    }
                });

                alert.show();
                return true;

            default:
            return super.onOptionsItemSelected(item);
        }
    }


    // ShowToast for Searching
    public void showToastSearching() {
        Toast.makeText(this,R.string.status_searching, Toast.LENGTH_SHORT).show();
    }


}
