import { useEffect, useState } from 'react';
import { analyticsService, MonthlySummary, CategorySummary, TrendPoint, PredictedExpense } from '../services/analyticsService';
import './Analytics.css';

const Analytics = () => {
  const [monthly, setMonthly] = useState<MonthlySummary[]>([]);
  const [yearly, setYearly] = useState<any[]>([]);
  const [byCategory, setByCategory] = useState<CategorySummary[]>([]);
  const [trends, setTrends] = useState<TrendPoint[]>([]);
  const [predicted, setPredicted] = useState<PredictedExpense | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadAnalytics();
  }, []);

  const loadAnalytics = async () => {
    try {
      const [monthlyData, yearlyData, categoryData, trendsData, predictedData] = await Promise.all([
        analyticsService.getMonthlySummary(),
        analyticsService.getYearlySummary(),
        analyticsService.getCategorySummary(),
        analyticsService.getTrends(),
        analyticsService.getPredictedExpense()
      ]);
      setMonthly(monthlyData);
      setYearly(yearlyData);
      setByCategory(categoryData);
      setTrends(trendsData);
      setPredicted(predictedData);
    } catch (error) {
      console.error('Failed to load analytics:', error);
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(amount);
  };

  const getMaxAmount = (data: any[]) => {
    if (data.length === 0) return 1;
    return Math.max(...data.map(d => d.total || 0), 1);
  };

  if (loading) {
    return <div className="loading">Loading analytics...</div>;
  }

  const maxMonthly = getMaxAmount(monthly);
  const maxCategory = getMaxAmount(byCategory);

  return (
    <div className="analytics-page">
      <h1>Analytics & Insights</h1>

      {predicted && (
        <div className="prediction-card">
          <h2>Next Month Prediction</h2>
          <div className="prediction-content">
            <div className="prediction-amount">{formatCurrency(predicted.predictedAmount)}</div>
            <p>Based on analysis of {predicted.monthsConsidered} months of data</p>
          </div>
        </div>
      )}

      <div className="analytics-grid">
        <div className="analytics-card">
          <h2>Monthly Summary</h2>
          {monthly.length === 0 ? (
            <p className="empty-state">No data available</p>
          ) : (
            <div className="chart-container">
              {monthly.slice(-6).map(item => (
                <div key={item.month} className="bar-item">
                  <div className="bar-label">{new Date(item.month + '-01').toLocaleDateString('en-US', { month: 'short', year: 'numeric' })}</div>
                  <div className="bar-wrapper">
                    <div
                      className="bar"
                      style={{ width: `${(item.total / maxMonthly) * 100}%` }}
                    ></div>
                    <span className="bar-value">{formatCurrency(item.total)}</span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="analytics-card">
          <h2>Expenses by Category</h2>
          {byCategory.length === 0 ? (
            <p className="empty-state">No data available</p>
          ) : (
            <div className="chart-container">
              {byCategory.slice(0, 8).map(item => (
                <div key={item.categoryId} className="bar-item">
                  <div className="bar-label">{item.categoryName}</div>
                  <div className="bar-wrapper">
                    <div
                      className="bar category-bar"
                      style={{ width: `${(item.total / maxCategory) * 100}%` }}
                    ></div>
                    <span className="bar-value">{formatCurrency(item.total)}</span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="analytics-card">
          <h2>Yearly Summary</h2>
          {yearly.length === 0 ? (
            <p className="empty-state">No data available</p>
          ) : (
            <div className="yearly-list">
              {yearly.map(item => (
                <div key={item.year} className="yearly-item">
                  <span className="year">{item.year}</span>
                  <span className="amount">{formatCurrency(item.total)}</span>
                  <span className="count">{item.count} expenses</span>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="analytics-card">
          <h2>Expense Trends</h2>
          {trends.length === 0 ? (
            <p className="empty-state">No data available</p>
          ) : (
            <div className="trends-list">
              {trends.slice(-10).map((item, idx) => (
                <div key={idx} className="trend-item">
                  <span className="date">{new Date(item.date).toLocaleDateString()}</span>
                  <span className="amount">{formatCurrency(item.total)}</span>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Analytics;



