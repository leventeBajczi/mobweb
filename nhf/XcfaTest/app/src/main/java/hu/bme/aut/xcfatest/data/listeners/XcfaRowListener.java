package hu.bme.aut.xcfatest.data.listeners;

import android.view.View;

import hu.bme.aut.xcfatest.data.model.XcfaRow;

public class XcfaRowListener implements View.OnClickListener {

    private XcfaRow xcfaRow;

    public XcfaRowListener() {
    }

    @Override
    public void onClick(View view) {

    }

    public void setXcfaRow(XcfaRow xcfaRow) {
        this.xcfaRow = xcfaRow;
    }
}
