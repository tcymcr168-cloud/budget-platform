const stageItems = [
  { label: 'Knowledge Base', value: 'Complete' },
  { label: 'Architecture', value: 'Complete' },
  { label: 'Product Scope', value: 'Complete' },
  { label: 'Engineering', value: 'Baseline' },
];

function App() {
  return (
    <main className="app-shell">
      <section className="workspace-panel" aria-labelledby="app-title">
        <div>
          <p className="eyebrow">Enterprise Budget Management</p>
          <h1 id="app-title">Budget Platform</h1>
          <p className="summary">
            Web Native budgeting workspace for governed models, templates,
            submissions, queries, and actual imports.
          </p>
        </div>

        <dl className="stage-grid" aria-label="Project baseline status">
          {stageItems.map((item) => (
            <div className="stage-item" key={item.label}>
              <dt>{item.label}</dt>
              <dd>{item.value}</dd>
            </div>
          ))}
        </dl>
      </section>
    </main>
  );
}

export default App;
