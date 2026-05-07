import { FormEvent, useEffect, useMemo, useState } from 'react';

import {
  activateBudgetModel,
  bindBudgetModelDimension,
  BudgetModel,
  BudgetModelDimensionBinding,
  createBudgetModel,
  deactivateBudgetModel,
  listBudgetModelDimensions,
  listBudgetModels,
} from './features/budgetModels/budgetModelApi';
import {
  createDimension,
  createMember,
  createWorkspace,
  Dimension,
  DimensionMember,
  DimensionType,
  listDimensions,
  listMembers,
  listWorkspaces,
  Workspace,
} from './features/metadata/metadataApi';

const dimensionTypes: DimensionType[] = [
  'ACCOUNT',
  'ENTITY',
  'TIME',
  'CATEGORY',
  'VERSION',
  'CUSTOM',
];

function App() {
  const [workspaces, setWorkspaces] = useState<Workspace[]>([]);
  const [dimensions, setDimensions] = useState<Dimension[]>([]);
  const [members, setMembers] = useState<DimensionMember[]>([]);
  const [budgetModels, setBudgetModels] = useState<BudgetModel[]>([]);
  const [modelBindings, setModelBindings] = useState<BudgetModelDimensionBinding[]>([]);
  const [selectedWorkspaceId, setSelectedWorkspaceId] = useState('');
  const [selectedDimensionId, setSelectedDimensionId] = useState('');
  const [selectedBudgetModelId, setSelectedBudgetModelId] = useState('');
  const [workspaceDraft, setWorkspaceDraft] = useState({ code: '', name: '' });
  const [dimensionDraft, setDimensionDraft] = useState({
    code: '',
    name: '',
    dimensionType: 'ACCOUNT' as DimensionType,
    system: true,
  });
  const [memberDraft, setMemberDraft] = useState({
    code: '',
    name: '',
    parentId: '',
    sortOrder: '',
  });
  const [budgetModelDraft, setBudgetModelDraft] = useState({
    code: '',
    name: '',
    description: '',
  });
  const [bindingDraft, setBindingDraft] = useState({
    dimensionId: '',
    requiredForEntry: true,
    displayOrder: '',
  });
  const [loading, setLoading] = useState(false);
  const [notice, setNotice] = useState('Connect the backend on port 8080 to manage metadata.');
  const [error, setError] = useState('');

  const selectedWorkspace = useMemo(
    () => workspaces.find((workspace) => workspace.id === selectedWorkspaceId),
    [selectedWorkspaceId, workspaces],
  );

  const selectedDimension = useMemo(
    () => dimensions.find((dimension) => dimension.id === selectedDimensionId),
    [dimensions, selectedDimensionId],
  );

  const selectedBudgetModel = useMemo(
    () => budgetModels.find((budgetModel) => budgetModel.id === selectedBudgetModelId),
    [budgetModels, selectedBudgetModelId],
  );

  const unboundDimensions = useMemo(() => {
    const boundIds = new Set(modelBindings.map((binding) => binding.dimensionId));
    return dimensions.filter((dimension) => !boundIds.has(dimension.id));
  }, [dimensions, modelBindings]);

  useEffect(() => {
    void refreshWorkspaces();
  }, []);

  useEffect(() => {
    if (!selectedWorkspaceId) {
      setDimensions([]);
      setSelectedDimensionId('');
      setBudgetModels([]);
      setSelectedBudgetModelId('');
      return;
    }
    void refreshDimensions(selectedWorkspaceId);
    void refreshBudgetModels(selectedWorkspaceId);
  }, [selectedWorkspaceId]);

  useEffect(() => {
    if (!selectedDimensionId) {
      setMembers([]);
      return;
    }
    void refreshMembers(selectedDimensionId);
  }, [selectedDimensionId]);

  useEffect(() => {
    if (!selectedBudgetModelId) {
      setModelBindings([]);
      return;
    }
    void refreshModelBindings(selectedBudgetModelId);
  }, [selectedBudgetModelId]);

  async function runAction(action: () => Promise<void>) {
    setLoading(true);
    setError('');
    try {
      await action();
    } catch (caught) {
      const message =
        caught && typeof caught === 'object' && 'message' in caught
          ? String(caught.message)
          : 'Request failed. Confirm the backend is running.';
      setError(message);
    } finally {
      setLoading(false);
    }
  }

  async function refreshWorkspaces() {
    await runAction(async () => {
      const nextWorkspaces = await listWorkspaces();
      setWorkspaces(nextWorkspaces);
      if (!selectedWorkspaceId && nextWorkspaces.length > 0) {
        setSelectedWorkspaceId(nextWorkspaces[0].id);
      }
      setNotice('Workspace metadata loaded.');
    });
  }

  async function refreshDimensions(workspaceId: string) {
    await runAction(async () => {
      const nextDimensions = await listDimensions(workspaceId);
      setDimensions(nextDimensions);
      setSelectedDimensionId((current) =>
        nextDimensions.some((dimension) => dimension.id === current)
          ? current
          : nextDimensions[0]?.id ?? '',
      );
      setNotice('Dimension metadata loaded.');
    });
  }

  async function refreshMembers(dimensionId: string) {
    await runAction(async () => {
      setMembers(await listMembers(dimensionId));
      setNotice('Member hierarchy loaded.');
    });
  }

  async function refreshBudgetModels(workspaceId: string) {
    await runAction(async () => {
      const nextBudgetModels = await listBudgetModels(workspaceId);
      setBudgetModels(nextBudgetModels);
      setSelectedBudgetModelId((current) =>
        nextBudgetModels.some((budgetModel) => budgetModel.id === current)
          ? current
          : nextBudgetModels[0]?.id ?? '',
      );
      setNotice('Budget models loaded.');
    });
  }

  async function refreshModelBindings(budgetModelId: string) {
    await runAction(async () => {
      setModelBindings(await listBudgetModelDimensions(budgetModelId));
      setNotice('Budget model dimensions loaded.');
    });
  }

  async function handleCreateWorkspace(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    await runAction(async () => {
      const workspace = await createWorkspace(workspaceDraft);
      if (workspace) {
        setWorkspaceDraft({ code: '', name: '' });
        const nextWorkspaces = await listWorkspaces();
        setWorkspaces(nextWorkspaces);
        setSelectedWorkspaceId(workspace.id);
        setNotice(`Workspace ${workspace.code} created.`);
      }
    });
  }

  async function handleCreateDimension(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedWorkspaceId) {
      setError('Create or select a workspace first.');
      return;
    }

    await runAction(async () => {
      const dimension = await createDimension({
        ...dimensionDraft,
        workspaceId: selectedWorkspaceId,
      });
      if (dimension) {
        setDimensionDraft({
          code: '',
          name: '',
          dimensionType: 'ACCOUNT',
          system: true,
        });
        await refreshDimensions(selectedWorkspaceId);
        setSelectedDimensionId(dimension.id);
        setNotice(`Dimension ${dimension.code} created.`);
      }
    });
  }

  async function handleCreateMember(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedDimensionId) {
      setError('Select a dimension first.');
      return;
    }

    await runAction(async () => {
      const sortOrder = memberDraft.sortOrder.trim()
        ? Number(memberDraft.sortOrder)
        : undefined;
      const member = await createMember(selectedDimensionId, {
        code: memberDraft.code,
        name: memberDraft.name,
        parentId: memberDraft.parentId || undefined,
        sortOrder,
      });
      if (member) {
        setMemberDraft({ code: '', name: '', parentId: '', sortOrder: '' });
        await refreshMembers(selectedDimensionId);
        setNotice(`Member ${member.code} created.`);
      }
    });
  }

  async function handleCreateBudgetModel(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedWorkspaceId) {
      setError('Create or select a workspace first.');
      return;
    }

    await runAction(async () => {
      const budgetModel = await createBudgetModel({
        workspaceId: selectedWorkspaceId,
        code: budgetModelDraft.code,
        name: budgetModelDraft.name,
        description: budgetModelDraft.description || undefined,
      });
      if (budgetModel) {
        setBudgetModelDraft({ code: '', name: '', description: '' });
        await refreshBudgetModels(selectedWorkspaceId);
        setSelectedBudgetModelId(budgetModel.id);
        setNotice(`Budget model ${budgetModel.code} created.`);
      }
    });
  }

  async function handleBindDimension(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedBudgetModelId) {
      setError('Select a budget model first.');
      return;
    }
    if (!bindingDraft.dimensionId) {
      setError('Select a dimension to bind.');
      return;
    }

    await runAction(async () => {
      const displayOrder = bindingDraft.displayOrder.trim()
        ? Number(bindingDraft.displayOrder)
        : undefined;
      const binding = await bindBudgetModelDimension(selectedBudgetModelId, {
        dimensionId: bindingDraft.dimensionId,
        requiredForEntry: bindingDraft.requiredForEntry,
        displayOrder,
      });
      if (binding) {
        setBindingDraft({ dimensionId: '', requiredForEntry: true, displayOrder: '' });
        await refreshModelBindings(selectedBudgetModelId);
        setNotice(`Dimension ${binding.dimensionCode} bound to model.`);
      }
    });
  }

  async function handleActivateBudgetModel() {
    if (!selectedBudgetModelId || !selectedWorkspaceId) {
      return;
    }
    await runAction(async () => {
      const budgetModel = await activateBudgetModel(selectedBudgetModelId);
      if (budgetModel) {
        await refreshBudgetModels(selectedWorkspaceId);
        setSelectedBudgetModelId(budgetModel.id);
        setNotice(`Budget model ${budgetModel.code} activated.`);
      }
    });
  }

  async function handleDeactivateBudgetModel() {
    if (!selectedBudgetModelId || !selectedWorkspaceId) {
      return;
    }
    await runAction(async () => {
      const budgetModel = await deactivateBudgetModel(selectedBudgetModelId);
      if (budgetModel) {
        await refreshBudgetModels(selectedWorkspaceId);
        setSelectedBudgetModelId(budgetModel.id);
        setNotice(`Budget model ${budgetModel.code} deactivated.`);
      }
    });
  }

  const memberNameById = new Map(members.map((member) => [member.id, member.name]));

  return (
    <main className="app-shell">
      <header className="top-bar">
        <div>
          <p className="eyebrow">Metadata Management</p>
          <h1>Budget Platform</h1>
        </div>
        <button type="button" onClick={() => void refreshWorkspaces()} disabled={loading}>
          Refresh
        </button>
      </header>

      <section className="status-row" aria-live="polite">
        <span>{notice}</span>
        {error ? <strong>{error}</strong> : null}
      </section>

      <section className="metadata-layout" aria-label="Metadata workspace">
        <section className="panel" aria-labelledby="workspace-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 1</p>
              <h2 id="workspace-title">Workspaces</h2>
            </div>
            <span>{workspaces.length}</span>
          </div>

          <form className="form-grid" onSubmit={(event) => void handleCreateWorkspace(event)}>
            <label>
              Code
              <input
                required
                value={workspaceDraft.code}
                onChange={(event) =>
                  setWorkspaceDraft({ ...workspaceDraft, code: event.target.value })
                }
              />
            </label>
            <label>
              Name
              <input
                required
                value={workspaceDraft.name}
                onChange={(event) =>
                  setWorkspaceDraft({ ...workspaceDraft, name: event.target.value })
                }
              />
            </label>
            <button type="submit" disabled={loading}>
              Create
            </button>
          </form>

          <div className="list" role="list">
            {workspaces.map((workspace) => (
              <button
                className="list-row"
                data-selected={workspace.id === selectedWorkspaceId}
                key={workspace.id}
                onClick={() => setSelectedWorkspaceId(workspace.id)}
                role="listitem"
                type="button"
              >
                <span>
                  <strong>{workspace.code}</strong>
                  {workspace.name}
                </span>
                <em>{workspace.status}</em>
              </button>
            ))}
          </div>
        </section>

        <section className="panel" aria-labelledby="dimension-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 2</p>
              <h2 id="dimension-title">Dimensions</h2>
            </div>
            <span>{selectedWorkspace?.code ?? 'No workspace'}</span>
          </div>

          <form className="form-grid" onSubmit={(event) => void handleCreateDimension(event)}>
            <label>
              Code
              <input
                required
                value={dimensionDraft.code}
                onChange={(event) =>
                  setDimensionDraft({ ...dimensionDraft, code: event.target.value })
                }
              />
            </label>
            <label>
              Name
              <input
                required
                value={dimensionDraft.name}
                onChange={(event) =>
                  setDimensionDraft({ ...dimensionDraft, name: event.target.value })
                }
              />
            </label>
            <label>
              Type
              <select
                value={dimensionDraft.dimensionType}
                onChange={(event) =>
                  setDimensionDraft({
                    ...dimensionDraft,
                    dimensionType: event.target.value as DimensionType,
                  })
                }
              >
                {dimensionTypes.map((type) => (
                  <option key={type}>{type}</option>
                ))}
              </select>
            </label>
            <label className="check-row">
              <input
                checked={dimensionDraft.system}
                type="checkbox"
                onChange={(event) =>
                  setDimensionDraft({ ...dimensionDraft, system: event.target.checked })
                }
              />
              System dimension
            </label>
            <button type="submit" disabled={loading || !selectedWorkspaceId}>
              Create
            </button>
          </form>

          <div className="list" role="list">
            {dimensions.map((dimension) => (
              <button
                className="list-row"
                data-selected={dimension.id === selectedDimensionId}
                key={dimension.id}
                onClick={() => setSelectedDimensionId(dimension.id)}
                role="listitem"
                type="button"
              >
                <span>
                  <strong>{dimension.code}</strong>
                  {dimension.name}
                </span>
                <em>{dimension.dimensionType}</em>
              </button>
            ))}
          </div>
        </section>

        <section className="panel wide-panel" aria-labelledby="member-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 3</p>
              <h2 id="member-title">Members</h2>
            </div>
            <span>{selectedDimension?.code ?? 'No dimension'}</span>
          </div>

          <form className="member-form" onSubmit={(event) => void handleCreateMember(event)}>
            <label>
              Code
              <input
                required
                value={memberDraft.code}
                onChange={(event) =>
                  setMemberDraft({ ...memberDraft, code: event.target.value })
                }
              />
            </label>
            <label>
              Name
              <input
                required
                value={memberDraft.name}
                onChange={(event) =>
                  setMemberDraft({ ...memberDraft, name: event.target.value })
                }
              />
            </label>
            <label>
              Parent
              <select
                value={memberDraft.parentId}
                onChange={(event) =>
                  setMemberDraft({ ...memberDraft, parentId: event.target.value })
                }
              >
                <option value="">Root member</option>
                {members.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Sort
              <input
                min="0"
                type="number"
                value={memberDraft.sortOrder}
                onChange={(event) =>
                  setMemberDraft({ ...memberDraft, sortOrder: event.target.value })
                }
              />
            </label>
            <button type="submit" disabled={loading || !selectedDimensionId}>
              Create
            </button>
          </form>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Code</th>
                  <th>Name</th>
                  <th>Parent</th>
                  <th>Status</th>
                  <th>Leaf</th>
                </tr>
              </thead>
              <tbody>
                {members.map((member) => (
                  <tr key={member.id}>
                    <td>{member.code}</td>
                    <td>{member.name}</td>
                    <td>{member.parentId ? memberNameById.get(member.parentId) : 'Root'}</td>
                    <td>{member.status}</td>
                    <td>{member.leaf ? 'Yes' : 'No'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </section>

      <section className="model-layout" aria-label="Budget model management">
        <section className="panel" aria-labelledby="model-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 4</p>
              <h2 id="model-title">Budget Models</h2>
            </div>
            <span>{selectedWorkspace?.code ?? 'No workspace'}</span>
          </div>

          <form className="model-form" onSubmit={(event) => void handleCreateBudgetModel(event)}>
            <label>
              Code
              <input
                required
                value={budgetModelDraft.code}
                onChange={(event) =>
                  setBudgetModelDraft({ ...budgetModelDraft, code: event.target.value })
                }
              />
            </label>
            <label>
              Name
              <input
                required
                value={budgetModelDraft.name}
                onChange={(event) =>
                  setBudgetModelDraft({ ...budgetModelDraft, name: event.target.value })
                }
              />
            </label>
            <label>
              Description
              <input
                value={budgetModelDraft.description}
                onChange={(event) =>
                  setBudgetModelDraft({
                    ...budgetModelDraft,
                    description: event.target.value,
                  })
                }
              />
            </label>
            <button type="submit" disabled={loading || !selectedWorkspaceId}>
              Create
            </button>
          </form>

          <div className="list" role="list">
            {budgetModels.map((budgetModel) => (
              <button
                className="list-row"
                data-selected={budgetModel.id === selectedBudgetModelId}
                key={budgetModel.id}
                onClick={() => setSelectedBudgetModelId(budgetModel.id)}
                role="listitem"
                type="button"
              >
                <span>
                  <strong>{budgetModel.code}</strong>
                  {budgetModel.name}
                </span>
                <em>{budgetModel.status}</em>
              </button>
            ))}
          </div>
        </section>

        <section className="panel" aria-labelledby="binding-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 5</p>
              <h2 id="binding-title">Model Dimensions</h2>
            </div>
            <span>{selectedBudgetModel?.code ?? 'No model'}</span>
          </div>

          <form className="binding-form" onSubmit={(event) => void handleBindDimension(event)}>
            <label>
              Dimension
              <select
                value={bindingDraft.dimensionId}
                onChange={(event) =>
                  setBindingDraft({ ...bindingDraft, dimensionId: event.target.value })
                }
              >
                <option value="">Select dimension</option>
                {unboundDimensions.map((dimension) => (
                  <option key={dimension.id} value={dimension.id}>
                    {dimension.code} - {dimension.dimensionType}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Order
              <input
                min="0"
                type="number"
                value={bindingDraft.displayOrder}
                onChange={(event) =>
                  setBindingDraft({ ...bindingDraft, displayOrder: event.target.value })
                }
              />
            </label>
            <label className="check-row">
              <input
                checked={bindingDraft.requiredForEntry}
                type="checkbox"
                onChange={(event) =>
                  setBindingDraft({
                    ...bindingDraft,
                    requiredForEntry: event.target.checked,
                  })
                }
              />
              Required for entry
            </label>
            <button type="submit" disabled={loading || !selectedBudgetModelId}>
              Bind
            </button>
          </form>

          <div className="model-actions">
            <button
              type="button"
              onClick={() => void handleActivateBudgetModel()}
              disabled={loading || !selectedBudgetModelId}
            >
              Activate
            </button>
            <button
              type="button"
              onClick={() => void handleDeactivateBudgetModel()}
              disabled={loading || !selectedBudgetModelId}
            >
              Deactivate
            </button>
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Dimension</th>
                  <th>Role</th>
                  <th>Required</th>
                  <th>Order</th>
                </tr>
              </thead>
              <tbody>
                {modelBindings.map((binding) => (
                  <tr key={binding.id}>
                    <td>
                      {binding.dimensionCode} - {binding.dimensionName}
                    </td>
                    <td>{binding.dimensionRole}</td>
                    <td>{binding.requiredForEntry ? 'Yes' : 'No'}</td>
                    <td>{binding.displayOrder}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </section>
    </main>
  );
}

export default App;
