package com.example.rajesh.expensetracker.dashboard;

import com.example.rajesh.expensetracker.category.ExpenseCategory;
import com.example.rajesh.expensetracker.report.ReportFragment;

import java.util.ArrayList;


public class DashboardPresenter implements DashboardPresenterContract, OnExpenseResultListener, OnAccountResultListener {

    private static final String TAG = DashboardPresenter.class.getSimpleName();
    ExpenseView.Display expenseView;
    ExpenseModel expenseModel;

    public DashboardPresenter(ExpenseView.Display expenseView) {
        this.expenseView = expenseView;
        this.expenseModel = new ExpenseModel();
    }

    @Override
    public void getData(String expenseType) {
        expenseModel.getExpense(this, expenseType);
    }

    @Override
    public void getTotalAmount() {
        expenseModel.getAccountsByMonth(this, ReportFragment.ReportType.REPORT_BY_MONTH);
    }

    @Override
    public void getDistinctRecurringExpense(String expenseType) {
        expenseModel.getDistinctRecurringExpense(this);
    }


    @Override
    public void onExpenseSuccess(ArrayList<Expense> expenses, ArrayList<ExpenseCategory> expenseCategories) {
        expenseView.showExpenses(expenses, expenseCategories);
    }

    @Override
    public void onExpenseFailure() {

    }

    @Override
    public void onAccountByTimeStampListSuccess(long totalAmount) {
        expenseView.provideTotalAmount(totalAmount);
    }

    @Override
    public void onAccountByTimeStampListFailure(String message) {

    }
}
