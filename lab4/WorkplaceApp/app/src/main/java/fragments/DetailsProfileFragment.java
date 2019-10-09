package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import data.DataManager;
import data.Person;
import hu.bme.aut.workplaceapp.R;

public class DetailsProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_detail, container, false);

        TextView tvId = rootView.findViewById(R.id.tvId);
        TextView tvSSN = rootView.findViewById(R.id.tvSSN);
        TextView tvTaxId = rootView.findViewById(R.id.tvTaxId);
        TextView tvRegistrationId = rootView.findViewById(R.id.tvRegistrationId);

        Person person = DataManager.getInstance().getPerson();

        tvId.setText(person.getId());
        tvSSN.setText(person.getSocialSecurityNumber());
        tvTaxId.setText(person.getTaxId());
        tvRegistrationId.setText(person.getRegistrationId());

        return rootView;
    }

}
