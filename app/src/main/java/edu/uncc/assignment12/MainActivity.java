package edu.uncc.assignment12;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;
import java.util.List;

import androidx.room.Room;

public class MainActivity extends AppCompatActivity implements
        BillsFragment.BillsListener,
        BillSummaryFragment.BillSummaryListener,
        CreateBillFragment.CreateBillListener,
        SelectCategoryFragment.SelectCategoryListener,
        SelectDiscountFragment.SelectDiscountListener,
        SelectBillDateFragment.SelectDateBillListener,
        EditBillFragment.EditBillListener {
    private final String TAG = "MainActivity";
    BillsDao billsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BillsDatabase db = Room.databaseBuilder(this, BillsDatabase.class, "bills-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        billsDao = db.billsDao();
        billsDao.deleteAll();
        billsDao.insertAll(
                new Bill("test1", "groceries", new Date(), 15.0, 27),
                new Bill("test2", "groceries", new Date(), 13.0, 200),
                new Bill("test3", "groceries", new Date(), 14.0, 150),
                new Bill("test4", "groceries", new Date(), 16.0, 139),
                new Bill("test5", "groceries", new Date(), 17.0, 59)
        );
        Log.d(TAG, "onCreate: " + billsDao.getAllBills());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new BillsFragment(), "bills-fragment")
                .commit();
    }


    @Override
    public void goToBillSummary(Bill bill) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, BillSummaryFragment.newInstance(bill))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToEditBill(Bill bill) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, EditBillFragment.newInstance(bill), "edit-bill-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public List<Bill> getAllBills() {
        //get the bills from the Rooms DB.
        return billsDao.getAllBills();
    }

    @Override
    public void gotoCreateBill() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new CreateBillFragment(), "create-bill-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void clearAllBills() {
        //delete all the bills from the Rooms DB.
        billsDao.deleteAll();
    }

    @Override
    public void deleteBillFromBills(Bill bill) {
        //delete the bill from the Rooms DB.
        billsDao.deleteBill(bill);
    }

    @Override
    public void deleteBill(Bill bill) {
        //delete the bill from the Rooms DB.
        billsDao.deleteBill(bill);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void closeBillSummary() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void createBillSuccessful(Bill bill) {
        //store the bill in the Rooms DB.
        billsDao.insertAll(bill);
        getSupportFragmentManager().findFragmentByTag("bills-fragment");
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void createBillCancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void editBillSuccessful(Bill bill) {
        //update the bill in the Rooms DB.
        billsDao.updateBill(bill);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void editBillCancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoSelectCategory() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SelectCategoryFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoSelectDate() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SelectBillDateFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoSelectDiscount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SelectDiscountFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void selectCategory(String category) {
        CreateBillFragment createBillFragment = (CreateBillFragment) getSupportFragmentManager().findFragmentByTag("create-bill-fragment");
        if (createBillFragment != null) {
            createBillFragment.setSelectedCategory(category);
        }

        EditBillFragment editBillFragment = (EditBillFragment) getSupportFragmentManager().findFragmentByTag("edit-bill-fragment");
        if (editBillFragment != null) {
            editBillFragment.setSelectedCategory(category);
        }

        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCancelSelectCategory() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onDiscountSelected(double discount) {
        CreateBillFragment createBillFragment = (CreateBillFragment) getSupportFragmentManager().findFragmentByTag("create-bill-fragment");
        if (createBillFragment != null) {
            createBillFragment.setSelectedDiscount(discount);
        }

        EditBillFragment editBillFragment = (EditBillFragment) getSupportFragmentManager().findFragmentByTag("edit-bill-fragment");
        if (editBillFragment != null) {
            editBillFragment.setSelectedDiscount(discount);
        }

        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCancelSelectDiscount() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBillDateSelected(Date date) {
        CreateBillFragment createBillFragment = (CreateBillFragment) getSupportFragmentManager().findFragmentByTag("create-bill-fragment");
        if (createBillFragment != null) {
            createBillFragment.setSelectedBillDate(date);
        }

        EditBillFragment editBillFragment = (EditBillFragment) getSupportFragmentManager().findFragmentByTag("edit-bill-fragment");
        if (editBillFragment != null) {
            editBillFragment.setSelectedBillDate(date);
        }

        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCancelSelectBillDate() {
        getSupportFragmentManager().popBackStack();
    }
}