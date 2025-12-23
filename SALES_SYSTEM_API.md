# 销售管理系统 API 文档

## 一、客户来访管理 (CustomerVisit)

### 接口列表

1. **查询来访列表**
   - `GET /api/customer-visit/list`
   - 参数：customerName, startDate, endDate, status
   - 返回：来访记录列表

2. **获取来访详情**
   - `GET /api/customer-visit/{id}`
   - 返回：来访记录详情

3. **保存来访记录**
   - `POST /api/customer-visit/save`
   - 请求体：CustomerVisit对象
   - 功能：新增或修改来访记录，选择客户后自动关联客户信息

4. **删除来访记录**
   - `DELETE /api/customer-visit/{id}`

5. **导出来访列表**
   - `GET /api/customer-visit/export`
   - 参数：customerName, startDate, endDate, status
   - 返回：Excel文件

### 实体字段
- customerSequence: 客户序号
- customerName: 客户名称
- visitDate: 日期
- status: 状态
- contactPerson: 联络员
- contactDepartment: 联络员所属部门
- visitMatter: 来访事宜
- remarks: 备注

---

## 二、销售机会管理 (ProjectOpportunity)

### 接口列表

1. **查询销售机会列表**
   - `GET /api/project-opportunity/list`
   - 参数：opportunityName, startDate, endDate, status, source, industry

2. **获取销售机会详情**
   - `GET /api/project-opportunity/{id}`

3. **保存销售机会**
   - `POST /api/project-opportunity/save`
   - 约束：已提交的销售机会不可修改

4. **删除销售机会**
   - `DELETE /api/project-opportunity/{id}`
   - 约束：已提交的销售机会不可删除

5. **提交销售机会**
   - `POST /api/project-opportunity/{id}/submit`
   - 功能：提交后流转至片区负责人处理

6. **传递片区**
   - `POST /api/project-opportunity/{id}/transfer-regions`
   - 请求体：{"regionIds": ["regionId1", "regionId2"]}
   - 功能：支持多片区共享

7. **分配员工**
   - `POST /api/project-opportunity/{id}/assign-employees`
   - 请求体：{"employeeIds": ["employeeId1", "employeeId2"]}
   - 功能：支持多员工共同跟踪

8. **关闭机会**
   - `POST /api/project-opportunity/{id}/close`
   - 请求体：{"closeReason": "关闭原因"}

9. **获取跟踪记录**
   - `GET /api/project-opportunity/{id}/trackings`

10. **保存跟踪记录**
    - `POST /api/project-opportunity/tracking/save`
    - 请求体：OpportunityTracking对象

11. **删除跟踪记录**
    - `DELETE /api/project-opportunity/tracking/{id}`

12. **获取关联片区**
    - `GET /api/project-opportunity/{id}/regions`

13. **获取关联员工**
    - `GET /api/project-opportunity/{id}/employees`

14. **获取关键人物**
    - `GET /api/project-opportunity/{id}/key-persons`

15. **保存关键人物**
    - `POST /api/project-opportunity/{id}/key-persons`
    - 请求体：OpportunityKeyPerson数组

16. **导出销售机会列表**
    - `GET /api/project-opportunity/export`

### 实体字段
- opportunityName: 机会名称
- opportunityCode: 机会编号
- projectName: 项目名称
- stage: 阶段
- customerId: 客户ID（可为空，支持临时客户）
- customerName: 客户名称
- budget: 预算
- industry: 行业
- regions: 片区（可多选）
- opportunityDate: 销售机会日期
- source: 线索来源
- status: 状态
- receiveStatus: 接收状态
- inventory: 存货
- isSubmitted: 是否已提交

---

## 三、销售库存查询 (Product)

### 接口列表

1. **库存查询**
   - `GET /api/product/inventory`
   - 参数：drawingNumber, name, isStagnant
   - 功能：排除已标记降库的产品

2. **库存查询导出**
   - `GET /api/product/inventory/export`

3. **降库产品查询**
   - `GET /api/product/reduced-stock`
   - 参数：caliber, motorPower, flow, head, filterMaterial, inletPressure, outletPressure
   - 功能：查询呆滞产品，排除已标记降库的产品

4. **降库产品导出**
   - `GET /api/product/reduced-stock/export`

5. **标记降库产品**
   - `POST /api/product/{id}/mark-reduced-stock`
   - 请求体：{"contractId": "合同ID"}
   - 功能：标记后其他销售人员不可重复查询

### 实体字段（库存相关）
- drawingNumber: 图号
- name: 名称
- material: 材料
- quantity: 数量
- isStagnant: 是否呆滞
- expectedDeliveryDate: 预计交货期
- storageAge: 库龄
- isReducedStock: 是否已标记降库
- contractId: 关联的销售合同ID

---

## 四、销售报价管理 (SalesQuotation)

### 接口列表

1. **查询报价单列表**
   - `GET /api/sales-quotation/list`
   - 参数：quotationName, customerName, startDate, endDate

2. **获取报价单详情**
   - `GET /api/sales-quotation/{id}`

3. **保存报价单**
   - `POST /api/sales-quotation/save`
   - 功能：关联销售机会后自动同步机会信息

4. **删除报价单**
   - `DELETE /api/sales-quotation/{id}`

5. **获取报价明细**
   - `GET /api/sales-quotation/{id}/items`

6. **保存报价明细**
   - `POST /api/sales-quotation/{id}/items`
   - 请求体：QuotationItem数组
   - 功能：自动计算总价

