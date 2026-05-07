import { FormEvent, useEffect, useMemo, useState } from 'react';

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
  const [selectedWorkspaceId, setSelectedWorkspaceId] = useState('');
  const [selectedDimensionId, setSelectedDimensionId] = useState('');
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

  useEffect(() => {
    void refreshWorkspaces();
  }, []);

  useEffect(() => {
    if (!selectedWorkspaceId) {
      setDimensions([]);
      setSelectedDimensionId('');
      return;
    }
    void refreshDimensions(selectedWorkspaceId);
  }, [selectedWorkspaceId]);

  useEffect(() => {
    if (!selectedDimensionId) {
      setMembers([]);
      return;
    }
    void refreshMembers(selectedDimensionId);
  }, [selectedDimensionId]);

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
    </main>
  );
}

export default App;
