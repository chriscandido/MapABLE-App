package up.envisage.mapable.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import up.envisage.mapable.databinding.Listener;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.repository.ReportRepository;

public class ReportViewModel extends AndroidViewModel {

    private ReportRepository reportRepository;
    private LiveData<List<ReportTable>> allReports;
    private LiveData<ReportTable> lastReport;

    public ReportViewModel (@NonNull Application application) {
        super(application);

        reportRepository = new ReportRepository(application);
        allReports = reportRepository.getAllReports();
        lastReport = reportRepository.getLastReport();
    }

    public void insert (ReportTable report) {
        reportRepository.insertReport(report);
    }

    public LiveData<ReportTable> getLastReport() {
        return lastReport;
    }

    public LiveData<List<ReportTable>> getAllReports() {
        return allReports;
    }
}
