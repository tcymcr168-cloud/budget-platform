# PROJECT_STEP_RECORD

## PROJECT-BOOTSTRAP-001

阶段名称：项目环境检查、BPC 资料索引与治理基线建立

记录日期：2026-05-06

### 阶段目标

本阶段只建立项目治理基线、完成环境检查、检查项目目录、索引 SAP BPC PDF 资料、制定 BPC 知识抽取计划。不写业务代码，不修改 `backend/src` 或 `frontend/src`，不新增数据库对象或迁移。

### 输入资料目录

`C:\codex\budget-platform\docs\source\bpc-pdf`

该目录下共有 6 个 SAP BPC 相关 PDF 文件。本阶段只做索引，不复制 PDF 原文。

### 环境检查结果

| 检查项 | 结果 | 说明 |
| --- | --- | --- |
| 当前工作目录 | `C:\codex\budget-platform` | 正常 |
| Git 仓库状态 | 失败 | 执行 `git status --short` 返回 `fatal: not a git repository (or any of the parent directories): .git` |
| 当前分支 | 不可用 | 当前目录不是 Git 仓库，无法读取分支 |
| remote 地址 | 不可用 | 当前目录不是 Git 仓库，无法读取 remote |
| Git 可执行文件 | `C:\Program Files\Git\cmd\git.exe` | Git 已安装，但当前目录未初始化为仓库 |
| Node 版本 | 不可用 | `node --version` 指向 `C:\Program Files\WindowsApps\OpenAI.Codex_26.429.8261.0_x64__2p2nqsd0c76g0\app\resources\node.exe`，启动失败：`拒绝访问` |
| pnpm 版本 | 不可用 | `where.exe pnpm` 返回未找到 |
| Java 版本 | 不可用 | `where.exe java` 返回未找到；直接执行版本命令超时 |
| Maven 版本 | 不可用 | `where.exe mvn` 返回未找到；直接执行版本命令超时 |
| Python 版本 | 不可用 | `py --version` 返回 `No installed Python found!`，`where.exe python` 返回未找到 |
| Docker | 不可用 | `docker --version` 返回命令未找到 |
| PostgreSQL CLI | 不可用 | `psql --version` 返回命令未找到 |

### 目录检查结果

| 路径 | 是否存在 | 说明 |
| --- | --- | --- |
| `backend` | 是 | 目录存在 |
| `frontend` | 是 | 目录存在 |
| `docs` | 是 | 目录存在 |
| `docs/source/bpc-pdf` | 是 | 目录存在 |
| `AGENTS.md` | 是 | 本阶段写入治理规则 |
| `PROJECT_STEP_RECORD.md` | 是 | 本阶段写入阶段记录 |
| `README.md` | 是 | 文件存在，当前为空 |
| `.gitignore` | 是 | 已包含并补充 PDF 排除说明 |

补充检查：当前 `backend/src` 不存在，`frontend/src` 不存在；本阶段未创建或修改这两个路径。

### PDF 索引结果

| 序号 | PDF 文件名 | 大小 | 页数判断 | 可直接提取文本判断 | 初步主题 | OCR 建议 |
| --- | --- | ---: | ---: | --- | --- | --- |
| 1 | `1.英文版官方标准PA教材-BPC420 SAP Business Planning and Consolidation, Version for SAP NetWeaver Standard Administration and Planning Configuration共379页 2018年编著 PDF版.pdf` | 195.4 MB | 381 个 Page 标记，文件名标注 379 页 | 不能稳定直接提取正文 | Standard 管理与计划配置、模型、维度、模板、填报、Work Status | 需要 |
| 2 | `2.英文版官方标准PA教材-BPC430 SAP Business Planning and Consolidation 11.0 version for SAP BW4HANA Reporting and Planning共231页 2018年编著 PDF版.pdf` | 111.4 MB | 231 页 | 不能稳定直接提取正文 | 报表、查询、计划读取与展示 | 需要 |
| 3 | `3.英文版官方标准PA教材-BPC440  SAP Business Planning and Consolidation Consolidation共267页 2018年编著 PDF版.pdf` | 137.4 MB | 267 页 | 不能稳定直接提取正文 | 合并流程、合并模型和报表思想 | 需要 |
| 4 | `4.英文版官方标准PA教材-BPC450_col17 SAP Embedded Business Planning and Consolidation 11.0 version for SAP BW4HANA共290页 2018年编著 PDF版.pdf` | 143.6 MB | 290 页 | 不能稳定直接提取正文 | Embedded BPC、BW/4HANA 嵌入式计划 | 需要 |
| 5 | `5.英文版官方标准PA教材-S4F80 SAP BPC Optimized for SAP S4HANA共230页 2018年编著 PDF版.pdf` | 121.0 MB | 230 页 | 不能稳定直接提取正文 | S/4HANA Optimized BPC、预算与实际同源 | 需要 |
| 6 | `6.英文版官方标准PA教材-s4f90 SAP Business Planning and Consolidation Embedded Consolidation共343页 2018年编著 PDF版.pdf` | 177.6 MB | 343 页 | 不能稳定直接提取正文 | Embedded Consolidation、状态、流程、权限和报表思想 | 需要 |

可读性判断依据：PDF 图片对象数量很高，原始语义关键词命中弱，说明这些文件疑似扫描图像为主；后续章节级知识抽取需要 OCR 或专业 PDF 文本抽取工具复核。

### 新增/修改文件

| 文件 | 变更 |
| --- | --- |
| `.gitignore` | 补充 SAP BPC PDF 原文禁止提交说明，保留 PDF 排除规则 |
| `AGENTS.md` | 写入项目治理、角色、阶段拆分、开发边界、测试、提交、文档沉淀和 PDF 保护规则 |
| `PROJECT_STEP_RECORD.md` | 写入 PROJECT-BOOTSTRAP-001 阶段记录 |
| `docs/product/bpc-source-index.md` | 写入 BPC PDF 索引、可读性判断和主题来源建议 |
| `docs/product/bpc-knowledge-extraction-plan.md` | 写入 BPC-KB-001 至 BPC-KB-009 抽取计划 |

### 关键决策

1. 当前阶段只做治理基线和资料索引，不写业务代码。
2. BPC PDF 原文和 OCR 全文不得进入 GitHub。
3. 后续 BPC 知识抽取按 BPC-KB-001 至 BPC-KB-009 小阶段推进。
4. 预算主线优先，合并报表、BI 图表、ERP 直连不进入当前阶段开发范围。
5. 当前目录不是 Git 仓库，需要后续明确仓库初始化或切换到正确仓库目录。

### 未解决问题

1. 当前目录缺少 `.git`，Git 状态、分支和 remote 无法获取。
2. Node 指向 Codex 应用内嵌 `node.exe`，启动被拒绝，无法确认项目可用 Node 版本。
3. pnpm、Java、Maven、Python、Docker、PostgreSQL CLI 当前不可用或未在 PATH 中。
4. PDF 缺少专业解析工具，页数和可读性为对象标记级索引结论，后续需要复核。
5. `README.md` 当前为空，后续阶段需补充项目说明。

### 下一阶段建议

建议下一阶段进入 BPC-KB-001：核心概念与术语。进入前优先解决本地资料抽取能力，建议安装或配置可靠的 PDF 文本抽取/OCR 工具，并继续保持“不提交 PDF 原文、不提交 OCR 全文”的资料治理边界。

### 测试与验证结果

本阶段不写业务代码，因此未运行前后端完整测试。

已执行验证：

1. `git status --short`：失败，真实错误为 `fatal: not a git repository (or any of the parent directories): .git`。
2. 已确认 `AGENTS.md` 存在。
3. 已确认 `PROJECT_STEP_RECORD.md` 存在。
4. 已确认 `docs/product/bpc-source-index.md` 存在。
5. 已确认 `docs/product/bpc-knowledge-extraction-plan.md` 存在。
6. 已确认 `.gitignore` 存在且排除 `docs/source/bpc-pdf/*.pdf` 和 `docs/source/bpc-pdf/*.PDF`。

### 风险

1. 环境运行时缺失会阻塞后续前后端开发和测试。
2. 当前目录不是 Git 仓库，会阻塞正式源码状态管理、提交和 PR 流程。
3. PDF 疑似扫描版，若没有 OCR，BPC 知识抽取只能停留在文件名和粗粒度索引层面。

### 是否建议关闭本阶段

建议关闭 PROJECT-BOOTSTRAP-001。

关闭理由：本阶段限定的治理文件、资料索引、抽取计划和验证均已完成；未解决问题已记录，不影响本阶段关闭，但会影响后续开发阶段启动。

## GIT-SETUP-001

阶段名称：GitHub 登录确认与正式仓库绑定

记录日期：2026-05-06

### 阶段目标

本阶段只处理 GitHub 登录确认、Git 可用性确认、正式仓库绑定和 PDF 资料保护。不写业务代码，不修改 `backend/src` 或 `frontend/src`，不新增 migration，不执行 commit，不执行 push。

### 工具与登录检查

| 检查项 | 结果 | 说明 |
| --- | --- | --- |
| `where git` | 未找到 | 当前 PowerShell PATH 尚未包含 Git |
| Git 完整路径 | `C:\Program Files\Git\cmd\git.exe` | 可用 |
| `git --version` | `git version 2.54.0.windows.1` | 通过完整路径确认 |
| `where gh` | 未找到 | 当前 PowerShell PATH 尚未刷新 GitHub CLI |
| GitHub CLI 完整路径 | `C:\Program Files\GitHub CLI\gh.exe` | 可用 |
| `gh --version` | `gh version 2.92.0 (2026-04-28)` | 通过完整路径确认 |
| `gh auth status` | 已登录 | 登录账号 `tcymcr168-cloud`，Git 协议为 HTTPS |

### 当前目录与资料检查

| 检查项 | 结果 |
| --- | --- |
| 当前目录 | `C:\codex\budget-platform` |
| 初始 Git 状态 | 初始检查时不是 Git 仓库 |
| PDF 原目录 | `C:\codex\budget-platform\docs\source\bpc-pdf` |
| PDF 原目录文件数 | 6 个 PDF |
| PDF 备份目录 | `C:\codex\bpc-pdf-backup` |
| PDF 备份文件数 | 6 个 PDF |

### 仓库绑定策略

优先尝试方案 A，但 `Rename-Item C:\codex\budget-platform budget-platform-local-bootstrap` 失败，真实错误为：

`The process cannot access the file because it is being used by another process.`

由于当前工作目录被进程占用，未强行清理或删除目录。随后检查正式远程仓库：

`https://github.com/tcymcr168-cloud/budget-platform.git`

远程仓库可访问，`origin/main` 存在，最近提交为：

`bc00af8 Initial commit`

最终采用方案 B 的安全变体：

1. 在当前目录执行 `git init`。
2. 将分支命名为 `main`。
3. 添加唯一 remote：`https://github.com/tcymcr168-cloud/budget-platform.git`。
4. 执行 `git fetch origin`。
5. 由于远程 `main` 只有 `README.md`，执行 `git reset --mixed origin/main` 对齐本地 HEAD 与远程提交历史；该操作不覆盖或删除工作区文件。
6. 设置 `main` 跟踪 `origin/main`。

### PDF 保护检查

`.gitignore` 已包含：

```gitignore
docs/source/bpc-pdf/*.pdf
docs/source/bpc-pdf/*.PDF
```

本阶段未删除 PDF，未执行 `git add`，未提交 PDF。

### 新增/修改文件

| 文件 | 变更 |
| --- | --- |
| `PROJECT_STEP_RECORD.md` | 追加 GIT-SETUP-001 阶段记录 |

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| `git status --short` | 已执行，PDF 未出现在待提交列表 |
| `git remote -v` | 指向 `https://github.com/tcymcr168-cloud/budget-platform.git` |
| `git branch --show-current` | `main` |
| `git log --oneline -5` | 可读取远程初始提交 |
| `AGENTS.md` | 存在 |
| `PROJECT_STEP_RECORD.md` | 存在 |
| `docs/product/bpc-source-index.md` | 存在 |
| `docs/product/bpc-knowledge-extraction-plan.md` | 存在 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |

### 未解决问题

1. 当前 PowerShell PATH 尚未刷新，直接执行 `git` 和 `gh` 仍可能失败；本阶段使用完整路径完成。
2. 远程 `README.md` 与本地空 `README.md` 不一致，`git reset --mixed origin/main` 后显示 `README.md` 为本地修改。后续提交前需要决定保留远程 README 内容、保留本地空 README，或正式补充项目 README。

### 是否建议关闭本阶段

建议关闭 GIT-SETUP-001。

关闭理由：GitHub 登录已确认，正式远程仓库已绑定，PDF 原目录和备份目录均保留，PDF 已被 `.gitignore` 排除，未写业务代码，未 commit，未 push。

### 下一阶段建议

建议下一阶段先处理仓库首次正式提交计划：确认 `README.md` 取舍，审查待提交文件范围，明确是否将治理文件和产品文档提交到正式仓库。提交或 push 前必须先输出计划并等待授权。

### 授权提交与推送记录

用户已授权提交并推送 GIT-SETUP-001 当前治理基线文件，commit message 使用：

`chore: initialize governance and BPC source index`

本次提交范围限定为：

1. `.gitignore`
2. `AGENTS.md`
3. `PROJECT_STEP_RECORD.md`
4. `docs/product/bpc-source-index.md`
5. `docs/product/bpc-knowledge-extraction-plan.md`

明确不提交 `docs/source/bpc-pdf` 下的 PDF 原文，不提交业务代码，不提交 `backend/src`、`frontend/src` 或 migration。

## FOUNDATION-001

阶段名称：开发工具链基线修复

记录日期：2026-05-06

### 阶段目标

本阶段只修复和诊断开发工具链，确保后续 OCR、前后端开发和测试具备基础运行环境。不写业务代码，不修改 `backend/src` 或 `frontend/src`，不新增 migration，不执行 commit，不执行 push，不开始 BPC-KB-001 或 OCR。

### 初始检查结果

| 工具 | 初始状态 | 真实错误或说明 |
| --- | --- | --- |
| Git | PATH 不可用 | `where.exe git` 返回 `INFO: Could not find files for the given pattern(s).`；`git --version` 返回 `The term 'git' is not recognized` |
| GitHub CLI | PATH 不可用 | `where.exe gh` 返回未找到；`gh --version` 返回 `The term 'gh' is not recognized` |
| GitHub CLI 登录 | 已登录但需完整路径 | `C:\Program Files\GitHub CLI\gh.exe auth status` 显示账号 `tcymcr168-cloud` 已登录，协议 HTTPS |
| Node.js | PATH 错误命中 | `where.exe node` 命中 Codex 内嵌 `node.exe`；`node -v` 失败：`拒绝访问` |
| pnpm | 不可用 | `pnpm -v` 返回 `The term 'pnpm' is not recognized` |
| Java | PATH 不可用 | `where.exe java` 返回未找到；`java -version` 返回 `The term 'java' is not recognized` |
| Maven | PATH 不可用 | `where.exe mvn` 返回未找到；`mvn -version` 返回 `The term 'mvn' is not recognized` |
| py | 可用 | 初始 `py --version` 显示 Python 3.11.9 |
| python | PATH 错误命中 | 初始 `python --version` 返回 Microsoft Store alias 提示 |
| pip | 不可用 | `pip --version` 返回 `The term 'pip' is not recognized` |
| Docker | 不可用 | `docker --version` 返回 `The term 'docker' is not recognized` |
| PostgreSQL CLI | 初始 PATH 不可用 | 初始 `psql --version` 返回 `The term 'psql' is not recognized` |

### 安装与修复动作

| 工具 | 动作 | 结果 |
| --- | --- | --- |
| Git | 执行 `winget install --id Git.Git -e --accept-source-agreements --accept-package-agreements` | winget 返回已安装且无可用更新 |
| GitHub CLI | 执行 `winget install --id GitHub.cli -e --accept-source-agreements --accept-package-agreements` | winget 返回已安装且无可用更新 |
| Node.js LTS | 执行 `winget install --id OpenJS.NodeJS.LTS -e --accept-source-agreements --accept-package-agreements` | winget 返回已安装且无可用更新；标准路径为 `C:\Program Files\nodejs` |
| Java 17 | 执行 `winget install --id EclipseAdoptium.Temurin.17.JDK -e --accept-source-agreements --accept-package-agreements` | winget 返回已安装且无可用更新；标准路径为 `C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot` |
| Maven | 尝试 `winget install --id Apache.Maven -e --accept-source-agreements --accept-package-agreements` | winget 返回 `No package found matching input criteria.` |
| Maven | 从 Apache 官方下载并解压 Maven 3.9.15 到 `C:\codex\tools\maven` | 安装成功；最终 PATH 中已有 `C:\Program Files\Apache\Maven\apache-maven-3.9.11` 且优先命中 Maven 3.9.11 |
| Python 3.12 | 执行 `winget install --id Python.Python.3.12 -e --accept-source-agreements --accept-package-agreements` | 安装成功，版本 3.12.10 |
| pnpm | 执行 `corepack enable` 与 `corepack prepare pnpm@latest --activate` | 安装/启用成功，版本 10.33.3 |
| PATH/JAVA_HOME/MAVEN_HOME | 写入用户级环境变量 | 已将 Git、GH、Node、Java、Maven、Python 3.12、pip 路径加入用户 PATH；已设置用户级 `JAVA_HOME` 与 `MAVEN_HOME` |

### 最终验证结果

最终验证在加载机器 PATH 与用户 PATH 后执行，等价于新终端环境。当前 Codex 父进程仍保留旧 PATH，因此建议重启 Codex 或重新打开终端后直接使用这些命令。

| 工具 | 最终结果 |
| --- | --- |
| `where git` | `C:\Program Files\Git\cmd\git.exe` |
| `git --version` | `git version 2.54.0.windows.1` |
| `where gh` | `C:\Program Files\GitHub CLI\gh.exe` |
| `gh --version` | `gh version 2.92.0 (2026-04-28)` |
| `gh auth status` | 已登录 `github.com`，账号 `tcymcr168-cloud`，协议 HTTPS |
| `where node` | `C:\Program Files\nodejs\node.exe` |
| `node -v` | `v24.15.0` |
| `where pnpm` | `C:\Program Files\nodejs\pnpm.CMD` 与用户 npm shim |
| `pnpm -v` | `10.33.3` |
| `where java` | `C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot\bin\java.exe` |
| `java -version` | OpenJDK `17.0.19` Temurin |
| `where mvn` | `C:\Program Files\Apache\Maven\apache-maven-3.9.11\bin\mvn.cmd`，另有 `C:\codex\tools\maven\bin\mvn.cmd` |
| `mvn -version` | Apache Maven `3.9.11`，Java `17.0.19` |
| `where py` | `C:\Windows\py.exe` |
| `py --version` | `Python 3.12.10` |
| `where python` | `C:\Users\Administrator\AppData\Local\Programs\Python\Python312\python.exe` |
| `python --version` | `Python 3.12.10` |
| `where pip` | `C:\Users\Administrator\AppData\Local\Programs\Python\Python312\Scripts\pip.exe` |
| `pip --version` | `pip 25.0.1` for Python 3.12 |
| `docker --version` | 不可用，命令未找到 |
| `where psql` | `C:\Program Files\PostgreSQL\16\bin\psql.exe` |
| `psql --version` | `psql (PostgreSQL) 16.13` |

### 仓库与资料保护检查

| 检查项 | 结果 |
| --- | --- |
| 当前目录 | `C:\codex\budget-platform` |
| 当前分支 | `main` |
| remote | `origin https://github.com/tcymcr168-cloud/budget-platform.git` |
| `git status --short` | `README.md` 仍有本地修改；另有若干 `docs/product` 未跟踪文件；本阶段未处理这些历史遗留变更 |
| PDF 原目录 | `docs/source/bpc-pdf` 存在 |
| PDF 文件数 | 6 |
| PDF ignore | `.gitignore` 命中 `docs/source/bpc-pdf/*.pdf` 和 `docs/source/bpc-pdf/*.PDF` |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 成功工具

Git、GitHub CLI、GitHub CLI 登录、Node.js、pnpm、Java 17、Maven、Python 3.12、pip、PostgreSQL CLI 均已可用。

### 失败工具

Docker 仍不可用，`docker --version` 返回命令未找到。本阶段未安装 Docker，因为 PostgreSQL CLI 已可用，且 Docker 并非当前阶段必须项。

### 是否需要用户重启终端

建议重启 Codex 或重新打开 PowerShell/终端。

原因：用户级 PATH、`JAVA_HOME`、`MAVEN_HOME` 已写入，但当前 Codex 父进程仍继承旧 PATH；本阶段最终验证通过显式加载机器 PATH 与用户 PATH 完成。新终端应直接命中系统 Node，而不是 Codex 内嵌 `node.exe`。

### 是否建议关闭本阶段

建议关闭 FOUNDATION-001。

关闭理由：除 Docker 外，后续 OCR、前后端开发和测试所需基础工具链已具备；Docker 状态已明确，PostgreSQL CLI 已可用；资料保护和业务代码边界已确认。

### 下一阶段建议

建议下一阶段进入 FOUNDATION-002：OCR 与 PDF 文本抽取工具链验证。该阶段只验证 OCR/PDF 抽取能力，不开始 BPC-KB-001，不提交 PDF 原文或 OCR 全文。

## FOUNDATION-002

阶段名称：BPC PDF OCR 与文本抽取工具链验证

记录日期：2026-05-06

### 阶段目标

本阶段只验证 OCR 与 PDF 文本抽取工具链，不写业务代码，不进入正式 BPC-KB-001，不执行全量 OCR，不提交 PDF 原文或大型 OCR 全文。

### 前置 PATH 检查

当前 Codex/PowerShell 父进程仍未加载 FOUNDATION-001 写入的用户级 PATH。普通命令直接执行时仍失败：

| 命令 | 结果 |
| --- | --- |
| `git --version` | `The term 'git' is not recognized` |
| `gh --version` | `The term 'gh' is not recognized` |
| `node -v` | 命中 Codex 内嵌 `node.exe` 并返回 `拒绝访问` |
| `pnpm -v` | `The term 'pnpm' is not recognized` |
| `java -version` | `The term 'java' is not recognized` |
| `mvn -version` | `The term 'mvn' is not recognized` |
| `python --version` | Microsoft Store alias 提示 |
| `py --version` | `Python 3.12.10` |

本阶段后续验证均显式加载机器 PATH 与用户 PATH，等价于新终端环境。建议重启 Codex 或重新打开 PowerShell。

### 使用工具

| 工具 | 结果 |
| --- | --- |
| Python | `Python 3.12.10` |
| PyMuPDF | 已安装，版本 `1.27.2.3` |
| Pillow | 已安装，版本 `12.2.0` |
| pytesseract | 已安装，版本 `0.3.13` |
| Tesseract OCR | 已安装，版本 `5.4.0.20240606` |

### 新增/修改文件

| 文件 | 变更 |
| --- | --- |
| `.gitignore` | 追加 OCR 缓存、文本和输出目录排除规则 |
| `tools/extract_bpc_pdf_text.py` | 新增 BPC PDF 直接文本抽取与抽样 OCR 诊断脚本 |
| `docs/product/bpc-pdf-readability-report.md` | 新增 PDF 可读性与 OCR 质量报告 |
| `docs/product/bpc-core-concepts.md` | 原为空文件，标记为 DRAFT 占位 |
| `docs/product/bpc-data-manager-actual-import.md` | 原为空文件，标记为 DRAFT 占位 |
| `docs/product/bpc-pain-points-and-design-guardrails.md` | 原为空文件，标记为 DRAFT 占位 |
| `docs/product/bpc-template-input-schedule.md` | 原为空文件，标记为 DRAFT 占位 |
| `PROJECT_STEP_RECORD.md` | 追加 FOUNDATION-002 阶段记录 |

生成但被 `.gitignore` 排除的本地缓存：

1. `docs/source/bpc-ocr-text/`
2. `docs/source/bpc-ocr-output/bpc-pdf-ocr-stats.json`
3. `docs/source/bpc-ocr-cache/`

### OCR 抽样结果

执行命令：

`py tools/extract_bpc_pdf_text.py --sample`

抽样策略：每个 PDF 前 5 页、中间 5 页、最后 5 页，共 15 页。直接文本有效阈值为 500 字符/页，低于阈值判定为疑似扫描页并执行 OCR。

| 序号 | 总页数 | 抽样页数 | 直接文本有效页 | 疑似扫描页 | OCR 成功页 | OCR 失败页 | 抽样字符数 | 建议全量 OCR |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | --- |
| 1 | 379 | 15 | 0 | 15 | 15 | 0 | 13298 | 是 |
| 2 | 231 | 15 | 0 | 15 | 15 | 0 | 18734 | 是 |
| 3 | 267 | 15 | 0 | 15 | 15 | 0 | 17325 | 是 |
| 4 | 290 | 15 | 0 | 15 | 15 | 0 | 16791 | 是 |
| 5 | 230 | 15 | 0 | 15 | 15 | 0 | 16715 | 是 |
| 6 | 343 | 15 | 0 | 15 | 15 | 0 | 17164 | 是 |

结论：6 个 PDF 均属于直接文本不足、OCR 可用的资料形态。

### 关键问题

1. 当前 Codex 进程 PATH 未刷新，普通命令仍不可直接使用完整工具链。
2. 直接文本抽取无法支撑正式知识包，需依赖 OCR。
3. 本阶段仅抽样 90 页，不能代表全量页面质量完全一致。
4. OCR 全文和抽样文本必须保持本地缓存，不得提交到 Git。

### 是否建议全量 OCR

建议进入后续全量 OCR 准备阶段，但不在 FOUNDATION-002 自动执行全量 OCR。

### 是否建议进入 BPC-KB-001

暂不建议直接进入 BPC-KB-001。建议先完成全量 OCR 或章节级 OCR，并建立页码定位与质量抽检机制。

### 是否关闭本阶段

建议关闭 FOUNDATION-002。

关闭理由：OCR 工具链可用，抽样脚本可执行，6 个 PDF 已完成抽样诊断，报告已生成，缓存目录已被 `.gitignore` 排除，业务代码边界未被触碰。

### 下一阶段建议

建议下一阶段进入 FOUNDATION-003：全量 OCR 执行方案与分批缓存生成。该阶段应继续禁止提交 PDF 原文和 OCR 全文，只提交脚本、报告和结构化摘要。

## AUTOPILOT-001

阶段名称：项目自主推进协议建立

记录日期：2026-05-06

### 阶段目标

建立 Codex 自主推进协议，使 Codex 从本阶段起作为项目总控、资料分析负责人、产品经理、总架构师、开发者、测试负责人和源码审查负责人自主推进企业级全面预算管理平台项目。

### 输入资料

1. 用户提供的 AUTOPILOT-001 协议。
2. `AGENTS.md`。
3. `PROJECT_STEP_RECORD.md`。
4. `docs/product/bpc-pdf-readability-report.md`。
5. 当前 Git 状态。

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `AGENTS.md` | 追加自主推进、授权暂停、OCR、BPC-KB、开发前置和阶段输出规则 |
| `PROJECT_STEP_RECORD.md` | 追加 AUTOPILOT-001 阶段记录 |

### 关键决策

1. 接受 AUTOPILOT-001 自主推进协议。
2. 后续不再等待用户逐阶段提供详细提示词；不涉及授权事项时，Codex 应自动推进文档、检查、验证、设计和阶段记录。
3. 涉及 git commit、git push、删除目录、全量 OCR、migration、业务模块、大型依赖、系统 PATH、系统软件安装、外部服务、密钥账号、remote 变更、范围变更时必须暂停等待用户授权。
4. BPC-KB 阶段只写文档，不写业务代码。
5. 完成 BPC-KB-001 至 BPC-KB-009、ARCH-001、PRODUCT-001 前，不进入业务代码开发。

### FOUNDATION-002 状态判断

FOUNDATION-002 已完成并建议关闭。验证结果显示：

1. OCR 工具链可用。
2. 6 个 PDF 的抽样 OCR 均成功。
3. 直接文本不足以支撑正式知识包。
4. 正式进入 BPC-KB-001 前，建议先完成全量 OCR 或章节级 OCR。

### 当前授权需求

根据 AUTOPILOT-001 和 OCR 阶段规则，全量 OCR 属于必须暂停等待用户授权的事项。

建议下一步进入 FOUNDATION-003：全量 OCR 执行方案与分批缓存生成。该阶段若只写方案文档不需要授权；若执行全量 OCR，需要用户明确授权。

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 读取 `AGENTS.md` | 已完成 |
| 读取 `PROJECT_STEP_RECORD.md` | 已完成 |
| 读取相关 `docs/product` | 已读取 `bpc-pdf-readability-report.md` |
| 读取相关 `docs/architecture` | 目录为空，无相关文件 |
| Git 状态 | 已检查，存在未提交文档变更和前序 README 修改 |

### 是否建议关闭本阶段

建议关闭 AUTOPILOT-001。

关闭理由：自主推进协议已被接受并沉淀到治理规则和阶段记录；下一步授权边界已明确。

### 下一阶段建议

下一阶段建议为 FOUNDATION-003：全量 OCR 执行方案与分批缓存生成。

如果用户授权执行全量 OCR，则进入全量 OCR 执行；如果未授权，则先只生成全量 OCR 执行方案与质量抽检方案，不生成 OCR 全文。

### 全自动授权更新

用户随后明确授权：后续除删除文件的步骤外，其他所有步骤都不需要用户授权，由 Codex 自主判断执行。

治理影响：

1. 删除文件或目录仍必须暂停等待明确授权。
2. 全量 OCR、commit、push、安装、阶段推进等可由 Codex 自主判断执行。
3. 仍必须遵守不得提交 PDF 原文、OCR 全文、大型缓存、密钥、构建产物和阶段外业务代码的边界。

## FOUNDATION-003

阶段名称：全量 OCR 执行方案与分批缓存生成

记录日期：2026-05-06

### 阶段目标

