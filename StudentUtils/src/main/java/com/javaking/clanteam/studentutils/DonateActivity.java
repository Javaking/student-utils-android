package com.javaking.clanteam.studentutils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class DonateActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start the paypal payment servic
        Intent intent = new Intent(this, PayPalService.class);

        // live: don't put any environment extra
        // sandbox: use PaymentActivity.ENVIRONMENT_SANDBOX
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);

        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "ATW8WBDeAoY2P229kAui_Ve_aFXFDSVuU_0C5XgWcT4LGRAca");

        startService(intent);

        setContentView(R.layout.activity_donate);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("donate", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("THANK YOU!!");
                    builder.setMessage("Thank you so much for making a donation!");
                    builder.setNeutralButton("You're Welcome",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });

                    builder.show();

                } catch (JSONException e) {
                    Log.e("donate", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("donate", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
            Log.i("donate", "An invalid payment was submitted. Please see the docs.");
        }
    }

    public void onDonatePressed(View pressed) {
        EditText amountEdit = (EditText)findViewById(R.id.amount);
        double amount = 0;
        if (amountEdit.getText() != null) {
            amount = Double.parseDouble(amountEdit.getText().toString());
        }
        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "USD", "Generous Donation");

        Intent intent = new Intent(this, PaymentActivity.class);

        // comment this line out for live or set to PaymentActivity.ENVIRONMENT_SANDBOX for sandbox
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_LIVE);

        // it's important to repeat the clientId here so that the SDK has it if Android restarts your
        // app midway through the payment UI flow.
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "ATW8WBDeAoY2P229kAui_Ve_aFXFDSVuU_0C5XgWcT4LGRAca");

        // Provide a payerId that uniquely identifies a user within the scope of your system,
        // such as an email address or user ID.
        intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, String.valueOf(System.identityHashCode(this)));

        intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, "<javaking.clanteam@gmail.com>");
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }


    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}