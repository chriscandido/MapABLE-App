package up.envisage.mapable.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import up.envisage.mapable.db.Database;
import up.envisage.mapable.db.dao.ReportDAO;
import up.envisage.mapable.db.table.ReportTable;

public class ReportRepository {

    private final ReportDAO reportDAO;
    private final LiveData<List<ReportTable>> allReports;
    private final LiveData<ReportTable> lastReport;

    public ReportRepository (Application application) {
        Database db = Database.getInstance(application);
        reportDAO = db.reportDAO();
        allReports = reportDAO.getAllReports();
        lastReport = reportDAO.getLastReport();
    }

    public LiveData<List<ReportTable>> getAllReports() {
        return allReports;
    }

    public LiveData<ReportTable> getLastReport() {
        return lastReport;
    }

    public void insertReport(ReportTable reportTable) {
        new ReportRepository.insertReportAsyncTask(reportDAO).execute(reportTable);
    }

    public void deleteReport(ReportTable reportTable) {
        new ReportRepository.deleteReportAsyncTask(reportDAO).execute(reportTable);
    }

    public static class insertReportAsyncTask extends AsyncTask<ReportTable, Void, Void> {

        private final ReportDAO reportDAO;

        private insertReportAsyncTask(ReportDAO dao) {
            reportDAO = dao;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(ReportTable... reports) {
            reportDAO.insertReport(reports[0]);
            Log.v("[ ReportRepository.java ]", "Data Successfully Inserted");
            return null;
        }
    }

    public static class deleteReportAsyncTask extends AsyncTask<ReportTable, Void, Void> {

        private final ReportDAO reportDAO;

        private deleteReportAsyncTask(ReportDAO dao) {
            reportDAO = dao;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(ReportTable... reports) {
            reportDAO.deleteReport(reports[0]);
            Log.v("[ ReportRepository.java ]", "Data Successfully Inserted");
            return null;
        }
    }

}