在 FOUNDATION-002 抽样验证通过后，执行 6 个 SAP BPC PDF 的全量 OCR，生成本地可复用 OCR 缓存和统计报告，为 BPC-KB-001 之后的知识抽取提供文本基础。本阶段不写业务代码，不修改 `backend/src` 或 `frontend/src`，不新增 migration，不删除文件，不提交 PDF 原文或 OCR 全文。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/source/bpc-pdf` 下 6 个 PDF；`tools/extract_bpc_pdf_text.py`；`docs/product/bpc-pdf-readability-report.md` |
| 允许修改 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`tools/extract_bpc_pdf_text.py`、`docs/product/*` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文 |
| 验证命令 | `py tools/extract_bpc_pdf_text.py --sample --pdf-index 1`、`py tools/extract_bpc_pdf_text.py --full --pdf-index N`、`py tools/extract_bpc_pdf_text.py --full`、`git status --short`、`git check-ignore` |
| 授权状态 | 全自动模式下，除删除文件外无需逐项授权；本阶段未执行删除 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `AGENTS.md` | 更新全自动授权规则，明确仅删除文件/目录需暂停 |
| `tools/extract_bpc_pdf_text.py` | 支持 `--pdf-index` 分批处理、逐页缓存、full/sample 分离输出 |
| `docs/product/bpc-full-ocr-report.md` | 新增全量 OCR 汇总报告 |
| `docs/product/bpc-pdf-readability-report.md` | 追加 FOUNDATION-003 后的全量 OCR 状态 |
| `PROJECT_STEP_RECORD.md` | 追加 FOUNDATION-003 阶段记录 |

生成但被 `.gitignore` 排除的本地缓存：

1. `docs/source/bpc-ocr-cache/`
2. `docs/source/bpc-ocr-text/*.full.txt`
3. `docs/source/bpc-ocr-output/bpc-pdf-ocr-stats.json`

### 执行命令

先验证脚本分批参数：

`py tools/extract_bpc_pdf_text.py --sample --pdf-index 1`

按 PDF 分批执行全量 OCR：

1. `py tools/extract_bpc_pdf_text.py --full --pdf-index 1`
2. `py tools/extract_bpc_pdf_text.py --full --pdf-index 2`
3. `py tools/extract_bpc_pdf_text.py --full --pdf-index 3`
4. `py tools/extract_bpc_pdf_text.py --full --pdf-index 4`
5. `py tools/extract_bpc_pdf_text.py --full --pdf-index 5`
6. `py tools/extract_bpc_pdf_text.py --full --pdf-index 6`

最后执行汇总：

`py tools/extract_bpc_pdf_text.py --full`

### 全量 OCR 结果

| PDF | 页数 | 直接文本有效页 | 疑似扫描页 | OCR 成功页 | OCR 失败页 | 字符数 |
| --- | ---: | ---: | ---: | ---: | ---: | ---: |
| BPC420 | 379 | 0 | 379 | 379 | 0 | 413962 |
| BPC430 | 231 | 0 | 231 | 231 | 0 | 239032 |
| BPC440 | 267 | 0 | 267 | 267 | 0 | 296021 |
| BPC450 | 290 | 3 | 287 | 287 | 0 | 329725 |
| S4F80 | 230 | 0 | 230 | 230 | 0 | 241677 |
| s4f90 | 343 | 0 | 343 | 343 | 0 | 376822 |

汇总：

| 指标 | 结果 |
| --- | ---: |
| 总 PDF 数 | 6 |
| 总页数 | 1740 |
| 直接文本有效页 | 3 |
| 疑似扫描页 | 1737 |
| OCR 成功页 | 1737 |
| OCR 失败页 | 0 |
| 统计字符数 | 1897239 |

### 关键问题

1. PDF 基本均为直接文本不足的扫描型资料，后续知识抽取必须基于 OCR。
2. OCR 全文不可作为正式知识包直接提交，只能作为本地来源材料。
3. BPC-KB 阶段必须标注来源 PDF、页码和 OCR 来源，避免大段复制。
4. 当前 Codex 父进程 PATH 仍需显式加载机器 PATH 与用户 PATH；建议重启 Codex 或终端。

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 分批 OCR | 6 个 PDF 均执行完成 |
| 汇总统计 | 已生成 `docs/source/bpc-ocr-output/bpc-pdf-ocr-stats.json` |
| OCR 文本缓存 | 已生成 `docs/source/bpc-ocr-text/*.full.txt` |
| 逐页缓存 | 已生成 `docs/source/bpc-ocr-cache/` |
| `.gitignore` | PDF、OCR 文本、OCR 输出和 OCR 缓存均被排除 |
| PDF 原文 | 仍保留 6 个，未删除，未提交 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 是否建议进入 BPC-KB-001

建议进入 BPC-KB-001：SAP BPC 核心概念与术语抽取。

理由：全量 OCR 已完成，关键词覆盖模型、维度、成员、层级、模板、报表、Work Status、Data Manager、Script Logic、Business Rule、Consolidation 等主题，具备结构化知识抽取条件。

### 是否建议关闭本阶段

建议关闭 FOUNDATION-003。

关闭理由：全量 OCR 已完成，缓存和统计结果已生成并被 `.gitignore` 保护，阶段报告已生成，业务代码边界未被触碰。

### 下一阶段建议

下一阶段：BPC-KB-001：SAP BPC 核心概念与术语抽取。该阶段只写 `docs/product` 文档，不写业务代码，不提交 PDF 原文或 OCR 全文。

## BPC-KB-001

阶段名称：SAP BPC 核心概念与术语抽取

记录日期：2026-05-06

### 阶段目标

建立 SAP BPC 核心概念与术语表，覆盖 Environment、Model、Dimension、Member、Hierarchy、Category、Version、Input Schedule、Work Status、Data Manager 等基础概念，并输出自研预算平台的命名建议、吸收原则和规避原则。本阶段只写文档，不写业务代码。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-knowledge-extraction-plan.md`、`docs/product/bpc-full-ocr-report.md`、本地 OCR 缓存 |
| 允许修改 | `docs/product/bpc-kb-001-core-terms.md`、`docs/product/bpc-core-concepts.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文、OCR 全文 |
| 验证命令 | OCR 缓存术语页码定位、`git status --short`、`git check-ignore`、禁止范围检查 |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/product/bpc-kb-001-core-terms.md` | 新增 BPC 核心概念与术语表 |
| `docs/product/bpc-core-concepts.md` | 更新为指向正式 BPC-KB-001 文档的 DRAFT 占位 |
| `PROJECT_STEP_RECORD.md` | 追加 BPC-KB-001 阶段记录 |

### 关键产出

1. 建立 BPC 核心术语表，覆盖 20 余个基础术语。
2. 给出自研平台命名建议，如预算空间、预算模型、维度、成员、层级、类别、版本、填报模板、导入任务等。
3. 明确必须吸收的设计原则：模型驱动、维度驱动、成员层级驱动、类别版本分离、模板只是入口、预算与实际同源。
4. 明确必须规避的复杂形态：Excel 插件依赖、黑盒 Data Manager、复杂 Script Logic、复杂 Work Status、多维权限矩阵、过早合并报表、过早 BI/ERP/差异分析。

### 来源定位

通过 OCR 缓存定位术语来源页。示例：

| 术语 | 主要来源 |
| --- | --- |
| Model | BPC420 p19-p22；BPC440 p10-p17；BPC450 p13-p14；s4f90 p20-p21 |
| Dimension | BPC420 p19-p22；BPC430 p16-p21；BPC440 p16-p17 |
| Member | BPC420 p20-p22；BPC430 p5-p21 |
| Hierarchy | BPC420 p20-p22, p56-p57；BPC450 p110-p127 |
| Input Schedule | BPC420 p18, p32, p41, p133, p320；BPC430 p57, p82, p209 |
| Work Status | BPC420 p82-p83, p109；BPC450 p98-p99；S4F80 p146-p147 |
| Data Manager | BPC420 p13, p25-p26, p73, p78；BPC430 p12-p25 |
| Script Logic | BPC420 p115-p121；BPC430 p108；BPC440 p49-p54 |

所有来源均标注为 OCR 来源，后续阶段应继续抽样复核页码。

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 读取治理文件 | 已读取 `AGENTS.md` 和 `PROJECT_STEP_RECORD.md` |
| 读取相关产品文档 | 已读取知识抽取计划和全量 OCR 报告 |
| 术语页码定位 | 已通过 OCR 缓存检索核心术语页码分布 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 失败项与修复记录

无失败项。本阶段未执行删除操作。

### 是否建议关闭本阶段

建议关闭 BPC-KB-001。

关闭理由：核心术语文档已生成，来源页码已标注，自研吸收与规避原则已明确，业务代码边界未被触碰。

### 下一阶段建议

下一阶段：BPC-KB-002：模型、维度、成员、层级知识抽取。

该阶段继续只写文档，不写业务代码，重点抽取元模型边界和自研预算平台的最小元数据闭环。

## BPC-KB-002

阶段名称：模型、维度、成员、层级知识抽取

记录日期：2026-05-06

### 阶段目标

抽取 SAP BPC 中模型、维度、成员、层级的产品思想，形成自研预算平台元数据模型的最小闭环建议。本阶段只写文档，不写业务代码。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-001-core-terms.md`、`docs/product/bpc-knowledge-extraction-plan.md`、本地 OCR 缓存 |
| 允许修改 | `docs/product/bpc-kb-002-model-dimension.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文、OCR 全文 |
| 验证命令 | `git status --short`、`git check-ignore`、禁止范围检查 |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/product/bpc-kb-002-model-dimension.md` | 新增模型、维度、成员、层级知识抽取文档 |
| `PROJECT_STEP_RECORD.md` | 追加 BPC-KB-002 阶段记录 |

### 关键产出

1. 明确自研平台的最小元数据闭环：Budget Workspace、Budget Model、Dimension、Dimension Member、Member Hierarchy、Model Dimension。
2. 明确事实数据粒度建议：模型、科目、组织、期间、类别、版本、自定义维度成员、数值、来源。
3. 明确 Category 与 Version 必须分开，Category 表示数据性质，Version 表示版本或场景。
4. 明确层级汇总应由层级计算产生，不依赖模板手工合计行。
5. 明确 MVP 只支持预算主线模型，不提前引入合并模型和复杂多层级权限。

### 来源定位

| 主题 | 来源 |
| --- | --- |
| Model | BPC420 p19-p22；BPC440 p10-p17；BPC450 p13-p14；S4F80 p11-p19；s4f90 p20-p21，OCR |
| Dimension | BPC420 p19-p22；BPC430 p16-p21；BPC440 p16-p17；BPC450 p31, p42；s4f90 p20-p34，OCR |
| Member | BPC420 p20-p22, p31；BPC430 p5-p21；BPC440 p18, p26-p28；BPC450 p83-p109，OCR |
| Hierarchy | BPC420 p20-p22, p56-p57；BPC430 p47, p67；BPC440 p28, p36-p38；BPC450 p110-p127，OCR |

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 读取治理文件 | 已读取 |
| 读取相关产品文档 | 已读取 BPC-KB-001 与知识抽取计划 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 失败项与修复记录

无失败项。本阶段未执行删除操作。

### 是否建议关闭本阶段

建议关闭 BPC-KB-002。

关闭理由：模型、维度、成员、层级的结构化知识抽取已完成，并已形成元数据模型建议和后续阶段输入。

### 下一阶段建议

下一阶段：BPC-KB-003：模板 / Input Schedule 知识抽取。

该阶段继续只写文档，不写业务代码，重点分析 Web Native 填报模板如何绑定模型、维度、成员和事实数据。

## BPC-KB-003

阶段名称：模板 / Input Schedule 知识抽取

记录日期：2026-05-06

### 阶段目标

抽取 SAP BPC 中模板、Input Schedule、Input Form 与报表输入体验的产品思想，转换为自研 Web Native 预算填报模板设计原则。本阶段只写文档，不写业务代码。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-001-core-terms.md`、`docs/product/bpc-kb-002-model-dimension.md`、本地 OCR 缓存 |
| 允许修改 | `docs/product/bpc-kb-003-input-schedule.md`、`docs/product/bpc-template-input-schedule.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文、OCR 全文 |
| 验证命令 | OCR 缓存术语页码定位、`git status --short`、`git check-ignore`、禁止范围检查 |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/product/bpc-kb-003-input-schedule.md` | 新增模板 / Input Schedule 知识抽取文档 |
| `docs/product/bpc-template-input-schedule.md` | 更新为指向正式 BPC-KB-003 文档的 DRAFT 占位 |
| `PROJECT_STEP_RECORD.md` | 追加 BPC-KB-003 阶段记录 |

### 关键产出

1. 明确 Input Schedule 本质是绑定模型与维度口径的数据输入视图。
2. 明确自研平台必须 Web Native 化，不开发 Excel 插件。
3. 明确模板只保存布局、轴定义、范围、校验和绑定规则，不保存事实数据结构。
4. 提出 Template、Axis Definition、Axis Item、Cell Rule、Template Scope、Data Binding 等模板对象建议。
5. 明确 MVP 模板能力边界：模型绑定、行列配置、筛选条件、数字填报、保存、提交和基础校验。

### 来源定位

| 主题 | 来源 |
| --- | --- |
| Input Schedule | BPC420 p18, p32, p41, p51, p133；BPC430 p57, p82, p209；BPC440 p58, p138, p144；s4f90 p117，OCR |
| Template | BPC420 p41, p44, p48, p51, p73；BPC430 p96-p104, p120；BPC440 p17, p23, p47, p57；S4F80 p180-p185，OCR |
| Workbook / Worksheet | BPC420 p135-p150；BPC430 p16, p20, p22, p33, p43-p49；BPC450 p81-p83，OCR |
| Save Data / Submit | BPC420 p142, p227, p292-p309；BPC450 p85, p146, p152；S4F80 p182-p183, p225，OCR |
| Member Recognition / Local Member | BPC420 p142, p244；BPC430 p43, p55-p56, p73, p94；BPC450 p121-p130，OCR |

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 读取治理文件 | 已读取 |
| 读取相关产品文档 | 已读取 BPC-KB-001 与 BPC-KB-002 |
| 术语页码定位 | 已通过 OCR 缓存检索 Input Schedule、Template、Input Form、Workbook、Worksheet、Save Data 等页码分布 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 失败项与修复记录

无失败项。本阶段未执行删除操作。

### 是否建议关闭本阶段

建议关闭 BPC-KB-003。

关闭理由：模板/Input Schedule 的结构化知识抽取已完成，Web Native 模板取舍和 MVP 边界已明确。

### 下一阶段建议

下一阶段：BPC-KB-004：填报、数据输入、Work Status 知识抽取。

该阶段继续只写文档，不写业务代码，重点分析保存、提交、锁定、退回、状态范围和 Work Status 简化策略。

## BPC-KB-004

阶段名称：填报、数据输入、Work Status 知识抽取

记录日期：2026-05-06

### 阶段目标

抽取 SAP BPC 中数据填报、保存、提交、审批、锁定与 Work Status 的产品思想，转换为自研 Web Native 预算填报状态设计原则。本阶段只写文档，不写业务代码。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-001-core-terms.md`、`docs/product/bpc-kb-003-input-schedule.md`、本地 OCR 缓存 |
| 允许修改 | `docs/product/bpc-kb-004-input-work-status.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文、OCR 全文 |
| 验证命令 | OCR 缓存术语页码定位、`git status --short`、`git check-ignore`、禁止范围检查 |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/product/bpc-kb-004-input-work-status.md` | 新增填报、数据输入、Work Status 知识抽取文档 |
| `PROJECT_STEP_RECORD.md` | 追加 BPC-KB-004 阶段记录 |

### 关键产出

1. 明确 Work Status 的可吸收思想是“按数据区域控制填报生命周期”，而不是直接照搬复杂切片锁定。
2. 明确自研平台的最小填报状态模型：`NOT_STARTED`、`DRAFT`、`SUBMITTED`、`RETURNED`、`APPROVED`、`LOCKED`。
3. 明确保存与提交必须分离：保存是草稿，提交是流程承诺。
4. 明确 MVP 状态范围建议：Template + Entity + Time + Category + Version。
5. 明确填报人、审核人、预算管理员、只读用户的最小权限边界。
6. 明确状态变更必须审计，避免 BPC 黑盒 Work Status 和隐式 Excel 提交体验。

### 来源定位

| 主题 | 来源 |
| --- | --- |
| Work Status | BPC420 p6, p16, p82-p83, p94, p109；BPC450 p98-p99, p228；S4F80 p146-p149；s4f90 p69, p107，OCR |
| Submit / Submitted | BPC420 p292, p295, p300, p302, p347；BPC450 p42, p85, p93, p146, p152；S4F80 p182-p183，OCR |
| Approve / Approval | BPC420 p108-p109, p291, p297, p300；BPC450 p246, p248, p278, p285；S4F80 p170, p183，OCR |
| Reject | BPC420 p292, p300；BPC440 p248, p253, p263；S4F80 p181, p183；s4f90 p258, p262-p263，OCR |
| Lock / Unlock | BPC420 p83, p94, p145-p149；BPC450 p16-p17, p94-p95, p182, p249；S4F80 p122-p123, p146-p147，OCR |
| Owner / Reviewer | BPC420 p16, p32-p33, p300；BPC440 p253-p263；S4F80 p170-p183；s4f90 p257-p267，OCR |
| Data Input / Save Data | BPC420 p133, p140, p142, p227, p296, p309；BPC430 p125；BPC450 p228-p229；S4F80 p78, p155, p192，OCR |

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 读取治理文件 | 已读取 |
| 读取相关产品文档 | 已读取 BPC-KB-001 与 BPC-KB-003 |
| 术语页码定位 | 已通过 OCR 缓存检索 Work Status、Submit、Approve、Reject、Lock、Owner、Reviewer、Save Data 等页码分布 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 失败项与修复记录

无失败项。本阶段未执行删除操作。

### 是否建议关闭本阶段

建议关闭 BPC-KB-004。

关闭理由：填报、数据输入与 Work Status 的结构化知识抽取已完成，状态模型、状态范围、权限最小化和审计要求已形成后续产品与架构输入。

### 下一阶段建议

下一阶段：BPC-KB-005：查询、汇总、报表知识抽取。

该阶段继续只写文档，不写业务代码，重点分析 SAP BPC 查询、报表、汇总与 EPM 报表思想如何转化为 Web Native 基础查询与汇总能力，并避免提前建设复杂 BI。

## BPC-KB-005

阶段名称：查询、汇总、报表知识抽取

记录日期：2026-05-06

### 阶段目标

抽取 SAP BPC 中查询、汇总、报表、EPM Report、Query、Axis、Refresh、Drill、Aggregation 等产品思想，转换为自研 Web Native 预算平台基础查询与汇总设计原则。本阶段只写文档，不写业务代码。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-001-core-terms.md` 至 `docs/product/bpc-kb-004-input-work-status.md`、本地 OCR 缓存 |
| 允许修改 | `docs/product/bpc-kb-005-query-reporting.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文、OCR 全文 |
| 验证命令 | OCR 缓存术语页码定位、`git status --short`、`git check-ignore`、禁止范围检查 |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/product/bpc-kb-005-query-reporting.md` | 新增查询、汇总、报表知识抽取文档 |
| `PROJECT_STEP_RECORD.md` | 追加 BPC-KB-005 阶段记录 |

### 关键产出

1. 明确 BPC 报表与查询的核心是“基于模型、维度、成员、层级和过滤条件读取事实数据，并按行列轴展示汇总结果”。
2. 明确自研平台查询对象建议：Query View、Query Axis、Axis Member Set、Query Filter、Query Measure、Aggregation Rule、Drill Context。
3. 明确报表视图只保存查询定义，不保存事实数据。
4. 明确汇总应来自成员层级和事实数据，不依赖模板或报表中的手工合计行。
5. 明确查询与 BPC-KB-004 填报状态的关系：正式报表默认使用已提交或已通过预算数据，草稿数据只在填报工作台展示。
6. 明确 MVP 不做 BI 图表、Office 插件报表、复杂动态计算、外部 Drill Through 和预算执行差异分析。

### 来源定位

| 主题 | 来源 |
| --- | --- |
| Report / Reporting | BPC420 p11-p15, p51, p74-p77；BPC430 p1, p5-p6, p13, p16, p18-p25, p34；BPC440 p11, p15, p23, p37-p38；BPC450 p6, p17, p32, p44, p47；S4F80 p5, p9, p14, p21，OCR |
| EPM Report | BPC420 p149, p153；BPC430 p5, p22, p43-p44, p61, p90, p107, p116-p117, p125；BPC440 p60；s4f90 p342，OCR |
| Query | BPC420 p86, p118, p123, p282-p285；BPC430 p95, p113, p115-p118, p143-p149；BPC450 p13, p17, p19, p38, p46, p49, p66-p67, p77-p78；S4F80 p71-p72, p79-p81，OCR |
| Page / Row / Column Axis | BPC420 p147-p150；BPC430 p48, p56, p61, p78, p97-p98, p103, p113, p119, p137-p140；BPC440 p61, p64, p66-p67；BPC450 p237，OCR |
| Member Selector | BPC420 p142, p146, p150；BPC430 p43-p44, p51, p53, p63, p65, p71-p72, p75；BPC440 p61, p63, p66-p67，OCR |
| Refresh / Retrieve | BPC420 p124, p142-p143, p154, p157, p258, p261, p285；BPC430 p51, p62, p85, p91, p127, p131, p135, p137, p173, p203，OCR |
| Drill / Drill Through | BPC420 p46, p94, p142, p282, p351, p376；BPC430 p17, p22, p33, p94-p95, p143-p146；S4F80 p16, p52, p119, p124，OCR |
| Aggregation / Aggregate | BPC420 p14, p21-p22, p115, p273, p282；BPC430 p79, p171, p176-p181, p226；BPC450 p29, p31-p32, p85, p150；S4F80 p11, p20, p58, p60，OCR |

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 读取治理文件 | 已读取 |
| 读取相关产品文档 | 已读取 BPC-KB-001 至 BPC-KB-004 |
| 术语页码定位 | 已通过 OCR 缓存检索 Report、EPM Report、Query、Axis、Member Selector、Refresh、Drill、Aggregation 等页码分布 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 失败项与修复记录

无失败项。本阶段未执行删除操作。

### 是否建议关闭本阶段

建议关闭 BPC-KB-005。

关闭理由：查询、汇总、报表的结构化知识抽取已完成，基础 Query View、轴定义、层级汇总、状态口径和 MVP 边界已形成后续产品与架构输入。

### 下一阶段建议

下一阶段：BPC-KB-006：实际数导入 / Data Manager 知识抽取。

该阶段继续只写文档，不写业务代码，重点分析 Data Manager、Transformation、Conversion、Import、Actual 数据导入如何转化为透明、可预览、可审计的 Web 导入能力。

## BPC-KB-006

阶段名称：实际数导入 / Data Manager 知识抽取

记录日期：2026-05-06

### 阶段目标

抽取 SAP BPC 中 Data Manager、Data Package、Transformation、Conversion、Flat File、Import、Actual Data 等产品思想，转换为自研 Web Native 预算平台实际数导入设计原则。本阶段只写文档，不写业务代码。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-002-model-dimension.md`、`docs/product/bpc-kb-004-input-work-status.md`、`docs/product/bpc-kb-005-query-reporting.md`、本地 OCR 缓存 |
| 允许修改 | `docs/product/bpc-kb-006-data-manager-actual-import.md`、`docs/product/bpc-data-manager-actual-import.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文、OCR 全文 |
| 验证命令 | OCR 缓存术语页码定位、`git status --short`、`git check-ignore`、禁止范围检查 |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/product/bpc-kb-006-data-manager-actual-import.md` | 新增实际数导入 / Data Manager 知识抽取文档 |
| `docs/product/bpc-data-manager-actual-import.md` | 从占位草稿更新为指向 BPC-KB-006 的 DRAFT index |
| `PROJECT_STEP_RECORD.md` | 追加 BPC-KB-006 阶段记录 |

### 关键产出

1. 明确 BPC Data Manager 的可吸收思想是导入过程编排，而不是黑盒数据包形态。
2. 明确自研平台导入对象建议：Import Job、Import Batch、Import File、Import Mapping、Conversion Rule、Validation Result、Validation Error、Import Preview、Import Commit、Import Audit Log。
3. 明确导入流程：Upload、Parse、Mapping、Conversion、Validate、Preview、Confirm Commit、Fact Value。
4. 明确 Actual 与 Budget 应进入同源 Fact Value，通过 Category 区分 Actual / Budget / Forecast。
5. 明确 Actual 导入不走填报提交流，但必须有批次状态、期间锁定控制和审计。
6. 明确 MVP 不做 ERP 直连、复杂 Transformation/Conversion 文件语法、预算执行差异分析。

### 来源定位

| 主题 | 来源 |
| --- | --- |
| Data Manager | BPC420 p13, p25-p26, p51, p73, p78, p81, p120, p133, p160-p162；BPC430 p12, p17, p20, p25, p39-p40；BPC440 p10, p50, p53-p54, p58, p75-p76, p82-p83；BPC450 p16，OCR |
| Data Package / Run Package | BPC420 p162-p166, p169, p173, p192-p195, p217, p264；BPC440 p58, p82；s4f90 p132，OCR |
| Transformation / Transformation File | BPC420 p75-p76, p161-p162, p173, p175-p179, p183, p187-p188, p191-p194, p209-p212；BPC440 p75-p79, p83, p159，OCR |
| Conversion / Conversion File | BPC420 p75-p76, p162, p175-p177, p179, p187, p191, p195, p207, p212, p224, p274；BPC440 p75-p79, p83, p153, p159，OCR |
| Flat File / Import | BPC420 p160, p175-p183, p305；BPC440 p75-p83, p111, p256；BPC450 p113-p114, p122, p125, p139-p140, p185-p188；S4F80 p208，OCR |
| Actual / Actual Data | BPC420 p12, p56, p147, p237, p242, p246, p248-p249, p271-p272, p303；BPC450 p49, p66, p170-p171；S4F80 p19, p50, p58-p68, p72, p103，OCR |

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 读取治理文件 | 已读取 |
| 读取相关产品文档 | 已读取 BPC-KB-002、BPC-KB-004、BPC-KB-005 |
| 术语页码定位 | 已通过 OCR 缓存检索 Data Manager、Data Package、Transformation、Conversion、Flat File、Import、Actual、Validation、Audit 等页码分布 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 失败项与修复记录

无失败项。本阶段未执行删除操作。

### 是否建议关闭本阶段

建议关闭 BPC-KB-006。

关闭理由：实际数导入 / Data Manager 的结构化知识抽取已完成，透明导入、映射转换、校验预览、批次审计和同源事实数据原则已形成后续产品与架构输入。

### 下一阶段建议

下一阶段：BPC-KB-007：权限、状态、流程与协作知识抽取。

该阶段继续只写文档，不写业务代码，重点分析 Data Access、Team、Owner、Reviewer、Process、Work Status 与自研最小权限和协作模型的关系。

## BPC-KB-007

阶段名称：权限、状态、流程与协作知识抽取

记录日期：2026-05-06

### 阶段目标

抽取 SAP BPC 中 Data Access、Task Profile、Team、Owner、Reviewer、Work Status、Business Process Flow、Activity、Comment、Audit 等产品思想，转换为自研 Web Native 预算平台最小权限、状态、流程与协作设计原则。本阶段只写文档，不写业务代码。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-002-model-dimension.md`、`docs/product/bpc-kb-004-input-work-status.md`、`docs/product/bpc-kb-006-data-manager-actual-import.md`、本地 OCR 缓存 |
| 允许修改 | `docs/product/bpc-kb-007-access-process-collaboration.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文、OCR 全文 |
| 验证命令 | OCR 缓存术语页码定位、`git status --short`、`git check-ignore`、禁止范围检查 |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/product/bpc-kb-007-access-process-collaboration.md` | 新增权限、状态、流程与协作知识抽取文档 |
| `PROJECT_STEP_RECORD.md` | 追加 BPC-KB-007 阶段记录 |

### 关键产出

1. 明确 BPC 的权限协作体系由功能权限、数据权限、团队、Owner/Reviewer、Work Status 和 Business Process Flow 共同构成。
2. 明确自研平台应分离功能权限与数据权限。
3. 明确 MVP 使用 Role、Permission、Responsibility Scope、Data Scope、Owner Assignment、Reviewer Assignment 和 Access Audit Log。
4. 明确 Owner / Reviewer 是填报协作最小闭环。
5. 明确 Work Status 只作为状态和可编辑性来源，不复刻复杂切片锁定。
6. 明确 BPF 的活动、评论、跟踪思想可吸收，但复杂流程设计器、多级审批和 Teams 后置。

### 来源定位

| 主题 | 来源 |
| --- | --- |
| Data Access / Data Access Profile | BPC420 p39, p77, p90, p93, p97-p100, p102, p276, p309, p333, p345；BPC440 p250, p264；BPC450 p15, p56, p192, p260, p265-p266, p268；s4f90 p17, p97, p342，OCR |
| Task Profile | BPC420 p39, p90, p93-p96, p99-p103, p162-p163；BPC430 p168；BPC440 p139, p250, p264；s4f90 p332，OCR |
| Team / Teams | BPC420 p39, p90, p93, p95-p104, p107, p182, p292；BPC440 p82, p102, p250, p252-p255；BPC450 p24, p35, p219, p246, p250-p251, p261, p265, p283；S4F80 p172, p182, p184，OCR |
| Owner / Reviewer | BPC420 p16, p32-p33, p300, p302, p307, p310-p311, p318；BPC440 p253-p254, p257, p262-p263；BPC450 p246-p250, p253, p277, p282, p284；S4F80 p170-p173, p182-p187；s4f90 p337-p338，OCR |
| Security / Authorization | BPC420 p16-p17, p27, p35, p39, p41, p46, p77, p89-p94, p101, p222, p332, p368；BPC450 p24, p123, p126, p203, p260-p267；s4f90 p330-p333，OCR |
| Business Process Flow / BPF | BPC420 p290, p296, p299-p303, p307, p309；BPC430 p35-p42, p203；BPC440 p246, p251-p264；BPC450 p245, p253, p277-p289；S4F80 p159, p170-p184；s4f90 p330, p334-p342，OCR |

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 读取治理文件 | 已读取 |
| 读取相关产品文档 | 已读取 BPC-KB-002、BPC-KB-004、BPC-KB-006 |
| 术语页码定位 | 已通过 OCR 缓存检索 Data Access、Task Profile、Team、Owner、Reviewer、Security、Authorization、Work Status、BPF、Activity、Comment、Audit 等页码分布 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 失败项与修复记录

无失败项。本阶段未执行删除操作。

### 是否建议关闭本阶段

建议关闭 BPC-KB-007。

关闭理由：权限、状态、流程与协作的结构化知识抽取已完成，最小权限模型、责任范围、Owner/Reviewer 协作、审计事件和 MVP 规避边界已形成后续产品与架构输入。

### 下一阶段建议

下一阶段：BPC-KB-008：用户痛点与自研规避原则。

该阶段继续只写文档，不写业务代码，重点把 BPC 复杂形态归纳为自研平台的设计护栏和阶段边界。

## BPC-KB-008

阶段名称：用户痛点与自研规避原则

记录日期：2026-05-06

### 阶段目标

基于 BPC-KB-001 至 BPC-KB-007 的资料抽取结果，归纳 SAP BPC 对自研 Web Native 全面预算平台的核心痛点、规避原则和产品护栏。本阶段只写文档，不写业务代码。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-001-core-terms.md`、`docs/product/bpc-kb-003-input-schedule.md`、`docs/product/bpc-kb-004-input-work-status.md`、`docs/product/bpc-kb-006-data-manager-actual-import.md`、`docs/product/bpc-kb-007-access-process-collaboration.md` |
| 允许修改 | `docs/product/bpc-kb-008-pain-points-guardrails.md`、`docs/product/bpc-pain-points-and-design-guardrails.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文、OCR 全文 |
| 验证命令 | 文档引用检查、`git status --short`、`git check-ignore`、禁止范围检查 |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/product/bpc-kb-008-pain-points-guardrails.md` | 新增用户痛点与自研规避原则文档 |
| `docs/product/bpc-pain-points-and-design-guardrails.md` | 从占位草稿更新为指向 BPC-KB-008 的 DRAFT index |
| `PROJECT_STEP_RECORD.md` | 追加 BPC-KB-008 阶段记录 |

### 关键产出

1. 明确自研平台必须吸收 BPC 的模型驱动、维度驱动、成员层级、Category/Version、模板、状态、实际与预算同源思想。
2. 明确必须规避 Excel / Office 插件依赖、黑盒 Data Manager、复杂 Script Logic、Work Status 切片锁定、多维权限矩阵。
3. 明确 BI 图表、合并报表、ERP 直连、预算执行差异分析不得提前进入 MVP。
4. 输出 Web Native First、Model First、Fact Value Single Source、Explicit Workflow、Explainable Access、Transparent Import、No Hidden Logic、Stage Discipline 八条正向设计原则。
5. 输出元数据、模板、填报、查询、实际数导入、权限的模块级护栏。

### 来源定位

| 痛点或护栏 | 来源 |
| --- | --- |
| Excel / EPM Add-in 依赖 | BPC-KB-003；BPC420 p135-p150；BPC430 p43-p61, p90, p95, p113-p117，OCR |
| 黑盒 Data Manager | BPC-KB-006；BPC420 p160-p179, p187-p195；BPC440 p75-p83，OCR |
| Transformation / Conversion 复杂语法 | BPC-KB-006；BPC420 p175-p179, p187-p192, p209-p212；BPC440 p75-p79，OCR |
| Script Logic / Dynamic Calculation 复杂化 | BPC-KB-001；BPC420 p115-p121, p282-p285；BPC430 p107-p109；BPC440 p49-p54，OCR |
| Work Status 切片锁定 | BPC-KB-004；BPC420 p82-p83, p290-p296；BPC450 p245-p250；S4F80 p146-p149, p170-p173，OCR |
| Data Access 多维权限矩阵 | BPC-KB-007；BPC420 p90-p104；BPC450 p260-p266；s4f90 p330-p333，OCR |
| BPF 流程复杂化 | BPC-KB-007；BPC420 p299-p309；BPC440 p246-p264；S4F80 p170-p184；s4f90 p334-p342，OCR |
| Actual 与 Budget 同源 | BPC-KB-002、BPC-KB-006；S4F80 p19, p50, p58-p68, p72, p103；BPC450 p49, p66，OCR |

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 读取治理文件 | 已读取 |
| 读取相关产品文档 | 已读取 BPC-KB-001、BPC-KB-003、BPC-KB-004、BPC-KB-006、BPC-KB-007 |
| 文档引用检查 | 已确认 BPC-KB-008 引用了前序知识包和来源页码 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 失败项与修复记录

无失败项。本阶段未执行删除操作。

### 是否建议关闭本阶段

建议关闭 BPC-KB-008。

关闭理由：用户痛点、规避原则、正向设计原则和模块级护栏已形成，能够作为 BPC-KB-009 路线图、ARCH-001 架构基线和 PRODUCT-001 MVP 范围的输入。

### 下一阶段建议

下一阶段：BPC-KB-009：自研预算平台模块路线图。

该阶段继续只写文档，不写业务代码，重点把前序知识包转换为可执行阶段路线图，并明确 MVP 与后置范围。

## BPC-KB-009

阶段名称：自研预算平台模块路线图

记录日期：2026-05-06

### 阶段目标

基于 BPC-KB-001 至 BPC-KB-008，制定自研 Web Native 企业级全面预算管理平台的模块路线图，明确 MVP 主线、后置范围、架构前置决策和产品前置决策。本阶段只写文档，不写业务代码。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-001-core-terms.md` 至 `docs/product/bpc-kb-008-pain-points-guardrails.md` |
| 允许修改 | `docs/product/bpc-kb-009-budget-platform-roadmap.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文、OCR 全文 |
| 验证命令 | 路线图边界检查、`git status --short`、`git check-ignore`、禁止范围检查 |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/product/bpc-kb-009-budget-platform-roadmap.md` | 新增自研预算平台模块路线图 |
| `PROJECT_STEP_RECORD.md` | 追加 BPC-KB-009 阶段记录 |

### 关键产出

1. 明确 MVP 主线闭环：治理基础、元数据模型、预算模型管理、预算模板管理、预算填报、预算查询与基础汇总、实际数导入。
2. 明确后置范围：预算执行差异分析、BI 图表、合并报表、ERP 直连、Excel 插件、通用脚本逻辑、复杂流程设计器和多级审批。
3. 输出 ARCH-001、PRODUCT-001、DEV-000、BUD-001 至 BUD-010 的阶段路线。
4. 明确核心对象演进顺序：Dimension/Member/Hierarchy -> Budget Model -> Input Template -> Submission Task -> Fact Value -> Query View，Import Job 写入 Fact Value。
5. 明确 ARCH-001 和 PRODUCT-001 必须回答的前置决策。
6. 明确不建议立即进入开发，需先完成 ARCH-001 和 PRODUCT-001。

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 读取治理文件 | 已读取 |
| 读取相关产品文档 | 已读取 BPC-KB-001 至 BPC-KB-008 |
| 路线图边界检查 | 已确认路线图未把 BI、合并报表、ERP 直连、预算执行差异分析放入 MVP |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 失败项与修复记录

无失败项。本阶段未执行删除操作。

### 是否建议关闭本阶段

建议关闭 BPC-KB-009。

关闭理由：自研预算平台模块路线图已形成，BPC-KB-001 至 BPC-KB-009 知识抽取阶段闭环完成，后续可以进入 ARCH-001 技术架构基线设计。

### 下一阶段建议

下一阶段：ARCH-001：技术架构基线设计。

该阶段继续只写架构与 ADR 文档，不创建业务代码。重点固定技术栈、分层架构、同源事实数据、动态维度落库、状态权限审计和导入批次设计。

## ARCH-001

阶段名称：技术架构基线设计

记录日期：2026-05-06

### 阶段目标

基于 BPC-KB-001 至 BPC-KB-009 的知识抽取结果，固定 Web Native 全面预算管理平台的技术架构基线、模块边界、同源事实数据原则、状态权限审计边界和导入架构。本阶段只写架构与 ADR 文档，不创建业务代码，不新增 migration。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-001-core-terms.md` 至 `docs/product/bpc-kb-009-budget-platform-roadmap.md` |
| 允许修改 | `docs/architecture/arch-001-technical-baseline.md`、`docs/adr/*.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文、OCR 全文 |
| 验证命令 | `git status --short`、`git check-ignore`、禁止范围检查、架构与 ADR 文件存在性检查 |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/arch-001-technical-baseline.md` | 新增技术架构基线文档 |
| `docs/adr/0001-technology-stack.md` | 新增技术栈基线 ADR |
| `docs/adr/0002-web-native-no-excel-plugin.md` | 新增 Web Native 与不做 Excel 插件 ADR |
| `docs/adr/0003-single-source-fact-value.md` | 新增同源事实数据 ADR |
| `PROJECT_STEP_RECORD.md` | 追加 ARCH-001 阶段记录 |

### 关键产出

1. 固定后端 Java 17 + Spring Boot 3.x + Maven、前端 React + TypeScript + Vite + pnpm、数据库 PostgreSQL 16 的技术基线。
2. 明确 MVP 采用模块化单体优先，不提前拆分微服务。
3. 明确核心领域模块：Governance、Metadata、Budget Model、Template、Submission、Query、Import、Access、Audit。
4. 明确同源事实数据原则：Budget、Actual、Forecast 不拆表，通过 Category / Version 与同一事实层表达。
5. 明确 `fact_value` + `fact_value_axis` 的混合坐标模型方向，核心维度显式列，自定义维度使用轴表。
6. 明确填报状态与导入批次状态分离，状态变更必须审计。
7. 明确 MVP 不包含 Excel 插件、ERP 直连、BI 图表平台、合并报表引擎、通用脚本语言和复杂工作流引擎。

### 关键决策

| 决策 | 结论 |
| --- | --- |
| 技术栈 | Java 17 + Spring Boot 3.x + Maven；React + TypeScript + Vite + pnpm；PostgreSQL 16 |
| 架构形态 | MVP 模块化单体优先 |
| 数据访问 | Spring Data JPA 起步，复杂查询在 BUD-008 前评估 jOOQ 或 SQL Mapper |
| 数据迁移 | Flyway 仅在允许 migration 的开发阶段启用 |
| 模板形态 | Web Native 模板，不做 Excel / Office 插件 |
| 事实数据 | `fact_value` 同源事实层，Budget / Actual / Forecast 不拆表 |

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 读取治理文件 | 已读取 |
| 读取路线图输入 | 已读取 BPC-KB-009，并基于 BPC-KB-001 至 BPC-KB-009 的阶段结论设计 |
| 架构文档 | `docs/architecture/arch-001-technical-baseline.md` 已存在 |
| ADR 文件 | 3 个 ADR 已存在 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 失败项与修复记录

无失败项。本阶段未执行删除操作。

### 未解决问题

1. README 当前仍有历史本地修改，未纳入本阶段提交范围。
2. Docker 暂不可用，DEV-000 或 BUD-001 需要确认本地 PostgreSQL 运行方式。
3. 动态维度最终物理索引策略需在 BUD-002 细化。
4. 导入覆盖、冲销或版本化策略需在 BUD-009 前细化。

### 是否建议关闭本阶段

建议关闭 ARCH-001。

关闭理由：技术栈、分层架构、模块边界、同源事实数据、模板/查询/导入/权限审计基线和关键 ADR 已形成，足以支撑 PRODUCT-001 进行 MVP 产品范围与阶段拆分。

### 下一阶段建议

下一阶段：PRODUCT-001：MVP 产品范围与阶段拆分。

该阶段继续只写产品文档，不写业务代码，重点固定 MVP 用户角色、用户故事、功能范围、默认预算场景、验收标准和阶段拆分。

## PRODUCT-001

阶段名称：MVP 产品范围与阶段拆分

记录日期：2026-05-06

### 阶段目标

基于 BPC-KB-001 至 BPC-KB-009 和 ARCH-001，将 SAP BPC 产品思想和自研规避原则转化为 MVP 产品范围、首个预算场景、用户角色、用户故事、状态流、验收标准和后续开发阶段拆分。本阶段只写产品文档，不创建后端或前端工程，不写业务代码，不新增 migration。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-009-budget-platform-roadmap.md`、`docs/architecture/arch-001-technical-baseline.md` |
| 允许修改 | `docs/product/product-001-mvp-scope.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文、OCR 全文 |
| 验证命令 | `git status --short`、`git check-ignore`、禁止范围检查、产品文档存在性检查 |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/product/product-001-mvp-scope.md` | 新增 MVP 产品范围与阶段拆分文档 |
| `PROJECT_STEP_RECORD.md` | 追加 PRODUCT-001 阶段记录 |

### 关键产出

1. 确定 MVP 首个业务场景为费用预算。
2. 确定 MVP 角色：Budget Admin、Metadata Manager、Template Designer、Budget Owner、Budget Reviewer、Import Operator、Read Only User。
3. 确定强制内置核心维度类型：Account、Entity、Time、Category、Version。
4. 确定模板 MVP 只支持单页 Web 模板、行轴、列轴、筛选和基础校验。
5. 确定填报状态保留 `APPROVED` 与 `LOCKED` 两个状态，不合并。
6. 确定实际数导入 MVP 先支持 CSV，XLSX 后续评估。
7. 确定查询 MVP 支持表格查询、基础汇总、明细和 CSV 导出，不做 BI 图表。
8. 确定 DEV-000、BUD-001 至 BUD-009 的产品范围拆分。

### 验证结果

| 验证项 | 结果 |
| --- | --- |
| 读取治理文件 | 已读取 |
| 读取产品路线图 | 已读取 BPC-KB-009 |
| 读取架构基线 | 已读取 ARCH-001 |
| 产品文档 | `docs/product/product-001-mvp-scope.md` 已存在 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| `backend/src` | 不存在，未修改 |
| `frontend/src` | 不存在，未修改 |
| migration | 未新增 |

### 失败项与修复记录

无失败项。本阶段未执行删除操作。

### 未解决问题

1. README 当前仍有历史本地修改，未纳入本阶段提交范围。
2. Docker 暂不可用，DEV-000 创建工程后如果需要本地数据库，应优先使用已可用的本地 PostgreSQL 或延后容器化。
3. XLSX 导入不进入 MVP 首轮验收，BUD-009 后续可评估。

### 是否建议关闭本阶段

建议关闭 PRODUCT-001。

关闭理由：MVP 场景、角色、功能边界、状态流、验收标准和开发阶段拆分已确定，满足进入 DEV-000 创建基础工程的前置条件。

### 下一阶段建议

下一阶段：DEV-000：创建后端 / 前端基础工程。

该阶段允许创建正式后端和前端基础工程，但仍不进入预算业务模块实现。重点是 Spring Boot、React Vite、基础测试命令、工程目录和 README 后续整理策略。

## DEV-000

阶段名称：创建后端 / 前端基础工程

记录日期：2026-05-06

### 阶段目标

创建正式 Spring Boot 后端基础工程和 React/Vite 前端基础工程，建立可运行的最小工程壳、依赖锁定、基础测试命令和构建忽略规则。本阶段不实现预算业务模块，不新增 migration，不创建数据库表，不提交 PDF 或 OCR 全文。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/architecture/arch-001-technical-baseline.md`、`docs/product/product-001-mvp-scope.md` |
| 允许修改 | `backend`、`frontend`、`.gitignore`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 预算业务模块、migration、PDF 原文、OCR 全文、临时入口页 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git status --short`、`git check-ignore` |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `.gitignore` | 增加后端 `target`、前端 `node_modules`、`dist`、Vite/TypeScript 构建副产物忽略规则 |
| `backend/pom.xml` | 新增 Spring Boot 3.5.10 + Java 17 + Maven 基础工程配置 |
| `backend/src/main/java/com/budgetplatform/BudgetPlatformApplication.java` | 新增 Spring Boot 启动类 |
| `backend/src/main/resources/application.yml` | 新增基础应用名和端口配置 |
| `backend/src/test/java/com/budgetplatform/BudgetPlatformApplicationTests.java` | 新增 Spring 上下文加载测试 |
| `frontend/package.json` | 新增 React/Vite/TypeScript/pnpm 工程脚本和依赖 |
| `frontend/pnpm-lock.yaml` | 新增前端依赖锁文件 |
| `frontend/index.html` | 新增正式 Vite 入口 |
| `frontend/tsconfig.json` | 新增前端 TypeScript 配置 |
| `frontend/tsconfig.node.json` | 新增 Vite 配置 TypeScript 配置 |
| `frontend/vite.config.ts` | 新增 Vite 配置 |
| `frontend/eslint.config.js` | 新增 ESLint flat config |
| `frontend/src/main.tsx` | 新增 React 挂载入口 |
| `frontend/src/App.tsx` | 新增正式工程壳组件 |
| `frontend/src/styles.css` | 新增基础样式 |
| `PROJECT_STEP_RECORD.md` | 追加 DEV-000 阶段记录 |

### 关键产出

1. 后端基础工程可通过 `mvn test`。
2. 前端基础工程可通过 `pnpm type-check`、`pnpm lint`、`pnpm build`。
3. `.gitignore` 已排除后端和前端构建产物，同时继续保护 PDF 与 OCR 缓存。
4. 前端只包含正式 React/Vite 工程壳，不是临时 HTML 入口。
5. 后端只包含应用启动类和上下文测试，不包含预算业务 API。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 1, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm install` | 通过；生成 `frontend/pnpm-lock.yaml` |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 第一次因构建副产物 `vite.config.d.ts` 被 ESLint 解析失败；修复后通过 |
| `pnpm build` | 第一次因缺少 Node 类型和 Node TS 配置不足失败；修复后通过 |
| `git check-ignore` | PDF、OCR、`backend/target`、`frontend/node_modules`、`frontend/dist`、Vite 构建副产物均被忽略 |

### 失败项与修复记录

1. `pnpm build` 初次失败，真实错误包括 `Cannot find type definition file for 'node'`、`Cannot find name 'Set'`、`Cannot find name 'Buffer'`。定位为 Vite 配置侧缺少 `@types/node` 且 `tsconfig.node.json` 未显式声明 `target/lib/types`。修复：新增 `@types/node`，更新 `tsconfig.node.json`。
2. `pnpm lint` 初次失败，真实错误为 `vite.config.d.ts` 不在 `parserOptions.project` 范围内。定位为 `tsc -b` 生成声明副产物。修复：构建脚本改为 `tsc --noEmit && vite build`，并将相关副产物加入 `.gitignore` 与 ESLint ignore。
3. 本阶段未删除任何生成文件，只通过忽略规则防止副产物进入 Git。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 预算业务模块 | 未新增 |
| migration | 未新增 |
| 数据库表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| 临时沙箱配置 | 未新增 |
| README | 仍为历史本地修改，未纳入本阶段提交范围 |

### 未解决问题

1. `frontend/vite.config.js`、`frontend/vite.config.d.ts` 和 TypeScript build info 是本地构建副产物，已被忽略但未删除，后续如需清理需用户明确授权。
2. Docker 暂不可用，后续数据库相关阶段优先使用本地 PostgreSQL 或另立容器化修复阶段。
3. README 历史本地修改仍需专门阶段处理。

### 是否建议关闭本阶段

建议关闭 DEV-000。

关闭理由：后端和前端基础工程已创建并通过基线验证，未进入预算业务模块，后续可以进入 BUD-001 项目治理与基础框架。

### 下一阶段建议

下一阶段：BUD-001：项目治理与基础框架。

该阶段可以在已创建工程内建立统一错误响应、基础异常结构、基础审计接口和工程规范，但仍不得进入元数据、预算模型、模板、填报、查询或导入业务模块。

## BUD-001

阶段名称：项目治理与基础框架

记录日期：2026-05-06

### 阶段目标

在 DEV-000 创建的正式工程内建立横切基础框架，包括统一 API 响应、统一异常响应、基础错误码、健康检查、基础审计接口和前端 API 类型封装。本阶段不进入元数据、预算模型、模板、填报、查询或导入业务模块，不新增 migration。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/architecture/arch-001-technical-baseline.md`、`docs/product/product-001-mvp-scope.md` |
| 允许修改 | `backend/src/main/java/com/budgetplatform/common`、`backend/src/test/java/com/budgetplatform/common`、`frontend/src/shared`、`docs/architecture/bud-001-framework-baseline.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 元数据、预算模型、模板、填报、查询、导入业务模块、migration、PDF 原文、OCR 全文 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git status --short`、`git check-ignore` |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/common/api/*` | 新增统一响应、错误结构、错误码、应用异常和全局异常处理 |
| `backend/src/main/java/com/budgetplatform/common/audit/*` | 新增基础审计事件、动作、服务接口和 Noop 实现 |
| `backend/src/main/java/com/budgetplatform/common/web/HealthController.java` | 新增 `GET /api/health` 健康检查 |
| `backend/src/test/java/com/budgetplatform/common/api/GlobalExceptionHandlerTests.java` | 新增统一异常响应测试 |
| `backend/src/test/java/com/budgetplatform/common/web/HealthControllerTests.java` | 新增健康检查测试 |
| `frontend/src/shared/api/types.ts` | 新增前端统一 API 类型 |
| `frontend/src/shared/api/http.ts` | 新增前端基础 JSON 请求封装 |
| `docs/architecture/bud-001-framework-baseline.md` | 新增基础框架说明 |
| `PROJECT_STEP_RECORD.md` | 追加 BUD-001 阶段记录 |

### 关键产出

1. 后端统一响应结构：`ApiResponse<T>`、`ApiError`。
2. 后端统一异常结构：`ApplicationException`、`ErrorCode`、`GlobalExceptionHandler`。
3. 后端基础健康检查：`GET /api/health`。
4. 后端基础审计接口：`AuditEvent`、`AuditAction`、`AuditService`、`NoopAuditService`。
5. 前端共享 API 类型和请求封装：`ApiResponse`、`ApiError`、`requestJson`。
6. 阶段架构说明已沉淀到 `docs/architecture/bud-001-framework-baseline.md`。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 修复后通过；Tests run: 3, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过 |

### 失败项与修复记录

1. `mvn test` 第一次失败：`No ParameterResolver registered for parameter MockMvc`。定位为 `@WebMvcTest` 测试类构造器注入未被当前测试上下文解析。修复：改用 `@Autowired` 字段注入。
2. `mvn test` 第二次失败：`/test/fail` 请求被 `ResourceHttpRequestHandler` 处理，返回 `NoResourceFoundException` 并被统一异常处理成 500。定位为嵌套测试 Controller 未被 `@WebMvcTest` 正确注册。修复：异常处理测试改为 `MockMvcBuilders.standaloneSetup(...).setControllerAdvice(...)`。
3. 修复后后端测试全部通过。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 元数据业务模块 | 未新增 |
| 预算模型业务模块 | 未新增 |
| 模板业务模块 | 未新增 |
| 填报业务模块 | 未新增 |
| 查询业务模块 | 未新增 |
| 导入业务模块 | 未新增 |
| migration | 未新增 |
| 数据库表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| README | 仍为历史本地修改，未纳入本阶段提交范围 |

### 未解决问题

1. 审计持久化未实现，需在后续审计存储或具体业务阶段进入。
2. 认证与授权上下文未实现，需在权限基础阶段进入。
3. README 历史本地修改仍需专门阶段处理。

### 是否建议关闭本阶段

建议关闭 BUD-001。

关闭理由：项目基础框架、统一响应、统一错误、健康检查、审计接口和前端 API 基础类型已建立并通过验证，后续可以进入 BUD-002 元数据模型设计。

### 下一阶段建议

下一阶段：BUD-002：元数据模型设计。

该阶段应先输出维度、成员、层级、Category、Version、预算模型、同源事实数据和权限范围的逻辑模型与物理模型设计，不直接新增 migration，除非阶段明确允许。

## BUD-002

阶段名称：元数据模型设计

记录日期：2026-05-06

### 阶段目标

设计全面预算平台 MVP 的元数据逻辑模型和物理模型候选，覆盖预算空间、预算模型、维度、成员、单主层级、Category、Version、同源事实数据坐标和权限范围预留。本阶段只写架构和 ADR 文档，不创建 migration，不新增数据库表，不实现后端 API 或前端页面。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-002-model-dimension.md`、`docs/product/product-001-mvp-scope.md`、`docs/architecture/arch-001-technical-baseline.md` |
| 允许修改 | `docs/architecture/bud-002-metadata-model-design.md`、`docs/adr/0004-dynamic-dimension-physical-model.md`、`docs/adr/0005-single-primary-hierarchy.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | migration、后端元数据业务 API、前端元数据页面、PDF 原文、OCR 全文 |
| 验证命令 | 文档存在性检查、`git status --short`、`git check-ignore`、migration 检查、现有测试 |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/bud-002-metadata-model-design.md` | 新增元数据模型设计文档 |
| `docs/adr/0004-dynamic-dimension-physical-model.md` | 新增动态维度物理模型 ADR |
| `docs/adr/0005-single-primary-hierarchy.md` | 新增单主层级 MVP ADR |
| `PROJECT_STEP_RECORD.md` | 追加 BUD-002 阶段记录 |

### 关键产出

1. 确定元数据逻辑模型：`budget_workspace`、`dimension`、`dimension_member`、`budget_model`、`model_dimension`、`fact_value`、`fact_value_axis`。
2. 确定 MVP 强制内置维度类型：Account、Entity、Time、Category、Version。
3. 确定动态维度采用混合坐标模型：核心维度显式列，自定义维度轴表。
4. 确定成员层级 MVP 只支持单主父子层级。
5. 确定事实数据不按模型动态建表，Budget / Actual / Forecast 不拆表。
6. 输出校验规则、索引候选和权限范围预留。

### 测试与验证结果

| 验证项 | 结果 |
| --- | --- |
| 文档存在性 | 3 个设计/ADR 文件已存在 |
| migration | 未新增 |
| 后端业务 API | 未新增 |
| 前端业务页面 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |

### 失败项与修复记录

无失败项。本阶段未执行删除操作。

### 未解决问题

1. BUD-003 进入实现前，需要明确是否允许新增 migration。
2. 历史层级版本、多层级和多父节点能力后置。
3. 导入覆盖、冲销或版本化策略仍需在 BUD-009 前决策。

### 是否建议关闭本阶段

建议关闭 BUD-002。

关闭理由：元数据逻辑模型、动态维度物理模型取舍、单主层级约束、索引候选和权限范围预留已形成，足以支撑 BUD-003 元数据后端实现。

### 下一阶段建议

下一阶段：BUD-003：元数据后端。

该阶段将首次进入元数据业务后端实现。根据当前全自动授权，除删除文件外无需额外授权；但必须在阶段计划中明确 migration 范围、测试命令和不进入前端/模板/填报/查询/导入模块。

## BUD-003

阶段名称：元数据后端

记录日期：2026-05-07

### 阶段目标

按 BUD-002 设计实现元数据后端最小闭环，包括预算空间、维度、成员和单主层级 API，新增 JPA/Flyway 数据访问基础和集成测试。本阶段明确允许新增 migration，但不实现预算模型、模板、填报、查询、导入或前端元数据页面。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/architecture/bud-002-metadata-model-design.md`、`docs/product/product-001-mvp-scope.md` |
| 允许修改 | `backend/pom.xml`、`backend/src/main/java/com/budgetplatform/metadata`、`backend/src/main/resources/db/migration`、`backend/src/test`、`docs/architecture/bud-003-metadata-backend.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 前端元数据页面、预算模型、模板、填报、查询、导入、PDF 原文、OCR 全文、删除接口 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git status --short`、`git check-ignore` |
| 授权状态 | 全自动模式，不涉及删除文件；本阶段明确允许新增 migration |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/pom.xml` | 新增 Spring Data JPA、Flyway、PostgreSQL runtime driver、H2 test dependency |
| `backend/src/main/resources/application.yml` | 新增 datasource、JPA validate、Flyway 配置 |
| `backend/src/test/resources/application-test.yml` | 新增 H2 PostgreSQL 模式测试配置 |
| `backend/src/main/resources/db/migration/V1__metadata_baseline.sql` | 新增 workspace、dimension、dimension_member 表和索引 |
| `backend/src/main/java/com/budgetplatform/metadata/domain/*` | 新增元数据实体和枚举 |
| `backend/src/main/java/com/budgetplatform/metadata/repository/*` | 新增 JPA Repository |
| `backend/src/main/java/com/budgetplatform/metadata/service/MetadataService.java` | 新增元数据应用服务和校验 |
| `backend/src/main/java/com/budgetplatform/metadata/api/*` | 新增元数据 REST API DTO 和 Controller |
| `backend/src/test/java/com/budgetplatform/metadata/api/MetadataControllerIntegrationTests.java` | 新增元数据 API 集成测试 |
| `backend/src/test/java/com/budgetplatform/BudgetPlatformApplicationTests.java` | 加入 `test` profile |
| `docs/architecture/bud-003-metadata-backend.md` | 新增本阶段实现说明 |
| `PROJECT_STEP_RECORD.md` | 追加 BUD-003 阶段记录 |

### 关键产出

1. 元数据后端 API 最小闭环：Workspace、Dimension、Dimension Member。
2. Flyway V1 migration：`budget_workspace`、`dimension`、`dimension_member`。
3. 单主层级基础规则：同维 parent、循环校验、leaf 状态维护。
4. 统一错误响应接入：重复编码返回 `CONFLICT`，跨维 parent 返回 `BAD_REQUEST`。
5. H2 PostgreSQL 模式集成测试验证 migration 和 API。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 修复后通过；Tests run: 6, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过 |
| `git check-ignore` | PDF、OCR、`backend/target`、`frontend/node_modules`、`frontend/dist` 仍被忽略 |

### 失败项与修复记录

1. 第一次 `mvn test` 编译失败，真实错误为 `from(...)` 方法在 `WorkspaceResponse`、`DimensionResponse`、`DimensionMemberResponse` 中不是 public，`MetadataService` 无法从不同包访问。修复：将三个 DTO 工厂方法改为 `public static`。
2. 源码审查发现成员移动父节点时旧父节点 `leaf` 状态可能不刷新。修复：新增 `refreshLeafState`，移动后重算旧父节点 leaf 状态。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 前端元数据页面 | 未新增 |
| 预算模型模块 | 未新增 |
| 模板模块 | 未新增 |
| 填报模块 | 未新增 |
| 查询模块 | 未新增 |
| 导入模块 | 未新增 |
| 删除接口 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| README | 仍为历史本地修改，未纳入本阶段提交范围 |

### 未解决问题

1. Actual / Budget / Forecast 同源事实表尚未实现，按路线后置。
2. Budget Model 绑定维度后端尚未实现，按路线进入 BUD-005。
3. 元数据前端页面尚未实现，建议 BUD-004 进入。
4. 认证与授权上下文尚未实现，仍为后续权限基础阶段事项。

### 是否建议关闭本阶段

建议关闭 BUD-003。

关闭理由：元数据后端最小闭环、migration、集成测试和错误校验已完成，且未越界进入其他业务模块。

### 下一阶段建议

下一阶段：BUD-004：元数据前端。

该阶段应只实现元数据管理基础 UI，调用 BUD-003 API，支持 Workspace/Dimension/Member 的基础查看与创建，不进入预算模型、模板、填报、查询或导入。

## BUD-004

阶段名称：元数据前端

记录日期：2026-05-07

### 阶段目标

实现元数据管理基础前端，支持 Workspace、Dimension、Dimension Member 的查看、创建和选择，调用 BUD-003 后端 API。本阶段不进入预算模型、模板、填报、查询或导入，不新增 migration。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/architecture/bud-003-metadata-backend.md` |
| 允许修改 | `frontend/src`、`frontend/vite.config.ts`、`docs/architecture/bud-004-metadata-frontend.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 后端业务扩展、migration、预算模型、模板、填报、查询、导入、PDF 原文、OCR 全文 |
| 验证命令 | `pnpm type-check`、`pnpm lint`、`pnpm build`、`mvn test`、`git status --short`、`git check-ignore` |
| 授权状态 | 全自动模式，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `frontend/vite.config.ts` | 增加 `/api` 到 `localhost:8080` 的开发代理 |
| `frontend/src/features/metadata/metadataApi.ts` | 新增元数据 API 类型和调用函数 |
| `frontend/src/App.tsx` | 将工程占位页替换为元数据管理工作台 |
| `frontend/src/styles.css` | 新增工作台、表单、列表和表格样式 |
| `docs/architecture/bud-004-metadata-frontend.md` | 新增本阶段实现说明 |
| `PROJECT_STEP_RECORD.md` | 追加 BUD-004 阶段记录 |

### 关键产出

1. Workspace 创建、列表和选择。
2. Dimension 创建、列表、类型选择和 workspace 关联。
3. Dimension Member 创建、列表和 parent 选择。
4. 统一元数据 API client。
5. Vite proxy 连接后端 API。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `pnpm type-check` | 修复后通过 |
| `pnpm lint` | 修复后通过 |
| `pnpm build` | 修复后通过 |
| `mvn test` | 通过；Tests run: 6, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm dev --host 127.0.0.1` | 前端可启动，`127.0.0.1:5173` 返回 200 |
| `mvn spring-boot:run -Dspring-boot.run.profiles=test` | 本地后端预览失败；仍连接 PostgreSQL，报 `SQL State 28P01`，用户 `budget_platform` 密码认证失败 |
| `mvn spring-boot:run -Dspring-boot.run.profiles=test -Dspring-boot.run.useTestClasspath=true` | 本地后端预览仍失败；同样落到 PostgreSQL 密码认证失败 |

### 失败项与修复记录

1. 第一次前端验证失败，真实错误为 `src/App.tsx` 末尾残留旧占位页 JSX，导致 `TS1128 Declaration or statement expected` 和 ESLint parsing error。修复：清除残留 JSX 和重复 `export default App`。
2. 修复后前端 type-check、lint、build 全部通过。
3. 本地后端预览启动失败，真实错误为 Flyway 获取 PostgreSQL 连接失败：`SQL State 28P01`，本机用户 `budget_platform` 密码认证失败。`mvn test` 仍通过，说明测试 profile 下的 H2 集成验证有效；后续应补充正式的 local profile 或配置本机 PostgreSQL 凭据。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 后端业务扩展 | 未新增 |
| migration | 未新增 |
| 预算模型页面 | 未新增 |
| 模板页面 | 未新增 |
| 填报页面 | 未新增 |
| 查询页面 | 未新增 |
| 导入页面 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| README | 仍为历史本地修改，未纳入本阶段提交范围 |

### 未解决问题

1. UI 当前只支持创建和查看，不支持编辑、停用和删除。
2. 本地前端预览可启动；本地后端 `spring-boot:run` 预览当前因 PostgreSQL 凭据失败未启动，需后续补充 local profile 或配置本机 PostgreSQL。
3. 认证与授权上下文尚未实现。

### 是否建议关闭本阶段

建议关闭 BUD-004。

关闭理由：元数据前端基础工作台已完成并通过构建验证，能够调用 BUD-003 API 完成元数据基础维护。

### 下一阶段建议

下一阶段：BUD-005：预算模型管理。

该阶段应基于元数据能力实现预算模型创建、维度绑定和启停，不进入模板、填报、查询或导入。

## BUD-005

阶段名称：预算模型管理

记录日期：2026-05-07

### 阶段目标

基于元数据能力实现预算模型管理 MVP，支持模型创建、查询、维度绑定、激活和停用。本阶段只做预算模型管理，不进入模板、填报、查询、实际数导入、BI、ERP 直连或预算执行差异分析。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/architecture/bud-002-metadata-model-design.md`、`docs/architecture/bud-003-metadata-backend.md`、`docs/architecture/bud-004-metadata-frontend.md`、`docs/product/product-001-mvp-scope.md` |
| 允许修改 | `backend/src/main/java/com/budgetplatform/budgetmodel`、`backend/src/test/java/com/budgetplatform/budgetmodel`、`backend/src/main/resources/db/migration/V2__budget_model_baseline.sql`、`frontend/src`、`docs/architecture/bud-005-budget-model-management.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 模板、填报、查询、导入、预算执行分析、BI、ERP、合并报表、PDF 原文、OCR 全文、删除接口 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git status --short`、`git check-ignore` |
| 授权状态 | 全自动模式；本阶段新增 migration 和业务模块已按授权记录风险，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/resources/db/migration/V2__budget_model_baseline.sql` | 新增预算模型和模型维度绑定表 |
| `backend/src/main/java/com/budgetplatform/budgetmodel/domain/*` | 新增预算模型实体、绑定实体和模型状态枚举 |
| `backend/src/main/java/com/budgetplatform/budgetmodel/repository/*` | 新增预算模型仓储和绑定仓储 |
| `backend/src/main/java/com/budgetplatform/budgetmodel/api/*` | 新增预算模型 API DTO 和 Controller |
| `backend/src/main/java/com/budgetplatform/budgetmodel/service/BudgetModelService.java` | 新增预算模型业务规则 |
| `backend/src/test/java/com/budgetplatform/budgetmodel/api/BudgetModelControllerIntegrationTests.java` | 新增预算模型集成测试 |
| `frontend/src/features/budgetModels/budgetModelApi.ts` | 新增预算模型前端 API client |
| `frontend/src/App.tsx` | 新增预算模型创建、选择、绑定、激活和停用 UI |
| `frontend/src/styles.css` | 新增预算模型管理区样式 |
| `docs/architecture/bud-005-budget-model-management.md` | 新增本阶段架构说明 |
| `PROJECT_STEP_RECORD.md` | 追加 BUD-005 阶段记录 |

### 关键产出

1. 预算模型以 workspace 为边界，code 在 workspace 内唯一。
2. 模型可绑定已有 Dimension，且维度必须来自同一 workspace。
3. 激活模型前校验 Account、Entity、Time、Category、Version 五类必需维度。
4. 前端支持预算模型创建、选择、维度绑定、激活和停用。
5. 不提供删除和解绑，先保持治理安全边界。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 10, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过 |

### 失败项与修复记录

1. 本阶段首轮后端和前端验证均通过，未出现需要修复的编译或测试失败。

### 风险与记录

1. 本阶段新增了 migration `V2__budget_model_baseline.sql`，属于 BUD-005 范围内的预算模型基础表变更，已按全自动授权记录。
2. 本阶段新增预算模型业务模块，但只包含模型治理最小闭环，未进入模板、填报、查询或导入。
3. 激活规则目前采用五类必需维度固定校验，后续如支持更多模型类型，需要在 BUD-005 后续增强或独立阶段扩展。
4. 当前仍不支持维度解绑、模型编辑、模型删除和复杂权限。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 模板模块 | 未新增 |
| 填报模块 | 未新增 |
| 查询模块 | 未新增 |
| 实际数导入 | 未新增 |
| 预算执行分析 | 未新增 |
| BI 图表 | 未新增 |
| ERP 直连 | 未新增 |
| 合并报表 | 未新增 |
| 删除接口 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| README | 仍为历史本地修改，未纳入本阶段提交范围 |

### 未解决问题

1. Budget Model 暂不支持编辑、删除、复制和维度解绑。
2. 模型激活后尚未锁定结构，后续模板阶段前需要明确变更策略。
3. 本地后端 `spring-boot:run` 预览仍依赖 PostgreSQL 凭据或后续 local profile。
4. 认证与授权上下文尚未实现。

### 是否建议关闭本阶段

建议关闭 BUD-005。

关闭理由：预算模型创建、维度绑定、激活校验、停用、前端基础管理和集成测试已完成，且未越界进入模板、填报、查询或导入。

### 下一阶段建议

下一阶段：BUD-006：预算模板管理。

该阶段应基于已绑定维度的预算模型，设计模板基础结构和模板字段布局，不进入填报执行、查询汇总或实际数导入。

## BUD-006

阶段名称：预算模板管理

记录日期：2026-05-07

### 阶段目标

实现预算模板管理 MVP，支持为已激活预算模型创建单页 Web 模板、配置 ROW/COLUMN/FILTER 轴、激活和停用模板。本阶段只做模板定义，不写填报数据、不新增事实数据写入、不进入查询汇总或实际数导入。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-003-input-schedule.md`、`docs/product/product-001-mvp-scope.md`、`docs/architecture/bud-005-budget-model-management.md` |
| 允许修改 | `backend/src/main/java/com/budgetplatform/budgettemplate`、`backend/src/test/java/com/budgetplatform/budgettemplate`、`backend/src/main/resources/db/migration/V3__budget_template_baseline.sql`、`frontend/src`、`docs/architecture/bud-006-budget-template-management.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 填报执行、事实数据写入、查询、导入、预算执行分析、BI、ERP、合并报表、PDF 原文、OCR 全文、删除接口 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git status --short`、`git check-ignore` |
| 授权状态 | 全自动模式；本阶段新增 migration 和模板模块已按授权记录风险，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/resources/db/migration/V3__budget_template_baseline.sql` | 新增预算模板和模板轴表 |
| `backend/src/main/java/com/budgetplatform/budgettemplate/domain/*` | 新增模板实体、模板轴实体、模板状态和轴类型枚举 |
| `backend/src/main/java/com/budgetplatform/budgettemplate/repository/*` | 新增模板仓储和模板轴仓储 |
| `backend/src/main/java/com/budgetplatform/budgettemplate/api/*` | 新增模板 API DTO 和 Controller |
| `backend/src/main/java/com/budgetplatform/budgettemplate/service/BudgetTemplateService.java` | 新增模板业务规则 |
| `backend/src/test/java/com/budgetplatform/budgettemplate/api/BudgetTemplateControllerIntegrationTests.java` | 新增模板集成测试 |
| `frontend/src/features/budgetTemplates/budgetTemplateApi.ts` | 新增模板前端 API client |
| `frontend/src/App.tsx` | 新增模板创建、选择、轴配置、激活和停用 UI |
| `frontend/src/styles.css` | 新增模板管理区样式 |
| `docs/architecture/bud-006-budget-template-management.md` | 新增本阶段架构说明 |
| `PROJECT_STEP_RECORD.md` | 追加 BUD-006 阶段记录 |

### 关键产出

1. 模板只能绑定 `ACTIVE` 预算模型。
2. 模板轴只能引用当前模型已绑定维度。
3. 同一模板不能重复使用同一模型维度绑定。
4. 模板激活前必须至少配置一个 ROW 轴和一个 COLUMN 轴。
5. 模板只保存布局配置，不保存事实数据或填报值。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 14, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过 |

### 失败项与修复记录

1. 本阶段首轮后端和前端验证均通过，未出现需要修复的编译或测试失败。

### 风险与记录

1. 本阶段新增了 migration `V3__budget_template_baseline.sql`，属于 BUD-006 范围内的模板基础表变更，已按全自动授权记录。
2. 本阶段新增预算模板业务模块，但只包含模板定义和轴配置，不写事实数据。
3. `member_selector` 当前是轻量集合语义，尚未展开到具体成员集合或成员选择器。
4. 激活规则只校验 ROW/COLUMN 轴存在，后续填报阶段需校验坐标唯一性和可编辑规则。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 填报任务 | 未新增 |
| 事实数据写入 | 未新增 |
| 查询模块 | 未新增 |
| 实际数导入 | 未新增 |
| 预算执行分析 | 未新增 |
| BI 图表 | 未新增 |
| ERP 直连 | 未新增 |
| 合并报表 | 未新增 |
| 删除接口 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| README | 仍为历史本地修改，未纳入本阶段提交范围 |

### 未解决问题

1. 模板暂不支持编辑、删除、复制、多 sheet 和复杂公式。
2. 模板暂不支持具体成员选择，只支持集合选择器。
3. 模板激活后尚未锁定结构，填报阶段需明确模板变更策略。
4. 认证与授权上下文尚未实现。

### 是否建议关闭本阶段

建议关闭 BUD-006。

关闭理由：预算模板创建、轴配置、激活校验、停用、前端基础管理和集成测试已完成，且未越界进入填报、事实数据、查询或导入。

### 下一阶段建议

下一阶段：BUD-007：预算填报基础版。

该阶段应基于已激活模板实现填报任务、草稿、提交、退回、通过和锁定，并首次引入统一事实数据写入；仍不得进入预算执行差异分析、BI、ERP 或实际数导入。

## BUD-007

阶段名称：预算填报基础版

记录日期：2026-05-07

### 阶段目标

基于已激活模板实现填报任务、草稿保存、提交、退回、通过和锁定，并首次引入统一事实数据写入。本阶段不进入查询汇总、实际数导入、BI、ERP、合并报表或预算执行差异分析。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-004-input-work-status.md`、`docs/product/product-001-mvp-scope.md`、`docs/architecture/bud-006-budget-template-management.md` |
| 允许修改 | `backend/src/main/java/com/budgetplatform/budgetsubmission`、`backend/src/test/java/com/budgetplatform/budgetsubmission`、`backend/src/main/resources/db/migration/V4__budget_submission_baseline.sql`、`frontend/src`、`docs/architecture/bud-007-budget-submission-baseline.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 查询汇总、实际数导入、预算执行分析、BI、ERP、合并报表、PDF 原文、OCR 全文、删除接口 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git status --short`、`git check-ignore` |
| 授权状态 | 全自动模式；本阶段新增 migration、填报模块和事实数据写入已按授权记录风险，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/resources/db/migration/V4__budget_submission_baseline.sql` | 新增填报任务和事实值表 |
| `backend/src/main/java/com/budgetplatform/budgetsubmission/domain/*` | 新增填报任务、事实值、状态和来源枚举 |
| `backend/src/main/java/com/budgetplatform/budgetsubmission/repository/*` | 新增填报任务和事实值仓储 |
| `backend/src/main/java/com/budgetplatform/budgetsubmission/api/*` | 新增填报 API DTO 和 Controller |
| `backend/src/main/java/com/budgetplatform/budgetsubmission/service/SubmissionService.java` | 新增填报状态机和事实值写入规则 |
| `backend/src/test/java/com/budgetplatform/budgetsubmission/api/SubmissionControllerIntegrationTests.java` | 新增填报集成测试 |
| `frontend/src/features/submissions/submissionApi.ts` | 新增填报前端 API client |
| `frontend/src/App.tsx` | 新增填报任务、草稿值、提交、退回、通过、锁定 UI |
| `frontend/src/styles.css` | 新增填报管理区样式 |
| `docs/architecture/bud-007-budget-submission-baseline.md` | 新增本阶段架构说明 |
| `PROJECT_STEP_RECORD.md` | 追加 BUD-007 阶段记录 |

### 关键产出

1. 填报任务范围固定为 Template + Entity + Time + Category + Version。
2. 事实值坐标固定为 Account + Entity + Time + Category + Version。
3. 支持 NOT_STARTED、DRAFT、SUBMITTED、RETURNED、APPROVED、LOCKED 状态流。
4. 保存值时写入统一 `fact_value`，后续 Actual 导入可复用事实表思想。
5. 已提交、已通过、已锁定状态不可继续保存填报值。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 18, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过 |

### 失败项与修复记录

1. 本阶段首轮后端和前端验证均通过，未出现需要修复的编译或测试失败。

### 风险与记录

1. 本阶段新增了 migration `V4__budget_submission_baseline.sql`，属于 BUD-007 范围内的填报和事实值基础表变更，已按全自动授权记录。
2. 本阶段首次新增事实数据写入，但仅用于预算填报草稿和状态推进，不实现查询、导入或差异分析。
3. 事实值当前仅覆盖费用预算核心五维：Account、Entity、Time、Category、Version；自定义维度事实坐标后置。
4. 审计仍接入 NoopAuditService，持久化审计存储后续独立阶段增强。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 查询模块 | 未新增 |
| 实际数导入 | 未新增 |
| 预算执行分析 | 未新增 |
| BI 图表 | 未新增 |
| ERP 直连 | 未新增 |
| 合并报表 | 未新增 |
| 删除接口 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| README | 仍为历史本地修改，未纳入本阶段提交范围 |

### 未解决问题

1. 填报 UI 暂不根据模板轴自动生成完整表格，只提供任务和 Account 金额录入基础闭环。
2. 暂不支持解锁、重开、复杂审批、多用户鉴权和持久化审计日志。
3. 暂不支持自定义维度事实坐标。
4. 查询与基础汇总尚未实现，需进入 BUD-008。

### 是否建议关闭本阶段

建议关闭 BUD-007。

关闭理由：填报任务、草稿值、提交、退回、通过、锁定和事实值写入已形成最小闭环，并通过后端和前端验证，未越界进入查询、导入或差异分析。

### 下一阶段建议

下一阶段：BUD-008：预算查询与基础汇总。

该阶段应基于 `fact_value` 实现表格查询、基础明细和轻量汇总，不做 BI 图表、不做预算执行差异分析。

## BUD-008

阶段名称：预算查询与基础汇总

记录日期：2026-05-07

### 阶段目标

基于 BUD-007 的 `fact_value` 统一事实表，实现预算明细只读查询、基础维度汇总和 CSV 文本导出。本阶段不新增 migration，不实现实际数导入、预算与实际差异分析、BI 图表、ERP 直连或合并报表。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/product-001-mvp-scope.md`、`docs/architecture/bud-007-budget-submission-baseline.md`、现有 `fact_value` |
| 允许修改 | `backend/src/main/java/com/budgetplatform/budgetquery`、`backend/src/test/java/com/budgetplatform/budgetquery`、必要的 `FactValue` 只读 getter、`frontend/src/features/budgetQuery`、`frontend/src/App.tsx`、`frontend/src/styles.css`、`docs/architecture/bud-008-budget-query-baseline.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 新增 migration、实际数导入、预算执行差异分析、BI 图表、ERP 直连、合并报表、PDF 原文、OCR 全文、删除接口 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git status --short`、`git check-ignore` |
| 授权状态 | 全自动模式；本阶段不涉及删除文件，不新增 migration |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/budgetsubmission/domain/FactValue.java` | 增加预算模型、模板、任务范围成员的只读 getter，供查询投影使用 |
| `backend/src/main/java/com/budgetplatform/budgetsubmission/repository/FactValueRepository.java` | 增加按预算模型读取事实值的查询方法 |
| `backend/src/main/java/com/budgetplatform/budgetquery/api/*` | 新增查询分组枚举、明细响应、汇总响应和 Controller |
| `backend/src/main/java/com/budgetplatform/budgetquery/service/BudgetQueryService.java` | 新增明细查询、基础汇总和 CSV 文本导出服务 |
| `backend/src/test/java/com/budgetplatform/budgetquery/api/BudgetQueryControllerIntegrationTests.java` | 新增预算查询集成测试 |
| `frontend/src/features/budgetQuery/budgetQueryApi.ts` | 新增预算查询前端 API client |
| `frontend/src/App.tsx` | 新增查询过滤、明细表、汇总表和 CSV 预览 UI |
| `frontend/src/styles.css` | 新增查询区域样式 |
| `docs/architecture/bud-008-budget-query-baseline.md` | 新增本阶段架构说明 |
| `PROJECT_STEP_RECORD.md` | 追加 BUD-008 阶段记录 |

### 关键产出

1. 查询入口固定为 Budget Model，保持模型驱动。
2. 明细查询读取统一 `fact_value`，当前支持 Account、Entity、Time、Category、Version 坐标。
3. 基础汇总支持按 Account、Entity、Time、Category、Version 求和。
4. CSV 导出仅为轻量文本核对能力，不引入报表引擎。
5. 查询模块为只读能力，不改变填报状态和事实数据。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 20, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过 |

### 失败项与修复记录

1. 本阶段前端首轮 `type-check`、`lint`、`build` 均通过。
2. 后端查询模块首轮 `mvn test` 已通过；实现过程中移除了未使用的异常辅助方法和 import，保持服务层简洁。

### 风险与记录

1. 查询当前按预算模型读取后在服务层过滤，适合 MVP；大数据量需下推数据库条件和分页。
2. 汇总当前是基础金额求和，不含层级递归、币种、单位、账户符号翻转或公式。
3. CSV 是轻量导出，不是正式报表包。
4. 当前没有鉴权上下文，后续权限阶段需补齐数据访问边界。
5. `README.md` 仍是历史本地修改，未纳入本阶段提交范围。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 新增 migration | 未新增 |
| 实际数导入 | 未新增 |
| 预算执行分析 | 未新增 |
| BI 图表 | 未新增 |
| ERP 直连 | 未新增 |
| 合并报表 | 未新增 |
| 删除接口 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| README | 仍为历史本地修改，未纳入本阶段提交范围 |

### 未解决问题

1. 查询暂不支持分页、排序配置、保存查询视图和下载文件。
2. 暂不支持层级汇总、自定义维度坐标和权限过滤。
3. 暂不支持 Actual 来源事实，因此没有预算与实际同源查询。

### 是否建议关闭本阶段

建议关闭 BUD-008。

关闭理由：预算事实明细查询、基础汇总、CSV 文本导出、前端查询工作台和集成测试已完成，且未越界进入导入、BI 或差异分析。

### 下一阶段建议

下一阶段：BUD-009：实际数导入。

该阶段应在统一事实表思想上引入 Actual 来源的手工 CSV 导入或轻量文件导入基线，保持导入透明、可校验、可回溯；仍不得进入 ERP 直连、复杂 Data Manager 黑盒或预算执行差异分析。

## BUD-009

阶段名称：实际数导入

记录日期：2026-05-07

### 阶段目标

实现 Actual CSV 导入最小闭环：固定表头 CSV 校验、批次记录、行级错误、显式提交和写入同源 `fact_value`。本阶段不进入 ERP 直连、复杂 Data Manager 黑盒、预算执行差异分析、BI 图表或合并报表。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/product/bpc-kb-006-data-manager-actual-import.md`、`docs/product/product-001-mvp-scope.md`、`docs/architecture/bud-008-budget-query-baseline.md` |
| 允许修改 | `backend/src/main/java/com/budgetplatform/budgetactual`、`backend/src/test/java/com/budgetplatform/budgetactual`、`backend/src/main/resources/db/migration/V5__actual_import_baseline.sql`、必要的 `FactValue`/查询响应扩展、`frontend/src/features/actualImports`、`frontend/src/App.tsx`、`frontend/src/styles.css`、`docs/architecture/bud-009-actual-import-baseline.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | ERP 直连、预算执行差异分析、BI 图表、合并报表、PDF 原文、OCR 全文、删除接口 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git status --short`、`git check-ignore` |
| 授权状态 | 全自动模式；本阶段新增 migration 和实际数导入模块已按授权记录风险，不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/resources/db/migration/V5__actual_import_baseline.sql` | 新增实际数导入批次、行级结果表，并扩展 `fact_value` 支持 `import_batch_id` |
| `backend/src/main/java/com/budgetplatform/budgetactual/domain/*` | 新增导入批次、导入行、批次状态和行状态 |
| `backend/src/main/java/com/budgetplatform/budgetactual/repository/*` | 新增导入批次和行级结果仓储 |
| `backend/src/main/java/com/budgetplatform/budgetactual/api/*` | 新增导入校验、提交、批次查询和行查询 API |
| `backend/src/main/java/com/budgetplatform/budgetactual/service/ActualImportService.java` | 新增 CSV 解析、成员校验、重复坐标校验、提交写入事实表 |
| `backend/src/main/java/com/budgetplatform/budgetsubmission/domain/FactValue.java` | 支持 Actual 导入事实来源和可空填报任务/模板 |
| `backend/src/main/java/com/budgetplatform/budgetsubmission/domain/FactSourceType.java` | 新增 `ACTUAL_IMPORT` |
| `backend/src/main/java/com/budgetplatform/budgetquery/api/FactQueryResponse.java` | 查询响应新增 `importBatchId` 并兼容 Actual 来源 |
| `backend/src/test/java/com/budgetplatform/budgetactual/api/ActualImportControllerIntegrationTests.java` | 新增实际数导入集成测试 |
| `frontend/src/features/actualImports/actualImportApi.ts` | 新增实际数导入前端 API client |
| `frontend/src/features/budgetQuery/budgetQueryApi.ts` | 查询行 source type 兼容 `ACTUAL_IMPORT` |
| `frontend/src/App.tsx` | 新增 Actual CSV Import UI 和提交后查询刷新 |
| `frontend/src/styles.css` | 新增导入区域样式 |
| `docs/architecture/bud-009-actual-import-baseline.md` | 新增本阶段架构说明 |
| `PROJECT_STEP_RECORD.md` | 追加 BUD-009 阶段记录 |

### 关键产出

1. Actual 导入以 Budget Model 为入口，要求模型已激活。
2. CSV 固定表头为 `account,entity,time,category,version,amount`。
3. 导入先生成批次和行级校验结果，错误行不会写入事实表。
4. 无错误批次提交后写入 `fact_value`，`source_type=ACTUAL_IMPORT`，`value_status=APPROVED`。
5. 查询模块可读取 Actual 来源事实，但不做预算与实际差异计算。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 22, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过 |

### 失败项与修复记录

1. 首轮 `mvn test` 失败：`ActualImportControllerIntegrationTests` 使用贪婪正则提取校验响应中的 `id`，误取到行级结果 id，导致 commit 请求使用 row id 并返回 404。
2. 真实错误：`Actual import batch was not found: <rowId>`。
3. 修复方式：测试中改为非贪婪匹配，确保提取响应首个 batch id。
4. 修复后再次执行 `mvn test` 通过。

### 风险与记录

1. 本阶段新增 migration `V5__actual_import_baseline.sql`，属于 BUD-009 范围内的导入批次、行级结果和事实表同源扩展。
2. 固定表头 CSV 暂不支持可视化字段映射和外部编码转换表。
3. Actual 分类强制要求 Category 成员编码为 `ACTUAL`。
4. 提交后暂不支持撤销、冲销、覆盖或重复批次治理。
5. 权限、期间锁定、导入审批和持久化审计增强后置。
6. `README.md` 仍是历史本地修改，未纳入本阶段提交范围。

### 越界检查

| 项 | 结果 |
| --- | --- |
| ERP 直连 | 未新增 |
| 预算执行分析 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| 复杂 Data Manager 包 | 未新增 |
| 删除接口 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| README | 仍为历史本地修改，未纳入本阶段提交范围 |

### 未解决问题

1. 导入暂不支持映射模板、转换规则维护、错误下载和文件上传存储。
2. 暂不支持撤销/冲销策略。
3. 暂不支持权限、锁定期间和组织范围校验。
4. 预算与实际差异分析仍未进入，需用户明确批准 BUD-010 才能开发。

### 是否建议关闭本阶段

建议关闭 BUD-009。

关闭理由：Actual CSV 校验、批次与行级结果、错误阻断、提交写入同源事实表、前端导入工作台和集成测试均已完成，且未越界进入 ERP、BI 或差异分析。

### 下一阶段建议

下一阶段：BUD-010：预算与实际差异分析。

根据项目规则，BUD-010 只有在用户明确批准后才能进入。当前应暂停在 BUD-009 收口状态，不自动开发差异分析。

## REVIEW-001

阶段名称：MVP 已实现模块源码审查与治理收口

记录日期：2026-05-07

### 阶段目标

对 BUD-001 至 BUD-009 已实现的 MVP 主线进行源码审查、边界扫描、测试复核和治理收口。本阶段不进入 BUD-010 预算与实际差异分析，不新增业务模块，不删除文件。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、BUD-001 至 BUD-009 架构文档、当前源码、当前 Git 状态 |
| 允许修改 | 当前已实现模块的小范围缺陷修复、对应测试、`docs/architecture/review-001-mvp-source-review.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | BUD-010 差异分析、ERP 直连、BI 图表、合并报表、删除文件、PDF 原文、OCR 全文 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git status --short`、`git check-ignore`、边界关键词扫描 |
| 授权状态 | 全自动模式；本阶段不涉及删除文件；未进入 BUD-010 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/budgetactual/service/ActualImportService.java` | 修复多个 CUSTOM 维度导致 Actual 导入维度映射重复 key 的风险 |
| `backend/src/test/java/com/budgetplatform/budgetactual/api/ActualImportControllerIntegrationTests.java` | 新增多个 CUSTOM 维度下 Actual CSV 校验测试 |
| `backend/src/main/java/com/budgetplatform/budgetsubmission/service/SubmissionService.java` | 修复填报退回后事实值状态未同步退回的问题 |
| `backend/src/test/java/com/budgetplatform/budgetsubmission/api/SubmissionControllerIntegrationTests.java` | 增强退回后事实值状态验证 |
| `docs/architecture/review-001-mvp-source-review.md` | 新增 MVP 源码审查与治理收口报告 |
| `PROJECT_STEP_RECORD.md` | 追加 REVIEW-001 阶段记录 |

### 关键产出

1. Actual 导入现在可兼容模型中存在多个 `CUSTOM` 维度的情况。
2. 填报任务退回后，关联事实值状态同步回到 `DRAFT`，避免污染按 `SUBMITTED` 状态的查询结果。
3. 完成删除接口、ERP、BI、合并报表、差异分析、临时入口、PDF/OCR 和构建产物边界扫描。
4. 已沉淀源码审查报告。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 23, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过 |

### 失败项与修复记录

1. 本阶段未出现测试失败。
2. 主动修复审查发现 1：Actual 导入维度映射忽略本阶段不消费的 `CUSTOM` 维度，并新增测试。
3. 主动修复审查发现 2：填报退回同步事实值为 `DRAFT`，并新增测试断言。

### 风险与记录

1. 本阶段未新增 migration。
2. 本阶段未新增业务模块，只对既有模块做一致性硬化。
3. `README.md` 仍是历史本地修改，未纳入本阶段提交范围。
4. BUD-010 差异分析仍需用户明确批准后才能进入。

### 越界检查

| 项 | 结果 |
| --- | --- |
| BUD-010 差异分析 | 未进入 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| 删除文件 | 未执行 |
| 删除接口 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交，仅本地 ignored 缓存 |
| 构建产物 | 未提交 |

### 未解决问题

1. 认证、授权、数据范围和持久化审计仍未实现。
2. 查询分页、数据库条件下推、模板版本锁定、Actual 撤销/冲销仍需后续阶段规划。
3. BUD-010 预算与实际差异分析仍未获明确批准。

### 是否建议关闭本阶段

建议关闭 REVIEW-001。

关闭理由：源码审查、两处小缺陷修复、后端测试、边界扫描和审查报告均已完成，未进入 BUD-010 或越界功能。

### 下一阶段建议

下一阶段仍是 BUD-010：预算与实际差异分析，但必须用户明确批准后才能进入。

## DOCS-README-001

阶段名称：README 项目入口文档收口

记录日期：2026-05-07

### 阶段目标

将当前空白的 `README.md` 更新为正式项目入口文档，说明项目定位、已实现 MVP 能力、技术栈、目录结构、验证命令、PDF/OCR 资料保护规则和当前下一步边界。本阶段不进入 BUD-010，不修改业务代码，不删除文件。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`docs/architecture/*`、当前 `README.md` 与 Git 状态 |
| 允许修改 | `README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 后端业务代码、前端业务代码、migration、BUD-010 差异分析、PDF 原文、OCR 全文、删除文件 |
| 验证命令 | `git status --short`、`git check-ignore`、必要时运行 `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build` |
| 授权状态 | 全自动模式；本阶段不涉及删除文件，不进入受控业务阶段 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `README.md` | 新增项目定位、MVP 能力、技术栈、目录结构、验证命令、PDF/OCR 保护和下一步边界 |
| `PROJECT_STEP_RECORD.md` | 追加 DOCS-README-001 阶段记录 |

### 关键产出

1. README 从空白状态恢复为正式项目入口文档。
2. README 明确 BUD-001 至 BUD-009 与 REVIEW-001 的已完成能力。
3. README 明确 BUD-010 仍需明确批准后才能进入。
4. README 明确 PDF/OCR 原文保护规则。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `git status --short` | 通过；仅 `README.md`、`PROJECT_STEP_RECORD.md` 为本阶段修改 |
| `git check-ignore` | 通过；PDF、OCR 缓存、前端构建产物、依赖目录和后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `mvn test` | 通过；Tests run: 23, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过 |

### 失败项与修复记录

1. 本阶段尚未出现失败项。

### 风险与记录

1. 本阶段只更新文档，不新增业务能力。
2. README 包含正式验证命令，不写入临时沙箱路径、临时预览 URL 或一次性调试入口。
3. 未提交 PDF 原文、OCR 全文或构建产物。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 后端业务代码 | 未修改 |
| 前端业务代码 | 未修改 |
| migration | 未修改 |
| BUD-010 差异分析 | 未进入 |
| 删除文件 | 未执行 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. README 仍可在后续阶段补充部署配置、环境变量和正式启动说明。
2. BUD-010 仍需用户明确批准后才能进入。

### 是否建议关闭本阶段

建议关闭 DOCS-README-001。

关闭理由：README 项目入口文档已收口，阶段记录已更新，后端与前端验证命令均通过，未修改业务代码、migration、PDF 原文、OCR 全文或 BUD-010 差异分析内容。

### 下一阶段建议

下一业务阶段仍是 BUD-010：预算与实际差异分析。该阶段在项目规则中属于明确受控范围，需用户明确批准“进入 BUD-010”后再开始业务实现。

## RELEASE-001

阶段名称：MVP 发布候选治理检查

记录日期：2026-05-08

### 阶段目标

对 BUD-001 至 BUD-009 已完成的预算平台 MVP 做发布候选治理检查，明确当前可交付能力、不能宣称完成的能力、测试矩阵、边界扫描、发布风险和下一阶段选择。本阶段不进入 BUD-010，不修改业务代码，不新增 migration，不删除文件。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`README.md`、`docs/product/product-001-mvp-scope.md`、`docs/architecture/*`、当前源码与 Git 状态 |
| 允许修改 | `docs/architecture/release-001-mvp-readiness.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、BUD-010 差异分析实现、PDF 原文、OCR 全文、删除文件 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git check-ignore`、`git status --short`、`git diff --check` |
| 授权状态 | 全自动模式；本阶段不涉及删除文件，不进入 BUD-010 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/release-001-mvp-readiness.md` | 新增 MVP 发布候选治理检查文档 |
| `PROJECT_STEP_RECORD.md` | 追加 RELEASE-001 阶段记录 |

### 关键产出

1. 明确当前代码可作为“预算平台 MVP 内部技术验证候选版本”。
2. 明确不能宣称生产可用，原因包括认证授权、持久化审计、分页性能、模板版本和导入撤销仍未实现。
3. 明确 BUD-010 预算与实际差异分析未进入，不能提前实现或宣传为已完成。
4. 补充后续非越界阶段建议：`SEC-001`、`AUDIT-001`、`PERF-001`、`E2E-001`。

### 工具诊断

| 工具 | 结果 |
| --- | --- |
| `rg` | 失败；命中 Codex 内嵌 `rg.exe` 并返回“拒绝访问” |
| PowerShell `Get-ChildItem` / `Select-String` | 已作为替代方案完成文件与边界扫描 |

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 23, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git status --short` | 通过；仅 `PROJECT_STEP_RECORD.md` 与 `docs/architecture/release-001-mvp-readiness.md` 为本阶段修改 |
| `git diff --check` | 通过；仅出现 Git 对 `PROJECT_STEP_RECORD.md` 的 LF/CRLF 换行提示，无空白错误 |

### 失败项与修复记录

1. `rg` 无法运行，真实错误为“拒绝访问”；本阶段改用 PowerShell 原生命令完成扫描。
2. 本阶段尚未出现文档生成失败或测试失败。

### 风险与记录

1. 本阶段只新增治理文档，不新增业务能力。
2. 本阶段未修改 `backend/src`、`frontend/src` 或 migration。
3. 本阶段未删除文件。
4. 本阶段未提交 PDF 原文、OCR 全文、构建产物或依赖目录。
5. BUD-010 仍需用户明确批准后才能进入。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 后端业务代码 | 未修改 |
| 前端业务代码 | 未修改 |
| migration | 未修改 |
| BUD-010 差异分析 | 未进入 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| 删除文件 | 未执行 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 认证、授权、数据范围和持久化审计仍未实现。
2. 查询分页、数据库条件下推、模板版本、Actual 撤销/冲销仍需后续阶段。
3. `rg` PATH 或可执行权限问题建议后续单独修复。
4. BUD-010 预算与实际差异分析仍未获明确批准。

### 是否建议关闭本阶段

建议关闭 RELEASE-001。

关闭理由：MVP 发布候选治理检查文档已沉淀，后端测试、前端类型检查、lint、build、Git 忽略保护和差异检查均通过，未修改业务代码、migration、PDF 原文、OCR 全文或 BUD-010 业务实现。

### 下一阶段建议

用户已在 2026-05-08 明确批准 BUD-010，下一阶段进入 `BUD-010`：预算与实际差异分析。

## BUD-010

阶段名称：预算与实际差异分析基线

记录日期：2026-05-08

### 阶段目标

在用户明确批准 BUD-010 后，实现最小版 Budget vs Actual 差异分析。本阶段复用 BUD-008 查询模块和 BUD-009 Actual 同源事实数据，不新增数据库表，不引入 BI 图表、ERP 直连、合并报表或复杂权限矩阵。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、BUD-008 查询文档、BUD-009 实际数导入文档、`fact_value` 同源事实表、现有前后端工作台 |
| 允许修改 | `budgetquery` 后端 API/服务/测试、前端 `budgetQuery` API 与工作台展示、`docs/architecture/bud-010-budget-actual-variance.md`、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | PDF 原文、OCR 全文、删除文件、ERP 直连、BI 图表、合并报表、复杂权限矩阵 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git check-ignore`、`git status --short`、`git diff --check` |
| 授权状态 | 用户已明确批准 BUD-010；全自动模式执行 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/budgetquery/api/BudgetActualVarianceResponse.java` | 新增 Budget vs Actual 差异响应结构 |
| `backend/src/main/java/com/budgetplatform/budgetquery/api/BudgetQueryController.java` | 新增 `/api/budget-query/variance` 只读接口 |
| `backend/src/main/java/com/budgetplatform/budgetquery/service/BudgetQueryService.java` | 新增按 Account + Entity + Time 聚合预算与实际并计算差异的服务逻辑 |
| `backend/src/test/java/com/budgetplatform/budgetquery/api/BudgetQueryControllerIntegrationTests.java` | 新增预算事实 + Actual 导入事实的差异分析集成测试 |
| `frontend/src/features/budgetQuery/budgetQueryApi.ts` | 新增差异分析 API 类型与调用函数，并修正事实查询 nullable id 类型 |
| `frontend/src/App.tsx` | 新增 Budget vs Actual 表格分析区域 |
| `frontend/src/styles.css` | 新增差异分析表单布局样式 |
| `docs/architecture/bud-010-budget-actual-variance.md` | 新增 BUD-010 架构与关闭建议文档 |
| `README.md` | 更新当前 MVP 能力，标记 BUD-010 已进入并完成基线 |
| `PROJECT_STEP_RECORD.md` | 追加 BUD-010 阶段记录 |

### 关键产出

1. 新增 `/api/budget-query/variance`，支持显式选择预算 Category 与实际 Category。
2. 支持预算 Version、实际 Version、Entity、Time 和状态过滤。
3. 默认不传状态时仅纳入 `APPROVED` 与 `LOCKED` 事实，避免草稿和提交中数据污染差异结果。
4. 差异结果按 Account + Entity + Time 聚合，输出预算金额、实际金额、差异额、差异率和两侧行数。
5. 前端新增表格型 Budget vs Actual 区域，不引入图表或 BI 组件。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 24, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过 |
| `git check-ignore` | 通过；PDF、OCR、前端构建产物、依赖目录和后端 `target` 均被忽略 |
| `git status --short` | 通过；仅 BUD-010 相关源码、文档和阶段记录修改 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| 边界关键词扫描 | 通过；`backend/src` 与 `frontend/src` 未发现 `@DeleteMapping`、ERP、Chart 或合并报表实现 |

### 失败项与修复记录

1. 本阶段后端测试、前端类型检查、lint 和 build 首轮均通过。
2. 未发生需要二次修复的测试失败。

### 风险与限制

1. 当前差异分析聚合粒度固定为 Account + Entity + Time。
2. 差异率以预算金额为分母，预算为 0 时返回 `null`。
3. 当前仍不做图表、趋势、钻取、看板、ERP 直连或合并报表。
4. 认证、授权、数据范围、持久化审计、查询分页和数据库条件下推仍需后续独立阶段。
5. 本阶段未新增 migration，继续复用现有 `fact_value`。

### 越界检查

| 项 | 结果 |
| --- | --- |
| BUD-010 明确批准 | 已批准 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| 复杂权限矩阵 | 未新增 |
| migration | 未新增 |
| 删除文件 | 未执行 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 差异分析尚未支持用户自定义分组、层级递归汇总或阈值标记。
2. 尚未支持差异钻取到预算任务与 Actual 导入批次的组合视图。
3. 查询分页、索引和权限过滤仍需后续阶段。

### 是否建议关闭本阶段

建议关闭 BUD-010。

关闭理由：预算与实际差异分析后端接口、服务逻辑、前端表格入口、集成测试和架构文档均已完成，所有验证命令通过，未新增 migration、ERP、BI 图表、合并报表、删除文件、PDF 原文或 OCR 全文。

### 下一阶段建议

建议下一阶段进入 `SEC-001`：认证、角色与数据范围设计，避免继续堆业务功能而缺少企业级治理底座。

## SEC-001

阶段名称：认证、角色与数据范围设计

记录日期：2026-05-08

### 阶段目标

定义预算平台的认证、角色、操作权限和 Entity 数据范围模型，为后续企业级治理实现建立清晰边界。本阶段只做架构和 ADR 文档，不修改后端或前端业务代码，不新增 migration，不删除文件。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`README.md`、`docs/product/product-001-mvp-scope.md`、BUD-001 至 BUD-010 架构文档、当前源码安全散点 |
| 允许修改 | `docs/architecture/sec-001-security-scope-design.md`、`docs/adr/0006-security-roles-and-entity-scope.md`、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | `backend/src`、`frontend/src`、migration、PDF 原文、OCR 全文、删除文件、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `git status --short`、`git diff --check`、`git check-ignore` |
| 授权状态 | 全自动模式；本阶段不涉及删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/sec-001-security-scope-design.md` | 新增认证、角色、操作权限、Entity 数据范围、API 和后续阶段设计 |
| `docs/adr/0006-security-roles-and-entity-scope.md` | 新增安全模型 ADR |
| `README.md` | 更新当前治理推进状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-001 阶段记录 |

### 关键产出

1. 明确采用“角色 + Entity 数据范围 + 流程责任人”的安全模型。
2. 定义 `BUDGET_ADMIN`、`METADATA_MANAGER`、`TEMPLATE_DESIGNER`、`BUDGET_OWNER`、`BUDGET_REVIEWER`、`IMPORT_OPERATOR`、`READ_ONLY` 七类角色。
3. 明确 MVP 不建设 Account、Time、Category、Version 组合式复杂权限矩阵。
4. 明确后续 `SEC-002`、`SEC-003`、`SEC-004`、`AUDIT-001`、`AUTH-001` 阶段拆分。
5. 明确 SEC-002 可先使用轻量请求头身份上下文，仅用于内部技术验证；生产登录、JWT 或 SSO 后置到 `AUTH-001`。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `git status --short` | 通过；仅 SEC-001 文档、ADR、README 和阶段记录修改 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |

### 失败项与修复记录

1. 本阶段尚未出现失败项。

### 风险与限制

1. SEC-001 只完成设计，不代表接口已经受保护。
2. 请求头身份上下文只能作为 SEC-002 内部技术验证方案，不可宣称生产认证完成。
3. 真正的权限过滤需要 SEC-003 接入业务接口。
4. 持久化审计仍需 `AUDIT-001`。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 后端业务代码 | 未修改 |
| 前端业务代码 | 未修改 |
| migration | 未新增 |
| 删除文件 | 未执行 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 用户、角色、Entity 范围表尚未实现。
2. 业务接口尚未接入授权判断。
3. 前端安全管理 UI 尚未实现。
4. 登录、JWT、SSO 和密码策略尚未实现。

### 是否建议关闭本阶段

建议关闭 SEC-001。

关闭理由：安全模型、角色边界、Entity 数据范围、后续阶段拆分和 ADR 已完成沉淀；本阶段未修改业务代码、migration、PDF 原文、OCR 全文或任何禁止范围。

### 下一阶段建议

下一阶段进入 `SEC-002`：后端安全基础，实现用户、角色、Entity 范围、轻量身份上下文和管理 API。

## SEC-002

阶段名称：后端安全基础

记录日期：2026-05-08

### 阶段目标

基于 SEC-001 安全设计，实现后端用户、角色、Entity 数据范围、轻量请求头身份上下文和管理 API。本阶段新增安全数据模型与 API，但不把授权判断接入所有业务接口；业务接口授权接入后置到 SEC-003。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | SEC-001 安全设计、ADR 0006、现有 metadata 与 common API 模式、当前 migration |
| 允许修改 | `backend/src/main/java/com/budgetplatform/security/*`、`backend/src/test/java/com/budgetplatform/security/*`、`backend/src/main/resources/db/migration/V6__security_baseline.sql`、`docs/architecture/sec-002-security-backend-baseline.md`、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 前端业务 UI、PDF 原文、OCR 全文、删除文件、ERP 直连、BI 图表、合并报表、全业务接口授权接入 |
| 验证命令 | `mvn test`、`git check-ignore`、`git status --short`、`git diff --check` |
| 授权状态 | 全自动模式；本阶段新增 migration 属于 SEC-002 范围，已记录风险 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/resources/db/migration/V6__security_baseline.sql` | 新增 `app_user`、`app_user_role`、`app_user_entity_scope` |
| `backend/src/main/java/com/budgetplatform/security/domain/*` | 新增安全用户、角色、Entity 范围实体与枚举 |
| `backend/src/main/java/com/budgetplatform/security/repository/*` | 新增安全仓储 |
| `backend/src/main/java/com/budgetplatform/security/api/*` | 新增安全管理 API 请求、响应和控制器 |
| `backend/src/main/java/com/budgetplatform/security/context/*` | 新增轻量请求头身份上下文解析 |
| `backend/src/main/java/com/budgetplatform/security/service/SecurityService.java` | 新增用户、角色、Entity 范围管理服务 |
| `backend/src/test/java/com/budgetplatform/security/api/SecurityControllerIntegrationTests.java` | 新增安全管理集成测试 |
| `docs/architecture/sec-002-security-backend-baseline.md` | 新增 SEC-002 架构与关闭建议文档 |
| `README.md` | 更新安全治理推进状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-002 阶段记录 |

### 关键产出

1. 新增安全数据模型 `app_user`、`app_user_role`、`app_user_entity_scope`。
2. 新增 `SecurityRoleCode` 七类角色枚举。
3. 新增 `/api/security/users`、角色授予、Entity 范围授予和 `/api/security/me`。
4. `/api/security/me` 支持从 `X-User-Id` 与 `X-User-Roles` 解析内部技术验证身份上下文。
5. 明确本阶段未接入全业务授权，避免一次性修改所有模块。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 27, Failures: 0, Errors: 0, Skipped: 0 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git status --short` | 通过；仅 SEC-002 后端安全基础、migration、文档和阶段记录修改 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| 边界关键词扫描 | 通过；`backend/src/main/java` 未发现 `@DeleteMapping`、ERP、Chart 或合并报表实现 |

### 失败项与修复记录

1. 本阶段后端测试首轮通过，未出现编译或测试失败。

### 风险与限制

1. 本阶段新增 migration `V6__security_baseline.sql`。
2. 安全管理 API 尚未强制 `BUDGET_ADMIN`，需 SEC-003 接入授权拦截。
3. 请求头身份上下文只适合内部技术验证，不可宣称生产认证完成。
4. 未提供删除、撤销或禁用角色/范围接口，后续如需撤销应单独设计停用或有效期策略。
5. 持久化审计仍需 `AUDIT-001`。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 前端业务 UI | 未修改 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| 全业务授权接入 | 未进入，后置 SEC-003 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 业务接口尚未接入授权判断。
2. 前端安全管理 UI 尚未实现。
3. 审计尚未持久化。
4. 登录、JWT、SSO 和密码策略尚未实现。

### 是否建议关闭本阶段

建议关闭 SEC-002。

关闭理由：后端安全基础数据模型、管理 API、轻量身份上下文、集成测试和架构文档均已完成；验证命令通过，未删除文件，未提交 PDF/OCR 全文或构建产物，未进入 ERP、BI、合并报表或全业务授权接入。

### 下一阶段建议

下一阶段进入 `SEC-003`：后端授权接入，将角色与 Entity 数据范围逐步接入元数据、模型、模板、填报、查询、Actual 导入和差异分析接口。

## SEC-003

阶段名称：后端授权入口接入

记录日期：2026-05-08

### 阶段目标

在 SEC-001 安全模型和 SEC-002 后端安全基础之上，建立统一授权服务，并先将授权接入安全管理 API 与预算查询读取 API。本阶段重点保护用户、角色、Entity 范围管理入口，以及事实明细、汇总、CSV 导出和预算与实际差异分析查询入口。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/sec-001-security-scope-design.md`、`docs/architecture/sec-002-security-backend-baseline.md`、现有 `SecurityController`、`BudgetQueryController`、`FactValueRepository` |
| 允许修改 | `backend/src/main/java/com/budgetplatform/security/*`、`backend/src/main/java/com/budgetplatform/budgetquery/*`、相关后端集成测试、SEC-003 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 前端 UI、PDF 原文、OCR 全文、删除文件、migration、ERP 直连、BI 图表、合并报表、全模块一次性授权重写 |
| 验证命令 | `mvn test`、`git check-ignore`、`git status --short`、`git diff --check`、后端边界关键词扫描 |
| 授权状态 | 用户已授权全自动推进；本阶段无删除文件，无 migration |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/security/service/AuthorizationService.java` | 新增统一授权服务，支持管理员校验、角色校验和 Entity 范围读取 |
| `backend/src/main/java/com/budgetplatform/security/api/SecurityController.java` | 为安全管理 API 接入管理员授权 |
| `backend/src/main/java/com/budgetplatform/budgetquery/api/BudgetQueryController.java` | 为查询接口解析请求头身份上下文 |
| `backend/src/main/java/com/budgetplatform/budgetquery/service/BudgetQueryService.java` | 为事实明细、汇总、CSV 和差异分析接入读取授权与 Entity 范围过滤 |
| `backend/src/main/java/com/budgetplatform/common/api/ErrorCode.java` | 新增 `UNAUTHORIZED` 与 `FORBIDDEN` 错误码 |
| `backend/src/main/java/com/budgetplatform/security/repository/AppUserRoleRepository.java` | 补充角色查询能力 |
| `backend/src/test/java/com/budgetplatform/security/api/SecurityControllerIntegrationTests.java` | 增加安全管理管理员校验测试 |
| `backend/src/test/java/com/budgetplatform/budgetquery/api/BudgetQueryControllerIntegrationTests.java` | 增加查询授权头和 Entity 范围过滤测试 |
| `backend/src/test/java/com/budgetplatform/budgetactual/api/ActualImportControllerIntegrationTests.java` | 为实际数导入后的查询验证增加管理员上下文 |
| `docs/architecture/sec-003-backend-authorization-entry-points.md` | 新增 SEC-003 架构与关闭建议文档 |
| `README.md` | 更新当前安全治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-003 阶段记录 |

### 关键产出

1. 新增 `AuthorizationService` 统一承载后端授权判断。
2. 安全管理 API 已要求 `BUDGET_ADMIN`。
3. 预算查询读取 API 已要求读取角色，并应用 Entity 数据范围过滤。
4. 预算与实际差异分析已复用读取授权和 Entity 范围过滤。
5. 新增 401/403 错误码，为后续接口授权提供统一错误语义。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 29, Failures: 0, Errors: 0, Skipped: 0 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git status --short` | 通过；仅 SEC-003 后端授权、测试、文档和阶段记录修改 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| 后端边界关键词扫描 | 通过；`backend/src/main/java` 未发现 `@DeleteMapping`、ERP、Chart 或合并报表实现 |

### 失败项与修复记录

1. 本阶段后端测试通过，未出现编译或测试失败。

### 风险与限制

1. 请求头身份上下文仍是内部技术验证机制，不代表生产登录完成。
2. 前端尚未统一注入身份请求头，部分受保护接口在前端直接调用时可能返回 401 或 403。
3. 本阶段只保护安全管理和预算查询读取面，元数据、模型、模板、填报、实际数导入等写接口授权仍需后续阶段继续接入。
4. Entity 范围只按事实数据 Entity 成员过滤，不引入 Account、Time、Category、Version 多维组合权限矩阵。
5. 持久化审计仍未实现。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| 前端 UI | 未修改 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 元数据、模型、模板、填报和实际数导入写接口尚未统一接入授权。
2. 前端安全请求头注入、用户切换和错误提示尚未实现。
3. 生产级认证、JWT/SSO、密码策略和会话管理尚未实现。
4. 持久化审计仍待 `AUDIT-001`。

### 是否建议关闭本阶段

建议关闭 SEC-003。

关闭理由：统一授权服务、安全管理 API 管理员保护、预算查询读取授权、Entity 数据范围过滤、集成测试和架构文档均已完成；后端测试通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-003B`：后端业务写接口授权接入，逐步保护元数据、预算模型、预算模板、预算填报和实际数导入接口。

## SEC-003B

阶段名称：元数据 API 授权接入

记录日期：2026-05-08

### 阶段目标

在 SEC-003 统一授权服务基础上，只针对元数据模块接入授权，保护 Workspace、Dimension 和 Dimension Member 的读写 API。本阶段不扩展到预算模型、模板、填报或实际数导入写接口。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/sec-003-backend-authorization-entry-points.md`、`docs/architecture/bud-002-metadata-model-design.md`、现有 `MetadataController` 与 `MetadataService` |
| 允许修改 | `backend/src/main/java/com/budgetplatform/metadata/*`、相关后端集成测试、SEC-003B 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 前端 UI、PDF 原文、OCR 全文、删除文件、migration、预算模型/模板/填报/导入模块授权、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git status --short`、`git diff --check`、后端边界关键词扫描 |
| 授权状态 | 用户已授权全自动推进；本阶段无删除文件，无 migration |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/metadata/api/MetadataController.java` | 解析请求头身份上下文并传入服务层 |
| `backend/src/main/java/com/budgetplatform/metadata/service/MetadataService.java` | 接入元数据读写授权规则 |
| `backend/src/test/java/com/budgetplatform/metadata/api/MetadataControllerIntegrationTests.java` | 新增元数据读写授权测试 |
| `backend/src/test/java/com/budgetplatform/budgetactual/api/ActualImportControllerIntegrationTests.java` | 为元数据准备步骤增加管理员请求头 |
| `backend/src/test/java/com/budgetplatform/budgetmodel/api/BudgetModelControllerIntegrationTests.java` | 为元数据准备步骤增加管理员请求头 |
| `backend/src/test/java/com/budgetplatform/budgetquery/api/BudgetQueryControllerIntegrationTests.java` | 为元数据准备步骤增加管理员请求头 |
| `backend/src/test/java/com/budgetplatform/budgetsubmission/api/SubmissionControllerIntegrationTests.java` | 为元数据准备步骤增加管理员请求头 |
| `backend/src/test/java/com/budgetplatform/budgettemplate/api/BudgetTemplateControllerIntegrationTests.java` | 为元数据准备步骤增加管理员请求头 |
| `backend/src/test/java/com/budgetplatform/security/api/SecurityControllerIntegrationTests.java` | 为元数据准备步骤增加管理员请求头 |
| `docs/architecture/sec-003b-metadata-authorization.md` | 新增 SEC-003B 架构与关闭建议文档 |
| `README.md` | 更新当前安全治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-003B 阶段记录 |

### 关键产出

1. Workspace 创建和 Workspace 清单要求请求头 `BUDGET_ADMIN`。
2. Dimension 与 Member 写操作要求 Workspace 内 `BUDGET_ADMIN` 或 `METADATA_MANAGER`。
3. Dimension 与 Member 读操作允许 Workspace 内任一业务读取角色。
4. 元数据控制器与服务层已统一接入 `CurrentUserContext`。
5. 所有依赖元数据夹具的集成测试均显式使用管理员上下文。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 30, Failures: 0, Errors: 0, Skipped: 0 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git status --short` | 通过；仅 SEC-003B 元数据授权、测试、文档和阶段记录修改 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| 后端边界关键词扫描 | 通过；`backend/src/main/java` 未发现 `@DeleteMapping`、ERP、Chart 或合并报表实现 |

### 失败项与修复记录

1. 使用 `rg` 搜索测试引用时，当前 Codex 桌面会话命中了打包路径 `C:\Program Files\WindowsApps\OpenAI.Codex_26.429.8261.0_x64__2p2nqsd0c76g0\app\resources\rg.exe` 并返回“拒绝访问”；已改用 PowerShell `Get-ChildItem` + `Select-String` 完成检索。
2. 后端测试首轮通过，未出现编译或测试失败。

### 风险与限制

1. 请求头身份上下文仍是内部技术验证机制，不代表生产登录完成。
2. 前端尚未统一注入身份请求头，元数据页面直接调用受保护 API 时可能返回 401 或 403。
3. 本阶段只保护元数据模块；预算模型、模板、填报、实际数导入写接口仍需后续阶段逐步接入授权。
4. Workspace 创建暂采用请求头管理员作为引导机制，后续应由生产认证替换。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| 前端 UI | 未修改 |
| 预算模型/模板/填报/导入授权 | 未进入 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 预算模型、模板、填报和实际数导入写接口尚未接入授权。
2. 前端安全请求头注入、用户切换和错误提示尚未实现。
3. 生产级认证、JWT/SSO、密码策略和会话管理尚未实现。
4. 持久化审计仍待 `AUDIT-001`。

### 是否建议关闭本阶段

建议关闭 SEC-003B。

关闭理由：元数据 API 读写授权、集成测试、阶段文档和阶段记录均已完成；后端测试通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-003C`：预算模型 API 授权接入，只处理预算模型模块。

## SEC-003C

阶段名称：预算模型 API 授权接入

记录日期：2026-05-08

### 阶段目标

在 SEC-003/SEC-003B 授权模式基础上，只针对预算模型模块接入授权，保护 Budget Model 创建、查询、维度绑定、激活和停用接口。本阶段不扩展到预算模板、预算填报或实际数导入。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/bud-005-budget-model-management.md`、`docs/architecture/sec-003b-metadata-authorization.md`、现有 `BudgetModelController` 与 `BudgetModelService` |
| 允许修改 | `backend/src/main/java/com/budgetplatform/budgetmodel/*`、相关后端集成测试、SEC-003C 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 前端 UI、PDF 原文、OCR 全文、删除文件、migration、预算模板/填报/导入授权、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git status --short`、`git diff --check`、后端边界关键词扫描 |
| 授权状态 | 用户已授权全自动推进；本阶段无删除文件，无 migration |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/budgetmodel/api/BudgetModelController.java` | 解析请求头身份上下文并传入服务层 |
| `backend/src/main/java/com/budgetplatform/budgetmodel/service/BudgetModelService.java` | 接入预算模型读写授权规则 |
| `backend/src/test/java/com/budgetplatform/budgetmodel/api/BudgetModelControllerIntegrationTests.java` | 新增预算模型读写授权测试 |
| `backend/src/test/java/com/budgetplatform/budgetactual/api/ActualImportControllerIntegrationTests.java` | 为预算模型准备步骤增加管理员请求头 |
| `backend/src/test/java/com/budgetplatform/budgetquery/api/BudgetQueryControllerIntegrationTests.java` | 为预算模型准备步骤增加管理员请求头 |
| `backend/src/test/java/com/budgetplatform/budgetsubmission/api/SubmissionControllerIntegrationTests.java` | 为预算模型准备步骤增加管理员请求头 |
| `backend/src/test/java/com/budgetplatform/budgettemplate/api/BudgetTemplateControllerIntegrationTests.java` | 为预算模型准备步骤增加管理员请求头 |
| `docs/architecture/sec-003c-budget-model-authorization.md` | 新增 SEC-003C 架构与关闭建议文档 |
| `README.md` | 更新当前安全治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-003C 阶段记录 |

### 关键产出

1. 预算模型创建、维度绑定、激活和停用要求 Workspace 内 `BUDGET_ADMIN` 或 `METADATA_MANAGER`。
2. 预算模型清单、详情和绑定维度清单允许 Workspace 内任一业务读取角色。
3. 预算模型控制器与服务层已统一接入 `CurrentUserContext`。
4. 所有依赖预算模型夹具的集成测试均显式使用管理员上下文。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 31, Failures: 0, Errors: 0, Skipped: 0 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git status --short` | 通过；仅 SEC-003C 预算模型授权、测试、文档和阶段记录修改 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| 后端边界关键词扫描 | 通过；`backend/src/main/java` 未发现 `@DeleteMapping`、ERP、Chart 或合并报表实现 |

### 失败项与修复记录

1. 后端测试首轮通过，未出现编译或测试失败。

### 风险与限制

1. 请求头身份上下文仍是内部技术验证机制，不代表生产登录完成。
2. 前端尚未统一注入身份请求头，预算模型页面直接调用受保护 API 时可能返回 401 或 403。
3. 本阶段只保护预算模型模块；预算模板、填报和实际数导入写接口仍需后续阶段逐步接入授权。
4. 写权限暂限定为 `BUDGET_ADMIN` 和 `METADATA_MANAGER`，避免填报角色修改模型结构。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| 前端 UI | 未修改 |
| 预算模板/填报/导入授权 | 未进入 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 预算模板、填报和实际数导入写接口尚未接入授权。
2. 前端安全请求头注入、用户切换和错误提示尚未实现。
3. 生产级认证、JWT/SSO、密码策略和会话管理尚未实现。
4. 持久化审计仍待 `AUDIT-001`。

### 是否建议关闭本阶段

建议关闭 SEC-003C。

关闭理由：预算模型 API 读写授权、集成测试、阶段文档和阶段记录均已完成；后端测试通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-003D`：预算模板 API 授权接入，只处理模板模块。

## SEC-003D

阶段名称：预算模板 API 授权接入

记录日期：2026-05-08

### 阶段目标

在 SEC-003C 授权模式基础上，只针对预算模板模块接入授权，保护 Budget Template 创建、查询、轴配置、激活和停用接口。本阶段不扩展到预算填报或实际数导入。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/bud-006-budget-template-management.md`、`docs/architecture/sec-003c-budget-model-authorization.md`、现有 `BudgetTemplateController` 与 `BudgetTemplateService` |
| 允许修改 | `backend/src/main/java/com/budgetplatform/budgettemplate/*`、相关后端集成测试、SEC-003D 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 前端 UI、PDF 原文、OCR 全文、删除文件、migration、预算填报/导入授权、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git status --short`、`git diff --check`、后端边界关键词扫描 |
| 授权状态 | 用户已授权全自动推进；本阶段无删除文件，无 migration |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/budgettemplate/api/BudgetTemplateController.java` | 解析请求头身份上下文并传入服务层 |
| `backend/src/main/java/com/budgetplatform/budgettemplate/service/BudgetTemplateService.java` | 接入预算模板读写授权规则 |
| `backend/src/test/java/com/budgetplatform/budgettemplate/api/BudgetTemplateControllerIntegrationTests.java` | 新增预算模板读写授权测试 |
| `backend/src/test/java/com/budgetplatform/budgetquery/api/BudgetQueryControllerIntegrationTests.java` | 为预算模板准备步骤增加管理员请求头 |
| `backend/src/test/java/com/budgetplatform/budgetsubmission/api/SubmissionControllerIntegrationTests.java` | 为预算模板准备步骤增加管理员请求头 |
| `docs/architecture/sec-003d-budget-template-authorization.md` | 新增 SEC-003D 架构与关闭建议文档 |
| `README.md` | 更新当前安全治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-003D 阶段记录 |

### 关键产出

1. 预算模板创建、轴配置、激活和停用要求 Workspace 内 `BUDGET_ADMIN` 或 `TEMPLATE_DESIGNER`。
2. 预算模板清单、详情和轴清单允许 Workspace 内任一业务读取角色。
3. 预算模板控制器与服务层已统一接入 `CurrentUserContext`。
4. 所有依赖模板夹具的集成测试均显式使用管理员上下文。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 32, Failures: 0, Errors: 0, Skipped: 0 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git status --short` | 通过；仅 SEC-003D 预算模板授权、测试、文档和阶段记录修改 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| 后端边界关键词扫描 | 通过；`backend/src/main/java` 未发现 `@DeleteMapping`、ERP、Chart 或合并报表实现 |

### 失败项与修复记录

1. 后端测试首轮通过，未出现编译或测试失败。

### 风险与限制

1. 请求头身份上下文仍是内部技术验证机制，不代表生产登录完成。
2. 前端尚未统一注入身份请求头，预算模板页面直接调用受保护 API 时可能返回 401 或 403。
3. 本阶段只保护预算模板模块；预算填报和实际数导入写接口仍需后续阶段逐步接入授权。
4. 写权限暂限定为 `BUDGET_ADMIN` 和 `TEMPLATE_DESIGNER`，避免填报角色修改模板结构。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| 前端 UI | 未修改 |
| 预算填报/导入授权 | 未进入 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 预算填报和实际数导入写接口尚未接入授权。
2. 前端安全请求头注入、用户切换和错误提示尚未实现。
3. 生产级认证、JWT/SSO、密码策略和会话管理尚未实现。
4. 持久化审计仍待 `AUDIT-001`。

### 是否建议关闭本阶段

建议关闭 SEC-003D。

关闭理由：预算模板 API 读写授权、集成测试、阶段文档和阶段记录均已完成；后端测试通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-003E`：预算填报 API 授权接入，只处理填报模块。

## SEC-003E

阶段名称：预算填报 API 授权接入

记录日期：2026-05-08

### 阶段目标

在 SEC-003D 授权模式基础上，只针对预算填报模块接入授权，保护 Submission Task 创建、查询、填报值保存、提交、退回、审批和锁定接口。本阶段不扩展到实际数导入或生产认证。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/bud-007-budget-submission-baseline.md`、`docs/architecture/sec-003d-budget-template-authorization.md`、现有 `SubmissionController` 与 `SubmissionService` |
| 允许修改 | `backend/src/main/java/com/budgetplatform/budgetsubmission/*`、相关后端集成测试、SEC-003E 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 前端 UI、PDF 原文、OCR 全文、删除文件、migration、实际数导入授权、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git status --short`、`git diff --check`、后端边界关键词扫描 |
| 授权状态 | 用户已授权全自动推进；本阶段无删除文件，无 migration |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/budgetsubmission/api/SubmissionController.java` | 解析请求头身份上下文并传入服务层 |
| `backend/src/main/java/com/budgetplatform/budgetsubmission/service/SubmissionService.java` | 接入填报任务读写、Owner/Reviewer 和 Entity 范围授权 |
| `backend/src/test/java/com/budgetplatform/budgetsubmission/api/SubmissionControllerIntegrationTests.java` | 新增填报授权测试，并为填报流程增加管理员请求头 |
| `backend/src/test/java/com/budgetplatform/budgetquery/api/BudgetQueryControllerIntegrationTests.java` | 为查询夹具中的填报步骤增加管理员请求头 |
| `docs/architecture/sec-003e-budget-submission-authorization.md` | 新增 SEC-003E 架构与关闭建议文档 |
| `README.md` | 更新当前安全治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-003E 阶段记录 |

### 关键产出

1. 填报任务创建、保存和提交支持 `BUDGET_ADMIN`、owner 本人或 `BUDGET_OWNER` + Entity 范围。
2. 退回、审批和锁定支持 `BUDGET_ADMIN`、reviewer 本人或 `BUDGET_REVIEWER` + Entity 范围。
3. 任务详情和值读取支持管理员、owner/reviewer 或读取角色 + Entity 范围。
4. 填报控制器与服务层已统一接入 `CurrentUserContext`。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 33, Failures: 0, Errors: 0, Skipped: 0 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git status --short` | 通过；仅 SEC-003E 预算填报授权、测试、文档和阶段记录修改 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| 后端边界关键词扫描 | 通过；`backend/src/main/java` 未发现 `@DeleteMapping`、ERP、Chart 或合并报表实现 |

### 失败项与修复记录

1. 后端测试首轮通过，未出现编译或测试失败。

### 风险与限制

1. 请求头身份上下文仍是内部技术验证机制，不代表生产登录完成。
2. 任务清单接口当前只做 Workspace 读取角色校验，尚未按 Entity 范围裁剪列表；任务详情和值读取已接入 Entity 范围。
3. 前端尚未统一注入身份请求头，预算填报页面直接调用受保护 API 时可能返回 401 或 403。
4. 持久化审计仍待 `AUDIT-001`。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| 前端 UI | 未修改 |
| 实际数导入授权 | 未进入 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 实际数导入写接口尚未接入授权。
2. 前端安全请求头注入、用户切换和错误提示尚未实现。
3. 生产级认证、JWT/SSO、密码策略和会话管理尚未实现。
4. 持久化审计仍待 `AUDIT-001`。

### 是否建议关闭本阶段

建议关闭 SEC-003E。

关闭理由：预算填报 API 主要读写授权、Owner/Reviewer 授权、Entity 范围检查、集成测试、阶段文档和阶段记录均已完成；后端测试通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-003F`：实际数导入 API 授权接入，只处理导入模块。

## SEC-003F

阶段名称：实际数导入 API 授权接入

记录日期：2026-05-08

### 阶段目标

在 SEC-003E 授权模式基础上，只针对实际数导入模块接入授权，保护 Actual CSV 校验、提交、批次查询和行级结果查询接口。本阶段不扩展到 ERP 直连、复杂 Data Manager 或生产认证。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/bud-009-actual-import-baseline.md`、`docs/architecture/sec-003e-budget-submission-authorization.md`、现有 `ActualImportController` 与 `ActualImportService` |
| 允许修改 | `backend/src/main/java/com/budgetplatform/budgetactual/*`、相关后端集成测试、SEC-003F 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 前端 UI、PDF 原文、OCR 全文、删除文件、migration、ERP 直连、BI 图表、合并报表、复杂 Data Manager |
| 验证命令 | `mvn test`、`git check-ignore`、`git status --short`、`git diff --check`、后端边界关键词扫描 |
| 授权状态 | 用户已授权全自动推进；本阶段无删除文件，无 migration |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/budgetactual/api/ActualImportController.java` | 解析请求头身份上下文并传入服务层 |
| `backend/src/main/java/com/budgetplatform/budgetactual/service/ActualImportService.java` | 接入实际数导入读写授权规则 |
| `backend/src/test/java/com/budgetplatform/budgetactual/api/ActualImportControllerIntegrationTests.java` | 新增实际数导入授权测试，并为导入流程增加管理员请求头 |
| `backend/src/test/java/com/budgetplatform/budgetquery/api/BudgetQueryControllerIntegrationTests.java` | 为差异分析夹具中的 Actual 导入步骤增加管理员请求头 |
| `docs/architecture/sec-003f-actual-import-authorization.md` | 新增 SEC-003F 架构与关闭建议文档 |
| `README.md` | 更新当前安全治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-003F 阶段记录 |

### 关键产出

1. Actual CSV Validate 和 Commit 要求 Workspace 内 `BUDGET_ADMIN` 或 `IMPORT_OPERATOR`。
2. Actual 批次和行级结果查询允许 Workspace 内管理员、导入员和业务读取角色。
3. Actual 导入控制器与服务层已统一接入 `CurrentUserContext`。
4. 原有 Actual 导入、错误报告和 Budget vs Actual 查询链路保持通过。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 34, Failures: 0, Errors: 0, Skipped: 0 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git status --short` | 通过；仅 SEC-003F 实际数导入授权、测试、文档和阶段记录修改 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| 后端边界关键词扫描 | 通过；`backend/src/main/java` 未发现 `@DeleteMapping`、ERP、Chart 或合并报表实现 |

### 失败项与修复记录

1. 后端测试首轮通过，未出现编译或测试失败。

### 风险与限制

1. 请求头身份上下文仍是内部技术验证机制，不代表生产登录完成。
2. 批次和行级结果读取当前按 Workspace 角色控制，尚未按行级 Entity 范围过滤。
3. 前端尚未统一注入身份请求头，Actual 导入页面直接调用受保护 API 时可能返回 401 或 403。
4. 本阶段不引入 ERP 直连，不实现黑盒 Data Manager，也不实现外部编码转换表。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| 前端 UI | 未修改 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| 复杂 Data Manager | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 前端安全请求头注入、用户切换和错误提示尚未实现。
2. 生产级认证、JWT/SSO、密码策略和会话管理尚未实现。
3. 持久化审计仍待 `AUDIT-001`。
4. 导入批次行级读取尚未按 Entity 范围细裁剪。

### 是否建议关闭本阶段

建议关闭 SEC-003F。

关闭理由：实际数导入 API 读写授权、集成测试、阶段文档和阶段记录均已完成；后端测试通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-004`：前端安全上下文接入，为现有页面统一注入请求头并处理 401/403。

## SEC-004

阶段名称：前端安全上下文接入

记录日期：2026-05-08

### 阶段目标

在不引入生产登录、JWT、SSO 或复杂权限管理页面的前提下，为现有 React/Vite MVP 接入内部安全上下文，统一向后端受保护 API 注入 `X-User-Id` 与 `X-User-Roles` 请求头，并让 401/403 授权错误在界面中更容易诊断。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/sec-003f-actual-import-authorization.md`、现有 `frontend/src/shared/api/http.ts` 与 `frontend/src/App.tsx` |
| 允许修改 | `frontend/src/shared/api/http.ts`、`frontend/src/App.tsx`、`frontend/src/styles.css`、SEC-004 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 后端业务逻辑、migration、PDF 原文、OCR 全文、删除文件、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `pnpm type-check`、`pnpm lint`、`pnpm build`、`git check-ignore`、`git diff --check`、`git status --short` |
| 授权状态 | 用户已授权全自动推进；本阶段无删除文件，无 migration |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `frontend/src/shared/api/http.ts` | 新增 API 安全上下文存取函数，并统一注入 `X-User-Id` 与 `X-User-Roles` |
| `frontend/src/App.tsx` | 新增顶层内部安全上下文控件，并增强 401/403 授权错误展示 |
| `frontend/src/styles.css` | 新增安全上下文区域的响应式样式 |
| `docs/architecture/sec-004-frontend-security-context.md` | 新增 SEC-004 前端安全上下文设计文档 |
| `README.md` | 更新当前安全治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-004 阶段记录 |

### 关键产出

1. 前端所有通过 `requestJson` 的 API 调用默认携带 `admin@example.com` 与 `BUDGET_ADMIN` 内部安全上下文。
2. 应用顶部提供 User 与 Role 控件，支持在 MVP 验证时切换调用身份和角色。
3. 授权失败时，界面会展示 `UNAUTHORIZED` 或 `FORBIDDEN` 错误码与后端消息。
4. 未新增生产认证、密钥处理、数据库迁移或复杂权限矩阵。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `pnpm type-check` | 通过；`tsc --noEmit` 无错误 |
| `pnpm lint` | 通过；`eslint .` 无错误 |
| `pnpm build` | 通过；Vite 生产构建成功，输出在被忽略的 `frontend/dist` |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 SEC-004 前端安全上下文、文档和阶段记录修改 |

### 失败项与修复记录

1. 首轮前端 type-check、lint、build 均通过，未出现需要修复的编译或测试失败。

### 风险与限制

1. 本阶段仍是内部请求头上下文，不是生产级登录或会话管理。
2. UI 当前一次选择一个角色；HTTP 客户端和后端仍支持逗号分隔的多角色请求头。
3. Entity 范围授予尚未在前端暴露，涉及 Entity 范围的角色仍依赖后端安全数据准备。
4. 持久化审计仍待后续审计阶段。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| backend/src | 未修改 |
| migration | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 生产登录、JWT/SSO、密码策略和会话管理尚未实现。
2. 前端尚未提供安全用户、角色授予和 Entity 范围维护页面。
3. 查询和导入结果的更细行级数据裁剪仍需后续治理阶段设计。
4. 持久化审计仍未完成。

### 是否建议关闭本阶段

建议关闭 SEC-004。

关闭理由：前端统一身份请求头注入、内部上下文切换、授权错误展示、阶段文档和阶段记录均已完成；前端 type-check、lint、build 均通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `AUDIT-001`：持久化审计基线设计与最小实现，优先覆盖安全管理与受保护业务写操作。

## AUDIT-001

阶段名称：持久化审计基线设计与最小实现

记录日期：2026-05-08

### 阶段目标

将既有 `AuditService` 从空实现升级为最小 JPA 持久化审计，实现 `audit_event` 表、审计实体、仓库和服务，覆盖安全用户管理、角色/Entity 范围授予，以及已有预算填报和实际数导入审计调用点。本阶段不建设审计查询 UI、告警、外部日志服务或复杂流程引擎。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | 现有 `common/audit` 包、SEC-003/SEC-004 安全治理结果、Flyway migration 目录、Security/Submission/Actual 服务 |
| 允许修改 | 后端审计包、必要写操作调用点、`V7__audit_event_baseline.sql`、后端测试、AUDIT-001 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 删除文件、PDF 原文、OCR 全文、ERP 直连、BI 图表、合并报表、前端新功能 |
| 验证命令 | `mvn -q -DskipTests compile`、`mvn test`、`git check-ignore`、`git diff --check`、`git status --short`、后端边界关键词扫描 |
| 授权状态 | 用户已授权全自动推进；本阶段新增 migration，未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/resources/db/migration/V7__audit_event_baseline.sql` | 新增 `audit_event` 表和审计查询索引 |
| `backend/src/main/java/com/budgetplatform/common/audit/AuditEventRecord.java` | 新增审计事件 JPA 实体 |
| `backend/src/main/java/com/budgetplatform/common/audit/AuditEventRecordRepository.java` | 新增审计事件仓库 |
| `backend/src/main/java/com/budgetplatform/common/audit/JpaAuditService.java` | 新增持久化审计服务 |
| `backend/src/main/java/com/budgetplatform/common/audit/NoopAuditService.java` | 保留空实现但不再作为 Spring Bean |
| `backend/src/main/java/com/budgetplatform/security/api/SecurityController.java` | 将当前用户上下文传给安全写操作服务 |
| `backend/src/main/java/com/budgetplatform/security/service/SecurityService.java` | 安全用户创建、角色授予、Entity 范围授予写入审计 |
| `backend/src/main/java/com/budgetplatform/budgetsubmission/service/SubmissionService.java` | 填报审计 actor 改为当前用户上下文 |
| `backend/src/main/java/com/budgetplatform/budgetactual/service/ActualImportService.java` | 实际数导入审计 actor 改为当前用户上下文 |
| `backend/src/test/java/com/budgetplatform/common/audit/AuditServiceIntegrationTests.java` | 新增审计持久化测试 |
| `backend/src/test/java/com/budgetplatform/security/api/SecurityControllerIntegrationTests.java` | 增加安全管理审计落库断言 |
| `docs/architecture/audit-001-persistent-audit-baseline.md` | 新增 AUDIT-001 架构与关闭建议文档 |
| `README.md` | 更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 AUDIT-001 阶段记录 |

### 关键产出

1. `audit_event` 表保存 actor、subject、action、occurred_at 和 JSON 详情。
2. Flyway 已验证 7 个 migration，并可在 H2 测试库完整迁移。
3. 安全用户创建、角色授予、Entity 范围授予写入 `ACCESS_CHANGE` 或 `CREATE` 审计事件。
4. 预算填报和实际数导入保留原有审计点，并记录当前请求用户作为 actor。
5. 审计持久化测试和安全管理审计断言已加入后端测试集。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn -q -DskipTests compile` | 通过；后端主代码编译成功 |
| `mvn test` | 通过；Tests run: 35, Failures: 0, Errors: 0, Skipped: 0 |
| Flyway migration | 通过；成功验证并应用 7 个 migrations，当前版本 v7 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 AUDIT-001 后端审计、测试、migration、文档和阶段记录修改 |
| 后端边界关键词扫描 | 通过；`backend/src/main/java` 未发现 `@DeleteMapping`、ERP、Chart 或合并报表实现 |

### 失败项与修复记录

1. 后端编译和测试首轮通过，未出现需要修复的编译或测试失败。

### 风险与限制

1. 审计写入失败当前会导致业务事务失败，这是 MVP 治理阶段的保守选择。
2. `details_json` 当前是 JSON 文本，后续如需要 PostgreSQL JSONB 索引需单独进入性能治理阶段。
3. 暂未提供审计查询 API 和前端审计视图。
4. 元数据、预算模型、预算模板写操作尚未全部补充审计调用点；本阶段仅覆盖安全管理和已有审计调用点。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 新增 `V7__audit_event_baseline.sql`，阶段内允许 |
| 前端 UI | 未修改 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 审计查询 API、审计筛选、导出和前端视图尚未实现。
2. 审计保留策略、归档策略和敏感字段脱敏策略尚未制定。
3. 元数据、预算模型和预算模板写操作审计仍需后续阶段覆盖。
4. 生产登录和服务端可信身份链路尚未完成。

### 是否建议关闭本阶段

建议关闭 AUDIT-001。

关闭理由：持久化审计表、JPA 实体/仓库/服务、安全管理审计、既有业务审计 actor 修正、测试、架构文档和阶段记录均已完成；后端测试通过，未删除文件，未提交 PDF/OCR 全文或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `AUDIT-002`：审计查询 API 与基础分页，只提供治理排查所需的只读接口，不建设 BI 图表或复杂审计报表。

## AUDIT-002

阶段名称：审计查询 API 与基础分页

记录日期：2026-05-08

### 阶段目标

在 AUDIT-001 持久化审计表基础上，新增只读审计查询 API，支持 actor、subject、action 过滤与基础分页，供治理排查使用。本阶段不建设前端审计页面、不提供导出、不做 BI 图表或复杂审计报表。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/audit-001-persistent-audit-baseline.md`、现有 `AuditEventRecordRepository`、安全上下文与授权服务 |
| 允许修改 | 审计查询 API/服务/响应、通用分页响应、审计查询测试、AUDIT-002 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 删除文件、PDF 原文、OCR 全文、前端 UI、ERP 直连、BI 图表、合并报表、审计删除能力 |
| 验证命令 | `mvn test`、`git check-ignore`、`git diff --check`、`git status --short`、后端边界关键词扫描 |
| 授权状态 | 用户已授权全自动推进；本阶段无删除文件，无 migration |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/common/api/PageResponse.java` | 新增通用分页响应结构 |
| `backend/src/main/java/com/budgetplatform/common/audit/AuditController.java` | 新增 `GET /api/audit/events` 只读审计查询接口 |
| `backend/src/main/java/com/budgetplatform/common/audit/AuditQueryService.java` | 新增审计查询过滤、分页和 BUDGET_ADMIN 校验 |
| `backend/src/main/java/com/budgetplatform/common/audit/AuditEventResponse.java` | 新增审计事件响应结构 |
| `backend/src/main/java/com/budgetplatform/common/audit/AuditEventRecordRepository.java` | 新增审计事件过滤分页查询 |
| `backend/src/test/java/com/budgetplatform/common/audit/AuditControllerIntegrationTests.java` | 新增查询、授权和参数校验集成测试 |
| `docs/architecture/audit-002-audit-query-api.md` | 新增 AUDIT-002 架构与关闭建议文档 |
| `README.md` | 更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 AUDIT-002 阶段记录 |

### 关键产出

1. `GET /api/audit/events` 支持 `actorId`、`subjectType`、`subjectId`、`action`、`page`、`size` 参数。
2. 审计查询要求请求头包含 `BUDGET_ADMIN`。
3. 分页响应包含 `items`、`page`、`size`、`totalElements`、`totalPages`。
4. `size` 限制为 1 到 100，非法 action 和分页参数会返回 400。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 38, Failures: 0, Errors: 0, Skipped: 0 |
| Flyway migration | 通过；成功验证 7 个 migrations，当前版本 v7 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 AUDIT-002 审计查询 API、测试、文档和阶段记录修改 |
| 后端边界关键词扫描 | 通过；`backend/src/main/java` 未发现 `@DeleteMapping`、ERP、Chart 或合并报表实现 |

### 失败项与修复记录

1. 后端测试首轮通过，未出现编译或测试失败。

### 风险与限制

1. 审计查询目前只允许 `BUDGET_ADMIN`，尚未设计按 Workspace 或 Entity 的审计可见性。
2. `detailsJson` 作为 JSON 文本返回，未解析为对象。
3. 本阶段没有前端审计页面、导出和保留策略。
4. 审计查询分页仅支持固定时间倒序，暂未开放排序参数。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| 前端 UI | 未修改 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| 审计删除能力 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 审计前端视图尚未实现。
2. 审计导出、保留、归档和脱敏策略尚未制定。
3. 审计可见性仍是全局管理员级别，未细分到工作区。
4. 元数据、预算模型和预算模板写操作审计仍需后续阶段补齐。

### 是否建议关闭本阶段

建议关闭 AUDIT-002。

关闭理由：审计只读查询 API、过滤分页、管理员授权、参数校验、测试、架构文档和阶段记录均已完成；后端测试通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `AUDIT-003`：补齐元数据、预算模型和预算模板写操作审计调用点，保持后端最小范围，不新增前端页面。

## AUDIT-003

阶段名称：核心配置写操作审计覆盖

记录日期：2026-05-08

### 阶段目标

在 AUDIT-001/AUDIT-002 基础上，补齐元数据、预算模型和预算模板写操作的审计调用点，让核心配置变更具备最小可追溯能力。本阶段不新增前端页面、不新增业务功能、不提供审计导出、不新增删除能力。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/audit-001-persistent-audit-baseline.md`、`docs/architecture/audit-002-audit-query-api.md`、Metadata/Model/Template 服务与测试 |
| 允许修改 | `MetadataService`、`BudgetModelService`、`BudgetTemplateService`、相关集成测试、AUDIT-003 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 删除文件、PDF 原文、OCR 全文、migration、前端 UI、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git diff --check`、`git status --short`、后端边界关键词扫描 |
| 授权状态 | 用户已授权全自动推进；本阶段无删除文件，无 migration |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/metadata/service/MetadataService.java` | 为 Workspace、Dimension、Member 创建和 Member 更新记录审计 |
| `backend/src/main/java/com/budgetplatform/budgetmodel/service/BudgetModelService.java` | 为预算模型创建、维度绑定、启停记录审计 |
| `backend/src/main/java/com/budgetplatform/budgettemplate/service/BudgetTemplateService.java` | 为预算模板创建、轴添加、启停记录审计 |
| `backend/src/test/java/com/budgetplatform/metadata/api/MetadataControllerIntegrationTests.java` | 增加元数据写操作审计断言 |
| `backend/src/test/java/com/budgetplatform/budgetmodel/api/BudgetModelControllerIntegrationTests.java` | 增加预算模型写操作审计断言 |
| `backend/src/test/java/com/budgetplatform/budgettemplate/api/BudgetTemplateControllerIntegrationTests.java` | 增加预算模板写操作审计断言 |
| `docs/architecture/audit-003-core-write-audit-coverage.md` | 新增 AUDIT-003 架构与关闭建议文档 |
| `README.md` | 更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 AUDIT-003 阶段记录 |

### 关键产出

1. 元数据写操作覆盖 `budget_workspace`、`dimension`、`dimension_member` 审计事件。
2. 预算模型写操作覆盖 `budget_model` 和 `budget_model_dimension` 审计事件。
3. 预算模板写操作覆盖 `budget_template` 和 `budget_template_axis` 审计事件。
4. 代表性集成测试已断言审计事件真实落库。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 38, Failures: 0, Errors: 0, Skipped: 0 |
| Flyway migration | 通过；成功验证 7 个 migrations，当前版本 v7 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 AUDIT-003 后端审计调用点、测试、文档和阶段记录修改 |
| 后端边界关键词扫描 | 通过；`backend/src/main/java` 未发现 `@DeleteMapping`、ERP、Chart 或合并报表实现 |

### 失败项与修复记录

1. 后端测试首轮通过，未出现编译或测试失败。

### 风险与限制

1. 本阶段只记录成功写操作，失败尝试暂不进入审计表。
2. 审计前端视图、导出、保留、归档和脱敏仍未实现。
3. 当前审计详情保持轻量，不记录完整请求体，避免过度沉淀敏感或冗余数据。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| 前端 UI | 未修改 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 审计前端视图尚未实现。
2. 审计导出、保留、归档和脱敏策略尚未制定。
3. 失败写尝试审计尚未设计。
4. 生产登录和服务端可信身份链路尚未完成。

### 是否建议关闭本阶段

建议关闭 AUDIT-003。

关闭理由：核心配置写操作审计调用点、代表性落库断言、架构文档和阶段记录均已完成；后端测试通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-005`：生产认证边界设计，明确 JWT/SSO/会话管理路线，但先做设计文档，不直接接入外部身份服务。

## SEC-005

阶段名称：生产认证边界设计

记录日期：2026-05-09

### 阶段目标

在不直接接入外部身份服务、不引入 JWT/OAuth 依赖、不新增登录页面、不新增 secrets 或 migration 的前提下，明确生产认证信任边界、当前请求头身份模式的风险、JWT/SSO/反向代理身份适配路线，以及后续 SEC 阶段拆分。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/sec-001-security-scope-design.md`、`docs/architecture/sec-002-security-backend-baseline.md`、`docs/architecture/sec-004-frontend-security-context.md`、`docs/architecture/audit-003-core-write-audit-coverage.md`、`README.md` |
| 允许修改 | `docs/architecture/sec-005-production-auth-boundary.md`、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 后端业务代码、前端实现、migration、PDF 原文、OCR 全文、外部身份服务接入、密钥配置、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `git check-ignore`、`git diff --check`、`git status --short` |
| 授权状态 | 用户已授权全自动推进；本阶段无删除文件，无代码实现，无 migration |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/sec-005-production-auth-boundary.md` | 新增生产认证边界、身份契约、分阶段落地路线和非目标 |
| `README.md` | 更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-005 阶段记录 |

### 关键产出

1. 明确当前 `X-User-Id` / `X-User-Roles` 是内部技术验证机制，不能作为生产可信身份。
2. 明确生产认证应由后端作为信任边界，前端不得声明角色。
3. 明确应用角色与 Entity 范围继续由预算平台自管，IdP 只负责认证人员。
4. 拆分后续 `SEC-006` 可信 Principal 适配器、`SEC-007` 移除请求头角色信任、`SEC-008` 前端登录边界、`SEC-009` 会话与 Token 运维规则。
5. 明确本阶段不实现 Spring Security、JWT/OAuth、登录页、密码存储、外部服务调用或 secrets。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 SEC-005 架构文档、README 和阶段记录修改 |

### 失败项与修复记录

1. 本阶段为文档设计阶段，未运行后端或前端测试；无代码编译风险。

### 风险与限制

1. 生产认证尚未实现，当前运行态仍依赖内部请求头上下文。
2. 可信 Principal 适配器和生产配置仍需后续实现阶段。
3. 前端内部身份选择器仍需后续生产登录阶段替换。
4. 审计 actor 的可信度仍依赖当前请求头，需在可信身份接入后升级。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端业务代码 | 未修改 |
| 前端实现 | 未修改 |
| migration | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. `SEC-006` 尚未实现可信 Principal 后端适配器。
2. `SEC-007` 尚未禁用生产环境中的 `X-User-Roles` 信任。
3. `SEC-008` 尚未替换前端内部身份选择器。
4. 生产 JWT/OIDC 或反向代理身份来源尚未最终选型。

### 是否建议关闭本阶段

建议关闭 SEC-005。

关闭理由：生产认证边界、身份契约、阶段路线和非目标已形成架构文档；本阶段未修改代码、未删除文件、未新增 migration、未提交 PDF/OCR 全文或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-006`：可信 Principal 后端适配器最小实现，保留显式 dev-header 模式，并为后续 JWT/反向代理模式预留接口。

## SEC-006

阶段名称：可信 Principal 后端适配器最小实现

记录日期：2026-05-09

### 阶段目标

围绕现有 `CurrentUserContextResolver` 建立可配置认证模式，让当前请求头身份机制明确限定为 `DEV_HEADER`，并为后续 JWT 和反向代理身份模式预留失败关闭的适配入口。本阶段不实现 JWT 验签、不接入外部 IdP、不新增 secrets、不修改数据库结构、不新增前端登录。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/sec-005-production-auth-boundary.md`、现有 `CurrentUserContextResolver`、`application.yml`、后端安全测试 |
| 允许修改 | 后端安全上下文/认证配置、测试配置、Resolver 单元测试、SEC-006 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | migration、前端 UI、PDF 原文、OCR 全文、外部身份服务接入、JWT/OAuth 依赖、secrets、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git diff --check`、`git status --short`、后端边界关键词扫描 |
| 授权状态 | 用户已授权全自动推进；本阶段无删除文件，无 migration，无外部服务访问 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/security/context/AuthMode.java` | 新增认证模式枚举：`DEV_HEADER`、`JWT`、`REVERSE_PROXY` |
| `backend/src/main/java/com/budgetplatform/security/context/AuthProperties.java` | 新增 `budget-platform.auth` 配置绑定 |
| `backend/src/main/java/com/budgetplatform/security/context/CurrentUserContextResolver.java` | 按认证模式解析当前用户上下文，JWT/反向代理模式失败关闭 |
| `backend/src/main/resources/application.yml` | 新增认证模式配置项和环境变量入口 |
| `backend/src/test/resources/application-test.yml` | 测试环境显式使用 `DEV_HEADER` 且允许请求头角色 |
| `backend/src/test/java/com/budgetplatform/security/context/CurrentUserContextResolverTests.java` | 新增 Resolver 单元测试 |
| `docs/architecture/sec-006-trusted-principal-adapter.md` | 新增 SEC-006 架构与关闭建议文档 |
| `README.md` | 更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-006 阶段记录 |

### 关键产出

1. 认证模式被显式配置为 `budget-platform.auth.mode`。
2. `DEV_HEADER` 模式保留本地和测试兼容性。
3. `allow-header-roles=false` 时 `X-User-Roles` 不再参与当前上下文解析。
4. `JWT` 和 `REVERSE_PROXY` 当前失败关闭，避免误配置成未实现的生产认证。
5. 后续 `SEC-007` 可以基于该适配器移除生产角色请求头信任。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 41, Failures: 0, Errors: 0, Skipped: 0 |
| Flyway migration | 通过；成功验证 7 个 migrations，当前版本 v7 |
| `git check-ignore` | 通过；PDF、OCR、构建产物、依赖目录和后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 SEC-006 后端安全上下文、配置、测试、文档和阶段记录修改 |
| 后端边界关键词扫描 | 通过；`backend/src/main/java` 未发现 `@DeleteMapping`、ERP、Chart 或合并报表实现 |

### 失败项与修复记录

1. 后端测试首轮通过，未出现编译或测试失败。

### 风险与限制

1. 生产认证仍未完成，JWT 和反向代理模式只是失败关闭的占位入口。
2. 默认 `DEV_HEADER` 仍允许 header roles，以保持当前 MVP 和测试可运行。
3. 生产环境必须在后续阶段移除 `X-User-Roles` 信任，并从数据库角色表解析授权。
4. 尚未接入 Spring Security 或任何外部 IdP。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| 前端 UI | 未修改 |
| JWT/OAuth 依赖 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. `SEC-007` 尚需移除生产环境中 `X-User-Roles` 的授权信任。
2. JWT 验签、issuer、audience、expiry 校验尚未实现。
3. 反向代理身份模式的可信网络边界和 header overwrite 规则尚未实现。
4. 前端内部身份选择器尚未替换为生产登录体验。

### 是否建议关闭本阶段

建议关闭 SEC-006。

关闭理由：认证模式配置、dev-header 适配器、生产候选模式失败关闭、单元测试、后端测试、架构文档和阶段记录均已完成；未删除文件，未新增 migration，未新增外部依赖、secrets、PDF/OCR 全文或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-007`：移除生产角色请求头信任，改为从 `app_user_role` 解析授权角色，同时保持测试和本地 bootstrap 路径可控。

## SEC-007

阶段名称：移除默认请求头角色信任

记录日期：2026-05-09

### 阶段目标

将 `X-User-Roles` 从默认可信授权路径中移除，使 Workspace 内授权默认只来自 `app_user_role` 和 `app_user_entity_scope`。在不接入外部 IdP、不新增 JWT/OAuth 依赖、不新增 migration、不改前端 UI 的前提下，保留显式配置的本地/测试兼容能力，并新增 bootstrap admin 用户白名单承接初始全局管理入口。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/sec-005-production-auth-boundary.md`、`docs/architecture/sec-006-trusted-principal-adapter.md`、`CurrentUserContextResolver`、`AuthorizationService`、认证配置和后端测试 |
| 允许修改 | 后端安全上下文、授权服务、认证配置、后端安全单元测试、SEC-007 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 删除文件、migration、前端 UI、PDF 原文、OCR 全文、外部 IdP/JWT/OAuth 接入、secrets、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git diff --check`、`git status --short`、后端边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/security/context/AuthProperties.java` | 将 `allowHeaderRoles` 默认值改为 `false`，新增 `bootstrapAdminUsers` 配置 |
| `backend/src/main/java/com/budgetplatform/security/service/AuthorizationService.java` | Workspace 角色默认只取持久化角色；请求头角色仅在显式允许时合并；全局引导入口支持 bootstrap admin 用户白名单 |
| `backend/src/main/resources/application.yml` | 将 `BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES` 默认值改为 `false`，新增 `BUDGET_PLATFORM_AUTH_BOOTSTRAP_ADMIN_USERS` |
| `backend/src/test/resources/application-test.yml` | 测试环境显式保留 header roles，并设置 `admin@example.com` 为 bootstrap admin |
| `backend/src/test/java/com/budgetplatform/security/service/AuthorizationServiceTests.java` | 新增授权服务单元测试，覆盖禁用请求头角色、bootstrap admin、显式启用 header roles |
| `docs/architecture/sec-007-remove-header-role-trust.md` | 新增 SEC-007 架构说明、配置、行为、非目标和验证结果 |
| `README.md` | 更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-007 阶段记录 |

### 关键产出

1. `budget-platform.auth.allow-header-roles` 生产默认值从 `true` 收紧为 `false`。
2. `AuthorizationService.rolesForWorkspace` 在默认配置下不再合并 `CurrentUserContext.roles()`。
3. Workspace 权限默认来自 `app_user_role`，Entity 范围仍来自 `app_user_entity_scope`。
4. `bootstrap-admin-users` 作为显式配置的全局引导管理员白名单，不自动授予 Workspace 角色。
5. 测试 profile 继续显式启用 header roles，避免为安全收敛阶段一次性重写所有既有集成测试夹具。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 45, Failures: 0, Errors: 0, Skipped: 0 |
| Flyway migration | 通过；H2 测试库成功验证并应用 7 个 migrations，未新增 migration |
| `git check-ignore` | 通过；PDF、OCR、前端依赖/构建产物、后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 SEC-007 后端安全配置、授权服务、测试、文档和阶段记录修改 |
| 后端边界关键词扫描 | 通过；`backend/src/main/java` 未发现 `@DeleteMapping`、ERP、Chart、BI 图表或合并报表实现 |

### 失败项与修复记录

1. 首轮后端测试通过，未出现编译或测试失败。
2. 阶段探索时 `rg.exe` 命中 Codex 打包路径并被 Windows 拒绝访问；已改用 PowerShell `Select-String` 进行源码检索，不影响实现和验证。

### 风险与限制

1. 生产认证仍未完成；`JWT` 与 `REVERSE_PROXY` 模式仍是 SEC-006 的失败关闭入口。
2. 默认禁用请求头角色后，非测试环境需要通过 `BUDGET_PLATFORM_AUTH_BOOTSTRAP_ADMIN_USERS` 指定初始全局管理员，或在受控本地验证中显式设置 `BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES=true`。
3. 前端仍会发送内部身份请求头；SEC-007 仅保证后端默认不信任角色头，前端登录边界需 SEC-008 继续推进。
4. 早期架构文档中关于请求头角色的描述已被 SEC-007 默认行为 supersede，后续可在文档治理阶段统一标注历史状态。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| 前端 UI | 未修改 |
| JWT/OAuth 依赖 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 生产 JWT/OIDC 或反向代理身份来源尚未真正实现。
2. 前端内部身份选择器尚未替换为生产登录或会话感知 UX。
3. bootstrap admin 白名单只是过渡机制，后续需要正式管理员初始化方案。

### 是否建议关闭本阶段

建议关闭 SEC-007。

关闭理由：默认角色头信任已移除，授权服务默认使用数据库角色，bootstrap admin 入口已显式配置，后端测试全部通过；未删除文件，未新增 migration，未提交 PDF/OCR 全文、secrets 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-008`：前端生产身份边界与内部身份选择器收敛。目标是在不实现完整登录/IdP 的前提下，把当前前端身份选择器明确限定为开发模式，并为生产认证接入预留稳定 UX 和 API 边界。

## SEC-008

阶段名称：前端生产身份边界与开发身份选择器收敛

记录日期：2026-05-09

### 阶段目标

让前端生产构建停止自动注入 `X-User-Id` 和 `X-User-Roles`，并隐藏内部开发身份选择器。保留 Vite dev 模式下的开发身份上下文用于本地 MVP 验证，但不把它伪装成生产登录系统。本阶段不实现 IdP、JWT、OAuth、密码登录、会话刷新、后端认证改造或 migration。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/sec-004-frontend-security-context.md`、`docs/architecture/sec-005-production-auth-boundary.md`、`docs/architecture/sec-007-remove-header-role-trust.md`、`frontend/src/shared/api/http.ts`、`frontend/src/App.tsx` |
| 允许修改 | 前端共享 HTTP 客户端、App 顶部身份控件、Vite 类型声明、SEC-008 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 删除文件、后端业务逻辑、migration、PDF 原文、OCR 全文、外部 IdP/JWT/OAuth 接入、secrets、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `pnpm type-check`、`pnpm lint`、`pnpm build`、`git check-ignore`、`git diff --check`、`git status --short`、前端边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `frontend/src/shared/api/http.ts` | 将内部身份请求头注入限制在 Vite dev 模式，并支持 `VITE_ENABLE_DEV_SECURITY_CONTEXT=false` 关闭 |
| `frontend/src/App.tsx` | 仅在 dev 身份上下文启用时展示内部 User/Role 选择器 |
| `frontend/src/vite-env.d.ts` | 新增 Vite 环境变量类型声明 |
| `docs/architecture/sec-008-frontend-auth-boundary.md` | 新增前端认证边界文档 |
| `README.md` | 更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-008 阶段记录 |

### 关键产出

1. 生产构建不再自动注入 `X-User-Id`。
2. 生产构建不再自动注入 `X-User-Roles`。
3. 内部 User/Role 选择器仅在 Vite dev 模式下渲染。
4. 开发模式可通过 `VITE_ENABLE_DEV_SECURITY_CONTEXT=false` 临时关闭内部身份注入。
5. 前端边界与 SEC-007 后端默认不信任角色头的策略保持一致。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过；Vite 生产构建成功，产物位于已忽略的 `frontend/dist` |
| `git check-ignore` | 通过；PDF、OCR、前端依赖/构建产物、后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 AUDIT-004 前端、文档和阶段记录修改 |
| 边界关键词扫描 | 通过；未发现审计导出、dashboard、ERP、Chart、BI 图表、合并报表、secrets 或 password；仅命中既有预算查询 CSV 导出 |
| `git check-ignore` | 通过；PDF、OCR、前端依赖/构建产物、后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 SEC-011 前端、文档和阶段记录修改 |
| 边界关键词扫描 | 通过；未发现 ERP、Chart、BI 图表、合并报表、secrets、password、新增删除或撤销实现；仅命中既有 `AuditAction.DELETE` 枚举 |
| `git check-ignore` | 通过；PDF、OCR、前端依赖/构建产物、后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 SEC-008 前端、文档和阶段记录修改 |
| 前端边界关键词扫描 | 通过；`frontend/src` 未发现 token/password/sessionStorage/localStorage、ERP、Chart、BI 图表或合并报表实现 |
| 生产 bundle 身份头检查 | 通过；`frontend/dist/assets/*.js` 不包含 `X-User-Id` 或 `X-User-Roles` 字符串，且 `frontend/dist` 被忽略 |

### 失败项与修复记录

1. 前端三项验证首轮通过，未出现类型、lint 或构建失败。
2. 阶段探索时前端目录列表误展开了 `node_modules`，后续只读取 `frontend/src` 和配置文件，未修改或删除依赖目录。

### 风险与限制

1. 正式生产登录仍未实现；生产构建停止注入身份头后，需要后续可信认证机制提供身份。
2. 开发模式默认仍保留内部身份选择器，以维持 MVP 手工验证效率。
3. 后端默认已不信任 `X-User-Roles`，本地非测试环境如需手工管理权限，需要配合 `BUDGET_PLATFORM_AUTH_BOOTSTRAP_ADMIN_USERS` 或显式允许 header roles。
4. 本阶段未改造为 `/api/security/me` 驱动的完整登录态 UX。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端业务逻辑 | 未修改 |
| migration | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 生产登录、会话、JWT/OIDC 或反向代理认证仍未实现。
2. 前端尚未基于 `/api/security/me` 展示可信当前用户。
3. Entity 范围维护仍缺少前端管理界面。

### 是否建议关闭本阶段

建议关闭 SEC-008。

关闭理由：生产构建身份头注入已关闭，开发身份选择器已限定为 dev 模式，前端 type-check、lint、build 全部通过；未删除文件，未新增 migration，未提交 PDF/OCR 全文、secrets 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-009`：会话与 Token 运维规则文档化。目标是在正式接入 JWT/OIDC 或反向代理认证前，先明确 token 生命周期、注销、时钟偏差、CORS/cookie、失败认证审计和密钥运维边界。

## SEC-009

阶段名称：会话与 Token 运维规则文档化

记录日期：2026-05-09

### 阶段目标

在不实现 JWT/OIDC、Spring Security、登录页、cookie session、secrets、外部身份服务或 migration 的前提下，定义后续生产认证必须遵守的 token 生命周期、校验规则、注销、时钟偏差、CORS/cookie、失败认证审计和密钥轮换边界，避免未来实现阶段临时拼接认证行为。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `docs/architecture/sec-005-production-auth-boundary.md`、`docs/architecture/sec-006-trusted-principal-adapter.md`、`docs/architecture/sec-007-remove-header-role-trust.md`、`docs/architecture/sec-008-frontend-auth-boundary.md`、审计阶段文档、`README.md` |
| 允许修改 | `docs/architecture/sec-009-session-token-operations.md`、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 业务代码、前端实现、migration、PDF 原文、OCR 全文、JWT/OAuth 依赖、外部 IdP、secrets、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未写代码，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/sec-009-session-token-operations.md` | 新增会话与 Token 运维规则文档 |
| `README.md` | 更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-009 阶段记录 |

### 关键产出

1. 明确未来 JWT/OIDC/反向代理认证模式的会话载体与后端职责。
2. 明确 token 校验规则：签名、issuer、audience、expiry、nbf、subject、算法和大小限制。
3. 明确推荐起点：60 秒 clock skew、5 到 15 分钟 access token 生命周期、JWKS 缓存与 key miss 刷新策略。
4. 明确浏览器 session 规则：不把 bearer token 存入 `localStorage`/`sessionStorage`，cookie 需 `HttpOnly`、`Secure` 和 SameSite。
5. 明确失败认证审计事件与禁止记录 raw token/secrets/大段 IdP payload。
6. 明确密钥轮换和缺失配置 fail-closed 原则。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| 文档阶段 | 未运行后端/前端测试；本阶段未修改代码 |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、前端构建/依赖、后端构建目录均被忽略 |
| `git diff --check` | 通过；仅提示 `PROJECT_STEP_RECORD.md` 与 `README.md` 在当前工作副本下 LF 后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 `PROJECT_STEP_RECORD.md`、`README.md` 和 `docs/architecture/auth-001-production-auth-implementation-plan.md` |
| 后端/前端边界关键词扫描 | 仅命中既有 `AuthMode.JWT` 和 `CurrentUserContextResolver` 的 JWT 失败关闭占位；本阶段未修改代码 |
| `git check-ignore` | 通过；PDF、OCR、前端依赖/构建产物、后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 OPS-001 runbook、README 和阶段记录修改 |
| 源码边界关键词扫描 | 通过；`backend/src/main/java` 和 `frontend/src` 未发现 ERP、Chart、BI 图表、合并报表、secrets 或 password；仅命中 SEC-006 既有 `JWT` 失败关闭占位 |
| `git check-ignore` | 通过；PDF、OCR、前端依赖/构建产物、后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 SEC-010 文档、README 和阶段记录修改 |
| 源码边界关键词扫描 | 通过；`backend/src/main/java` 和 `frontend/src` 未发现 ERP、Chart、BI 图表、合并报表、secrets、password 或删除接口 |
| 历史文档状态标注检查 | 通过；10 份 SEC/AUDIT 历史文档均已增加 `当前状态说明` 或 `Current Status` |
| `git check-ignore` | 通过；PDF、OCR、前端依赖/构建产物、后端 `target` 均被忽略 |
| `git diff --check` | 通过；仅出现 Git 对 LF/CRLF 的换行提示，无空白错误 |
| `git status --short` | 通过；仅 SEC-009 文档、README 和阶段记录修改 |
| 源码边界关键词扫描 | 通过；`backend/src/main/java` 和 `frontend/src` 未发现 ERP、Chart、BI 图表、合并报表、secrets 或删除接口；仅命中 SEC-006 既有 `JWT` 失败关闭占位 |

### 失败项与修复记录

1. 本阶段为文档治理阶段，未出现验证失败。

### 风险与限制

1. SEC-009 只定义运维规则，不实现真实生产认证。
2. 未来接入 JWT/OIDC 或反向代理身份时，仍需实现后端校验、前端会话 UX、失败认证审计写入和密钥配置。
3. 当前项目仍不能在生产环境依赖 dev-header 模式。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 业务代码 | 未修改 |
| 前端实现 | 未修改 |
| migration | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 生产认证实现阶段尚未开始。
2. 失败认证事件尚未写入 `audit_event`。
3. `/api/security/me` 尚未成为前端生产登录态入口。

### 是否建议关闭本阶段

建议关闭 SEC-009。

关闭理由：会话与 Token 运维边界已形成文档，README 和阶段记录已更新；本阶段未写代码、未删除文件、未新增 migration、未提交 PDF/OCR 全文、secrets 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-010`：安全文档一致性收敛。目标是标注 SEC-001 至 SEC-004 中请求头角色方案的历史状态，避免后续阅读者误把早期 MVP header roles 当成当前默认生产策略。

## SEC-010

阶段名称：安全文档一致性收敛

记录日期：2026-05-09

### 阶段目标

在不修改代码、不新增 migration、不删除文件的前提下，标注早期 SEC 与 AUDIT 文档中请求头角色方案的历史状态，明确当前默认行为已由 SEC-007、SEC-008 和 SEC-009 收敛，避免后续团队误把 `X-User-Roles` 当作当前默认生产授权策略。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `SEC-001` 至 `SEC-009` 架构文档、`AUDIT-002`、`README.md`、`PROJECT_STEP_RECORD.md` |
| 允许修改 | 安全与审计架构文档状态说明、`docs/architecture/sec-010-security-document-consistency.md`、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 后端业务代码、前端实现、migration、PDF 原文、OCR 全文、JWT/OAuth 依赖、secrets、外部服务、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `git check-ignore`、`git diff --check`、`git status --short`、源码边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未写代码，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/sec-001-security-scope-design.md` | 标注早期请求头角色为历史 MVP 方案 |
| `docs/architecture/sec-002-security-backend-baseline.md` | 标注当前默认策略已由 SEC-006/SEC-007/SEC-008 收敛 |
| `docs/architecture/sec-003-backend-authorization-entry-points.md` | 标注 Workspace 角色默认来自持久化角色，bootstrap admin 为引导入口 |
| `docs/architecture/sec-003b-metadata-authorization.md` | 标注元数据引导管理的当前策略 |
| `docs/architecture/sec-003c-budget-model-authorization.md` | 标注预算模型授权当前策略 |
| `docs/architecture/sec-003d-budget-template-authorization.md` | 标注预算模板授权当前策略 |
| `docs/architecture/sec-003e-budget-submission-authorization.md` | 标注填报授权当前策略 |
| `docs/architecture/sec-003f-actual-import-authorization.md` | 标注实际数导入授权当前策略 |
| `docs/architecture/sec-004-frontend-security-context.md` | 标注 SEC-008 已 supersede 生产构建行为 |
| `docs/architecture/audit-002-audit-query-api.md` | 标注审计查询请求头管理员规则已被 SEC-007 默认策略 supersede |
| `docs/architecture/sec-010-security-document-consistency.md` | 新增一致性收敛说明 |
| `README.md` | 更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-010 阶段记录 |

### 关键产出

1. 早期请求头角色方案保留为阶段历史，不再被误读为当前默认策略。
2. 明确当前默认策略：后端不默认信任 `X-User-Roles`，前端生产构建不自动发送身份/角色请求头。
3. 明确 Workspace 角色来自 `app_user_role`，Entity 范围来自 `app_user_entity_scope`。
4. 明确 JWT/OIDC/反向代理仍是后续实现阶段，必须遵守 SEC-009 运维规则。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| 文档阶段 | 未运行后端/前端测试；本阶段未修改代码 |

### 失败项与修复记录

1. 本阶段为文档治理阶段，未出现验证失败。

### 风险与限制

1. SEC-010 只做文档一致性，不实现生产认证。
2. 早期文档仍保留历史实现描述；读者需要参考新增当前状态说明和 SEC-007/SEC-008/SEC-009。
3. 生产登录、失败认证审计和 `/api/security/me` 前端生产体验仍需后续阶段。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 业务代码 | 未修改 |
| 前端实现 | 未修改 |
| migration | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 正式生产认证仍未实现。
2. Entity 范围维护仍缺少前端管理界面。
3. 审计查询前端体验仍未建设。

### 是否建议关闭本阶段

建议关闭 SEC-010。

关闭理由：早期安全文档已补充当前状态说明，SEC-010 一致性文档、README 和阶段记录已更新；本阶段未写代码、未删除文件、未新增 migration、未提交 PDF/OCR 全文、secrets 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-011`：前端 Entity 范围管理 MVP。目标是在现有安全 API 范围内，为用户角色与 Entity 范围维护提供最小前端入口，补齐当前安全治理中的实际操作缺口。

## SEC-011

阶段名称：前端 Entity 范围管理 MVP

记录日期：2026-05-09

### 阶段目标

基于现有 `/api/security` 后端 API，在前端补齐安全用户、Workspace 角色和 Entity 范围授权的最小管理入口。阶段范围只做新增、查询和授权，不做删除/撤销、不改后端、不新增 migration、不引入复杂多维权限矩阵、不实现生产登录。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `SecurityController` API、`CreateSecurityUserRequest`、`GrantUserRoleRequest`、`GrantEntityScopeRequest`、现有 `App.tsx`、`metadataApi.ts`、SEC-007/SEC-008 文档 |
| 允许修改 | 前端安全 API 客户端、`App.tsx` 安全管理面板、`styles.css`、SEC-011 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 删除文件、后端业务逻辑、migration、PDF 原文、OCR 全文、JWT/OAuth 依赖、secrets、外部服务、ERP 直连、BI 图表、合并报表、授权撤销/删除 |
| 验证命令 | `pnpm type-check`、`pnpm lint`、`pnpm build`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `frontend/src/features/security/securityApi.ts` | 新增 typed client，封装用户、角色、Entity 范围管理 API |
| `frontend/src/App.tsx` | 新增安全管理面板：用户创建/选择、角色授权、Entity 范围授权和列表 |
| `frontend/src/styles.css` | 新增安全管理面板布局、表单和紧凑表格样式 |
| `docs/architecture/sec-011-frontend-entity-scope-management.md` | 新增 SEC-011 架构说明 |
| `README.md` | 更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-011 阶段记录 |

### 关键产出

1. 前端新增 `/api/security` typed client。
2. 前端支持创建安全用户并选择当前管理用户。
3. 前端支持给选中用户授予当前 Workspace 下的角色。
4. 前端支持给选中用户授予当前 Workspace 下的 Entity 成员范围。
5. 前端展示选中用户在当前 Workspace 的角色和 Entity 范围列表。
6. 本阶段没有实现删除、撤销、禁用或复杂权限矩阵。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过；Vite 生产构建成功，产物位于已忽略的 `frontend/dist` |

### 失败项与修复记录

1. 前端三项验证首轮通过，未出现类型、lint 或构建失败。
2. 本阶段未修改后端代码，因此未运行 `mvn test`。

### 风险与限制

1. 后端默认不信任 `X-User-Roles`；本地手工测试需要配置 bootstrap admin 或显式启用受控 header roles。
2. 没有撤销/删除入口；重复授权会由后端唯一约束返回错误。
3. Entity 范围选择依赖当前 Workspace 的 Entity 成员已加载。
4. 本阶段不是生产登录实现，仍依赖后续可信认证阶段。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端业务逻辑 | 未修改 |
| migration | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 授权撤销、用户禁用和批量授权尚未设计。
2. 生产登录仍未实现。
3. 审计查询前端体验仍未建设。

### 是否建议关闭本阶段

建议关闭 SEC-011。

关闭理由：安全用户、角色和 Entity 范围的最小前端管理入口已实现，前端 type-check、lint、build 全部通过；未删除文件，未改后端业务逻辑，未新增 migration，未提交 PDF/OCR 全文、secrets 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `AUDIT-004`：审计查询前端 MVP。目标是复用现有只读审计 API，提供最小筛选与分页查看能力，不做 BI 仪表盘、导出或复杂报表。

## AUDIT-004

阶段名称：审计查询前端 MVP

记录日期：2026-05-09

### 阶段目标

复用现有 `/api/audit/events` 只读审计查询 API，在前端提供最小筛选、分页和结果查看能力。本阶段不新增后端接口、不新增 migration、不做 CSV 导出、不做 BI 图表或仪表盘、不做审计删除/清理、不做失败认证审计写入。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AuditController`、`AuditEventResponse`、`PageResponse`、`AUDIT-002`、`AUDIT-003`、现有 `App.tsx` |
| 允许修改 | 前端审计 API 客户端、`App.tsx` 审计查询面板、`styles.css`、AUDIT-004 架构文档、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 删除文件、后端业务逻辑、migration、PDF 原文、OCR 全文、JWT/OAuth 依赖、secrets、外部服务、ERP 直连、BI 图表、合并报表、审计导出/删除 |
| 验证命令 | `pnpm type-check`、`pnpm lint`、`pnpm build`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `frontend/src/features/audit/auditApi.ts` | 新增 typed audit query client |
| `frontend/src/App.tsx` | 新增只读审计筛选、分页和结果表 |
| `frontend/src/styles.css` | 新增审计查询布局和表单样式 |
| `docs/architecture/audit-004-frontend-audit-query.md` | 新增 AUDIT-004 架构说明 |
| `README.md` | 更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 AUDIT-004 阶段记录 |

### 关键产出

1. 前端新增 `/api/audit/events` typed client。
2. 支持按 actor、subject type、subject id、action 筛选。
3. 支持上一页/下一页分页查看。
4. 展示审计时间、actor、subject、action 和 details JSON。
5. 保持只读，不提供导出、删除、清理或 BI 图表。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过；Vite 生产构建成功，产物位于已忽略的 `frontend/dist` |

### 失败项与修复记录

1. 前端三项验证首轮通过，未出现类型、lint 或构建失败。
2. 本阶段未修改后端代码，因此未运行 `mvn test`。

### 风险与限制

1. 审计查询访问仍依赖后端既有授权与当前认证模式。
2. `detailsJson` 暂以文本展示，未做结构化报表或字段级解析。
3. 未提供导出、保留策略、归档或失败认证事件写入。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端业务逻辑 | 未修改 |
| migration | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| 审计导出/删除 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 失败认证审计仍未实现。
2. 审计导出和保留/归档策略仍未进入阶段范围。
3. 生产登录仍未实现。

### 是否建议关闭本阶段

建议关闭 AUDIT-004。

关闭理由：只读审计查询前端、typed API client、分页和筛选已完成，前端 type-check、lint、build 全部通过；未删除文件，未改后端业务逻辑，未新增 migration，未提交 PDF/OCR 全文、secrets 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `OPS-001`：本地运行与验证手册。目标是沉淀当前后端、前端、认证 bootstrap、PDF/OCR 保护和常用测试命令，降低后续 24 小时巡视时的交接成本。

## OPS-001

阶段名称：本地运行与验证手册

记录日期：2026-05-09

### 阶段目标

沉淀当前项目的本地运行、测试、认证 bootstrap、资料保护和日常巡视检查步骤，降低后续自主推进和 24 小时巡视时的交接成本。本阶段只写运维文档，不修改业务代码、不新增 migration、不访问外部服务、不写 secrets。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `README.md`、`application.yml`、`application-test.yml`、`frontend/package.json`、`.gitignore`、`PROJECT_STEP_RECORD.md` |
| 允许修改 | `docs/architecture/ops-001-local-runbook.md`、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 后端业务代码、前端实现、migration、PDF 原文、OCR 全文、JWT/OAuth 依赖、secrets、外部服务、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未写代码，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/ops-001-local-runbook.md` | 新增本地运行与日常巡视手册 |
| `README.md` | 增加 runbook 引用并更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 OPS-001 阶段记录 |

### 关键产出

1. 明确仓库位置、分支、remote 和基础 Git 检查命令。
2. 明确后端测试、前端 type-check/lint/build 命令。
3. 明确本地认证 bootstrap 配置：`BUDGET_PLATFORM_AUTH_BOOTSTRAP_ADMIN_USERS` 和受控 `BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES`。
4. 明确前端 dev identity 开关：`VITE_ENABLE_DEV_SECURITY_CONTEXT=false`。
5. 明确 PDF/OCR、构建产物、依赖目录和 secrets 不得提交。
6. 明确常见 401/403、PDF 出现在 Git、Flyway 失败等问题的第一检查点。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| 文档阶段 | 未运行后端/前端测试；本阶段未修改代码 |

### 失败项与修复记录

1. 本阶段为文档治理阶段，未出现验证失败。

### 风险与限制

1. OPS-001 是本地运行与巡视手册，不是生产部署手册。
2. 生产认证、授权撤销、失败认证审计仍未实现。
3. Docker 当前不是本项目本地验证的必需项。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端业务代码 | 未修改 |
| 前端实现 | 未修改 |
| migration | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 生产部署手册尚未建立。
2. 正式登录实现、授权撤销设计和失败认证审计仍需后续阶段。

### 是否建议关闭本阶段

建议关闭 OPS-001。

关闭理由：本地运行、验证、认证 bootstrap、资料保护和巡视检查步骤已沉淀；本阶段未写代码、未删除文件、未新增 migration、未提交 PDF/OCR 全文、secrets 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `AUTH-001`：生产认证实现方案拆分。目标是基于 SEC-005 至 SEC-009 形成可实施的 JWT/OIDC 与反向代理二选一路线、风险清单和最小实现拆分；只有设计确认后再进入代码实现。

## AUTH-001

阶段名称：生产认证实现方案拆分

记录日期：2026-05-09

### 阶段目标

基于 SEC-005、SEC-006、SEC-007、SEC-008 和 SEC-009，将生产认证从“边界原则”推进到“可执行实施路线”。本阶段只做文档设计，不实现 JWT/OIDC、不实现反向代理适配代码、不新增依赖、不修改后端或前端业务代码、不新增 migration、不访问外部服务、不写 secrets。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `sec-005-production-auth-boundary.md`、`sec-006-trusted-principal-adapter.md`、`sec-007-remove-header-role-trust.md`、`sec-008-frontend-auth-boundary.md`、`sec-009-session-token-operations.md`、现有认证代码状态 |
| 允许修改 | `docs/architecture/auth-001-production-auth-implementation-plan.md`、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 后端代码、前端代码、migration、PDF 原文、OCR 全文、JWT/OAuth 依赖、secrets、外部服务、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未写代码，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/auth-001-production-auth-implementation-plan.md` | 新增生产认证实施路线与阶段拆分 |
| `README.md` | 增加 AUTH-001 文档入口并更新当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 AUTH-001 阶段记录 |

### 关键产出

1. 对比反向代理可信身份与 JWT/OIDC bearer 校验两条生产认证路线。
2. 建议优先实现 `AUTH-002A` 反向代理可信身份模式；当部署环境不具备可信网关时，再进入 `AUTH-002B` JWT/OIDC bearer 校验。
3. 明确 Workspace 角色和 Entity 范围继续由平台表维护，不从 token 或 IdP group 直接推导预算授权。
4. 拆分后续最小阶段：`AUTH-002A`、`AUTH-002B`、`AUTH-003`、`AUTH-004`、`AUTH-005`、`AUTH-006`。
5. 明确本阶段不写代码、不加依赖、不接外部服务、不加 secrets。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| 文档阶段 | 未运行后端/前端测试；本阶段未修改代码 |

### 失败项与修复记录

1. 本阶段为认证方案拆分阶段，未出现验证失败。

### 风险与限制

1. AUTH-001 不是生产认证代码实现；`JWT` 和 `REVERSE_PROXY` 运行模式仍需后续实现。
2. 反向代理路线依赖网关剥离/覆盖身份请求头，并要求后端不可被浏览器绕过直连。
3. JWT/OIDC 路线会引入依赖、密钥轮换和 IdP 配置复杂度，需独立阶段实现与测试。
4. 失败认证审计和授权撤销仍未实现。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端代码 | 未修改 |
| 前端代码 | 未修改 |
| migration | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. `AUTH-002A` 需要实现 `REVERSE_PROXY` 模式并补充测试。
2. `AUTH-002B` 需要在实际需要时选择 JWT/OIDC 验证依赖并设计测试密钥。
3. `/api/security/me`、失败认证审计、生产部署 runbook 仍需后续阶段。

### 是否建议关闭本阶段

建议关闭 AUTH-001。

关闭理由：生产认证两条路线、推荐顺序、后续最小阶段、非目标和风险控制已沉淀；本阶段未删除文件，未修改后端/前端代码，未新增 migration，未提交 PDF/OCR 全文、secrets 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `AUTH-002A`：反向代理可信身份后端适配。目标是在不引入 JWT/OIDC 依赖、不修改前端登录、不新增 migration 的前提下，实现 `REVERSE_PROXY` 模式的可信 principal 解析和测试。

## AUTH-002A

阶段名称：反向代理可信身份后端适配

记录日期：2026-05-09

### 阶段目标

实现 `REVERSE_PROXY` 认证模式的最小后端适配：从配置的可信反向代理头读取 principal，缺失或空值时失败关闭，并在该模式下继续忽略客户端传入的角色头。本阶段不实现 JWT/OIDC、不新增依赖、不修改前端登录、不新增 migration、不接外部服务、不写 secrets。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `auth-001-production-auth-implementation-plan.md`、`CurrentUserContextResolver`、`AuthProperties`、现有 resolver 测试 |
| 允许修改 | 后端认证 resolver 与测试、`docs/architecture/auth-002a-reverse-proxy-auth-adapter.md`、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 前端登录、JWT/OIDC 依赖、migration、PDF 原文、OCR 全文、secrets、外部服务、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/security/context/CurrentUserContextResolver.java` | 实现 `REVERSE_PROXY` 模式读取当前 HTTP request 的可信 principal header |
| `backend/src/test/java/com/budgetplatform/security/context/CurrentUserContextResolverTests.java` | 增加反向代理 principal 成功、缺失、空值、配置空白和角色头忽略测试 |
| `docs/architecture/auth-002a-reverse-proxy-auth-adapter.md` | 新增阶段架构说明 |
| `README.md` | 更新当前治理状态和 AUTH-002A 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 AUTH-002A 阶段记录 |

### 关键产出

1. `REVERSE_PROXY` 模式不再失败关闭，而是读取 `budget-platform.auth.reverse-proxy-user-header` 指定的可信 header。
2. 缺失 principal、空白 principal、空白配置 header 名均返回 `UNAUTHORIZED`。
3. `REVERSE_PROXY` 模式忽略 `X-User-Id` 和 `X-User-Roles` 这类客户端身份/角色输入。
4. 角色和 Entity 范围仍由平台持久化授权模型控制。
5. 未引入 JWT/OIDC 依赖，未修改数据库结构。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `rg -n "CurrentUserContextResolver|AuthProperties|REVERSE_PROXY|allowHeaderRoles|X-User-Roles" backend\src\test backend\src\main` | 失败；Codex 内嵌 `rg.exe` 启动时报 `拒绝访问`，已改用 PowerShell `Select-String` 完成检索 |
| `mvn test` | 通过；Tests run: 49, Failures: 0, Errors: 0, Skipped: 0，BUILD SUCCESS |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示多个工作副本文件 LF 后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 AUTH-002A 相关后端 resolver/test、文档、README 和阶段记录 |
| 后端/前端边界关键词扫描 | 仅命中既有 `AuthMode.JWT` 与 `CurrentUserContextResolver` JWT 失败关闭占位；未新增 JWT/OAuth 依赖、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. `rg` 工具不可用，真实错误为 `Program 'rg.exe' failed to run ... 拒绝访问`；已使用 PowerShell `Get-ChildItem` + `Select-String` 继续定位。

### 风险与限制

1. 如果后端被浏览器或外部客户端绕过网关直连，反向代理 header 仍可能被伪造。
2. 本阶段不做可信代理 IP/CIDR 校验。
3. 本阶段不做 JWT/OIDC bearer 校验。
4. 失败认证审计仍未实现。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 前端登录 | 未修改 |
| migration | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. `AUTH-002B` JWT/OIDC bearer 校验尚未实现。
2. 失败认证审计尚未实现。
3. 生产部署需要确保网关剥离/覆盖身份 header，并禁止外部直连后端。

### 是否建议关闭本阶段

建议关闭 AUTH-002A。

关闭理由：`REVERSE_PROXY` 模式已实现可信 principal header 解析和失败关闭，角色头在该模式下不被信任；后端 `mvn test` 通过，仓库保护检查通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文、secrets 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `AUTH-004`：前端生产当前用户边界收敛。由于 `/api/security/me` 已存在，目标是确认前端生产构建只展示后端可信当前用户，不重新引入开发身份注入或角色覆盖；如发现前端已满足，则以文档和测试方式关闭该阶段。`AUTH-005` 失败认证审计可随后进入。

## AUTH-003

阶段名称：当前用户 API 与审计 actor 信任收敛

记录日期：2026-05-09

### 阶段目标

补齐 `/api/security/me` 的生产可信用户契约，使其返回认证模式、应用内 Workspace 角色和 Entity 范围摘要；同时验证反向代理模式下审计 actor 来自可信代理 principal，而不是客户端伪造的身份/角色头。本阶段不实现 JWT/OIDC、不修改前端登录、不新增 migration、不接外部服务、不写 secrets。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `auth-001-production-auth-implementation-plan.md`、`auth-002a-reverse-proxy-auth-adapter.md`、`SecurityController`、`SecurityService`、现有安全集成测试 |
| 允许修改 | 当前用户响应 DTO、认证上下文、SecurityService、后端测试、AUTH-003 文档、README、PROJECT_STEP_RECORD |
| 禁止修改 | 前端登录、JWT/OIDC 依赖、migration、PDF 原文、OCR 全文、secrets、外部服务、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/security/context/CurrentUserContext.java` | 增加 `authMode` 字段并保留兼容构造函数 |
| `backend/src/main/java/com/budgetplatform/security/context/CurrentUserContextResolver.java` | 在 dev header 与 reverse proxy 模式下写入 auth mode |
| `backend/src/main/java/com/budgetplatform/security/api/CurrentUserResponse.java` | 增加 `authMode`、`applicationRoles`、`entityScopes` |
| `backend/src/main/java/com/budgetplatform/security/service/SecurityService.java` | `/api/security/me` 汇总当前用户应用角色和 Entity 范围 |
| `backend/src/test/java/com/budgetplatform/security/context/CurrentUserContextResolverTests.java` | 增加 auth mode 断言 |
| `backend/src/test/java/com/budgetplatform/security/api/SecurityControllerReverseProxyIntegrationTests.java` | 新增反向代理 `/me` 与审计 actor 集成测试 |
| `docs/architecture/auth-003-current-user-actor-trust.md` | 新增 AUTH-003 架构说明 |
| `README.md` | 更新当前治理状态和 AUTH-003 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 AUTH-003 阶段记录 |

### 关键产出

1. `/api/security/me` 返回 `authMode`，帮助前端区分 `DEV_HEADER` 与 `REVERSE_PROXY`。
2. `/api/security/me` 返回持久化 `applicationRoles` 和 `entityScopes`，为生产当前用户展示提供后端可信摘要。
3. 反向代理模式下 `X-User-Id` 与 `X-User-Roles` 不影响 `/me` 响应。
4. 反向代理模式下创建安全用户的审计 actor 被验证为 `X-Trusted-User` principal，而不是客户端伪造的 `X-User-Id`。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` 首轮 | 失败；`SecurityService.java:[154,50] 找不到符号 符号: 变量 Set` |
| 最小修复 | 已补充 `java.util.Set` import |
| `mvn test` 第二轮 | 通过；Tests run: 52, Failures: 0, Errors: 0, Skipped: 0，BUILD SUCCESS |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示多个工作副本文件 LF 后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 AUTH-003 相关后端安全上下文、当前用户响应、服务、测试、文档、README 和阶段记录 |
| 后端/前端边界关键词扫描 | 仅命中既有 `AuthMode.JWT` 与 `CurrentUserContextResolver` JWT 失败关闭占位；未新增 JWT/OAuth 依赖、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 首轮 `mvn test` 编译失败，真实错误为 `SecurityService.java:[154,50] 找不到符号 符号: 变量 Set`。
2. 修复方式：补充 `import java.util.Set;`。
3. 第二轮 `mvn test` 通过，未发现新的测试失败。

### 风险与限制

1. 未注册但已认证的 principal 会返回空应用角色和空 Entity 范围摘要。
2. 本阶段不实现失败认证审计。
3. 本阶段不做 JWT/OIDC bearer 校验。
4. 前端生产当前用户展示仍需后续阶段收敛。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 前端登录 | 未修改 |
| migration | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 失败认证审计尚未实现。
2. JWT/OIDC bearer 校验尚未实现。
3. 前端需要在生产构建中消费 `/api/security/me` 的可信字段。

### 是否建议关闭本阶段

建议关闭 AUTH-003。

关闭理由：`/api/security/me` 已返回 auth mode、应用角色和 Entity 范围摘要；反向代理模式下当前用户与审计 actor 已通过集成测试验证；后端全量测试通过，仓库保护检查通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文、secrets 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `AUTH-004`：前端生产当前用户边界收敛。目标是让前端读取 `/api/security/me` 的可信字段并展示当前用户/auth mode，同时保持生产构建不注入开发身份或角色覆盖。

## AUTH-004

阶段名称：前端当前用户边界收敛

记录日期：2026-05-09

### 阶段目标

让前端读取后端 `/api/security/me` 的可信当前用户契约，并展示 userId、authMode、应用角色数量和 Entity 范围数量；同时保持开发身份选择器只在 Vite dev 模式启用，不在生产构建中注入 `X-User-Id` 或 `X-User-Roles`。本阶段不实现登录、登出、JWT/OIDC token、cookie session、不新增 migration、不接外部服务、不写 secrets。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `auth-003-current-user-actor-trust.md`、`sec-008-frontend-auth-boundary.md`、`frontend/src/shared/api/http.ts`、`frontend/src/App.tsx` |
| 允许修改 | 前端安全 API client、App 当前用户展示、样式、AUTH-004 文档、README、PROJECT_STEP_RECORD |
| 禁止修改 | 后端代码、JWT/OIDC 依赖、登录页、token 存储、migration、PDF 原文、OCR 全文、secrets、外部服务、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `pnpm type-check`、`pnpm lint`、`pnpm build`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `frontend/src/features/security/securityApi.ts` | 新增 `CurrentUser` 类型和 `getCurrentUser()` |
| `frontend/src/App.tsx` | 新增当前用户状态、刷新逻辑和顶部可信用户摘要 |
| `frontend/src/styles.css` | 新增当前用户摘要布局 |
| `docs/architecture/auth-004-frontend-current-user-boundary.md` | 新增 AUTH-004 架构说明 |
| `README.md` | 更新当前治理状态和 AUTH-004 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 AUTH-004 阶段记录 |

### 关键产出

1. 前端通过 `getCurrentUser()` 调用 `/api/security/me`。
2. 顶部展示后端可信 `userId`、`authMode`、应用角色数量和 Entity 范围数量。
3. 生产构建仍不会自动注入 `X-User-Id` 或 `X-User-Roles`。
4. 开发身份选择器仍仅在 dev security context 启用时显示。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过；Vite 生产构建成功，产物位于已忽略的 `frontend/dist` |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示多个工作副本文件 LF 后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 AUTH-004 相关前端代码、文档、README 和阶段记录 |
| 边界关键词扫描 | 命中既有 `AuthMode.JWT`、`CurrentUserContextResolver` JWT 失败关闭占位，以及前端 `CurrentUser.authMode` 的类型枚举；未新增 JWT/OAuth 依赖、token 存储、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 前端三项验证首轮通过，未出现类型、lint 或构建失败。
2. 本阶段未修改后端代码，因此未运行 `mvn test`。

### 风险与限制

1. 本阶段不是登录/登出实现。
2. 如果后端返回 401，仍由现有错误条展示。
3. 当前用户授权信息以数量摘要展示，未展开复杂权限矩阵。
4. 失败认证审计仍未实现。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端代码 | 未修改 |
| migration | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| token 存储 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 失败认证审计尚未实现。
2. JWT/OIDC bearer 校验尚未实现。
3. 正式登录/登出流程尚未实现。

### 是否建议关闭本阶段

建议关闭 AUTH-004。

关闭理由：前端已消费 `/api/security/me` 并展示后端可信当前用户摘要；生产构建仍不注入开发身份头；前端 type-check、lint、build 通过，仓库保护检查通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文、secrets 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `AUTH-005`：失败认证审计。目标是在不存储 raw token、secrets 或 PII-heavy payload 的前提下，记录缺失 principal、可信头缺失、未实现 JWT 模式等认证失败类别。

## AUTH-005

阶段名称：失败认证审计

记录日期：2026-05-09

### 阶段目标

在不新增 migration、不存储 raw token、不写 secrets、不接外部身份服务的前提下，为认证解析失败记录低敏安全审计事件。本阶段只覆盖当前 resolver 边界内的失败认证类别，不实现 JWT/OIDC bearer 校验，不做登录/登出。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `sec-009-session-token-operations.md`、`auth-004-frontend-current-user-boundary.md`、`CurrentUserContextResolver`、现有 audit 模型 |
| 允许修改 | 审计 action enum、认证 resolver、相关后端测试、前端 audit action 类型/选项、AUTH-005 文档、README、PROJECT_STEP_RECORD |
| 禁止修改 | migration、JWT/OIDC 依赖、token 存储、secrets、外部服务、PDF 原文、OCR 全文、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/common/audit/AuditAction.java` | 新增 `AUTH_FAILURE` |
| `backend/src/main/java/com/budgetplatform/security/context/CurrentUserContextResolver.java` | 认证失败时记录低敏审计事件 |
| `backend/src/test/java/com/budgetplatform/security/api/SecurityControllerReverseProxyIntegrationTests.java` | 验证反向代理缺失 principal 会写入失败认证审计 |
| `frontend/src/features/audit/auditApi.ts` | 增加 `AUTH_FAILURE` 类型 |
| `frontend/src/App.tsx` | 审计筛选 action 增加 `AUTH_FAILURE` |
| `docs/architecture/auth-005-failed-authentication-audit.md` | 新增 AUTH-005 架构说明 |
| `README.md` | 更新当前治理状态和 AUTH-005 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 AUTH-005 阶段记录 |

### 关键产出

1. 认证失败以 `subjectType=authentication`、`subjectId=failure`、`action=AUTH_FAILURE` 写入既有 audit 表。
2. 失败详情只包含 reason、message、authMode、headerName、method、path、requestId 等低敏字段。
3. 反向代理可信头缺失会记录 `MISSING_REVERSE_PROXY_PRINCIPAL`。
4. JWT 模式未实现会记录 `JWT_NOT_IMPLEMENTED`。
5. 前端审计筛选支持 `AUTH_FAILURE`。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 52, Failures: 0, Errors: 0, Skipped: 0，BUILD SUCCESS |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过；Vite 生产构建成功，产物位于已忽略的 `frontend/dist` |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示多个工作副本文件 LF 后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 AUTH-005 相关后端认证/审计、前端审计类型、文档、README 和阶段记录 |
| 边界关键词扫描 | 命中既有 `AuthMode.JWT`、`CurrentUserContextResolver` JWT 失败关闭占位，以及前端 `CurrentUser.authMode` 的类型枚举；未新增 OAuth 依赖、token 存储、Bearer 处理、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 后端与前端验证首轮通过，未出现测试、类型、lint 或构建失败。

### 风险与限制

1. 本阶段记录当前 resolver 边界内的认证失败，不覆盖所有授权失败。
2. JWT/OIDC bearer 校验仍未实现，因此 JWT 失败只覆盖“模式未实现”的失败类别。
3. 失败认证事件与业务审计共用既有 audit 表，后续可再设计保留与归档策略。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| token 存储 | 未新增 |
| raw token 审计 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. JWT/OIDC bearer 校验尚未实现。
2. 授权撤销/禁用用户流程尚未实现。
3. 审计保留、归档和安全事件告警仍未设计。

### 是否建议关闭本阶段

建议关闭 AUTH-005。

关闭理由：认证失败审计已覆盖当前 resolver 边界，反向代理缺失 principal 的失败审计已由集成测试验证；后端测试、前端 type-check/lint/build 和仓库保护检查均通过，未删除文件，未新增 migration，未提交 PDF/OCR 全文、secrets、raw token 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `AUTH-006`：部署与密钥运维手册。目标是沉淀 `DEV_HEADER`、`REVERSE_PROXY` 和未来 JWT/OIDC 的环境变量、网关 header 覆盖、CORS/TLS、日志脱敏、回滚与检查清单；仍不实现 JWT/OIDC 代码。

## AUTH-006

阶段名称：部署与密钥运维手册

记录日期：2026-05-09

### 阶段目标

沉淀生产认证部署与运维控制：认证模式矩阵、环境变量、反向代理 header 覆盖、未来 JWT/OIDC 配置、CORS/TLS、日志脱敏、回滚和日常巡检。本阶段只写文档，不修改运行时代码、不新增 migration、不新增依赖、不写 secrets、不访问外部服务。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `auth-005-failed-authentication-audit.md`、`ops-001-local-runbook.md`、`sec-009-session-token-operations.md` |
| 允许修改 | `docs/architecture/auth-006-deployment-secret-operations-runbook.md`、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 后端代码、前端代码、migration、JWT/OIDC 依赖、token 存储、secrets、外部服务、PDF 原文、OCR 全文、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；本阶段未删除文件，未写代码，未新增 migration，未访问外部服务 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/auth-006-deployment-secret-operations-runbook.md` | 新增生产认证部署与密钥运维手册 |
| `README.md` | 更新当前治理状态和 AUTH-006 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 AUTH-006 阶段记录 |

### 关键产出

1. 明确 `DEV_HEADER` 仅限本地/测试，`REVERSE_PROXY` 是当前生产候选，`JWT` 仍失败关闭。
2. 明确认证相关环境变量和哪些属于未来 JWT/OIDC 配置。
3. 明确反向代理部署前必须剥离客户端身份头、设置可信 principal header、禁止外部直连后端。
4. 明确未来 JWT/OIDC 的 issuer/audience/JWKS/clock skew/算法校验/不存 token 原则。
5. 明确 CORS/TLS/cookie/CSRF、日志脱敏、回滚和日常生产巡检清单。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| 文档阶段 | 未运行后端/前端测试；本阶段未修改代码 |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示 `PROJECT_STEP_RECORD.md` 与 `README.md` 在当前工作副本下 LF 后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 `PROJECT_STEP_RECORD.md`、`README.md` 和 `docs/architecture/auth-006-deployment-secret-operations-runbook.md` |
| 边界关键词扫描 | 仅命中既有 `AuthMode.JWT`、`CurrentUserContextResolver` JWT 失败关闭占位，以及前端 `CurrentUser.authMode` 的类型枚举；本阶段未修改代码，未新增 OAuth 依赖、token 存储、Bearer 处理、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 本阶段为文档阶段，未出现验证失败。

### 风险与限制

1. 本阶段是 runbook，不是生产部署自动化。
2. JWT/OIDC bearer 校验仍未实现。
3. 授权撤销、用户禁用和审计保留策略仍需后续阶段。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端代码 | 未修改 |
| 前端代码 | 未修改 |
| migration | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| token 存储 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. JWT/OIDC bearer 校验尚未实现。
2. 授权撤销/用户禁用流程尚未设计实现。
3. 审计保留、归档和告警策略尚未进入实现阶段。

### 是否建议关闭本阶段

建议关闭 AUTH-006。

关闭理由：生产认证部署与密钥运维手册已沉淀，仓库保护检查通过；本阶段未删除文件，未修改后端/前端代码，未新增 migration，未提交 PDF/OCR 全文、secrets、token 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-012`：授权撤销与用户禁用设计。目标是先设计最小撤销/禁用边界，再决定是否需要 migration 和后端/前端实现阶段。

## SEC-012

阶段名称：授权撤销与用户禁用设计

记录日期：2026-05-09

### 阶段目标

设计用户禁用、Workspace 角色撤销和 Entity 范围撤销的最小治理边界，避免用物理删除作为普通业务流。本阶段只写设计文档，不新增后端/前端代码，不新增 migration，不删除文件，不访问外部服务。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `sec-001-security-scope-design.md`、`sec-011-frontend-entity-scope-management.md`、现有安全数据模型和项目删除授权规则 |
| 允许修改 | `docs/architecture/sec-012-revoke-disable-design.md`、`README.md`、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | 后端代码、前端代码、migration、物理删除接口、PDF 原文、OCR 全文、secrets、外部服务、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；删除文件仍需暂停，本阶段未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/sec-012-revoke-disable-design.md` | 新增授权撤销与用户禁用设计 |
| `README.md` | 更新当前治理状态并增加 SEC-012 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-012 阶段记录 |

### 关键产出

1. 明确用户禁用优先复用现有 `app_user.status`，不需要先做 migration。
2. 明确角色/Entity 范围撤销推荐软撤销，需要独立 migration 阶段。
3. 明确 MVP 不以物理删除作为普通安全治理动作。
4. 明确后续拆分：`SEC-013` 用户禁用后端、`SEC-014` inactive 用户授权拦截、`SEC-015` 授权软撤销 schema/API、`SEC-016` 前端禁用/撤销 MVP。
5. 明确审计、前端理由输入、无批量撤销和不级联删除业务数据。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| 文档阶段 | 未运行后端/前端测试；本阶段未修改代码 |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示 `PROJECT_STEP_RECORD.md` 与 `README.md` 在当前工作副本下 LF 后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 `PROJECT_STEP_RECORD.md`、`README.md` 和 `docs/architecture/sec-012-revoke-disable-design.md` |
| 边界关键词扫描 | 命中既有 `AuditAction.DELETE`、审计前端 `DELETE` 筛选、既有 JWT 失败关闭和 `CurrentUser.authMode` 类型枚举；本阶段未修改代码，未新增 `DeleteMapping`、OAuth 依赖、token 存储、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 本阶段为设计文档阶段，未出现验证失败。

### 风险与限制

1. 本阶段为设计阶段，不提供实际禁用/撤销接口。
2. 角色和 Entity 范围软撤销需要 migration，必须在后续实现阶段显式进入。
3. 当前系统仍未拦截 inactive 用户，需要 SEC-013/SEC-014 实现。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端代码 | 未修改 |
| 前端代码 | 未修改 |
| migration | 未新增 |
| 物理删除接口 | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| token 存储 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 用户禁用后端实现尚未开始。
2. inactive 用户授权拦截尚未实现。
3. 角色/Entity 范围软撤销 schema 和 API 尚未实现。

### 是否建议关闭本阶段

建议关闭 SEC-012。

关闭理由：授权撤销与用户禁用设计、实现拆分和风险边界已沉淀，仓库保护检查通过；本阶段未删除文件，未修改后端/前端代码，未新增 migration，未提交 PDF/OCR 全文、secrets、token 或构建产物，未进入 ERP、BI 或合并报表。

### 下一阶段建议

下一阶段建议进入 `SEC-013`：用户禁用/启用后端 MVP。目标是复用现有 `app_user.status`，新增 disable/enable action endpoints、审计和测试；不做角色/Entity 范围软撤销 migration。

## SEC-013

阶段名称：用户禁用/启用后端 MVP

记录日期：2026-05-09

### 阶段目标

复用现有 `app_user.status` 实现用户 disable/enable 后端 action endpoints，并记录 `STATUS_CHANGE` 审计。本阶段不新增 migration，不做角色/Entity 范围撤销，不做物理删除，不做前端 UI。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `sec-012-revoke-disable-design.md`、`SecurityController`、`SecurityService`、`AppUser`、现有安全集成测试 |
| 允许修改 | 后端安全用户状态方法、status change request、SecurityController、SecurityService、安全集成测试、SEC-013 文档、README、PROJECT_STEP_RECORD |
| 禁止修改 | migration、角色/Entity 范围撤销、物理删除接口、前端 UI、PDF 原文、OCR 全文、secrets、外部服务、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；删除文件仍需暂停，本阶段未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/security/domain/AppUser.java` | 增加 `disable()` / `enable()` |
| `backend/src/main/java/com/budgetplatform/security/api/SecurityUserStatusChangeRequest.java` | 新增状态变更请求 |
| `backend/src/main/java/com/budgetplatform/security/api/SecurityController.java` | 新增 disable/enable action endpoints |
| `backend/src/main/java/com/budgetplatform/security/service/SecurityService.java` | 新增 disable/enable 服务、审计和自禁用保护 |
| `backend/src/test/java/com/budgetplatform/security/api/SecurityControllerIntegrationTests.java` | 覆盖 disable/enable 审计和自禁用拒绝 |
| `docs/architecture/sec-013-user-disable-backend.md` | 新增 SEC-013 架构说明 |
| `README.md` | 更新当前治理状态和 SEC-013 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-013 阶段记录 |

### 关键产出

1. 新增 `POST /api/security/users/{userId}/disable`。
2. 新增 `POST /api/security/users/{userId}/enable`。
3. 状态变更复用现有 `app_user.status`，不新增 migration。
4. 状态变更记录 `STATUS_CHANGE` 审计和 reason。
5. 当前用户不能禁用自己，避免最小治理场景下直接锁死当前管理员。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；54 个测试全部通过，0 failures，0 errors，0 skipped |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示当前工作副本下若干文本文件 LF 后续可能由 Git 触碰为 CRLF，无空白错误 |
| 边界关键词扫描 | 仅命中既有 `AuditAction.DELETE`、`AuthMode.JWT`、JWT fail-closed 占位、前端 `CurrentUser.authMode` 类型和审计 `DELETE` 筛选；未新增 `DeleteMapping`、OAuth 依赖、token 存储、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 首次 `mvn test` 失败，真实错误为 `SecurityService.java` 找不到 `SecurityUserStatusChangeRequest` 符号。
2. 定位原因：新增 request record 后未在 `SecurityService` 中导入该类型。
3. 最小修复：补充 `import com.budgetplatform.security.api.SecurityUserStatusChangeRequest;`。
4. 复测结果：`mvn test` 通过，54 个测试全部通过。

### 风险与限制

1. inactive 用户授权拦截留到 SEC-014。
2. 角色/Entity 范围软撤销留到 SEC-015。
3. 前端 disable/enable 按钮留到 SEC-016。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| 物理删除接口 | 未新增 |
| 角色/Entity 范围撤销 | 未新增 |
| 前端 UI | 未修改 |
| JWT/OAuth 依赖 | 未新增 |
| token 存储 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. inactive 用户仍需 SEC-014 接入授权拦截。
2. 角色/Entity 范围软撤销仍需后续 migration 和 API。
3. 前端禁用/启用入口尚未实现。

### 是否建议关闭本阶段

建议关闭 SEC-013。

关闭理由：用户禁用/启用后端 action endpoints、状态审计和自禁用保护已完成；后端测试、资料保护检查、空白检查和越界扫描均通过。本阶段未删除文件，未新增 migration，未新增物理删除接口，未修改前端 UI，未提交 PDF/OCR 全文、secrets、构建产物或阶段外功能。

### 下一阶段建议

下一阶段建议进入 `SEC-014`：inactive 用户授权拦截。目标是在授权服务中复用现有 `app_user.status`，拒绝 inactive 用户继续执行受保护操作，并补充测试与文档；不新增 migration，不做前端 UI，不做角色/Entity 范围软撤销。

## SEC-014

阶段名称：inactive 用户授权拦截

记录日期：2026-05-09

### 阶段目标

在后端授权层复用现有 `app_user.status`，拒绝已注册 inactive 用户继续执行受保护操作。本阶段不新增 migration，不做角色/Entity 范围软撤销，不做前端 UI，不做物理删除，不接入外部 IdP。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `sec-012-revoke-disable-design.md`、`sec-013-user-disable-backend.md`、`AuthorizationService`、授权服务测试 |
| 允许修改 | `AuthorizationService`、`AuthorizationServiceTests`、SEC-014 架构文档、README、PROJECT_STEP_RECORD |
| 禁止修改 | migration、前端 UI、角色/Entity 范围软撤销 schema、物理删除接口、PDF 原文、OCR 全文、secrets、外部服务、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；删除文件仍需暂停，本阶段未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/security/service/AuthorizationService.java` | 对已注册 inactive 用户执行授权失败关闭 |
| `backend/src/test/java/com/budgetplatform/security/service/AuthorizationServiceTests.java` | 新增 inactive bootstrap 与 inactive 请求头角色绕过测试 |
| `docs/architecture/sec-014-inactive-user-authorization.md` | 新增 SEC-014 架构说明 |
| `README.md` | 更新当前治理状态和 SEC-014 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-014 阶段记录 |

### 关键产出

1. `requireHeaderAdmin` 在允许 bootstrap 或可信请求角色前检查已注册用户是否 inactive。
2. `rolesForWorkspace` 在返回持久化角色或请求头角色前检查已注册用户是否 inactive。
3. 未注册 bootstrap 管理员仍保留初始化能力，避免新环境被锁死。
4. 已注册 inactive 用户不能通过 bootstrap、请求头角色、Workspace 角色或 Entity 范围继续使用受保护操作。
5. 本阶段不新增 migration，不改变授权数据结构。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `rg ...` | 失败；当前 Codex 打包路径下的 `rg.exe` 启动报 Windows “拒绝访问”，已改用 `Get-ChildItem` + `Select-String` 检索 |
| `mvn test` 第一次 | 失败；`AuthorizationServiceTests.java:[96,40] 找不到符号 BUDGET_PLANNER` |
| `mvn test` 第二次 | 通过；56 个测试全部通过，0 failures，0 errors，0 skipped |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示当前工作副本下若干文本文件 LF 后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 SEC-014 相关代码、测试、文档、README 和阶段记录 |
| 边界关键词扫描 | 仅命中既有 `AuditAction.DELETE`、`AuthMode.JWT`、JWT fail-closed 占位、前端 `CurrentUser.authMode` 类型和审计 `DELETE` 筛选；未新增 `DeleteMapping`、OAuth 依赖、token 存储、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 工具检索失败：`rg.exe` 因 Windows “拒绝访问”无法启动，使用 PowerShell `Select-String` 完成同等检索。
2. 首次后端测试失败：测试误用了不存在的 `SecurityRoleCode.BUDGET_PLANNER`。
3. 最小修复：改为项目已有 `SecurityRoleCode.BUDGET_OWNER`。
4. 复测结果：`mvn test` 通过，56 个测试全部通过。

### 风险与限制

1. inactive 用户已有 Workspace 角色和 Entity 范围仍保留在数据库中；重新启用用户会恢复这些授权。
2. 角色/Entity 范围软撤销仍需后续 migration 和 API。
3. 前端禁用/启用入口尚未实现。
4. JWT/OIDC bearer 校验仍未实现。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 未新增 |
| 物理删除接口 | 未新增 |
| 角色/Entity 范围软撤销 | 未新增 |
| 前端 UI | 未修改 |
| JWT/OAuth 依赖 | 未新增 |
| token 存储 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 角色/Entity 范围软撤销仍需后续 migration 和 API。
2. 前端禁用/启用与撤销入口尚未实现。
3. JWT/OIDC bearer 校验尚未实现。

### 是否建议关闭本阶段

建议关闭 SEC-014。

关闭理由：inactive 用户授权拦截已在 `AuthorizationService` 集中实现，inactive bootstrap 和 inactive 请求头角色绕过测试已覆盖；后端测试、资料保护检查、空白检查和越界扫描均通过。本阶段未删除文件，未新增 migration，未新增物理删除接口，未修改前端 UI，未提交 PDF/OCR 全文、secrets、构建产物或阶段外功能。

### 下一阶段建议

下一阶段建议进入 `SEC-015`：角色与 Entity 范围软撤销 schema/API。目标是为 Workspace 角色授权和 Entity 范围授权增加软撤销能力、撤销 action endpoints、审计和测试；该阶段将涉及 migration，但用户已授权非删除事项自动推进。

## SEC-015

阶段名称：角色与 Entity 范围软撤销 schema/API

记录日期：2026-05-09

### 阶段目标

为 Workspace 角色授权和 Entity 范围授权增加软撤销字段、撤销 action endpoints、active-only 授权查询、审计和测试。本阶段允许新增 migration，但不做物理删除，不做前端 UI，不做批量撤销，不进入 ERP、BI 或合并报表。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `sec-012-revoke-disable-design.md`、`sec-014-inactive-user-authorization.md`、安全授权实体/仓库/服务/控制器/集成测试 |
| 允许修改 | 授权实体、授权仓库、授权服务、SecurityController、授权响应、撤销请求、migration、安全集成测试、SEC-015 文档、README、PROJECT_STEP_RECORD |
| 禁止修改 | 删除文件、物理删除接口、前端 UI、批量撤销、PDF 原文、OCR 全文、secrets、外部服务、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；删除文件仍需暂停，本阶段未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/resources/db/migration/V8__security_grant_soft_revoke.sql` | 新增授权软撤销字段和状态索引 |
| `backend/src/main/java/com/budgetplatform/security/domain/SecurityGrantStatus.java` | 新增授权状态枚举 |
| `backend/src/main/java/com/budgetplatform/security/domain/AppUserRole.java` | 增加 `ACTIVE`/`REVOKED` 状态、撤销和重新激活行为 |
| `backend/src/main/java/com/budgetplatform/security/domain/AppUserEntityScope.java` | 增加 `ACTIVE`/`REVOKED` 状态、撤销和重新激活行为 |
| `backend/src/main/java/com/budgetplatform/security/repository/AppUserRoleRepository.java` | 查询收敛为 active grant 并支持按唯一键查找旧授权 |
| `backend/src/main/java/com/budgetplatform/security/repository/AppUserEntityScopeRepository.java` | 查询收敛为 active grant 并支持按唯一键查找旧授权 |
| `backend/src/main/java/com/budgetplatform/security/service/AuthorizationService.java` | 角色和 Entity 范围授权判断只读取 active grant |
| `backend/src/main/java/com/budgetplatform/security/service/SecurityService.java` | 新增撤销服务、重新 grant 激活旧记录、active-only 列表和 current user grants |
| `backend/src/main/java/com/budgetplatform/security/api/SecurityController.java` | 新增 role/entity-scope revoke action endpoints |
| `backend/src/main/java/com/budgetplatform/security/api/SecurityGrantRevokeRequest.java` | 新增撤销请求 |
| `backend/src/main/java/com/budgetplatform/security/api/UserRoleResponse.java` | 返回授权状态和撤销时间 |
| `backend/src/main/java/com/budgetplatform/security/api/EntityScopeResponse.java` | 返回授权状态和撤销时间 |
| `backend/src/test/java/com/budgetplatform/security/api/SecurityControllerIntegrationTests.java` | 覆盖撤销、active-only 列表、重新 grant 激活和审计 |
| `docs/architecture/sec-015-grant-soft-revoke-api.md` | 新增 SEC-015 架构说明 |
| `README.md` | 更新当前治理状态和 SEC-015 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-015 阶段记录 |

### 关键产出

1. `app_user_role` 和 `app_user_entity_scope` 新增 `status`、`revoked_at`、`revoked_by`、`revoked_reason`。
2. 新增 `POST /api/security/users/{userId}/roles/{roleId}/revoke`。
3. 新增 `POST /api/security/users/{userId}/entity-scopes/{scopeId}/revoke`。
4. 授权判断、当前用户授权摘要和授权列表仅使用 `ACTIVE` grants。
5. 撤销后的同一授权再次 grant 时重新激活原记录，避免数据库方言相关的部分唯一索引。
6. 撤销和重新激活均记录 `ACCESS_CHANGE` 审计。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` 第一次 | 失败；`V8__security_grant_soft_revoke.sql` 第 1 行在 H2 下不支持一次 `ALTER TABLE ... ADD COLUMN` 添加多列 |
| `mvn test` 第二次 | 通过；57 个测试全部通过，0 failures，0 errors，0 skipped；Flyway 成功应用 8 个 migration |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示当前工作副本下若干文本文件 LF 后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 SEC-015 相关后端代码、migration、测试、文档、README 和阶段记录 |
| 边界关键词扫描 | 仅命中既有 `AuditAction.DELETE`、`AuthMode.JWT`、JWT fail-closed 占位、前端 `CurrentUser.authMode` 类型和审计 `DELETE` 筛选；未新增 `DeleteMapping`、OAuth 依赖、token 存储、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 首次后端测试失败：H2 报 `Syntax error in SQL statement "ALTER TABLE app_user_role ADD COLUMN status ... , ADD COLUMN ..."`。
2. 定位原因：migration 使用了多列 ADD COLUMN 写法，H2 不兼容。
3. 最小修复：将 V8 migration 拆为逐列 `ALTER TABLE ... ADD COLUMN`。
4. 复测结果：`mvn test` 通过，57 个测试全部通过。

### 风险与限制

1. 当前 active list endpoints 隐藏 revoked grants，历史追踪主要依赖 audit。
2. 重新 grant 会复用原 grant id。
3. 前端禁用/启用和撤销入口仍需 SEC-016。
4. JWT/OIDC bearer 校验仍未实现。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| migration | 新增 `V8__security_grant_soft_revoke.sql`，符合本阶段范围 |
| 物理删除接口 | 未新增 |
| 前端 UI | 未修改 |
| 批量撤销 | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| token 存储 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |

### 未解决问题

1. 前端禁用/启用与撤销入口尚未实现。
2. 审计生命周期事件筛选体验仍可在后续 AUDIT 阶段优化。
3. JWT/OIDC bearer 校验尚未实现。

### 是否建议关闭本阶段

建议关闭 SEC-015。

关闭理由：角色与 Entity 范围软撤销 schema/API、active-only 授权查询、撤销审计和重新 grant 激活行为已完成；后端测试、资料保护检查、空白检查和越界扫描均通过。本阶段未删除文件，未新增物理删除接口，未修改前端 UI，未提交 PDF/OCR 全文、secrets、构建产物或阶段外功能。

### 下一阶段建议

下一阶段建议进入 `SEC-016`：前端禁用/启用与撤销 MVP。目标是在现有安全管理前端中增加用户 disable/enable、角色 revoke、Entity scope revoke 的最小操作入口和 reason 输入；不新增后端 schema，不做批量操作，不做复杂审批流。

## SEC-016

阶段名称：前端禁用/启用与撤销 MVP

记录日期：2026-05-09

### 阶段目标

在现有安全管理前端中增加用户 disable/enable、Workspace 角色 revoke、Entity scope revoke 的最小操作入口和 reason 输入。本阶段不新增后端 schema，不做批量撤销，不做复杂审批流，不新增物理删除控制。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `sec-013-user-disable-backend.md`、`sec-015-grant-soft-revoke-api.md`、现有 `App.tsx` 安全管理区域、`securityApi.ts` |
| 允许修改 | `frontend/src/features/security/securityApi.ts`、`frontend/src/App.tsx`、`frontend/src/styles.css`、SEC-016 文档、README、PROJECT_STEP_RECORD |
| 禁止修改 | 后端 schema、后端 API、新 migration、批量撤销、复杂审批流、物理删除控制、PDF 原文、OCR 全文、secrets、外部服务、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `pnpm type-check`、`pnpm lint`、`pnpm build`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；删除文件仍需暂停，本阶段未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `frontend/src/features/security/securityApi.ts` | 增加 disable/enable/revoke API client 与 grant status 类型 |
| `frontend/src/App.tsx` | 在安全管理界面增加 reason 输入、用户禁用/启用、角色撤销、Entity 范围撤销操作 |
| `frontend/src/styles.css` | 增加安全操作区与表格操作按钮样式 |
| `docs/architecture/sec-016-frontend-disable-revoke-mvp.md` | 新增 SEC-016 架构说明 |
| `README.md` | 更新当前治理状态和 SEC-016 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 SEC-016 阶段记录 |

### 关键产出

1. 前端 API client 支持 `disableSecurityUser`、`enableSecurityUser`、`revokeUserRole`、`revokeEntityScope`。
2. 安全管理界面为选中用户提供 disable/enable 操作。
3. Workspace role 与 Entity scope active 列表提供逐行 revoke 操作。
4. 操作共用一个可选 reason 输入，提交后进入后端审计。
5. 操作成功后刷新用户、授权和当前用户摘要。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过；Vite 成功构建，产物位于被忽略的 `frontend/dist` |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示当前工作副本下若干文本文件 LF 后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 SEC-016 相关前端代码、文档、README 和阶段记录 |
| 边界关键词扫描 | 仅命中既有 `AuditAction.DELETE`、`AuthMode.JWT`、JWT fail-closed 占位、前端 `CurrentUser.authMode` 类型和审计 `DELETE` 筛选；未新增 `DeleteMapping`、OAuth 依赖、token 存储、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 本阶段前端类型检查、lint 和构建未出现失败。

### 风险与限制

1. reason 输入为共享字段，不做每行独立 reason 状态。
2. revoked grant 历史不在安全管理列表展示，当前通过审计查看。
3. 后端仍负责 self-disable 等关键保护，前端不重复实现所有业务守卫。
4. JWT/OIDC bearer 校验仍未实现。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端 schema | 未修改 |
| migration | 未新增 |
| 物理删除控制 | 未新增 |
| 批量撤销 | 未新增 |
| 复杂审批流 | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| token 存储 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |
| 构建产物 | `frontend/dist` 已被忽略，未提交 |

### 未解决问题

1. 安全生命周期事件的审计筛选体验仍可在后续 AUDIT 阶段优化。
2. JWT/OIDC bearer 校验尚未实现。

### 是否建议关闭本阶段

建议关闭 SEC-016。

关闭理由：前端 disable/enable/revoke MVP 已完成，类型检查、lint、构建、资料保护检查、空白检查和越界扫描均通过。本阶段未删除文件，未新增后端 schema 或 migration，未新增物理删除控制、批量撤销、复杂审批流、PDF/OCR 全文、secrets、构建产物或阶段外功能。

### 下一阶段建议

下一阶段建议进入 `AUDIT-005`：安全生命周期审计体验优化。目标是在现有审计前端筛选与展示中更清楚地支持 `STATUS_CHANGE`、`ACCESS_CHANGE`、`AUTH_FAILURE` 等安全治理事件；不新增 BI 图表，不做告警系统，不做外部服务。

## AUDIT-005

阶段名称：安全生命周期审计体验优化

记录日期：2026-05-09

### 阶段目标

优化现有审计前端对安全生命周期事件的筛选与展示，支持用户状态、角色授权、Entity 授权和认证失败的快捷筛选，并提升 `detailsJson` 可读性。本阶段不新增 BI 图表、不新增告警系统、不接入外部服务、不修改后端 schema。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `sec-016-frontend-disable-revoke-mvp.md`、现有审计 API、现有 `App.tsx` 审计区域 |
| 允许修改 | `frontend/src/App.tsx`、`frontend/src/styles.css`、AUDIT-005 文档、README、PROJECT_STEP_RECORD |
| 禁止修改 | 后端 schema、新审计表、BI 图表、告警系统、外部服务、物理删除控制、PDF 原文、OCR 全文、secrets、ERP 直连、合并报表 |
| 验证命令 | `pnpm type-check`、`pnpm lint`、`pnpm build`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；删除文件仍需暂停，本阶段未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `frontend/src/App.tsx` | 增加安全审计快捷筛选、清空筛选和 JSON details 格式化 |
| `frontend/src/styles.css` | 增加审计快捷按钮与 details 代码块样式 |
| `docs/architecture/audit-005-security-lifecycle-audit-ux.md` | 新增 AUDIT-005 架构说明 |
| `README.md` | 更新当前治理状态和 AUDIT-005 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 AUDIT-005 阶段记录 |

### 关键产出

1. 新增 User status、Role access、Entity access、Auth failures 审计快捷筛选。
2. 快捷筛选复用现有 `/api/audit/events` 查询能力，不新增后端接口。
3. 有效 JSON details 在表格中格式化展示，提升安全事件检查效率。
4. 保持审计页面为运维型表格，不引入图表 BI 或告警系统。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过；Vite 成功构建，产物位于被忽略的 `frontend/dist` |

### 失败项与修复记录

1. 本阶段前端类型检查、lint 和构建未出现失败。

### 风险与限制

1. 快捷筛选覆盖当前安全生命周期事件，未实现保存筛选或导出。
2. details 仍为只读 JSON 文本展示。
3. 不包含告警、通知、审计保留和归档策略。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端 schema | 未修改 |
| 新审计表 | 未新增 |
| BI 图表 | 未新增 |
| 告警系统 | 未新增 |
| 外部服务接入 | 未执行 |
| 物理删除控制 | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| token 存储 | 未新增 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |
| 构建产物 | `frontend/dist` 已被忽略，未提交 |

### 未解决问题

1. JWT/OIDC bearer 校验尚未实现。
2. 审计保留、归档和告警策略仍未实现。

### 是否建议关闭本阶段

建议关闭 AUDIT-005。

关闭理由：安全生命周期快捷筛选和 JSON details 格式化展示已完成，前端类型检查、lint、构建、资料保护检查、空白检查和越界扫描均通过。本阶段未删除文件，未修改后端 schema，未新增 BI 图表、告警系统、外部服务、PDF/OCR 全文、secrets、构建产物或阶段外功能。

### 下一阶段建议

下一阶段建议进入 `OPS-002`：阶段巡视与发布前治理检查。目标是对当前安全治理主线进行仓库状态、测试命令、资料保护、未解决风险和下一批阶段拆分检查；不新增业务功能。

## OPS-002

阶段名称：阶段巡视与发布前治理检查

记录日期：2026-05-09

### 阶段目标

对当前安全治理主线进行仓库状态、后端/前端验证、资料保护、边界扫描、阶段记录一致性和下一批阶段拆分检查。本阶段不新增业务功能，不修改后端/前端业务代码，不新增 migration，不删除文件。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、README、近期架构文档、当前 Git 状态 |
| 允许修改 | `docs/architecture/ops-002-governance-readiness-check.md`、README、PROJECT_STEP_RECORD；允许修正阶段记录文字错位 |
| 禁止修改 | 后端业务代码、前端业务代码、migration、物理删除、PDF 原文、OCR 全文、secrets、外部服务、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git check-ignore`、`git diff --check`、`git status --short`、Git remote/log、PDF 清单、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；删除文件仍需暂停，本阶段未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/ops-002-governance-readiness-check.md` | 新增治理巡视与发布前检查文档 |
| `README.md` | 更新当前治理状态和 OPS-002 文档入口 |
| `PROJECT_STEP_RECORD.md` | 修正 SEC-016 验证表文字错位，并追加 OPS-002 阶段记录 |

### 关键产出

1. 确认当前分支为 `main`，remote 为 `https://github.com/tcymcr168-cloud/budget-platform.git`。
2. 确认近期安全治理提交已推送到 `main`，当前 head 为 `9853fe2 feat: improve security audit review UX`。
3. 确认 6 个 BPC PDF 仍在本地 `docs/source/bpc-pdf`。
4. 确认后端、前端、资料保护、空白检查和边界扫描均通过。
5. 修正 SEC-016 阶段记录中重复和错位的验证行。
6. 明确下一批阶段建议：`AUTH-007`、`E2E-001`、`PERF-001`、`TEMPLATE-001`。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；57 个测试全部通过，0 failures，0 errors，0 skipped；Flyway 成功验证并应用 8 个 migration |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过；Vite 成功构建，产物位于被忽略的 `frontend/dist` |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过，无空白错误 |
| `git status --short` | OPS-002 修改前为 clean；OPS-002 修改后仅显示 README、PROJECT_STEP_RECORD 和 OPS-002 文档 |
| Git remote/log | 当前分支 `main`；remote `origin` 指向正式仓库；最近提交包含 AUDIT-005、SEC-016、SEC-015、SEC-014、SEC-013 |
| PDF 清单 | `docs/source/bpc-pdf` 下 6 个 PDF 均存在 |
| 边界关键词扫描 | 仅命中既有 `AuditAction.DELETE`、`AuthMode.JWT`、JWT fail-closed 占位、前端 `CurrentUser.authMode` 类型和审计 `DELETE` 筛选；未新增 `DeleteMapping`、OAuth 依赖、token 存储、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 本阶段验证命令未出现失败。
2. 发现并修复 `PROJECT_STEP_RECORD.md` 中 SEC-016 验证表重复/错位文字，属于阶段记录一致性修复，不影响产品行为。

### 风险与限制

1. JWT/OIDC bearer 校验尚未实现；当前生产推荐路径仍是反向代理可信身份。
2. 审计保留、归档和告警策略尚未实现。
3. 端到端浏览器流程测试尚未实现。
4. 查询分页和性能治理仍未推进。
5. 模板版本生命周期治理仍未推进。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端业务代码 | 未修改 |
| 前端业务代码 | 未修改 |
| migration | 未新增 |
| 物理删除控制 | 未新增 |
| JWT/OAuth 依赖 | 未新增 |
| token 存储 | 未新增 |
| 外部服务接入 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |
| 构建产物 | `frontend/dist` 已被忽略，未提交 |

### 未解决问题

1. JWT/OIDC bearer 校验尚未实现。
2. E2E smoke 测试计划和实现尚未完成。
3. 查询分页与性能治理尚未完成。
4. 模板版本生命周期治理尚未完成。

### 是否建议关闭本阶段

建议关闭 OPS-002。

关闭理由：治理巡视、验证命令、资料保护、阶段记录一致性修复和下一阶段拆分已完成；本阶段未删除文件，未新增业务功能、migration、后端/前端业务代码、PDF/OCR 全文、secrets、构建产物或阶段外功能。

### 下一阶段建议

下一阶段建议进入 `AUTH-007`：JWT/OIDC bearer 校验设计。目标是先设计 production bearer validation 的依赖、配置、失败关闭、审计、测试和回滚边界；设计阶段不新增依赖、不实现 token 校验。

## AUTH-007

阶段名称：JWT/OIDC bearer 校验设计

记录日期：2026-05-09

### 阶段目标

设计未来 `budget-platform.auth.mode=JWT` 的 JWT/OIDC bearer 校验边界，明确依赖方向、配置项、请求处理、失败关闭、审计脱敏、测试矩阵和回滚方案。本阶段只写设计文档，不新增依赖、不修改运行时代码、不访问外部 IdP、不新增 secrets。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `auth-001-production-auth-implementation-plan.md`、`auth-006-deployment-secret-operations-runbook.md`、`sec-009-session-token-operations.md`、`auth-005-failed-authentication-audit.md`、当前认证代码与 Maven 依赖 |
| 允许修改 | `docs/architecture/auth-007-jwt-oidc-bearer-validation-design.md`、README、PROJECT_STEP_RECORD |
| 禁止修改 | 后端运行时代码、前端代码、Maven 依赖、migration、secrets、外部 IdP 访问、PDF 原文、OCR 全文、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；删除文件仍需暂停，本阶段未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/auth-007-jwt-oidc-bearer-validation-design.md` | 新增 JWT/OIDC bearer 校验设计 |
| `README.md` | 更新当前治理状态和 AUTH-007 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 AUTH-007 阶段记录 |

### 关键产出

1. 明确未来 JWT adapter 应接入现有 `CurrentUserContextResolver` 边界，只解析可信 principal。
2. 推荐未来实现优先使用 Spring Security OAuth2 Resource Server，并跟随 Spring Boot BOM；本阶段不新增依赖。
3. 明确 JWT 配置契约：issuer、audience、JWKS URI、username claim、clock skew、token length、allowed algorithms。
4. 明确失败关闭和审计 reason 分类：missing bearer、unsupported scheme、malformed token、invalid issuer/audience/signature、expired token 等。
5. 明确禁止把 IdP group/scope claim 作为预算授权来源，预算授权继续来自 `app_user_role` 和 Entity scope。
6. 明确实现拆分：`AUTH-008` 依赖与配置、`AUTH-009` adapter 实现、`AUTH-010` 前端 bearer 边界、`AUTH-011` 部署烟测与回滚。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| 文档阶段 | 未运行后端/前端完整测试；本阶段未修改运行时代码、依赖或 migration |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示当前工作副本下 README 与阶段记录 LF 后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 AUTH-007 文档、README 和阶段记录 |
| 边界关键词扫描 | 仅命中既有 `AuditAction.DELETE`、`AuthMode.JWT`、JWT fail-closed 占位、前端 `CurrentUser.authMode` 类型和审计 `DELETE` 筛选；本阶段未新增 `DeleteMapping`、OAuth 依赖、token 存储、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 本阶段为设计文档阶段，未出现验证失败。

### 风险与限制

1. 本阶段是设计阶段，不提供实际 JWT/OIDC 校验能力。
2. 后续实现会新增 Spring Security/OAuth2 相关依赖，需要单独阶段验证。
3. JWKS 访问、缓存和 key rotation 仍需实现阶段细化。
4. 反向代理可信身份仍是当前已实现的生产推荐路径。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 后端运行时代码 | 未修改 |
| 前端代码 | 未修改 |
| Maven 依赖 | 未新增 |
| migration | 未新增 |
| 外部 IdP 访问 | 未执行 |
| secrets | 未新增 |
| token 存储 | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |
| 构建产物 | 未提交 |

### 未解决问题

1. JWT/OIDC bearer 校验实现尚未开始。
2. 前端 direct bearer token 流是否需要仍需部署形态确认。
3. 审计保留、归档和告警策略仍未实现。

### 是否建议关闭本阶段

建议关闭 AUTH-007。

关闭理由：JWT/OIDC bearer 校验设计、配置契约、失败关闭、审计脱敏、测试矩阵、实现拆分和回滚边界已沉淀；资料保护检查、空白检查和越界扫描通过。本阶段未删除文件，未新增运行时代码、依赖、migration、外部服务访问、secrets、PDF/OCR 全文、构建产物或阶段外功能。

### 下一阶段建议

下一阶段建议进入 `AUTH-008`：JWT/OIDC 依赖与配置属性基线。目标是新增最小依赖和配置结构，但暂不实现完整 token 校验逻辑；需要后端测试覆盖配置绑定和 JWT 模式失败关闭行为不退化。

## AUTH-008

阶段名称：JWT/OIDC 依赖与配置属性基线

记录日期：2026-05-09

### 阶段目标

新增未来 JWT/OIDC bearer adapter 所需的最小后端依赖和配置属性基线，覆盖配置默认值与绑定测试；本阶段不实现 token 校验，不访问外部 IdP/JWKS，不存储 token，不新增 secrets，不修改前端，不新增 migration。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、`auth-007-jwt-oidc-bearer-validation-design.md`、当前认证代码与 Maven 依赖 |
| 允许修改 | `backend/pom.xml`、`AuthProperties.java`、`application.yml`、后端配置测试、`docs/architecture/auth-008-jwt-config-baseline.md`、README、PROJECT_STEP_RECORD |
| 禁止修改 | 前端业务代码、migration、token 校验逻辑、外部 IdP/JWKS 访问、secrets、PDF 原文、OCR 全文、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；删除文件仍需暂停，本阶段未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/pom.xml` | 新增 `spring-security-oauth2-jose` 依赖，为后续 JWT 验证提供 JOSE/JWT 能力 |
| `backend/src/main/java/com/budgetplatform/security/context/AuthProperties.java` | 新增 `jwt` 嵌套配置属性及安全默认值 |
| `backend/src/main/resources/application.yml` | 新增环境变量驱动的 `budget-platform.auth.jwt.*` 配置 |
| `backend/src/test/java/com/budgetplatform/security/context/AuthPropertiesTests.java` | 新增 JWT 配置默认值与绑定测试 |
| `docs/architecture/auth-008-jwt-config-baseline.md` | 新增 JWT 配置基线文档 |
| `README.md` | 更新当前认证治理状态和 AUTH-008 文档入口 |
| `PROJECT_STEP_RECORD.md` | 追加 AUTH-008 阶段记录 |

### 关键产出

1. 新增最小 `spring-security-oauth2-jose` 依赖，但不引入 resource-server 自动过滤器。
2. 新增 `issuer`、`audience`、`jwksUri`、`usernameClaim`、`clockSkewSeconds`、`maxTokenLength`、`allowedAlgorithms` 配置结构。
3. 默认 username claim 为 `sub`，默认 clock skew 为 `60` 秒，默认 max token length 为 `8192`，默认算法 allow-list 为 `RS256`。
4. `budget-platform.auth.mode=JWT` 仍保持失败关闭，未实现 bearer token 解析或验签。
5. 未新增外部 IdP/JWKS 访问、token 存储、secrets、migration、前端 token 处理或阶段外功能。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；59 个测试全部通过，0 failures，0 errors，0 skipped；Flyway 成功验证并应用 8 个 migration |
| 首次 `mvn test` | 失败于新增测试编译：`BindResult.orElseThrow()` 需要 supplier，不能无参调用 |
| 修复记录 | 将测试中的 `.orElseThrow()` 改为 `.orElseThrow(() -> new AssertionError("Auth properties should bind."))` 后重新执行 Maven 测试通过 |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示 README、pom、AuthProperties、application.yml 后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 AUTH-008 相关后端配置、测试、README、架构文档和阶段记录 |
| 边界关键词扫描 | 命中既有 `AuditAction.DELETE`、`AuthMode.JWT`、JWT fail-closed 占位、前端 `CurrentUser.authMode` 和审计 `DELETE` 类型；新增命中仅为 `AuthProperties.Jwt` 配置结构，未新增 bearer 处理、token 存储、DeleteMapping、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 初次 Maven 测试编译失败，真实错误为 `BindResult.orElseThrow()` 缺少 supplier 参数。
2. 已在 `AuthPropertiesTests` 中补充 `AssertionError` supplier 并重新运行 `mvn test`，验证通过。

### 风险与限制

1. 本阶段只提供配置与依赖基线，不提供实际 JWT/OIDC bearer 校验能力。
2. `JWT` 模式仍保持失败关闭，生产可用路径仍是已实现的 `REVERSE_PROXY` trusted principal 模式。
3. 后续 `AUTH-009` 需要补齐签名、issuer、audience、expiry、username claim、未知用户和 inactive 用户测试。
4. JWKS 缓存、key rotation 和 IdP outage 回滚仍需实现阶段细化。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 前端业务代码 | 未修改 |
| migration | 未新增 |
| token 校验逻辑 | 未新增 |
| token 存储 | 未新增 |
| 外部 IdP/JWKS 访问 | 未执行 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |
| 构建产物 | 未提交 |

### 未解决问题

1. JWT/OIDC bearer 校验实现尚未开始。
2. JWT 失败审计 reason 分类需要在 adapter 实现阶段落地。
3. 前端 direct bearer token 流是否需要仍需部署形态确认。

### 是否建议关闭本阶段

建议关闭 AUTH-008。

关闭理由：最小 JWT 依赖、配置结构、环境变量、配置绑定测试、架构文档、README 和阶段记录已完成；后端 Maven 测试、资料保护检查、空白检查和越界扫描均通过。本阶段未删除文件，未新增 token 校验、token 存储、外部服务访问、secrets、migration、PDF/OCR 全文、构建产物、ERP、BI、合并报表或阶段外功能。

### 下一阶段建议

下一阶段建议进入 `AUTH-009`：JWT bearer adapter 后端实现。目标是在 `CurrentUserContextResolver` 边界内实现 bearer token 提取、长度限制、签名/issuer/audience/expiry 校验、username claim 映射、未知用户处理、失败审计分类和测试覆盖；不引入前端 token 存储，不使用 IdP group/scope 作为预算授权。

## AUTH-009

阶段名称：JWT bearer adapter 后端实现

记录日期：2026-05-09

### 阶段目标

在现有 `CurrentUserContextResolver` 边界内实现 `budget-platform.auth.mode=JWT` 的后端 bearer token adapter：提取 `Authorization: Bearer`、限制 token 长度、使用 Nimbus/Spring Security JWT decoder 校验签名与标准 claims、映射 username claim、拒绝未知或 inactive 应用用户，并记录脱敏失败审计。本阶段不修改前端、不新增 token 存储、不新增 migration、不接入 ERP/BI/合并报表、不处理 PDF/OCR。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `auth-007-jwt-oidc-bearer-validation-design.md`、`auth-008-jwt-config-baseline.md`、现有 `CurrentUserContextResolver`、`AuthorizationService`、`AppUser` 生命周期规则 |
| 允许修改 | 后端认证上下文 resolver、后端认证单测、`docs/architecture/auth-009-jwt-bearer-adapter.md`、README、PROJECT_STEP_RECORD |
| 禁止修改 | 前端业务代码、migration、PDF 原文、OCR 全文、secrets、token 存储、外部 IdP 配置文件、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `mvn test`、`git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；删除文件仍需暂停，本阶段未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `backend/src/main/java/com/budgetplatform/security/context/CurrentUserContextResolver.java` | 实现 JWT bearer adapter、decoder 构建、失败 reason 分类、未知/inactive 用户拦截 |
| `backend/src/test/java/com/budgetplatform/security/context/CurrentUserContextResolverTests.java` | 新增 JWT 成功与失败路径单测 |
| `docs/architecture/auth-009-jwt-bearer-adapter.md` | 新增 JWT bearer adapter 架构与运维说明 |
| `README.md` | 更新 AUTH-009 文档入口与当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 AUTH-009 阶段记录 |

### 关键产出

1. `JWT` 模式不再只是未实现占位；后端可从 HTTP `Authorization: Bearer` 解析 JWT。
2. 生产 decoder 基于 `NimbusJwtDecoder.withJwkSetUri(...)`，配置 issuer、audience、timestamp validator 和 JWS algorithm allow-list。
3. 缺失 bearer、错误 scheme、空 token、超长 token、缺配置、坏配置、校验失败、缺 username claim、未知用户、inactive 用户均失败关闭。
4. JWT 映射后的角色为空，预算授权继续通过 `AuthorizationService` 从 `app_user_role` 与 `app_user_entity_scope` 查询。
5. JWT 不使用 IdP group/scope 作为预算授权，不自动创建应用用户。
6. 审计只记录稳定 reason、authMode、headerName 和请求元数据，不记录 raw token、JWT header、payload、cookie、refresh token、private key 或 client secret。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；68 个测试全部通过，0 failures，0 errors，0 skipped；Flyway 成功验证并应用 8 个 migration |
| 新增单测覆盖 | valid JWT、missing bearer、unsupported scheme、oversized token、missing username claim、expired validation failure、malformed token、unknown user、inactive user、missing JWT configuration |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示 README 和 Java 文件后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 AUTH-009 后端认证代码、测试、README、架构文档和阶段记录 |
| 边界关键词扫描 | 命中既有授权服务引用、`AuditAction.DELETE`、`AuthMode.JWT`、前端 `CurrentUser.authMode`/审计 `DELETE` 类型，以及本阶段新增 JWT/bearer/Authorization adapter 代码；未新增 `DeleteMapping`、前端 token 存储、ERP、BI 图表、合并报表、PDF/OCR 处理或 secrets |

### 失败项与修复记录

1. 本阶段第一次 `mvn test` 通过，后续清理代码后再次运行 `mvn test` 仍通过。
2. 未出现需要两轮最小修复的阻塞。

### 风险与限制

1. JWT 生产启用仍依赖正确的 issuer、audience、JWKS URI、算法 allow-list 和用户预置。
2. JWKS 访问发生在 token decode 阶段；若 IdP/JWKS 不可用，请回退到已实现的 `REVERSE_PROXY` trusted principal 模式。
3. 失败 reason 依赖 Spring Security/Nimbus 异常描述做稳定归类，未来可在真实 IdP smoke test 后继续细化。
4. 本阶段未实现前端 direct bearer flow，也未设计 token 获取或刷新流程。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 前端业务代码 | 未修改 |
| migration | 未新增 |
| token 存储 | 未新增 |
| localStorage/sessionStorage | 未新增 |
| 外部 IdP/JWKS 配置文件 | 未新增 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |
| 构建产物 | 未提交 |

### 未解决问题

1. 前端是否需要 direct bearer token flow 仍需单独阶段确认。
2. 真实 IdP/JWKS smoke test 与回滚手册仍需 `AUTH-011` 或后续运维阶段沉淀。
3. JWT key rotation、缓存观察和告警仍未实现。

### 是否建议关闭本阶段

建议关闭 AUTH-009。

关闭理由：JWT bearer 后端 adapter、失败关闭、脱敏审计、应用用户生命周期拦截、架构文档、README 和阶段记录已完成；后端 Maven 测试、资料保护检查、空白检查和越界扫描均通过。本阶段未删除文件，未修改前端业务代码，未新增 migration、token 存储、secrets、外部配置、PDF/OCR 全文、构建产物、ERP、BI、合并报表或阶段外功能。

### 下一阶段建议

下一阶段建议进入 `AUTH-010`：前端 bearer 边界决策与设计。目标是判断浏览器是否需要 direct bearer flow；若需要，只做安全边界设计并避免 `localStorage`/`sessionStorage` token 存储；若不需要，则明确 JWT 用于 API/gateway 客户端，浏览器生产流量继续走 reverse proxy trusted principal。

## AUTH-010

阶段名称：前端 bearer 边界决策与设计

记录日期：2026-05-09

### 阶段目标

在后端 JWT bearer adapter 已可用后，明确 React 浏览器前端是否进入 direct bearer token flow。本阶段只做架构决策与文档沉淀，不修改前端运行时代码，不新增 token 存储，不新增 login/logout UI，不新增 IdP SDK，不修改后端认证行为，不新增 migration。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `auth-009-jwt-bearer-adapter.md`、`auth-004-frontend-current-user-boundary.md`、`sec-008-frontend-auth-boundary.md`、README、当前 Git 状态 |
| 允许修改 | `docs/architecture/auth-010-frontend-bearer-boundary.md`、README、PROJECT_STEP_RECORD |
| 禁止修改 | 前端运行时代码、后端运行时代码、migration、token 存储、IdP SDK、secrets、PDF 原文、OCR 全文、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；删除文件仍需暂停，本阶段未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/auth-010-frontend-bearer-boundary.md` | 新增前端 bearer 边界决策文档 |
| `README.md` | 更新 AUTH-010 文档入口和当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 AUTH-010 阶段记录 |

### 关键产出

1. MVP 决策：不实现浏览器 direct bearer token flow。
2. 生产浏览器流量继续优先采用 `REVERSE_PROXY` trusted principal，由企业 SSO/gateway 在浏览器与后端之间处理身份会话。
3. 后端 `JWT` adapter 定位为 API gateway、非浏览器 API 客户端、自动化客户端或受控 gateway-to-backend 调用。
4. 明确拒绝在 `localStorage` 或 `sessionStorage` 中保存 access token。
5. 明确未来若必须进入 direct bearer flow，需要先完成 OIDC Authorization Code + PKCE、BFF/session-cookie、CSRF/CORS、logout、刷新失败、E2E 和回滚设计。
6. 保持应用预算授权继续来自 `app_user_role` 与 `app_user_entity_scope`，不使用 IdP group/scope claim。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| 文档阶段 | 未运行后端/前端完整测试；本阶段未修改运行时代码、依赖或 migration |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示 README、PROJECT_STEP_RECORD 和 AUTH-010 文档后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 AUTH-010 文档、README 和阶段记录 |
| 边界关键词扫描 | 仅命中文档中的 bearer/JWT/localStorage/sessionStorage/ERP/BI/合并报表禁用说明；未新增前端 token 存储、后端代码、migration、PDF/OCR 处理、ERP、BI 或合并报表代码 |

### 失败项与修复记录

1. 本阶段未出现验证失败。

### 风险与限制

1. 本阶段是架构决策，不实现新的浏览器登录能力。
2. 生产浏览器认证仍依赖 reverse proxy/gateway 的企业 SSO 和 header stripping 能力。
3. 后端 JWT adapter 已可用，但真实 IdP/JWKS smoke test 和回滚手册尚未沉淀。
4. 如未来必须支持 direct bearer browser flow，需要单独阶段引入更完整的威胁模型和 E2E 验证。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 前端运行时代码 | 未修改 |
| 后端运行时代码 | 未修改 |
| migration | 未新增 |
| token 存储 | 未新增 |
| localStorage/sessionStorage | 未新增代码 |
| IdP SDK | 未新增 |
| secrets | 未新增 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |
| 构建产物 | 未提交 |

### 未解决问题

1. `REVERSE_PROXY` 和 `JWT` 两种生产认证模式仍缺部署 smoke 与回滚手册。
2. 真实企业 SSO/gateway 产品形态尚未绑定到具体部署拓扑。
3. JWT key rotation、JWKS 可用性监控和认证失败告警尚未实现。

### 是否建议关闭本阶段

建议关闭 AUTH-010。

关闭理由：前端 bearer 边界决策、拒绝浏览器 token 存储原则、生产 reverse proxy 路线、JWT adapter 使用场景、未来 direct bearer 前置条件、README 和阶段记录已完成；资料保护、空白检查和越界扫描通过。本阶段未删除文件，未修改前后端运行时代码，未新增 migration、token 存储、IdP SDK、secrets、PDF/OCR 全文、构建产物、ERP、BI、合并报表或阶段外功能。

### 下一阶段建议

下一阶段建议进入 `AUTH-011`：生产认证部署 smoke 与回滚手册。目标是为 `REVERSE_PROXY` 和 `JWT` 模式整理环境变量、启动前检查、HTTP smoke、审计检查、常见失败定位和回滚步骤；不提交 secrets，不访问真实外部 IdP。

## AUTH-011

阶段名称：生产认证部署 smoke 与回滚手册

记录日期：2026-05-09

### 阶段目标

为 `REVERSE_PROXY` 和 `JWT` 两条生产候选认证路线沉淀部署前检查、环境变量、HTTP smoke、审计核对、失败 reason 定位和回滚步骤。本阶段只写文档，不提交 secrets、raw token、`.env`、真实网关/IdP 配置，不访问真实外部 IdP，不修改前后端运行时代码，不新增 migration。

### 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `auth-006-deployment-secret-operations-runbook.md`、`auth-009-jwt-bearer-adapter.md`、`auth-010-frontend-bearer-boundary.md`、`ops-001-local-runbook.md`、当前 Git 状态 |
| 允许修改 | `docs/architecture/auth-011-auth-deployment-smoke-rollback.md`、`auth-006-deployment-secret-operations-runbook.md`、README、PROJECT_STEP_RECORD |
| 禁止修改 | 前后端运行时代码、migration、secrets、raw token、`.env`、真实外部服务配置、PDF 原文、OCR 全文、ERP 直连、BI 图表、合并报表 |
| 验证命令 | `git check-ignore`、`git diff --check`、`git status --short`、边界关键词扫描 |
| 授权状态 | 用户已完全授权全自动推进；删除文件仍需暂停，本阶段未删除文件 |

### 修改文件

| 文件 | 变更 |
| --- | --- |
| `docs/architecture/auth-011-auth-deployment-smoke-rollback.md` | 新增生产认证 smoke 与回滚手册 |
| `docs/architecture/auth-006-deployment-secret-operations-runbook.md` | 更新 JWT 状态和环境变量清单，避免旧的“future/fails closed”表述 |
| `docs/architecture/sec-006-trusted-principal-adapter.md` | 增加当前状态说明，标记 SEC-006 为历史阶段 |
| `docs/architecture/sec-007-remove-header-role-trust.md` | 更新风险说明，指向 AUTH-009 至 AUTH-011 当前认证状态 |
| `docs/architecture/sec-010-security-document-consistency.md` | 增加 AUTH-011 后的当前认证状态说明 |
| `README.md` | 更新 AUTH-011 文档入口和当前治理状态 |
| `PROJECT_STEP_RECORD.md` | 追加 AUTH-011 阶段记录 |

### 关键产出

1. 明确 `REVERSE_PROXY` 模式部署前置条件：后端不可被浏览器直连、网关认证、header stripping、可信 principal header、禁用 header role trust。
2. 明确 `JWT` 模式环境变量：issuer、audience、JWKS URI、username claim、clock skew、max token length、allowed algorithms。
3. 提供 reverse proxy 正向 smoke、direct-backend negative smoke、JWT 正向 smoke、JWT negative smoke 和审计检查命令模板。
4. 明确禁止提交 raw token、`.env`、private key、client secret、真实 gateway/IdP 配置。
5. 建立失败 reason triage 表：missing principal、missing bearer、invalid issuer/audience/signature、expired、unknown user、inactive user 等。
6. 建立回滚表：gateway route 暴露、header missing、JWT 配置漂移、key rotation outage、用户锁定、日志泄露等。
7. 明确生产不得回滚到 `DEV_HEADER`；`DEV_HEADER` 仍限本地开发和受控测试。
8. 补充 SEC-006、SEC-007、SEC-010 的当前状态说明，避免历史阶段文档继续表达 JWT/reverse proxy 仍为未实现占位。

### 测试与验证结果

| 命令 | 结果 |
| --- | --- |
| 文档阶段 | 未运行后端/前端完整测试；本阶段未修改运行时代码、依赖或 migration |
| `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | 通过；PDF、OCR、构建产物与依赖目录均被忽略 |
| `git diff --check` | 通过；仅提示 README、PROJECT_STEP_RECORD 与 AUTH 文档后续可能由 Git 触碰为 CRLF，无空白错误 |
| `git status --short` | 仅显示 AUTH-011 文档、AUTH-006 文档、README 和阶段记录 |
| 边界关键词扫描 | 仅命中文档中的 JWT/bearer/token/secrets/ERP/BI/合并报表禁用或 smoke 说明，以及既有代码中的授权/JWT 实现；未新增前后端代码、migration、PDF/OCR 处理、secrets、ERP、BI 或合并报表 |

### 失败项与修复记录

1. 本阶段未出现验证失败。
2. 修正 `auth-006-deployment-secret-operations-runbook.md` 中 JWT 仍为 future/fails closed 的历史表述，使其与 AUTH-009 后端 adapter 状态一致。
3. 补充 SEC-006、SEC-007、SEC-010 的当前状态说明，避免安全历史文档与当前认证主线冲突。

### 风险与限制

1. 本阶段不访问真实 IdP/JWKS，因此无法证明某个具体企业 IdP 配置可用。
2. 手册中的 PowerShell smoke 命令包含占位域名和占位 token 变量，实际部署必须由运维在环境变量和 secret store 中注入。
3. JWT key rotation、JWKS 可用性监控和告警仍未实现。
4. 生产网关 header stripping 能力必须由部署环境实际验证。

### 越界检查

| 项 | 结果 |
| --- | --- |
| 删除文件 | 未执行 |
| 前端运行时代码 | 未修改 |
| 后端运行时代码 | 未修改 |
| migration | 未新增 |
| secrets/raw token/.env | 未新增 |
| 外部 IdP 访问 | 未执行 |
| 真实 gateway/IdP 配置 | 未提交 |
| ERP 直连 | 未新增 |
| BI 图表 | 未新增 |
| 合并报表 | 未新增 |
| PDF 原文 | 未修改，未提交 |
| OCR 全文 | 未提交 |
| 构建产物 | 未提交 |

### 未解决问题

1. 真实部署拓扑、网关产品和 IdP 租户尚未接入。
2. JWT key rotation/JWKS 监控/认证失败告警尚未实现。
3. 端到端浏览器 smoke 自动化仍未实现。

### 是否建议关闭本阶段

建议关闭 AUTH-011。

关闭理由：生产认证 smoke 与回滚手册、AUTH-006 历史状态修正、README 和阶段记录已完成；资料保护、空白检查和越界扫描通过。本阶段未删除文件，未修改前后端运行时代码，未新增 migration、secrets、raw token、外部配置、PDF/OCR 全文、构建产物、ERP、BI、合并报表或阶段外功能。

### 下一阶段建议

下一阶段建议进入 `E2E-001`：端到端 smoke 测试基线设计与最小实现。目标是为核心 MVP 路径建立可重复的浏览器/API smoke 验证，但需先检查当前前端测试工具链，避免一次性引入过大测试框架。
