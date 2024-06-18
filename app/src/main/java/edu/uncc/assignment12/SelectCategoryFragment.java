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

import edu.uncc.assignment12.databinding.FragmentSelectCategoryBinding;
import edu.uncc.assignment12.databinding.SelectionRowItemBinding;


public class SelectCategoryFragment extends Fragment {

    String[] mCategories = {"Housing", "Transportation", "Food", "Health", "Other"};

    public SelectCategoryFragment() {
        // Required empty public constructor
    }

    FragmentSelectCategoryBinding binding;
    CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelSelectCategory();
            }
        });
        adapter = new CategoryAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SelectionRowItemBinding itemBinding = SelectionRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new CategoryViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            holder.setupUI(mCategories[position]);
        }

        @Override
        public int getItemCount() {
            return mCategories.length;
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder{
            SelectionRowItemBinding itemBinding;
            String mCategory;
            public CategoryViewHolder(SelectionRowItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void setupUI(String category){
                this.mCategory = category;
                itemBinding.textViewItem.setText(category);
                itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.selectCategory(mCategory);
                    }
                });
            }
        }
    }

    SelectCategoryListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SelectCategoryListener) {
            mListener = (SelectCategoryListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SelectCategoryListener");
        }
    }

    interface SelectCategoryListener {
        void selectCategory(String category);
        void onCancelSelectCategory();
    }

}