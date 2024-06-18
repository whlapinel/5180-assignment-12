package edu.uncc.assignment12;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.uncc.assignment12.databinding.BillRowItemBinding;
import edu.uncc.assignment12.databinding.FragmentBillsBinding;

public class BillsFragment extends Fragment {
    private final String TAG = "BillsFragment";
    
    public BillsFragment() {
        // Required empty public constructor
    }

    FragmentBillsBinding binding;

    private ArrayList<Bill> mBills = new ArrayList<>();
    BillsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        binding = FragmentBillsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        mBills.clear();
        mBills.addAll(mListener.getAllBills());
        adapter = new BillsAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(layoutManager);


        binding.buttonClear.setOnClickListener(v -> {
            mListener.clearAllBills();
            mBills.clear();
            mBills.addAll(mListener.getAllBills());
            adapter.notifyDataSetChanged();
        });

        binding.buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoCreateBill();
            }
        });
    }

    class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.BillsViewHolder> {


        @NonNull
        @Override
        public BillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            BillRowItemBinding itemBinding = BillRowItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new BillsViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull BillsViewHolder holder, int position) {
            holder.setupUI(mBills.get(position));
        }

        @Override
        public int getItemCount() {
            return mBills.size();
        }

        class BillsViewHolder extends RecyclerView.ViewHolder {
            BillRowItemBinding itemBinding;
            Bill mBill;
            public BillsViewHolder(BillRowItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void setupUI(Bill bill){
                mBill = bill;
                itemBinding.textViewBillName.setText(bill.getName());
                itemBinding.textViewBillAmount.setText("Bill Amount: " + String.format("$%.2f", bill.getAmount()));
                double discountAmount = bill.getAmount() * bill.getDiscount() / 100;
                itemBinding.textViewBillDiscount.setText("Bill Discount" + String.format("%.2f", bill.getDiscount()) + " (" + String.format("$%.2f", discountAmount) + ")");
                itemBinding.textViewTotalBill.setText("Total Bill: " + String.format("$%.2f", bill.getAmount() - discountAmount));
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                itemBinding.textViewBillDate.setText(sdf.format(bill.billDate));
                itemBinding.textViewBillCategory.setText(bill.getCategory());

                itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.goToBillSummary(mBill);
                    }
                });

                itemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.deleteBillFromBills(mBill);
                        mBills.clear();
                        mBills.addAll(mListener.getAllBills());
                        notifyDataSetChanged();
                    }
                });

                itemBinding.imageViewEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.goToEditBill(mBill);
                    }
                });
            }
        }
    }

    BillsListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BillsListener) {
            mListener = (BillsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BillsListener");
        }
    }

    interface BillsListener {
        void goToBillSummary(Bill bill);
        void goToEditBill(Bill bill);
        List<Bill> getAllBills();
        void gotoCreateBill();
        void clearAllBills();
        void deleteBillFromBills(Bill bill);
    }
}