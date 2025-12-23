package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {
    
    @Column(name = "name", nullable = false, length = 200)
    private String name; // 名称
    
    @Column(name = "model", length = 100)
    private String model; // 型号
    
    @Column(name = "parameters", length = 1000)
    private String parameters; // 参数
    
    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price; // 价格
    
    @Column(name = "weight", length = 50)
    private String weight; // 重量
    
    @Column(name = "dimensions", length = 200)
    private String dimensions; // 尺寸
    
    @Column(name = "delivery_cycle", length = 100)
    private String deliveryCycle; // 交货周期
    
    @Column(name = "product_type", nullable = false, length = 50)
    private String productType; // 产品类型：SINGLE（单体设备）、SPARE（备品备件）、COMPLETE（设备成套）
    
    @Column(name = "drawing_number", length = 100)
    private String drawingNumber; // 图号
    
    @Column(name = "material", length = 200)
    private String material; // 材料
    
    @Column(name = "quantity", precision = 15, scale = 2)
    private java.math.BigDecimal quantity; // 数量
    
    @Column(name = "is_stagnant")
    private Boolean isStagnant; // 是否呆滞
    
    @Column(name = "expected_delivery_date")
    private java.time.LocalDate expectedDeliveryDate; // 预计交货期
    
    @Column(name = "storage_age", length = 50)
    private String storageAge; // 库龄
    
    @Column(name = "is_reduced_stock")
    private Boolean isReducedStock = false; // 是否已标记降库
    
    @Column(name = "contract_id", length = 36)
    private String contractId; // 关联的销售合同ID（降库产品）
    
    // 降库查询参数
    @Column(name = "caliber", length = 100)
    private String caliber; // 口径
    
    @Column(name = "motor_power", length = 100)
    private String motorPower; // 电机功率
    
    @Column(name = "flow", length = 100)
    private String flow; // 流量
    
    @Column(name = "head", length = 100)
    private String head; // 扬程
    
    @Column(name = "filter_material", length = 100)
    private String filterMaterial; // 过滤材质
    
    @Column(name = "inlet_pressure", length = 100)
    private String inletPressure; // 入口压力
    
    @Column(name = "outlet_pressure", length = 100)
    private String outletPressure; // 出口压力
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getDeliveryCycle() {
        return deliveryCycle;
    }

    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDrawingNumber() {
        return drawingNumber;
    }

    public void setDrawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public java.math.BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(java.math.BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsStagnant() {
        return isStagnant;
    }

    public void setIsStagnant(Boolean isStagnant) {
        this.isStagnant = isStagnant;
    }

    public java.time.LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(java.time.LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getStorageAge() {
        return storageAge;
    }

    public void setStorageAge(String storageAge) {
        this.storageAge = storageAge;
    }

    public Boolean getIsReducedStock() {
        return isReducedStock;
    }

    public void setIsReducedStock(Boolean isReducedStock) {
        this.isReducedStock = isReducedStock;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }

    public String getMotorPower() {
        return motorPower;
    }

    public void setMotorPower(String motorPower) {
        this.motorPower = motorPower;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getFilterMaterial() {
        return filterMaterial;
    }

    public void setFilterMaterial(String filterMaterial) {
        this.filterMaterial = filterMaterial;
    }

    public String getInletPressure() {
        return inletPressure;
    }

    public void setInletPressure(String inletPressure) {
        this.inletPressure = inletPressure;
    }

    public String getOutletPressure() {
        return outletPressure;
    }

    public void setOutletPressure(String outletPressure) {
        this.outletPressure = outletPressure;
    }
}

