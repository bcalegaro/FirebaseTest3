package com.bcalegaro.firebasetest3.ui.ration;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcalegaro.firebasetest3.R;
import com.bcalegaro.firebasetest3.data.Ration;
import com.bcalegaro.firebasetest3.data.RationAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RationMainFragment extends Fragment {
    private RationAdapter rationAdapter;
    private ArrayList<Ration> listRation;
    private String userId;

    private DatabaseReference mDatabase;

    private RationMainViewModel mViewModel;

    public static RationMainFragment newInstance() {
        return new RationMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_ration, container, false);

        FloatingActionButton addFab = view.findViewById(R.id.addFloatActionButton);
        addFab.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate(R.id.action_rationMainFragment_to_addRationFragment);
        });

        RecyclerView recyclerView = view.findViewById(R.id.rationRecyclerView);
        // Configura a recycler view para exibir os itens na vertical
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        // anexa um ItemDecorator personalizado para desenhar divisórias entre os itens da lista
        recyclerView.addItemDecoration(new ItemDivider(getContext()));
        // configura o adaptador da lista
        rationAdapter = new RationAdapter();
        recyclerView.setAdapter(rationAdapter);

        // Initialize firebase auth to acess firebase user
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        //assign userId to current firebase user logged in
        this.userId = firebaseUser.getUid();
        //simples firebase acess reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //initialize a blank list of itens
        listRation = new ArrayList<Ration>();
        //configure read events on firebase data
        configureFirebaseListener();

        return view;
    }

    public void configureFirebaseListener() {
        //acessa a posição do firebase específica do usuário
        DatabaseReference myRations = mDatabase.child("rations").child(userId);
        //configura a leitura dos dados
        myRations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //apaga a lista de valores
                listRation.clear();
                //le cada valor existente e adiciona na lista
                for (DataSnapshot rationSnapshot: snapshot.getChildren()){
                    Ration newRation = rationSnapshot.getValue(Ration.class);
                    Log.d("FirebaseTestTAG", "Child Value listener Ration load " + rationSnapshot.getValue());
                    listRation.add(newRation);
                }
                //atualiza o adaptador para mostrar os itens a partir de lista lida
                rationAdapter.setmRations(listRation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RationMainViewModel.class);
        // TODO: Use the ViewModel
    }
}