import { ReactNode } from 'react';
import './Layout.css';

interface LayoutProps {
  children: ReactNode;
}

const Layout = ({ children }: LayoutProps) => (
  <div className="app-shell">
    <header className="app-header">
      <h1>FinTrack</h1>
      <span>Smart Expense Tracking</span>
    </header>
    <main className="app-content">{children}</main>
  </div>
);

export default Layout;
