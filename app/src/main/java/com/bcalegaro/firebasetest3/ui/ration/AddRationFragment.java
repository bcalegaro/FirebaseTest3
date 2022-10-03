package com.bcalegaro.firebasetest3.ui.ration;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bcalegaro.firebasetest3.R;
import com.bcalegaro.firebasetest3.data.Ration;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddRationFragment extends Fragment {
    private TextInputLayout nameTextInputLayout;
    private TextInputLayout quantityTextInputLayout;

    private AddRationViewModel mViewModel;

    private DatabaseReference mDatabase;
    private String userId;


    public static AddRationFragment newInstance() {
        return new AddRationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_ration, container, false);

        Button btn = view.findViewById(R.id.addRationButton);
        btn.setOnClickListener(v -> addRation() );

        nameTextInputLayout = view.findViewById(R.id.nameTextInputLayout);
        quantityTextInputLayout = view.findViewById(R.id.quantityTextInputLayout);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Initialize firebase auth to acess firebase user
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        //assign userId to current firebase user logged in
        this.userId = firebaseUser.getUid();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddRationViewModel.class);
        // TODO: Use the ViewModel
    }

    private void addRation(){
        //read input
        String name = nameTextInputLayout.getEditText().getText().toString();
        double quantity = Double.parseDouble(quantityTextInputLayout.getEditText().getText().toString());
        //get current time
        SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
        String timestamp = ISO_8601_FORMAT.format(new Date());
        //create new object to store in firebase
        Ration r = new Ration(name,quantity,timestamp);
        Log.d("FirebaseTestTAG","Criando objeto: ," + name + "," + quantity + "," + timestamp);
        //salva objeto na seguinte referência - rations - userid - push code - objeto
        mDatabase.child("rations").child(userId).push().setValue(r);
        // solicita o retorno a tela anterior automaticamente
        Navigation.findNavController(getView()).popBackStack();
    }
}