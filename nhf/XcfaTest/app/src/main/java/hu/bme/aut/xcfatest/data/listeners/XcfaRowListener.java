package hu.bme.aut.xcfatest.data.listeners;

import android.content.Intent;
import android.view.View;

import hu.bme.aut.xcfatest.RunActivity;
import hu.bme.aut.xcfatest.data.model.XcfaRow;

public class XcfaRowListener implements View.OnClickListener {

    private XcfaRow xcfaRow;

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), RunActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("file", xcfaRow.getName());
        view.getContext().startActivity(intent);
    }

    public void setXcfaRow(XcfaRow xcfaRow) {
        this.xcfaRow = xcfaRow;
    }
}
