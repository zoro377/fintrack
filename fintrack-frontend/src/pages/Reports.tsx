import { useState } from 'react';
import { reportService } from '../services/reportService';
import './Reports.css';

const Reports = () => {
  const [downloading, setDownloading] = useState<'csv' | 'pdf' | null>(null);

  const handleExport = async (type: 'csv' | 'pdf') => {
    setDownloading(type);
    try {
      if (type === 'csv') {
        await reportService.exportCsv();
      } else {
        await reportService.exportPdf();
      }
    } catch (error) {
      alert(`Failed to export ${type.toUpperCase()}. Please try again.`);
    } finally {
      setDownloading(null);
    }
  };

  return (
    <div className="reports-page">
      <h1>Export Reports</h1>
      <p className="subtitle">Download your expense data in various formats</p>

      <div className="reports-grid">
        <div className="report-card">
          <div className="report-icon">📊</div>
          <h2>CSV Export</h2>
          <p>Download your expenses as a CSV file. Perfect for Excel, Google Sheets, or data analysis.</p>
          <button
            onClick={() => handleExport('csv')}
            disabled={downloading !== null}
            className="btn-primary"
          >
            {downloading === 'csv' ? 'Downloading...' : 'Download CSV'}
          </button>
        </div>

        <div className="report-card">
          <div className="report-icon">📄</div>
          <h2>PDF Export</h2>
          <p>Generate a formatted PDF report of all your expenses. Great for printing or sharing.</p>
          <button
            onClick={() => handleExport('pdf')}
            disabled={downloading !== null}
            className="btn-primary"
          >
            {downloading === 'pdf' ? 'Generating...' : 'Download PDF'}
          </button>
        </div>
      </div>

      <div className="info-box">
        <h3>What's included in the reports?</h3>
        <ul>
          <li>All expense records with dates, amounts, and categories</li>
          <li>Payment mode information</li>
          <li>Expense descriptions</li>
          <li>Formatted for easy reading and analysis</li>
        </ul>
      </div>
    </div>
  );
};

export default Reports;



