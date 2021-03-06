package com.example.rajesh.expensetracker.dashboard;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rajesh.expensetracker.R;
import com.example.rajesh.expensetracker.account.edit.AccountEditActivity;
import com.example.rajesh.expensetracker.base.frament.BaseFragment;
import com.example.rajesh.expensetracker.category.CategoryEditActivity;
import com.example.rajesh.expensetracker.category.ExpenseCategory;
import com.example.rajesh.expensetracker.expense.ExpenseEditActivity;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;


public class DashBoardFragment extends BaseFragment implements ExpenseView.Display {
    ExpenseAdapter expenseAdapter;

    @Bind(R.id.rv_dashboard)
    RecyclerView rvDashBoard;

    @Bind(R.id.tv_total_amount)
    TextView tvTotalAmount;

    @Bind(R.id.tv_total_expense)
    TextView tvTotalExpense;

    @Bind(R.id.tv_remaining_amount)
    TextView tvRemainingAmount;

    @Bind(R.id.progress_amount)
    ProgressBar progressAmount;

    @Bind(R.id.progress_expense)
    ProgressBar progressExpense;

    @Bind(R.id.fab_menu)
    FloatingActionMenu fabMenu;

    DashboardPresenter dashboardPresenter;
    long mExpenses = 0;
    long mTotalAmount = 0;
    long remainingAmount = 0;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabMenu.setClosedOnTouchOutside(true);

        setRecyclerViewAdapter();

        dashboardPresenter = new DashboardPresenter(this);
        dashboardPresenter.getData(null);
        dashboardPresenter.getTotalAmount();

        tvRemainingAmount.setText("" + getRemainingAmount());

        progressAmount.setMax((int) mTotalAmount);
        progressAmount.setProgress((int) remainingAmount);

        progressExpense.setMax((int) mTotalAmount);
        progressExpense.setProgress((int) mExpenses);


    }

    private long getRemainingAmount() {
        return remainingAmount = mTotalAmount - mExpenses;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_dash_board;
    }

    private void setRecyclerViewAdapter() {
        ArrayList<Expense> expenses = new ArrayList<>();
        ArrayList<ExpenseCategory> expenseCategories = new ArrayList<>();
        rvDashBoard.setLayoutManager(new LinearLayoutManager(getActivity()));
        expenseAdapter = new ExpenseAdapter(getActivity(), expenses, expenseCategories);
        rvDashBoard.setAdapter(expenseAdapter);

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                expenseAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvDashBoard);
    }

    @OnClick({R.id.fab_add_expense, R.id.fab_add_account, R.id.fab_add_category})
    public void onClick(View view) {
        Timber.d("clicked");
        switch (view.getId()) {
            case R.id.fab_add_account:
                Timber.d("add account button clicked");
                getActivity().startActivity(AccountEditActivity.getLaunchIntent(getActivity(), null));
                break;
            case R.id.fab_add_expense:
                getActivity().startActivity(ExpenseEditActivity.getLaunchIntent(getActivity(), null, null));
                break;
            case R.id.fab_add_category:
                getActivity().startActivity(CategoryEditActivity.getLaunchIntent(getActivity(), null));
                break;
        }
        fabMenu.close(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void showExpenses(ArrayList<Expense> expenses, ArrayList<ExpenseCategory> categories) {
        expenseAdapter.addExpenses(expenses, categories);
        calculateTotalExpense(expenses);
    }

    private void calculateTotalExpense(ArrayList<Expense> expenses) {
        for (Expense expense : expenses) {
            mExpenses = mExpenses + expense.expenseAmount;
        }
        tvTotalExpense.setText("" + mExpenses);
    }

    @Override
    public void showNoExpensesView() {

    }

    @Override
    public void provideTotalAmount(long totalAmount) {
        mTotalAmount = totalAmount;
        tvTotalAmount.setText("" + totalAmount);
    }
}
