
export interface InvoiceTemplateModel {
    id?: number;
    invoiceTemplateId?: number;
    state?: number;
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    supplierId?: number;
    invoiceGroup?: string;
    templateCode?: string;
    invoiceName?: string;
    description?: string;
    templatePath?: string;
    logeFile?: any;
    invoicetemplateFile?: any;
    invoiceFoFile?: any;
    exchangeInvoiceFoFile?: any;
    updateTemplate?: number;
    emailTemplateId?: number;
    listExcelTemplatePath?: string;
    listPdfTemplatePath?: string;
    listSignatureReason?: string;
    barcodeType?: string;
    logoFilePath?: string;
    usingHtmlLogo?: number;
    tenantId?: number;
    tenantBranchId?: number;
    usigJavasript?: number;
    javaScriptFilePath?: string;
    invoiceTypeId?: number;
    invoiceTypeName?: string;
    usingWateMark?: number;
    watermarkFilePath?: number;
    watermarkFile?: any;
    startDate?: Date;
    endDate?: Date;
    invoiceTemplatePrototypeId?: Number;
    imageFile?: any;
    taxPolicy?: string;
    discountPolicy?: string;
    discountDisplayType?: string;
    isMultiple?: number;
    listInfoUpdate?: string;
    status?: number;
}