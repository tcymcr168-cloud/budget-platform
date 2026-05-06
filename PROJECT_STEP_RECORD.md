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
