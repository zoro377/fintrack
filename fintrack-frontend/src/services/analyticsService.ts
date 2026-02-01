import api from './api';

export interface MonthlySummary {
  year: number;
  month: number;
  total: number;
}

export interface YearlySummary {
  year: number;
  total: number;
  count: number;
}

export interface CategorySummary {
  categoryId: number;
  categoryName: string;
  total: number;
}

export interface TrendPoint {
  date: string;
  total: number;
}

export interface PredictedExpense {
  predictedAmount: number;
  monthsConsidered: number;
}

export const analyticsService = {
  getMonthlySummary: async (): Promise<MonthlySummary[]> => {
    const response = await api.get('/analytics/monthly-summary');
    return response.data;
  },

  getYearlySummary: async (): Promise<YearlySummary[]> => {
    const response = await api.get('/analytics/yearly-summary');
    return response.data;
  },

  getCategorySummary: async (): Promise<CategorySummary[]> => {
    const response = await api.get('/analytics/by-category');
    return response.data;
  },

  getTrends: async (): Promise<TrendPoint[]> => {
    const response = await api.get('/analytics/trends');
    return response.data;
  },

  getPredictedExpense: async (): Promise<PredictedExpense> => {
    const response = await api.get('/analytics/predicted-expense');
    return response.data;
  }
};



