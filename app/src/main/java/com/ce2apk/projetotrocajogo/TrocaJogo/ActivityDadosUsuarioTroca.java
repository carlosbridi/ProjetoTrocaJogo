package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;

import com.ce2apk.projetotrocajogo.R;
import com.github.mrengineer13.snackbar.SnackBar;
import com.iangclifton.android.floatlabel.FloatLabel;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by carlosbridi on 18/04/16.
 */
public class ActivityDadosUsuarioTroca extends FragmentActivity {

    private int mIDUsuario;
    private FloatLabel edtNome;
    private FloatLabel edtNomeUsuario;
    private FloatLabel edtTelefone;
    private FloatLabel edtEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitydadosusuario);

        edtNome = (FloatLabel) findViewById(R.id.edtNome);
        edtNomeUsuario = (FloatLabel) findViewById(R.id.edtNomeUsuario);
        edtTelefone = (FloatLabel) findViewById(R.id.edtTelefone);
        edtEmail = (FloatLabel) findViewById(R.id.edtEmail);

        edtNome.getEditText().setEnabled(false);
        edtNomeUsuario.getEditText().setEnabled(false);
        edtTelefone.getEditText().setEnabled(false);
        edtEmail.getEditText().setEnabled(false);


        Bundle extras = getIntent().getExtras();
        if (extras != null){
            mIDUsuario = extras.getInt("id");
            edtNome.setText(extras.getString("nome"));
            edtNomeUsuario.setText(extras.getString("nomeusuario"));
            edtTelefone.setText(extras.getString("telefone"));
            edtEmail.setText(extras.getString("email"));
        }

        Button btnAdicionar = (Button) findViewById(R.id.btnAdicionarContatos);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarContatos(v);
            }
        });
    }


    public void adicionarContatos(View view){

        ContentResolver contentResolver;

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(edtTelefone.getEditText().getText().toString()));
        String name = "?";

        contentResolver = getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

        try{
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                if (!name.isEmpty()) {
                    new SnackBar.Builder(this)
                            .withMessage("Encontramos um contato com esse telefone!")
                            .withStyle(SnackBar.Style.DEFAULT)
                            .withDuration((short)3000)
                            .show();
                    return;
                }
            }
        }
            finally {
                if (contactLookup != null) {
                    contactLookup.close();
                }
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                        edtNome.getEditText().getText().toString()).build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.Email.ADDRESS,
                        edtEmail.getEditText().getText().toString()).build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                        edtTelefone.getEditText().getText().toString())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

        try {
            ContentProviderResult[] contentProviderResult = contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
            if (contentProviderResult[0].uri == null) {
                throw new Exception("Erro ao incluir usuário");
            } else {
                new SnackBar.Builder(this)
                        .withMessage("Usuário incluído aos contatos com sucesso!")
                        .withStyle(SnackBar.Style.DEFAULT)
                        .withDuration((short) 3000)
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void fechar(View view){
        finish();
    }


}
