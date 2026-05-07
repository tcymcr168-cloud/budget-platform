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
