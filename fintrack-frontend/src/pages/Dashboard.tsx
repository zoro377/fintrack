import { useEffect, useState } from 'react';
import api from '../services/api';

interface SummaryCard {
  title: string;
  value: string;
}

const Dashboard = () => {
  const [cards, setCards] = useState<SummaryCard[]>([
    { title: 'Monthly Total', value: '$0.00' },
    { title: 'Yearly Total', value: '$0.00' },
    { title: 'Top Category', value: 'N/A' }
  ]);

  useEffect(() => {
    // Placeholder fetch example
    const fetchSummary = async () => {
      try {
        const response = await api.get('/analytics/monthly-summary');
        if (response.data?.length) {
          const latest = response.data[response.data.length - 1];
          setCards((current) => [
            { ...current[0], value: `$${Number(latest.total).toFixed(2)}` },
            current[1],
            current[2]
          ]);
        }
      } catch (error) {
        console.warn('Unable to load summary yet', error);
      }
    };
    fetchSummary();
  }, []);

  return (
    <section>
      <h2>Dashboard</h2>
      <p className="muted">Phase 8 placeholder UI for upcoming modules.</p>
      <div className="cards-grid">
        {cards.map((card) => (
          <article key={card.title} className="summary-card">
            <h3>{card.title}</h3>
            <p>{card.value}</p>
          </article>
        ))}
      </div>
    </section>
  );
};

export default Dashboard;
