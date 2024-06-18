package edu.uncc.assignment12;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import edu.uncc.assignment12.databinding.FragmentSelectDiscountBinding;
import edu.uncc.assignment12.databinding.SelectionRowItemBinding;


public class SelectDiscountFragment extends Fragment {
    public SelectDiscountFragment() {
        // Required empty public constructor
    }

    FragmentSelectDiscountBinding binding;
    double[] mDiscounts = {10, 15, 18, -1};
    DiscountAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectDiscountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.seekBar.setMax(50);
        binding.seekBar.setProgress(25);

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.textViewSeekBarProgress.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelSelectDiscount();
            }
        });

        adapter = new DiscountAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>{
        @NonNull
        @Override
        public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SelectionRowItemBinding itemBinding = SelectionRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new DiscountViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
            holder.setupUI(mDiscounts[position]);
        }

        @Override
        public int getItemCount() {
            return mDiscounts.length;
        }

        class DiscountViewHolder extends RecyclerView.ViewHolder{
            SelectionRowItemBinding itemBinding;
            double mDiscount;
            public DiscountViewHolder(SelectionRowItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void setupUI(double discount){
                this.mDiscount = discount;
                if(mDiscount > 0 ){
                    itemBinding.textViewItem.setText(discount + "%");
                } else {
                    itemBinding.textViewItem.setText("Custom");
                }

                itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mDiscount > 0 ){
                            mListener.onDiscountSelected(mDiscount);
                        } else {
                            mListener.onDiscountSelected((double) binding.seekBar.getProgress());
                        }
                    }
                });
            }
        }
    }

    SelectDiscountListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SelectDiscountListener) {
            mListener = (SelectDiscountListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SelectDiscountListener");
        }
    }

    interface SelectDiscountListener {
        void onDiscountSelected(double discount);
        void onCancelSelectDiscount();
    }
}