7. **获取打印数据**
   - `GET /api/sales-quotation/{id}/print`
   - 返回：报价单和明细数据（用于前端打印/导出PDF）

8. **导出报价单列表**
   - `GET /api/sales-quotation/export`

### 实体字段
- quotationCode: 报价编号
- quotationName: 报价名称
- quotationType: 报价类型
- opportunityId: 销售机会ID
- customerName: 报价客户
- projectName: 项目名称
- totalPrice: 总价
- taxRate: 税率
- competitorName: 竞争对手名称
- competitorQuotation: 竞争对手报价
- quotationDate: 报价日期

### 报价明细字段 (QuotationItem)
- productId: 产品ID
- productName: 产品/零件名称
- drawingNumber: 图号
- inventoryQuantity: 零件库存
- quantity: 数量
- isStagnant: 是否呆滞
- unitPrice: 报价（单价）
- totalPrice: 报价（总价）

---

## 五、销售降库管理

### 功能说明
- 查询呆滞库存产品（支持多条件筛选）
- 推荐可替代产品
- 标记降库产品，关联销售合同
- 已标记降库的产品，其他销售人员不可重复查询

### 接口
参考"销售库存查询"模块的降库相关接口

---

## 六、投标管理 (BiddingInfo)

### 接口列表

1. **查询投标信息列表**
   - `GET /api/bidding-info/list`
   - 参数：biddingName, biddingType, biddingStatus, customerName

2. **获取投标信息详情**
   - `GET /api/bidding-info/{id}`

3. **保存投标信息**
   - `POST /api/bidding-info/save`
   - 功能：关联销售机会后自动同步机会信息
   - 支持上传技术方案和报价单附件

4. **删除投标信息**
   - `DELETE /api/bidding-info/{id}`

5. **提交投标总结**
   - `POST /api/bidding-info/{id}/summary`
   - 请求体：{"biddingResult": "已中/未中/流标/废标", "biddingSummary": "总结内容", "attachment": "附件路径"}
   - 功能：
     - 废标或未中标时，销售机会状态自动改为"已关闭"
     - 已中标且录入合同后，销售机会状态自动改为"已关闭"（需在合同创建时处理）
     - 流标时，销售机会保留可重新跟踪

6. **导出投标信息列表**
   - `GET /api/bidding-info/export`

### 实体字段
- biddingCode: 投标编号
- biddingName: 投标名称
- biddingType: 投标类型
- opportunityId: 销售机会ID
- customerName: 投标客户
- projectName: 项目名称
- technicalSolution: 技术方案（附件路径）
- quotationFile: 报价单（附件路径）
- biddingStatus: 投标状态
- biddingResult: 投标结果
- biddingSummary: 投标总结
- attachment: 附件路径

---

## 数据约束说明

1. **销售机会提交后不可修改删除**
   - 已提交的销售机会（isSubmitted=true）不可修改和删除

2. **降库产品标记后不可重复查询**
   - 已标记降库的产品（isReducedStock=true）在库存查询和降库查询中会被排除
   - 仅可在对应合同产品明细中选择

3. **投标结果关联销售机会状态更新**
   - 废标或未中标：销售机会状态自动改为"已关闭"
   - 已中标且录入合同后：销售机会状态自动改为"已关闭"
   - 流标：销售机会保留可重新跟踪

4. **跨模块关联**
   - 销售机会关联客户信息、投标信息
   - 报价单关联销售机会及库存数据
   - 降库产品关联销售合同
   - 投标信息关联销售机会

---

## 前端开发建议

1. **客户来访管理页面**
   - 列表展示：客户序号、客户名称、所属区域、所属行业、公司地址、地区、日期、状态、联络员、联络员所属部门、来访事宜、备注
   - 支持按客户名称、日期等条件筛选
   - 新增时选择客户名称后，自动关联客户所属区域、行业等基础信息

2. **销售机会管理页面**
   - 登记页面：机会名称、项目名称、阶段、客户、预算、行业、片区（可多选）、销售机会日期、线索来源、备注、客户关键人物
   - 列表展示：销售机会日期、销售机会主题、存货、预算、线索来源、接收状态
   - 分配传递页面：支持传递片区、分配员工、关闭机会、查看信息

3. **销售库存查询页面**
   - 列表展示：图号、名称、材料、数量、是否呆滞、预计交货期
   - 支持条件筛选和导出

4. **销售报价管理页面**
   - 报价单编辑：关联销售机会、选择产品/零件、自动计算总价、填写竞争对手信息
   - 列表展示：报价编号、日期、项目名称、报价客户、报价总额、备注
   - 支持打印和导出PDF

5. **销售降库管理页面**
   - 查询参数：口径、电机功率、流量、扬程、过滤材质、入口压力、出口压力
   - 列表展示：图号、名称、材料、数量、库龄、是否呆滞
   - 支持标记降库并关联销售合同

6. **投标管理页面**
   - 新建投标：录入投标基础信息、上传技术方案及报价单附件
   - 列表展示：投标编号、投标名称、投标类型、投标客户、机会名称、机会编号、项目名称、技术方案、报价单、投标状态
   - 投标总结：录入投标结果及总结内容、上传相关附件

---

## 注意事项

1. 所有列表界面需支持"导出"功能
2. 销售报价管理模块支持报价单"打印""导出PDF"功能（前端实现）
3. 投标管理模块支持技术方案、报价单等附件上传并关联至投标记录
4. 界面按钮位置、名称需与文档附图一致
5. 下拉选择项需严格遵循文档指定内容（如投标类型、报价类型、税率、投标结果等）

